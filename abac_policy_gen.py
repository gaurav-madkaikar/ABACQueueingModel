# Script to generate our ABAC System
import string
import random
import numpy as np
import json
import time
import re

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

abac_policy = dict()
NO_OF_ATTRIBUTES = 3
NO_OF_VALUES = 2
MAX_DUPLICATES = 2

# Access Control Matrix corresponding to the original low-level policy
ACM = []

# Access Control Matrix corresponding to the refined ABAC policy
ACM_prime = []

# Generate user and object attributes


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


def preprocess():
    global abac_policy, NO_OF_ATTRIBUTES, NO_OF_VALUES
    user_attr = []
    object_attr = []
    env_attr = []
    # access_attr = ["read", "write", "modify", "execute"]
    # 0 - read, 1 - write
    access_attr = ["read", "write"]

    for i in range(NO_OF_ATTRIBUTES):
        N = random.randint(3, 7)
        res = ''.join(random.choices(string.ascii_uppercase, k=N))
        user_attr.append(res)

    for i in range(NO_OF_ATTRIBUTES):
        N = random.randint(3, 7)
        res = ''.join(random.choices(string.ascii_uppercase, k=N))
        object_attr.append(res)

    # for i in range(NO_OF_ATTRIBUTES):
    #     N = random.randint(3, 7)
    #     res = ''.join(random.choices(string.ascii_uppercase +
    #                                  string.digits, k=N))
    #     env_attr.append(res)

    user_attr_val = {}
    object_attr_val = {}
    # env_attr_val = {}
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
    # for str_ in env_attr:
    #     env_attr_val[str_] = []
    #     for j in range(NO_OF_VALUES):
    #         N = random.randint(3, 5)
    #         res = ''.join(random.choices(string.ascii_lowercase +
    #                                      string.digits, k=N))
    #         env_attr_val[str_].append(res)

    print("User Attributes:")
    print(user_attr)

    print("\nObject Attributes:")
    print(object_attr)

    # print("\nEnvironment Attributes:")
    # print(env_attr)

    # print("Environment Variables")
    # print(env_attr)

    print("\nUser attribute-value pairs:")
    print(user_attr_val)

    print("\nObject attribute-value pairs:")
    print(object_attr_val)

    # print("\nEnvironment attribute-value pairs:")
    # print(env_attr_val)

    print("\n\n")

    print("+++" * 15 + "   Generating set of subjects   " + "+++" * 15)
    NO_OF_SUBJECTS = 4
    user_info = {}
    used_users = []
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
        # print(f"TEMP: {temp}")
        temp_list = list(temp.items())
        temp_list.sort
        if temp_list not in unique_usr_list:
            usr_counter += 1
            index = "usr_" + str(usr_counter)
            unique_user_attr_dict[index] = {}
            unique_user_attr_dict[index].update(temp)
            unique_usr_list.append(temp_list)
    print(
        f"------------ Unique user attr-val pairs = {len(unique_user_attr_dict)} ------------")
    # print(unique_user_attr_dict)

    print("\n\n\n")

    rule_count = 0
    abac_policy = {}
    for each_object in obj_info:
        random.seed(time.time())
        for each_usr in unique_user_attr_dict:
            rule_taken = random.randint(0, 1)
            if rule_taken == 0:
                continue
            rule_count += 1
            rule_key = "rule_" + str(rule_count)
            abac_policy[rule_key] = {}
            abac_policy[rule_key]["sub"] = {}
            abac_policy[rule_key]["obj"] = {}
            abac_policy[rule_key]["op"] = {}

            # ---- Collect user attributes ----
            no_of_usr_attr = random.randint(
                0, len(unique_user_attr_dict[each_usr]))
            # Use random.sample to select random keys without replacement
            random_keys = random.sample(
                list(unique_user_attr_dict[each_usr].keys()), no_of_usr_attr)
            # Create a new dictionary with the selected keys and their corresponding values
            random_pairs = {}
            for key in user_attr:
                if key in random_keys:
                    random_pairs[key] = unique_user_attr_dict[each_usr][key]
                else:
                    random_pairs[key] = "*"
            # random_pairs = {
            #     key: unique_user_attr_dict[each_usr][key] for key in random_keys}

            abac_policy[rule_key]["sub"] = random_pairs

            # ---- Collect object attributes ----
            random_pairs = obj_info[each_object]
            abac_policy[rule_key]["obj"] = random_pairs

            # Assign operation
            abac_policy[rule_key]["op"] = access_attr[0]

    # Write the ABAC Policy set into the specififed JSON file
    with open("database/policy.json", "w") as db:
        json.dump(abac_policy, db)

    with open("database/userbase.json") as db:
        userbase = json.load(db)

    with open("database/objectbase.json") as db:
        objectbase = json.load(db)

    with open("database/policy.json") as db:
        policybase = json.load(db)

    # Write into an output .abac file
    file_ptr = open("./ABACMining/ori_policy.abac", "w")

    file_ptr.write("# User Attribute Data\n")
    for user_id in userbase:
        write_str = "userAttrib(" + user_id + ", "
        for attr in userbase[user_id]:
            write_str += attr + "=" + userbase[user_id][attr] + ", "
        write_str = write_str[:-2]
        write_str += ")\n"
        file_ptr.write(write_str)

    file_ptr.write("\n")

    file_ptr.write("# Resource Attribute Data\n")
    for obj_id in objectbase:
        write_str = "resourceAttrib(" + obj_id + ", "
        for attr in objectbase[obj_id]:
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
            if sub_tmp[sub_keys] == '*':
                continue
            else:
                flg1 = True
                write_str += sub_keys + " in {" + sub_tmp[sub_keys] + "}, "
        if flg1:
            write_str = write_str[:-2]
        write_str += "; "
        # print(f"String: {write_str}", end = ' | ')
        for obj_keys in obj_tmp:
            if obj_tmp[obj_keys] == '*':
                continue
            else:
                flg2 = True
                write_str += obj_keys + " in {" + obj_tmp[obj_keys] + "}, "
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

    # Create rules for the ABAC Policy
    # Consider n users and m objects in the system
    # ABAC Policy must include atleast 2 rules corresponding to each object/resource
    # Each rule must include
    # NO_OF_RULES = 1000
    # NO_OF_ZEROES = NO_OF_RULES / 3
    # NO_OF_ONES = NO_OF_RULES - NO_OF_ZEROES
    # take_or_not_take = [0 for i in range(NO_OF_ZEROES)] + [1 for i in range(NO_OF_ONES)]
    # random.shuffle(take_or_not_take)
    # for i in range(NO_OF_RULES):
    #     print(f"ITERATION {i}:")
    # if take_or_not_take[i] == 0:
    #     continue
    # For each resource,

    # Create the Access Control Matrix (ACM)

    # POLICY_SIZE = 1000
    # for i in range(1, 1001):
    #     index = "rule_" + str(i)
    #     abac_policy[index] = dict()
    #     abac_policy[index]["sub"] = {}
    #     abac_policy[index]["obj"] = {}
    #     abac_policy[index]["env"] = {}
    #     abac_policy[index]["operation"] = access_attr[random.randint(0, 3)]
    '''
    # Create 10000 rules
    POLICY_SIZE = 10
    used_sub_attrs = []
    used_obj_attrs = set()
    used_env_attrs = set()
    used_rule_combinations = set()
    
    for i in range(1, POLICY_SIZE + 1):
        index = "rule_" + str(i)
        abac_policy[index] = {}
        abac_policy[index]["sub"] = {}
        abac_policy[index]["obj"] = {}
        abac_policy[index]["op"] = {}

    # for i in range(1, 11):
        # Collect subject attributes
    # for i in range(5):
        print(f"-- RULE {i}: --")
        no_of_sub_attr = random.randint(1, 3)
        sub_attr_val = user_attr_val
        flg = 1
        while flg == 1:
            curr_sub_attrs_list = []
            curr_sub_attrs = {}
            overall_sub_attrs = {}
            cnt = 0
            # attr_list_choice = list(user_attr_val.keys())
            # Get key-value pairs 
            while cnt != no_of_sub_attr:
                sub_attr_key = random.choice(user_attr)
                # print(f"Subject Attribute key: {sub_attr_key}")
                sub_attr_val_n = random.choice(user_attr_val[sub_attr_key])
                # print(f"Subject Attribute Value: {sub_attr_val_n}")
                if curr_sub_attrs.get(sub_attr_key) == None:
                    cnt += 1
                    curr_sub_attrs[sub_attr_key] = sub_attr_val_n
            curr_sub_attrs_list = list(curr_sub_attrs.items()).sort()
            # print(f"CURRENT LIST: {curr_sub_attrs_list}", end = ',:,')
            if curr_sub_attrs_list not in used_sub_attrs:
                flg = 0
                overall_sub_attrs.update(curr_sub_attrs)
                used_sub_attrs.append(curr_sub_attrs_list)
            # if len(used_sub_attrs) == no_of_sub_attr:
            #     flg = 0
        # print("--- STOP ---\n")
        # if i == POLICY_SIZE:
        #     for item in overall:
        #         print(item)
        print(f"RULE {i}: {overall_sub_attrs}")
        abac_policy[index]["sub"] = overall_sub_attrs
        # rule_combination = (sub_attr, obj_attr, env_attr,
        #                     sub_val, obj_val, env_val)
        # # Ensure the rule combination is unique
        # if rule_combination not in used_rule_combinations:
        #     used_rule_combinations.add(rule_combination)

        #     index = "rule_" + str(len(abac_policy) + 1)
        #     abac_policy[index] = {
        #         "sub": {sub_attr: sub_val},
        #         "obj": {obj_attr: obj_val},
        #         "env": {env_attr: env_val},
        #         "operation": access_attr[random.randint(0, 3)]
        #     }
    #     # Assign 'sub' key with at least one distinct attribute-value pair
    #     sub_attr = random.choice(list(user_attr_val.keys()))
    #     sub_val = random.choice(user_attr_val[sub_attr])
    #     abac_policy[index]["sub"] = {sub_attr: sub_val}
    #     used_sub_attrs.add(sub_attr)

    #     # Assign 'obj' key with at least one distinct attribute-value pair
    #     obj_attr = random.choice(list(object_attr_val.keys()))
    #     obj_val = random.choice(object_attr_val[obj_attr])
    #     abac_policy[index]["obj"] = {obj_attr: obj_val}
    #     used_obj_attrs.add(obj_attr)

    #     # Assign 'env' key with at least one distinct attribute-value pair
    #     env_attr = random.choice(list(env_attr_val.keys()))
    #     env_val = random.choice(env_attr_val[env_attr])
    #     abac_policy[index]["env"] = {env_attr: env_val}
    #     used_env_attrs.add(env_attr)

    #     # Assign 'operation' key randomly
    #     abac_policy[index]["operation"] = access_attr[random.randint(0, 3)]

    # # Make sure each attribute is used at least once in the rules
    # for attr in user_attr_val.keys():
    #     if attr not in used_sub_attrs:
    #         index = random.choice(list(abac_policy.keys()))
    #         sub_val = random.choice(user_attr_val[attr])
    #         abac_policy[index]["sub"][attr] = sub_val

    # for attr in object_attr_val.keys():
    #     if attr not in used_obj_attrs:
    #         index = random.choice(list(abac_policy.keys()))
    #         obj_val = random.choice(object_attr_val[attr])
    #         abac_policy[index]["obj"][attr] = obj_val

    # for attr in env_attr_val.keys():
    #     if attr not in used_env_attrs:
    #         index = random.choice(list(abac_policy.keys()))
    #         env_val = random.choice(env_attr_val[attr])
    #         abac_policy[index]["env"][attr] = env_val

    # Print the generated ABAC policy
    # print("\nGenerated ABAC Policy:")
    # with open("database/policy.json", "w") as db:
    #     json.dump(abac_policy, db)
    # print(abac_policy)
    '''


