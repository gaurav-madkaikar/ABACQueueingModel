from subprocess import run
from sys import argv
from os import getcwd, environ, kill
from pathlib import Path
from threading import Thread
from time import sleep
from subprocess import Popen

BASE_DIR = Path(__file__).resolve().parent.parent


SERVER = BASE_DIR / 'client-server-model' / 'server' / 'main_server.py'
CLIENT = BASE_DIR / 'client-server-model' / 'client' / 'AR_client.py'

def run_server():
    run(args=['run', str(SERVER)])

def run_client():
    run(args=['run', str(CLIENT)])


def main():
    server_process = None
    client_process = None
    try:
        server_process = Popen(['run', str(SERVER), '-a', '150'])
        client_process = Popen(['run', str(CLIENT), '-a', '20'])
        server_process.wait()
        client_process.wait()
    except Exception as e:
        print(e)
        if server_process: server_process.kill()
        if client_process: client_process.kill()
    finally:
        if server_process: server_process.kill()
        if client_process: client_process.kill()
    


if __name__ == '__main__':
    main()