import os, subprocess, shutil
from pathlib import Path
import numpy as np
import socket
from threading import Lock, Thread, Event
import sys
import pickle
import time
import json
import random
from collections import deque
from argparse import ArgumentParser
import logging
from enum import Enum
from poltree import NPolTree

# Generate test data
# import gen_test_data as gtd

CURRENT_DIR = Path(__file__).resolve().parent

BASE_DIR = Path(__file__).resolve().parent.parent.parent
SERVER_LOG = BASE_DIR / 'logs' / 'server.log'

DATABASE_DIR = CURRENT_DIR / "database"
ACCESSES_FILE = CURRENT_DIR / "experimental_data" / "access_req_stats.txt"

# logging.basicConfig(filename=SERVER_LOG, level=logging.DEBUG, datefmt='%Y-%m-%d %H:%M:%S')
SERVER_LOG.parent.mkdir(parents=True, exist_ok=True)
SERVER_LOG.touch()
logging.basicConfig(level=logging.DEBUG, format='[Server] %(levelname)s: %(message)s', filename=SERVER_LOG)
logging.info(f'----------------- Server started at {time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())} -----------------')

# Global file pointers
ar_stats = open(ACCESSES_FILE, 'w')

# Main server operations:
#   1. Generate test data (attribute/value pairs, ACM, original policy) (Independent, one-time operation)
#   2. Resolve access requests in the auxiliary list
#   3. Convert accesses in the auxiliary list into ABAC rules and run the ABAC Mining algorithm

# Global Variables
curr_server_state = 0
system_start_time = 0

# Access Request Queue
access_request_queue = deque()
user_attr = []
obj_attr = []
sub_attr_val = {}
obj_attr_val = {}
userbase = {}
objectbase = {}
sub_obj_pairs_not_taken = []
policy = {}

# --- Shared Variables ---

# Auxiliary list
aux_list = deque()
MAX_AUX_LIST = 10000
AL_UPDATE_RATE = 15

# Global Locks
access_request_lock = Lock()
aux_list_lock = Lock()
policy_lock = Lock()
curr_server_state_lock = Lock()

# Global start timer
global_start_timer = 0
NO_OF_VACATIONS = 1

# Change in vacation model
NO_OF_ACCESS_REQUESTS_SERVED = 0
MAX_AUX_LIST_LEN_PER_VACATION = 100
MAX_ACCESS_REQUESTS_PER_VACATION = 500
MAX_NO_OF_VACATIONS = 5

class VacationModel(Enum):
    ACCESS_QUEUE = 1
    ACCESS_SERVED = 2
    AUX_LIST = 3

CURRENT_VACATION_MODEL = VacationModel.ACCESS_QUEUE

class PolicyResolution(Enum):
    LINEAR = 1
    POLICY_TREE = 2
CURRENT_POLICY_RESOLUTION = PolicyResolution.POLICY_TREE
PolTree: NPolTree = None

# Helper Functions
# -- SEND AND RECEIVE DATA --
HEADERSIZE = 16
FORMAT = 'utf-8'

THREAD_EVENT: Event = None
CLIENT_THREAD_FIRST_EVENT: Event = None
RUNNING = True

def sendMessage(conn, message):
    msg_send = b''
    if type(message) == dict:
        msg_send = pickle.dumps(message)
    else:
        msg_send = bytes(message, FORMAT)
    msg_send = bytes(f'{len(msg_send):16}', 'utf-8') + msg_send
    conn.sendall(msg_send)


def receiveMessage(conn, _type='str'):
    decoded_message = ''
    decoded_obj = {}

    recv_msg_len = conn.recv(16).decode('utf-8')
    if recv_msg_len == '':
        # Handle case where connection is closed
        return ''
    recv_msg_len = int(recv_msg_len)
    recv_msg = b''
    
    while len(recv_msg) < recv_msg_len:
        chunk = conn.recv(recv_msg_len - len(recv_msg))
        if not chunk:
            # Handle case where connection is closed
            break
        recv_msg += chunk

    if _type == 'dict':
        decoded_obj = pickle.loads(recv_msg)
        return decoded_obj
    else:
        decoded_message = recv_msg.decode('utf-8')
        return decoded_message
    
