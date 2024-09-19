import os
from pathlib import Path
from subprocess import Popen
import shlex

BASE_DIR = Path(__file__).resolve().parent.parent


SERVER = BASE_DIR / 'client-server-model' / 'server' / 'main_server.py'
CLIENT = BASE_DIR / 'client-server-model' / 'client' / 'AR_client.py'
DATA_GENERATOR = BASE_DIR / 'client-server-model' / 'server' / 'gen_test_data.py'
STATS = BASE_DIR / 'client-server-model' / 'server' / 'experimental_data' / 'access_req_stats.txt'
ALL_EXPERIMENTS_HISTORY_DIR = BASE_DIR / 'client-server-model' / 'experiments'
ALL_EXPERIMENTS_HISTORY_DIR.mkdir(parents=True, exist_ok=True)
EXPERIMENTS_PER_CONFIG = 1

SYSTEM_CONFIGS = {
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

MODES_CONFIGS = {}
modes_count = 0
for policy_resolution in range(1, 1 + 2):
    for vacation_model in range(1, 1 + 3):
        modes_count += 1
        MODES_CONFIGS[f'm{modes_count}'] = {
            'policy_resolution': policy_resolution,
            'vacation_model': vacation_model
        }

def get_new_experiment():
    for cfgkey, config in SYSTEM_CONFIGS.items():
        for modekey, modeconfig in MODES_CONFIGS.items():
            config.update(modeconfig)
            for i in range(EXPERIMENTS_PER_CONFIG):
                yield f'{cfgkey}_{modekey}_{i}.txt', config

def experiment(server_config: dict, client_config: dict):
    server_process = None
    client_process = None
    try:
        server_process = Popen(shlex.split(f'run {SERVER} ' + ' '.join([f'--{k} {v}' for k, v in server_config.items()])))
        client_process = Popen(shlex.split(f'run {CLIENT} ' + ' '.join([f'--{k} {v}' for k, v in client_config.items()])))
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
        command = f'run {DATA_GENERATOR} ' + ' '.join([f'--{k} {v}' for k, v in config.items()])
        gen_data_process = Popen(shlex.split(command))
        gen_data_process.wait()
    except Exception as e:
        print(f'[Runner] Error: {e}')
        if gen_data_process: gen_data_process.kill()
        raise e
    finally:
        if gen_data_process: gen_data_process.kill()

def main():
    for experiment_file, config in get_new_experiment():
        print(f'[Runner] Running experiment: {experiment_file.split(".")[0]}')
        datagen({
            'attributes': config.get('attributes'),
            'subjects': config.get('subjects'),
            'objects': config.get('objects')
        })
        experiment(
            server_config={
                'al_update_rate': config.get('al_update_rate'),
                'policy_resolution': config.get('policy_resolution'),
                'vacation_model': config.get('vacation_model')
            },
            client_config={
                'ar_arrival_rate': config.get('ar_arrival_rate'),
            }
        )
        os.rename(STATS, ALL_EXPERIMENTS_HISTORY_DIR / experiment_file)
        print(f'[Runner] Done running experiment: {experiment_file.split(".")[0]}')
    print('[Runner] Done running all experiments')

if __name__ == '__main__':
    main()