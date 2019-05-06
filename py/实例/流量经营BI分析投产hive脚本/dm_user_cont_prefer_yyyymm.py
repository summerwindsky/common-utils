#!/usr/bin/env python
# -*-coding:utf-8 -*-
#*******************************************************************************************
# **  文件名称：dm_user_cont_prefer_yyyymmtest.py
# **  功能描述：用户内容偏好模型月表
# **  输入表：  DM_USER_CONT_PREFER_BASE_YYYYMM    用户内容偏好基础信息月表                                           
# **  输出表:   DM_USER_CONT_PREFER_YYYYMM         用户内容偏好模型月表
# **           
# **  创建者:   周美金、余鹏  yupeng@info-linkage.com
# **  创建日期: 2013/09/23
# **  修改日志:
# **  修改日期: 修改人：余鹏   修改内容：加入权重配置
# ** ---------------------------------------------------------------------------------------
# **  
# ** ---------------------------------------------------------------------------------------
# **  
# **  程序调用格式：python dm_user_cont_prefer_yyyymmtest.py 201308
# **    
#********************************************************************************************
# **  Copyright(c) 2013 AsiaInfo Technologies (China), Inc. 
# **  All Rights Reserved.
#********************************************************************************************
import os,sys
from settings import *
from hqltools import *

#程序开始执行
name =  sys.argv[0][sys.argv[0].rfind(os.sep)+1:].rstrip('.py')
dates=sys.argv[1]

try:
#传递日期参数
    dicts={}
    Pama(dicts,dates)
    Start(name,dates)
