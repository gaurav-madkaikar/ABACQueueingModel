# Script to generate our ABAC System
import string
import random
import numpy as np
import json
import time
import re
import scriptRunner as srun
import queue
import auxiliary_list as aux_list
import threading
import shutil
# from collections import Queue

# 5 attributes - each can take 3 values + additional 2 values (* and -)
# * implies it can match any value except -
# - implies the given attribute is not present in the corresponding rule

# class DataGenerator:
#     def __init__(self) -> None:
#         # Declare class variables
#         pass
#     def generateAttributes(self):
#         pass
#     def generateAttrValues(self):
#         pass
#     def generateSubjectSet(self):
#         pass
#     def generateObjectSet(self):
#         pass
#     def generateABACPolicy(self):
#         pass
#     def generateACM(self):
#         pass
#     def generateFormattedOutput(self):
#         pass

abac_policy = {}
NO_OF_ATTRIBUTES = 2
NO_OF_VALUES = 3
MAX_DUPLICATES = 2

sub_obj_pairs_not_taken = []

MAX_LIST_SIZE = 1000
auxList = aux_list.auxiliaryList(MAX_LIST_SIZE)

# SUCCESS_PROBABILITY = 0.6
# NUMBER_OF_TRIALS = 10

# Access Control Matrix corresponding to the original low-level policy
ACM = []

# Access Control Matrix corresponding to the refined ABAC policy
ACM_prime = []

# Generate user and object attributes
user_attr = []
object_attr = []
user_attr_val = {}
object_attr_val = {}

userbase = {}
objectbase = {}
policybase = {}


def genComponentSet(NO_OF_COMPONENTS, user_attr, user_attr_val, init_str="sub_"):
    # Set a different random seed
    random.seed(time.time())
    global abac_policy, NO_OF_ATTRIBUTES, NO_OF_VALUES, MAX_DUPLICATES
    user_info = {}
    used_users = []
    unique_count = 0

    for i in range(1, NO_OF_COMPONENTS + 1):
        # print(f"I: {i}")
        flg = 1
        # flg2 = 1
        # flg3 = 1
        # final_flg = flg & flg2 & flg3
        while flg == 1:
            unique_ID = init_str + str(i)
            user_info[unique_ID] = {}
            curr_user_attr = {}

            for attr in user_attr:
                attr_usr_val = user_attr_val[attr][random.randint(
                    0, NO_OF_VALUES - 1)]
                curr_user_attr[attr] = attr_usr_val

            curr_user_attr_list = list(curr_user_attr.items())
            curr_user_attr_list.sort()
            cf = used_users.count(curr_user_attr_list)

            if init_str == "sub_":
                curr_user_attr["uid"] = unique_ID
            elif init_str == "obj_":
                curr_user_attr["rid"] = unique_ID

            # At max 3 users can have the same attribute-value pairs
            # We can fine tune the distribution of users as required
            # Divide users based on similarity in a ratio of 3:2:1, i.e. 3/6 - no. of unique users, 2/6 - no. of users with max 1 more duplicates, 1/6 - no. of users with max 2 more duplicates
            if cf < MAX_DUPLICATES:
                if cf == 0:
                    unique_count += 1
                used_users.append(curr_user_attr_list)
                user_info[unique_ID].update(curr_user_attr)
                flg = 0

        # if cf == 0:
        #     unique_count += 1
        # used_users.append(curr_user_attr_list)
        # user_info[unique_ID].update(curr_user_attr)

    if init_str == "sub_":
        print(f"Total Users = {NO_OF_COMPONENTS}")
        print(f"Total unique users = {unique_count}")
    elif init_str == "obj_":
        print(f"Total Objects = {NO_OF_COMPONENTS}")
        print(f"Total unique objects = {unique_count}")

    return user_info


