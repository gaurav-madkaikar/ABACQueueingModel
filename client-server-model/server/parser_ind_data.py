file_path = './experimental_data/access_req_stats.txt'

with open(file_path, 'r') as file:
    file_contents = file.read()

jobs_list = file_contents.split("\n\n")

tot_avg_wait_time = 0
tot_avg_res_time = 0
cnt_of_jobs = 0

vac_durations = []
vac_start_time = []
no_of_jobs = []

for i in range(0, len(jobs_list)):
    job_list = jobs_list[i]
    wt_time_list = []

    if len(job_list) != 0:
        jobs = job_list.split("\n")
        flg = 0
        for job in jobs:
            if job.startswith("-----"):
                flg = 1
                continue
            elif job.startswith("------"):
                flg = 0
                continue
            elif flg == 1:
                if job.startswith("Start"):
                    vac_start_time.append(float(job.split(": ")[1]))
                elif job.startswith("Duration"):
                    vac_durations.append(float(job.split(": ")[1]))
                elif job.startswith("Jobs"):
                    no_of_jobs.append(int(job.split(": ")[1]))
            elif job.startswith("Job"):
                entities = job.split(" | ")
                times = [float(entities[i].split(" = ")[1]) for i in range(2)]
                tot_avg_wait_time += times[0]
                tot_avg_res_time += times[1]
                cnt_of_jobs += 1
            else:
                if job.startswith("-----"):
                    vac_durations.append(float(job.split(": ")[1]))

tot_avg_wait_time /= cnt_of_jobs
tot_avg_res_time /= cnt_of_jobs

avg_vac_duration = sum(vac_durations) / len(vac_durations)
avg_no_of_jobs = sum(no_of_jobs) // len(no_of_jobs)

print(f"Average waiting time: {tot_avg_wait_time} sec")
print(f"Average access resolution time: {tot_avg_res_time} sec")
print(f"Average vacation duration: {avg_vac_duration} sec")
print(f"Average number of jobs: {avg_no_of_jobs}")
print(f"Vacation start times: {vac_start_time}")
print(f'Vacation durations: {vac_durations}')