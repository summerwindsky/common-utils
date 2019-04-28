echo `hostname`
app_65='tomcat-checkHbase-6188 
		tomcat-data-hbase-7888 
		tomcat-data-hbase-dump-9998 
		tomcat-data-kafka-fy-7688 
		tomcat-data-kafka-jcy-7788 		
		tomcat-data-kafka-tag-7988 		
		tomcat-dealah-7588 		
		tomcat-df-Services-8888 		
		tomcat-WritAnalyzerServices-7088 		
		tomcat-WritAnalyzerServices-8088 		
		updateWritAnalyzerService';
if [ `hostname` = 'idc-rhcluster2-65' ]; then
	for app in $app_65
	do
		echo $app
	done
fi
