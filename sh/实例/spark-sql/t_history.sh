#!/bin/bash
######################################################################
# script in shell
# 创建时间: 2018-1-10 16:57:01
# 目标表  :
#           fkmx.t_history_loan_data
#           fkmx.t_history_apply_data
#           fkmx.t_history_plan_data
#           fkmx.t_history_data
#           fkmx.t_history_data_all
#           fkmx.t_history_loan_data_bcard
#           fkmx.t_history_data_bcard
#           fkmx.t_history_loan_data_fkmx
#           fkmx.t_history_apply_data_bcard_fkmx
#           fkmx.t_history_loan_data_bcard_fkmx
#           fkmx.t_history_plan_data_bcard_fkmx
# 源表:
#           ods.ods_fc_ccs_acct
#           ods.ods_fc_ccs_customer
#           ods.ods_fc_ccs_loan
#           ods.ods_mj_cca_tm_app_main
#           ods.ods_fc_ccs_plan
#           ods.ods_mj_cca_tm_app_prim_applicant_info
# 脚本功能: 风控建模-行为指标分析
# 加工策略：每日跑批
# 责任人  : 李传鹏
######################################################################
# 昨天日期
dt=`date -d "1 day ago" +%F`

spark-sql -e "
-- 生成贷款历史数据汇总表数据
insert overwrite table fkmx.t_history_loan_data partition(dt='${dt}')
select
    customer.ID_NO,
    customer.CREATE_TIME,
    loan.CONTR_NBR,
    act.ACCT_NBR,
    loan.max_dpd,
    loan.ACTIVE_DATE
from
(
    select
        *
    from ods.ods_fc_ccs_acct
    where dt='${dt}'
) act
,
(
    select
        *
    from ods.ods_fc_ccs_customer
    where dt='${dt}'
) customer
,
(
    select
        *
    from ods.ods_fc_ccs_loan
    where dt='${dt}'
)loan
where act.CUST_ID=customer.CUST_ID and act.ACCT_NBR =loan.ACCT_NBR;

-- 贷款历史数据汇总表涉指标计算
insert overwrite table fkmx.t_history_loan_data_fkmx partition(dt='${dt}')
select
    ID_NO,
    count(CONTR_NBR) as loan_total,
    count(case when max_dpd is not null and max_dpd>0 then CONTR_NBR else null end) as DKLX1073,
    count(case when max_dpd is not null and max_dpd>7 then CONTR_NBR else null end) as DKLX1077,
    count(case when max_dpd is not null and max_dpd>3 then CONTR_NBR else null end) as DKLX1078,
    count(case when max_dpd is not null and max_dpd>15 then CONTR_NBR else null end) as DKLX1079,
    round(avg(case when max_dpd is not null and max_dpd>0 then max_dpd else null end),2) as DKLX1080,
    round(stddev_pop(case when max_dpd is not null and max_dpd>0 then max_dpd else null end),2) as DKLX1084,
    max((case when rn =1 then max_dpd else null end)) as DKLX1085,
    max((case when rn <3 then max_dpd else null end)) as DKLX1086,
    max((case when rn <4 then max_dpd else null end)) as DKLX1087,
    max(case when ACTIVE_DATE is not null then datediff(now(),ACTIVE_DATE) else null end) as DKLX1088
from (
      select
        *,
        row_number() OVER (PARTITION BY ID_NO ORDER BY CREATE_TIME DESC) as rn
      from fkmx.t_history_loan_data
      where dt='${dt}'
     ) loan_data
group by ID_NO;

-- 申请主表涉及指标计算
insert  overwrite table fkmx.t_history_apply_data partition(dt='${dt}')
select
     ID_NO,
     count(case when status in ('N','J','A','E','C','S')then APP_NO else null end) as DKLX1089,
     count(APP_NO) as apply_total
from
(
    select
        *
    from ods.ods_mj_cca_tm_app_main
    where dt='${dt}'
) t
group by ID_NO;

-- 还款计划表涉及指标计算
insert overwrite table fkmx.t_history_plan_data partition(dt='${dt}')
select
    loan_data.ID_NO,
    count(loan_data.ID_NO) as DKLX1075