def generateStollerInput():
    """ Generate a representation of the Access Control Policy in the form of low-level rules"""
    global policy, userbase, objectbase
    with policy_lock:
        policy_ = policy.copy()

    # Write into an output .abac file in the ABACMining/ folder
    file_ptr = open("../../ABACMining/ori_policy.abac", "w")

    file_ptr.write("# User Attribute Data\n")
    for user_id in userbase:
        write_str = "userAttrib(" + user_id + ", "
        for attr in userbase[user_id]:
            if attr == "uid":
                continue
            write_str += attr + "=" + userbase[user_id][attr] + ", "
        write_str = write_str[:-2]
        write_str += ")\n"
        file_ptr.write(write_str)

    file_ptr.write("\n")

    file_ptr.write("# Resource Attribute Data\n")
    for obj_id in objectbase:
        write_str = "resourceAttrib(" + obj_id + ", "
        for attr in objectbase[obj_id]:
            if attr == "rid":
                continue
            write_str += attr + "=" + objectbase[obj_id][attr] + ", "
        write_str = write_str[:-2]
        write_str += ")\n"
        file_ptr.write(write_str)

    file_ptr.write("\n")

    file_ptr.write("# Low-level ABAC Rules\n")
    
    for rule_id in policy_:
        write_str = "rule("
        sub_tmp = policy_[rule_id]["sub"]
        obj_tmp = policy_[rule_id]["obj"]
        operation = policy_[rule_id]["op"]
        flg1 = flg2 = False

        for sub_keys in sub_tmp:
            if sub_tmp[sub_keys] == ['*']:
                continue
            else:
                flg1 = True
                write_str += sub_keys + " in {"
                for val in sub_tmp[sub_keys]:
                    write_str += val + " "
                write_str = write_str[:-1]
                write_str += "}, "
        if flg1:
            write_str = write_str[:-2]
        write_str += "; "
        for obj_keys in obj_tmp:
            if obj_tmp[obj_keys] == ['*']:
                continue
            else:
                flg2 = True
                write_str += obj_keys + " in {"
                for val in obj_tmp[obj_keys]:
                    write_str += val + " "
                write_str = write_str[:-1]
                write_str += "}, "
        if flg2:
            write_str = write_str[:-2]
        write_str += "; "

        write_str += "{" + operation + "}; "
        write_str += ")\n"
        file_ptr.write(write_str)

    file_ptr.close()


# TODO: Implement PolTree
def resolveAccessRequestfromPolicy(access_request, policy, type_=1):
    """ Resolve access request from policy"""
    # result = 0 implies access not granted, result = 1 implies access granted
    result = 0
    policy_ = {}
    with policy_lock:
        policy_ = policy.copy()

    # Process the result
    for i in range(len(policy_)):
        key = "rule_" + str(i + 1)
        rule = policy_.get(key)
        sub_attr_check = 1
        for attr in rule["sub"]:
            if rule["sub"][attr] == ['*']:
                continue
            curr_sub_attr_check = 0
            for value in access_request["sub"][attr]:
                if value in rule["sub"][attr]:
                    curr_sub_attr_check = 1
                    break
            if curr_sub_attr_check == 0:
                sub_attr_check = 0
                break
        if sub_attr_check == 0:
            continue
        obj_attr_check = 1
        for attr in rule["obj"]:
            if rule["obj"][attr] == ['*']:
                continue
            curr_obj_attr_check = 0

            for value in access_request["obj"][attr]:
                if value in rule["obj"][attr]:
                    curr_obj_attr_check = 1
                    break
            if curr_obj_attr_check == 0:
                obj_attr_check = 0
                break
        if obj_attr_check == 0:
            continue
        else:
            result = 1
            break
    return result

def resolveAccessRequestfromPolicyTree(access_request):
    """ Resolve access request from policy tree"""
    global PolTree
    result = PolTree.resolve(access_request)
    return result

