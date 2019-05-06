	#!/bin/bash
	####################################################
	# script in shell
	# 脚本功能：车贷预警回款FST回款率
	# 时   间：2018-1-30 14:05:13
	# 使用表：
	#         ods.ods_fc_ccs_plan
	#         ods.ods_fc_ccs_loan
	# 使用方式：随机调度
	# 责 任 人：姜云鹏
	####################################################
	
	Yesterday=`date -d "1 day ago" +%Y-%m-%d`
	last7day=`date -d "7 day ago" +%Y-%m-%d`
	last8day=`date -d "8 day ago" +%Y-%m-%d`
	
	mkdir -p /data1/share_dir/jyp/script/myc_overdue_receivedpay/data/$Yesterday
	
	#计算逻辑
	impala-shell -l -u hive --auth_creds_ok_in_clear --ldap_password_cmd='echo -n hive' -q"
	select
	      a.M,
	      case when term = 1 then 'FPD' 
	           when term = 2 then 'SPD'
	           else 'TPD' end as acq_id,
	      a.PLAN_ADD_DATE,
	      sum(a.loan_init_prin-a.PAID_PRINCIPAL),
	               sum(case when a.finsh=0 then a.loan_init_prin-a.PAID_PRINCIPAL else 0 end),
	      sum(case when a.finsh=0 then a.loan_init_prin-a.PAID_PRINCIPAL else 0 end)/sum(a.loan_init_prin-a.PAID_PRINCIPAL)
	from
	(select
	   ccsplan.acct_nbr, --账户号
	   ccsloan.loan_init_term, --分期总期数
	   ccsplan.term,    --当前期数
	   ccsplan.plan_add_date,  --计划还款日期
	   ccsplan.paid_out_date,  --实际还款日期
	   ccsloan.loan_init_prin, --分期总本金
	   ccsloan.PAID_PRINCIPAL,  --已偿还本金
	   ccsplan.M,    --逾期阶段
	   ccsplan.finsh --逾期标志
	  from
	  (SELECT distinct 
	    acct_nbr,
	    term,   
	    plan_add_date,   
	    month('$last7day')-month(plan_add_date)+1 as M,  --阶段
	    case when paid_out_date = '' then 1 else 0 end as finsh, --逾期标志 1 为未还 0 为已还 
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
	     where ccsplan.plan_add_date>'2017-10-01') a 
	 where a.M=1 and a.term in(1,2,3)
	 group by a.PLAN_ADD_DATE ,acq_id
	 " -B -o   /data1/share_dir/jyp/script/myc_overdue_receivedpay/data/$Yesterday/FST_receivepay.txt 