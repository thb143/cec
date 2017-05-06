<@mail.mail_send>
<p class="content">
	系统于<strong>${syncTime?datetime}</strong>同步<strong>${cinemaName}</strong>的排期出现<strong>${errorType}</strong>。
</p>
<p class="content">同步排期-${errorType}如下：</p>
<table class="grid">
	<thead>
		<tr>
			<th width="130">排期编码</th>
			<th width="100">影片编码</th>
			<th width="120">放映时间</th>
			<th width="300">异常信息</th>
		</tr>
	</thead>
	<tbody>
		<#list errorLogs as errorLog>
		<tr>
			<td align="center">${errorLog.showCode}</td>
			<td align="center">${errorLog.filmCode}</td>
			<td align="center">${errorLog.showTime}</td>
			<td align="left">${errorLog.msg}</td>
		</tr>
		</#list>
	</tbody>
</table>
<p class="content">以上排期同步失败，请及时处理，感谢您的配合。</p>
</@mail.mail_send>
