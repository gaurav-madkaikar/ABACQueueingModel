# Configurations
| Configuration  | No. of attributes | AL Update Rate | AR Arrival Rate | Initial Policy Size |
|---------------|-------------------|----------------|-----------------|---------------------|
| C1            |  4                | 20             | 150             |  500                |
| C2            |  6                | 15             |  80             | 1000                |
| C3            |  8                |  7             |  20             | 5000                |
| C4            | 10                |  2             |   5             | 8000                |

| Configuration  | No. of attributes | No. of subjects | No. of objects | Policy Size |
|---------------|-------------------|-----------------|----------------|-------------|
| C1            |  4                |  35             |  35            | 578         |
| C2            |  6                |  45             |  45            | 1027        |
| C3            |  8                |  100            |  100           | 4935        |
| C4            | 10                |  125            |  125           | 7821        |

# Scripts

## 1. Generate Data

**Script:** [gen_test_data.py](./client-server-model/server/gen_test_data.py)

### Description
```bash
python gen_test_data.py --help
usage: gen_test_data.py [-h] [-a ATTRIBUTES] [-v VALUES] [-s SUBJECTS] [-o OBJECTS]

options:
  -h, --help            show this help message and exit
  -a ATTRIBUTES, --attributes ATTRIBUTES
                        Number of attributes
  -v VALUES, --values VALUES
                        Number of values
  -s SUBJECTS, --subjects SUBJECTS
                        Number of subjects
  -o OBJECTS, --objects OBJECTS
                        Number of objects
```

### Example
```bash
python gen_test_data.py -a 4 -s 35 -o 35
```

## 2. Run Client

**Script:** [AR_client.py](./client-server-model/client/AR_client.py)

### Description
```bash
python AR_client.py --help
usage: AR_client.py [-h] [-a ARRIVAL_RATE]

options:
  -h, --help            show this help message and exit
  -a ARRIVAL_RATE, --arrival_rate ARRIVAL_RATE
                        Mean arrival rate of access requests
```

### Example
```bash
python AR_client.py -a 150
```

## 3. Run Server

**Script:** [main_server.py](./client-server-model/server/main_server.py)

### Description
```bash
python main_server.py --help
usage: main_server.py [-h] [-a AL_UPDATE_RATE] [-v VACATION_MODEL] [-mal MAX_AUX_LIST_LEN] [-mar MAX_ACCESS_REQUESTS]
                      [-mv MAX_NO_OF_VACATIONS]

options:
  -h, --help            show this help message and exit
  -a AL_UPDATE_RATE, --al_update_rate AL_UPDATE_RATE
                        Auxiliary list update rate
  -v VACATION_MODEL, --vacation_model VACATION_MODEL
                        Vacation model: 1: Access Queue - The server goes on vacation when the access request queue is empty
                        2: Access Served - The server goes on vacation after serving a certain number of access requests 3:
                        Aux List - The server goes on vacation when the auxiliary list reaches a certain length
  -mal MAX_AUX_LIST_LEN, --max_aux_list_len MAX_AUX_LIST_LEN
                        Maximum length of the auxiliary list per vacation
  -mar MAX_ACCESS_REQUESTS, --max_access_requests MAX_ACCESS_REQUESTS
                        Maximum number of access requests per vacation
  -mv MAX_NO_OF_VACATIONS, --max_no_of_vacations MAX_NO_OF_VACATIONS
                        Maximum number of vacations
```

### Example
```bash
python main_server.py -a 20 -v 1
```

## 4. Runner Script