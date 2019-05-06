#!/usr/bin/env python
# -*-coding:utf-8 -*-
#*******************************************************************************************
# **  文件名称： tas_frequent_use_top_yyyymm.py           
# **  功能描述： 交际圈用户常用TOP排名
# **  输入表：   dw_contacts_user_info_yyyymm         DW_交际圈用户信息表
# **  输入表：   dw_cal_gprs_bh_yyyymm                DW_GPRS用户行为小时轻度月汇总
# **  输入表：   dim_app                              应用维表
# **  输入表：   dim_web_site                         网站维表
# **  输入表：   dim_cont_contacts                    内容-交际圈分类维表
# **  输出表:    tas_frequent_use_top_yyyymm          交际圈用户常用TOP排名 
# **           
# **  创建者:   程朋祥  18638112972  aoxiang37820@163.com
# **  创建日期: 2013/12/27
# **  修改日志:
# **  修改日期: 修改人   修改内容
# ** ---------------------------------------------------------------------------------------
# **  
# ** ---------------------------------------------------------------------------------------
# **  
# **  程序调用格式：python tas_frequent_use_top_yyyymm.py 201312
# **    
#********************************************************************************************
# **  Copyright(c) 2013 AsiaInfo Technologies (China), Inc. 
# **  All Rights Reserved.
#********************************************************************************************
import os,sys
from settings import *
from hqltools import *

#程序开始执行
name = sys.argv[0][sys.argv[0].rfind(os.sep)+1:].rstrip('.py')
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
    source_dw_cal_gprs_bh             = "dw_cal_gprs_bh_yyyymm"
    source_dw_contacts_user_info      = "dw_contacts_user_info_yyyymm"
    source_dim_app                    = "dim_app"
    source_dim_web_site               = "dim_web_site"
    source_dim_cont_contacts          = "dim_cont_contacts"
#===========================================================================================
#自定义变量声明---目标表声明
#===========================================================================================
    target_tas_frequent_use_top = "tas_frequent_use_top_yyyymm"
#===========================================================================================
#自定义变量声明---临时表声明
#===========================================================================================
    tmp_tas_frequent_user_flow         = "tmp_tas_frequent_user_flow"
    tmp_tas_frequent_web_site_top      = "tmp_tas_frequent_web_site_top"
    tmp_tas_frequent_app_top           = "tmp_tas_frequent_app_top"
    tmp_tas_frequent_cont_music_top    = "tmp_tas_frequent_cont_music_top"
    tmp_tas_frequent_cont_book_top     = "tmp_tas_frequent_cont_book_top"
    tmp_tas_frequent_cont_game_top     = "tmp_tas_frequent_cont_game_top"
    
    tmp_tas_frequent_use_top_1          = "tmp_tas_frequent_use_top_1"
    tmp_tas_frequent_use_top_2          = "tmp_tas_frequent_use_top_2"
    tmp_tas_frequent_use_top_3          = "tmp_tas_frequent_use_top_3"
    
#===========================================================================================
#第一步----创建目标表
#===========================================================================================
#向SQL变量赋值
    hivesql = []
    hivesql.append('''
    create table if not exists %(target_tas_frequent_use_top)s
    (
       statis_month   string  --时间（月）   
      ,phone_no       string  --主计费号码   
      ,user_id        string  --用户标识    
      ,l_212010       string  --常上网站top1
      ,l_212011       string  --常上网站top2
      ,l_212012       string  --常上网站top3
      ,l_212013       string  --常上网站top4
      ,l_212014       string  --常上网站top5
      ,l_212015       string  --常用应用top1
      ,l_212016       string  --常用应用top2
      ,l_212017       string  --常用应用top3
      ,l_212018       string  --常用应用top4
      ,l_212019       string  --常用应用top5
      ,l_212020       string  --常听音乐top1
      ,l_212021       string  --常听音乐top2
      ,l_212022       string  --常听音乐top3
      ,l_212023       string  --常听音乐top4
      ,l_212024       string  --常听音乐top5
      ,l_212025       string  --常读书籍top1
      ,l_212026       string  --常读书籍top2
      ,l_212027       string  --常读书籍top3
      ,l_212028       string  --常读书籍top4
      ,l_212029       string  --常读书籍top5
      ,l_212030       string  --常玩游戏top1
      ,l_212031       string  --常玩游戏top2
      ,l_212032       string  --常玩游戏top3
      ,l_212033       string  --常玩游戏top4
      ,l_212034       string  --常玩游戏top5
    )
    comment 'this is %(target_tas_frequent_use_top)s'
    partitioned by (%(Tas_MONTH_ID)s string)
    row format delimited
    fields terminated by '%(Tas_FS)s'
    stored as textfile
    ''' % vars())
