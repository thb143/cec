<div class="page">
    <div class="pageHeader">
    <@dwz.pageForm action="/stat/cinemaSaleStat-list"  alt="可输入影院编码、影院名称检索">
        <li>统计日期：<@s.input path="searchModel.startDate" class="date" dateFmt="yyyy-MM-dd" readonly="true"/></li>
        <li>至：<@s.input path="searchModel.endDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" minRelation="#startDate"/></li>
    </@dwz.pageForm>
    </div>
    <div class="pageContent" id="cinemaSaleStat">
        <div class="panelBar">
            <ul class="toolBar">
                <li>
                	<@dwz.a href="/stat/cinemaSaleStat-export?startDate=${searchModel.startDateStr?date}&endDate=${searchModel.endDateStr?date}"
                		target="dwzExport" targettype="navTab" class="exportLink"  title="您是否确定要导出报表？"><span class="a17">导出</span>
                	</@dwz.a>
                </li>
            </ul>
        </div>
        <table class="list" width="99%" layoutH="89">
            <thead>
            <tr>
                <th width="30" align="center" rowspan="2">
                	<@s.checkbox class="checkboxCtrl" group="cinemaId" value="true" />
                </th>
                <th width="30" rowspan="2">序号</th>
                <th align="center" width="300" rowspan="2">影院名称</th>
                <th align="center" width="100" rowspan="2">总票数</th>
                <th align="center" width="100" rowspan="2">总金额</th>
                <th align="center" width="100" rowspan="2">退票数</th>
                <th align="center" width="100" rowspan="2">退票金额</th>
                <th align="center" width="100" rowspan="2">实际票数</th>
                <th align="center" width="100" rowspan="2">实际金额</th>
                <th align="center" width="350" colspan="${(ShowType?values)?size}">实际票数</th>
                <th align="center" width="350" colspan="${(ShowType?values)?size}">退票数</th>
                <th width="140" align="center" rowspan="2">操作</th>
            </tr>
            <tr>
            <#list ShowType?values as showType>
                <td width="60" align="center">${showType}</td>
            </#list>
            <#list ShowType?values as showType>
                <td width="60" align="center">${showType}</td>
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
            <#list cinemaChannelSaleStat.contents as daily>
            <tr>
                <td align="center">
                    <@s.checkbox name="cinemaId" value="${daily.cinema.id}" class="cinemaIdBox"/>
                </td>
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
                <td align="center">
                	<@dwz.a href="/stat/cinemaChannelSaleStat-list?cinemaId=${daily.cinema.id}&startDate=${searchModel.startDateStr?date}&endDate=${searchModel.endDateStr?date}"
                		target="dialog" width="XL" title="影院【${daily.cinema.name}】渠道销售统计">影院渠道销售统计
                	</@dwz.a>
                </td>
            </tr>
            </#list>
            </tbody>
            <tfoot>
            <tr>
                <td align="center">
                </td>
                <td width="30"></td>
                <td width="100">合计</td>
                <td width="100" align="right">${totalTicket}</td>
                <td width="100" align="right">${totalMoney?string(",##0.00")}</td>
                <td width="100" align="right">${refundTicket}</td>
                <td width="100" align="right">${refundMoney?string(",##0.00")}</td>
                <td align="right">${actualTicket}</td>
                <td width="100" align="right">${actualMoney?string(",##0.00")}</td>
            	<#list ShowType?values as showType>
                	<td class="totalActualTicket_${showType}" align="right">0</td>
            	</#list>
            	<#list ShowType?values as showType>
               		<td class="totalRefundTicket_${showType}" align="right">0</td>
          	  	</#list>
                <td>
                </td>
            </tr>
            </tfoot>
        </table>
    <div class="panelBar"><@dwz.pageNav pageModel=cinemaChannelSaleStat /></div>
</div>
<script>
    $(function () {
        var cinemaStatExportUrl = "stat/cinemaSaleStat-export?startDate=${searchModel.startDateStr?date}&endDate=${searchModel.endDateStr?date}";
    <#list ShowType?values as showType>
        var totalActualTicket_${showType} = 0;
        $(".actualTicket_${showType}").each(function () {
            totalActualTicket_${showType}= totalActualTicket_${showType}* 1 + $(this).text() * 1;
        });
        $(".totalActualTicket_${showType}").text(totalActualTicket_${showType});

        var totalRefundTicket_${showType}= 0;
        $(".refundTicket_${showType}").each(function () {
            totalRefundTicket_${showType}= totalRefundTicket_${showType}* 1 + $(this).text() * 1;
        });
        $(".totalRefundTicket_${showType}").text(totalRefundTicket_${showType});
    </#list>

        $(".exportLink").live("click", function () {
            var ids = getCheckedIds();
            if (ids == '') {
                alertMsg.error('请选择影院。', {});
                return;
            }
            var url = cinemaStatExportUrl + "&cinemaId=" + ids;
            $(this).attr("href", url);
        });
    });

    function getCheckedIds() {
        var ids = [];
        $("#cinemaSaleStat .cinemaIdBox").each(function () {
            if ($(this).attr("checked") == "checked") {
                ids.push($(this).val())
            }
        });
        return ids.join(",");
    }
</script>
