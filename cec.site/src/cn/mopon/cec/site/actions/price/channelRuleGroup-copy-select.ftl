<div class="page">
	<div class="pageHeader">
		<@dwz.pageForm action="/price/channelRuleGroup-copy-select?groupId=${groupId}&policyId=${policyId}" targetType="dialog" searchModel=searchModel alt="可输入影院编码、影院名称检索"/>
    </div>
    <div class="pageContent">
    	<@dwz.form action="/price/channelRuleGroup-copy" >
    	<input type="hidden" name="groupId" value="${groupId}" />
    	<input type="hidden" name="policyId" value="${policyId}" />
		<table class="table" width="100%" layoutH="96">
			<thead>
				<tr>
                    <th width="30" align="center">序号</th>
                    <th width="20"><input type="checkbox" class="checkboxCtrl" group="groupIds"/></th>
                    <th width="150" align="center">影院编码</th>
                    <th align="center">影院名称</th>
                    <th width="80" align="center">开放状态</th>
                </tr>
            </thead>
            <tbody id="ruleGroups">
            <#list groupPage as group>
	            <tr rel="${group_index}" onclick="selectTr(event);">
	            	<td>${group_index+1}</td>
	            	<td><input type="checkbox" name="groupIds" value="${group.id}"/></td>
	            	<td>${group.cinema.code}</td>
	            	<td>${group.cinema.name}</td>
	            	<td class="${group.status.color}">${group.status}</td>
	            </tr>
            </#list>
            </tbody>
        </table>
        <@dwz.formBar />
        </@dwz.form>
    </div>
</div>