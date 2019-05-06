#!/bin/bash
datadir="/data/test/testdata"
svc=(a100 b200 c300 d400 e500 f600 g700 h800 i900 j1000 k1100)
for((i=0;i<${#svc[*]};i++))
do
mkdir -p $datadir/${svc[$i]}/{bin,conf,log}
done