#===========================================================================================
#时间参数引入
#===========================================================================================
    ARG_TODAY                 = dicts['ARG_TODAY']                    #获得yyyymmddhh格式的当前日期
    ARG_TODAY_ISO             = dicts['ARG_TODAY_ISO']                #获得yyyy-mm-dd hh格式的当前日期
    ARG_OPTIME                = dicts['ARG_OPTIME']                   #获得yyyymmdd格式的数据日期
    ARG_OPTIME_ISO            = dicts['ARG_OPTIME_ISO']               #获得yyyy-mm-dd格式的数据日期
    ARG_OPTIME_YEAR           = dicts['ARG_OPTIME_YEAR']              #获得yyyy格式的数据日期
    ARG_OPTIME_MONTH          = dicts['ARG_OPTIME_MONTH']             #获得yyyymm格式的数据日期
    ARG_OPTIME_MONTH01        = dicts['ARG_OPTIME_MONTH01']           #获得传入的数据日期的当年第1个月yyyy-mm格式的数据日期
    ARG_OPTIME_MONTH12        = dicts['ARG_OPTIME_MONTH12']           #获得传入的数据日期的当年第12个月yyyy-mm格式的数据日期
    ARG_OPTIME_HOUR           = dicts['ARG_OPTIME_HOUR']              #获得yyyymmddhh格式的数据日期
    ARG_OPTIME_HOUR_STD       = dicts['ARG_OPTIME_HOUR_STD']          #获得hh格式的数据日期
    ARG_OPTIME_DAY            = dicts['ARG_OPTIME_DAY']               #获得dd格式的数据日期
    ARG_OPTIME_THISMON        = dicts['ARG_OPTIME_THISMON']           #获得mm格式的数据日期
    ARG_OPTIME_LASTDAY        = dicts['ARG_OPTIME_LASTDAY']           #获得传入的数据日期的前一天yyyymmdd格式的数据日期
    ARG_OPTIME_LASTDAY_ISO    = dicts['ARG_OPTIME_LASTDAY_ISO']       #获得传入的数据日期的前一天yyyy-mm-dd格式的数据日期
    ARG_OPTIME_LASTMON        = dicts['ARG_OPTIME_LASTMON']           #获得传入的数据日期的上月同期日期yyyymmdd格式的数据日期
    ARG_OPTIME_LASTMON_ISO    = dicts['ARG_OPTIME_LASTMON_ISO']       #获得传入的数据日期的上月同期日期yyyy-mm-dd格式的数据日期
    ARG_OPTIME_LAST2MON       = dicts['ARG_OPTIME_LAST2MON']          #获得传入的数据日期的上2月同期日期yyyymmdd格式的数据日期
    ARG_OPTIME_LAST2MON_ISO   = dicts['ARG_OPTIME_LAST2MON_ISO']      #获得传入的数据日期的上2月同期日期yyyy-mm-dd格式的数据日期
    ARG_OPTIME_LAST3MON       = dicts['ARG_OPTIME_LAST3MON']          #获得传入的数据日期的上3月同期日期yyyymmdd格式的数据日期
    ARG_OPTIME_LAST3MON_ISO   = dicts['ARG_OPTIME_LAST3MON_ISO']      #获得传入的数据日期的上3月同期日期yyyy-mm-dd格式的数据日期
    ARG_OPTIME_LASTYEAR       = dicts['ARG_OPTIME_LASTYEAR']          #获得传入的数据日期的去年同期日期yyyymmdd格式的数据日期
    ARG_OPTIME_LASTYEAR_ISO   = dicts['ARG_OPTIME_LASTYEAR_ISO']      #获得传入的数据日期的去年同期日期yyyy-mm-dd格式的数据日期
    ARG_OPTIME_YEAR01         = dicts['ARG_OPTIME_YEAR01']            #获得传入的数据日期的当年第一天日期yyyymmdd格式的数据日期
    ARG_OPTIME_YEAR01_ISO     = dicts['ARG_OPTIME_YEAR01_ISO']        #获得传入的数据日期的当年第一天日期yyyy-mm-dd格式的数据日期
    ARG_OPTIME_YEAR12         = dicts['ARG_OPTIME_YEAR12']            #获得传入的数据日期的当年最后一天日期yyyymmdd格式的数据日期
    ARG_OPTIME_YEAR12_ISO     = dicts['ARG_OPTIME_YEAR12_ISO']        #获得传入的数据日期的当年最后一天日期yyyy-mm-dd格式的数据日期
    ARG_OPTIME_MON01          = dicts['ARG_OPTIME_MON01']             #获得传入的数据日期的本月第一天日期yyyymmdd格式的数据日期
    ARG_OPTIME_MON01_ISO      = dicts['ARG_OPTIME_MON01_ISO']         #获得传入的数据日期的本月第一天日期yyyy-mm-dd格式的数据日期
    ARG_OPTIME_LASTMON01      = dicts['ARG_OPTIME_LASTMON01']         #获得传入的数据日期的上月第一天日期yyyymmdd格式的数据日期
    ARG_OPTIME_LASTMON01_ISO  = dicts['ARG_OPTIME_LASTMON01_ISO']     #获得传入的数据日期的上月第一天日期yyyy-mm-dd格式的数据日期
    ARG_OPTIME_MONEND         = dicts['ARG_OPTIME_MONEND']            #获得传入的数据日期的本月最后一天日期yyyymmdd格式的数据日期
    ARG_OPTIME_MONEND_ISO     = dicts['ARG_OPTIME_MONEND_ISO']        #获得传入的数据日期的本月最后一天日期yyyy-mm-dd格式的数据日期
    ARG_OPTIME_LASTMONEND     = dicts['ARG_OPTIME_LASTMONEND']        #获得传入的数据日期的上月最后一天日期yyyymmdd格式的数据日期
    ARG_OPTIME_LASTMONEND_ISO = dicts['ARG_OPTIME_LASTMONEND_ISO']    #获得传入的数据日期的上月最后一天日期yyyy-mm-dd格式的数据日期
    ARG_OPTIME_LASTMONTH      = dicts['ARG_OPTIME_LASTMONTH']         #获得yyyymm格式的上月数据日期
    ARG_OPTIME_LASTMONTH_ISO  = dicts['ARG_OPTIME_LASTMONTH_ISO']     #获得yyyy-mm格式的上月数据日期
    ARG_OPTIME_LAST2MONTH     = dicts['ARG_OPTIME_LAST2MONTH']         #获得yyyymm格式的上2月数据日期
    ARG_OPTIME_LAST2MONTH_ISO = dicts['ARG_OPTIME_LAST2MONTH_ISO']     #获得yyyy-mm格式的上2月数据日期
    ARG_OPTIME_LAST3MONTH     = dicts['ARG_OPTIME_LAST3MONTH']         #获得yyyymm格式的上3月数据日期
    ARG_OPTIME_LAST3MONTH_ISO = dicts['ARG_OPTIME_LAST3MONTH_ISO']     #获得yyyy-mm格式的上3月数据日期
    ARG_OPTIME_LASTYEARMON    = dicts['ARG_OPTIME_LASTYEARMON']       #获得传入的数据日期的去年同月yyyymm格式的数据日期
    ARG_OPTIME_LASTYEARMON_ISO= dicts['ARG_OPTIME_LASTYEARMON_ISO']   #获得传入的数据日期的去年同月yyyy-mm格式的数据日期
