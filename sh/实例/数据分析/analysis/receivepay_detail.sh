#!/bin/bash
####################################################
# script in shell
# �ű����ܣ�����Ԥ���ؿ���ϸ
# ʱ   �䣺2018-1-30 14:05:13
# ʹ�ñ�
#         ods.ods_fc_ccs_plan
#         ods.ods_fc_ccs_loan
# ʹ�÷�ʽ���������
# �� �� �ˣ�������
####################################################

Yesterday=`date -d "1 day ago" +%Y-%m-%d`
last7day=`date -d "7 day ago" +%Y-%m-%d`
last8day=`date -d "8 day ago" +%Y-%m-%d`

mkdir -p /data1/share_dir/jyp/script/myc_overdue_receivedpay/data/$Yesterday

#�����߼�
impala-shell -l -u hive --auth_creds_ok_in_clear --ldap_password_cmd='echo -n hive' -q"
  select
   ccsplan.acct_nbr, --�˻���
   ccsloan.loan_init_term, --����������
   ccsplan.term,    --��ǰ����
   ccsplan.plan_add_date,  --�ƻ���������
   ccsplan.paid_out_date,  --ʵ�ʻ�������
   ccsloan.loan_init_prin, --�����ܱ���
   ccsloan.PAID_PRINCIPAL,  --�ѳ�������
   ccsplan.M,    --���ڽ׶�
   ccsplan.finsh --���ڱ�־
  from
  (SELECT distinct 
    acct_nbr,
    term,   
    plan_add_date,   
    month('$last7day')-month(plan_add_date)+1 as M,  --�׶�
    case when paid_out_date = '' then 1 else 0 end as finsh, --���ڱ�־ 1 Ϊδ�� 0 Ϊ�ѻ� 
    paid_out_date
     from
      ods.ods_fc_ccs_plan
     where dt = '${Yesterday}'
     and (plan_add_date<paid_out_date or paid_out_date='')
     and plan_add_date='$last8day'
     and product_cd in('008803','008802') and plan_type='Q'
     ) ccsplan
     left join 
     (
     select
      acct_nbr,
      loan_init_term,
      loan_init_prin,
      PAID_PRINCIPAL
     from
      ods.ods_fc_ccs_loan 
      where dt = '${Yesterday}'
      and loan_code in('8803','8802')
      and LOAN_PROD_GROUP = 'MYCHE'
     ) ccsloan 
     on ccsplan.acct_nbr=ccsloan.acct_nbr
     where ccsplan.plan_add_date>'2017-10-01';
" -B -o   /data1/share_dir/jyp/script/myc_overdue_receivedpay/data/$Yesterday/receivepay_detail.txt   
