#!/usr/bin/env python
# -*-coding:utf-8 -*-
##******************************************************************************
## **  文件名称：dw_user_prefer_base_yyyymm.py
## **  功能描述：用户偏好基础数据
## **  输入表    DW_CAL_GPRS_BH_YYYYMM
## **
## **  输出表:   DM_USER_CONT_PREFER_BASE_YYYYMM         用户内容偏好基础表
## **            dm_user_app_prefer_base_YYYYMM          用户应用偏好基础表
## **            DM_USER_LAC_PREFER_BASE_YYYYMM          用户位置偏好基础表
## **            DM_USER_MOMENT_PREFER_BASE_YYYYMM       用户时段偏好基础表
## **  创建者:   周美金 18651866031 zhoumu@asiainfo-linkage.com
## **  创建日期: 2013/09/23
## **  修改日志:
## **  修改日期:   修改人   修改内容
# ** ---------------------------------------------------------------
# **
# ** ---------------------------------------------------------------
# **
# **  程序调用格式：python dw_user_prefer_base_yyyymm.py 201305
# **
#*******************************************************************************
# **  Copyright(c) 2013 AsiaInfo Technologies (China), Inc.
# **  All Rights Reserved.
#*******************************************************************************
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
    ARG_OPTIME_LAST2MONTH     = dicts['ARG_OPTIME_LAST2MONTH']        #获得yyyymm格式的上2月数据日期
    ARG_OPTIME_LAST2MONTH_ISO = dicts['ARG_OPTIME_LAST2MONTH_ISO']    #获得yyyy-mm格式的上2月数据日期
    ARG_OPTIME_LAST3MONTH     = dicts['ARG_OPTIME_LAST3MONTH']        #获得yyyymm格式的上3月数据日期
    ARG_OPTIME_LAST3MONTH_ISO = dicts['ARG_OPTIME_LAST3MONTH_ISO']    #获得yyyy-mm格式的上3月数据日期
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
#APP_Flag                业务
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
#set Month_Day  datediff('${ARG_OPTIME_MONEND}', '${ARG_OPTIME_MON01}')

##===========================================================================
##自定义变量声明---源表声明
##===========================================================================
    source_dw_cal_gprs_bh_yyyymm =   "dw_cal_gprs_bh_yyyymm"

##===========================================================================
##自定义变量声明---目标表声明
##===========================================================================
    dm_user_cont_prefer_base     =  "dm_user_cont_prefer_base_yyyymm"
    dm_user_app_prefer_base     =  "dm_user_app_prefer_base_yyyymm"
    dm_user_lac_prefer_base      =  "dm_user_lac_prefer_base_yyyymm"
    dm_user_moment_prefer_base   =  "dm_user_moment_prefer_base_yyyymm"

##===========================================================================
##自定义变量声明---临时表声明
##===========================================================================


#===========================================================================================
#创建临时表
#===========================================================================================

##===========================================================================
##Create table 创建内容偏好目标表
##===========================================================================
#向SQL变量赋值
    hivesql = []
    hivesql.append('''
create table if not exists %(dm_user_cont_prefer_base)s
(
     statis_month         STRING   --统计月份
    ,PHONE_NO             STRING   --手机号码
    ,cont_app_type        bigint   --内容标识一级
    ,cont_app_id          bigint   --内容标识二级
    ,cont_classify_id     bigint   --内容标识三级
    ,cont_type_id         bigint   --内容标识四级
    ,GPRS_CNT             DOUBLE   --用户浏览某类内容次数
    ,FLOW                 DOUBLE   --用户浏览某类内容流量
    ,FREQ                 DOUBLE   --用户浏览某类内容频率
)
    comment 'this is %(dm_user_cont_prefer_base)s'
    partitioned by ( %(Tas_MONTH_ID)s string)
    row format delimited
    fields terminated by '%(Tas_FS)s'
    stored as sequencefile
''' % vars())

#执行hivesql语句
    HiveExe(hivesql,name,dates)