#===========================================================================================
#其它参数引入
#===========================================================================================
#Tas_MONTH_ID             月分区键
#Tas_DAY_ID               日分区键
#Tas_HOUR_ID              小时分区键
#mindate                  最小时长
#maxdate                  最大时长
#Tas_FS                   分隔符
#term_chg_days            换机天数
#Data_No_Flag             数据未知变量
#No_Flag                  未知变量
#City_No_Flag             地市未知变量
#County_No_Flag           区县未知变量
#Section_No_Flag          片区未知变量
#Town_No_Flag             乡镇未知变量
#Chn_No_Flag     
#Brand_No_Flag            品牌未知变量
#Plan_No_Flag             
#dayofweeknum 
#City_Flag                城市
#Busi_Flag                业务
#Brand_Flag               品牌
#County_Flag              地域
#Sex_Flag                 性别
#Consume_Flag             消费层次
#Flow_Flag                流量层次
#Const_Flag               星座标识
#Age_Flag                 年龄层次
#Active_Flag              活跃
#Market_Flag              市场类别
#WLAN_SSID_Flag           WLAN 接入方式
#Net_Type_Flag            网络类型
#Apn_Flag                 GPRS接入方式
#Agree_Flag               终端是否定制
#Term_Price_Flag          终端价位区间
#Term_Level_Flag          终端档次
#No_Flow_Plan_Flag        无流量套餐标謿
#Over_Flow_Flag           流量溢出标记
#Cont_Type_Flag           内容全部变量
#Tac_Flag = -1            终端TAC
#Site_Name_Flag           网站名称
#Logic_Area_Subtype_Flag  逻辑区子类
#Logic_Area_Type_Flag     逻辑区类别
#Day_Type_Flag            全部日期类型
#All_Flag                 共用全部变量
#M_Conversion             流量转换变量(M)
#K_Conversion             流量转换变量(K)
#term_type_phone          终端类型变量
#Lib_Source               内容解析包路径
#===========================================================================================
#自定义变量声明---源表声明
#===========================================================================================
    source_dw_user_cont_prefer_base   = "dm_user_cont_prefer_base_yyyymm"
    source_dm_user_cont_imcd_yyyymmdd   = "dm_user_cont_imcd_yyyymmdd" 
    dm_user_prefer_weight_config_text = "/home/ocdc/app/hubei/setings/dm_user_prefer_weight.conf"
#===========================================================================================
#自定义变量声明---目标表声明
#===========================================================================================
    target_dm_user_cont_prefer        = "dm_user_cont_prefer_yyyymm"
    target_prefer_weight_config       = 'dm_user_cont_prefer_weight_config'
#===========================================================================================
#自定义变量声明---临时表声明
#===========================================================================================
    temp_table                        ="dm_user_cont_prefer_yyyymm_tmp"        
    temp_table_01                     ="dm_user_cont_prefer_yyyymm_tmp_01"  
    temp_table_02                     ="dm_user_cont_prefer_yyyymm_tmp_02" 
#===========================================================================
#导入权重配置
    read_file=open(r'%(dm_user_prefer_weight_config_text)s'%vars(),'r')
    mylists=[]
    for i in read_file.readlines():
    	mylists.append(i.split())
    mydicts={}
    for j in mylists:
    	mydicts[j[0]]=j[1:]
    prefer_weight_config=mydicts['%(target_prefer_weight_config)s'%vars()]
    GPRS_CNT_WEIGHT     =prefer_weight_config[0]
    GPRS_ALL_CNT_WEIGHT =prefer_weight_config[1]
    FLOW_WEIGHT         =prefer_weight_config[2]
    ALL_FLOW_WEIGHT     =prefer_weight_config[3]
    FREQ_WEIGHT         =prefer_weight_config[4]
    ALL_FREQ_WEIGHT     =prefer_weight_config[5]
    prefer_weight_config_sum=sum(map(float,prefer_weight_config))
