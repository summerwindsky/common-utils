#!/usr/bin/env python
# -*-coding:utf-8 -*-
#*******************************************************************************************
# **  文件名称：dm_user_cont_app_prefer_yyyymm.py
# **  功能描述：用户内容偏好模型月表
# **  输入表：  DM_USER_cont_PREFER_BASE_YYYYMM    用户内容偏好基础信息月表
# **  输入表：  DW_USER_ALL_INFO_YYYYMM            用户中间层月表
# **  输出表:   dm_user_cont_app_prefer_yyyymm     用户内容偏好模型月表
# **
# **  创建者:   周美金 zhoumj@info-linkage.com
# **  创建日期: 2013/12/18
# **  修改日志:
# **  修改日期: 修改人   修改二级内容
# ** ---------------------------------------------------------------------------------------
# **
# ** ---------------------------------------------------------------------------------------
# **
# **  程序调用格式：python dm_user_cont_app_prefer_yyyymm.py 201312
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
#app_Type_Flag           二级内容全部变量
#Tac_Flag = -1            终端TAC
#Site_Name_Flag           网站名称
#Logic_Area_Subtype_Flag  逻辑区子类
#Logic_Area_Type_Flag     逻辑区类别
#Day_Type_Flag            全部日期类型
#All_Flag                 共用全部变量
#M_Conversion             流量转换变量(M)
#K_Conversion             流量转换变量(K)
#term_type_phone          终端类型变量
#Lib_Source               二级内容解析包路径
#===========================================================================================
#自定义变量声明---源表声明
#===========================================================================================
    source_dw_user_cont_prefer_base    = "DM_USER_cont_PREFER_BASE_YYYYMM"
    source_dw_user_all_info            = "dw_user_all_info_yyyymm"

#===========================================================================================
#自定义变量声明---目标表声明
#===========================================================================================
    target_dm_user_app_prefer        = "dm_user_cont_app_prefer_yyyymm"

#===========================================================================================_
#自定义变量声明---临时表声明
#===========================================================================================
    temp_table_01                     ="dm_user_cont_app_prefer_yyyymm_tmp_01"
    temp_table_02                     ="dm_user_cont_app_prefer_yyyymm_tmp_02"
    temp_table_03                     ="dm_user_cont_app_prefer_yyyymm_tmp_03"
    temp_table_04                     ="dm_user_cont_app_prefer_yyyymm_tmp_04"
    temp_table_05                     ="dm_user_cont_app_prefer_yyyymm_tmp_05"
#===========================================================================