'''
def genUserSet(NO_OF_SUBJECTS, user_attr, user_attr_val, init_str="sub_"):
    # Set a different random seed
    random.seed(time.time())
    global abac_policy, NO_OF_ATTRIBUTES, NO_OF_VALUES, MAX_DUPLICATES
    user_info = {}
    used_users = []
    unique_count = 0

    for i in range(1, NO_OF_SUBJECTS + 1):
        # print(f"I: {i}")
        flg = 1
        # flg2 = 1
        # flg3 = 1
        # final_flg = flg & flg2 & flg3
        while flg == 1:
            unique_ID = init_str + str(i)
            user_info[unique_ID] = {}
            curr_user_attr = {}
            for attr in user_attr:
                attr_usr_val = user_attr_val[attr][random.randint(
                    0, NO_OF_VALUES - 1)]
                curr_user_attr[attr] = attr_usr_val

            curr_user_attr_list = list(curr_user_attr.items())
            curr_user_attr_list.sort
            cf = used_users.count(curr_user_attr_list)

            # At max 3 users can have the same attribute-value pairs
            # We can fine tune the distribution of users as required
            # Divide users based on similarity in a ratio of 3:2:1, i.e. 3/6 - no. of unique users, 2/6 - no. of users with max 1 more duplicates, 1/6 - no. of users with max 2 more duplicates

            if cf < MAX_DUPLICATES:
                if cf == 0:
                    unique_count += 1
                used_users.append(curr_user_attr_list)
                user_info[unique_ID].update(curr_user_attr)
                flg = 0

        # if cf == 0:
        #     unique_count += 1
        # used_users.append(curr_user_attr_list)
        # user_info[unique_ID].update(curr_user_attr)
    print(f"Total Users = {NO_OF_SUBJECTS}")
    print(f"Total unique users = {unique_count}")
    return user_info

def genObjectSet(NO_OF_OBJECTS, obj_attr, obj_attr_val, init_str):
    # Set a different random seed
    random.seed(time.time())
    # All objects are unique
    global abac_policy, NO_OF_ATTRIBUTES, NO_OF_VALUES
    user_info = {}
    used_users = []
    for i in range(1, NO_OF_OBJECTS + 1):
        # print(f"I: {i}")
        flg = 1
        while flg == 1:
            unique_ID = init_str + str(i)
            user_info[unique_ID] = {}
            curr_obj_attr = {}
            for attr in obj_attr:
                attr_usr_val = obj_attr_val[attr][random.randint(
                    0, NO_OF_VALUES - 1)]
                curr_obj_attr[attr] = attr_usr_val

            curr_obj_attr_list = list(curr_obj_attr.items())
            curr_obj_attr_list.sort

            if curr_obj_attr_list not in used_users:
                used_users.append(curr_obj_attr_list)
                user_info[unique_ID].update(curr_obj_attr)
                flg = 0
    return user_info
'''


def genAttributeValues():
    global NO_OF_ATTRIBUTES, NO_OF_VALUES, user_attr, object_attr, user_attr_val, object_attr_val

    # user_attr.append("uid")
    # object_attr.append("rid")

    for i in range(NO_OF_ATTRIBUTES):
        N = random.randint(3, 7)
        res = ''.join(random.choices(string.ascii_uppercase, k=N))
        user_attr.append(res)

    for i in range(NO_OF_ATTRIBUTES):
        N = random.randint(3, 7)
        res = ''.join(random.choices(string.ascii_uppercase, k=N))
        object_attr.append(res)

    for str_ in user_attr:
        user_attr_val[str_] = []
        for j in range(NO_OF_VALUES):
            N = random.randint(3, 5)
            res = ''.join(random.choices(string.ascii_lowercase, k=N))
            user_attr_val[str_].append(res)
    for str_ in object_attr:
        object_attr_val[str_] = []
        for j in range(NO_OF_VALUES):
            N = random.randint(3, 5)
            res = ''.join(random.choices(string.ascii_lowercase, k=N))
            object_attr_val[str_].append(res)

    print("User Attributes:")
    print(user_attr)

    print("\nObject Attributes:")
    print(object_attr)

    print("\nUser attribute-value pairs:")
    print(user_attr_val)

    print("\nObject attribute-value pairs:")
    print(object_attr_val)


