<div class="page">
	<div class="pageContent">
    	<@pri.specialPolicy_head specialPolicyLog.policy />
		<div class="pageFormContent" layoutH="110">
	        <@pri.specialRule_list specialPolicy=specialPolicyLog.policy rel="specialPolicyBox" />
	    </div>
	    <@dwz.formBar showSubmitBtn=false showCancelBtn=false>
        	<li>
                <@dwz.a class="button" href="/price/specialPolicy-audit-pass?policyLogId=${specialPolicyLog.id}" target="ajaxTodo" callback="navTabAjaxDone" title="您是否确定要审核通过该策略？"><span>审核通过</span></@dwz.a>
            </li>
            <li>
                <@dwz.a class="button" href="/price/specialPolicy-audit-refuse?policyLogId=${specialPolicyLog.id}" target="dialog" width="S" height="SS"><span>审核退回</span></@dwz.a>
            </li>
           	<#if specialPolicyLog.policy.rules?size gt 0>
		        <li>
		            <a class="button" href="javascript:void(0);" onclick="policyStretch('policyStretch');" id="policyStretch" name="close"><span>全部展开/收缩</span></a>
		        </li>
        	</#if>
        </@dwz.formBar>
	</div>
</div>