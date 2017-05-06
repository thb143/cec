<@dwz.reload action="/price/specialPolicy-refuse-view" policyLogId="${specialPolicyLog.id}" /> 
<div class="page">
	<@pri.specialPolicy_head specialPolicyLog.policy />
	<div class="pageContent">
	    <div class="pageFormContent" layoutH="110">
	    	<@pri.specialRule_list specialPolicy=specialPolicyLog.policy rel="specialPolicyBox" />
	    </div>
	    <@pri.specialPolicy_bar specialPolicyLog.policy />
	</div>
</div>
