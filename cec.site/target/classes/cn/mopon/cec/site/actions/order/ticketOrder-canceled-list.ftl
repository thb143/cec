<div class="page">
    <div class="pageHeader">
        <@dwz.pageForm action="/order/ticketOrder-canceled-list" alt="可输入订单号、影院订单号、接入商、影院名称、影院排期编号、渠道排期编号、手机号码检索">
        	<@ord.ticketOrderSearch searchModel=searchModel />
        </@dwz.pageForm>
    </div>
    <div class="pageContent">
        <table class="table" width="100%" layoutH="85">
            <thead>
                <tr>
                    <th width="30" align="center">序号</th>
                    <th width="125" align="center">平台订单号</th>
                    <th>影院名称</th>
                    <th width="80">渠道名称</th>
                    <th width="50">接入商</th>
                    <th width="55" align="center">订单类型</th>
                    <th width="80" align="center">创建时间</th>
                    <th width="80" align="center">过期时间</th>
					<th width="85" align="right"><@s.message code="channelAmount"/></th>
					<th width="60" align="right"><@s.message code="submitAmount"/></th>
					<th width="50" align="right"><@s.message code="connectFee"/></th>
					<th width="50" align="right"><@s.message code="circuitFee"/></th>
					<th width="50" align="right"><@s.message code="subsidyFee"/></th>
                </tr>
            </thead>
            <tbody>
            	<#list orderPages.contents as order>
	                <tr>
	                    <td>${order_index+1}</td>
	                    <td>
	                    	<@dwz.a href="/order/ticketOrder-view?ticketOrder=${order.id}" target="dialog" title="查看订单" width="L" height="L">${order.code}</@dwz.a>
	                    </td>
	                    <td>${order.cinema.name}</td>
	                    <td>${order.channel.name}</td>
	                    <td>${order.provider}</td>
	                    <td>${order.type}</td>
	                    <td>${order.createTime?string("MM-dd HH:mm")}</td>
	                    <td>${order.expireTime?string("MM-dd HH:mm")}</td>
	                    <td>${order.channelAmount?string(",##0.00")}</td>
	                    <td>${order.submitAmount?string(",##0.00")}</td>
	                    <td>${order.connectFee?string(",##0.00")}</td>
	                    <#if order.circuitFee gt 0>
							<#assign circuitFeeColor = StatusColor.GREEN>
						<#else>
							<#assign circuitFeeColor = StatusColor.BLACK>
						</#if>
						<td class="${circuitFeeColor}">${order.circuitFee?string(",##0.00")}</td>
						<#if order.subsidyFee gt 0>
							<#assign subsidyFeeColor = StatusColor.RED>
						<#else>
							<#assign subsidyFeeColor = StatusColor.BLACK>
						</#if>
						<td class="${subsidyFeeColor}">${order.subsidyFee?string(",##0.00")}</td>
	                </tr>
                </#list>
            </tbody>
        </table>
        <div class="panelBar">
           	<@dwz.pageNav pageModel=orderPages />
        </div>
    </div>
</div>