<div class="page">
    <div class="pageHeader">
        <@dwz.pageForm action="/order/snackSuccessOrder-list.ftl" alt="可输入平台订单号,凭证号,手机号码,影院名称进行检索">
        	<@ord.snackOrderSearch searchModel=searchModel />
        </@dwz.pageForm>
    </div>
    <div class="pageContent">
        <table class="table" width="100%" layoutH="85">
            <thead>
                <tr>
                    <th width="30" align="center">序号</th>
                    <th width="130" align="center">平台订单号</th>
                    <th width="130" align="center">凭证号</th>
                    <th width="130" align="center">手机号码</th>
                    <th width="130">影院名称</th>
                    <th width="130">渠道名称</th>
                    <th width="200" align="center">确认时间</th>
                    <th width="80" align="center">销售价</th>
                    <th width="80" align="center">门市价</th>
                    <th width="80" align="center">影院结算价</th>
                    <th width="80" align="center">渠道结算价</th>
                    <th width="80" align="center">渠道费</th>
                    <th width="80" align="center"><@s.message code="discountAmount" /></th>
                </tr>
            </thead>
            <tbody>
            	<#list orderPages.contents as order>
	                <tr>
	                    <td>${order_index+1}</td>
	                    <td>
	                    	<@dwz.a href="/order/snackOrder-view?snackOrder=${order.id}" target="dialog" title="查看订单" width="L" height="L">${order.code}</@dwz.a>
	                    </td>
	                    <td>
	                    	<@dwz.a href="/order/snackOrder-view?snackOrder=${order.id}&viewType=voucher" target="dialog" title="查看凭证" width="L" height="L">${order.voucher.code}</@dwz.a>
	                    </td>
	                    <td>${order.mobile}</td>
	                    <td>${order.cinema.name}</td>
	                    <td>${order.channel.name}</td>
	                    <td>${order.createTime?datetime}</td>
	                    <td>${order.amount?string(",##0.00")}</td>
	                    <td>${order.stdAmount?string(",##0.00")}</td>
	                    <td>${order.cinemaAmount?string(",##0.00")}</td>
	                    <td>${order.channelAmount?string(",##0.00")}</td>
	                    <td>${order.channelFee?string(",##0.00")}</td>
	                    <td>${order.discountAmount?string(",##0.00")}</td>
	                </tr>
                </#list>
            </tbody>
        </table>
        <div class="panelBar">
           	<@dwz.pageNav pageModel=orderPages />
        </div>
    </div>
</div>