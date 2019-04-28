#!/bin/bash
####################################################
# 	监控第三阶段日志文件
# 	定时任务执行时间间隔：一小时
# 	一小时以上没有新日志增加，则在指定目录生成 run_data.ok 文件
#####################################################


# 监控的日志文件所在路径
LOG_TAIL_PATH=./test.log
# 确认文件所在路劲
DONE_FILE_PATH=./run_data.ok
# 上一次记录的日志时间的文件路径
LAST_TIME_PATH=./log_tail_monitor.lasttime
# 上一次记录的日志时间
LAST_TIME=
# 当前日志的时间
CURR_TIME=

# 脚本执行的日志文件
LOG_FILE=./log_tail_monitor.log

CURR_TIME=`tail -1 $LOG_TAIL_PATH | awk '{print $1" "$2}'| date -d - +%s`

if [ -s $LAST_TIME_PATH ]; then
    LAST_TIME=`head -1 $LAST_TIME_PATH`
    echo "上次日志时间"$LAST_TIME >> $LOG_FILE
	let hour=($CURR_TIME-$LAST_TIME)
	if [ "$hour" -eq  0 ]; then
		echo "程序已停止" >> $LOG_FILE
		touch $DONE_FILE_PATH
	else
		echo $CURR_TIME > $LAST_TIME_PATH
	fi
else
    echo $CURR_TIME > $LAST_TIME_PATH
    echo "首次获取日志时间:"$CURR_TIME >> $LOG_FILE
fi