#执行hivesql语句
    HiveExe(hivesql,name,dates)    

#===========================================================================================
#第二步----创建临时表
#===========================================================================================
#向SQL变量赋值
    hivesql = []
    hivesql.append('''
    create table if not exists %(tmp_tas_frequent_user_flow)s
    (
       user_id           string   --主计费号码
      ,phone_no          string   --用户帐号
      ,busi_id           int      --业务编码
      ,app_id            int      --应用编码
      ,busi_type_id      int      --业务类别编码
      ,site_id           int      --网站编码
      ,site_category_id  int      --网站频道编码
      ,cont_app_type     bigint   --内容应用体系分类ID
      ,cont_app_id       bigint   --内容应用体系ID
      ,cont_classify_id  bigint   --内容分类角度ID
      ,cont_type_id      bigint   --内容分类ID
      ,gprs_cnt          bigint   --GPRS访问次数
      ,total_flow        bigint   --流量
    )
    row format delimited
    fields terminated by '%(Tas_FS)s'
    stored as textfile
    ''' % vars())
    hivesql.append('''
    create table if not exists %(tmp_tas_frequent_web_site_top)s
    (
       phone_no       string  --主计费号码   
      ,user_id        string  --用户标识    
      ,l_212010       string  --常上网站top1
      ,l_212011       string  --常上网站top2
      ,l_212012       string  --常上网站top3
      ,l_212013       string  --常上网站top4
      ,l_212014       string  --常上网站top5
    )
    row format delimited
    fields terminated by '%(Tas_FS)s'
    stored as textfile
    ''' % vars())
    hivesql.append('''
    create table if not exists %(tmp_tas_frequent_app_top)s
    (
       phone_no       string  --主计费号码
      ,user_id        string  --用户标识
      ,l_212015       string  --常用应用top1
      ,l_212016       string  --常用应用top2
      ,l_212017       string  --常用应用top3
      ,l_212018       string  --常用应用top4
      ,l_212019       string  --常用应用top5
    )
    row format delimited
    fields terminated by '%(Tas_FS)s'
    stored as textfile
    ''' % vars())
    hivesql.append('''
    create table if not exists %(tmp_tas_frequent_cont_music_top)s
    (
       phone_no       string  --主计费号码
      ,user_id        string  --用户标识
      ,l_212020       string  --常听音乐top1
      ,l_212021       string  --常听音乐top2
      ,l_212022       string  --常听音乐top3
      ,l_212023       string  --常听音乐top4
      ,l_212024       string  --常听音乐top5
    )
    row format delimited
    fields terminated by '%(Tas_FS)s'
    stored as textfile
    ''' % vars())
    hivesql.append('''
    create table if not exists %(tmp_tas_frequent_cont_book_top)s
    (
       phone_no       string  --主计费号码
      ,user_id        string  --用户标识
      ,l_212025       string  --常读书籍top1
      ,l_212026       string  --常读书籍top2
      ,l_212027       string  --常读书籍top3
      ,l_212028       string  --常读书籍top4
      ,l_212029       string  --常读书籍top5
    )
    row format delimited
    fields terminated by '%(Tas_FS)s'
    stored as textfile
    ''' % vars())
    hivesql.append('''
    create table if not exists %(tmp_tas_frequent_cont_game_top)s
    (
       phone_no       string  --主计费号码
      ,user_id        string  --用户标识
      ,l_212030       string  --常玩游戏top1
      ,l_212031       string  --常玩游戏top2
      ,l_212032       string  --常玩游戏top3
      ,l_212033       string  --常玩游戏top4
      ,l_212034       string  --常玩游戏top5
    )
    row format delimited
    fields terminated by '%(Tas_FS)s'
    stored as textfile
    ''' % vars())
    hivesql.append('''
    create table if not exists %(tmp_tas_frequent_use_top_1)s
    (
       statis_month   string  --时间（月）   
      ,phone_no       string  --主计费号码   
      ,user_id        string  --用户标识    
      ,l_212010       string  --常上网站top1
      ,l_212011       string  --常上网站top2
      ,l_212012       string  --常上网站top3
      ,l_212013       string  --常上网站top4
      ,l_212014       string  --常上网站top5
      ,l_212015       string  --常用应用top1
      ,l_212016       string  --常用应用top2
      ,l_212017       string  --常用应用top3
      ,l_212018       string  --常用应用top4
      ,l_212019       string  --常用应用top5
      ,l_212020       string  --常听音乐top1
      ,l_212021       string  --常听音乐top2
      ,l_212022       string  --常听音乐top3
      ,l_212023       string  --常听音乐top4
      ,l_212024       string  --常听音乐top5
      ,l_212025       string  --常读书籍top1
      ,l_212026       string  --常读书籍top2
      ,l_212027       string  --常读书籍top3
      ,l_212028       string  --常读书籍top4
      ,l_212029       string  --常读书籍top5
      ,l_212030       string  --常玩游戏top1
      ,l_212031       string  --常玩游戏top2
      ,l_212032       string  --常玩游戏top3
      ,l_212033       string  --常玩游戏top4
      ,l_212034       string  --常玩游戏top5
    )
    row format delimited
    fields terminated by '%(Tas_FS)s'
    stored as textfile
    ''' % vars())
    hivesql.append('''
    create table if not exists %(tmp_tas_frequent_use_top_2)s like %(tmp_tas_frequent_use_top_1)s
    ''' % vars())
    hivesql.append('''
    create table if not exists %(tmp_tas_frequent_use_top_3)s like %(tmp_tas_frequent_use_top_1)s
    ''' % vars())