def extractRefinedPolicy():
    blockOfRefinedCode = []
    flg = 0
    # Iterate through each line in the file
    with open('refined_policy.abac', 'r') as file:
        for line in file:
            # Process the current line (e.g., print it)

            line = line.strip(' \n')
            # print(line)
            if line == '':
                continue
            if line == 'OUTPUT RULES':
                flg = 1
                print(line)
                continue
            if flg == 1:
                print(line)
                if line.startswith('='):
                    break
                blockOfRefinedCode.append(line)
    print(blockOfRefinedCode)

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

            new_line = line.split('(')
            attributes_list = new_line[1].split(';')
            for i in range(len(attributes_list)):
                attr_val = attributes_list[i].split(',')
                if i == 0:
                    # Subject attributes
                    for x in attr_val:
                        x = x.strip()
                        if x == "":
                            continue
                        temp_list = x.split('in')
                        temp_list[0] = temp_list[0].strip()
                        if temp_list[0] == '':
                            continue
                        temp_list[1] = temp_list[1].strip()
                        modified_rules[rule_ID]['sub'][temp_list[0]
                                                       ] = temp_list[1][1:-1]

                elif i == 1:
                    # Object attributes
                    for x in attr_val:
                        x = x.strip(' \n')
                        temp_list = x.split('in')
                        temp_list[0] = temp_list[0].strip()
                        if temp_list[0] == '':
                            continue
                        temp_list[1] = temp_list[1].strip()
                        modified_rules[rule_ID]['obj'][temp_list[0]
                                                       ] = temp_list[1][1:-1]

                elif i == 2:
                    # Allowed operation
                    modified_rules[rule_ID]['op'] = attr_val[0].strip()[
                        1:-1].strip()
                    # for x in attr_val:
                    #     x = x.strip(' \n')
                    #     temp_list = x.split('in')
                    #     temp_list[0] = temp_list[0].strip()
                    #     temp_list[1] = temp_list[1].strip()
                    #     modified_rules[rule_ID]['o'][temp_list[0]
                    #                                    ] = temp_list[1][1:-1]
    print(f"\n\n------------------------- Modified ABAC Policy -------------------------\n")
    # print(modified_rules)
    print(f"Refined number of rules = {no_of_rules}")
    with open("database/refined_policy.json", "w") as db:
        json.dump(modified_rules, db)

def generateACM():
    pass

def generateACM_modified():
    pass


if __name__ == "__main__":
    print("+" * 45, end='')
    print(" Welcome to the ABAC Policy Generator ", end='')
    print("+" * 45)
    # print("\n\n\n")
    # preprocess()
    # convertACLtoPolicy()
    extractRefinedPolicy()

    # Create a queue Q which will store access requests 
