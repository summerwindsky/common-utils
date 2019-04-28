#!/bin/bash

servers='idc-rhcluster2-65 idc-rhcluster2-66 idc-rhcluster2-67 idc-rhcluster2-68'

#建目录
for nim in $servers
do
    ssh -T $nim <<EOF
	rm -rf /home/elasticsearch/elasticsearch-5.6.9/es1/logs/*
	rm -rf /home/elasticsearch/elasticsearch-5.6.9/es2/logs/*
EOF
echo 从节点 $nim 修改elasticsearch.yml的配置
done