from fkmx.t_history_loan_data loan_data,
(
    select
        max(case when ccs_plan.rn=1 then datediff(ccs_plan.PAID_OUT_DATE,ccs_plan.PLAN_ADD_DATE) else 0 end) as over_day,
        ccs_plan.ACCT_NBR
    from
        (
            select
                ACCT_NBR,
                PLAN_ADD_DATE,
                (case when PAID_OUT_DATE is null then now() else PAID_OUT_DATE end) as PAID_OUT_DATE,
                row_number() OVER (PARTITION BY ACCT_NBR ORDER BY PLAN_ADD_DATE asc) as rn
            from ods.ods_fc_ccs_plan
            where dt='${dt}'
        ) ccs_plan
    group by ccs_plan.ACCT_NBR
) plan
where loan_data.ACCT_NBR=plan.ACCT_NBR and plan.over_day>0 and loan_data.dt='${dt}'
group by loan_data.ID_NO;

-- 贷款历史指标合并及汇总
insert overwrite table fkmx.t_history_data partition(dt='${dt}')
select
    apply.ID_NO,
    (case when loan.DKLX1073 is not null then loan.DKLX1073 else -999 end) as DKLX1073,
    round(case when loan.loan_total >0 and loan.DKLX1073 is not null then  100*loan.DKLX1073/loan.loan_total else -999 end) as DKLX1074,
    (case when plan.DKLX1075 is not null then plan.DKLX1075 else -999 end) as DKLX1075,
    round(case when loan.loan_total >0 and plan.DKLX1075 is not null then  100*plan.DKLX1075/loan.loan_total else -999 end) as DKLX1076,
    (case when loan.DKLX1077 is not null then loan.DKLX1077 else -999 end) as DKLX1077,
    (case when loan.DKLX1078 is not null then loan.DKLX1078 else -999 end) as DKLX1078,
    (case when loan.DKLX1079 is not null then loan.DKLX1079 else -999 end) as DKLX1079,
    (case when loan.DKLX1080 is not null then loan.DKLX1080 else -999 end) as DKLX1080,
    round(case when loan.loan_total >0 and loan.DKLX1077 is not null then  100*loan.DKLX1077/loan.loan_total else -999 end) as DKLX1081,
    round(case when loan.loan_total >0 and loan.DKLX1078 is not null then  100*loan.DKLX1078/loan.loan_total else -999 end) as DKLX1082,
    round(case when loan.loan_total >0 and loan.DKLX1079 is not null then  100*loan.DKLX1079/loan.loan_total else -999 end) as DKLX1083,
    (case when loan.DKLX1084 is not null then loan.DKLX1084 else -999 end) as DKLX1084,
    (case when loan.DKLX1085 is not null then loan.DKLX1085 else -999 end) as DKLX1085,
    (case when loan.DKLX1086 is not null then loan.DKLX1086 else -999 end) as DKLX1086,
    (case when loan.DKLX1087 is not null then loan.DKLX1087 else -999 end) as DKLX1087,
    (case when loan.DKLX1088 is not null then loan.DKLX1088 else -999 end) as DKLX1088,
    (case when apply.DKLX1089 is not null then apply.DKLX1089 else -999 end) as DKLX1089,
    round(case when apply.apply_total >0 and apply.DKLX1089 is not null then  100*apply.DKLX1089/apply.apply_total else -999 end) as DKLX1090
from fkmx.t_history_apply_data apply
left join fkmx.t_history_loan_data_fkmx loan
on loan.ID_NO=apply.ID_NO and apply.dt='${dt}' and loan.dt='${dt}'
left join fkmx.t_history_plan_data plan
on plan.ID_NO=apply.ID_NO and plan.dt='${dt}';

-- 贷款历史指标合并及汇总
insert overwrite table fkmx.t_history_data_all partition(dt='${dt}')
select
    b.CREATE_TIME,
    a.*
from fkmx.t_history_data a,
(
    select
        *
    from ods.ods_mj_cca_tm_app_prim_applicant_info
    where dt='${dt}'
)b
where a.ID_NO =b.ID_NO and to_date(b.CREATE_TIME)='2017-12-24' and a.dt='${dt}';

-- 生成贷款历史数据汇总表数据
insert overwrite table fkmx.t_history_loan_data_bcard partition(dt='${dt}')
select
    customer.ID_NO,
    customer.CREATE_TIME,
    loan.CONTR_NBR,
    act.ACCT_NBR,
    loan.max_dpd,
    loan.ACTIVE_DATE
