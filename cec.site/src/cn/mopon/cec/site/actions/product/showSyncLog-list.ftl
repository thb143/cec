<div class="page">
    <div class="pageHeader">
        <@dwz.pageForm action="/product/showSyncLog-list" targetType="dialog" alt="可输入排期编码、影厅编码、影片编码检索">
        	<@s.hidden path="searchModel.cinemaId" />
        	<li>同步状态：<@s.select path="searchModel.status" items=SyncStatus?values itemValue="value" itemLabel="text" headerLabel="全部" /></li>
        	<li>同步时间：<@s.input path="searchModel.startDate" class="date" dateFmt="yyyy-MM-dd" readonly="true"/></li>
        	<li>至：<@s.input path="searchModel.endDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" minRelation="#startDate"/></li>
        </@dwz.pageForm>
    </div>
    <div class="pageContent">
        <table class="table" width="100%" layoutH="88">
            <thead>
                <tr>
                    <th width="30" align="center">序号</th>
                    <th width="130" align="center">同步时间</th>
                    <th width="80" align="center">耗时</th>
                    <th width="80" align="center">处理数量</th>
                    <th width="80" align="center">新增数量</th>
                    <th width="80" align="center">更新数量</th>
                    <th width="80" align="center">删除数量</th>
                    <th width="80" align="center">异常数量</th>
                    <th width="80" align="center">同步状态</th>
                    <th>操作</th>
                </tr>
            </thead>
            <tbody>
                <#list showSyncLogPage.contents as showSyncLog>
	                <tr>
	                    <td>${showSyncLog_index+1}</td>
	                    <td>${showSyncLog.syncTime?datetime}</td>
	                    <td>${(showSyncLog.duration/1000)?string("0.000")}</td>
	                    <td>${showSyncLog.processCount}</td>
	                    <td>${showSyncLog.createCount}</td>
	                    <td>${showSyncLog.updateCount}</td>
	                    <td>${showSyncLog.deleteCount}</td>
	                    <td>${showSyncLog.errorCount}</td>
	                    <td class="${showSyncLog.status.color}">${showSyncLog.status}</td>
	                    <td>
	                    	<#if showSyncLog.status == SyncStatus.ERROR>
	                        	<@dwz.a href="/product/showErrorLog-list?showSyncLogId=${showSyncLog.id}" target="dialog" width="L" height="L">查看异常排期</@dwz.a>
	                        </#if>
	                        <#if showSyncLog.status == SyncStatus.FAILED>
	                        	<@dwz.a href="/product/showSyncLog-view?showSyncLogId=${showSyncLog.id}" target="dialog" width="L" height="L">查看异常信息</@dwz.a>
	                        </#if>
	                    </td>
	                </tr>
                </#list>
            </tbody>
        </table>
        <div class="panelBar">
            <@dwz.pageNav pageModel=showSyncLogPage targetType="dialog" />
        </div>
    </div>
</div>