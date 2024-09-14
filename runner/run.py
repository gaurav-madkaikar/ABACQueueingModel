import os
from pathlib import Path
from subprocess import Popen

BASE_DIR = Path(__file__).resolve().parent.parent


SERVER = BASE_DIR / 'client-server-model' / 'server' / 'main_server.py'
CLIENT = BASE_DIR / 'client-server-model' / 'client' / 'AR_client.py'
DATA_GENERATOR = BASE_DIR / 'client-server-model' / 'server' / 'gen_test_data.py'
STATS = BASE_DIR / 'client-server-model' / 'server' / 'experimental_data' / 'access_req_stats.txt'
ALL_EXPERIMENTS_HISTORY_DIR = BASE_DIR / 'client-server-model' / 'experiments'
ALL_EXPERIMENTS_HISTORY_DIR.mkdir(parents=True, exist_ok=True)
EXPERIMENTS_PER_CONFIG = 1

CONFIGS = {
    'c1': {
        'al_update_rate': 20,
        'ar_arrival_rate': 150,
        'attributes': 4,
        'subjects': 35,
        'objects': 35
    },
    'c2': {
        'al_update_rate': 15,
        'ar_arrival_rate': 80,
        'attributes': 6,
        'subjects': 45,
        'objects': 45
    },
    'c3': {
        'al_update_rate': 7,
        'ar_arrival_rate': 20,
        'attributes': 8,
        'subjects': 100,
        'objects': 100
    },
    'c4': {
        'al_update_rate': 2,
        'ar_arrival_rate': 5,
        'attributes': 10,
        'subjects': 125,
        'objects': 125
    }
}

def get_new_experiment():
    for cfgkey, config in CONFIGS.items():
        for i in range(EXPERIMENTS_PER_CONFIG):
            yield f'{cfgkey}_{i+1}.txt', config

def experiment(config: dict):
    server_process = None
    client_process = None
    try:
        if not config.get('al_update_rate') or not config.get('ar_arrival_rate'):
            raise Exception('Invalid configuration')
        server_process = Popen(['run', str(SERVER), '-a', str(config.get('al_update_rate'))])
        client_process = Popen(['run', str(CLIENT), '-a', str(config.get('ar_arrival_rate'))])
        server_process.wait()
        client_process.wait()
    except Exception as e:
        print(f'[Runner] Error: {e}')
        if server_process: server_process.kill()
        if client_process: client_process.kill()
        raise e
    finally:
        if server_process: server_process.kill()
        if client_process: client_process.kill()
    
def datagen(config: dict):
    gen_data_process = None
    try:
        if not config.get('attributes') or not config.get('subjects') or not config.get('objects'):
            raise Exception('Invalid configuration')
        gen_data_process = Popen([
            'run', str(DATA_GENERATOR), 
            '-a', str(config.get('attributes')), 
            '-s', str(config.get('subjects')), 
            '-o', str(config.get('objects'))
        ])
        gen_data_process.wait()
    except Exception as e:
        print(f'[Runner] Error: {e}')
        if gen_data_process: gen_data_process.kill()
        raise e
    finally:
        if gen_data_process: gen_data_process.kill()


if __name__ == '__main__':
    for experiment_file, config in get_new_experiment():
        print(f'[Runner] Running experiment: {experiment_file.split(".")[0]}')
        datagen(config)
        experiment(config)
        os.rename(STATS, ALL_EXPERIMENTS_HISTORY_DIR / experiment_file)
        print(f'[Runner] Done running experiment: {experiment_file.split(".")[0]}')
    print('[Runner] Done running all experiments')
    