from
(
    select
        *
    from ods.ods_fc_ccs_acct
    where dt='${dt}'
) act,
(
    select
        *
    from ods.ods_fc_ccs_customer
    where dt='${dt}'
) customer
,
(
    select
        *
    from ods.ods_fc_ccs_loan
    where dt='${dt}'
)loan
where act.CUST_ID=customer.CUST_ID and act.ACCT_NBR =loan.ACCT_NBR;

-- 贷款历史数据汇总表涉指标计算
insert overwrite table fkmx.t_history_loan_data_bcard_fkmx partition(dt='${dt}')
select
    ID_NO,
    count(CONTR_NBR) as loan_total,
    count(case when max_dpd is not null and max_dpd>0 then CONTR_NBR else null end) as DKLX1073,
    count(case when max_dpd is not null and max_dpd>7 then CONTR_NBR else null end) as DKLX1077,
    count(case when max_dpd is not null and max_dpd>3 then CONTR_NBR else null end) as DKLX1078,
    count(case when max_dpd is not null and max_dpd>15 then CONTR_NBR else null end) as DKLX1079,
    round(avg(case when max_dpd is not null and max_dpd>0 then max_dpd else null end),2) as DKLX1080,
    round(stddev_pop(case when max_dpd is not null and max_dpd>0 then max_dpd else null end),2) as DKLX1084,
    max((case when rn =1 then max_dpd else null end)) as DKLX1085,
    max((case when rn <3 then max_dpd else null end)) as DKLX1086,
    max((case when rn <4 then max_dpd else null end)) as DKLX1087,
    max(case when ACTIVE_DATE is not null then datediff(now(),ACTIVE_DATE) else null end) as DKLX1088
from
    (
        select
            *
            ,row_number() OVER (PARTITION BY ID_NO ORDER BY CREATE_TIME DESC) as rn
        from fkmx.t_history_loan_data
        where dt='${dt}'
    ) loan_data
group by ID_NO;

-- 申请主表涉及指标计算
insert overwrite table fkmx.t_history_apply_data_bcard_fkmx partition(dt='${dt}')
select
    ID_NO,
    count(case when status in ('N','J','A','E','C','S') then APP_NO else null end) as DKLX1089,
    count(APP_NO) as apply_total
from
(
    select
        *
    from ods.ods_mj_cca_tm_app_main
    where dt='${dt}'
)t
group by ID_NO;

-- 还款计划表涉及指标计算
insert overwrite table fkmx.t_history_plan_data_bcard_fkmx partition(dt='${dt}')
select
    loan_data.ID_NO,
    count(loan_data.ID_NO) as DKLX1075
from fkmx.t_history_loan_data loan_data,
(
    select
        max(case when ccs_plan.rn=1 then datediff(ccs_plan.PAID_OUT_DATE,ccs_plan.PLAN_ADD_DATE) else 0 end) as over_day,
        ccs_plan.ACCT_NBR
    from
        (
            select
                ACCT_NBR,
                PLAN_ADD_DATE,
                (case when PAID_OUT_DATE is null then now() else PAID_OUT_DATE end) as PAID_OUT_DATE,
                row_number() OVER (PARTITION BY ACCT_NBR ORDER BY PLAN_ADD_DATE asc) as rn
            from ods.ods_fc_ccs_plan
            where dt='${dt}'
        ) ccs_plan
    group by ccs_plan.ACCT_NBR
) plan
where loan_data.ACCT_NBR=plan.ACCT_NBR and plan.over_day>0 and loan_data.dt='${dt}'
group by loan_data.ID_NO;

