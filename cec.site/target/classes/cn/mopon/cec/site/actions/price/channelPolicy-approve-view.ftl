<@dwz.reload action="/price/channelPolicy-approve-view" channelPolicyLogId="${channelPolicyLog.id}" cinemaId="${cinemaId}"/>
<div class="page">
	<div class="pageContent">
		<@pri.channelPolicy_head channelPolicyLog.policy/>
	    <div class="pageFormContent" layoutH="110">
		    <@pri.channelRule_list channelPolicy=channelPolicyLog.policy treeName="channelPolicyApproveCinema" show=false/>
	   	</div>
	   	<@dwz.formBar showSubmitBtn=false showCancelBtn=false>
        	<li><@dwz.a class="button" href="/price/channelPolicy-approve-pass?channelPolicyLogId=${channelPolicyLog.id}" target="ajaxTodo" callback="navTabAjaxDone" title="您是否确定要审批通过该策略？"><span>审批通过</span></@dwz.a></li>
        	<li><@dwz.a class="button" href="/price/channelPolicy-approve-refuse?channelPolicyLogId=${channelPolicyLog.id}" target="ajaxTodo" callback="navTabAjaxDone" title="您是否确定要审批退回该策略？"><span>审批退回</span></@dwz.a></li>
        	<li><a id="collapseBtn_${channelPolicy.id}" class="button" href="javascript:expanAll('channelPolicyApproveCinema',4);"><span>全部展开/收缩</span></a></li>
        </@dwz.formBar>
	</div>
</div>