#=========================================================================== 
#Create table 创建目标表                                                          
#=========================================================================== 
#向SQL变量赋值
    hivesql = []
    hivesql.extend(['''
create table if not exists %(temp_table)s
(
    STATIS_MONTH          STRING     --统计月份
   ,PHONE_NO              STRING     --手机号码
   ,CONT_APP_ID           BIGINT     --内容标识   
   ,GPRS_CNT              DOUBLE     --本网用户浏览某类内容次数
   ,GPRS_ALL_CNT          DOUBLE     --本网用户浏览某类内容次数
   ,FLOW                  DOUBLE     --本网用户浏览某类内容流量
   ,ALL_FLOW              DOUBLE     --本网用户浏览某类内容流量
   ,FREQ                  DOUBLE     --本网用户浏览某类内容频率
   ,ALL_FREQ              DOUBLE     --本网用户浏览全体内容频率
)
row format delimited
fields terminated by '%(Tas_FS)s'
stored as sequencefile 
''' % vars(),'''
create table if not exists %(temp_table_01)s
(
    STATIS_MONTH          STRING     --统计月份
   ,CONT_APP_ID           BIGINT     --内容标识
   ,max_GPRS_CNT          DOUBLE     --本网用户浏览某类内容次数最大值 
   ,max_GPRS_ALL_CNT      DOUBLE     --本网用户浏览某类内容次数最大值 
   ,max_FLOW              DOUBLE     --本网用户浏览某类内容流量最大值 
   ,max_all_FLOW          DOUBLE     --本网用户浏览某类内容流量最大值 
   ,max_FREQ              DOUBLE     --本网用户浏览某类内容频率最大值 
   ,max_ALL_FREQ          DOUBLE     --本网用户浏览全体内容频率最大值
   ,min_GPRS_CNT          DOUBLE     --本网用户浏览某类内容次数最小值
   ,min_GPRS_all_CNT      DOUBLE     --本网用户浏览某类内容次数最小值
   ,min_FLOW              DOUBLE     --本网用户浏览某类内容流量最小值
   ,min_all_FLOW          DOUBLE     --本网用户浏览某类内容流量最小值
   ,min_FREQ              DOUBLE     --本网用户浏览某类内容频率最小值
   ,min_ALL_FREQ          DOUBLE     --本网用户浏览全体内容频率最小值
)
row format delimited
fields terminated by '%(Tas_FS)s'
stored as sequencefile
''' % vars(),'''
create table if not exists %(temp_table_02)s
(
    STATIS_MONTH           STRING     --统计月份
    ,PHONE_NO              STRING     --手机号码
    ,CONT_APP_ID           BIGINT     --内容标识
    ,prefer                DOUBLE     --偏好度
)
row format delimited
fields terminated by '%(Tas_FS)s'
stored as sequencefile ''' % vars(),'''
create table if not exists %(target_dm_user_cont_prefer)s
(
    STATIS_MONTH          STRING      --统计月份
   ,PHONE_NO              STRING      --手机号码
   ,CONT_APP_ID           BIGINT      --内容标识
   ,prefer                DOUBLE      --偏好
   ,prefer_level          STRING      --偏好等级
)
partitioned by (%(Tas_MONTH_ID)s string)
row format delimited
fields terminated by '%(Tas_FS)s'
stored as sequencefile
''' % vars()])

#执行hivesql语句
    HiveExe(hivesql,name,dates)
#===========================================================================
# 第一步：取出用户最近三个月的指标汇总值
#===========================================================================
    hivesql = []
    hivesql.append('''
insert overwrite table %(temp_table)s
select  '%(ARG_OPTIME_MONTH)s'
        ,PHONE_NO
        ,CONT_APP_ID
        ,sum(GPRS_CNT)             as GPRS_CNT
        ,sum(GPRS_ALL_CNT)         as GPRS_ALL_CNT
        ,sum(FLOW)                 as FLOW
        ,sum(ALL_FLOW)             as ALL_FLOW 
        ,sum(nvl(FREQ,0.00))       as FREQ
        ,sum(nvl(ALL_FREQ,0.00))   as ALL_FREQ
from  %(source_dw_user_cont_prefer_base)s
where %(Tas_MONTH_ID)s in ( '%(ARG_OPTIME_LAST2MONTH)s','%(ARG_OPTIME_LASTMONTH)s', '%(ARG_OPTIME_MONTH)s')
group by  PHONE_NO
          ,CONT_APP_ID
''' % vars())

