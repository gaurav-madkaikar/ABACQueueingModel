import os, subprocess, shutil
import numpy as np
import socket
from threading import Thread
from threading import Lock
import sys
import pickle
import time
import json
import random
import aux_list as al
from collections import deque
import aux_list as al

# Generate test data
# import gen_test_data as gtd


# Global file pointers
ar_stats = open('experimental_data/access_req_stats.txt', 'w')

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

# Global Locks
access_request_lock = Lock()
aux_list_lock = Lock()
policy_lock = Lock()
curr_server_state_lock = Lock()

# Global start timer
global_start_timer = 0
NO_OF_VACATIONS = 1

# Helper Functions
# -- SEND AND RECEIVE DATA --
HEADERSIZE = 16
FORMAT = 'utf-8'

def sendMessage(conn, message):
    msg_send = b''
    if type(message) == dict:
        msg_send = pickle.dumps(message)
    else:
        msg_send = bytes(message, FORMAT)
    msg_send = bytes(f'{len(msg_send):16}', 'utf-8') + msg_send
    conn.sendall(msg_send)

# def receiveMessage(conn, _type='str'):
#     decoded_message = ''
#     decoded_obj = {}
#     recv_msg_len = int(conn.recv(4096).decode('utf-8'))
#     recv_msg = conn.recv(recv_msg_len)
#     if _type == 'dict':
#         decoded_obj = pickle.loads(recv_msg)
#         return decoded_obj
#     else:
#         decoded_message = recv_msg.decode('utf-8')
#         return decoded_message
def receiveMessage(conn, _type='str'):
    decoded_message = ''
    decoded_obj = {}

    recv_msg_len = int(conn.recv(16).decode('utf-8'))
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
    
# def statsReporter():
#     start_time = time.perf_counter()

#     # reportResult(access_request, resolution_type, ar_policy_time, ar_ACM_time, ar_queue_time, no_of_jobs)

#     end_time = time.perf_counter()



#     time.sleep(0.02)
#     pass
    
# Generate a representation of the Access Control Policy in the form of low-level rules
def generateStollerInput():
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
    # print('Stollers input done!')



# Resolve access request from policy
def resolveAccessRequestfromPolicy(access_request, policy, type_=1):
    # result = 0 implies access not granted, result = 1 implies access granted
    # global policy
    result = 0
    policy_ = {}
    with policy_lock:
        policy_ = policy.copy()
    # if type_ == 2:
    #     print(f"New policy policy_: {policy_}")

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

# Access Request 
def handle_client(conn, address):
    # Send attribute-value pairs to the client
    global sub_attr_val, obj_attr_val, policy

    sendMessage(conn, sub_attr_val)
    sendMessage(conn, obj_attr_val)
    sendMessage(conn, policy)

    ar_cnt = 0
    while True:
        # Receive access request from the client
        access_request = receiveMessage(conn, 'dict')
        ar_cnt += 1

        # print(f"Request No. {ar_cnt}: Received access request on server side:")
        with access_request_lock:
            # Add the access request to a queue
            access_request_queue.append([ar_cnt, access_request, time.perf_counter()])

            if NO_OF_VACATIONS == 5:
                # print('Client must be closed immediately!\nVACATIONS ARE DONE!!!!\n')
                break
        # if ar_cnt == 100:
        #     break
    print('Connection closed on server side!\n')

def accept_client():
    global sub_attr_val, obj_attr_val, sub_obj_pairs_not_taken, curr_server_state, global_start_timer
    try:
        while True:
            # Accept the connection from the client
            client, client_address = SERVER.accept()

            print("%s:%s has connected." % client_address)

            curr_server_state = 1
            global_start_timer = time.perf_counter()

            HANDLE_CLIENT = Thread(target=handle_client, args=(client, client_address), daemon=True)
            HANDLE_CLIENT.start()
            HANDLE_CLIENT.join()
    except KeyboardInterrupt:
        print('Keyboard interrupt caught')
        print('Exiting thread...')


def updateAuxList():
    global sub_obj_pairs_not_taken, sub_attr_val, obj_attr_val, aux_list, aux_list_lock, userbase, objectbase
    # Update the auxiliary list with random accesses
    # Introduce slightly larger delays

    # users = list(sub_attr_val.keys())
    # objects = list(obj_attr_val.keys())

    number_of_users = len(userbase)
    number_of_objects = len(objectbase)

    # while len(sub_obj_pairs_not_taken) != 0:
    while True:
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
            
        # Exponential delay (for now consider constant delay)
        mean_tw = 66.67
        tw = max(1, np.random.poisson(mean_tw, 1)[0]) * 1e-3
        time.sleep(tw)