#执行hivesql语句
    HiveExe(hivesql,name,dates)

#===========================================================================================
#第三步----向临时表插入数据
#===========================================================================================
#--向 tmp_tas_frequent_user_flow 插入数据
    hivesql = []
    hivesql.append('''
    insert overwrite table %(tmp_tas_frequent_user_flow)s
    select
         ta.user_id
        ,ta.phone_no
        ,tb.busi_id
        ,tb.app_id
        ,tb.busi_type_id
        ,tb.site_id
        ,tb.site_category_id
        ,tb.cont_app_type
        ,tb.cont_app_id
        ,tb.cont_classify_id
        ,tb.cont_type_id
        ,sum(tb.gprs_cnt) as acce_cnt
        ,sum(coalesce(tb.up_flow,0) + coalesce(tb.down_flow,0)) as total_flow
    from
    (select * from %(source_dw_contacts_user_info)s where %(Tas_MONTH_ID)s=%(ARG_OPTIME_MONTH)s) ta
    left outer join (select * from %(source_dw_cal_gprs_bh)s where %(Tas_MONTH_ID)s=%(ARG_OPTIME_MONTH)s) tb
    on ta.oppo_no=tb.phone_no
    group by
         ta.user_id
        ,ta.phone_no
        ,tb.busi_id
        ,tb.app_id
        ,tb.busi_type_id
        ,tb.busi_provider_id
        ,tb.site_id
        ,tb.site_category_id
        ,tb.cont_app_type
        ,tb.cont_app_id
        ,tb.cont_classify_id
        ,tb.cont_type_id
    ''' % vars())
    
#执行hivesql语句
    HiveExe(hivesql,name,dates)
    
#--向 tmp_tas_frequent_web_site_top 插入数据    
    hivesql = []
    hivesql.append('''
    insert overwrite table %(tmp_tas_frequent_web_site_top)s
    select
         user_id
        ,phone_no
        ,if(rownum=1,site_name,null) as l_212010
        ,if(rownum=2,site_name,null) as l_212011
        ,if(rownum=3,site_name,null) as l_212012
        ,if(rownum=4,site_name,null) as l_212013
        ,if(rownum=5,site_name,null) as l_212014
    from
    (
      select
          user_id
          ,phone_no
          ,site_name
          ,row_number() over(partition by phone_no order by total_flow desc) as rownum
      from
      (
        select
            ta.user_id
           ,ta.phone_no
           ,coalesce(tb.site_name,'%(No_Flag)s') as site_name
           ,sum(ta.total_flow) as total_flow
        from
        %(tmp_tas_frequent_user_flow)s ta
        left outer join %(source_dim_web_site)s tb on ta.site_id=tb.site_id
        group by
            ta.user_id
           ,ta.phone_no
           ,tb.site_name
      )tc
    ) td
    where rownum <=5
    ''' % vars())
    
#执行hivesql语句
    HiveExe(hivesql,name,dates)