#asd=HiveExe(hivesql1,name,dates)
#print asd

##===========================================================================
## load data 装载数据到内容偏好表中
##===========================================================================

    hivesql = []
    hivesql.append('''insert overwrite table %(dm_user_cont_prefer_base)s partition (%(Tas_MONTH_ID)s='%(ARG_OPTIME_MONTH)s')
select
         '%(ARG_OPTIME_MONTH)s' statis_month
        ,PHONE_NO              --手机号码
        ,cont_app_type         --内容标识一级
        ,cont_app_id           --内容标识二级
        ,cont_classify_id      --内容标识三级
        ,cont_type_id          --内容标识四级
        ,sum(GPRS_CNT)          GPRS_CNT     --用户浏览某类内容次数
        ,sum(UP_FLOW+DOWN_FLOW) FLOW         --用户浏览某类内容流量
        ,max(ADD_USE_DAYS) / 30 FREQ         --用户浏览某类内容频率
 from %(source_dw_cal_gprs_bh_yyyymm)s
where %(Tas_MONTH_ID)s='%(ARG_OPTIME_MONTH)s'
  and cont_type_id <> %(No_Flag)s
group by 
         PHONE_NO              --手机号码
        ,cont_app_type         --内容标识一级
        ,cont_app_id           --内容标识二级
        ,cont_classify_id      --内容标识三级
        ,cont_type_id
''' % vars())
#执行hivesql语句
    HiveExe(hivesql,name,dates)

#asd=HiveExe(hivesql1,name,dates)
#print asd

##===========================================================================
##Create table 创建应用偏好目标表
##===========================================================================
#向SQL变量赋值
    hivesql = []
    hivesql.append('''
create table if not exists %(dm_user_app_prefer_base)s
(
     statis_month         STRING   --统计月份
    ,PHONE_NO             STRING   --手机号码
    ,APP_TYPE_ID          INT      --应用分类标识
    ,APP_ID               INT      --应用标识
    ,GPRS_CNT             DOUBLE   --用户浏览某类应用次数
    ,FLOW                 DOUBLE   --用户浏览某类应用流量
    ,FREQ                 DOUBLE   --用户浏览某类应用频率
)
comment 'this is %(dm_user_app_prefer_base)s'
partitioned by ( %(Tas_MONTH_ID)s string)
row format delimited
fields terminated by '%(Tas_FS)s'
stored as sequencefile
''' % vars())
#执行hivesql语句
    HiveExe(hivesql,name,dates)

#asd=HiveExe(hivesql1,name,dates)
#print asd

##===========================================================================
## load data 装载数据到应用偏好表中
##===========================================================================

    hivesql = []
    hivesql.append('''insert overwrite table %(dm_user_app_prefer_base)s partition (%(Tas_MONTH_ID)s='%(ARG_OPTIME_MONTH)s')
select
         '%(ARG_OPTIME_MONTH)s' statis_month
        ,PHONE_NO              --手机号码
        ,BUSI_TYPE_ID APP_TYPE_ID           --应用分类标识
        ,APP_ID                --应用标识
        ,sum(GPRS_CNT)          GPRS_CNT     --次数
        ,sum(UP_FLOW+DOWN_FLOW) FLOW         --流量
        ,max(ADD_USE_DAYS) / 30 FREQ         --频率
 from %(source_dw_cal_gprs_bh_yyyymm)s
where %(Tas_MONTH_ID)s='%(ARG_OPTIME_MONTH)s'
  and APP_ID <> %(No_Flag)s
group by 
         PHONE_NO              --手机号码
        ,BUSI_TYPE_ID           --应用分类标识
        ,APP_ID                --应用标识
''' % vars())
#执行hivesql语句
    HiveExe(hivesql,name,dates)

#asd=HiveExe(hivesql1,name,dates)
#print asd

