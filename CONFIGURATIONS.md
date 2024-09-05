# Configuration commands
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


## Configuration 1
```bash
run gen_test_data.py -a 4 -s 35 -o 35
```

## Configuration 2
```bash
run gen_test_data.py -a 6 -s 45 -o 45
```

## Configuration 3
```bash
run gen_test_data.py -a 8 -s 100 -o 100
```

## Configuration 4
```bash
run gen_test_data.py -a 10 -s 125 -o 125
```




