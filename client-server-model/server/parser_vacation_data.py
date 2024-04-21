file_path = './experimental_data/access_req_stats.txt'

with open(file_path, 'r') as file:
    file_contents = file.read()

jobs_list = file_contents.split("\n\n")


avg_wt_time_list = []
avg_access_res_time_list = []

for i in range(0, len(jobs_list), 2):
    
    job_list = jobs_list[i]
    # print(f"no. of jobs: {len(job_list)}")
    wt_time_list = []
    acces_res_list = []
    
    if(len(job_list) != 0):
        
        jobs = job_list.split("\n")
        
        for job in jobs:
            
            entities = job.split(" | ")
            # print(f"entities: {entities}")
            if len(entities) > 1:
                # entities = [entity[1].split(" = ") for entity in entities]
                # print(entities[1].split(" = ")[1])
                acces_res_list.append(float(entities[1].split(" = ")[1]))
            # if(entities[0] == 'Job'):
            #     wt_time_list.append(float(entities[5]))
        # if len(acces_res_list) != 0:  
        avg_access_res_time_list.append(sum(acces_res_list) / len(acces_res_list))
        # else:
        #     avg_access_res_time_list.append(0)
        
for ele in avg_access_res_time_list:
    print(f"average access res time: {ele}")