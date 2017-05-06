<div class="page">
    <div class="pageHeader">
    <@dwz.pageForm action="/stat/channelSaleStat-list"  alt="可输入渠道编码、渠道名称检索">
        <li>统计日期：<@s.input path="searchModel.startDate" class="date" dateFmt="yyyy-MM-dd" readonly="true"/></li>
        <li>至：<@s.input path="searchModel.endDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" minRelation="#startDate"/></li>
    </@dwz.pageForm>
    </div>
    <div class="pageContent" id="channelSaleStat">
        <div class="panelBar">
            <ul class="toolBar">
                <li><@dwz.a href="/stat/channelSaleStat-export?startDate=${searchModel.startDateStr?date}&endDate=${searchModel.endDateStr?date}" target="dwzExport" targettype="navTab"  class="channelExportLink" title="您是否确定要导出报表？">
                    <span class="a17">导出</span></@dwz.a>
                </li>
            </ul>
        </div>
        <table class="list" width="100%" layoutH="89">
            <thead>
            <tr>
                <th width="30" align="center" rowspan="2">
               		<@s.checkbox class="checkboxCtrl" group="channelId" value="true" />
                </th>
                <th width="30" rowspan="2">序号</th>
                <th align="center" width="30" rowspan="2">渠道编号</th>
                <th align="center" rowspan="2">渠道名称</th>
                <th align="center" width="100" rowspan="2">总票数</th>
                <th align="center" width="100" rowspan="2">总金额</th>
                <th align="center" width="60" rowspan="2">退票数</th>
                <th align="center" width="100" rowspan="2">退票金额</th>
                <th align="center" width="100" rowspan="2">实际票数</th>
                <th align="center" width="100" rowspan="2">实际金额</th>
                <th align="center" width="400" colspan="${(ShowType?values)?size}">实际票数</th>
                <th align="center" width="400" colspan="${(ShowType?values)?size}">退票数</th>
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
            <#list channelDailys.contents as daily>
            <tr>
                <td align="center">
                    <@s.checkbox name="channelId" class="channelIdBox" value="${daily.channel.id}" />
                </td>
                <td align="center">${daily_index+1}</td>
                <td>${daily.channel.code}</td>
                <td>${daily.channel.name}</td>
                <td align="right">${daily.normalOrderStat.ticketCount}<#assign totalTicket=totalTicket+daily.normalOrderStat.ticketCount></td>
                <td align="right">${daily.normalOrderStat.amount?string(",##0.00")}<#assign totalMoney=totalMoney+daily.normalOrderStat.amount></td>
                <td align="right">${daily.refundOrderStat.ticketCount}<#assign refundTicket=refundTicket+daily.refundOrderStat.ticketCount></td>
                <td align="right">${daily.refundOrderStat.revokeAmount?string(",##0.00")}<#assign refundMoney=refundMoney+daily.refundOrderStat.revokeAmount></td>
                <td align="right">${daily.normalOrderStat.ticketCount+daily.refundOrderStat.ticketCount}<#assign actualTicket=actualTicket+(daily.normalOrderStat.ticketCount+daily.refundOrderStat.ticketCount)></td>
                <td align="right">${(daily.normalOrderStat.amount+daily.refundOrderStat.revokeAmount)?string(",##0.00")}<#assign actualMoney=actualMoney+(daily.normalOrderStat.amount+daily.refundOrderStat.revokeAmount)></td>
                <#list ShowType?values as showType>
                    <td class="channelActualTicket_${showType}" align="right">${daily.getShowTypeStat(showType).saleCount+daily.getShowTypeStat(showType).refundCount}</td>
                </#list>
                <#list ShowType?values as showType>
                    <td class="channelRefundTicket_${showType}" align="right">${daily.getShowTypeStat(showType).refundCount}</td>
                </#list>
                <td align="center">
                	<@dwz.a href="/stat/channelCinemaSaleStat-list?channelId=${daily.channel.id}&startDate=${searchModel.startDateStr?date}&endDate=${searchModel.endDateStr?date}"
                		target="dialog" width="XL" title="渠道【${daily.channel.name}】影院销售统计">渠道影院销售统计
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
                <td width="100"></td>
                <td width="100">合计</td>
                <td align="right">${totalTicket}</td>
                <td align="right">${totalMoney?string(",##0.00")}</td>
                <td align="right">${refundTicket}</td>
                <td align="right">${refundMoney?string(",##0.00")}</td>
                <td align="right">${actualTicket}</td>
                <td align="right">${actualMoney?string(",##0.00")}</td>
            	<#list ShowType?values as showType>
                	<td align="right" class="channelTotalActualTicket_${showType}">0</td>
            	</#list>
            	<#list ShowType?values as showType>
               		<td align="right" class="channelTotalRefundTicket_${showType}">0</td>
          	 	</#list>
                <td>
                </td>
            </tr>
            </tfoot>
        </table>
    <div class="panelBar"><@dwz.pageNav pageModel=channelDailys /></div>
    </div>
    <script>
        var channelStatExportUrl = "stat/channelSaleStat-export?startDate=${searchModel.startDateStr?date}&endDate=${searchModel.endDateStr?date}";
        $(function () {
        <#list ShowType?values as showType>
            var totalActualTicket_${showType} = 0;
            $("#channelSaleStat .channelActualTicket_${showType}").each(function () {
                totalActualTicket_${showType}= totalActualTicket_${showType}* 1 + $(this).text() * 1;
            });
            $("#channelSaleStat .channelTotalActualTicket_${showType}").text(totalActualTicket_${showType});

            var totalRefundTicket_${showType}= 0;
            $("#channelSaleStat .channelRefundTicket_${showType}").each(function () {
                totalRefundTicket_${showType}= totalRefundTicket_${showType}* 1 + $(this).text() * 1;
            });
            $("#channelSaleStat .channelTotalRefundTicket_${showType}").text(totalRefundTicket_${showType});
        </#list>

            $("#channelSaleStat .channelExportLink").live("click", function () {
                var ids = getCheckedChannelIds();
                if (ids == '') {
                    alertMsg.error('请选择渠道。', {});
                    return;
                }
                var url = channelStatExportUrl + "&channelId=" + ids;
                $(this).attr("href", url);
            });
        });

        function getCheckedChannelIds() {
            var ids = [];
            $("#channelSaleStat .channelIdBox").each(function () {
                if ($(this).attr("checked") == "checked") {
                    ids.push($(this).val())
                }
            });
            return ids.join(",");
        }

    </script>
</div>