def handle_client(conn, address):
    """ Access Request """
    global sub_attr_val, obj_attr_val, policy, THREAD_EVENT, RUNNING, CURRENT_VACATION_MODEL, VacationModel, curr_server_state
    global MAX_NO_OF_VACATIONS, NO_OF_VACATIONS, CLIENT_THREAD_FIRST_EVENT, global_start_timer

    # Send attribute-value pairs to the client
    sendMessage(conn, sub_attr_val)
    sendMessage(conn, obj_attr_val)
    sendMessage(conn, policy)
    
    global_start_timer = time.perf_counter()
    CLIENT_THREAD_FIRST_EVENT.set()

    ar_cnt = 0
    while True:
        # Receive access request from the client
        access_request = receiveMessage(conn, 'dict')
        if access_request == '':
            break
        ar_cnt += 1

        # print(f"Request No. {ar_cnt}: Received access request on server side:")
        with access_request_lock:
            # Add the access request to a queue
            access_request_queue.append([ar_cnt, access_request, time.perf_counter()])

            if NO_OF_VACATIONS == MAX_NO_OF_VACATIONS:
                break
    
    # Close the connection
    conn.close()
    logging.info('Connection closed on server side!\n')
    RUNNING = False
    THREAD_EVENT.set()

def accept_client():
    global sub_attr_val, obj_attr_val, sub_obj_pairs_not_taken, curr_server_state, global_start_timer, RUNNING
    global CURRENT_VACATION_MODEL, VacationModel, CLIENT_THREAD_FIRST_EVENT, SERVER
    try:
        while RUNNING:
            # Accept the connection from the client
            client, client_address = SERVER.accept()

            logging.info("%s:%s has connected." % client_address)

            HANDLE_CLIENT = Thread(target=handle_client, args=(client, client_address), daemon=True)
            HANDLE_CLIENT.start()
            HANDLE_CLIENT.join()
    except KeyboardInterrupt:
        logging.debug('Keyboard interrupt caught')
        logging.info('Exiting thread...')
    finally:
        logging.info('Done accepting clients')


def updateAuxList():
    """ Update the auxiliary list with random accesses
        Introduce slightly larger delays"""
    global sub_obj_pairs_not_taken, sub_attr_val, obj_attr_val, aux_list, aux_list_lock, userbase, objectbase, RUNNING, curr_server_state, AL_UPDATE_RATE, CURRENT_VACATION_MODEL, VacationModel

    number_of_users = len(userbase)
    number_of_objects = len(objectbase)

    while RUNNING:
        add_choice = random.randint(0, 1)
        if add_choice == 0:
            with aux_list_lock:
                if len(sub_obj_pairs_not_taken) == 0:
                    continue
                aux_list.append([sub_obj_pairs_not_taken[0][0], sub_obj_pairs_not_taken[0][1], 'read'])
                sub_obj_pairs_not_taken.pop(0)
        else:
            with aux_list_lock:
                aux_list.append(['sub_'+str(random.randint(1, number_of_users)), 'obj_'+str(random.randint(1, number_of_objects)), 'read'])
        if CURRENT_VACATION_MODEL == VacationModel.AUX_LIST:
            curr_server_state = 1

        # Exponential delay (for now consider constant delay)
        mean_tw = 1000 / AL_UPDATE_RATE
        tw = max(1, np.random.poisson(mean_tw, 1)[0]) * 1e-3
        time.sleep(tw)
    
    logging.info('Stopped auxiliary list updates')

def generateCombinedPolicy():
    """Combine policy P and auxiliary list to generate a new policy P' which is then passed to the ABAC mining algorithm"""
    global aux_list, policy, userbase, objectbase, user_attr, obj_attr
    with policy_lock:
        combined_policy = policy.copy()
    
    ori_no_of_rules = len(combined_policy)
    rule_ID = ori_no_of_rules + 1
    
    with aux_list_lock:
        while len(aux_list) != 0:
            temp_access = aux_list.popleft()
            usr = temp_access[0]
            obj = temp_access[1]
            op = temp_access[2]

            rule_key = "rule_" + str(rule_ID)
            rule_ID += 1
            combined_policy[rule_key] = {}
            combined_policy[rule_key]["sub"] = {}  
            combined_policy[rule_key]["obj"] = {}
            combined_policy[rule_key]["op"] = op
            
            # Add subject attributes
            for attr in userbase[usr]:
                if attr == "uid":
                    combined_policy[rule_key]["sub"][attr] = ["*"]
                    continue
                combined_policy[rule_key]["sub"][attr] = [userbase[usr][attr]]
            
            # Add object attributes
            for attr in objectbase[obj]:
                if attr == "rid":
                    combined_policy[rule_key]["obj"][attr] = ["*"]
                    continue
                combined_policy[rule_key]["obj"][attr] = [objectbase[obj][attr]]
    logging.debug(f"Original: {ori_no_of_rules} | Combined: = {len(combined_policy)}")

    with open(DATABASE_DIR / "policy" / "curr_policy.json", "w") as db:
        json.dump(combined_policy, db)
    with policy_lock:
        policy = combined_policy.copy()
    logging.debug("Combination Process Over!")

