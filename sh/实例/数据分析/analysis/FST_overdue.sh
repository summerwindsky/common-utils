#!/bin/bash
####################################################
# script in shell
# �ű����ܣ�����Ԥ��FST���ڷ���
# ʱ   �䣺 2018-1-30 11:42:12
# ʹ�ñ�
#         ods.ods_fc_ccs_plan
#         ods.ods_fc_ccs_loan
# ʹ�÷�ʽ���������
# �� �� �ˣ�������
####################################################
today=`date -d "now" +%Y-%m-%d`
Yesterday=`date -d "1 day ago" +%Y-%m-%d`
last2day=`date -d "2 day ago" +%Y-%m-%d`
last4day=`date -d "4 day ago" +%Y-%m-%d`

mkdir -p /data1/share_dir/jyp/script/myc_overdue_receivedpay/data/$Yesterday

#�����߼�
impala-shell -l -u hive --auth_creds_ok_in_clear --ldap_password_cmd='echo -n hive' -q"
select
   a.PLAN_ADD_DATE,
   a.term,
   sum(case when a.LOAN_INIT_PRIN=a.PAID_PRINCIPAL then a.LOAN_INIT_PRIN else a.LOAN_INIT_PRIN-a.PAID_PRINCIPAL end) as ys_bj, --��ʣ�౾��
   sum(case when a.dpd <> 0 then a.LOAN_INIT_PRIN-a.PAID_PRINCIPAL else 0 end) as yq_sybj,  --���ڱ���
   sum(case when a.dpd <> 0 then a.LOAN_INIT_PRIN-a.PAID_PRINCIPAL else 0 end)/sum(case when a.LOAN_INIT_PRIN=a.PAID_PRINCIPAL then a.LOAN_INIT_PRIN else a.LOAN_INIT_PRIN-a.PAID_PRINCIPAL end) as yql
from(
select 
   ccsplan.acct_nbr, --�˻���
   ccsplan.QUAL_DUE_PRINCIPAL,   --�ۼ�Ӧ������
   ccsplan.TERM,           --��ǰ����
   ccsplan.PLAN_ADD_DATE,  --�ƻ���������
   ccsplan.PAID_OUT_DATE,  --ʵ�ʻ�������
   ccsloan.LOAN_INIT_PRIN, --�ܱ���
   ccsloan.LOAN_INIT_TERM, --����������
   ccsloan.ACTIVE_DATE,    --��������
   ccsloan.PAID_PRINCIPAL,  --�ѳ�������
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
 where dt = '$Yesterday'            --�޶�����
 and product_cd in('008803','008802') and plan_type='Q'
 and plan_add_date in ('$Yesterday','$last2day','$last4day')  --�޶����գ����գ�����
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
 and loan_code in('8803','8802')      --�޶����׳��ĺ�ͬ
 and LOAN_PROD_GROUP = 'MYCHE'
 ) ccsloan
 on ccsplan.acct_nbr = ccsloan.acct_nbr) a
 where a.PLAN_ADD_DATE = '$Yesterday'
 and a.term in (1,2,3)
  group by a.PLAN_ADD_DATE,term;
" -B -o   /data1/share_dir/jyp/script/myc_overdue_receivedpay/data/$Yesterday/FST_overdue.txt   
