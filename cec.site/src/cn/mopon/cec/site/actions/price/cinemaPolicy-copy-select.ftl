<div class="page">
	<div class="pageHeader">
	<@dwz.pageForm action="/price/cinemaPolicy-copy-select" targetType="dialog" alt="可输入影院名称、策略名称检索">
	<input type="hidden" name="cinemaId" value="${cinemaId}" />
	</@dwz.pageForm>
    </div>
    <div class="pageContent">
		<table class="table" width="100%" layoutH="95">
			<thead>
				<tr>
                    <th width="30" align="center">序号</th>
                    <th width="150" align="center">影院名称</th>
                    <th width="150" align="center">策略名称</th>
                    <th width="40" align="center">启用状态</th>
                    <th width="20" align="center">操作</th>
                </tr>
            </thead>
            <tbody>
            <#list policyPage.contents as policy>
            <tr rel="${policy_index}">
            	<td>${policy_index+1}</td>
            	<td>${policy.cinema.name}</td>
            	<td>${policy.name}</td>
            	<td class="${policy.enabled.color}">${policy.enabled}</td>
            	<td><@dwz.a href="/price/cinemaPolicy-copy?policyId=${policy.id}&cinemaId=${cinemaId}" callback="dialogAjaxDone" target="ajaxTodo" title="您是否确定要复制该策略？">复制</@dwz.a></td>
            </tr>
            </#list>
            </tbody>
        </table>
        <div class="panelBar">
        	<@dwz.pageNav pageModel=policyPage targetType="dialog"/>
		</div>
    </div>
</div>