#===========================================================================
#Create table 创建目标表
#===========================================================================
#向SQL变量赋值
    hivesql = []
    hivesql.extend(['''
create table if not exists %(temp_table_01)s
(
    STATIS_MONTH          STRING     --统计月份
   ,cont_app_id           STRING     --二级内容标识
   ,max_GPRS_CNT          DOUBLE     --次数最大值
   ,max_FLOW              DOUBLE     --流量最大值
   ,max_FREQ              DOUBLE     --频率最大值
   ,min_GPRS_CNT          DOUBLE     --次数最小值
   ,min_FLOW              DOUBLE     --流量最小值
   ,min_FREQ              DOUBLE     --频率最小值
   ,GPRS_ALTERED          DOUBLE     --次数变异系数
   ,FLOW_ALTERED          DOUBLE     --流量变异系数
   ,FREQ_ALTERED          DOUBLE     --频率变异系数
)
row format delimited
fields terminated by '%(Tas_FS)s'
stored as sequencefile
''' % vars(),'''
create table if not exists %(temp_table_02)s
(
    STATIS_MONTH           STRING     --统计月份
    ,PHONE_NO              STRING     --手机号码
    ,cont_app_id           INT        --二级内容标识
    ,GPRS_CNT              INT        --本网用户浏览某类二级内容次数
    ,prefer                DOUBLE     --偏好度
)
row format delimited
fields terminated by '%(Tas_FS)s'
stored as sequencefile ''' % vars(),'''
create table if not exists %(temp_table_03)s
(
    STATIS_MONTH           STRING     --统计月份
    ,PHONE_NO              STRING     --手机号码
    ,cont_app_id           bigint     --二级内容标识
    ,GPRS_CNT              INT        --本网用户浏览某类二级内容次数
    ,prefer                DOUBLE     --偏好度
    ,rank_user_at_app      bigint     --用户偏好度的排名
)
row format delimited
fields terminated by '%(Tas_FS)s'
stored as sequencefile ''' % vars(),'''
create table if not exists %(temp_table_04)s
(
    STATIS_MONTH           STRING     --统计月份
    ,PHONE_NO              STRING     --手机号码
    ,cont_app_id           bigint     --二级内容标识
    ,GPRS_CNT              INT        --本网用户浏览某类二级内容次数
    ,prefer                DOUBLE     --偏好度
    ,rank_user_at_app      bigint     --用户偏好度的排名
    ,rank_app_at_user      int        --用户使用的二级内容安次数排倒序
    ,USER_CNT              bigint     --使用用户数
)
row format delimited
fields terminated by '%(Tas_FS)s'
stored as sequencefile ''' % vars(),'''
create table if not exists %(temp_table_05)s
(
    STATIS_MONTH           STRING     --统计月份
    ,PHONE_NO              STRING     --手机号码
    ,cont_app_id           bigint        --二级内容标识
    ,GPRS_CNT              INT        --本网用户浏览某类二级内容次数
    ,prefer                DOUBLE     --偏好度
    ,rank_NEW              bigint     --用户偏好度的排名(打上偏好标签用户再次排名，用以打上偏好强度标签)
)
row format delimited
fields terminated by '%(Tas_FS)s'
stored as sequencefile ''' % vars(),'''
create table if not exists %(target_dm_user_app_prefer)s
(
     STATIS_MONTH         STRING      --统计月份
    ,USER_ID              STRING      --用户标识
    ,PHONE_NO             STRING      --主计费号
    ,CITY_ID              STRING      --地市编码
    ,COUNTY_ID            STRING      --区县编码
    ,SECTION_ID           STRING      --片区编码
    ,BRAND_ID             SMALLINT    --品牌编码
    ,cont_app_id          bigint      --二级内容标识
    ,prefer               DOUBLE      --偏好
    ,prefer_level         int         --偏好等级
)
partitioned by (%(Tas_MONTH_ID)s string)
row format delimited
fields terminated by '%(Tas_FS)s'
stored as sequencefile
''' % vars()])

#执行hivesql语句
    HiveExe(hivesql,name,dates)

#asd=HiveExe(hivesql1,name,dates)
#print asd

#===========================================================================
# 第二步：取出用户指标的最大最小值,变异系数
#===========================================================================
    hivesql = []
    hivesql.append('''
insert overwrite table %(temp_table_01)s
select  '%(ARG_OPTIME_MONTH)s'
         ,a.cont_app_id
        ,max(case when a.GPRS_CNT>(b.avg_GPRS_CNT+3*b.stddev_GPRS_CNT)
             then null
             else a.GPRS_CNT
             end )               as max_GPRS_CNT
        ,max(case when a.FLOW>(b.avg_FLOW+3*b.stddev_FLOW)
             then null
             else a.FLOW
             end )                as max_FLOW
        ,max(case when a.FREQ>(b.avg_FREQ+3*b.stddev_FREQ)
             then 0.00
             else a.FREQ
             end )                as max_FREQ
         ,min(GPRS_CNT)           as min_GPRS_CNT
         ,min(FLOW)               as min_FLOW
         ,min(nvl(FREQ,0.00))     as min_FREQ
         ,max( case when stddev_GPRS_CNT = 0 then 1.00 else
                1 - (stddev_GPRS_CNT/avg_GPRS_CNT)/
                (stddev_GPRS_CNT/avg_GPRS_CNT + stddev_FLOW/avg_FLOW + stddev_FREQ/avg_FREQ ) end )         as  GPRS_ALTERED   --次数变异系数
         ,max(case when stddev_FLOW = 0 then 1.00 else
               1 - (stddev_FLOW/avg_FLOW) /
               (stddev_GPRS_CNT/avg_GPRS_CNT
               + stddev_FLOW/avg_FLOW
               + stddev_FREQ/avg_FREQ ) end )         as  FL0W_ALTERED   --流量变异系数
         ,max(case when stddev_FREQ = 0 then 1.00 else
                1- (stddev_FREQ/avg_FREQ)/
              (stddev_GPRS_CNT/avg_GPRS_CNT
              + stddev_FLOW/avg_FLOW
              + stddev_FREQ/avg_FREQ ) end )          as  FREQ_ALTERED   --频率变异系数
from  %(source_dw_user_cont_prefer_base)s a
left outer join
       (select  cont_app_id
              ,avg(GPRS_CNT)                as avg_GPRS_CNT
              ,avg(FLOW)                    as avg_FLOW
              ,avg(FREQ)                    as avg_FREQ
              ,stddev_samp(GPRS_CNT)        as stddev_GPRS_CNT
              ,stddev_samp(FLOW)            as stddev_FLOW
              ,stddev_samp(FREQ)            as stddev_FREQ
        from %(source_dw_user_cont_prefer_base)s
      where %(Tas_MONTH_ID)s='%(ARG_OPTIME_MONTH)s'
        group by cont_app_id
        ) b
    on a.cont_app_id=b.cont_app_id
where a.%(Tas_MONTH_ID)s='%(ARG_OPTIME_MONTH)s'
group by a.cont_app_id
''' % vars())