-- 贷款历史指标合并及汇总
insert overwrite  table fkmx.t_history_data_bcard partition(dt='${dt}')
select
    20180103,
    apply.ID_NO,
    (case when loan.DKLX1073 is not null then loan.DKLX1073 else -999 end) as DKLX1073,
    round(case when loan.loan_total >0 and loan.DKLX1073 is not null then  100*loan.DKLX1073/loan.loan_total else -999 end) as DKLX1074,
    (case when plan.DKLX1075 is not null then plan.DKLX1075 else -999 end) as DKLX1075,
    round(case when loan.loan_total >0 and plan.DKLX1075 is not null then  100*plan.DKLX1075/loan.loan_total else -999 end) as DKLX1076,
    (case when loan.DKLX1077 is not null then loan.DKLX1077 else -999 end) as DKLX1077,
    (case when loan.DKLX1078 is not null then loan.DKLX1078 else -999 end) as DKLX1078,
    (case when loan.DKLX1079 is not null then loan.DKLX1079 else -999 end) as DKLX1079,
    (case when loan.DKLX1080 is not null then loan.DKLX1080 else -999 end) as DKLX1080,
    round(case when loan.loan_total >0 and loan.DKLX1077 is not null then  100*loan.DKLX1077/loan.loan_total else -999 end) as DKLX1081,
    round(case when loan.loan_total >0 and loan.DKLX1078 is not null then  100*loan.DKLX1078/loan.loan_total else -999 end) as DKLX1082,
    round(case when loan.loan_total >0 and loan.DKLX1079 is not null then  100*loan.DKLX1079/loan.loan_total else -999 end) as DKLX1083,
    (case when loan.DKLX1084 is not null then loan.DKLX1084 else -999 end) as DKLX1084,
    (case when loan.DKLX1085 is not null then loan.DKLX1085 else -999 end) as DKLX1085,
    (case when loan.DKLX1086 is not null then loan.DKLX1086 else -999 end) as DKLX1086,
    (case when loan.DKLX1087 is not null then loan.DKLX1087 else -999 end) as DKLX1087,
    (case when loan.DKLX1088 is not null then loan.DKLX1088 else -999 end) as DKLX1088,
    (case when apply.DKLX1089 is not null then apply.DKLX1089 else -999 end) as DKLX1089,
    round(case when apply.apply_total >0 and apply.DKLX1089 is not null then  100*apply.DKLX1089/apply.apply_total else -999 end) as DKLX1090
from fkmx.t_history_apply_data_bcard_fkmx apply
left join fkmx.t_history_loan_data_bcard_fkmx loan
on loan.ID_NO=apply.ID_NO and apply.dt='${dt}' and loan.dt='${dt}'
left join fkmx.t_history_plan_data_bcard_fkmx plan
on plan.ID_NO=apply.ID_NO and plan.dt='${dt}';
"

# 建表语句
#
# -- 新建贷款历史汇总表
# CREATE EXTERNAL TABLE fkmx.t_history_data_all
# (apply_time timestamp,
# ID_NO String,
# DKLX1073 int,
# DKLX1074 int,
# DKLX1075 int,
# DKLX1076 int,
# DKLX1077 int,
# DKLX1078 int,
# DKLX1079 int,
# DKLX1080 double,
# DKLX1081 int,
# DKLX1082 int,
# DKLX1083 int,
# DKLX1084 double,
# DKLX1085 int,
# DKLX1086 int,
# DKLX1087 int,
# DKLX1088 int,
# DKLX1089 int,
# DKLX1090 int
# )
# partitioned by(dt string)
# ROW FORMAT DELIMITED
# FIELDS TERMINATED BY ','
# STORED AS PARQUET
# location '/data1/fkmx/t_history_data_all';
#
# -- 新建贷款历史数据汇总表
# CREATE EXTERNAL TABLE fkmx.t_history_loan_data
# (ID_NO String,
# CREATE_TIME DATE,
# CONTR_NBR String,
# ACCT_NBR String,
# max_dpd int,
# ACTIVE_DATE date
# )
# partitioned by (dt string)
# ROW FORMAT DELIMITED
# FIELDS TERMINATED BY ','
# STORED AS PARQUET
# location '/data1/fkmx/t_history_loan_data/';
#
#
# -- 新建贷款历史指标
# CREATE EXTERNAL TABLE fkmx.t_history_data
# (ID_NO String,
# DKLX1073 int,
# DKLX1074 int,
# DKLX1075 int,
# DKLX1076 int,
# DKLX1077 int,
# DKLX1078 int,
# DKLX1079 int,
# DKLX1080 double,
# DKLX1081 int,
# DKLX1082 int,
# DKLX1083 int,
# DKLX1084 double,
# DKLX1085 int,
# DKLX1086 int,
# DKLX1087 int,
# DKLX1088 int,
# DKLX1089 int,
# DKLX1090 int
# )
# partitioned by (dt string)
# ROW FORMAT DELIMITED
# FIELDS TERMINATED BY ','
# STORED AS PARQUET
# location '/data1/fkmx/t_history_data';
#
# -- 贷款历史数据汇总表涉指标计算
# CREATE EXTERNAL TABLE fkmx.t_history_loan_data_fkmx
# (
#     ID_NO string,
#     loan_total int,
#     DKLX1073 int,
#     DKLX1077 int,
#     DKLX1078 int,
#     DKLX1079 int,
#     DKLX1080 double,
#     DKLX1084 double,
#     DKLX1085 int,
#     DKLX1086 int,
#     DKLX1087 int,
#     DKLX1088 int
# )
# partitioned by(dt string)
# ROW FORMAT DELIMITED
# FIELDS TERMINATED BY ','
# STORED AS PARQUET
# location '/data1/fkmx/t_history_loan_data_fkmx';
#
# -- -- 申请主表涉及指标计算
# CREATE EXTERNAL TABLE fkmx.t_history_apply_data(
#     ID_NO string,
#     DKLX1089 int,
#     apply_total int
# )
# partitioned by (dt string)
# ROW FORMAT DELIMITED
# FIELDS TERMINATED BY ','
# STORED AS PARQUET
# location '/data1/fkmx/t_history_apply_data';
#
# -- 还款计划表涉及指标计算
# CREATE EXTERNAL TABLE fkmx.t_history_plan_data(
#     ID_NO string,
#     DKLX1075 int
# )
# partitioned by(dt string)
# ROW FORMAT DELIMITED
# FIELDS TERMINATED BY ','
# STORED AS PARQUET
# location '/data1/fkmx/t_history_plan_data';
#
# -- 新建贷款历史指标
# CREATE EXTERNAL TABLE fkmx.t_history_data_bcard
# (acct_data int,
# ID_NO String,
# DKLX1073 int,
# DKLX1074 int,
# DKLX1075 int,
# DKLX1076 int,
# DKLX1077 int,
# DKLX1078 int,
# DKLX1079 int,
# DKLX1080 double,
# DKLX1081 int,
# DKLX1082 int,
# DKLX1083 int,
# DKLX1084 double,
# DKLX1085 int,
# DKLX1086 int,
# DKLX1087 int,
# DKLX1088 int,
# DKLX1089 int,
# DKLX1090 int
# )
# PARTITIONED BY(dt string)
# ROW FORMAT DELIMITED
# FIELDS TERMINATED BY ','
# STORED AS PARQUET
# location  '/data1/fkmx/t_history_data_bcard';

