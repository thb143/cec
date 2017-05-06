<div class="page">
    <div class="pageHeader">
        <@dwz.pageForm action="/order/benefitCardOpenOrder-list.ftl" alt="可输入卡号、渠道开卡订单号、手机号码检索">
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
                    <th width="130">开卡渠道编码</th>
                    <th width="130">开卡渠道名称</th>
                    <th width="80" align="right">开卡金额</th>
                    <th width="200">渠道开卡订单号</th>
                    <th width="200" align="center">开卡时间</th>
                </tr>
            </thead>
            <tbody>
            	<#list orderPages.contents as order>
	                <tr>
	                    <td>${order_index+1}</td>
	                    <td>
	                    	<@dwz.a href="/product/benefitCard-view?benefitCard=${order.id}" target="dialog" title="查看权益卡信息" width="M" height="S">${order.code}</@dwz.a>
	                    </td>
	                    <td>${order.user.mobile}</td>
	                    <td>${order.type.name}</td>
	                    <td>${order.channel.code}</td>
	                    <td>${order.channel.name}</td>
	                    <td>${order.initAmount?string(",##0.00")}</td>
	                    <td>${order.channelOrderCode}</td>
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