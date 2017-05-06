<divss="page">
<div class="pageHeader">
<@dwz.pageForm action="/stat/channelTicketOrderStat-view" targetType="dialog" alt="可输入订单号、影院订单号、影院名称检索" >
    <@s.hidden path="searchModel.startDate" />
    <@s.hidden path="searchModel.endDate" />
    <@s.hidden path="searchModel.channelId" />
    <li>
        放映类型：<@s.select path="searchModel.showType" items=ShowType?values itemValue="value" itemLabel="text" class="required" headerLabel="全部"/></li>
</@dwz.pageForm>
</div>
<div class="pageContent">
    <table class="list" layoutH="65" width="100%">
    <thead>
	        <tr>
	        	<th width="30" align="center">序号</th>
	            <th width="120" align="center">订单号</th>
	            <th width="120" align="center">影院订单号</th>
	            <th width="120" align="center">影院名称</th>
	            <th width="60" align="center">订单类型</th>
	            <th width="50" align="center">放映类型</th>
	            <th width="60" align="center">订单类别</th>
	            <th width="40" align="center">购票数</th>
	            <th width="50" align="center">订单金额</th>
	            <th width="50" align="center"><@s.message code="cinemaAmount"/></th>
	            <th width="50" align="center"><@s.message code="channelAmount"/></th>
	            <th width="50" align="center"><@s.message code="submitAmount"/></th>
	            <th width="50" align="center"><@s.message code="serviceFee"/></th>
	            <th width="50" align="center"><@s.message code="connectFee"/></th>
	            <th width="50" align="center"><@s.message code="channelFee"/></th>
	            <th width="50" align="center"><@s.message code="circuitFee"/></th>
	            <th width="50" align="center"><@s.message code="subsidyFee"/></th>
	            <th width="130" align="center">出票时间</th>
	            <th width="130" align="center">退票时间</th>
	        </tr>
        </thead>
	    <#assign ticketCount=0>
	    <#assign refundCount=0>
	    <#assign amount=0>
	    <#assign cinemaAmount=0>
	    <#assign channelAmount=0>
	    <#assign submitAmount=0>
	    <#assign serviceFee=0>
	    <#assign connectFee=0>
	    <#assign channelFee=0>
	    <#assign circuitFee=0>
	    <#assign subsidyFee=0>
	    <#list channelTicketPage.contents as orderStat>
        <tr>
         	<td align="center">${orderStat_index+1}</td>
            <td align="center">${orderStat.code}</td>
            <td align="center">${orderStat.cinemaOrderCode}</td>
            <td align="left">${orderStat.cinema.name}</td>
            <td align="center">${orderStat.type}</td>
            <td align="center">${orderStat.showType}</td>
            <td align="center" class="${orderStat.kind.color}">${orderStat.kind}</td>
            <td align="right">${orderStat.ticketCount}<#assign ticketCount=ticketCount+orderStat.ticketCount></td>
            <td align="right">${orderStat.amount?string(",##0.00")}<#assign amount=amount+orderStat.amount></td>
            <td align="right">${orderStat.cinemaAmount?string(",##0.00")}<#assign cinemaAmount=cinemaAmount+orderStat.cinemaAmount></td>
            <td align="right">${orderStat.channelAmount?string(",##0.00")}<#assign channelAmount=channelAmount+orderStat.channelAmount></td>
            <td align="right">${orderStat.submitAmount?string(",##0.00")}<#assign submitAmount=submitAmount+orderStat.submitAmount></td>
            <td align="right">${orderStat.serviceFee?string(",##0.00")}<#assign serviceFee=serviceFee+orderStat.serviceFee></td>
            <td align="right">${orderStat.connectFee?string(",##0.00")}<#assign connectFee=connectFee+orderStat.connectFee></td>
            <td align="right">${orderStat.channelFee?string(",##0.00")}<#assign channelFee=channelFee+orderStat.channelFee></td>
            <td align="right">${orderStat.circuitFee?string(",##0.00")}<#assign circuitFee=circuitFee+orderStat.circuitFee></td>
            <td align="right">${orderStat.subsidyFee?string(",##0.00")}<#assign subsidyFee=subsidyFee+orderStat.subsidyFee></td>
            <td align="center">${(orderStat.confirmTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
            <td align="center">${(orderStat.revokeTime?string("yyyy-MM-dd HH:mm:ss"))!}</td>
        </tr>
    </#list>
        </tbody>
        <tfoot>
        <tr>
        	<td align="center"></td>
            <td align="center"></td>
            <td align="center"></td>
            <td align="center"></td>
             <td align="center"></td>
            <td align="center" colspan="2">当前页合计</td>
            <td align="right">${ticketCount}</td>
            <td align="right">${amount?string(",##0.00")}</td>
            <td align="right">${cinemaAmount?string(",##0.00")}</td>
            <td align="right">${channelAmount?string(",##0.00")}</td>
            <td align="right">${submitAmount?string(",##0.00")}</td>
            <td align="right">${serviceFee?string(",##0.00")}</td>
            <td align="right">${connectFee?string(",##0.00")}</td>
            <td align="right">${channelFee?string(",##0.00")}</td>
            <td align="right">${circuitFee?string(",##0.00")}</td>
            <td align="right">${subsidyFee?string(",##0.00")}</td>
            <td align="center"></td>
            <td align="center"></td>
        </tr>
        </tfoot>
    </table>
    <div class="panelBar">
    <@dwz.pageNav pageModel=channelTicketPage targetType="dialog"/>
    </div>
</div>
</div>

