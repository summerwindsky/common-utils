#脚本名：neo4j 图数据库 初始数据批量导入工具
#功能：
#    1、将phoenix中的数据导入neo4j
#        source：phoenix
#        sink：  neo4j
#    2、描述
#        1）、通过pig将phoenix中数据导入到hdfs，生成csv文件
#        2）、将hdfs中的数据到出文件
#        3）、通过neo4j-import 工具，将文件数据导入到neo4j

#!/bin/bash
usage="Usage: $0 <runall|> <date>"
#表的日期后缀
date=$2
#入neo4j 节点命令拼接
nodes_relationships=""

#配置文件路径
table_conf_path=./all_tables.txt
header_conf_path=./all_header.txt
index_conf_path=./all_index.txt
header_file=./header/zksj

#pig脚本路径
pig_script_path=.
nodes_pig_file=$pig_script_path/nodes.pig
relationships_pig_file=$pig_script_path/relationships.pig

#集群
#PIG=/home/hadoopadmin/pig-0.17.0/bin
#HADOOP=/home/hadoopadmin/hadoop-2.7.1/bin/
#NEO4J_HOME=/home/graphdb/neo4j-community-3.4.5/
#PHOENIX_HOME=/home/hadoopadmin/apache-phoenix-4.9.0-HBase-1.2-bin
PIG=/home/hadoopadmin/pig/bin
HADOOP=/home/hadoopadmin/hadoop-2.7.1/bin/
#65
#NEO4J_HOME=/home/hadoopadmin/neo4j/
#NEO4J_HOME=/home/graphdb/neo4j/
NEO4J_HOME=/home/graphdb/neo4j-community-3.4.5/
PHOENIX_HOME=/home/hadoopadmin/phoenix-4.9.0-HBase-1.2

NEO4J=$NEO4J_HOME/bin
NEO4J_GRAPHDB=$NEO4J_HOME/data/databases/graph.db

PHOENIX_JAR=$PHOENIX_HOME/phoenix-4.9.0-HBase-1.2-client.jar

ZOOKEEPER_CLUSTER=idc-rhcluster2-65,idc-rhcluster2-66,idc-rhcluster2-67:2181
#NEO4J_HOST=192.168.99.80
#NEO4J_HOST=192.168.99.65
#ZOOKEEPER_CLUSTER=172.16.124.7,172.16.124.8,172.16.124.9:2181
NEO4J_HOST=172.16.124.8
NEO4J_USER_HOST=graphdb@$NEO4J_HOST
#NEO4J_USER_HOST=hadoopadmin@$NEO4J_HOST
CREATE_INDEX_CMD=$NEO4J/cypher-shell
NEO4J_USER=neo4j
NEO4J_PWD=rhtp

#日志文件
LOG_DIR=./phoenix_to_neo4j.log