def resolveAccessRequestfromAuxList(access_request):
    """Resolve access requests from the auxiliary list"""
    global userbase, objectbase
    with aux_list_lock:
        for i in range(len(aux_list)):
            temp_policy = {}
            temp_access = aux_list[i]
            usr = temp_access[0]
            obj = temp_access[1]
            op = temp_access[2]

            rule_key = "rule_1"
            temp_policy[rule_key] = {}
            temp_policy[rule_key]["sub"] = {}  
            temp_policy[rule_key]["obj"] = {}
            temp_policy[rule_key]["op"] = op

            # Add subject attributes
            for attr in userbase[usr]:
                if attr == "uid":
                    temp_policy[rule_key]["sub"][attr] = ["*"]
                    continue
                temp_policy[rule_key]["sub"][attr] = [userbase[usr][attr]]
            
            # Add object attributes
            for attr in objectbase[obj]:
                if attr == "rid":
                    temp_policy[rule_key]["obj"][attr] = ["*"]
                    continue
                temp_policy[rule_key]["obj"][attr] = [objectbase[obj][attr]]
            result = resolveAccessRequestfromPolicy(access_request, temp_policy, 2)
            if result == 1:
                return 1
    return 0

def reportResult(access_request, resolution_type, ar_policy_time, ar_ACM_time, ar_queue_time, no_of_jobs):
    """Report stats
        
        stats: start time, end time, wait time, time involved in resolving access request, number of jobs in the access_request_queue and the status of the server, number of rules in the policy, number of updates in auxiliary list
    """
    global ar_stats
    tot_resolution_time = ar_policy_time + ar_ACM_time
    if resolution_type == 1:
        res_type = "Policy"
    elif resolution_type == 2:
        res_type = "Aux List"
    else:
        res_type = "None"
    ar_stats.write(f"Job {access_request[0]}: Waiting time = {ar_queue_time} | Resolution time = {tot_resolution_time} | Resolution type = {res_type}\n")
    
