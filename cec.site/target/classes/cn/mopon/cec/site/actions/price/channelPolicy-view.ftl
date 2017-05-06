<@dwz.reload action="/price/channelPolicy-view" channelPolicyId="${channelPolicy.id}" cinemaId="${cinemaId}"/>
<div class="page">
	<div class="pageContent">
		<@pri.channelPolicy_head show=false />
	    <div class="pageFormContent" layoutH="110">
		    <@pri.channelRule_list channelPolicy=channelPolicy treeName="channelPolicyView"/>
	   	</div>
	   	<@pri.channelPolicy_bar/>
	</div>
</div>