#执行hivesql语句
    HiveExe(hivesql,name,dates)
#===========================================================================
# 第二步：取出用户指标的最大最小值
#===========================================================================
    hivesql = []
    hivesql.append('''
insert overwrite table %(temp_table_01)s 
select  '%(ARG_OPTIME_MONTH)s'
         ,a.CONT_APP_ID
        ,max(case when a.GPRS_CNT>(b.avg_GPRS_CNT+3*b.stddev_GPRS_CNT)
             then null
             else a.GPRS_CNT
             end )   as max_GPRS_CNT
        ,max(case when a.GPRS_ALL_CNT>(b.avg_GPRS_ALL_CNT+3*b.stddev_GPRS_ALL_CNT)
             then null
             else a.GPRS_ALL_CNT
             end )   as max_GPRS_ALL_CNT
        ,max(case when a.FLOW>(b.avg_FLOW+3*b.stddev_FLOW)
             then null
             else a.FLOW
             end )   as max_FLOW
        ,max(case when a.ALL_FLOW>(b.avg_ALL_Flow+3*b.stddev_ALL_FLOW)
             then null
             else a.ALL_FLOW
             end )   as max_ALL_FLOW
        ,max(case when a.FREQ>(b.avg_FREQ+3*b.stddev_FREQ)
             then 0.00
             else a.FREQ
             end )   as max_FREQ
         ,max(case when a.ALL_FREQ>(b.avg_ALL_FREQ+3*b.stddev_ALL_FREQ)
             then 0.00
             else a.ALL_FREQ
             end )   as  max_ALL_FREQ
         ,min(GPRS_CNT)           as min_GPRS_CNT
         ,min(GPRS_ALL_CNT)       as min_GPRS_ALL_CNT
         ,min(FLOW)               as min_FLOW
         ,min(ALL_FLOW)           as min_ALL_FLOW
         ,min(nvl(FREQ,0.00))               as min_FREQ
         ,min(nvl(ALL_FREQ,0.00))           as min_ALL_FREQ                   
from  %(temp_table)s a
left outer join  
       (select  CONT_APP_ID
              ,avg(GPRS_CNT)                as avg_GPRS_CNT   
              ,avg(GPRS_ALL_CNT)            as avg_GPRS_ALL_CNT                           
              ,avg(FLOW)                    as avg_FLOW
              ,avg(ALL_FLOW)                as avg_ALL_FLOW
              ,avg(FREQ)                    as avg_FREQ
              ,avg(ALL_FREQ)                as avg_ALL_FREQ
              ,stddev_samp(GPRS_CNT)        as stddev_GPRS_CNT    
              ,stddev_samp(GPRS_ALL_CNT)    as stddev_GPRS_ALL_CNT        
              ,stddev_samp(FLOW)            as stddev_FLOW        
              ,stddev_samp(ALL_FLOW)        as stddev_ALL_FLOW    
              ,stddev_samp(FREQ)            as stddev_FREQ        
              ,stddev_samp(ALL_FREQ)        as stddev_ALL_FREQ                  
        from %(temp_table)s
        group by cont_app_id
        ) b
    on a.CONT_APP_ID=b.CONT_APP_ID
group by a.cont_app_id
''' % vars())

#执行hivesql语句
    HiveExe(hivesql,name,dates)
