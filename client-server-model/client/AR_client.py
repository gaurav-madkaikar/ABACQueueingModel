import sys
import json
import socket
import select
from threading import Thread
import numpy as np
import pickle
import os
import random
from argparse import ArgumentParser
from threading import Thread
from pathlib import Path
import logging
import time

HEADERSIZE = 16
FORMAT = 'utf-8'

AR_ARRIVAL_RATE = 80

CURRENT_DIR = Path(__file__).resolve().parent

BASE_DIR = Path(__file__).resolve().parent.parent.parent
CLIENT_LOG = BASE_DIR / 'logs' / 'client.log'
CLIENT_LOG.parent.mkdir(parents=True, exist_ok=True)
CLIENT_LOG.touch()

LOCALBASE = CURRENT_DIR / 'localbase'

logging.basicConfig(level=logging.DEBUG, format='[Client] %(levelname)s: %(message)s', filename=CLIENT_LOG)
logging.info(f'----------------- Client started at {time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())} -----------------')


def sendMessage(conn, message):
    msg_send = b''
    if type(message) == dict:
        msg_send = pickle.dumps(message)
    else:
        msg_send = bytes(message, 'utf-8')

    msg_send = bytes(f'{len(msg_send):16}', 'utf-8') + msg_send
    conn.sendall(msg_send)

def receiveMessage(conn, _type='str'):
    decoded_message = ''
    decoded_obj = {}

    recv_msg_len = conn.recv(16).decode('utf-8')
    if recv_msg_len == '':
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

def resolveAR(access_request, policy, id):
    result = 0 # implies access not granted, result = 1 implies access granted

    # Process the result
    for i in range(len(policy)):
        key = "rule_" + str(i + 1)
        rule = policy.get(key)
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

CLIENT = None
def main():
    logging.info("---------------------------------- Client Side ----------------------------------")

    # Client Initialization
    
    # Get local machine name
    host = '127.0.0.1'
    port = 8080
    ADDR = (host, port)

    # Create a socket object
    while True:
        try:
            CLIENT = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
            CLIENT.connect(ADDR)
            break
        except ConnectionRefusedError:
            time.sleep(1e-1)
            logging.error("Server not available. Retrying...")
            if CLIENT: CLIENT.close()
            continue
    
    logging.info("Server Connection successful!")

    user_attr_val = receiveMessage(CLIENT, _type='dict')
    logging.debug(f'user attribute-values: {user_attr_val}')
    
    obj_attr_val = receiveMessage(CLIENT, _type='dict')
    logging.info(f'object attribute-values: {obj_attr_val}')

    policy = receiveMessage(CLIENT, _type='dict')
    logging.debug(f"Received policy succesfully!")
    tot_len_of_policy = len(policy)

    with open(LOCALBASE / 'policy.json', "w") as db:
        json.dump(policy, db)


    logging.debug('Objects received successfully !')

    ar_count = 0
    file_ptr = open(LOCALBASE / 'access_request.txt', "w")

    obj_choice = random.choices([0, 1], weights=[0.4, 0.6], k=50005)
    
    inputs = [CLIENT]
    outputs = []
    try:
        logging.debug(f"Starting the sending of access requests with arrival rate: {AR_ARRIVAL_RATE}")
        while inputs:
            mean_tw = 1000 / AR_ARRIVAL_RATE
            ar_tw = max(1, np.random.poisson(mean_tw, 1)[0]) * 1e-3
            # time.sleep(ar_tw) # Old architecture
            print
            if ar_count == 5000:
                break
            read_ready, write_ready, exception = select.select(inputs, outputs, inputs, ar_tw)
            
            for s in read_ready:
                if s is CLIENT:
                    logging.info("Server message received: ")
                    message = receiveMessage(CLIENT)
                    if message == '':
                        logging.info("Server closed connection")
                        inputs.remove(s)
                        break
                    logging.info(f"Server message: {message}")
            
            for s in exception:
                logging.error(f"Exception occurred: {s}")
                pass
        
            
            # Timeout
            if not (read_ready or write_ready or exception):
                ar_count += 1
                # Generate a random access request
                access_request = {}
                access_request['sub'] = {}
                access_request['obj'] = {}
                access_request['op'] = 'read'

                access_request['sub']['uid'] = ['*']
                access_request['obj']['rid'] = ['*']

                for sub_attr in user_attr_val:
                    access_request['sub'][sub_attr] = random.sample(user_attr_val[sub_attr] + ['*'], 1)[0]
                for obj_attr in obj_attr_val:
                    access_request['obj'][obj_attr] = random.sample(obj_attr_val[obj_attr] + ['*'], 1)[0]
                AR_send = {}
                if obj_choice[ar_count - 1] == 1:
                    AR_send = policy['rule_' + str(random.randint(1, tot_len_of_policy))]
                else:
                    AR_send = access_request
                file_ptr.write(str(AR_send) + '\n')
                result = resolveAR(AR_send, policy, ar_count)
                logging.info(f"-- ACCESS REQUEST {ar_count} on client side: ")
                if result == 1:
                    logging.info("Access granted !")
                else:
                    logging.info("Access denied !")

                # Send access request to the server
                sendMessage(CLIENT, AR_send)
                
    except KeyboardInterrupt:
        logging.info("Exiting...")
        pass
    except Exception as e:
        logging.error(f"Exception: {e}")
        pass
        
    file_ptr.close()

    logging.info('Client closing connection...\nSee ya!!!!!!\n')
    CLIENT.close()

if __name__== "__main__":
    arg_parser = ArgumentParser()
    arg_parser.add_argument('-a', '--arrival_rate', type=int, help='Mean arrival rate of access requests')
    
    arg_parser.set_defaults(arrival_rate=10)
    
    args = arg_parser.parse_args()
    
    if args.arrival_rate:
        AR_ARRIVAL_RATE = args.arrival_rate
        
    sys.path.append(str(CURRENT_DIR.parent))
    logging.info(f"Current directory: {os.getcwd()}")
    
    try:
        main()
    except KeyboardInterrupt:
        logging.info('Closing the client...')
        if CLIENT: CLIENT.close()
        logging.info('------------- Client Closed ! -------------')
        sys.exit(0)
