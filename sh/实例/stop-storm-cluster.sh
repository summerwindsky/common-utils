#!/bin/bash
#nimbus节点
nimbusServers='node4 node5'

#supervisor节点
supervisorServers='node3 node4 node5'

allNodes='node3 node4 node5'

#停止所有的nimbus和ui
for nim in $nimbusServers
do
	ssh $nim "ps -ef|grep Ddaemon.name=nimbus|grep -v grep|awk '{ print $2}'| xargs kill -9"  >/dev/null 2&>1 &
	echo 从节点 $nim 停止nimbus...[ done ]
	ssh $nim "ps -ef|grep Ddaemon.name=ui|grep -v grep|awk '{ print $2}'| xargs kill -9"  >/dev/null 2&>1 &
	echo 从节点 $nim 停止ui...[ done ]
done

#停止所有的supervisor
for visor in $supervisorServers
do
	ssh $visor "ps -ef|grep Ddaemon.name=supervisor|grep -v grep|awk '{ print $2}'| xargs kill -9"  >/dev/null 2&>1 &
	echo 从节点 $visor 停止supervisor...[ done ]
done

#停止所有的logviewer.LogWriter.worker
for node in $allNodes
do
	ssh $node "ps -ef|grep Ddaemon.name=logviewer|grep -v grep|awk '{ print $2}'| xargs kill -9"  >/dev/null 2&>1 &
	echo 从节点 $node 停止logviewer...[ done ]
	ssh $node "ps -ef|grep worker|grep -v grep|awk '{ print $2}'| xargs kill -9"  >/dev/null 2&>1 &
	echo 从节点 $node 停止worker...[ done ]
	ssh $node "ps -ef|grep LogWriter|grep -v grep|awk '{ print $2}'| xargs kill -9"  >/dev/null 2&>1 &
	echo 从节点 $node 停止LogWriter...[ done ]
done
