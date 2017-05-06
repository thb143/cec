<div class="page">
    <div class="pageContent">
        <table class="table" width="100%" layoutH="30">
            <thead>
                <tr>
                    <th width="30" align="center">序号</th>
                    <th width="100" align="center">卡号</th>
                    <th align="center">卡类名称</th>
                    <th width="70" align="center">开卡渠道</th>
                    <th width="80" align="center">有效开始日期</th>
                    <th width="80" align="center">有效结束日期</th>
                    <th width="80" align="center">开卡金额</th>
                    <th width="80" align="center">优惠总次数</th>
                    <th width="80" align="center">当天消费次数</th>
                    <th width="50" align="center">状态</th>
                    <th width="140" align="center">开卡时间</th>
                    <th width="100" align="center">首次消费影院</th>
                    <th width="80" align="center">操作</th>
                </tr>
            </thead>
            <tbody>
                <#list cards as card>
                	<tr>
                		<td>${card_index+1}</td>
                		<td>${card.code}</td>
                		<td title="${card.type.name}">${card.type.name}</td>
                		<td>${card.channel.name}</td>
                		<td>${card.startDate?date}</td>
                		<td>${card.endDate?date}</td>
                		<td>${card.initAmount?string(",##0.00")}</td>
                		<td>${card.totalDiscountCount}</td>
                		<td>${card.dailyDiscountCount}</td>
                		<td class="${card.status.color}">${card.status}</td>
                		<td>${card.createDate?datetime}</td>
                		<td>${card.firstCinema.name}</td>
                		<td>
                			<#if card.status == BenefitCardStatus.NORMAL>
                				<@dwz.a href="/product/benefitCard-disable?benefitCard=${card.id}" target="ajaxTodo" callback="dialogReloadDone" title="您是否确定要冻结该权益卡？">冻结</@dwz.a>
                			</#if>
                			<#if card.status == BenefitCardStatus.DISABLE>
                				<@dwz.a href="/product/benefitCard-enable?benefitCard=${card.id}" target="ajaxTodo" callback="dialogReloadDone" title="您是否确定要解冻该权益卡？">解冻</@dwz.a>
                			</#if>
                			<@dwz.a href="/product/benefitCard-delay?benefitCard=${card.id}"  target="ajaxTodo" callback="dialogReloadDone"  title="您是否确定要延期该权益卡？">延期</@dwz.a>
                		</td>
                	</tr>
                </#list>
            </tbody>
        </table>
    </div>
</div>