def generateCombinedPolicy():
    # Combine policy P and auxiliary list to generate a new policy P' which is then passed to the ABAC mining algorithm
    global aux_list, policy, userbase, objectbase, user_attr, obj_attr
    with policy_lock:
        combined_policy = policy.copy()
    # userbase = {}
    # objectbase = {}
    # with open("database/policy/curr_policy.json") as db:
    #     combined_policy = json.load(db)
    # with open("database/userbase/userbase.json") as db:
    #     userbase = json.load(db)
    # with open("database/objectbase/objectbase.json") as db:
    #     objectbase = json.load(db)

    ori_no_of_rules = len(combined_policy)
    rule_ID = ori_no_of_rules + 1
    # file_ptr = open('database/auxiliary_list.txt', 'r')
    # temp_update_list = []
    # for line in file_ptr:
    #     line = line.strip(' \n')
    #     line = line[1:-1]
    #     # print(line)
    #     temp_update_list.append(line.split(', '))
    # temp_update_list = aux_list
    # temp_update_list = aux_list
    
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
            # print(f'2 Userbase: {userbase}\nUser: {usr}')
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
    print(f"Original: {ori_no_of_rules} | Combined: = {len(combined_policy)}")

    with open("database/policy/curr_policy.json", "w") as db:
        json.dump(combined_policy, db)
    with policy_lock:
        policy = combined_policy.copy()
    print("Combination Process Over!")

def resolveAccessRequestfromAuxList(access_request):
    global userbase, objectbase
    # Resolve access requests from the auxiliary list
    # temp_policy = {}
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
            # print(f'1 Userbase: {userbase}\nUser: {usr}')
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
    # print(f'Access Request resolved from the auxiliary list')

def reportResult(access_request, resolution_type, ar_policy_time, ar_ACM_time, ar_queue_time, no_of_jobs):
    global ar_stats
    # Report start time, end time, wait time, time involved in resolving access request, number of jobs in the access_request_queue and the status of the server, number of rules in the policy, number of updates in auxiliary list
    tot_resolution_time = ar_policy_time + ar_ACM_time
    if resolution_type == 1:
        res_type = "Policy"
    elif resolution_type == 2:
        res_type = "Aux List"
    else:
        res_type = "None"
    ar_stats.write(f"Job {access_request[0]}: Waiting time = {ar_queue_time} | Resolution time = {tot_resolution_time} | Resolution type = {res_type}\n")
    

# satisfied = 0

