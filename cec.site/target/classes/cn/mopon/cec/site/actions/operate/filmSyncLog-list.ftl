<div class="page">
    <div class="pageHeader">
        <@dwz.pageForm action="/operate/filmSyncLog-list" targetType="dialog" alt="可输入影片编码、影片名称检索">
        	<li>同步状态：<@s.select path="searchModel.status" items=SyncStatus?values itemValue="value" itemLabel="text" headerLabel="全部" /></li>
        	<li>同步时间：<@s.input path="searchModel.startDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" /></li>
        	<li>至：<@s.input path="searchModel.endDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" minRelation="#startDate" /></li>
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
                    <th width="80" align="center">异常数量</th>
                    <th width="80" align="center">同步状态</th>
                    <th align="center">操作</th>
                </tr>
            </thead>
            <tbody>
                <#list filmSyncLogPage.contents as filmSyncLog>
	                <tr>
	                    <td>${filmSyncLog_index+1}</td>
	                    <td>${filmSyncLog.syncTime?datetime}</td>
	                    <td>${(filmSyncLog.duration/1000)?string("0.000")}</td>
	                    <td>${filmSyncLog.processCount}</td>
	                    <td>${filmSyncLog.createCount}</td>
	                    <td>${filmSyncLog.updateCount}</td>
	                    <td>${filmSyncLog.errorCount}</td>
	                    <td class="${filmSyncLog.status.color}">${filmSyncLog.status}</td>
	                    <td>
	                    	<#if filmSyncLog.status == SyncStatus.ERROR>
	                        	<@dwz.a href="/operate/filmErrorLog-list?filmSyncLogId=${filmSyncLog.id}" target="dialog" width="L" height="L">查看异常影片</@dwz.a>
	                        </#if>
	                        <#if filmSyncLog.status == SyncStatus.FAILED>
	                        	<@dwz.a href="/operate/filmSyncLog-view?filmSyncLogId=${filmSyncLog.id}" target="dialog" width="L" height="L">查看异常信息</@dwz.a>
	                        </#if>
	                    </td>
	                </tr>
                </#list>
            </tbody>
        </table>
        <div class="panelBar">
            <@dwz.pageNav pageModel=filmSyncLogPage targetType="dialog" />
        </div>
    </div>
</div>