#===========================================================================
# 第三步：标准化三类指标值
#===========================================================================
    hivesql = []
    hivesql.append('''
insert overwrite table %(temp_table_02)s 
select    '%(ARG_OPTIME_MONTH)s'
          ,m.PHONE_NO
          ,m.CONT_APP_ID
          ,( (case when m.GPRS_CNT>n.max_GPRS_CNT then 1.00
                   when m.GPRS_CNT=n.min_GPRS_CNT then 0.00 
                   else (m.GPRS_CNT-n.min_GPRS_CNT)*1.00/(n.max_GPRS_CNT-n.min_GPRS_CNT)
                   end)*%(GPRS_CNT_WEIGHT)s                
             + 
             (case when m.GPRS_ALL_CNT>n.max_GPRS_ALL_CNT then 1.00
                   when m.GPRS_ALL_CNT=n.min_GPRS_ALL_CNT then 0.00
              else (m.GPRS_ALL_CNT-n.min_GPRS_ALL_CNT)*1.00/(n.max_GPRS_ALL_CNT-n.min_GPRS_ALL_CNT)
              end)*%(GPRS_ALL_CNT_WEIGHT)s 
             + 
             (case when m.FLOW>n.max_FLOW then 1.00
                   when m.FLOW=n.min_FLOW then 0.00
              else (m.FLOW-n.min_FLOW)*1.00/(n.max_FLOW-n.min_FLOW)
              end)*%(FLOW_WEIGHT)s
             +
             (case when m.all_FLOW>n.max_ALL_FLOW then 1.00
                               when m.all_FLOW=n.min_ALL_FLOW then 0.00
               else (m.all_FLOW-n.min_ALL_FLOW)*1.00/(n.max_ALL_FLOW-n.min_ALL_FLOW)
              end)*%(ALL_FLOW_WEIGHT)s
             +(case when m.FREQ>n.max_FREQ then 1.00
                    when m.FREQ=n.min_FREQ then 0.00
               else (m.FREQ-n.min_FREQ)*1.00/(n.max_FREQ-n.min_FREQ)
               end) *%(FREQ_WEIGHT)s
             +(case when m.ALL_FREQ>n.max_ALL_FREQ then 1.00
                                when m.ALL_FREQ=n.min_ALL_FREQ then 0.00
               else (m.ALL_FREQ-n.min_ALL_FREQ)*1.00/(n.max_ALL_FREQ-n.min_ALL_FREQ)
               end)*%(ALL_FREQ_WEIGHT)s  
              ) * (case when nvl(t.cont_imcd_weight,0)*0.05>0.2 then 1.2 else nvl(t.cont_imcd_weight,0)*0.05 +1  end)/%(prefer_weight_config_sum)s*100  as prefer    --偏好度
from   %(temp_table)s m
left outer join %(temp_table_01)s n
 on m.CONT_APP_ID=n.CONT_APP_ID
left outer join (select
     USER_ID
    ,PHONE_NO
    ,CONT_APP_ID
    ,sum(GPRS_CNT) cont_imcd_weight
  from  %(source_dm_user_cont_imcd_yyyymmdd)s
group by USER_ID
    ,PHONE_NO
    ,CONT_APP_ID)   t
 on m.phone_no=t.phone_no
and m.CONT_APP_ID=t.CONT_APP_ID
''' % vars())

#执行hivesql语句
    HiveExe(hivesql,name,dates)
#===========================================================================
# 第四步：将数据插入到结果表
#===========================================================================
    hivesql = []
    hivesql.append('''
insert overwrite table %(target_dm_user_cont_prefer)s  partition ( %(Tas_MONTH_ID)s='%(ARG_OPTIME_MONTH)s')
select  '%(ARG_OPTIME_MONTH)s'
                                ,a.PHONE_NO
                                ,a.CONT_APP_ID
                                ,a.prefer
                                ,case when a.prefer>=(b.avg_prefer+b.stddev_prefer)  then '高'
                                      when a.prefer>=(b.avg_prefer+0.5*b.stddev_prefer) and a.prefer<(b.avg_prefer+b.stddev_prefer) then '中'
                                       else '低'
                                  end  as prefer_level     
from %(temp_table_02)s a
left outer join (  select CONT_APP_ID
                          ,avg(prefer)  as avg_prefer
                          ,stddev_samp(prefer)  as stddev_prefer
                   from   %(temp_table_02)s
                   group by CONT_APP_ID
                ) b
   on a.CONT_APP_ID=b.CONT_APP_ID
''' % vars())

#执行hivesql语句
    HiveExe(hivesql,name,dates)
#===========================================================================
# 删除临时表
#===========================================================================
    hivesql = []
    hivesql.append('''drop table %(temp_table)s''' % vars())
    hivesql.append('''drop table %(temp_table_01)s''' % vars())
    hivesql.append('''drop table %(temp_table_02)s''' % vars())
#执行hivesql语句
    HiveExe(hivesql,name,dates)
#程序结束
    End(name,dates)
#异常处理
except Exception,e:
    Except(name,dates,e)
