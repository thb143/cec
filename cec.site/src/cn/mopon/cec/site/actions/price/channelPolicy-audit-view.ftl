<@dwz.reload action="/price/channelPolicy-audit-view" channelPolicyLogId="${channelPolicyLog.id}" cinemaId="${cinemaId}"/>
<div class="page">
	<div class="pageContent">
		<@pri.channelPolicy_head channelPolicyLog.policy/>
	    <div class="pageFormContent" layoutH="110">
		    <@pri.channelRule_list channelPolicy=channelPolicyLog.policy treeName="channelPolicyAuditCinema" show=false/>
	   	</div>
	   	<@dwz.formBar showSubmitBtn=false showCancelBtn=false>
        	<li><@dwz.a class="button" href="/price/channelPolicy-audit-pass?channelPolicyLogId=${channelPolicyLog.id}" target="ajaxTodo" callback="navTabAjaxDone" title="您是否确定要审核通过该策略？"><span>审核通过</span></@dwz.a></li>
        	<li><@dwz.a class="button" href="/price/channelPolicy-audit-refuse?channelPolicyLogId=${channelPolicyLog.id}" target="ajaxTodo" callback="navTabAjaxDone" title="您是否确定要审核退回该策略？"><span>审核退回</span></@dwz.a></li>
        	<li><a id="collapseBtn_${channelPolicy.id}" class="button" href="javascript:expanAll('channelPolicyAuditCinema',4);"><span>全部展开/收缩</span></a></li>
        </@dwz.formBar>
	</div>
</div>