build_conf () {


#if [ ! -n "$date" -o ! -n "$file_path" ];then
if [ ! -n "$date" ];then
        echo "输入参数为空! usage: $0 date file_path"
        exit 2
fi
#0、清除历史数据
#1)、hdfs
cat $table_conf_path | while read line
do
        LAB_NAME=`echo $line|awk -F "|" '{print $1}'`
        TBL_NAME=`echo $line|awk -F "|" '{print $2}'`
		TBL_COLS=`echo $line|awk -F "|" '{print $3}'`
    echo "TABLE:${line}"    >> $LOG_DIR 2>&1
	if [[ ${line} =~ "ref" ]]
	then
	    hdfs_path=/zksj/relationships/${TBL_NAME}_$date
#        if [ ! -d ${hdfs_path} ]; then
#            echo "${hdfs_path}不存在" >> $LOG_DIR
#        else
		    $HADOOP/hadoop fs -rmr  $hdfs_path  >> $LOG_DIR 2>&1
#        fi
	else
	    hdfs_path=/zksj/nodes/${TBL_NAME}_$date
#	    if [ ! -d ${hdfs_path} ]; then
#            echo "${hdfs_path}不存在" >> $LOG_DIR
#        else
	    	$HADOOP/hadoop fs -rmr  $hdfs_path    >> $LOG_DIR 2>&1
#        fi
	fi
done
	if [ $? -ne 0 ]; then
		echo "############ 清除 hdfs fail ############"     >> $LOG_DIR 2>&1
	else
		echo "############ 清除 hdfs success ############"    >> $LOG_DIR 2>&1
	fi
#一、准备header文件
rm -rf $header_file/*.csv
mkdir -p $header_file
for line in `cat $header_conf_path`
do
	    FILE_NAME=`echo $line|awk -F "###" '{print $1}'`
          CONTENT=`echo $line|awk -F "###" '{print $2}'`
		  echo "${CONTENT}" >> $header_file/$FILE_NAME
		  echo "生成header文件：${header_file}/${FILE_NAME} " >> $LOG_DIR
done

#date;sleep 2m;date

#二、生成pig脚本，neo4j 命令
rm -rf $pig_script_path/relationships.pig $pig_script_path/nodes.pig
echo "删除pig脚本：$pig_script_path/relationships.pig $pig_script_path/nodes.pig" >> $LOG_DIR
echo "REGISTER ${PHOENIX_JAR}" >> $nodes_pig_file
echo "REGISTER ${PHOENIX_JAR}" >> $relationships_pig_file

#cat $table_conf_path | while read line 用此方法 变量nodes_relationships最终取不到值，应该是作用域的问题
for line in `cat $table_conf_path`
do
    #echo "TABLE:${line}"

        LAB_NAME=`echo $line|awk -F "|" '{print $1}'`
        TBL_NAME=`echo $line|awk -F "|" '{print $2}'`
		TBL_COLS=`echo $line|awk -F "|" '{print $3}'`

        if [[ ${TBL_NAME} =~ "ref" ]]
        then
                relationship=`echo "--relationships:${LAB_NAME} ${header_file}/${TBL_NAME}_header.csv,${header_file}/${TBL_NAME}_$date.csv "`
                nodes_relationships=`echo "${nodes_relationships} ${relationship} "`

				echo "rows = load 'hbase://query/SELECT ${TBL_COLS} FROM \"${TBL_NAME}_${date}\"' USING org.apache.phoenix.pig.PhoenixHBaseLoader('${ZOOKEEPER_CLUSTER}');
				STORE rows INTO 'hdfs://ns1/zksj/relationships/${TBL_NAME}_${date}' USING PigStorage('|');" >> $relationships_pig_file
        else
                node=`echo --nodes:${LAB_NAME} ${header_file}/${TBL_NAME}_header.csv,${header_file}/${TBL_NAME}_$date.csv `
                nodes_relationships=`echo "$nodes_relationships $node "`

				echo "rows = load 'hbase://query/SELECT ${TBL_COLS} FROM \"${TBL_NAME}_${date}\"' USING org.apache.phoenix.pig.PhoenixHBaseLoader('${ZOOKEEPER_CLUSTER}');
				STORE rows INTO 'hdfs://ns1/zksj/nodes/${TBL_NAME}_${date}' USING PigStorage('|');" >> $nodes_pig_file

        fi
done
echo "生成pig脚本文件：${nodes_pig_file} " >> $LOG_DIR
echo "生成pig脚本文件：${relationships_pig_file} " >> $LOG_DIR
echo echo $nodes_relationships >> $LOG_DIR

#exit 0
}
#date;sleep 2m;date

phoenix_cvs(){


#三、执行pig脚本
#nohup $PIG/pig -x mapreduce $relationships_pig_file >> $LOG_DIR 2>&1 &
#nohup $PIG/pig -x mapreduce $nodes_pig_file >> $LOG_DIR 2>&1 &
$PIG/pig -x mapreduce $nodes_pig_file >> $LOG_DIR 2>&1
	if [ $? -ne 0 ]; then
		echo "pig nodes fail"
		exit 3
	else
		echo "pig nodes success"
	fi
echo "执行pig脚本文件：${nodes_pig_file} " >> $LOG_DIR
$PIG/pig -x mapreduce $relationships_pig_file >> $LOG_DIR 2>&1
	if [ $? -ne 0 ]; then
		echo "pig relationships fail"
	else
		echo "pig relationships success"
	fi
echo "执行pig脚本文件：${relationships_pig_file} " >> $LOG_DIR

#date;sleep 2m;date
}

hdfs_output(){


#四、从hdfs 导出文件
cat $table_conf_path | while read line
do
        LAB_NAME=`echo $line|awk -F "|" '{print $1}'`
        TBL_NAME=`echo $line|awk -F "|" '{print $2}'`
		TBL_COLS=`echo $line|awk -F "|" '{print $3}'`
    echo "TABLE:${line}"     >> $LOG_DIR 2>&1
	if [[ ${line} =~ "ref" ]]
	then
		$HADOOP/hdfs dfs -getmerge  /zksj/relationships/${TBL_NAME}_$date/part-m*     $header_file/${TBL_NAME}_$date.csv >> $LOG_DIR 2>&1
	else
		$HADOOP/hdfs dfs -getmerge  /zksj/nodes/${TBL_NAME}_$date/part-m*     $header_file/${TBL_NAME}_$date.csv >> $LOG_DIR 2>&1
	fi
done

#date;sleep 2m;date
}

into_neo4j(){
#五、执行导入neo4j
echo $nodes_relationships >> $LOG_DIR

rm -rf ./neo4j_import_run.sh
echo LOG=`date +"%Y-%m-%d %H:%M.%S"`_run.log >> ./neo4j_import_run.sh
echo $NEO4J/neo4j stop >> ./neo4j_import_run.sh
echo rm -rf $NEO4J_GRAPHDB >> ./neo4j_import_run.sh
echo "${NEO4J}/neo4j-import --into ${NEO4J_GRAPHDB}  ${nodes_relationships} --delimiter \"|\" --multiline-fields=true --skip-bad-relationships=true --bad-tolerance=990000000 --high-io  --ignore-missing-nodes=true --ignore-extra-columns=true --quote \"'\" >> ${LOG} 2>&1" >> ./neo4j_import_run.sh

scp ./neo4j_import_run.sh $NEO4J_USER_HOST:$NEO4J
ssh $NEO4J_USER_HOST <<EOF
	sh $NEO4J/neo4j_import_run.sh
EOF

#$NEO4J/neo4j stop
#rm -rf $NEO4J_GRAPHDB

#$NEO4J/neo4j-import --into $NEO4J_GRAPHDB  $nodes_relationships --delimiter "|" --multiline-fields=true --skip-bad-relationships=true --bad-tolerance=990000000 --high-io  --ignore-missing-nodes=true --ignore-extra-columns=true --quote "'" >> $LOG_DIR 2>&1

if [ $? -ne 0 ]; then
    echo "neo4j-import fail"
else
    echo "neo4j-import success"
	$NEO4J/neo4j start
fi
}

into_neo4j_local(){
#五、执行导入neo4j 本地图数据库
echo $nodes_relationships >> $LOG_DIR

rm -rf ./neo4j_import_run.sh
echo LOG=`date +"%Y-%m-%d %H:%M.%S"`_run.log >> ./neo4j_import_run.sh
echo $NEO4J/neo4j stop >> ./neo4j_import_run.sh
#echo rm -rf $NEO4J_GRAPHDB >> ./neo4j_import_run.sh
echo "${NEO4J}/neo4j-import --into ${NEO4J_GRAPHDB}  ${nodes_relationships} --delimiter \"|\" --multiline-fields=true --skip-bad-relationships=true --bad-tolerance=990000000 --high-io  --ignore-missing-nodes=true --ignore-extra-columns=true --quote \"'\" >> ${LOG} 2>&1" >> ./neo4j_import_run.sh

#sh $NEO4J/neo4j_import_run.sh
sh ./neo4j_import_run.sh

#$NEO4J/neo4j stop
#rm -rf $NEO4J_GRAPHDB

#$NEO4J/neo4j-import --into $NEO4J_GRAPHDB  $nodes_relationships --delimiter "|" --multiline-fields=true --skip-bad-relationships=true --bad-tolerance=990000000 --high-io  --ignore-missing-nodes=true --ignore-extra-columns=true --quote "'" >> $LOG_DIR 2>&1

if [ $? -ne 0 ]; then
    echo "neo4j-import fail"
else
    echo "neo4j-import success"
	$NEO4J/neo4j start
fi
}

create_index(){
cat $index_conf_path | while read line
do
    LAB_NAME=`echo $line|awk -F "###" '{print $1}'`
    COL_NAME=`echo $line|awk -F "###" '{print $2}'`
    COLS_ARRAY=(`echo $COL_NAME | sed -e 's/|/\n/g'`)
#    awk 遍历
    echo "${line}"     >> $LOG_DIR 2>&1
    for col in $COLS_ARRAY; do
        cmd=`echo ${CREATE_INDEX_CMD} -u ${NEO4J_USER} -p ${NEO4J_PWD} "\"CREATE INDEX ON : ${LAB_NAME} (${col})\""`
        echo ${cmd}
        $cmd
    done
done
}

case "$1" in
     runall)
          build_conf
          phoenix_cvs
          hdfs_output
          into_neo4j
#          into_neo4j_local
#          create_index
     ;;
     *)
          echo ${usage}
          exit 1
esac