<@mail.mail_send>
	<p class="content">
		系统处理异常订单失败，异常订单如下：
	</p>
	<table class="grid">
		<thead>
			<tr>
				<th width="120">订单号</th>
				<th width="120">渠道名称</th>
				<th width="180">影院名称</th>
				<th width="150">影片名称</th>
				<th width="120">影院排期编码</th>
				<th width="120">渠道排期编码</th>
			</tr>
		</thead>
		<tbody>
			<#list abnormalOrders as order>
			<tr>
				<td align="center">${order.orderCode}</td>
				<td align="center">${order.channelName}</td>
				<td align="center">${order.cinemaName}</td>
				<td align="center">${order.filmName}</td>
				<td align="center">${order.cinemaShowCode}</td>
				<td align="center">${order.channelShowCode}</td>
			</tr>
			</#list>
		</tbody>
	</table>
	<p class="content">以上异常订单系统自动处理失败，请人工干预及时处理，感谢您的配合。</p>
</@mail.mail_send>