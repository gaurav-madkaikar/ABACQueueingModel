import os
import subprocess


path = os.getcwd()
print(path)

# Set the PATH environment variable to include the location of Java
os.environ['PATH'] = 'C:/Program Files/Java/jdk-15.0.1/bin' + os.environ['PATH']
# print(os.environ['PATH'])

# Command to run the Bash script
bash_command = "./bash_script.sh"

# Run the Bash script using Git Bash
subprocess.run(["bash", bash_command])
