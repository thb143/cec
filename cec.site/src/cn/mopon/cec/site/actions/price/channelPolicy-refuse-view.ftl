<@dwz.reload action="/price/channelPolicy-refuse-view" channelPolicyLogId="${channelPolicyLog.id}" cinemaId="${cinemaId}"/>
<div class="page">
	<@pri.channelPolicy_head channelPolicyLog.policy/>
	<div class="pageContent">
	    <div class="pageFormContent" layoutH="110">
		    <@pri.channelRule_list channelPolicy=channelPolicyLog.policy treeName="channelPolicyRefuseCinema" show=false />
	   	</div>
	   	<@pri.channelPolicy_bar channelPolicy=channelPolicyLog.policy treeName="channelPolicyRefuseCinema"/>
	</div>
</div>