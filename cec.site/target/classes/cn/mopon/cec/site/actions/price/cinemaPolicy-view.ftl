<@dwz.reload action="/price/cinemaPolicy-view" policyId="${cinemaPolicy.id}" />
<div class="page">
	<div class="pageContent">
		<@pri.cinemaPolicy_head show=false/>
	    <div class="pageFormContent" layoutH="110">
			<@pri.cinemaRule_list cinemaRuleList=cinemaRuleList policy=cinemaPolicy treeId="cinemaRuleSubmit"/>
	   	</div>
	   	<@pri.cinemaPolicy_bar treeId="cinemaRuleSubmit"/>
	</div>
</div>