#--向 tmp_tas_frequent_app_top 插入数据    
    hivesql = []
    hivesql.append('''
    insert overwrite table %(tmp_tas_frequent_app_top)s
    select
         user_id
        ,phone_no
        ,if(rownum=1,app_name,null) as l_212015
        ,if(rownum=2,app_name,null) as l_212016
        ,if(rownum=3,app_name,null) as l_212017
        ,if(rownum=4,app_name,null) as l_212018
        ,if(rownum=5,app_name,null) as l_212019
    from
    (
      select
          user_id
          ,phone_no
          ,app_name
          ,row_number() over(partition by phone_no order by total_flow desc) as rownum
      from
      (
        select
            ta.user_id
           ,ta.phone_no
           ,coalesce(tb.app_name,'%(No_Flag)s') as app_name
           ,sum(ta.total_flow) as total_flow
        from
        %(tmp_tas_frequent_user_flow)s ta
        left outer join %(source_dim_app)s tb on ta.busi_id=tb.app_id
        where ta.app_id <> '%(No_Flag)s'
        group by
            ta.user_id
           ,ta.phone_no
           ,tb.app_name
      )tc
    ) td
    where rownum <=5
    ''' % vars())
    
#执行hivesql语句
    HiveExe(hivesql,name,dates)

#--向 tmp_tas_frequent_cont_music_top 插入数据    
    hivesql = []
    hivesql.append('''
    insert overwrite table %(tmp_tas_frequent_cont_music_top)s
    select
         user_id
        ,phone_no
        ,if(rownum=1,cont_type_name,null) as l_212020
        ,if(rownum=2,cont_type_name,null) as l_212021
        ,if(rownum=3,cont_type_name,null) as l_212022
        ,if(rownum=4,cont_type_name,null) as l_212023
        ,if(rownum=5,cont_type_name,null) as l_212024
    from
    (
      select
          user_id
          ,phone_no
          ,cont_type_name
          ,row_number() over(partition by phone_no order by total_flow desc) as rownum
      from
      (
        select
            ta.user_id
           ,ta.phone_no
           ,coalesce(tb.cont_type_name,'%(No_Flag)s') as cont_type_name
           ,sum(ta.total_flow) as total_flow
        from
        %(tmp_tas_frequent_user_flow)s ta
        left outer join %(source_dim_cont_contacts)s tb 
        on ta.cont_type_id=tb.cont_type_id and ta.cont_app_id=56
        where ta.cont_type_id <> '%(No_Flag)s'
        group by
            ta.user_id
           ,ta.phone_no
           ,tb.cont_type_name
      )tc
    ) td
    where rownum <=5
    ''' % vars())
    
#执行hivesql语句
    HiveExe(hivesql,name,dates)
#--向 tmp_tas_frequent_cont_book_top 插入数据  
    hivesql = []
    hivesql.append('''
    insert overwrite table %(tmp_tas_frequent_cont_book_top)s
    select
         user_id
        ,phone_no
        ,if(rownum=1,cont_type_name,null) as l_212025
        ,if(rownum=2,cont_type_name,null) as l_212026
        ,if(rownum=3,cont_type_name,null) as l_212027
        ,if(rownum=4,cont_type_name,null) as l_212028
        ,if(rownum=5,cont_type_name,null) as l_212029
    from
    (
      select
          user_id
          ,phone_no
          ,cont_type_name
          ,row_number() over(partition by phone_no order by total_flow desc) as rownum
      from
      (
        select
            ta.user_id
           ,ta.phone_no
           ,coalesce(tb.cont_type_name,'%(No_Flag)s') as cont_type_name
           ,sum(ta.total_flow) as total_flow
        from
        %(tmp_tas_frequent_user_flow)s ta
        left outer join %(source_dim_cont_contacts)s tb 
        on ta.cont_type_id=tb.cont_type_id and ta.cont_app_id=52
        where ta.cont_type_id <> '%(No_Flag)s'
        group by
            ta.user_id
           ,ta.phone_no
           ,tb.cont_type_name
      )tc
    ) td
    where rownum <=5
    ''' % vars())
    
#执行hivesql语句
    HiveExe(hivesql,name,dates)     
