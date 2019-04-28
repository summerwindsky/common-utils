#!/bin/bash

servers='idc-rhcluster2-65 idc-rhcluster2-66 idc-rhcluster2-67 idc-rhcluster2-68'

#建目录
for nim in $servers
do
    ssh -T $nim <<EOF
        /home/elasticsearch/elasticsearch-5.6.9/es.sh stop all
EOF
done



