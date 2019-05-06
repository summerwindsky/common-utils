#!/bin/bash
####################################################
# script in shell
# 脚本功能：车贷预警逾期明细
# 时   间：2018-1-30 13:41:06
# 使用表：
#         ods.ods_fc_ccs_plan
#         ods.ods_fc_ccs_loan
# 使用方式：随机调度
# 责 任 人：姜云鹏
####################################################
today=`date -d "now" +%Y-%m-%d`
Yesterday=`date -d "1 day ago" +%Y-%m-%d`
last2day=`date -d "2 day ago" +%Y-%m-%d`
last4day=`date -d "4 day ago" +%Y-%m-%d`

mkdir -p /data1/share_dir/jyp/script/myc_overdue_receivedpay/data/$Yesterday

#计算逻辑
impala-shell -l -u hive --auth_creds_ok_in_clear --ldap_password_cmd='echo -n hive' -q"
select 
   ccsplan.acct_nbr, --账户号
   ccsplan.QUAL_DUE_PRINCIPAL,   --累计应还本金
   ccsplan.TERM,           --当前期数
   ccsplan.PLAN_ADD_DATE,  --计划还款日期
   ccsplan.PAID_OUT_DATE,  --实际还款日期
   ccsloan.LOAN_INIT_PRIN, --总本金
   ccsloan.LOAN_INIT_TERM, --分期总期数
   ccsloan.ACTIVE_DATE,    --激活日期
   ccsloan.PAID_PRINCIPAL,  --已偿还本金
   case when ccsplan.PAID_OUT_DATE <> '' and ccsplan.PAID_OUT_DATE>=ccsplan.PLAN_ADD_DATE then datediff(ccsplan.PAID_OUT_DATE,ccsplan.PLAN_ADD_DATE)
     when ccsplan.PAID_OUT_DATE =  '' and ccsplan.PLAN_ADD_DATE<'$today' then datediff('$today',ccsplan.PLAN_ADD_DATE)
     when ccsplan.PAID_OUT_DATE <> '' and ccsplan.PAID_OUT_DATE<ccsplan.PLAN_ADD_DATE then 0
     when ccsplan.PAID_OUT_DATE =  '' and ccsplan.PLAN_ADD_DATE>='$today' then 0
     else 0 end as dpd
from
 (select 
   acct_nbr,
   QUAL_DUE_PRINCIPAL,
   term,
   PLAN_ADD_DATE,
   PAID_OUT_DATE
  from
  ods.ods_fc_ccs_plan
 where dt = '$Yesterday'            --限定分区
 and product_cd in('008803','008802') and plan_type='Q'
 and plan_add_date in ('$Yesterday','$last2day','$last4day')  --限定当日，次日，三日
 )ccsplan
 left join
 (select
    LOAN_INIT_PRIN,
    acct_nbr,
    LOAN_INIT_TERM,
    ACTIVE_DATE,
    PAID_PRINCIPAL
  from
  ods.ods_fc_ccs_loan
 where dt = '$Yesterday'
 and loan_code in('8803','8802')      --限定美易车的合同
 and LOAN_PROD_GROUP = 'MYCHE'
 ) ccsloan
 on ccsplan.acct_nbr = ccsloan.acct_nbr;
" -B -o   /data1/share_dir/jyp/script/myc_overdue_receivedpay/data/$Yesterday/overdue_detail.txt   
