<div class="page">
	<div class="tree-l-box" layoutH="0" style="width:20%;">
		<div class="pageHeader">
			<@dwz.pageForm action="/price/channelPolicy-approve-list" buttonText="筛选" alt="可输入策略名称检索" />
	    </div>
		<div class="pageContent" layoutH="40">
			<#if channelChannelPolicyListModel.items?size gt 0>
				<ul class="tree expand">
					<#list channelChannelPolicyListModel.items as item>
						<li>
				            <a>${item.channel.name}</a>
				            <ul>
				                <#list item.items as groupListModel>
					                <li>
						                <@dwz.a href="/price/channelPolicy-approve-cinema-list?channelPolicyLogId=${groupListModel.channelPolicyLog.id}" target="ajax" rel="channelPolicyApproveBox">
								            ${groupListModel.channelPolicyLog.policy.name}[${groupListModel.count}]
								        </@dwz.a>
						            </li>
				                </#list>
				            </ul>
				        </li>
			        </#list>
			    </ul>
		    <#else>
		    	<div class="tree-msg">
		    		<h3>没有待审批的策略</h3>
		    	</div>
		    </#if>
		</div>
	</div>
	<div id="channelPolicyApproveBox" class="tree-r-box" style="width:79%;"></div>
</div>
<script>
    $(function() {
        setTimeout(function() {
            $("a[rel=channelPolicyApproveBox]").eq(0).click();
        }, 300);
    }); 
</script>