#!/bin/bash

servers='idc-rhcluster2-65 idc-rhcluster2-66 idc-rhcluster2-67 idc-rhcluster2-68'
#map 映射关系
declare -A servers_map=(["59"]="65" ["60"]="66" ["62"]="67" ["63"]="68")

#建目录

        for num in `seq 1 2`
        do
                path='/home/elasticsearch/elasticsearch-5.6.9/es'${num}'/config/elasticsearch.yml'
                jvm_path='/home/elasticsearch/elasticsearch-5.6.9/es'${num}'/config/jvm.options'
                sed -i "s/HYYD_5_6_SSD_1/HYYD_5_6_SSD_2/g" $path
                sed -i "s/-Xms30g/-Xms10g/g" ${jvm_path}
                sed -i "s/-Xmx30g/-Xmx30g/g" ${jvm_path}
                #sed -i "s/17953/17954/g" ${path}
                #sed -i "s/16953/16954/g" ${path}
		sed -i 's@'\"'idc-rhcluster2-65:17954'\"','\"'idc-rhcluster2-65:17954'\"','\"'idc-rhcluster2-66:17954'\"','\"'idc-rhcluster2-66:17954'\"','\"'idc-rhcluster2-67:17954'\"','\"'idc-rhcluster2-67:17954'\"','\"'idc-rhcluster2-68:17954'\"','\"'idc-rhcluster2-68:17954'\"'@'\"'idc-rhcluster2-65:17843'\"','\"'idc-rhcluster2-65:17844'\"','\"'idc-rhcluster2-66:17843'\"','\"'idc-rhcluster2-66:17844'\"','\"'idc-rhcluster2-67:17843'\"','\"'idc-rhcluster2-67:17844'\"','\"'idc-rhcluster2-68:17843'\"','\"'idc-rhcluster2-68:17844'\"'@g' $path
                sed -i "s@/ssd"${num}"/elasticsearch/es5.6@/esdata/data"${num}"/es5.6@g" $path
                for key in ${!servers_map[@]}
                do
                        sed -i "s/idc-rhcluster1-"${key}"/idc-rhcluster2-"${servers_map[$key]}"/g" $path
                        sed -i "s/hyyd"${key}"/hyyd"${servers_map[$key]}"/g" $path
                done
        done

echo 从节点 $nim 修改elasticsearch.yml的配置




