<div class="page">
    <div class="pageHeader">
        <@dwz.pageForm action="/order/benefitCardConsumeSnackOrder-list.ftl" alt="可输入卡号、平台订单号、渠道订单号、手机号码检索">
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
	                    <td>${model.snackOrder.channel.code}</td>
	                    <td>${model.snackOrder.channel.name}</td>
	                    <#if model.snackOrder.status == TicketOrderStatus.PAID>
							<#assign statusColor = StatusColor.ORANGE>
						<#elseif model.snackOrder.status == TicketOrderStatus.SUCCESS>
							<#assign statusColor = StatusColor.GREEN>
						<#elseif model.snackOrder.status == TicketOrderStatus.FAILED>
							<#assign statusColor = StatusColor.RED>
						<#elseif model.snackOrder.status == TicketOrderStatus.REVOKED>
							<#assign statusColor = StatusColor.GRAY>
						</#if>
	                    <td class="${statusColor}">${model.snackOrder.status}</td>
	                    <td>${model.snackOrder.amount?string(",##0.00")}</td>
	                    <td>${model.discountAmount?string(",##0.00")}</td>
	                    <td>
	                    	<@dwz.a href="/order/snackOrder-view?snackOrder=${model.snackOrder.id}" target="dialog" title="查看卖品订单信息" width="L" height="L">${model.snackOrder.code}</@dwz.a>
	                    </td>
	                    <td>${model.snackOrder.createTime?datetime}</td>
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