<div class="page">
    <div class="pageHeader">
        <@dwz.pageForm action="/order/benefitCardRechargeOrder-list.ftl" alt="可输入卡号、渠道续费订单号、手机号码检索">
        	<@ord.benefitCardOrderSearch searchModel=searchModel />
        </@dwz.pageForm>
    </div>
    <div class="pageContent">
        <table class="table" width="100%" layoutH="85">
            <thead>
                <tr>
                    <th width="30" align="center">序号</th>
                    <th width="90" align="center">卡号</th>
                    <th width="90" align="center">手机号码</th>
                    <th>卡类名称</th>
                    <th width="100">渠道名称</th>
                    <th width="130">渠道订单号</th>
                    <th width="60" align="center">订单状态</th>
                    <th width="60" align="right">续费金额</th>
                    <th width="80" align="center">续费后总次数</th>
                    <th width="100" align="center">续费前可用次数</th>
                    <th width="100" align="center">续费后可用次数</th>
                    <th width="60" align="center">过期次数</th>
                    <th width="100" align="center">续费前结束日期</th>
                    <th width="100" align="center">有效结束日期</th>
                    <th width="130" align="center">续费时间</th>
                </tr>
            </thead>
            <tbody>
            	<#list orderPages.contents as order>
	                <tr>
	                    <td>${order_index+1}</td>
	                    <td>
	                    	<@dwz.a href="/product/benefitCard-view?benefitCard=${order.card.id}" target="dialog" title="查看权益卡信息" width="M" height="S">${order.card.code}</@dwz.a>
	                    </td>
	                    <td>${order.card.user.mobile}</td>
	                    <td>${order.card.type.name}</td>
	                    <td>${order.channel.name}</td>
	                    <td>${order.channelOrderCode}</td>
	                    <td class="${order.status.color}">${order.status}</td>
	                    <td>${order.amount?string(",##0.00")}</td>
	                    <td>${order.totalDiscountCount}</td>
	                    <td>${order.oldDiscountCount}</td>
	                    <td>${order.discountCount}</td>
	                    <td>${order.expireCount}</td>
	                    <td>${order.oldEndDate?date}</td>
	                    <td>${(order.endDate?string("yyyy-MM-dd"))!}</td>
	                    <td>${order.createDate?datetime}</td>
	                </tr>
                </#list>
            </tbody>
        </table>
        <div class="panelBar">
           	<@dwz.pageNav pageModel=orderPages />
        </div>
    </div>
</div>