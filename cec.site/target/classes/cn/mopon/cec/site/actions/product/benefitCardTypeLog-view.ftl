<div class="page">
	<div class="pageContent">
		<div class="pageFormContent" layoutH="60">
		<#list benefitCardTypeLogs as benefitCardTypeLog>
			<@pro.benefitCardTypeLog_view benefitCardTypeLog />
		</#list>
		</div>
		<@dwz.formBar showSubmitBtn=false />
	</div>
</div>