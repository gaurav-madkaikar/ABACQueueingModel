# Generate test data required for conducting the experiments

import json 
import numpy as np
import string
import random
import time

NO_OF_ATTRIBUTES = 5
NO_OF_VALUES = 4
MAX_DUPLICATES_ALLOWED = 2

NO_OF_SUBJECTS = 100
NO_OF_OBJECTS = 100


# Global variables
# User/Object attributes and values
user_attr = []
object_attr = []
user_attr_val = {}
object_attr_val = {}

# User, Object sets
userbase = {}
objectbase = {}

# Stroller low-levePolicy 
policy = {}

ACM = []
sub_obj_pairs_not_taken = []


# Generate ACM corresponding to the original ABAC policy (P)
def generateACM():
    global ACM, userbase, objectbase, policy, sub_obj_pairs_not_taken


    # Policy has been loaded into original_policy
    for i in range(len(userbase)):
        key_sub = "sub_" + str(i + 1)
        usr_attr_val_dict = userbase.get(key_sub)
        access_result_list = []
        for j in range(len(objectbase)):
            key_obj = "obj_" + str(j + 1)
            obj_attr_val_dict = objectbase.get(key_obj)
            result = resolveAccessRequest(usr_attr_val_dict, obj_attr_val_dict, policy)
            if result == 0:
                sub_obj_pairs_not_taken.append([key_sub, key_obj])
                # auxList.add(key_sub, key_obj, "read")
            access_result_list.append(result)
        ACM.append(access_result_list)

    print("ACM generated successfully!")
    file_ptr = open('database/ACM/ACM_ori.txt', 'w')
    file_ptr.write('\n'.join(['   '.join([str(cell) for cell in row])
                              for row in ACM]))
    file_ptr.close()

    print(f"\n\nSubject object pairs not taken: {sub_obj_pairs_not_taken}")
    # Store these not-take sub-obj pairs in a text file
    random.shuffle(sub_obj_pairs_not_taken)
    write_str = ''
    file_ptr = open('database/aux_list/sub_obj_pairs_not_taken.txt', 'w')
    for ind in range(len(sub_obj_pairs_not_taken)):
        pair = sub_obj_pairs_not_taken[ind]
        write_str += '{' + pair[0] + ', ' + pair[1] + ', read}\n'
        if ind == len(sub_obj_pairs_not_taken) - 1:
            write_str = write_str[:-1]
    file_ptr.write(write_str)
    file_ptr.close()
        


# Generate a set of subjects or objects
def genComponentSet(NO_OF_COMPONENTS, comp_attr, comp_attr_val, init_str="sub_"):
    global abac_policy, NO_OF_ATTRIBUTES, NO_OF_VALUES, MAX_DUPLICATES_ALLOWED

    # Set a different random seed
    random.seed(time.time())

    component_info = {}
    used_components = []
    unique_count = 0

    for i in range(1, NO_OF_COMPONENTS + 1):
        flg = 1
        while flg == 1:
            unique_ID = init_str + str(i)
            component_info[unique_ID] = {}
            curr_comp_attr = {}

            for attr in comp_attr:
                attr_usr_val = comp_attr_val[attr][random.randint(
                    0, NO_OF_VALUES - 1)]
                curr_comp_attr[attr] = attr_usr_val

            curr_comp_attr_list = list(curr_comp_attr.items())
            curr_comp_attr_list.sort()
            cf = used_components.count(curr_comp_attr_list)

            if init_str == "sub_":
                curr_comp_attr["uid"] = unique_ID
            elif init_str == "obj_":
                curr_comp_attr["rid"] = unique_ID

            # At max 3 users can have the same attribute-value pairs
            # We can fine tune the distribution of users as required
            # Divide users based on similarity in a ratio of 3:2:1, i.e. 3/6 - no. of unique users, 2/6 - no. of users with max 1 more duplicates, 1/6 - no. of users with max 2 more duplicates
            if cf < MAX_DUPLICATES_ALLOWED:
                if cf == 0:
                    unique_count += 1
                used_components.append(curr_comp_attr_list)
                component_info[unique_ID].update(curr_comp_attr)
                flg = 0

    if init_str == "sub_":
        print(f"Total Users = {NO_OF_COMPONENTS}")
        print(f"Total unique users = {unique_count}")
    elif init_str == "obj_":
        print(f"Total Objects = {NO_OF_COMPONENTS}")
        print(f"Total unique objects = {unique_count}")

    return component_info

