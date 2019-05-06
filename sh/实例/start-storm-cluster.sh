#!/bin/bash
#nimbus节点
nimbusServers='node4 node5'

#supervisor节点
supervisorServers='node3 node4 node5'

#logviewer
logviewerNodes='node3 node4 node5'

#启动所有的nimbus
for nim in $nimbusServers
do
    ssh -T $nim <<EOF
        source /etc/profile
        cd \$STORM_HOME
        bin/storm nimbus >/dev/null 2>&1 &
EOF
echo 从节点 $nim 启动nimbus...[ done ]
done

#启动所有的ui
for u in $nimbusServers
do
    ssh -T $u <<EOF
        source /etc/profile
        cd \$STORM_HOME
        bin/storm ui >/dev/null 2>&1 &
EOF
echo 从节点 $u 启动ui...[ done ]
done

#启动所有的supervisor
for visor in $supervisorServers
do
    ssh -T $visor <<EOF
        source /etc/profile
        cd \$STORM_HOME
        bin/storm supervisor >/dev/null 2>&1 &
EOF
echo 从节点 $visor 启动supervisor...[ done ]
done


#启动所有的logviewer
for node in $logviewerNodes
do
    ssh -T $node <<EOF
        source /etc/profile
        cd \$STORM_HOME
        bin/storm logviewer >/dev/null 2>&1 &
EOF
echo 从节点 $node 启动logviewer...[ done ]
done