#--向 tmp_tas_frequent_cont_game_top 插入数据    
    hivesql = []
    hivesql.append('''
    insert overwrite table %(tmp_tas_frequent_cont_game_top)s
    select
         user_id
        ,phone_no
        ,if(rownum=1,cont_type_name,null) as l_212030
        ,if(rownum=2,cont_type_name,null) as l_212031
        ,if(rownum=3,cont_type_name,null) as l_212032
        ,if(rownum=4,cont_type_name,null) as l_212033
        ,if(rownum=5,cont_type_name,null) as l_212034
    from
    (
      select
          user_id
          ,phone_no
          ,cont_type_name
          ,row_number() over(partition by phone_no order by total_flow desc) as rownum
      from
      (
        select
            ta.user_id
           ,ta.phone_no
           ,coalesce(tb.cont_type_name,'%(No_Flag)s') as cont_type_name
           ,sum(ta.total_flow) as total_flow
        from
        %(tmp_tas_frequent_user_flow)s ta
        left outer join %(source_dim_cont_contacts)s tb 
        on ta.cont_type_id=tb.cont_type_id and ta.cont_app_id=57
        where ta.cont_type_id <> '%(No_Flag)s'
        group by
            ta.user_id
           ,ta.phone_no
           ,tb.cont_type_name
      )tc
    ) td
    where rownum <=5
    ''' % vars())
    
#执行hivesql语句
    HiveExe(hivesql,name,dates)


#--向 tmp_tas_frequent_use_top_1 插入数据
    hivesql = []
    hivesql.append('''
    insert overwrite table %(tmp_tas_frequent_use_top_1)s
    select
         %(ARG_OPTIME_MONTH)s
        ,user_id
        ,phone_no
        ,max(l_212010)
        ,max(l_212011)
        ,max(l_212012)
        ,max(l_212013)
        ,max(l_212014)
        ,max(l_212015)
        ,max(l_212016)
        ,max(l_212017)
        ,max(l_212018)
        ,max(l_212019)
        ,null
        ,null
        ,null
        ,null
        ,null
        ,null
        ,null
        ,null
        ,null
        ,null
        ,null
        ,null
        ,null
        ,null
        ,null
    from
    (
      select
           coalesce(ta.user_id,tb.user_id)   as user_id
          ,coalesce(ta.phone_no,tb.phone_no) as phone_no
          ,ta.l_212010
          ,ta.l_212011
          ,ta.l_212012
          ,ta.l_212013
          ,ta.l_212014
          ,tb.l_212015
          ,tb.l_212016
          ,tb.l_212017
          ,tb.l_212018
          ,tb.l_212019
      from
      %(tmp_tas_frequent_web_site_top)s ta
      left outer join %(tmp_tas_frequent_app_top)s tb
      on ta.phone_no=tb.phone_no
    ) tc
    group by 
             user_id
            ,phone_no
    ''' % vars())
    
#执行hivesql语句
    HiveExe(hivesql,name,dates)
    
#--向 tmp_tas_frequent_use_top_2 插入数据
    hivesql = []
    hivesql.append('''
    insert overwrite table %(tmp_tas_frequent_use_top_2)s
    select
         %(ARG_OPTIME_MONTH)s
        ,coalesce(ta.user_id,tb.user_id)   as user_id
        ,coalesce(ta.phone_no,tb.phone_no) as phone_no
        ,ta.l_212010
        ,ta.l_212011
        ,ta.l_212012
        ,ta.l_212013
        ,ta.l_212014
        ,ta.l_212015
        ,ta.l_212016
        ,ta.l_212017
        ,ta.l_212018
        ,ta.l_212019
        ,tb.l_212020
        ,tb.l_212021
        ,tb.l_212022
        ,tb.l_212023
        ,tb.l_212024
        ,null
        ,null
        ,null
        ,null
        ,null
        ,null
        ,null
        ,null
        ,null
        ,null
    from
    %(tmp_tas_frequent_use_top_1)s ta
    left outer join (select user_id
                     ,phone_no
                     ,max(l_212020) as l_212020 
                     ,max(l_212021) as l_212021
                     ,max(l_212022) as l_212022
                     ,max(l_212023) as l_212023
                     ,max(l_212024) as l_212024
               from %(tmp_tas_frequent_cont_music_top)s
               group by 
                        user_id
                       ,phone_no) tb
    on ta.phone_no=tb.phone_no
    ''' % vars())
    
#执行hivesql语句
    HiveExe(hivesql,name,dates)

