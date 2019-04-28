#!/bin/bash

resource_dir=/home/zk/tomcat/*/conf/server.xml
target_dir=/home/zk/server_conf/
filelist=`ls $resource_dir`
for file in $filelist
do
	res_file=`echo $file | awk -F "/" '{print $(NF-2)}'`
	echo $res_file
	echo $file
	cp $file $target_dir$res_file".config.xml"
done

scp -r $target_dir/* root@idc-rhcluster2-65:/home/zk/server_conf_all