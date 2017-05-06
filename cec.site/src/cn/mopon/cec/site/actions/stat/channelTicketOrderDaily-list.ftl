<div class="page">
    <div class="pageHeader">
    <@dwz.pageForm action="/stat/channelTicketOrderDaily-list" alt="可输入渠道编码、渠道名称检索">
        <li>统计日期：<@s.input path="searchModel.startDate" class="date" dateFmt="yyyy-MM-dd" readonly="true"/></li>
        <li>至：<@s.input path="searchModel.endDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" minRelation="#startDate"/></li>
    </@dwz.pageForm>
    </div>
    <div class="pageContent" id="channelTicketOrderDaily">
        <div class="panelBar">
            <ul class="toolBar">
                <li><@dwz.a href="/stat/channelStat-export?startDate=${searchModel.startDateStr?date}&endDate=${searchModel.endDateStr?date}"  target="dwzExport" targettype="navTab" title="您是否确定要导出报表？" class="settExportLink">
                    <span class="a17">导出</span></@dwz.a>
                </li>
            </ul>
        </div>
        <table class="list" width="99%" layoutH="89">
            <thead>
            <tr>
                <th width="30" align="center" rowspan="2">
                	<@s.checkbox class="checkboxCtrl" group="channelId" value="true" />
                </th>
                <th width="30" align="center" rowspan="2">序号</th>
                <th width="60" align="center" rowspan="2">渠道编码</th>
                <th width="200" align="center" rowspan="2">渠道名称</th>
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
            <#list channelTicketNormalOrderPage.contents as dailyStat>
	            <tr>
	                <td align="center">
	                    <@s.checkbox name="channelId" value="${dailyStat.channel.id}" class="settChannelIdBox" />
	                </td>
	                <td align="center">${dailyStat_index+1}</td>
	                <td>${dailyStat.channel.code}</td>
	                <td align="left">${dailyStat.channel.name}</td>
	                <td class="settleTicketCount" align="right">${dailyStat.normalOrderStat.ticketCount+dailyStat.refundOrderStat.ticketCount}</td>
	                <td class="settleAmount" align="right">${(dailyStat.normalOrderStat.channelAmount+dailyStat.refundOrderStat.channelAmount)?string(",##0.00")}</td>
	                <#list ShowType?values as showType>
	                    <td class="settleTicketCount_${showType}" align="right">${dailyStat.getShowTypeStat(showType).saleCount+dailyStat.getShowTypeStat(showType).refundCount}</td>
	                    <td class="settleAmount_${showType}" align="right">${(dailyStat.getShowTypeStat(showType).channelAmount+dailyStat.getShowTypeStat(showType).refundSettlePrice)?string(",##0.00")}</td>
	                </#list>
	                <td align="center">
	                	<@dwz.a href="/stat/channelTicketOrderStat-view?channelId=${dailyStat.channel.id}&startDate=${searchModel.startDateStr?date}&endDate=${searchModel.endDateStr?date}" target="dialog" width="XL">订单明细</@dwz.a>
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
    </div>
    <div class="panelBar"><@dwz.pageNav pageModel=channelTicketNormalOrderPage /></div>
    <script>
        var exportUrl = "stat/channelStat-export?startDate=${searchModel.startDateStr?date}&endDate=${searchModel.endDateStr?date}";
        $(function () {
            var totalSettleTicketCount = 0;
            $("#channelTicketOrderDaily .settleTicketCount").each(function () {
                totalSettleTicketCount = totalSettleTicketCount * 1 + $(this).text() * 1;
            });
            $("#channelTicketOrderDaily .totalSettleTicketCount").text(totalSettleTicketCount);

            var totalSettleAmount = 0;
            $("#channelTicketOrderDaily .settleAmount").each(function () {
                totalSettleAmount = totalSettleAmount * 1 + $(this).text().replaceAll(",", "") * 1;
            });
            $("#channelTicketOrderDaily .totalSettleAmount").text(totalSettleAmount.toFixed(2));

        <#list ShowType?values as showType>
            var settleTicketCount_${showType} = 0;
            $("#channelTicketOrderDaily .settleTicketCount_${showType}").each(function () {
                settleTicketCount_${showType}= settleTicketCount_${showType}* 1 + $(this).text() * 1;
            });
            $("#channelTicketOrderDaily .totalSettleTicketCount_${showType}").text(settleTicketCount_${showType});

            var totalSettleAmount_${showType} = 0;
            $("#channelTicketOrderDaily .settleAmount_${showType}").each(function () {
                totalSettleAmount_${showType} = totalSettleAmount_${showType}* 1 + $(this).text().replaceAll(",", "") * 1;
            });
            $("#channelTicketOrderDaily .totalSettleAmount_${showType}").text(totalSettleAmount_${showType}.toFixed(2))
        </#list>

            $("#channelTicketOrderDaily .settExportLink").live("click", function () {
                var ids = getCheckedIds();
                if (ids == '') {
                    alertMsg.error('请选择渠道。', {});
                    return;
                }
                var url = exportUrl + "&channelId=" + ids;
                $(this).attr("href", url);
            });
        });

        function getCheckedIds() {
            var ids = [];
            $("#channelTicketOrderDaily .settChannelIdBox").each(function () {
                if ($(this).attr("checked") == "checked") {
                    ids.push($(this).val())
                }
            });
            return ids.join(",");
        }
    </script>
</div>
