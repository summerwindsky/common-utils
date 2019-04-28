#!/bin/bash
servers='idc-rhcluster2-65 idc-rhcluster2-66 idc-rhcluster2-67 idc-rhcluster2-68'
#copy文件
for src in $servers
do
	scp -r $1 $2@$src:$3
EOF
echo scp $src
done

