#!/bin/bash

root_path=/home/zk/tomcat8

servers='idc-rhcluster2-65 idc-rhcluster2-66 idc-rhcluster2-67 idc-rhcluster2-68'

tomcat8=apache-tomcat-8.5.32

unzip apache-tomcat-8.5.32.zip >> /dev/null
sed -i '109iJAVA_OPTS="-Xms1024m -Xmx3072m -Xss1024K -XX:PermSize=128m -XX:MaxPermSize=256m"' ./apache-tomcat-8.5.32/bin/catalina.sh
chmod +x ./apache-tomcat-8.5.32/bin/*

#建目录
for nim in $servers
do
    ssh -T $nim <<EOF
		rm -rf /home/zk/tomcat8
		mkdir /home/zk/tomcat8
EOF
echo 从节点 $nim 创建目录/home/zk/tomcat8
done

for nim in $servers
do
	scp -r $tomcat8 root@$nim:$root_path >> /dev/null
	scp -r change_name.sh root@$nim:$root_path >> /dev/null
echo scp文件到$nim
done

for nim in $servers
do
    ssh -T $nim <<EOF
		/home/zk/tomcat8/change_name.sh
EOF
echo 从节点 $nim 启动change_name.sh
done

sleep 10

for nim in $servers
do
    ssh -T $nim <<EOF
		rm -rf /home/zk/tomcat8/change_name.sh
		chown -R zk:zk /home/zk/tomcat8
EOF
echo 从节点 $nim 启动修改用户，删除脚本
done

