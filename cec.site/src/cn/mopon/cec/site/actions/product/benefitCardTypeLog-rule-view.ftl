<div class="page">
	<div class="pageContent">
	   <@pro.benefitCardTypePolicy_head benefitCardType=benefitCardTypeLog.type/>
		<div class="pageFormContent" layoutH="111">
		   <@pro.benefitCardType_basicInfo benefitCardType=benefitCardTypeLog.type />
		   <#if benefitCardTypeLog.type.isBounded()>
		   		<@pro.benefitCardTypeRule_list benefitCardType=benefitCardTypeLog.type.boundType show=false />
		   <#else>
		   		<@pro.benefitCardTypeRule_list benefitCardType=benefitCardTypeLog.type show=false />
		   </#if>
	    </div>
	    <#if benefitCardTypeLog.status == AuditStatus.AUDIT>
	    	<@pro.benefitCardTypeAudit_bar benefitCardTypeLog=benefitCardTypeLog/>
	    <#else>
	    	<@pro.benefitCardTypeApprove_bar benefitCardTypeLog=benefitCardTypeLog/>
	    </#if>
	</div>
</div>