##===========================================================================
##Create table 创建目标表
##===========================================================================
#向SQL变量赋值
    hivesql = []
    hivesql.append('''
create table if not exists %(dm_user_moment_prefer_base)s
(
     statis_month         STRING   --统计月份
    ,PHONE_NO             STRING   --手机号码
    ,moment_id            SMALLINT --时段标识
    ,GPRS_CNT             DOUBLE   --次数
    ,FLOW                 DOUBLE   --流量
    ,FREQ                 DOUBLE   --频率
)
comment 'this is %(dm_user_moment_prefer_base)s'
partitioned by ( %(Tas_MONTH_ID)s string)
row format delimited
fields terminated by '%(Tas_FS)s'
stored as sequencefile
''' % vars())
#执行hivesql语句
    HiveExe(hivesql,name,dates)

#asd=HiveExe(hivesql1,name,dates)
#print asd

##===========================================================================
## load data 装载数据到Hive中
##===========================================================================

    hivesql = []
    hivesql.append('''insert overwrite table %(dm_user_moment_prefer_base)s partition (%(Tas_MONTH_ID)s='%(ARG_OPTIME_MONTH)s')
select
         '%(ARG_OPTIME_MONTH)s' statis_month
        ,PHONE_NO              --手机号码
        ,moment_id             --时段
        ,sum(GPRS_CNT)          GPRS_CNT     --次数
        ,sum(UP_FLOW+DOWN_FLOW) FLOW         --流量
        ,max(ADD_USE_DAYS) / 30 FREQ         --频率
 from %(source_dw_cal_gprs_bh_yyyymm)s
where %(Tas_MONTH_ID)s='%(ARG_OPTIME_MONTH)s'
  and moment_id <> %(No_Flag)s
  group by
        PHONE_NO      --手机号码
       ,moment_id
''' % vars())
#执行hivesql语句
    HiveExe(hivesql,name,dates)

#asd=HiveExe(hivesql1,name,dates)
#print asd

##===========================================================================
##Create table 创建目标表
##===========================================================================
#向SQL变量赋值
    hivesql = []
    hivesql.append('''
create table if not exists %(dm_user_lac_prefer_base)s
(
     statis_month         STRING   --统计月份
    ,PHONE_NO             STRING   --手机号码
    ,lac                  string   --位置区编码
    ,ci                   string   --小区编码
    ,LAC_CI               STRING   --位置标识
    ,GPRS_CNT             DOUBLE   --次数
    ,FLOW                 DOUBLE   --流量
    ,FREQ                 DOUBLE   --频率
)
comment 'this is %(dm_user_lac_prefer_base)s'
partitioned by ( %(Tas_MONTH_ID)s string)
row format delimited
fields terminated by '%(Tas_FS)s'
stored as sequencefile
''' % vars())
#执行hivesql语句
    HiveExe(hivesql,name,dates)

#asd=HiveExe(hivesql1,name,dates)
#print asd

##======================================================================
## load data 装载数据到Hive中
##======================================================================
    hivesql = []
    hivesql.append('''insert overwrite table  %(dm_user_lac_prefer_base)s partition (%(Tas_MONTH_ID)s='%(ARG_OPTIME_MONTH)s')
select
         '%(ARG_OPTIME_MONTH)s' statis_month
        ,PHONE_NO              --手机号码
        ,lac                   --位置区编码
        ,ci                    --小区编码
        ,LAC_CI                --位置标识
        ,sum(GPRS_CNT)          GPRS_CNT     --次数
        ,sum(UP_FLOW+DOWN_FLOW) FLOW         --流量
        ,max(ADD_USE_DAYS) / 30 FREQ         --频率
 from %(source_dw_cal_gprs_bh_yyyymm)s
where %(Tas_MONTH_ID)s='%(ARG_OPTIME_MONTH)s'
  and LAC_CI <> '%(No_Flag)s'
  group by
        PHONE_NO      --手机号码
        ,lac          --位置区编码
        ,ci           --小区编码
        ,LAC_CI       --位置标识
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
                                   
                                   