first_vac_dur = 0
def resolveAccessRequest():
    global sub_attr_val, obj_attr_val, policy, curr_server_state, access_request_queue, access_request_lock, policy, global_start_timer, ar_stats, NO_OF_VACATIONS
    global first_vac_dur, NO_OF_ACCESS_REQUESTS_SERVED, MAX_ACCESS_REQUESTS_PER_VACATION, CLIENT_THREAD_FIRST_EVENT, RUNNING, CURRENT_VACATION_MODEL, MAX_NO_OF_VACATIONS, VacationModel
    satisfied = 0
    curr_time = None
    CLIENT_THREAD_FIRST_EVENT.wait()
    while RUNNING:
        with access_request_lock:
            match CURRENT_VACATION_MODEL:
                case VacationModel.ACCESS_QUEUE:
                    condn_check = len(access_request_queue)==0
                case VacationModel.ACCESS_SERVED:
                    condn_check = NO_OF_ACCESS_REQUESTS_SERVED >= MAX_ACCESS_REQUESTS_PER_VACATION
                case VacationModel.AUX_LIST:
                    condn_check = len(aux_list) >= MAX_AUX_LIST_LEN_PER_VACATION
                case _:
                    condn_check = len(access_request_queue)==0
        
        curr_time = time.perf_counter()

        if condn_check == 1 and curr_server_state == 1 and (CURRENT_VACATION_MODEL != VacationModel.ACCESS_QUEUE or (curr_time - global_start_timer) >= 3):
            logging.debug(f'Time elapsed: {curr_time - global_start_timer}')
            logging.debug(f'Number of access requests served: {NO_OF_ACCESS_REQUESTS_SERVED}')
            logging.info('Mining process starting... stay tuned!')
            if NO_OF_VACATIONS >= 2:
                ar_stats.write(f'Normal period {NO_OF_VACATIONS} ended!\n')
            ar_stats.write('\n----- VACATION Started -----\n')
            ar_stats.write(f'Start time: {time.perf_counter() - global_start_timer}\n')
            # Follow the Policy Mining Procedure
            t_start = time.perf_counter()

            # Generate combined policy P + L
            generateCombinedPolicy()

            # Generate the input file
            generateStollerInput()

            # Run the ABAC Mining Algorithm
            minePolicy()
            
            # Extract the refined policy
            extractRefinedPolicy()
            
            # Update the policy tree
            if CURRENT_POLICY_RESOLUTION == PolicyResolution.POLICY_TREE:
                PolTree.update_policy(policy)

            t_end = time.perf_counter()
            vac_dur = t_end - t_start
            ar_stats.write(f'Duration: {vac_dur}\n')
            if NO_OF_VACATIONS == 1:
                first_vac_dur = time.perf_counter()
            NO_OF_VACATIONS += 1
            with access_request_lock:
                no_of_jobs = len(access_request_queue)
            ar_stats.write(f'Jobs in the system: {no_of_jobs}\n')
            ar_stats.write('------ VACATION ENDED ------\n\n')
            if NO_OF_VACATIONS == MAX_NO_OF_VACATIONS:
                logging.info('Handling clients must be stopped!')
                ar_stats.close()
                break
            ar_stats.write(f'--- Normal Period {NO_OF_VACATIONS} starts... ---\n')
            NO_OF_ACCESS_REQUESTS_SERVED = 0
            curr_server_state = 0
        else:
            ar_ACM_time = 0
            ar_policy_time = 0
            ar_queue_time = 0
            no_of_jobs = 0

            access_request = {}
            with access_request_lock:
                if len(access_request_queue) == 0:
                    continue
                else:
                    access_request = access_request_queue.popleft()
                    no_of_jobs = len(access_request_queue)
            
            t_start = time.perf_counter()
            with policy_lock:
                policy_ = policy
            result = None
            match CURRENT_POLICY_RESOLUTION:
                case PolicyResolution.LINEAR:
                    result = resolveAccessRequestfromPolicy(access_request[1], policy_)
                case PolicyResolution.POLICY_TREE:
                    result = resolveAccessRequestfromPolicyTree(access_request[1])
            t_end = time.perf_counter()

            ar_policy_time = t_end - t_start
            ar_ACM_time = 0

            resolution_type = 0
            if result == 1:
                resolution_type = 1
                satisfied += 1
                ar_queue_time = time.perf_counter() - access_request[2]
            else:
                t_start = time.perf_counter()
                result = resolveAccessRequestfromAuxList(access_request[1])
                t_end = time.perf_counter()
                ar_ACM_time = t_end - t_start
                if result == 1:
                    resolution_type = 2
                    satisfied += 1
                    ar_queue_time = time.perf_counter() - access_request[2]
                else:
                    resolution_type = 3
                    ar_queue_time = time.perf_counter() - access_request[2]
            
            reportResult(access_request, resolution_type, ar_policy_time, ar_ACM_time, ar_queue_time, no_of_jobs)
            NO_OF_ACCESS_REQUESTS_SERVED += 1
            if CURRENT_VACATION_MODEL in [VacationModel.ACCESS_QUEUE, VacationModel.ACCESS_SERVED]:
                curr_server_state = 1

    logging.info('Stopped access request resolution')

# Perform the ABAC Mining Procedure
def minePolicy():
    # Set the PATH environment variable to include the location of Java
    os.environ['PATH'] = 'C:/Program Files/Java/jdk-15.0.1/bin' + os.environ['PATH']
    bash_command = "./bash_script.sh"
    # Run the bash script
    subprocess.run(["bash", bash_command])
    return

