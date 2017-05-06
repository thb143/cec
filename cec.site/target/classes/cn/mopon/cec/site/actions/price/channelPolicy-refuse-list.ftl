<div class="page">
	<div class="tree-l-box" layoutH="0" style="width:20%;">
		<div class="pageHeader">
			<@dwz.pageForm action="/price/channelPolicy-refuse-list" buttonText="筛选" alt="可输入策略名称检索">
				<input type="hidden" name="selectedChannelPolicyLogId" value="${selectedChannelPolicyLogId}" />
			</@dwz.pageForm>
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
						                <@dwz.a href="/price/channelPolicy-refuse-cinema-list?channelPolicyLogId=${groupListModel.channelPolicyLog.id}" target="ajax" rel="channelPolicyRefuseBox" refusedChannelPolicyLogId="${groupListModel.channelPolicyLog.id}">
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
		    		<h3>没有退回的策略</h3>
		    	</div>
		    </#if>
		</div>
	</div>
	<div id="channelPolicyRefuseBox" class="tree-r-box" style="width:79%;"></div>
</div>
<script>
    $(function() {
    	$("a[refusedChannelPolicyLogId]").each(function() {
    		$(this).click(function() {
    			$("input[name=selectedChannelPolicyLogId]",$(this).getPageDiv()).val($(this).attr("refusedChannelPolicyLogId"));
    		});
    	});
    	var selectedPolicyLogLink = $("a[refusedChannelPolicyLogId]").eq(0);
    	if("${selectedChannelPolicyLogId}" && $("a[refusedChannelPolicyLogId=${selectedChannelPolicyLogId}]").size() > 0) {
    		selectedPolicyLogLink = $("a[refusedChannelPolicyLogId=${selectedChannelPolicyLogId}]");
    	}
    	setTimeout(function() {
    		selectedPolicyLogLink.click();
        }, 300);
    }); 
</script>