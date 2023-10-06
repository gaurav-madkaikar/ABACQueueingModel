#!/bin/bash

echo "\n------------ Running ABACMining! ---------------\n"

cd ABACMining/
 
# ./run -m ../case-studies/online-video.abac -verbose > ../refined_policy.abac
./run -m ../ori_policy.abac -verbose > ../refined_policy.abac

echo "\n------------ Process Over! ---------------"