def preprocess():
    global abac_policy, NO_OF_ATTRIBUTES, NO_OF_VALUES, user_attr, object_attr, user_attr_val, object_attr_val, objectbase, userbase, policybase, sub_obj_pairs_not_taken

    # env_attr = []
    # access_attr = ["read", "write", "modify", "execute"]
    # 0 - read, 1 - write
    access_attr = ["read", "write"]

    print("\n\n")

    print("+++" * 15 + "   Generating set of subjects   " + "+++" * 15)
    NO_OF_SUBJECTS = 5
    user_info = {}
    # used_users = []
    '''
        # user_info = {
        #     "sub_i": {
        #         attr_1: val_1,
        #         attr_2: val_2,
        #         ...
        #         attr_k: val_k
        #     }
        # }
    '''

    user_info = genComponentSet(
        NO_OF_SUBJECTS, user_attr, user_attr_val, "sub_")
    # print(user_info)
    with open("database/userbase.json", "w") as db:
        json.dump(user_info, db)

    print("\n\n")

    print("+++" * 15 + "   Generating set of objects    " + "+++" * 15)
    NO_OF_OBJECTS = 5
    # user_info = {}
    # used_users = []
    '''
        obj_info = {
            "obj_i": {
                attr_1: val_1,
                attr_2: val_2,
                ...
                attr_k: val_k
            }
        }
    '''
    obj_info = genComponentSet(
        NO_OF_OBJECTS, object_attr, object_attr_val, "obj_")
    # Write the object set into the specififed JSON file
    with open("database/objectbase.json", "w") as db:
        json.dump(obj_info, db)

    print("\n\n")
    # print(user_info)
    # Formation of rules
    # For each object resource, we allow at max 6 rules (<= the number of users)
    # Also, each object must have atleast one corresponding to it
    # for obj_id in obj_info.keys():
    unique_user_attr_dict = dict()
    unique_usr_list = []
    usr_counter = 0
    for user_id in user_info:
        # Get unique user atrribute-value pairs
        temp = user_info[user_id]
        if temp.get("uid") is not None:
            del temp["uid"]
        temp_list = list(temp.items())
        temp_list.sort()
        if temp_list not in unique_usr_list:
            usr_counter += 1
            index = "usr_" + str(usr_counter)
            unique_user_attr_dict[index] = {}
            unique_user_attr_dict[index].update(temp)
            unique_usr_list.append(temp_list)
    print(
        f"------------ Unique user attr-val pairs = {len(unique_user_attr_dict)} ------------")
    print(unique_user_attr_dict)

    print("\n\n\n")

    rule_count = 0
    abac_policy = {}

    # Include unique rules
    used_rules = []

    # myActualACM = []

    for each_object in obj_info:
        # Initialize the random seed
        random.seed(time.time())
        # ls = []

        for each_usr in unique_user_attr_dict:
            rule_taken = random.randint(0, 1)
            # ls.append(rule_taken)
            if rule_taken == 0:
                # sub_obj_pairs_not_taken.append([each_usr, each_object])
                continue
            rule_count += 1
            # print(f"\n----- RULE {rule_count} -----")
            rule_key = "rule_" + str(rule_count)
            abac_policy[rule_key] = {}
            abac_policy[rule_key]["sub"] = {}
            abac_policy[rule_key]["obj"] = {}
            abac_policy[rule_key]["op"] = {}

            # ---- Collect user attributes ----
            while True:
                # Select some subset of attributes from the user attribute set
                # usr_attr_set = unique_user_attr_dict[each_usr]
                no_of_usr_attr = random.randint(
                    1, len(unique_user_attr_dict[each_usr]))
                # Use random.sample to select random user attributes (only attributes)
                random_keys = random.sample(
                    list(unique_user_attr_dict[each_usr].keys()), no_of_usr_attr)
                # Create a new dictionary with the selected keys and their corresponding values
                random_usr_pairs = {}
                random_usr_pairs["uid"] = ["*"]

                curr_rule_list = []
                for key in user_attr:
                    random_usr_pairs[key] = []
                    if key in random_keys:
                        random_usr_pairs[key].append(
                            unique_user_attr_dict[each_usr][key])
                        curr_rule_list.append(
                            [key, unique_user_attr_dict[each_usr][key]])
                    else:
                        random_usr_pairs[key].append("*")
                        curr_rule_list.append([key, "*"])

                # ---- Collect object attributes ----
                random_obj_pairs = {}
                random_obj_pairs["rid"] = ["*"]

                for curr_obj_attr in obj_info[each_object]:
                    if curr_obj_attr == "rid":
                        continue
                    random_obj_pairs[curr_obj_attr] = [
                        obj_info[each_object][curr_obj_attr]]
                    curr_rule_list.append(
                        [curr_obj_attr, obj_info[each_object][curr_obj_attr]])

                curr_rule_list.sort()
                # print(curr_rule_list)

                # print(f"Random Pairs:\n1: {random_usr_pairs}")
                # print(f"2: {random_obj_pairs}")

                if curr_rule_list not in used_rules:
                    used_rules.append(curr_rule_list)
                    break

            abac_policy[rule_key]["sub"] = random_usr_pairs
            abac_policy[rule_key]["obj"] = random_obj_pairs

            # Assign operation
            abac_policy[rule_key]["op"] = access_attr[0]
        # myActualACM.append(ls)

    # myActualACM = np.transpose(myActualACM)
    # print(\n'.join(['   '.join([str(cell) for cell in row] for row in ACM_prime]))
    # print('\n'.join(['   '.join([str(cell)
    #                                       for cell in row]) for row in myActualACM]))

    # print("\n------------- ABAC POLICY -------------\n")
    # print(abac_policy)

    print(f"\nTotal number of rules: {rule_count}")
    print(f"Unique number of rules: {len(used_rules)}")

    for etup in sub_obj_pairs_not_taken:
        print(etup)
    # Write the ABAC Policy set into the specififed JSON file
    with open("database/policy.json", "w") as db:
        json.dump(abac_policy, db)

    with open("database/userbase.json") as db:
        userbase = json.load(db)

    with open("database/objectbase.json") as db:
        objectbase = json.load(db)

    with open("database/policy.json") as db:
        policybase = json.load(db)

    # Write into an output .abac file in the ABACMining/ folder
    file_ptr = open("./ABACMining/ori_policy.abac", "w")

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
    # print(policybase)
    for rule_id in policybase:
        write_str = "rule("
        sub_tmp = policybase[rule_id]["sub"]
        obj_tmp = policybase[rule_id]["obj"]
        operation = policybase[rule_id]["op"]

        # print(f"Subject Attributes: {sub_tmp}", end = ' | ')
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
                # print(write_str)
        if flg1:
            write_str = write_str[:-2]
        write_str += "; "
        # print(f"String: {write_str}", end = ' | ')
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
                # print(write_str)
        if flg2:
            write_str = write_str[:-2]
        write_str += "; "
        # print(f"Object Attributes: {obj_tmp}", end = ' | ')
        # print(f"String: {write_str}")
        # print()

        write_str += "{" + operation + "}; "
        write_str += ")\n"
        file_ptr.write(write_str)

    file_ptr.close()

    print(f"Original Number of rules = {len(abac_policy)}")

    '''
        abac_policy = {
            "rule_1": {
                sub: {attr1:val1, attr2:val2},
                obj: {attr3:val3},
                env: {attr4:val4, attr5:val5},  // Optional attribute-value pairs
                op: action1
            }
        }
        No. of subject attributes = s, No. of subject attribute-values = j1, j2, ..., jo 
        No. of object attributes = o, No. of object attribute-values = i1, i2, ..., io 
        No. of rules in ABAC Policy = r
        No. of users in the system = Usys
    '''


