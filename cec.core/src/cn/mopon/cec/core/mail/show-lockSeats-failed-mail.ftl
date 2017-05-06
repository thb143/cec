<@mail.mail_send>
	<p class="content">
		系统于<strong>${.now?datetime}</strong>锁座失败，失败排期如下：
	</p>
	<table class="grid">
		<thead>
			<tr>
				<th width="120">渠道名称</th>
				<th width="180">影院名称</th>
				<th width="150">影片名称</th>
				<th width="120">影院排期编码</th>
				<th width="120">渠道排期编码</th>
				<th width="150">放映时间</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td align="center">${channelName}</td>
				<td align="center">${cinemaName}</td>
				<td align="center">${filmName}</td>
				<td align="center">${cinemaShowCode}</td>
				<td align="center">${channelShowCode}</td>
				<td align="center">${showTime}</td>
			</tr>
			<tr>
				<td align="left" colspan="6">
				异常信息：
				<pre>${cause}</pre>
				</td>
			</tr>
		</tbody>
	</table>
	<p class="content">以上排期锁座失败，请及时处理，感谢您的配合。</p>
</@mail.mail_send>