'''
def extractRefinedPolicy():
    global user_attr, obj_attr, policy
    blockOfRefinedCode = []
    flg = 0
    # Iterate through each line in the file
    with open('./database/policy/refined_policy.abac', 'r') as file:
        for line in file:
            # Process the current line (e.g., print it)

            line = line.strip(' \n')
            # print(line)
            if line == '':
                continue
            if line == 'OUTPUT RULES':
                flg = 1
                # print(line)
                continue
            if flg == 1:
                # print(line)
                if line.startswith('='):
                    break
                blockOfRefinedCode.append(line)
    # print(blockOfRefinedCode)

    modified_rules = {}
    no_of_rules = 0
    # modified_rules['sub'] = {}
    # modified_rules['obj'] = {}

    # modified_rules['op'] =
    for line in blockOfRefinedCode:
        # if re.match(r'^\d', line) is not None:
        if line.startswith('rule'):
            no_of_rules += 1
            rule_ID = 'rule_' + str(no_of_rules)
            modified_rules[rule_ID] = {}
            modified_rules[rule_ID]['sub'] = {}
            modified_rules[rule_ID]['obj'] = {}

            # print(line)
            line = line.strip(' \n')[:-1].strip(' \n')
            new_line = line.split('(')[1].strip(' \n')
            # new_line = line.strip()[:-1].strip().split('(')
            attributes_list = new_line.split(';')
            # print(attributes_list)
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
                        # print(x, end=' ')
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
                    # for x in attr_val:
                    #     x = x.strip(' \n')
                    #     temp_list = x.split('in')
                    #     temp_list[0] = temp_list[0].strip()
                    #     temp_list[1] = temp_list[1].strip()
                    #     modified_rules[rule_ID]['o'][temp_list[0]] = temp_list[1][1:-1]
                else:
                    continue
                # print()

            # Last endline
            # print("\n")

    print(f"\n\n------------------------- Modified ABAC Policy -------------------------\n")
    print(f"Refined number of rules = {no_of_rules}")
    with open("database/policy/curr_policy.json", "w") as db:
        json.dump(modified_rules, db)
    with policy_lock:
        policy = modified_rules
'''
first_vac_dur = 0
def resolveAccessRequest():
    global sub_attr_val, obj_attr_val, policy, curr_server_state, access_request_queue, access_request_lock, policy, curr_server_state, global_start_timer, ar_stats, NO_OF_VACATIONS, first_vac_dur
    satisfied = 0
    
    while True:
        with access_request_lock:
            condn_check = len(access_request_queue)
        # if curr_server_state == 1 or condn_check == 0:
        curr_time = time.perf_counter()
        # print(f'')
        if condn_check == 0 and curr_server_state == 1 and (curr_time - global_start_timer) > 6:
            
            print('Mining process starting... stay tuned!')
            if NO_OF_VACATIONS >= 2:
                ar_stats.write('Normal period {NO_OF_VACATIONS} ended!\n')
            ar_stats.write('\n----- VACATION Started -----\n')
            ar_stats.write(f'Start time: {time.perf_counter() - global_start_timer}\n')
            # Follow the Policy Mining Procedure
            t_start = time.perf_counter()

            # Generate combined policy P + L
            generateCombinedPolicy()

            # Generate the input file
            generateStollerInput()

            # print('Mining policy!!!')
            # Run the ABAC Mining Algorithm
            minePolicy()
            # print('Mining over!!')
            # Extract the refined policy
            extractRefinedPolicy()

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
            if NO_OF_VACATIONS == 5:
                print('Handling clients must be stopped!')
                ar_stats.close()
                break
            ar_stats.write(f'--- Normal Period {NO_OF_VACATIONS} starts... ---\n')
        else:
            # ar_stats.write('\n----- NORMAL PERIOD -----\n')
            # access_request = {request_ID, Access_request_object, start_time}
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
            result = resolveAccessRequestfromPolicy(access_request[1], policy_)
            t_end = time.perf_counter()

            ar_policy_time = t_end - t_start
            ar_ACM_time = 0

            resolution_type = 0
            if result == 1:
                # print("Access granted from policy!")
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
            
            # print("Access denied from auxliary list as well!")
            reportResult(access_request, resolution_type, ar_policy_time, ar_ACM_time, ar_queue_time, no_of_jobs)

# Perform the ABAC Mining Procedure
def minePolicy():
    # Set the PATH environment variable to include the location of Java
    # print('here!')
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
    with open('./database/policy/refined_policy.abac', 'r') as file:
        for line in file:
            # Process the current line (e.g., print it)
            line = line.strip(' \n')
            # print(line)
            if line == '':
                continue
            if line == 'OUTPUT RULES':
                flg = 1
                # print(line)
                continue
            if flg == 1:
                # print(line)
                if line.startswith('='):
                    break
                blockOfRefinedCode.append(line)
    # print(blockOfRefinedCode)
    # print('extractrefinedpolicy!!!!!')
    modified_rules = {}
    no_of_rules = 0
    
    for line in blockOfRefinedCode:
        # if re.match(r'^\d', line) is not None:
        if line.startswith('rule'):
            no_of_rules += 1
            rule_ID = 'rule_' + str(no_of_rules)
            modified_rules[rule_ID] = {}
            modified_rules[rule_ID]['sub'] = {}
            modified_rules[rule_ID]['obj'] = {}

            # print(line)
            line = line.strip(' \n')[:-1].strip(' \n')
            new_line = line.split('(')[1].strip(' \n')
            # new_line = line.strip()[:-1].strip().split('(')
            attributes_list = new_line.split(';')
            # print(attributes_list)
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
                        # print(x, end=' ')
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
                    # for x in attr_val:
                    #     x = x.strip(' \n')
                    #     temp_list = x.split('in')
                    #     temp_list[0] = temp_list[0].strip()
                    #     temp_list[1] = temp_list[1].strip()
                    #     modified_rules[rule_ID]['o'][temp_list[0]] = temp_list[1][1:-1]
                else:
                    continue
                # print()

            # Last endline
            # print("\n")

    # print(f"\n\n------------------------- Modified ABAC Policy -------------------------\n")
    with policy_lock:
        policy = modified_rules.copy()
    print(f"Refined number of rules = {no_of_rules}")
    with open("database/policy/refined_policy.json", "w") as db:
        json.dump(modified_rules, db)
    with open("database/policy/curr_policy.json", "w") as db:
        json.dump(modified_rules, db)

    