#执行hivesql语句
    HiveExe(hivesql,name,dates)

#asd=HiveExe(hivesql1,name,dates)
#print asd


#===========================================================================
# 第三步：标准化三类指标值,求偏好系数 偏好系数=标准化值*权重 相加
#===========================================================================
    hivesql = []
    hivesql.append('''
insert overwrite table %(temp_table_02)s
select    '%(ARG_OPTIME_MONTH)s'
          ,m.PHONE_NO
          ,m.cont_app_id
          ,m.GPRS_CNT
          ,( (case when m.GPRS_CNT>n.max_GPRS_CNT then 1.00
                   when m.GPRS_CNT=n.min_GPRS_CNT then 0.00
                   else (m.GPRS_CNT-n.min_GPRS_CNT)*1.00/(n.max_GPRS_CNT-n.min_GPRS_CNT)
                   end) *  n.GPRS_ALTERED
             +
             (case when m.FLOW>n.max_FLOW then 1.00
                   when m.FLOW=n.min_FLOW then 0.00
              else (m.FLOW-n.min_FLOW)*1.00/(n.max_FLOW-n.min_FLOW)
              end) * n.FLOW_ALTERED
             +
             (case when m.FREQ>n.max_FREQ then 1.00
                    when m.FREQ=n.min_FREQ then 0.00
               else (m.FREQ-n.min_FREQ)*1.00/(n.max_FREQ-n.min_FREQ)
               end) * n.FREQ_ALTERED
             ) * 100/2  as prefer    --偏好度
from   %(source_dw_user_cont_prefer_base)s m
left outer join %(temp_table_01)s n
 on m.cont_app_id=n.cont_app_id
where m.%(Tas_MONTH_ID)s='%(ARG_OPTIME_MONTH)s'
''' % vars())

#执行hivesql语句
    HiveExe(hivesql,name,dates)

#asd=HiveExe(hivesql1,name,dates)
#print asd

#===========================================================================
# 第四步：排序，USER_CNT取每个APP下对应的用户数，用于后面取中位数，分位数
#===========================================================================
    hivesql = []
    hivesql.append('''add jar /home/ocdc/app/hubei/lib/hdm-udf.jar''')
    hivesql.append(''' create temporary function row_num_udf as "com.ailk.hdm.hive.udf.UDFRowNumber"''')
    hivesql.append('''insert overwrite table %(temp_table_03)s
select    '%(ARG_OPTIME_MONTH)s'
          ,m.PHONE_NO
          ,m.cont_app_id
          ,m.GPRS_CNT
          ,m.prefer      --偏好度
          ,row_num_udf (cont_app_id) as rank_user_at_app
from (select * from %(temp_table_02)s distribute by  cont_app_id sort by cont_app_id,prefer desc) m
''' % vars())

#执行hivesql语句
    HiveExe(hivesql,name,dates)

#asd=HiveExe(hivesql1,name,dates)
#print asd

