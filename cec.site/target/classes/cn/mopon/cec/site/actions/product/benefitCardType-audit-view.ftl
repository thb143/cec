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
	    <@pro.benefitCardTypeAudit_bar benefitCardTypeLog=cardTypeLog/>
	</div>
</div>