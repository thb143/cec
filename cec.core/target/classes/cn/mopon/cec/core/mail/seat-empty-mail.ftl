<@mail.mail_send>
<p class="content">
	系统于<strong>${syncTime?datetime}</strong>同步<strong>${cinemaName}</strong>的影厅出现异常。
</p>
<p class="content">同步影厅异常如下：</p>
<table class="grid">
	<thead>
		<tr>
			<th width="130">影厅编码</th>
			<th width="300">影厅名称</th>
			<th width="100">座位数量</th>
		</tr>
	</thead>
	<tbody>
		<#list errorHalls as errorHall>
		<tr>
			<td align="center">${errorHall.hallCode}</td>
			<td align="center">${errorHall.hallName}</td>
			<td align="center">${errorHall.seatCount}</td>
		</tr>
		</#list>
	</tbody>
</table>
<p class="content">以上影厅同步异常，请及时处理，感谢您的配合。</p>
</@mail.mail_send>