def extractRefinedPolicy():
    blockOfRefinedCode = []
    flg = 0
    # Iterate through each line in the file
    with open('./refined_policy.abac', 'r') as file:
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

                    for attr in object_attr:
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
    with open("database/refined_policy.json", "w") as db:
        json.dump(modified_rules, db)


def resolveAccessRequest(sub_key, obj_key, user_attr_val_dict, obj_attr_val_dict, policy):
    # result = 0 implies access not granted, result = 1 implies access granted
    result = 0

    # Process the result
    for i in range(len(policy)):
        key = "rule_" + str(i + 1)
        rule = policy.get(key)
        # if sub_key == "sub_1" and obj_key == "obj_1":
        #     print(rule)
        sub_attr_check = 1
        for attr in rule["sub"]:
            # if sub_key == "sub_1" and obj_key == "obj_1":
            #     print(rule["sub"][attr], end=' ')
            #     print(user_attr_val_dict[attr])
            if rule["sub"][attr] == ['*']:
                continue
            curr_sub_attr_check = 0
            for values in rule["sub"][attr]:
                if values == user_attr_val_dict[attr]:
                    curr_sub_attr_check = 1
                    break
            if curr_sub_attr_check == 0:
                sub_attr_check = 0
                break
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
            for values in rule["obj"][attr]:
                if values == obj_attr_val_dict[attr]:
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

    # if sub_key == "sub_1" and obj_key == "obj_1":
    #     print(f"RESULT: {result}")
    # Return the result
    return result