#--向 tmp_tas_frequent_use_top_3 插入数据
    hivesql = []
    hivesql.append('''
    insert overwrite table %(tmp_tas_frequent_use_top_3)s
    select
         %(ARG_OPTIME_MONTH)s
        ,coalesce(ta.user_id,tb.user_id)   as user_id
        ,coalesce(ta.phone_no,tb.phone_no) as phone_no
        ,ta.l_212010
        ,ta.l_212011
        ,ta.l_212012
        ,ta.l_212013
        ,ta.l_212014
        ,ta.l_212015
        ,ta.l_212016
        ,ta.l_212017
        ,ta.l_212018
        ,ta.l_212019
        ,ta.l_212020
        ,ta.l_212021
        ,ta.l_212022
        ,ta.l_212023
        ,ta.l_212024
        ,tb.l_212025
        ,tb.l_212026
        ,tb.l_212027
        ,tb.l_212028
        ,tb.l_212029
        ,null
        ,null
        ,null
        ,null
        ,null
    from
    %(tmp_tas_frequent_use_top_2)s ta
    left outer join (select user_id
                     ,phone_no
                     ,max(l_212025) as l_212025
                     ,max(l_212026) as l_212026
                     ,max(l_212027) as l_212027
                     ,max(l_212028) as l_212028
                     ,max(l_212029) as l_212029
               from %(tmp_tas_frequent_cont_book_top)s
               group by 
                        user_id
                       ,phone_no) tb
    on ta.phone_no=tb.phone_no
    ''' % vars())
    
#执行hivesql语句
    HiveExe(hivesql,name,dates)
#===========================================================================================
#第四步----向目标表插入数据
#===========================================================================================
    hivesql = []
    hivesql.append('''
    insert overwrite table %(target_tas_frequent_use_top)s partition(%(Tas_MONTH_ID)s=%(ARG_OPTIME_MONTH)s)
    select
         %(ARG_OPTIME_MONTH)s
        ,coalesce(ta.user_id,tb.user_id)   as user_id
        ,coalesce(ta.phone_no,tb.phone_no) as phone_no
        ,ta.l_212010
        ,ta.l_212011
        ,ta.l_212012
        ,ta.l_212013
        ,ta.l_212014
        ,ta.l_212015
        ,ta.l_212016
        ,ta.l_212017
        ,ta.l_212018
        ,ta.l_212019
        ,ta.l_212020
        ,ta.l_212021
        ,ta.l_212022
        ,ta.l_212023
        ,ta.l_212024
        ,ta.l_212025
        ,ta.l_212026
        ,ta.l_212027
        ,ta.l_212028
        ,ta.l_212029
        ,tb.l_212030
        ,tb.l_212031
        ,tb.l_212032
        ,tb.l_212033
        ,tb.l_212034
    from
    %(tmp_tas_frequent_use_top_3)s ta
    left outer join (select user_id
                     ,phone_no
                     ,max(l_212030) as l_212030
                     ,max(l_212031) as l_212031
                     ,max(l_212032) as l_212032
                     ,max(l_212033) as l_212033
                     ,max(l_212034) as l_212034
               from %(tmp_tas_frequent_cont_game_top)s
               group by 
                        user_id
                       ,phone_no) tb
    on ta.phone_no=tb.phone_no
    ''' % vars())
    
#执行hivesql语句
    HiveExe(hivesql,name,dates)

#===========================================================================================
#第五步----删除临时表
#===========================================================================================
    hivesql = []
    hivesql.append('''drop table if exists %(tmp_tas_frequent_user_flow)s ''' % vars())
    hivesql.append('''drop table if exists %(tmp_tas_frequent_web_site_top)s ''' % vars())
    hivesql.append('''drop table if exists %(tmp_tas_frequent_app_top)s ''' % vars())
    hivesql.append('''drop table if exists %(tmp_tas_frequent_cont_music_top)s ''' % vars())
    hivesql.append('''drop table if exists %(tmp_tas_frequent_cont_book_top)s ''' % vars())
    hivesql.append('''drop table if exists %(tmp_tas_frequent_cont_game_top)s ''' % vars())
    hivesql.append('''drop table if exists %(tmp_tas_frequent_use_top_1)s ''' % vars())
    hivesql.append('''drop table if exists %(tmp_tas_frequent_use_top_2)s ''' % vars())
    hivesql.append('''drop table if exists %(tmp_tas_frequent_use_top_3)s ''' % vars())
    HiveExe(hivesql,name,dates)
#程序结束
    End(name,dates)
#异常处理
except Exception,e:
    Except(name,dates,e)
