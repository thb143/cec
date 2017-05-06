<div class="page">
    <div class="pageHeader">
        <@dwz.pageForm action="/product/benefitCard-list" alt="可输入权益卡号、用户手机号码检索">
        	<li>状态：<@s.select path="searchModel.status" headerLabel="全部" items=BenefitCardStatus?values itemValue="value" itemLabel="text" /></li>
        	<li>卡类：<@s.select path="searchModel.typeId" headerLabel="全部" items=types itemValue="id" itemLabel="name" /></li>
        	<li>开卡渠道：<@s.select path="searchModel.channelId" headerLabel="全部" items=channels itemValue="id" itemLabel="name" /></li>
        	<li>开卡时间：<@s.input path="searchModel.startDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" /></li>
			<li>至<@s.input path="searchModel.endDate" class="date" dateFmt="yyyy-MM-dd" readonly="true" minRelation="#startDate" /></li>
        </@dwz.pageForm>
    </div>
    <div class="pageContent">
        <table class="table" width="100%" layoutH="86">
            <thead>
                <tr>
                    <th width="30" align="center">序号</th>
                    <th width="120" align="center">卡号</th>
                    <th width="120" align="center">手机号</th>
                    <th align="center">卡类名称</th>
                    <th width="90" align="center">开卡渠道</th>
                    <th width="90" align="center">有效开始日期</th>
                    <th width="90" align="center">有效结束日期</th>
                    <th width="80" align="center">开卡金额</th>
                    <th width="80" align="center">优惠总次数</th>
                    <th width="60" align="center">续费次数</th>
                    <th width="60" align="center">剩余次数</th>
                    <th width="80" align="center">优惠金额</th>
                    <th width="80" align="center">当天消费次数</th>
                    <th width="50" align="center">状态</th>
                    <th width="140" align="center">开卡时间</th>
                    <th width="80" align="center">操作</th>
                </tr>
            </thead>
            <tbody>
                <#list benefitCardPage.contents as card>
                	<tr>
                		<td>${card_index+1}</td>
                		<td><@dwz.a href="/product/benefitCard-view?benefitCard=${card.id}" target="dialog" title="查看权益卡信息" width="M" height="S">${card.code}</@dwz.a></td>
                		<td>${card.user.mobile}</td>
                		<td title="${card.type.name}">${card.type.name}</td>
                		<td>${card.channel.name}</td>
                		<td>${card.startDate?date}</td>
                		<td>${card.endDate?date}</td>
                		<td>${card.initAmount?string(",##0.00")}</td>
                		<td>${card.totalDiscountCount}</td>
                		<td>${card.rechargeCount}</td>
                		<td>${card.availableDiscountCount}</td>
                		<td>${card.discountAmount?string(",##0.00")}</td>
                		<td>${card.dailyDiscountCount}</td>
                		<td class="${card.status.color}">${card.status}</td>
                		<td>${card.createDate?datetime}</td>
                		<td>
                			<#if card.status == BenefitCardStatus.NORMAL>
                				<@dwz.a href="/product/benefitCard-disable?benefitCard=${card.id}" target="ajaxTodo" title="您是否确定要冻结该权益卡？">冻结</@dwz.a>
                			</#if>
                			<#if card.status == BenefitCardStatus.DISABLE>
                				<@dwz.a href="/product/benefitCard-enable?benefitCard=${card.id}" target="ajaxTodo" title="您是否确定要解冻该权益卡？">解冻</@dwz.a>
                			</#if>
                			<@dwz.a href="/product/benefitCard-delay?benefitCard=${card.id}"  target="ajaxTodo" title="您是否确定要延期该权益卡？">延期</@dwz.a>
                		</td>
                	</tr>
                </#list>
            </tbody>
        </table>
        <div class="panelBar">
            <@dwz.pageNav pageModel=benefitCardPage />
        </div>
    </div>
</div>