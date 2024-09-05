import sys
import json
import socket
from threading import Thread
import numpy as np
import pickle
import time
import random
from argparse import ArgumentParser
from threading import Thread

HEADERSIZE = 16
FORMAT = 'utf-8'

AR_ARRIVAL_RATE = 80


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
    print("---------------------------------- Client Side ----------------------------------")

    # Client Initialization
    
    # Get local machine name
    host = '127.0.0.1'
    port = 8080
    ADDR = (host, port)

    flg = 0

    # Create a socket object
    client_socket = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client_socket.connect(ADDR)
    print("Server Connection successful!")

    user_attr_val = receiveMessage(client_socket, _type='dict')
    # user_attr_val = receiveObj(client_socket)
    print(f'user attribute-values: {user_attr_val}')
    print('\n')

    # Object attributes
    # obj_attr_val = receiveObj(client_socket)
    # recv_msg = client_socket.recv(4).decode('utf-8')

    # len_recv_msg = int(recv_msg)
    # recv_msg = client_socket.recv(len_recv_msg)
    # obj_attr_val = pickle.loads(recv_msg)
    obj_attr_val = receiveMessage(client_socket, _type='dict')
    print(f'object attribute-values: {obj_attr_val}')
    print('\n')

    # with open("localbase/user_attr_val.json", "w") as db:
    #     json.dump(user_attr_val, db)
    # with open("localbase/obj_attr_val.json", "w") as db:
    #     json.dump(obj_attr_val, db)

    policy = receiveMessage(client_socket, _type='dict')
    print(f"Received policy succesfully!")
    tot_len_of_policy = len(policy)

    with open("localbase/policy.json", "w") as db:
        json.dump(policy, db)


    print('Objects received successfully !')
    
    # tmp_ind = 'rule_' + str(random.randint(1, tot_len_of_policy))
    # sample_ar = policy[tmp_ind]
    ar_count = 0
    file_ptr = open("localbase/access_request.txt", "w")

    obj_choice = random.choices([0, 1], weights=[0.4, 0.6], k=50005)
    # count_1 = obj_choice.count(1)
    # print(f'Count of 1s: {count_1}\n')
    # print('rule_' + str(random.randint(1, tot_len_of_policy)))
    # print(f'Rule {1280}: {sample_ar}')
    # print(f'Result: {resolveAR(sample_ar, policy, 1)}')
    # print('\n')
    try:
        print(f"Starting the sending of access requests with arrival rate: {AR_ARRIVAL_RATE}")
        while True:
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
            print(f"-- ACCESS REQUEST {ar_count} on client side: ", end = '')
            if result == 1:
                print("Access granted !")
            else:
                print("Access denied !")

            # Send access request to the server
            sendMessage(client_socket, AR_send)
            #sendObj(client_socket, access_request)
            mean_tw = 1000 / AR_ARRIVAL_RATE
            ar_tw = max(1, np.random.poisson(mean_tw, 1)[0]) * 1e-3
            time.sleep(ar_tw)
            if ar_count == 1000000:
                break
    except KeyboardInterrupt:
        print("Exiting...")
        pass
    except Exception as e:
        print(f"Exception: {e}")
        pass
        
    file_ptr.close()

    print('Client closing connection...\nSee ya!!!!!!\n')
    client_socket.close()

if __name__== "__main__":
    arg_parser = ArgumentParser()
    arg_parser.add_argument('-a', '--arrival_rate', type=int, help='Mean arrival rate of access requests')
    
    arg_parser.set_defaults(arrival_rate=10)
    
    args = arg_parser.parse_args()
    
    if args.arrival_rate:
        AR_ARRIVAL_RATE = args.arrival_rate
    
    main()