#===========================================================================
# 第五步：用户使用的二级内容安次数排倒序
#===========================================================================
    hivesql = []
    hivesql.append('''
insert overwrite table %(temp_table_04)s
select
        '%(ARG_OPTIME_MONTH)s' --统计月份
       ,PHONE_NO             --手机号码
       ,cont_app_id               --二级内容标识
       ,GPRS_CNT_NEW GPRS_CNT       --本网用户浏览某类二级内容次数(中位数以下置0)
       ,prefer               --偏好度
       ,rank_user_at_app     --用户偏好度的排名
       ,ROW_NUMBER() OVER (PARTITION BY PHONE_NO ORDER BY  GPRS_CNT_NEW DESC ) AS rank_app_at_user --每个用户使用的二级内容按次数排倒序
       ,USER_CNT             --使用用户数
from (select  
             a.PHONE_NO
            ,a.cont_app_id
            ,a.GPRS_CNT
            ,a.prefer      --偏好度
            ,a.rank_user_at_app
            ,b.USER_CNT
            ,case when b.USER_CNT/2 > a.rank_user_at_app then 0 else a.GPRS_CNT end GPRS_CNT_NEW  --中位数以下的用户不
       from %(temp_table_03)s a
    left outer join
       (select
       cont_app_id         --二级内容标识
      ,count(cont_app_id) as USER_CNT       --使用用户数
 from %(temp_table_02)s
group by cont_app_id ) b
     on a.cont_app_id = b.cont_app_id  
        )  t
''' % vars())

#执行hivesql语句
    HiveExe(hivesql,name,dates)

#asd=HiveExe(hivesql1,name,dates)
#print asd

#===========================================================================
# 第六步：利用H-Index算法对用户内容偏好进行打标签,并存放进表再次排序
#===========================================================================
    hivesql = []
    hivesql.append('''add jar /home/ocdc/app/hubei/lib/hdm-udf.jar''')
    hivesql.append('''create temporary function row_num_udf as "com.ailk.hdm.hive.udf.UDFRowNumber"''')
    hivesql.append('''insert overwrite table %(temp_table_05)s
select
         '%(ARG_OPTIME_MONTH)s'      --统计月份
        ,PHONE_NO                   --手机号码
        ,cont_app_id                     --二级内容标识
        ,GPRS_CNT                   --本网用户浏览某类二级内容次数
        ,prefer                     --偏好度
        ,row_num_udf (cont_app_id) rank_NEW  --用户偏好度的排名(打上偏好标签用户再次排名，用以打上偏好等级标签)
from (select *
       from ( select * from  %(temp_table_04)s
      where rank_user_at_app < USER_CNT/2 --重度用户（排名在中位数之上）
        and GPRS_CNT > rank_app_at_user   --份额较高（H-Index算法）
          ) a
        distribute by  cont_app_id sort by cont_app_id,prefer desc
        ) t
''' % vars())

#执行hivesql语句
    HiveExe(hivesql,name,dates)

#asd=HiveExe(hivesql1,name,dates)
#print asd

#===========================================================================
# 第七步：将数据插入到结果表
#===========================================================================
    hivesql = []
    hivesql.append('''
insert overwrite table %(target_dm_user_app_prefer)s  partition ( %(Tas_MONTH_ID)s='%(ARG_OPTIME_MONTH)s')
select  '%(ARG_OPTIME_MONTH)s'
      ,a.USER_ID                --用户标识
      ,a.PHONE_NO               --主计费号
      ,a.CITY_ID                --地市编码
      ,a.COUNTY_ID              --区县编码
      ,a.SECTION_ID             --片区编码
      ,a.BRAND_ID               --品牌编码
      ,b.cont_app_id                 --二级内容标识
      ,b.prefer                 --偏好度
      ,case when RANK_NEW>c.USER_CNT*5/6 then 1  --发烧用户
            when RANK_NEW<=c.USER_CNT*5/6 and RANK_NEW>c.USER_CNT*4/6 then 2 --强偏好用户
            when RANK_NEW<=c.USER_CNT*4/6 and RANK_NEW>c.USER_CNT*2/6 then 3 --一般偏好用户
       else 4 --弱偏好用户
       end as prefer_level             --偏好等级
from %(source_dw_user_all_info)s a
join
     %(temp_table_05)s  b
  on a.PHONE_NO=b.PHONE_NO
left outer join
      (select
              cont_app_id         --二级内容标识
             ,count(cont_app_id) as USER_CNT       --使用用户数
        from %(temp_table_05)s
        group by cont_app_id) c
  on b.cont_app_id = c.cont_app_id
where a.%(Tas_MONTH_ID)s='%(ARG_OPTIME_MONTH)s'
''' % vars())

#执行hivesql语句
    HiveExe(hivesql,name,dates)

#asd=HiveExe(hivesql1,name,dates)
#print asd

#程序结束
    End(name,dates)
#异常处理
except Exception,e:
    Except(name,dates,e)
