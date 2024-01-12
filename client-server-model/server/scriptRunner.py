import os, subprocess

os.environ['PATH'] = 'C:/Program Files/Java/jdk-15.0.1/bin' + os.environ['PATH']

bash_command = "./bash_script.sh"

# Run the bash script
subprocess.run(["bash", bash_command])