'''
# Running mode of the server depending on the curr_server_state bit
# 0 - Normal mode
# 1 - Vacation mode
def flipServerMode():
    global curr_server_state
    while True:
        # lambda_param = random.randint(1, 3)
        lambda_param = 10
        rand_time_period = max(1, np.random.exponential(
            lambda_param, 1)[0])
        # print("Current server state: ", end = '')
        with access_request_lock:
            if len(access_request_queue) == 0:
                # Take a vacation
                curr_server_state = 1
                
                
        if curr_server_state == 0:
            print("Normal Mode")
            print(f"Jobs in the queue: {len(access_request_queue)}")
        else:
            print("Vacation Mode")
            print(f"Jobs in the queue: {len(access_request_queue)}")

        curr_server_state = 1 - curr_server_state
        time.sleep(rand_time_period)
'''

def init():
    global sub_attr, obj_attr, sub_attr_val, obj_attr_val, sub_obj_pairs_not_taken,userbase, objectbase, policy
    with open('database/userbase/sub_attr.json', 'r') as db:
        sub_attr = json.load(db)
    with open('database/objectbase/obj_attr.json', 'r') as db:
        obj_attr = json.load(db)
    with open('database/userbase/sub_attr_val.json', 'r') as db:
        sub_attr_val = json.load(db)
    with open('database/objectbase/obj_attr_val.json', 'r') as db:
        obj_attr_val = json.load(db)
    with open('database/userbase/userbase.json') as db:
        userbase = json.load(db)
    with open('database/objectbase/objectbase.json') as db:
        objectbase = json.load(db)
    with open('database/policy/policy.json', 'r') as db:
        policy = json.load(db)

    # with open('database/policy/curr_policy.json', 'w') as db:
    #     json.dump(policy, db)

    # Make a copy of the original policy
    original_file_name = "database/policy/policy.json"
    copy_file_name = "database/policy/curr_policy.json"

    shutil.copyfile(original_file_name, copy_file_name)

    file_ptr = open('database/aux_list/sub_obj_pairs_not_taken.txt', 'r')
    for line in file_ptr.readlines():
        line = line.strip(' \n')
        if line == '':
            continue
        usr_obj_pair = line.split(', ')[:2]
        sub_obj_pairs_not_taken.append([usr_obj_pair[0][1:], usr_obj_pair[1]])
    
    # print(f"\nSubject object pairs not taken: {sub_obj_pairs_not_taken}\n")
    # print()
        
    # Pass initial ABAC Policy to the ABAC Mining algorithm
    # Generate the input file
    # generateStollerInput()

    # # Run the ABAC Mining Algorithm
    # minePolicy()

    # # Extract the refined policy
    # extractRefinedPolicy()
    

    print("Init process over !\n")

if __name__ == "__main__":
    # Initialize variable with test data
    init()

    print('+' * 45 + " Server Side " + '+' * 45)
    # Server Initialization
    # Generate test data
    # gen_test_data = gtd.GenTestData()
    # gtd.main()

    # Wait for access requests from the client
    ADDR = ('127.0.0.1', 8080)
    SERVER = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    # Reuse the port if already in use
    SERVER.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    SERVER.bind(ADDR)
    print(f"[ -- LISTENING -- ] Server is listening on {ADDR[0]}:{ADDR[1]}...")

    SERVER.listen(5)
    print("Waiting for a connection...")

    # while True:
    #     client, client_address = SERVER.accept()
    #     print("%s:%s has connected." % client_address)
    # Accept the connection from the client
    ACCEPT_THREAD = Thread(target=accept_client, daemon=True)

    # Start the thread
    ACCEPT_THREAD.start()

    print("Main hello world! This works as well!")
    # Spawn a thread for updating the auxiliary list
    UPDATE_AUX_LIST_THREAD = Thread(target=updateAuxList, daemon=True)
    UPDATE_AUX_LIST_THREAD.start()

    ACCESS_REQUEST_RESOLVER = Thread(target=resolveAccessRequest, daemon=True)
    ACCESS_REQUEST_RESOLVER.start()

    # STATS_REPORTER = Thread(target=statsReporter, daemon=True)
    # STATS_REPORTER.start()
    
    # Wait for the thread to finish
    ACCEPT_THREAD.join()
    UPDATE_AUX_LIST_THREAD.join()
    ACCESS_REQUEST_RESOLVER.join()


    # Close the server
    SERVER.close()
    print("------------- Server Closed ! -------------")
