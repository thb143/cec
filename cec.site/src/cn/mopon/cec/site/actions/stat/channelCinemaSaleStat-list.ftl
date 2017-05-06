<div class="page">
    <div class="pageHeader">
   <!-- <@dwz.pageForm action="/stat/channelCinemaSaleStat-list" targetType="dialog" alt="可输入影院编码、影院名称检索" >
                <@s.hidden path="searchModel.channelId"/>
                <@s.hidden path="searchModel.startDate" class="date" dateFmt="yyyy-MM-dd" readonly="true"/>
                <@s.hidden path="searchModel.endDate" class="date" dateFmt="yyyy-MM-dd" readonly="true"/>
    </@dwz.pageForm> -->
    </div>
    <div class="pageContent">
        <table class="list" width="99%" layoutH="20">
            <thead>
            <tr>
                <th width="50" align="center" rowspan="2">序号</th>
                <th align="center" width="300" rowspan="2">影院名称</th>
                <th align="center" width="100" rowspan="2">总票数</th>
                <th align="center" width="100" rowspan="2">总金额</th>
                <th align="center" width="100" rowspan="2">退票数</th>
                <th align="center" width="100" rowspan="2">退票金额</th>
                <th align="center" width="100" rowspan="2">实际票数</th>
                <th align="center" width="100" rowspan="2">实际金额</th>
                <th align="center" width="350" colspan="${(ShowType?values)?size}">实际票数</th>
                <th align="center" width="350" colspan="${(ShowType?values)?size}">退票数</th>
            </tr>
            <tr>
            <#list ShowType?values as showType>
                <td width="90" align="center">${showType}</td>
            </#list>
            <#list ShowType?values as showType>
                <td width="90" align="center">${showType}</td>
            </#list>
            </tr>
            </thead>
            <tbody>
            <#assign totalTicket=0>
            <#assign totalMoney=0>
            <#assign refundTicket=0>
            <#assign refundMoney=0>
            <#assign actualTicket =0>
            <#assign actualMoney=0>
            <#list dailyPage.contents as daily>
            <tr>
                <td align="center">${daily_index+1}</td>
                <td>${daily.cinema.name}</td>
                <td align="right">${daily.normalOrderStat.ticketCount}<#assign totalTicket=totalTicket+daily.normalOrderStat.ticketCount></td>
                <td align="right">${daily.normalOrderStat.amount?string(",##0.00")}<#assign totalMoney=totalMoney+daily.normalOrderStat.amount></td>
                <td align="right">${daily.refundOrderStat.ticketCount}<#assign refundTicket=refundTicket+daily.refundOrderStat.ticketCount></td>
                <td align="right">${daily.refundOrderStat.revokeAmount?string(",##0.00")}<#assign refundMoney=refundMoney+daily.refundOrderStat.revokeAmount></td>
                <td align="right">${daily.normalOrderStat.ticketCount+daily.refundOrderStat.ticketCount}<#assign actualTicket=actualTicket+(daily.normalOrderStat.ticketCount+daily.refundOrderStat.ticketCount)></td>
                <td align="right">${(daily.normalOrderStat.amount+daily.refundOrderStat.revokeAmount)?string(",##0.00")}<#assign actualMoney=actualMoney+(daily.normalOrderStat.amount+daily.refundOrderStat.revokeAmount)></td>
                <#list ShowType?values as showType>
                    <td class="actualTicket_${showType}" align="right">${daily.getShowTypeStat(showType).saleCount+daily.getShowTypeStat(showType).refundCount}</td>
                </#list>
                <#list ShowType?values as showType>
                    <td class="refundTicket_${showType}" align="right">${daily.getShowTypeStat(showType).refundCount}</td>
                </#list>
            </tr>
            </#list>
            </tbody>
            <tfoot>
            <tr>
                <td></td>
                <td>合计</td>
                <td align="right">${totalTicket}</td>
                <td align="right">${totalMoney?string(",##0.00")}</td>
                <td align="right">${refundTicket}</td>
                <td align="right">${refundMoney?string(",##0.00")}</td>
                <td align="right">${actualTicket}</td>
                <td align="right">${actualMoney?string(",##0.00")}</td>
            	<#list ShowType?values as showType>
                	<td class="totalActualTicket_${showType}" align="right">0</td>
            	</#list>
           	 	<#list ShowType?values as showType>
                	<td class="totalRefundTicket_${showType}" align="right">0</td>
            	</#list>
            </tr>
            </tfoot>
        </table>
    <!--  <div class="panelBar"><@dwz.pageNav pageModel=dailyPage /></div>-->
    </div>
</div>
<script>
    <#list ShowType?values as showType>
    var totalActualTicket_${showType} = 0;
    $(".dialog").find(".actualTicket_${showType}").each(function () {
        totalActualTicket_${showType}= totalActualTicket_${showType}* 1 + $(this).text() * 1;
    });
    $(".dialog").find(".totalActualTicket_${showType}").text(totalActualTicket_${showType});

    var totalRefundTicket_${showType}= 0;
    $(".dialog").find(".refundTicket_${showType}").each(function () {
        totalRefundTicket_${showType}= totalRefundTicket_${showType}* 1 + $(this).text() * 1;
    });
    $(".dialog").find(".totalRefundTicket_${showType}").text(totalRefundTicket_${showType});
    </#list>
</script>
