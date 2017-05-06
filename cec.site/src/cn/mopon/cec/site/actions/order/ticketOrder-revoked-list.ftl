<div class="page">
    <div class="pageHeader">
        <@dwz.pageForm action="/order/ticketOrder-revoked-list" alt="可输入订单号、影院订单号、接入商、影院名称、影院排期编号、渠道排期编号、手机号码检索">
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
                    <th width="80" align="center">确认时间</th>
                    <th width="80" align="center">退票时间</th>
                    <th width="55" align="right"><@s.message code="amount"/></th>
                    <th width="75" align="right"><@s.message code="ticketAmount"/></th>
                    <th width="55" align="right"><@s.message code="snackAmount"/></th>
					<th width="85" align="right"><@s.message code="channelAmount"/></th>
					<th width="60" align="right"><@s.message code="submitAmount"/></th>
					<th width="50" align="right"><@s.message code="serviceFee"/></th>
					<th width="50" align="right"><@s.message code="connectFee"/></th>
					<th width="50" align="right"><@s.message code="circuitFee"/></th>
					<th width="50" align="right"><@s.message code="subsidyFee"/></th>
					<th width="50" align="right"><@s.message code="channelFee"/></th>
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
	                    <td>${order.confirmTime?string("MM-dd HH:mm")}</td>
	                    <td>${order.revokeTime?string("MM-dd HH:mm")}</td>
	                    <td>${order.amount?string(",##0.00")}</td>
	                    <td>${order.ticketAmount?string(",##0.00")}</td>
	                    <td>
	                    	<#if order.snackOrder != null>
	                    		${order.snackOrder.amount?string(",##0.00")}
	                    	<#else>
	                    		0.00
	                    	</#if>
	                    </td>
	                    <td>${order.channelAmount?string(",##0.00")}</td>
	                    <td>${order.submitAmount?string(",##0.00")}</td>
	                    <td>${order.serviceFee?string(",##0.00")}</td>
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
						<td>${order.channelFee?string(",##0.00")}</td>
	                </tr>
                </#list>
            </tbody>
        </table>
        <div class="panelBar">
           	<@dwz.pageNav pageModel=orderPages />
        </div>
    </div>
</div>