def extractRefinedPolicy():
    global user_attr, obj_attr, policy
    blockOfRefinedCode = []
    flg = 0
    # Iterate through each line in the file
    with open(DATABASE_DIR / 'policy' / 'refined_policy.abac', 'r') as file:
        for line in file:
            line = line.strip(' \n')
            if line == '':
                continue
            if line == 'OUTPUT RULES':
                flg = 1
                continue
            if flg == 1:
                if line.startswith('='):
                    break
                blockOfRefinedCode.append(line)
    modified_rules = {}
    no_of_rules = 0
    
    for line in blockOfRefinedCode:
        if line.startswith('rule'):
            no_of_rules += 1
            rule_ID = 'rule_' + str(no_of_rules)
            modified_rules[rule_ID] = {}
            modified_rules[rule_ID]['sub'] = {}
            modified_rules[rule_ID]['obj'] = {}

            line = line.strip(' \n')[:-1].strip(' \n')
            new_line = line.split('(')[1].strip(' \n')
            attributes_list = new_line.split(';')
            encountered_sub_attr = []
            encountered_obj_attr = []

            for i in range(len(attributes_list)):
                ind_attr_val = attributes_list[i].split(',')
                if i == 0:
                    # Subject attributes
                    for x in ind_attr_val:
                        x = x.strip(' \n')
                        if x == '':
                            continue
                        temp_list = x.split(' in ')
                        possible_attr = temp_list[0].strip(' \n')
                        encountered_sub_attr.append(possible_attr)

                        temp_list[1] = temp_list[1].strip(
                            ' \n')[1:-1].strip(' \n')
                        possible_attr_values = temp_list[1].split(' ')
                        modified_rules[rule_ID]['sub'][possible_attr] = [
                            val for val in possible_attr_values if val != '']

                    for attr in user_attr:
                        if attr not in encountered_sub_attr:
                            modified_rules[rule_ID]['sub'][attr] = ['*']
                    if "uid" not in encountered_sub_attr:
                        modified_rules[rule_ID]['sub']['uid'] = ['*']
                elif i == 1:
                    # Object attributes
                    for x in ind_attr_val:
                        x = x.strip(' \n')
                        if x == '':
                            continue
                        temp_list = x.split(' in ')
                        possible_attr = temp_list[0].strip(' \n')
                        encountered_obj_attr.append(possible_attr)

                        temp_list[1] = temp_list[1].strip(
                            ' \n')[1:-1].strip(' \n')
                        possible_attr_values = temp_list[1].split(' ')
                        modified_rules[rule_ID]['obj'][possible_attr] = [
                            val for val in possible_attr_values if val != '']

                    for attr in obj_attr:
                        if attr not in encountered_obj_attr:
                            modified_rules[rule_ID]['obj'][attr] = ['*']
                    if "rid" not in encountered_obj_attr:
                        modified_rules[rule_ID]['obj']["rid"] = ['*']
                elif i == 2:
                    # Allowed operation
                    modified_rules[rule_ID]['op'] = ind_attr_val[0].strip(' \n')[
                        1:-1].strip(' \n')
                else:
                    continue

    with policy_lock:
        policy = modified_rules.copy()
    logging.debug(f"Refined number of rules = {no_of_rules}")
    with open(DATABASE_DIR / "policy" / "refined_policy.json", "w") as db:
        json.dump(modified_rules, db)
    with open(DATABASE_DIR / "policy" / "curr_policy.json", "w") as db:
        json.dump(modified_rules, db)


def init():
    global sub_attr, obj_attr, sub_attr_val, obj_attr_val, sub_obj_pairs_not_taken,userbase, objectbase, policy
    with open(DATABASE_DIR / "userbase" / "sub_attr.json", 'r') as db:
        sub_attr = json.load(db)
    with open(DATABASE_DIR / "objectbase" / "obj_attr.json", 'r') as db:
        obj_attr = json.load(db)
    with open(DATABASE_DIR / "userbase" / "sub_attr_val.json", 'r') as db:
        sub_attr_val = json.load(db)
    with open(DATABASE_DIR / "objectbase" / "obj_attr_val.json", 'r') as db:
        obj_attr_val = json.load(db)
    with open(DATABASE_DIR / "userbase" / "userbase.json") as db:
        userbase = json.load(db)
    with open(DATABASE_DIR / "objectbase" / "objectbase.json") as db:
        objectbase = json.load(db)
    with open(DATABASE_DIR / "policy" / "policy.json", 'r') as db:
        policy = json.load(db)


    # Make a copy of the original policy
    original_file_name = DATABASE_DIR / "policy" / "policy.json"
    copy_file_name = DATABASE_DIR / "policy" / "curr_policy.json"

    shutil.copyfile(original_file_name, copy_file_name)

    file_ptr = open(DATABASE_DIR / "aux_list" / "sub_obj_pairs_not_taken.txt", 'r')
    for line in file_ptr.readlines():
        line = line.strip(' \n')
        if line == '':
            continue
        usr_obj_pair = line.split(', ')[:2]
        sub_obj_pairs_not_taken.append([usr_obj_pair[0][1:], usr_obj_pair[1]])

    logging.debug("Init process over !\n")

