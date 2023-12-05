#!/bin/bash

echo "\n------------ Running ABACMining Procedure! ---------------\n"

cd ../../ABACMining/
 
# ./run -m ../case-studies/online-video.abac -verbose > ../refined_policy.abac
./run -m ../ori_policy.abac -verbose > ../client-server-model/server/database/policy/refined_policy.abac

echo "\n------------ Mining Process Over! ---------------"