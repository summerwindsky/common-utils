#!/bin/bash
           
#snapshot file dir
dataDir=/home/nileader/taokeeper/zk_data/version-2
#tran log dir
dataLogDir=/home/nileader/taokeeper/zk_log/version-2
#zk log dir
logDir=/home/nileader/taokeeper/logs
#Leave 60 files
count=60
count=$[$count+1]
ls -t $dataLogDir/log.* | tail -n +$count | xargs rm -f
ls -t $dataDir/snapshot.* | tail -n +$count | xargs rm -f
ls -t $logDir/zookeeper.log.* | tail -n +$count | xargs rm -f