SERVER = None
def main():
    global sub_attr_val, obj_attr_val, policy, userbase, objectbase, SERVER, RUNNING, THREAD_EVENT, CLIENT_THREAD_FIRST_EVENT
    # Initialize variable with test data
    init()

    logging.info('+' * 45 + " Server Side " + '+' * 45)
    
    # Wait for access requests from the client
    ADDR = ('127.0.0.1', 8080)
    SERVER = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    
    # Reuse the port if already in use
    SERVER.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    SERVER.bind(ADDR)
    logging.info(f"[ -- LISTENING -- ] Server is listening on {ADDR[0]}:{ADDR[1]}...")
    logging.info(f"Vacation Model: {CURRENT_VACATION_MODEL.name}")
    logging.info(f"Max Aux List Length per Vacation: {MAX_AUX_LIST_LEN_PER_VACATION}")
    logging.info(f"Max Access Requests per Vacation: {MAX_ACCESS_REQUESTS_PER_VACATION}")
    logging.info(f"Max Number of Vacations: {MAX_NO_OF_VACATIONS}")
    logging.info(f"Auxiliary List Update Rate: {AL_UPDATE_RATE}")
    logging.info(f"Policy Resolution: {CURRENT_POLICY_RESOLUTION.name}")
    
    logging.info("Building the policy tree...")
    global PolTree
    build_time = time.perf_counter()
    PolTree = NPolTree(policy)
    logging.info(f"Policy tree built in {time.perf_counter() - build_time} seconds")

    SERVER.listen(5)
    logging.info("Waiting for a connection...")
    
    THREAD_EVENT = Event()
    CLIENT_THREAD_FIRST_EVENT = Event()

    # Accept the connection from the client
    ACCEPT_THREAD = Thread(target=accept_client, daemon=True)

    # Start the thread
    ACCEPT_THREAD.start()

    logging.debug("Main hello world! This works as well!")
    # Spawn a thread for updating the auxiliary list
    UPDATE_AUX_LIST_THREAD = Thread(target=updateAuxList, daemon=True)
    UPDATE_AUX_LIST_THREAD.start()

    ACCESS_REQUEST_RESOLVER = Thread(target=resolveAccessRequest, daemon=True)
    ACCESS_REQUEST_RESOLVER.start()
    
    # Wait for the thread to finish
    ACCESS_REQUEST_RESOLVER.join()
    THREAD_EVENT.wait()
    RUNNING = False
    ACCEPT_THREAD.join()
    UPDATE_AUX_LIST_THREAD.join()

    # Close the server
    SERVER.close()
    logging.info("------------- Server Closed ! -------------")

if __name__ == "__main__":
    argparser = ArgumentParser()
    argparser.add_argument('-a', '--al_update_rate', type=int, help='Auxiliary list update rate')
    argparser.add_argument('-mal', '--max_aux_list_len', type=int, default=100, help='Maximum length of the auxiliary list per vacation')
    argparser.add_argument('-mar', '--max_access_requests', type=int, default=500, help='Maximum number of access requests per vacation')
    argparser.add_argument('-mv', '--max_no_of_vacations', type=int, default=5, help='Maximum number of vacations')
    argparser.add_argument('-pr', '--policy_resolution', type=int, default=2, help="""Policy resolution mode:
    1: Linear Search
    2: Policy Tree Based
    """)
    argparser.add_argument('-v', '--vacation_model', type=int, default=3, help="""Vacation model:
    1: Access Queue Empty
    2: Fixed Number of Access Requests Served
    3: Aux List Full
    """)
    
    args = argparser.parse_args()
    
    if args.al_update_rate:
        AL_UPDATE_RATE = args.al_update_rate
    if args.vacation_model:
        CURRENT_VACATION_MODEL = VacationModel(args.vacation_model)
    if args.max_aux_list_len:
        MAX_AUX_LIST_LEN_PER_VACATION = args.max_aux_list_len
    if args.max_access_requests:
        MAX_ACCESS_REQUESTS_PER_VACATION = args.max_access_requests
    if args.max_no_of_vacations:
        MAX_NO_OF_VACATIONS = args.max_no_of_vacations
    
    os.chdir(CURRENT_DIR)
    logging.info(f'Current directory: {os.getcwd()}')
    try:
        main()
    except KeyboardInterrupt:
        logging.info('Closing the server...')
        SERVER.close()
        logging.info('------------- Server Closed ! -------------')
        sys.exit(0)
        
