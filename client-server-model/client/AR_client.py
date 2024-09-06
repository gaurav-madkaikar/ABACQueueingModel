import sys
import json
import socket
import select
from threading import Thread
import numpy as np
import pickle
import time
import random
from argparse import ArgumentParser
from threading import Thread
from pathlib import Path
import logging

HEADERSIZE = 16
FORMAT = 'utf-8'

AR_ARRIVAL_RATE = 80

CURRENT_DIR = Path(__file__).resolve().parent
sys.path.append(str(CURRENT_DIR.parent))

BASE_DIR = Path(__file__).resolve().parent.parent.parent
CLIENT = BASE_DIR / 'client-server-model' / 'client' / 'AR_client.py'
CLIENT_LOG = BASE_DIR / 'logs' / 'client.log'

# logging.basicConfig(filename=CLIENT_LOG, level=logging.DEBUG)
logging.basicConfig(level=logging.DEBUG)
logging.info('Client logging started...')


def sendMessage(conn, message):
    msg_send = b''
    if type(message) == dict:
        msg_send = pickle.dumps(message)
    else:
        msg_send = bytes(message, 'utf-8')

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
    # if id == 1:
    #     print(f"Access Request: {access_request}")
    # result = 0 implies access not granted, result = 1 implies access granted
    result = 0

    # Process the result
    for i in range(len(policy)):
        key = "rule_" + str(i + 1)
        rule = policy.get(key)
        # if i == 1279:
        #     print(f"\nRule: {rule}")
        sub_attr_check = 1
        for attr in rule["sub"]:
            # if i == 1279:
            #     print(f" Attr: {attr}")
            if rule["sub"][attr] == ['*']:
                continue
            curr_sub_attr_check = 0
            for value in access_request["sub"][attr]:
                if value in rule["sub"][attr]:
                    curr_sub_attr_check = 1
                    break
            # for value in rule["sub"][attr]:
            #     if i == 1279:
            #         print(f"** {value} : {access_request['sub'][attr]}")
            #     for access_val in access_request["sub"][attr]:
            #         if value == access_val:
            #             curr_sub_attr_check = 1
            #             break
                # if value == access_request["sub"][attr]:
                #     curr_sub_attr_check = 1
                #     break
            if curr_sub_attr_check == 0:
                sub_attr_check = 0
                break
        # if i == 2:
        #     print(f"Sub-attr-check: {sub_attr_check}")
        if sub_attr_check == 0:
            continue
            # if rule["sub"][attr] != user_attr_val_dict[attr]:
            #     result = 0
            #     break
        obj_attr_check = 1
        for attr in rule["obj"]:
            if rule["obj"][attr] == ['*']:
                continue
            curr_obj_attr_check = 0

            for value in access_request["obj"][attr]:
                if value in rule["obj"][attr]:
                    curr_obj_attr_check = 1
                    break

            # for value in rule["obj"][attr]:
            #     for access_val in access_request["sub"][attr]:
            #         if value == access_val:
            #             curr_obj_attr_check = 1
            #             break
                # if values == access_request["obj"][attr]:
                #     curr_obj_attr_check = 1
                #     break
            if curr_obj_attr_check == 0:
                obj_attr_check = 0
                break
        # if i == 2:
        #     print(f"Obj-attr-check: {obj_attr_check}")
        if obj_attr_check == 0:
            continue
        else:
            result = 1
            break
    return result


def main():
    logging.info("---------------------------------- Client Side ----------------------------------")

    # Client Initialization
    
    # Get local machine name
    host = '127.0.0.1'
    port = 8080
    ADDR = (host, port)

    flg = 0

    # Create a socket object
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_socket.connect(ADDR)
    logging.info("Server Connection successful!")

    user_attr_val = receiveMessage(client_socket, _type='dict')
    logging.debug(f'user attribute-values: {user_attr_val}')
    
    obj_attr_val = receiveMessage(client_socket, _type='dict')
    logging.info(f'object attribute-values: {obj_attr_val}')

    policy = receiveMessage(client_socket, _type='dict')
    logging.debug(f"Received policy succesfully!")
    tot_len_of_policy = len(policy)

    with open("localbase/policy.json", "w") as db:
        json.dump(policy, db)


    logging.debug('Objects received successfully !')

    ar_count = 0
    file_ptr = open("localbase/access_request.txt", "w")

    obj_choice = random.choices([0, 1], weights=[0.4, 0.6], k=50005)
    
    inputs = [client_socket]
    outputs = []
    try:
        logging.debug(f"Starting the sending of access requests with arrival rate: {AR_ARRIVAL_RATE}")
        while inputs:
            mean_tw = 1000 / AR_ARRIVAL_RATE
            ar_tw = max(1, np.random.poisson(mean_tw, 1)[0]) * 1e-3
            # time.sleep(ar_tw)
            print
            if ar_count == 5000:
                break
            read_ready, write_ready, exception = select.select(inputs, outputs, inputs, ar_tw)
            
            for s in read_ready:
                if s is client_socket:
                    logging.info("Server message received: ")
                    message = receiveMessage(client_socket)
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
                    # access_request['sub'][sub_attr] = [np.random.choice([user_attr_val[sub_attr], '*'])]
                for obj_attr in obj_attr_val:
                    access_request['obj'][obj_attr] = random.sample(obj_attr_val[obj_attr] + ['*'], 1)[0]
                    # access_request['obj'][obj_attr] = [np.random.choice([obj_attr_val[obj_attr], '*'])]
                AR_send = {}
                if obj_choice[ar_count - 1] == 1:
                    AR_send = policy['rule_' + str(random.randint(1, tot_len_of_policy))]
                else:
                    AR_send = access_request
                # if ar_count % 10 == 0:
                #     access_request = sample_ar   
                file_ptr.write(str(AR_send) + '\n')
                result = resolveAR(AR_send, policy, ar_count)
                logging.info(f"-- ACCESS REQUEST {ar_count} on client side: ")
                if result == 1:
                    logging.info("Access granted !")
                else:
                    logging.info("Access denied !")

                # Send access request to the server
                sendMessage(client_socket, AR_send)
                #sendObj(client_socket, access_request)
                
    except KeyboardInterrupt:
        logging.info("Exiting...")
        pass
    except Exception as e:
        logging.error(f"Exception: {e}")
        pass
        
    file_ptr.close()

    logging.info('Client closing connection...\nSee ya!!!!!!\n')
    client_socket.close()

if __name__== "__main__":
    arg_parser = ArgumentParser()
    arg_parser.add_argument('-a', '--arrival_rate', type=int, help='Mean arrival rate of access requests')
    
    arg_parser.set_defaults(arrival_rate=10)
    
    args = arg_parser.parse_args()
    
    if args.arrival_rate:
        AR_ARRIVAL_RATE = args.arrival_rate
    
    main()
