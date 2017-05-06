<@dwz.reload action="/price/specialRule-view" policyId="${specialPolicy.id}" />
<div class="page">
	<div class="pageContent">
    	<@pri.specialPolicy_head specialPolicy />
		<div class="pageFormContent" layoutH="110">
	        <@pri.specialRule_list specialPolicy=specialPolicy rel="specialPolicyBox" />
	    </div>
	    <@pri.specialPolicy_bar specialPolicy />
	</div>
</div>