<div class="page">
	<div class="pageContent">
    	<@pri.specialPolicy_head specialPolicyLog.policy />
		<div class="pageFormContent" layoutH="110">
	        <@pri.specialRule_list specialPolicy=specialPolicyLog.policy rel="specialPolicyBox" />
	    </div>
	    <@dwz.formBar showSubmitBtn=false showCancelBtn=false>
        	<li>
                <@dwz.a class="button" href="/price/specialPolicy-approve-pass?policyLogId=${specialPolicyLog.id}" target="ajaxTodo" callback="navTabAjaxDone" title="您是否确定要审批通过该策略？"><span>审批通过</span></@dwz.a>
            </li>
            <li>
                <@dwz.a class="button" href="/price/specialPolicy-approve-refuse?policyLogId=${specialPolicyLog.id}" target="dialog" width="S" height="SS"><span>审批退回</span></@dwz.a>
            </li>
            <#if specialPolicyLog.policy.rules?size gt 0>
		        <li>
		            <a class="button" href="javascript:void(0);" onclick="policyStretch('policyStretch');" id="policyStretch" name="close"><span>全部展开/收缩</span></a>
		        </li>
        	</#if>
        </@dwz.formBar>
	</div>
</div>