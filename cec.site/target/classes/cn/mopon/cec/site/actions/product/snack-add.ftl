<div class="page">
	<div class="pageHeader">
		<@dwz.pageForm action="/product/snack-add?cinema=${cinema.id}" targetType="dialog" searchModel=searchModel alt="可输入卖品类型名称、卖品内容检索"/>
    </div>
    <div class="pageContent">
    	<@dwz.form action="/product/snack-save" >
    	<input type="hidden" name="cinemaId" value="${cinema.id}" />
		<table class="table" width="100%" layoutH="96">
			<thead>
				<tr>
                    <th width="30" align="center">序号</th>
                    <th width="20"><input type="checkbox" class="checkboxCtrl" group="snackTypeIds"/></th>
                    <th width="150" align="center">卖品类型名称</th>
                    <th align="center">卖品内容</th>
                    <th width="80" align="center">卖品类型分类</th>
                </tr>
            </thead>
            <tbody id="snackTypeGroups">
            <#list snackTypes as snackType>
	            <tr rel="${snackType_index}" onclick="selectTr(event);">
	            	<td>${snackType_index+1}</td>
	            	<td><input type="checkbox" name="snackTypeIds" value="${snackType.id}"/></td>
	            	<td title="${snackType.name}">${snackType.name}</td>
	            	<td title="${snackType.remark}">${snackType.remark}</td>
	            	<td>${snackType.group.name}</td>
	            </tr>
            </#list>
            </tbody>
        </table>
        <@dwz.formBar />
        </@dwz.form>
    </div>
</div>