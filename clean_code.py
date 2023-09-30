# Script to generate our ABAC System
import string
import random
import json
import time

abac_policy = dict()
NO_OF_ATTRIBUTES = 5
NO_OF_VALUES = 3
MAX_DUPLICATES = 3

# Generate user and object attributes
def genComponentSet(NO_OF_COMPONENTS, user_attr, user_attr_val, init_str="sub_"):
    # Set a different random seed
    random.seed(time.time())
    global abac_policy, NO_OF_ATTRIBUTES, NO_OF_VALUES, MAX_DUPLICATES
    user_info = {}
    used_users = []
    unique_count = 0

    for i in range(1, NO_OF_COMPONENTS + 1):
        flg = 1
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

    if init_str == "sub_":
        print(f"Total Users = {NO_OF_COMPONENTS}")
        print(f"Total unique users = {unique_count}")
    elif init_str == "obj_":
        print(f"Total Objects = {NO_OF_COMPONENTS}")
        print(f"Total unique objects = {unique_count}")

    return user_info


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

    user_attr_val = {}
    object_attr_val = {}
    env_attr_val = {}
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

    print("Environment Variables")
    print(env_attr)

    print("\nUser attribute-value pairs:")
    print(user_attr_val)

    print("\nObject attribute-value pairs:")
    print(object_attr_val)

    print("\n\n")

    print("+++" * 15 + "   Generating set of subjects   " + "+++" * 15)
    NO_OF_SUBJECTS = 200
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
    
    with open("database/userbase.json", "w") as db:
        json.dump(user_info, db)

    print("\n\n")

    print("+++" * 15 + "   Generating set of objects    " + "+++" * 15)
    NO_OF_OBJECTS = 200
    
    '''
        # obj_info = {
        #     "obj_i": {
        #         attr_1: val_1,
        #         attr_2: val_2,
        #         ...
        #         attr_k: val_k
        #     }
        # }
    '''
    obj_info = genComponentSet(NO_OF_OBJECTS, object_attr, object_attr_val, "obj_")
    # Write the object set into the specififed JSON file
    with open("database/objectbase.json", "w") as db:
        json.dump(obj_info, db)

    print("\n\n")

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
    print(f"------------ Unique user attr-val pairs = {len(unique_user_attr_dict)} ------------")
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
            no_of_usr_attr = random.randint(0, len(unique_user_attr_dict[each_usr]))
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

    print(f"Number of rules = {len(abac_policy)}")
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
    file_ptr = open("policy.abac", "w")

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
        for obj_keys in obj_tmp:
            if obj_tmp[obj_keys] == '*':
                continue
            else:
                flg2 = True
                write_str += obj_keys + " in {" + obj_tmp[obj_keys] + "}, "
        if flg2:
            write_str = write_str[:-2]
        write_str += "; "

        write_str += "{" + operation + "}; "
        write_str += ")\n"
        file_ptr.write(write_str)

    file_ptr.close()

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



if __name__ == "__main__":
    print("+" * 45, end='')
    print(" Welcome to the ABAC Policy Generator ", end='')
    print("+" * 45)
    print("\n\n\n")
    preprocess()

'''
8 
FEDCBECD 
FABBGACG 
CDEDGAEC
BFFEGGBA
FCEEAFDA
AGFADEAC
ADGDCBAA
EAABDDFF

BCDCB


'''