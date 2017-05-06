<div class="page">
    <div class="pageHeader">
        <@dwz.pageForm action="/order/benefitCardConsumeOrder-list.ftl" alt="可输入卡号、平台订单号、渠道订单号、手机号码检索">
        	<@ord.benefitCardOrderSearch searchModel=searchModel />
        </@dwz.pageForm>
    </div>
    <div class="pageContent">
        <table class="table" width="100%" layoutH="85">
            <thead>
                <tr>
                    <th width="30" align="center">序号</th>
                    <th width="130" align="center">卡号</th>
                    <th width="130" align="center">手机号码</th>
                    <th>卡类名称</th>
                    <th width="130">消费渠道编码</th>
                    <th width="130">消费渠道名称</th>
                    <th width="80" align="center">订单状态</th>
                    <th width="80" align="right">消费金额</th>
                    <th width="80" align="right">优惠金额</th>
                    <th width="130" align="center">消费订单号</th>
                    <th width="180" align="center">消费时间</th>
                </tr>
            </thead>
            <tbody>
            	<#list orderPages.contents as model>
	                <tr>
	                    <td>${model_index+1}</td>
	                    <td>
	                    	<@dwz.a href="/product/benefitCard-view?benefitCard=${model.card.id}" target="dialog" title="查看权益卡信息" width="M" height="S">${model.card.code}</@dwz.a>
	                    </td>
	                    <td>${model.card.user.mobile}</td>
	                    <td>${model.card.type.name}</td>
	                    <td>${model.order.channel.code}</td>
	                    <td>${model.order.channel.name}</td>
	                    <#if model.order.status == TicketOrderStatus.PAID>
							<#assign statusColor = StatusColor.ORANGE>
						<#elseif model.order.status == TicketOrderStatus.SUCCESS>
							<#assign statusColor = StatusColor.GREEN>
						<#elseif model.order.status == TicketOrderStatus.FAILED>
							<#assign statusColor = StatusColor.RED>
						<#elseif model.order.status == TicketOrderStatus.REVOKED>
							<#assign statusColor = StatusColor.GRAY>
						</#if>
	                    <td class="${statusColor}">${model.order.status}</td>
	                    <td>${model.order.amount?string(",##0.00")}</td>
	                    <td>${model.discountAmount?string(",##0.00")}</td>
	                    <td>
	                    	<@dwz.a href="/order/ticketOrder-view?ticketOrder=${model.order.id}" target="dialog" title="查看票务订单信息" width="L" height="L">${model.order.code}</@dwz.a>
	                    </td>
	                    <td>${model.order.payTime?datetime}</td>
	                </tr>
                </#list>
            </tbody>
        </table>
        <div class="panelBar">
           	<@dwz.pageNav pageModel=orderPages />
        </div>
    </div>
</div>
<script type="text/javascript">
$(function(){
	$(".page").find("select[name='consumeStatus']").find("option[value='1']").remove();
	$(".page").find("select[name='consumeStatus']").find("option[value='2']").remove();
});
</script>