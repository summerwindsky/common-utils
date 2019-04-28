

#!/bin/bash

servers='idc-rhcluster2-65 idc-rhcluster2-66 idc-rhcluster2-67 idc-rhcluster2-68'

resource_dir=/home/zk/tomcat/*/server.xml
target_dir=/home/zk/server_conf

for nim in $servers
do
	scp -r find_scp.sh root@$nim:/home/zk
echo scp文件到$nim
done

for nim in $servers
do
    ssh -T $nim <<EOF
		mkdir /home/zk/server_conf
		/home/zk/find_scp.sh
EOF
done

sleep 5

for nim in $servers
do
    ssh -T $nim <<EOF
		rm -rf /home/zk/find_scp.sh
		rm -rf /home/zk/server_conf
EOF
done

