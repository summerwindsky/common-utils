#!/bin/bash
root_path=/home/zk/tomcat8/

servers='idc-rhcluster2-65 idc-rhcluster2-66 idc-rhcluster2-67 idc-rhcluster2-68'

tomcat8=/home/zk/tomcat8/apache-tomcat-8.5.32
app_65='tomcat-data-hbase-7888
tomcat-data-kafka-fy-7688
tomcat-data-kafka-jcy-7788
tomcat-data-kafka-tag-7988
tomcat-dealah-7588
tomcat-df-Services-8888
tomcat-WritAnalyzerServices-7088
tomcat-WritAnalyzerServices-8088'	
				
app_66='tomcat-datadump-7788 
tomcat-df-Services-8888
tomcat-hbase2hbase-6088
tomcat-WritAnalyzerServices-7088
tomcat-WritAnalyzerServices-8088'

app_67='tomcat-df-Services-8888
tomcat-importCase-5088
tomcat-WritAnalyzerServices-7088
tomcat-WritAnalyzerServices-8088'

app_68='tomcat-datawashnew-7188
tomcat-WritAnalyzerServices-7088
tomcat-WritAnalyzerServices-8088'

#TODO 改成map的形式
if [ `hostname` = 'idc-rhcluster2-65' ]; then
	for app in $app_65; do
		cp -r $tomcat8 $root_path$app 
	done
fi

if [ `hostname` = 'idc-rhcluster2-66' ]; then
	for app in $app_66; do
		cp -r $tomcat8 $root_path$app
	done
fi

if [ `hostname` = 'idc-rhcluster2-67' ]; then
	for app in $app_67; do
		cp -r $tomcat8 $root_path$app
	done
fi

if [ `hostname` = 'idc-rhcluster2-68' ]; then
	for app in $app_68; do
		cp -r $tomcat8 $root_path$app
	done
fi

#修改端口
conf_dir=/home/zk/tomcat8/*/conf/server.xml
filelist=`ls $conf_dir`
for file in $filelist
do
	res_file=`echo $file | awk -F "/" '{print $(NF-2)}'`
	if [ $res_file = 'tomcat-datadump-7788' ]; then
		sed -i "s/8005/9000/g" $file
		sed -i "s/8080/7788/g" $file
		sed -i "s/8443/7440/g" $file
		sed -i "s/8009/9010/g" $file
	fi
	if [ $res_file = 'tomcat-data-hbase-7888' ]; then
		sed -i "s/8005/9005/g" $file	
		sed -i "s/8080/7888/g" $file	
		sed -i "s/8443/7448/g" $file	
		sed -i "s/8009/9019/g" $file		

	fi
	if [ $res_file = 'tomcat-data-kafka-fy-7688' ]; then
		sed -i "s/8005/7006/g" $file	
		sed -i "s/8080/7688/g" $file	
		sed -i "s/8443/7448/g" $file	
		sed -i "s/8009/7019/g" $file		

	fi
	if [ $res_file = 'tomcat-data-kafka-jcy-7788' ]; then
		sed -i "s/8005/7801/g" $file	
		sed -i "s/8080/7788/g" $file	
		sed -i "s/8443/7441/g" $file	
		sed -i "s/8009/7011/g" $file		

	fi
	if [ $res_file = 'tomcat-data-kafka-tag-7988' ]; then
		sed -i "s/8005/7009/g" $file	
		sed -i "s/8080/7988/g" $file	
		sed -i "s/8443/7499/g" $file	
		sed -i "s/8009/7019/g" $file		

	fi
	if [ $res_file = 'tomcat-datawashnew-7188' ]; then
		sed -i "s/8005/9100/g" $file	
		sed -i "s/8080/7188/g" $file	
		sed -i "s/8443/7140/g" $file	
		sed -i "s/8009/9110/g" $file		

	fi
	if [ $res_file = 'tomcat-dealah-7588' ]; then
		sed -i "s/8005/9507/g" $file	
		sed -i "s/8080/7588/g" $file	
		sed -i "s/8443/7448/g" $file	
		sed -i "s/8009/9059/g" $file		

	fi
	if [ $res_file = 'tomcat-df-Services-8888' ]; then
		sed -i "s/8005/9000/g" $file	
		sed -i "s/8080/8888/g" $file	
		sed -i "s/8443/7440/g" $file	
		sed -i "s/8009/9010/g" $file		

	fi
	if [ $res_file = 'tomcat-hbase2hbase-6088' ]; then
		sed -i "s/8005/6005/g" $file	
		sed -i "s/8080/6088/g" $file	
		sed -i "s/8443/6443/g" $file	
		sed -i "s/8009/7009/g" $file		

	fi
	if [ $res_file = 'tomcat-importCase-5088' ]; then
		sed -i "s/8005/2005/g" $file	
		sed -i "s/8080/5088/g" $file	
		sed -i "s/8443/2443/g" $file	
		sed -i "/8009/10009/g" $file		

	fi
	if [ $res_file = 'tomcat-WritAnalyzerServices-7088' ]; then
		sed -i "s/8005/8815/g" $file	
		sed -i "s/8080/7088/g" $file	
		sed -i "s/8443/8442/g" $file	
		sed -i "s/8009/8810/g" $file		

	fi
	if [ $res_file = 'tomcat-WritAnalyzerServices-8088' ]; then
		sed -i "s/8005/8816/g" $file	
		sed -i "s/8080/8088/g" $file	
		sed -i "s/8443/8443/g" $file	
		sed -i "s/8009/8820/g" $file		

	fi
done

rm -rf $tomcat8
