<@mail.mail_send>
<p class="content">
	系统于<strong>${.now?datetime}</strong>发现渠道结算价低于影院排期最低价的渠道排期，排期如下：
</p>
<table class="grid">
	<thead>
		<tr>
			<th width="120">渠道名称</th>
			<th width="180">影院名称</th>
			<th width="150">影片名称</th>
			<th width="120">影院排期编码</th>
			<th width="150">放映时间</th>
			<th width="45">最低价</th>
			<th width="45">标准价</th>
			<th width="70">影院结算价</th>
			<th width="70">渠道结算价</th>
			<th width="45">票房价</th>
			<th width="45">接入费</th>
			<th width="45">手续费</th>
			<th width="45">补贴费</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td align="center">${channelName}</td>
			<td align="center">${cinemaName}</td>
			<td align="center">${filmName}</td>
			<td align="center">${showCode}</td>
			<td align="center">${showTime?string('yyyy-MM-dd HH:mm')}</td>
			<td align="center">${minPrice?string(",##0.00")}</td>
			<td align="center">${stdPrice?string(",##0.00")}</td>
			<td align="center">${cinemaPrice?string(",##0.00")}</td>
			<td align="center">${channelPrice?string(",##0.00")}</td>
			<td align="center">${submitPrice?string(",##0.00")}</td>
			<td align="center">${connectFee?string(",##0.00")}</td>
			<td align="center">${circuitFee?string(",##0.00")}</td>
			<td align="center" style="color:red">${subsidyFee?string(",##0.00")}</td>
		</tr>
	</tbody>
</table>
<p class="content">以上渠道排期的渠道结算价低于影院排期的最低价，生成渠道排期失败，请及时处理，感谢您的配合。</p>
</@mail.mail_send>