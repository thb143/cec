<div class="page">
	<div class="pageHeader">
	<@dwz.pageForm action="/price/channelPolicy-copy-select" targetType="dialog" alt="可输入渠道名称、策略名称检索">
	<input type="hidden" name="channelId" value="${channelId}" />
	</@dwz.pageForm>
    </div>
    <div class="pageContent">
		<table class="table" width="100%" layoutH="95">
			<thead>
				<tr>
                    <th width="30" align="center">序号</th>
                    <th width="150" align="center">渠道名称</th>
                    <th align="center">策略名称</th>
                    <th width="80" align="center">启用状态</th>
                    <th width="80" align="center">操作</th>
                </tr>
            </thead>
            <tbody>
            <#list policyPage.contents as channelPolicy>
            <tr rel="${channelPolicy_index}">
            	<td>${channelPolicy_index+1}</td>
            	<td>${channelPolicy.channel.name}</td>
            	<td>${channelPolicy.name}</td>
            	<td class="${channelPolicy.enabled.color}">${channelPolicy.enabled}</td>
            	<td><@dwz.a href="/price/channelPolicy-copy?channelPolicyId=${channelPolicy.id}&channelId=${channelId}" callback="dialogAjaxDone" target="ajaxTodo" title="您是否确定要复制该策略？">复制</@dwz.a></td>
            </tr>
            </#list>
            </tbody>
        </table>
        <div class="panelBar">
        	<@dwz.pageNav pageModel=policyPage targetType="dialog"/>
		</div>
    </div>
</div>