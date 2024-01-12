file_path = './experimental_data/access_req_stats.txt'

with open(file_path, 'r') as file:
    file_contents = file.read()

jobs_list = file_contents.split("\n\n")


avg_wt_time_list = []

for i in range(0, len(jobs_list), 2):
    
    job_list = jobs_list[i]
    # print(f"no. of jobs: {len(job_list)}")
    wt_time_list = []
    
    if(len(job_list) != 0):
        
        jobs = job_list.split("\n")
        
        for job in jobs:
            
            entities = job.split(" ")
            # print(f"entities: {entities}")
            
            if(entities[0] == 'Job'):
                wt_time_list.append(float(entities[5]))
            
        avg_wt_time_list.append(sum(wt_time_list) / len(wt_time_list))
        
        
for avg_wt_time in avg_wt_time_list:
    print(f"average waiting time: {avg_wt_time}")