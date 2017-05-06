<div class="page">
	<div class="tree-l-box" layoutH="0" style="width:20%;">
		<div class="pageHeader">
			<@dwz.pageForm action="/price/channelPolicy-audit-list" buttonText="筛选" alt="可输入策略名称检索"/>
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
						                <@dwz.a href="/price/channelPolicy-audit-cinema-list?channelPolicyLogId=${groupListModel.channelPolicyLog.id}" target="ajax" rel="channelPolicyAuditBox">
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
		    		<h3>没有待审核的策略</h3>
		    	</div>
		    </#if>
		</div>
	</div>
	<div id="channelPolicyAuditBox" class="tree-r-box" style="width:79%;"></div>
</div>
<script>
    $(function() {
        setTimeout(function() {
            $("a[rel=channelPolicyAuditBox]").eq(0).click();
        }, 300);
    }); 
</script>