# Generate ACM corresponding to the original ABAC policy (P)


def generateACM():
    global ACM, sub_obj_pairs_not_taken

    original_policy = dict()
    with open("database/policy.json") as db:
        original_policy = json.load(db)
    with open("database/userbase.json") as db:
        userbase = json.load(db)
    with open("database/objectbase.json") as db:
        objectbase = json.load(db)

    # Policy has been loaded into original_policy
    for i in range(len(userbase)):
        key_sub = "sub_" + str(i + 1)
        usr_attr_val_dict = userbase.get(key_sub)
        access_result_list = []
        for j in range(len(objectbase)):
            key_obj = "obj_" + str(j + 1)
            obj_attr_val_dict = objectbase.get(key_obj)
            result = resolveAccessRequest(key_sub, key_obj,
                                          usr_attr_val_dict, obj_attr_val_dict, original_policy)
            if result == 0:
                sub_obj_pairs_not_taken.append([key_sub, key_obj])
                # auxList.add(key_sub, key_obj, "read")
            access_result_list.append(result)
        ACM.append(access_result_list)

    print("ACM generated successfully!")
    file_ptr = open('database/ACM_ori/ACM_ori.txt', 'w')
    file_ptr.write('\n'.join(['   '.join([str(cell) for cell in row])
                     for row in ACM]))
    file_ptr.close()

# Generate ACM corresponding to the refined ABAC policy (P')
def generateACM_modified():
    global ACM_prime

    refined_policy = dict()
    with open("database/refined_policy.json") as db:
        refined_policy = json.load(db)
    with open("database/userbase.json") as db:
        userbase = json.load(db)
    with open("database/objectbase.json") as db:
        objectbase = json.load(db)

    # Policy has been loaded into the original_policy dictionary
    for i in range(len(userbase)):
        key_sub = "sub_" + str(i + 1)
        usr_attr_val_dict = userbase.get(key_sub)
        access_result_list = []
        for j in range(len(objectbase)):
            key_obj = "obj_" + str(j + 1)
            obj_attr_val_dict = objectbase.get(key_obj)
            result = resolveAccessRequest(key_sub, key_obj,
                                          usr_attr_val_dict, obj_attr_val_dict, refined_policy)
            access_result_list.append(result)
        ACM_prime.append(access_result_list)

    print("ACM' generated successfully!")
    file_ptr = open('database/ACM_prime/ACM_prime.txt', 'w')
    file_ptr.write('\n'.join(['   '.join([str(cell) for cell in row]) for row in ACM_prime]))
    file_ptr.close()


def checkNoOfMismatches():
    global ACM, ACM_prime
    no_of_mismatches = 0
    no_of_rows = len(ACM)
    no_of_cols = len(ACM[0])

    for i in range(no_of_rows):
        for j in range(no_of_cols):
            if ACM[i][j] != ACM_prime[i][j]:
                no_of_mismatches += 1

    return no_of_mismatches


def generateAuxiliaryList():
    global sub_obj_pairs_not_taken, auxList
    # Generate an auxiliary list
    # random.seed(time.time())
    random.shuffle(sub_obj_pairs_not_taken)
    for sub_obj_pair in sub_obj_pairs_not_taken:
        auxList.add(sub_obj_pair[0], sub_obj_pair[1], "read")

    print(f"Length of auxiliary list = {auxList.size()}")

    file_ptr = open('database/auxiliary_list.txt', 'w')
    for i in range(auxList.size()):
        write_str = ""
        tmp_access_update = list(auxList.getKeyValue(i))
        write_str += "{"
        for j in range(len(tmp_access_update)):
            write_str += tmp_access_update[j] + ", "
        write_str = write_str[:-2]
        write_str += "}"
        file_ptr.write(write_str)
        if i != auxList.size() - 1:
            file_ptr.write("\n")

    file_ptr.close()


