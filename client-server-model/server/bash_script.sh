#!/bin/bash

cd ../../ABACMining/
pwd
./compile.sh
./run -m ../ori_policy.abac -verbose > ../client-server-model/server/database/policy/refined_policy.abac