# Generate attribute-value pairs
def genAttributeValues():
    # Start timer
    t_start = time.perf_counter()

    global NO_OF_ATTRIBUTES, NO_OF_VALUES, user_attr, object_attr, user_attr_val, object_attr_val

    # Generate attributes
    for i in range(NO_OF_ATTRIBUTES):
        N = random.randint(3, 7)
        res = ''.join(random.choices(string.ascii_uppercase, k=N))
        user_attr.append(res)

    for i in range(NO_OF_ATTRIBUTES):
        N = random.randint(3, 7)
        res = ''.join(random.choices(string.ascii_uppercase, k=N))
        object_attr.append(res)

    # Generate set of values corresponding to each attribute
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

    t_end = time.perf_counter()
    print(f"1: Time required to generate attribute-value pairs = {t_end - t_start} seconds")

    print("User Attributes:")
    print(user_attr)

    print("\nObject Attributes:")
    print(object_attr)

    print("\nUser attribute-value pairs:")
    print(user_attr_val)

    print("\nObject attribute-value pairs:")
    print(object_attr_val)

    # Store the attribute-value pairs in a JSON file
    # User attribute-value pairs

    # No need
    with open("database/userbase/sub_attr.json", "w") as db:
        json.dump(user_attr, db)

    with open("database/userbase/sub_attr_val.json", "w") as db:
        json.dump(user_attr_val, db)

    # Object attribute-value pairs

    # No need
    with open("database/objectbase/obj_attr.json", "w") as db:
        json.dump(object_attr, db)

    with open("database/objectbase/obj_attr_val.json", "w") as db:
        json.dump(object_attr_val, db)
    

def genSubjectObjectSets():
    global NO_OF_OBJECTS, NO_OF_SUBJECTS, user_attr, object_attr, user_attr_val, object_attr_val, userbase, objectbase

    # Schema 1:
    # user_info = {
    #     "sub_i": {
    #         attr_1: val_1,
    #         attr_2: val_2,
    #         ...
    #         attr_k: val_k
    #     }
    # }
    t_start = time.perf_counter()
    print("+++" * 15 + "   Generating set of subjects   " + "+++" * 15)
    user_info = genComponentSet(NO_OF_SUBJECTS, user_attr, user_attr_val, "sub_")
    t_end = time.perf_counter()
    print(f"2: Time required to generate set of subjects = {t_end - t_start} seconds")
    userbase = user_info

    print("\n\n")

    # Schema 2:
    # obj_info = {
    #     "obj_i": {
    #         attr_1: val_1,
    #         attr_2: val_2,
    #         ...
    #         attr_k: val_k
    #     }
    # }
    print("+++" * 15 + "   Generating set of objects    " + "+++" * 15)
    t3_start = time.perf_counter()
    obj_info = genComponentSet(NO_OF_OBJECTS, object_attr, object_attr_val, "obj_")

    t_end = time.perf_counter()
    print(f"3: Time required to generate set of objects = {t_end - t_start} seconds")
    objectbase = obj_info
    
    # Write the subject and object set into the specififed JSON file
    with open("database/userbase/userbase.json", "w") as db:
        json.dump(user_info, db)

    with open("database/objectbase/objectbase.json", "w") as db:
        json.dump(obj_info, db)

# Resolve the access request from policy
def resolveAccessRequestfromPolicy(access_request, policy):
    # result = 0 implies access not granted, result = 1 implies access granted
    result = 0

    # Process the result
    for i in range(len(policy)):
        key = "rule_" + str(i + 1)
        rule = policy.get(key)

        sub_attr_check = 1
        for attr in access_request["sub"]:
            if access_request["sub"][attr] == ['*']:
                continue
            curr_sub_attr_check = 0
            for value in access_request["sub"][attr]:
                if value == policy["sub"][attr]:
                    curr_sub_attr_check = 1
                    break
            if curr_sub_attr_check == 0:
                sub_attr_check = 0
                break
        if sub_attr_check == 0:
            continue
        obj_attr_check = 1
        for attr in access_request["obj"]:
            if access_request["obj"][attr] == ['*']:
                continue
            curr_obj_attr_check = 0
            for values in access_request["obj"][attr]:
                if values == policy["obj"][attr]:
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

    # Return the result
    return result


