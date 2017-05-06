<div class="page">
    <div class="pageHeader">
        <@dwz.pageForm action="/system/log-list" alt="可根据操作人、日志信息、原数据、新数据进行检索。">
        <li>操作时间：<@s.input path="searchModel.startDate" class="date" dateFmt="yyyy-MM-dd" readonly="true"/></li>
        <li>至：<@s.input path="searchModel.endDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" minRelation="#startDate"/></li>
   </@dwz.pageForm>
    </div>
    <div class="pageContent">
        <table class="table" width="100%" layoutH="85">
            <thead>
                <tr>
                    <th width="130px">操作时间</th>
                    <th width="100px">操作用户</th>
                    <th>操作内容</th>
                    <th width="80px" align="center">详细日志</th>
                </tr>
            </thead>
            <tbody>
                <#list logPage.contents as log>
	                <tr>
	                    <td>${log.createDate?datetime}</td>
	                    <td>${log.creator}</td>
	                    <td style="text-align:left">${log.message}</td>
	                    <td>
	                    	<#if log.hasData()>
	                    		<@dwz.a href="/system/log-view?log=${log.id}" target="dialog" title="查看详细日志">查看</@dwz.a>
	                    	</#if>
	                    </td>
	                </tr>
                </#list>
            </tbody>
        </table>
        <div class="panelBar">
            <@dwz.pageNav pageModel=logPage />
        </div>
    </div>
</div>
