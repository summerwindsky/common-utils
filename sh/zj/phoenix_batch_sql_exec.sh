#功能
#1. 可配置表
#2. 查询
#    1）、查询表数量
#    2）、查询前5条数据

# TODO
# 存在问题
#    1.每次exec都会启动一次sqlline,会话

#!/bin/bash
JAR_SERVICE="main|fy|fg|lsls|qwal|ptal"
JAR_ARRAY=(`echo $JAR_SERVICE | sed -e 's/|/\n/g'`)
usage="Usage: $0 <start> <all|$JAR_SERVICE>"

DATE=$3

HOST=localhost:7588
declare -A servers_map=(
["ay"]="t_ay t_ay_flys_ref t_ay_ref t_flys"
["ft"]="t_ay_ft_reft_ft t_ft_ft_ref t_ft_ys_ref t_law t_law_ft_ref"
["fg"]="t_fg t_fg_fy_ref t_fg_ws_ref t_fy_ws_ref"
["fy"]="t_fy t_fy_fy_ref"
["lsls"]="t_lawyer t_lawyer_ls_ref t_lawyer_ws_ref t_ls t_ls_ws_ref"
["ptal"]="t_ptal_aj t_ptal_aj_ajjc_ref t_ptal_ajjc t_ptal_ajjc_ws_ref t_ptal_flys_yssl_ref t_ptal_ws t_ptal_ws_ay_ref t_ptal_ws_yssl_ref t_ptal_ws_ft_ref t_ptal_yssl"
["qwal"]="t_qwal t_qwal_ay_ref t_qwal_flys_yssl_ref t_qwal_yssl t_qwal_yssl_ref"
)

cmd=/home/hadoopadmin/apache-phoenix-4.9.0-HBase-1.2-bin/bin/sqlline.py

select_sql="select * from \"%s\" limit 5;"
count_sql="select COUNT(*) from \"%s\";"

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
  echo "执行${servers_map[$1]}"
  SQL=''
  for i in ${servers_map[$1]}
  do
    s=`printf "${count_sql}" "${i}_${DATE}"`
    SQL=$SQL$s
  done
  SQL=(`echo $JAR_SERVICE | sed -e 's/;/;\n/g'`)
  echo $SQL
  if [  "" -ne "$SQL" ];then

    exec $cmd >>EOF
        $SQL
    EOF
  fi
  check_status "$1 run"
}

query(){
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
     query)
          query $2
     ;;
     *)
          echo ${usage}
          exit 1
esac
