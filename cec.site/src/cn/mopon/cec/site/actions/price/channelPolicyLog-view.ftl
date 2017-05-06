<div class="page">
	<div class="pageContent">
		<div class="pageFormContent" layoutH="60">
		<#list channelPolicyLogs as channelPolicyLog>
			<@pri.channelPolicyLog_view channelPolicyLog />
		</#list>
		</div>
		<@dwz.formBar showSubmitBtn=false />
	</div>
</div>