# -- 生成贷款历史数据汇总表数据
# CREATE EXTERNAL TABLE fkmx.t_history_loan_data_bcard(
#     ID_NO string,
#     CREATE_TIME string,
#     CONTR_NBR string,
#     ACCT_NBR string,
#     max_dpd string,
#     ACTIVE_DATE string
# )
# PARTITIONED BY(dt string)
# ROW FORMAT DELIMITED
# FIELDS TERMINATED BY ','
# STORED AS PARQUET
# location '/data1/fkmx/t_history_loan_data_bcard';

# -- 贷款历史数据汇总表涉指标计算
# CREATE EXTERNAL TABLE fkmx.t_history_loan_data_bcard_fkmx(
#     ID_NO string,
#     loan_total int,
#     DKLX1073 int,
#     DKLX1077 int,
#     DKLX1078 int,
#     DKLX1079 int,
#     DKLX1080 double,
#     DKLX1084 double,
#     DKLX1085 int,
#     DKLX1086 int,
#     DKLX1087 int,
#     DKLX1088 int
# )
# PARTITIONED BY(dt string)
# ROW FORMAT DELIMITED
# FIELDS TERMINATED BY ','
# STORED AS PARQUET
# location '/data1/fkmx/t_history_loan_data_bcard_fkmx';

# -- 申请主表涉及指标计算
# CREATE EXTERNAL TABLE fkmx.t_history_apply_data_bcard_fkmx(
#     ID_NO string,
#     DKLX1089 int,
#     apply_total int
# )
# PARTITIONED BY(dt string)
# ROW FORMAT DELIMITED
# FIELDS TERMINATED BY ','
# STORED AS PARQUET
# location '/data1/fkmx/t_history_apply_data_bcard_fkmx';
#
# -- 还款计划表涉及指标计算
# CREATE EXTERNAL TABLE fkmx.t_history_plan_data_bcard_fkmx(
#     ID_NO string,
#     DKLX1075 int
# )
# PARTITIONED BY (dt string)
# ROW FORMAT DELIMITED
# FIELDS TERMINATED BY ','
# STORED AS PARQUET
# location '/data1/fkmx/t_history_plan_data_bcard_fkmx';
