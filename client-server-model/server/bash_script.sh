#!/bin/bash

cd ../../ABACMining/
./compile.sh
./run -m ../ori_policy.abac -verbose > ../client-server-model/server/database/policy/refined_policy.abac