def generateCombinedPolicy():
    # Combine policy P and auxiliary list to generate a new policy P' which is then passed to the ABAC mining algorithm
    combined_policy = {}
    userbase = {}
    objectbase = {}
    with open("database/curr_policy.json") as db:
        combined_policy = json.load(db)
    with open("database/userbase.json") as db:
        userbase = json.load(db)
    with open("database/objectbase.json") as db:
        objectbase = json.load(db)

    ori_no_of_rules = len(combined_policy)
    file_ptr = open('database/auxiliary_list.txt', 'r')
    temp_update_list = []
    for line in file_ptr:
        line = line.strip(' \n')
        line = line[1:-1]
        # print(line)
        temp_update_list.append(line.split(', '))

    rule_ID = ori_no_of_rules + 1
    X = np.random.binomial(1, 0.3, len(temp_update_list))
    for i in range(len(temp_update_list)):
        temp_access = temp_update_list[i]
        usr = temp_access[0].strip(' \n')
        obj = temp_access[1].strip(' \n')
        op = temp_access[2].strip(' \n')

        if X[i] == 0:
            continue
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
    print(f"Combined number of rules = {len(combined_policy)}")

    with open("database/combined_policy.json", "w") as db:
        json.dump(combined_policy, db)

    file_ptr.close()
    print("Process Over!")

AR_dict = {}
cnt_AR = 0

# For now consider only users which are present in the system
def genAccessRequest(access_req_container):
    global user_attr_val, object_attr_val, user_attr, object_attr, NO_OF_ATTRIBUTES, NO_OF_VALUES, AR_dict, cnt_AR

    X = np.random.binomial(1, 0.5, NO_OF_ATTRIBUTES)
    access_request = []

    sub_attributes = []
    obj_attributes = []

    # Assign user attributes
    for i in range(NO_OF_ATTRIBUTES):
        attr = user_attr[i]
        attr_val = user_attr_val[attr]
        for j in range(NO_OF_VALUES):
            if X[j] == 0:
                continue
            # print(f"User attribute: {attr} | Value: {attr_val[j]}")
            access_request.append((attr, attr_val[j], "read"))


    X = np.random.binomial(1, 0.5, NO_OF_ATTRIBUTES)
    # Assign object attributes
    for i in range(NO_OF_ATTRIBUTES):
        attr = object_attr[i]
        attr_val = object_attr_val[attr]
        for j in range(NO_OF_VALUES):
            if X[j] == 0:
                continue
            # print(f"Object attribute: {attr} | Value: {attr_val[j]}")
            access_request.append((attr, attr_val[j], "read"))

    if cnt_AR == 0:
        cnt_AR += 1
        AR_dict["ar_"+str(cnt_AR)] = {}
        AR_dict["ar_"+str(cnt_AR)]["sub"] = {}
        AR_dict["ar_"+str(cnt_AR)]["obj"] = {}
        AR_dict["ar_"+str(cnt_AR)]["op"] = "read"
    
    AR_dict["ar_"+str(cnt_AR)]["sub"].update()
        
    print(f"Generated sample access request: {access_request}")
    print(access_request)

def resolveAccessRequestinQ():
    pass

# Objectbase and Userbase must be pre-loaded into program memory since they will not change once generated

if __name__ == "__main__":
    # print("+" * 45, end='')
    # print(" Welcome to the ABAC Policy Generator ", end='')
    # print("+" * 45)
    
    # genAttributeValues()
    # preprocess()
    generateACM()

    # Generate the auxiliary list
    generateAuxiliaryList()

    # # Make a copy of the original policy
    original_file_name = "database/policy.json"
    copy_file_name = "database/curr_policy.json"

    shutil.copyfile(original_file_name, copy_file_name)

    # Combine policy P' and the auxiliary list
    generateCombinedPolicy()

    # Pass this combined policy P' to the ABAC Mining algorithm to generate P'' 
    # Run the Bash script using Git Bash
    srun.subprocess.run(["bash", srun.bash_command])

    # # # To be coded !!
    # # convertACLtoPolicy()

    extractRefinedPolicy()
    generateACM_modified()
    no_of_mismatches = checkNoOfMismatches()

    print(f"\nNo. of mismatches = {no_of_mismatches}")

    # Randomly generate Access Requests in a separate thread
    # access_req_container = Queue()
    access_req_container = []

    # # Repeat for a number of times
    # for i in range(1):
    #     print(f"\nSample access request {i+1}:")
    #     genAccessRequest(access_req_container)

