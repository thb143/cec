<div class="page">
    <div class="pageContent">
        <table class="table" width="100%" layoutH="60">
            <thead>
                <tr>
                    <th width="30" align="center">序号</th>
                    <th width="130" align="center">订单号</th>
                    <th>渠道名称</th>
                    <th width="130" align="center">影院订单号</th>
                    <th width="80" align="center">接入商</th>
                    <th width="130" align="center">下单时间</th>
                    <th width="130" align="center">确认时间</th>
                    <th width="60" align="center">选座数量</th>
                    <th width="60" align="right"><@s.message code="amount"/></th>
                </tr>
            </thead>
            <tbody>
                <#list orders as order>
	                <tr>
	                    <td>${order_index+1}</td>
	                    <td>${order.code} </td>
	                    <td>${order.channel.name}</td>
	                    <td>${order.cinemaOrderCode}</td>
	                    <td>${order.provider}</td>
	                    <td>${order.createTime?datetime}</td>
	                    <td>${order.confirmTime?datetime}</td>
	                    <td>${order.ticketCount}</td>
	                    <td>${order.amount?string(",##0.00")}</td>
	                </tr>
                </#list>
            </tbody>
        </table>
    </div>
</div>