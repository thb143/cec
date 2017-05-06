<div class="page">
	<div class="pageContent">
		<div class="pageFormContent" layoutH="30">
		<#list policyLogs as policyLog>
		<@pri.cinemaPolicyLog_view cinemaPolicyLog=policyLog />
		</#list>
		</div>
	</div>
</div>