def resolveAccessRequest(user_attr_val_dict, obj_attr_val_dict, policy):
    # result = 0 implies access not granted, result = 1 implies access granted
    result = 0

    # Process the result
    for i in range(len(policy)):
        key = "rule_" + str(i + 1)
        rule = policy.get(key)
        
        sub_attr_check = 1
        for attr in rule["sub"]:
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
    return result

# Generate a representation of the Access Control Policy in the form of low-level rules
def generateStrollerInput():
    global userbase, objectbase, policy, user_attr, object_attr, user_attr_val, object_attr_val
    access_attr = ["read"]
    rule_count = 0
    abac_policy = {}

    # abac_policy = {
    #     "rule_1": {
    #         sub: {attr1:val1, attr2:val2},
    #         obj: {attr3:val3},
    #         env: {attr4:val4, attr5:val5},  // Optional attribute-value pairs
    #         op: action1
    #     }
    # }
    # No. of subject attributes = s, No. of subject attribute-values = j1, j2, ..., jo
    # No. of object attributes = o, No. of object attribute-values = i1, i2, ..., io
    # No. of rules in ABAC Policy = r
    # No. of users in the system = U_sys
    # No. of objects in the system = O_sys

    t_start = time.perf_counter()

    # Include already used rules
    used_rules = []

    unique_user_attr_dict = dict()
    unique_usr_list = []
    usr_counter = 0
    for user_id in userbase:
        # Get unique user atrribute-value pairs
        temp = userbase[user_id]
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

    for each_object in objectbase:
        # Initialize the random seed
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
            while True:
                # Select some subset of attributes from the user attribute set
                # usr_attr_set = unique_user_attr_dict[each_usr]
                no_of_usr_attr = random.randint(1, len(unique_user_attr_dict[each_usr]))
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

                for curr_obj_attr in objectbase[each_object]:
                    if curr_obj_attr == "rid":
                        continue
                    random_obj_pairs[curr_obj_attr] = [
                        objectbase[each_object][curr_obj_attr]]
                    curr_rule_list.append(
                        [curr_obj_attr, objectbase[each_object][curr_obj_attr]])

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
    t_end = time.perf_counter()
    print(f"4: Time required to generate ABAC Policy = {t_end - t_start} seconds")

    print(f"\nTotal number of low-level rules: {rule_count}")
    # assert(rule_count == len(abac_policy))
    # print(f"Unique number of low-level rules: {len(used_rules)}")

    # Write the ABAC Policy set into the specififed JSON file
    with open("database/policy/policy.json", "w") as db:
        json.dump(abac_policy, db)

    # with open("database/userbase/userbase.json") as db:
    #     userbase = json.load(db)

    # with open("database/objectbase/objectbase.json") as db:
    #     objectbase = json.load(db)

    # with open("database/policy.json") as db:
    #     policybase = json.load(db)
    
    policy = abac_policy

    t_start = time.perf_counter()

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
    
    for rule_id in policy:
        write_str = "rule("
        sub_tmp = policy[rule_id]["sub"]
        obj_tmp = policy[rule_id]["obj"]
        operation = policy[rule_id]["op"]
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

    t_end = time.perf_counter()
    print(f"5: Time required to generate Stoller's input file = {t_end - t_start} seconds")



# Main function to generate the test data
if __name__ == "__main__":
    # Generate attributes and the corresponding values
    # This is immutable
    genAttributeValues()

    # Generate set of users and objects
    # This is immutable
    genSubjectObjectSets()

    # Generate a random policy according to stroller's code
    generateStrollerInput()

    # Generate ACM corresoponding to Stroller's input
    generateACM()

    # generateACM()
    print("\n\n -------- Test data successfully generated ! -------- \n\n")
