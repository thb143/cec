<div class="page">
     <div class="pageHeader">
        <@dwz.pageForm action="/product/show-list" targetType="div" rel="showListBox" alt="可输入排期编码、影片名称、影厅名称检索">
        	<@s.hidden path="searchModel.cinemaId" />
        	<li>状态：<@s.select path="searchModel.status" headerLabel="全部" items=ShowStatus?values itemValue="value" itemLabel="text" /></li>
            <li>放映时间：<@s.input path="searchModel.startDate" class="date" dateFmt="yyyy-MM-dd" readonly="true"/></li>
            <li>至：<@s.input path="searchModel.endDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" minRelation="#startDate"/></li>
        </@dwz.pageForm>
    </div>
    <div class="pageContent">
        <div class="panelBar">
            <ul class="toolBar">
                <@sec.any name="PRODUCT_MANAGE">
                	<#if cinema.status == EnabledStatus.ENABLED>
		                <li><@dwz.a href="/product/show-batch-gen" target="selectedTodo" rel="showIds" title="您是否确定要批量重新生成渠道排期？"><span class="a38">批量重新生成渠道排期</span></@dwz.a></li>
		            </#if>
                </@sec.any>
                <@sec.any name="PRODUCT_MANAGE">
                	<#if cinema.status == EnabledStatus.ENABLED>
		                <li><@dwz.a href="/product/show-sync?cinemaId=${searchModel.cinemaId}" target="ajaxTodo" title="您是否确定要同步该影院的排期？"><span class="a34">同步排期</span></@dwz.a></li>
	                </#if>
                </@sec.any>
            	<li><@dwz.a href="/product/showSyncLog-list?cinemaId=${searchModel.cinemaId}" target="dialog" width="L" height="L"><span class="a15">查看同步日志</span></@dwz.a></li>
            </ul>
        </div>
        <table class="table" width="100%" layoutH="112">
            <thead>
                <tr>
                    <th width="30" align="center">序号</th>
                    <th width="30" align="center"><@s.checkbox class="checkboxCtrl" group="showIds" /></th>
                    <th width="170">影院排期编码</th>
                    <th>影片名称</th>
                    <th width="70" align="center">影厅名称</th>
                    <th width="60" align="center">放映类型</th>
                    <th width="80" align="center">放映时间</th>
                    <th width="50" align="right">最低价</th>
                    <th width="50" align="right"><@s.message code="stdPrice"/></th>
                    <th width="30" align="center">状态</th>
                    <th width="192" align="center">操作</th>
                </tr>
            </thead>
            <tbody>
                <#list showPage.contents as show>
	                <tr>
	                    <td>${show_index+1}</td>
	                    <td>
		                    <#if show.isValid()>
			                   <@s.checkbox name="showIds" value="${show.id}" />
			                </#if>
	                    </td>
	                    <td><@dwz.a href="/product/show-view?show=${show.id}" target="dialog" width="L" height="L" title="查看排期">${show.code}</@dwz.a></td>
	                    <td><@dwz.a href="/operate/film-view?film=${show.film.id}" target="dialog" title="查看影片">${show.film.name}</@dwz.a></td>
	                    <td>${show.hall.name}</td>
	                    <td>${show.showType}</td>
	                    <td>${show.showTime?string('MM-dd HH:mm')}</td>
	                    <td>${show.minPrice?string("0.00")}</td>
	                    <td>${show.stdPrice?string("0.00")}</td>
	                    <td class="${show.status.color}">${show.status}</td>
	                    <td class="l-text">
	                        <@dwz.a href="/product/show-channel-list?show=${show.id}" target="dialog" rel="show-channelShow-list" width="L">查看渠道排期</@dwz.a>
	                        <@sec.any name="PRODUCT_MANAGE">
		                        <#if show.isValid()>
				                    <@dwz.a href="/product/show-gen?show=${show.id}" target="ajaxTodo" title="您是否确定要重新生成渠道排期？">重新生成渠道排期</@dwz.a>
			                    </#if>
		                    </@sec.any>
	                    </td>
	                </tr>
                </#list>
            </tbody>
        </table>
        <div class="panelBar">
            <@dwz.pageNav pageModel=showPage rel="showListBox" />
        </div>
    </div>
</div>