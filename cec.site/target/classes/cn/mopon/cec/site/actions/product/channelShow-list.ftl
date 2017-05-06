<div class="page">
     <div class="pageHeader">
        <@dwz.pageForm action="/product/channelShow-list" targetType="div" rel="channelShowListBox" alt="可输入排期编码、接入商、渠道排期编码、影院名称、影片名称、影厅名称检索">
        	<@s.hidden path="searchModel.channelId" />
        	<li>状态：<@s.select path="searchModel.status" headerLabel="全部" items=ShelveStatus?values itemValue="value" itemLabel="text" /></li>
            <li>放映时间：<@s.input path="searchModel.startDate" class="date" dateFmt="yyyy-MM-dd" readonly="true"/></li>
            <li>至：<@s.input path="searchModel.endDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" minRelation="#startDate"/></li>
        </@dwz.pageForm>
    </div>
    <div class="pageContent">
        <div class="panelBar">
            <ul class="toolBar">
                <@sec.any name="PRODUCT_SWITCH">
                	<#if channel.opened>
		                <li><@dwz.a href="/product/channelShow-batch-enable" target="selectedTodo" rel="channelShowIds" callback="navTabAjaxDone" title="您是否确定要批量上架渠道排期？"><span class="a16">批量上架</span></@dwz.a></li>
		                <li><@dwz.a href="/product/channelShow-batch-disable" target="selectedTodo" rel="channelShowIds" callback="navTabAjaxDone" title="您是否确定要批量下架渠道排期？"><span class="a17">批量下架</span></@dwz.a></li>
	                </#if>
                </@sec.any>
            </ul>
        </div>
        <table class="table" width="100%" layoutH="112">
            <thead>
                <tr>
                    <th width="30" align="center">序号</th>
                    <th width="30" align="center"><@s.checkbox class="checkboxCtrl" group="channelShowIds" /></th>
                    <th width="100" align="center">渠道排期编码</th>
                    <th width="100" align="left">影片名称</th>
                    <th>影院名称</th>
                    <th width="55" align="center">影厅名称</th>
                    <th width="55" align="center">放映类型</th>
                    <th width="80" align="center">放映时间</th>
                    <th width="45" align="right">最低价</th>
                    <th width="45" align="right"><@s.message code="stdPrice"/></th>
					<th width="70" align="right"><@s.message code="channelPrice"/></th>
					<th width="45" align="right"><@s.message code="submitPrice"/></th>
					<th width="45" align="right"><@s.message code="connectFee"/></th>
					<th width="45" align="right"><@s.message code="circuitFee"/></th>
					<th width="45" align="right"><@s.message code="subsidyFee"/></th>
                    <th width="45" align="center">状态</th>
                    <th width="45" align="left">操作</th>
                </tr>
            </thead>
            <tbody>
                <#list channelShowPage.contents as channelShow>
	                <tr>
	                    <td>${channelShow_index+1}</td>
	                    <td>
		                    <#if channelShow.isOnable() || channelShow.isOffable()>
			                   <@s.checkbox name="channelShowIds" value="${channelShow.id}" />
			                </#if>
	                    </td>
	                    <td><@dwz.a href="/product/channelShow-view?channelShow=${channelShow.id}" target="dialog" width="L" height="L" title="查看渠道排期">${channelShow.code}</@dwz.a></td>
	                    <td><@dwz.a href="/operate/film-view?film=${channelShow.film.id}" target="dialog" title="查看影片">${channelShow.film.name}</@dwz.a></td>
	                    <td>${channelShow.cinema.name}</td>
	                    <td>${channelShow.hall.name}</td>
	                    <td>${channelShow.showType}</td>
	                    <td>${channelShow.showTime?string('MM-dd HH:mm')}</td>
	                    <td>${channelShow.minPrice?string(",##0.00")}</td>
						<td>${channelShow.stdPrice?string(",##0.00")}</td>
						<td>${channelShow.channelPrice?string(",##0.00")}</td>
						<td>${channelShow.submitPrice?string(",##0.00")}</td>
						<td>${channelShow.connectFee?string(",##0.00")}</td>
						<#if channelShow.circuitFee gt 0>
							<#assign circuitFeeColor = StatusColor.GREEN>
						<#else>
							<#assign circuitFeeColor = StatusColor.BLACK>
						</#if>
						<td class="${circuitFeeColor}">${channelShow.circuitFee?string(",##0.00")}</td>
						<#if channelShow.subsidyFee gt 0>
							<#assign subsidyFeeColor = StatusColor.RED>
						<#else>
							<#assign subsidyFeeColor = StatusColor.BLACK>
						</#if>
						<td class="${subsidyFeeColor}">${channelShow.subsidyFee?string(",##0.00")}</td>
	                    <td class="${channelShow.status.color}">${channelShow.status}</td>
	                    <td>
	                    	<@sec.any name="PRODUCT_SWITCH">
		                        <#if channelShow.isOffable()>
		                        	<@dwz.a href="/product/channelShow-disable?channelShow=${channelShow.id}" target="ajaxTodo" title="您是否确定要下架该渠道排期？">下架</@dwz.a>
		                        </#if>
		                        <#if channelShow.isOnable()>
		                        	<@dwz.a href="/product/channelShow-enable?channelShow=${channelShow.id}" target="ajaxTodo" title="您是否确定要上架该渠道排期？">上架</@dwz.a>
		                        </#if>
	                        </@sec.any>
	                    </td>
	                </tr>
                </#list>
            </tbody>
        </table>
        <div class="panelBar">
            <@dwz.pageNav pageModel=channelShowPage rel="channelShowListBox" />
        </div>
    </div>
</div>