<div class="page">
	<div class="pageContent">
	   <@pro.benefitCardTypePolicy_head benefitCardType=cardTypeLog.type/>
		<div class="pageFormContent" layoutH="111">
			<@pro.benefitCardType_basicInfo benefitCardType=cardTypeLog.type />
			<!-- 
			<#if cardTypeLog.type.isBounded()>
		   		<@pro.benefitCardTypeRule_list benefitCardType=cardTypeLog.type.boundType show=false/>
		   <#else>
		   		<@pro.benefitCardTypeRule_list benefitCardType=cardTypeLog.type show=false/>
		   </#if>
		   -->
	    </div>
	    <@dwz.formBar showSubmitBtn=false showCancelBtn=false>
        	<li>
                <@dwz.a class="button" href="/product/benefitCardType-approve-pass?cardTypeLogId=${cardTypeLog.id}" target="ajaxTodo" callback="navTabAjaxDone" title="您是否确定要审批通过该卡类？"><span>审批通过</span></@dwz.a>
            </li>
            <li>
                <@dwz.a class="button" href="/product/benefitCardType-approve-refuse?cardTypeLogId=${cardTypeLog.id}" target="dialog" width="S" height="SS"><span>审批退回</span></@dwz.a>
            </li>
           	<#if cardTypeLog.type.isBounded()>
	           	<#if cardTypeLog.type.boundType.rules?size gt 0>
	           		<li>
	           		    <a class="button" href="javascript:void(0);" onclick="policyStretch('policyStretch');" id="policyStretch" name="close"><span>全部展开/收缩</span></a>
	           		</li>
	        	</#if>
	        <#else>
	        	<#if cardTypeLog.type.rules?size gt 0>
	           		<li>
	           		    <a class="button" href="javascript:void(0);" onclick="policyStretch('policyStretch');" id="policyStretch" name="close"><span>全部展开/收缩</span></a>
	           		</li>
	        	</#if>
        	</#if>
        </@dwz.formBar>
	</div>
</div>