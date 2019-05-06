#!/bin/bash

JAR_SERVICE="app-collection|app-rm|app-io"
JAR_ARRAY=(`echo $JAR_SERVICE | sed -e 's/|/\n/g'`)
JAR_PATH=/root/wangyukai/app
LOG_PATH=/root/wangyukai/app/log

usage="Usage: $0 <start|stop|restart> <all|$JAR_SERVICE>"

# 打印GC详情
#JAVA_OPTS="-verbosegc"
#JAVA_OPTS="$JAVA_OPTS -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+DisableExplicitGC"
# 关闭检查
# GC overhead limt exceed检查是Hotspot VM 1.6定义的一个策略，通过统计GC时间来预测是否要OOM了，提前抛出异常，防止OOM发生。Sun 官方对此的定义是：“并行/并发回收器在GC回收时间过长时会抛出OutOfMemroyError。过长的定义是，超过98%的时间用来做GC并且回收了不到2%的堆内存。用来避免内存过小造成应用不能正常工作。“
#JAVA_OPTS="$JAVA_OPTS -XX:-UseGCOverheadLimit"
# 设置初始堆大小和最大堆大小为2G
JAVA_OPTS="$JAVA_OPTS -Xms2g -Xmx2g -XX:-HeapDumpOnOutOfMemoryError  -XX:HeapDumpPath=./java_pid<pid>.hprof"

check_status(){
    if [ $? -eq 0 ];then
        echo "$1 successful"
    else
        echo "$1 failed"
	exit 2
    fi
}

start_single_command(){
     JAR_NAME="$1.jar"
     JAR_LOG="$1.log"
     PID=`jps -l|grep $JAR_NAME |awk '{print $1}'`
     if [ "$PID" != "" ]; then
          echo "$JAR_NAME already started of pid $PID."
     else
          nohup java $JAVA_OPTS -jar $JAR_PATH/$JAR_NAME > $LOG_PATH/$JAR_LOG 2>&1 &
          check_status "$1 start"
     fi
}

stop_single_command(){
     JAR_NAME="$1.jar"
     PID=`jps -l|grep $JAR_NAME  |awk '{print $1}'`
     if [ "$PID" != "" ]; then
          echo "find $JAR_NAME of pid $PID, killing..."
          kill -9 $PID
          check_status "$1 kill"
     else
          echo "$JAR_NAME not started."
     fi
}

start(){
    if [ "$1" = "all" ];
    then
        for jar in ${JAR_ARRAY[@]}
        do
            start_single_command $jar
        done
    else
        start_single_command $1
    fi
}

stop(){
    if [ "$1" = "all" ];
    then
        for jar in ${JAR_ARRAY[@]}
        do
            stop_single_command $jar
        done
    else
        stop_single_command $1
    fi
}

case "$1" in
     start)
          start $2
     ;;
     stop)
          stop $2
     ;;
     restart)
          stop $2
          sleep 3
          start $2
     ;;
     *)
          echo ${usage}
          exit 1
esac