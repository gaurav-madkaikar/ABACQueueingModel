import json
from test_poltree.poltree import NPolTree
from test_poltree.poltree import resolveAccessRequestfromPolicy  # Assuming your slow function is in a separate file
from time import perf_counter as pf
from time import perf_counter_ns as pfn

print("Reading policy.json")
with open('policy.json', 'r') as f:
    policy_str = f.read()

print("Converting policy to dictionary")
policy = json.loads(policy_str)

print("Initializing faster policy tree")
pol_tree = NPolTree(policy_str)
print("Node count: ", pol_tree.node_count)
print("Allow count: ", pol_tree.allow_count)
print("Deny count: ", pol_tree.deny_count)

print("Reading access_request.txt")
access_requests = []
with open('access_request.txt', 'r') as f:
    for line in f:
        if line.strip():
            access_requests.append(eval(line.strip()))


def test_access_resolution():
    passed = 0
    failed = 0
    fast_access_resolution_times = []
    slow_access_resolution_times = []
    
    for i, access_request in enumerate(access_requests):
        slow_time = pfn()
        slow_result = resolveAccessRequestfromPolicy(access_request, policy)
        slow_time = pfn() - slow_time
        
        slow_access_resolution_times.append(slow_time)
        
        fast_time = pfn()
        fast_result = pol_tree.resolve(access_request)
        fast_time = pfn() - fast_time
        
        fast_access_resolution_times.append(fast_time)
        
        # print(f"Test {i+1}")
        # print(f"Access Resolution Time - Slow: {slow_time} ns, Fast: {fast_time} ns")
        
        # Normalize fast_result for comparison (Allow/Deny to 1/0)
        fast_result_normalized = 1 if fast_result == "Allow" else 0
        
        if slow_result == fast_result_normalized:
            passed += 1
        else:
            failed += 1
            
            print(f"Test {i+1} failed")
            print(f"Access Request: {access_request}")
            print(f"Expected: {slow_result}")
            print(f"Got: {fast_result_normalized}")
            print()
    
    print(f"Total Passed: {passed}")
    print(f"Total Failed: {failed}")
    
    print("Average Access Resolution Time - Slow: ", sum(slow_access_resolution_times)/len(slow_access_resolution_times), "ns")
    print("Average Access Resolution Time - Fast: ", sum(fast_access_resolution_times)/len(fast_access_resolution_times), "ns")
    exit(0)

if __name__ == "__main__":
    test_access_resolution()