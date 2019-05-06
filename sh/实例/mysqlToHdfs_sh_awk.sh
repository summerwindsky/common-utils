#!/bin/sh
##############################################################
#Author:
#Date:2016-10-26
#Describe:从mysql中导出数据到hdfs和hbase中
#############################################################
#要执行的SQL语句
sql="select * from mysql.user";
#数据库用户名
DB_USER="root";
#数据库地址
DB_IP="192.168.3.21";
#数据库密码
DB_PASSWD="root";
#数据库名称
DB_NAME="idc_smms_run";
#获取昨天日期
date=$(date -d yesterday +%Y-%m-%d);
#要上传到hdfs的文件名
file="url"$date".txt";
#上传到hdfs的地址
path="/";

#从mysql中读取昨天的数据记录
echo "[INFO] starting read now data from mysql ..."
ret=$(mysql -u$DB_USER -h ${DB_IP} -p${DB_PASSWD} $DB_NAME -e "$sql"|awk 'NR>1' |awk '{print "http://"$0"\n"}');
#将数据结果存到文件中
echo $ret  >> $file;

#定义变量用来执行hadoop或者hbase命令
HADOOP=$(which hadoop);
HBASE=$(which hbase);

#如果hdfs上传目录不存在就创建目录
$HADOOP fs -test -e $path
	if [ $? -ne 0 ]; then
		echo "[WARNING] $path is not exists !"
		echo "[INFO] making now $path"
		$HADOOP fs -mkdir -p $path
		if [ $? -eq 0 ]; then
			echo "[INFO] making hdfs $path successful"
		else
			echo "[ERROR] making hdfs $path error"
			exit 212
		fi
	else
		echo "[INFO] the hdfs $path is over, it is no empty"
	fi
#将文件上传到hdfs中
echo "[INFO] starting put now $file to hdfs $path ......"
$HADOOP fs -put $file  $path
	if [ $? -eq 0 ]; then
		echo "[INFO] puting $file to hdfs $path is successful"
	else
		echo "[ERROR] puting $file to hdfs $path error !"
		exit 213 
	fi