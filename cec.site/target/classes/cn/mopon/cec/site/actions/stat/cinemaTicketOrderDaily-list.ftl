<div class="page">
    <div class="pageHeader">
    <@dwz.pageForm action="/stat/cinemaTicketOrderDaily-list" alt="可输入影院编码、影院名称检索">
        <li>统计日期：<@s.input path="searchModel.startDate" class="date" dateFmt="yyyy-MM-dd" readonly="true"/></li>
        <li>至：<@s.input path="searchModel.endDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" minRelation="#startDate"/></li>
    </@dwz.pageForm>
    </div>
    <div class="pageContent" id="cinemaTicketOrderDaily">
        <div class="panelBar">
            <ul class="toolBar">
                <li><@dwz.a href="/stat/cinemaStat-export?startDate=${searchModel.startDateStr?date}&endDate=${searchModel.endDateStr?date}"  target="dwzExport" targettype="navTab" title="您是否确定要导出报表？" class="settExportLink">
                    <span class="a17">导出</span></@dwz.a>
                </li>
            </ul>
        </div>
        <table class="list" width="99%" layoutH="89">
            <thead>
            <tr>
                <th width="30" align="center" rowspan="2">
                	<@s.checkbox class="checkboxCtrl" group="cinemaId" value="true" />
                </th>
                <th width="30" align="center" rowspan="2">序号</th>
                <th width="60" align="center" rowspan="2">影院编码</th>
                <th width="200" align="center" rowspan="2">影院名称</th>
                <th width="80" align="center" rowspan="2">应结数量</th>
                <th width="80" align="center" rowspan="2">应结金额</th>
            	<#list ShowType?values as showType>
               	 <th align="center" colspan="2">${showType}</th>
            	</#list>
                <th width="80" align="center" rowspan="2">操作</th>
            </tr>
            <tr>
            <#list ShowType?values as showType>
                <td width="60" align="center">应结数量</td>
                <td width="60" align="right">应结金额</td>
            </#list>
            </tr>
            </thead>
            <tbody>
            <#list cinemaTicketOrderDailyPage.contents as dailyStat>
	            <tr>
	                <td align="center">
	                    <@s.checkbox name="cinemaId" value="${dailyStat.cinema.id}" class="settCinemaIdBox" />
	                </td>
	                <td align="center">${dailyStat_index+1}</td>
	                <td>${dailyStat.cinema.code}</td>
	                <td align="left">${dailyStat.cinema.name}</td>
	                <td class="settleTicketCount" align="right">${dailyStat.normalOrderStat.ticketCount+dailyStat.refundOrderStat.ticketCount}</td>
	                <td class="settleAmount" align="right">${(dailyStat.normalOrderStat.cinemaAmount+dailyStat.refundOrderStat.cinemaAmount)?string(",##0.00")}</td>
	                <#list ShowType?values as showType>
	                  	<td class="settleTicketCount_${showType}" align="right">${dailyStat.getShowTypeStat(showType).saleCount+dailyStat.getShowTypeStat(showType).refundCount}</td>
	                    <td class="settleAmount_${showType}" align="right">${(dailyStat.getShowTypeStat(showType).cinemaAmount+dailyStat.getShowTypeStat(showType).refundSettlePrice)?string(",##0.00")}</td>
	                </#list>
	                <td align="center">
	                	<@dwz.a href="/stat/ticketOrderStat-view?cinemaId=${dailyStat.cinema.id}&startDate=${searchModel.startDateStr?date}&endDate=${searchModel.endDateStr?date}" target="dialog" width="XL">订单明细</@dwz.a>
	                </td>
	            </tr>
            </#list>
            </tbody>
            <tfoot>
            <tr>
               	<td></td>
                <td></td>
                <td></td>
                <td>合计</td>
                <td class="totalSettleTicketCount" align="right"></td>
                <td class="totalSettleAmount" align="right"></td>
            	<#list ShowType?values as showType>
	                <td class="totalSettleTicketCount_${showType}" align="right"></td>
	                <td class="totalSettleAmount_${showType}" align="right"></td>
            	</#list>
                <td>
                </td>
            </tr>
            </tfoot>
        </table>
        <div class="panelBar"><@dwz.pageNav pageModel=cinemaTicketOrderDailyPage /></div>
    </div>
    <script>
        var exportUrl = "stat/cinemaStat-export?startDate=${searchModel.startDateStr?date}&endDate=${searchModel.endDateStr?date}";
        $(function () {
            var totalSettleTicketCount = 0;
            $("#cinemaTicketOrderDaily .settleTicketCount").each(function () {
                totalSettleTicketCount = totalSettleTicketCount * 1 + $(this).text() * 1;
            });
            $("#cinemaTicketOrderDaily .totalSettleTicketCount").text(totalSettleTicketCount);

            var totalSettleAmount = 0;
            $("#cinemaTicketOrderDaily .settleAmount").each(function () {
                totalSettleAmount = totalSettleAmount * 1 + $(this).text().replaceAll(",", "") * 1;
            });
            $("#cinemaTicketOrderDaily .totalSettleAmount").text(totalSettleAmount.toFixed(2));

        <#list ShowType?values as showType>
            var settleTicketCount_${showType} = 0;
            $("#cinemaTicketOrderDaily .settleTicketCount_${showType}").each(function () {
                settleTicketCount_${showType}= settleTicketCount_${showType}* 1 + $(this).text() * 1;
            });
            $("#cinemaTicketOrderDaily .totalSettleTicketCount_${showType}").text(settleTicketCount_${showType});

            var totalSettleAmount_${showType} = 0;
            $("#cinemaTicketOrderDaily .settleAmount_${showType}").each(function () {
                totalSettleAmount_${showType} = totalSettleAmount_${showType}* 1 + $(this).text().replaceAll(",", "") * 1;
            });
            $("#cinemaTicketOrderDaily .totalSettleAmount_${showType}").text(totalSettleAmount_${showType}.toFixed(2))
        </#list>

            $("#cinemaTicketOrderDaily .settExportLink").live("click", function () {
                var ids = getSettCheckedIds();
                if (ids == '') {
                    alertMsg.error('请选择影院。', {});
                    return;
                }
                var url = exportUrl + "&cinemaId=" + ids;
                $(this).attr("href", url);
            });
        });

        function getSettCheckedIds() {
            var ids = [];
            $("#cinemaTicketOrderDaily .settCinemaIdBox").each(function () {
                if ($(this).attr("checked") == "checked") {
                    ids.push($(this).val())
                }
            });
            return ids.join(",");
        }
    </script>
</div>
