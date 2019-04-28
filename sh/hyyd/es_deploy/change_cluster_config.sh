#!/bin/bash

servers='idc-rhcluster2-65 idc-rhcluster2-66 idc-rhcluster2-67 idc-rhcluster2-68'

for nim in $servers
do
    scp config.sh elasticsearch@$nim:/home/elasticsearch/elasticsearch-5.6.9/
echo scp config.sh 到 $nim
done

#建目录
for nim in $servers
do
    ssh -T $nim <<EOF
        /home/elasticsearch/elasticsearch-5.6.9/config.sh
EOF
echo 从节点 $nim 修改elasticsearch.yml的配置
done



