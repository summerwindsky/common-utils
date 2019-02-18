#脚本名：http 任务工作流调度工具
#功能：
#    1、按配置顺序，执行url对应的任务

#存在问题
#    1、未完成，未验证 2019-02-18
#!/bin/bash
JAR_SERVICE="main|fy|fg|lsls|qwal|ptal"
JAR_ARRAY=(`echo $JAR_SERVICE | sed -e 's/|/\n/g'`)
usage="Usage: $0 <start> <all|$JAR_SERVICE>"

HOST=localhost:7588
declare -A servers_map=(
["main"]="curl http://$HOST/lore-relation/AyYsFtStart"
["ay"]="curl http://$HOST/lore-relation/ay/start"
["ys"]="curl http://$HOST/lore-relation/ys/start"
["ft"]="curl http://$HOST/lore-relation/ft/saveFt"
["ayft"]="curl http://$HOST/lore-relation/ft/saveAyFt"
["fg"]="curl http://$HOST/lore-relation/fg/start"
["fy"]="curl http://$HOST/lore-relation/fy/start"
["lsls"]="curl http://$HOST/lore-relation/lsls/start"
["ptal"]="curl http://$HOST/lore-relation/ptal/start"
["qwal"]="curl http://$HOST/lore-relation/qwal/scanCase"
)
#for key in ${!servers_map[@]}
#do
#        echo ${key} ${servers_map[$key]}
#done
check_status(){
    if [ $? -eq 0 ];then
        echo "$1 successful"
    else
        echo "$1 failed"
	exit 2
    fi
}

start_single_command(){
  echo "执行 ${servers_map[$1]}"
  ${servers_map[$1]}
  check_status "$1 run"
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

case "$1" in
     start)
          start $2
     ;;
     *)
          echo ${usage}
          exit 1
esac
