<@mail.mail_send>
<p class="content">
	系统于<strong>${syncTime?datetime}</strong>同步CSP影片，发现有新增加的影片。
</p>
<p class="content">新增影片如下：</p>
<table class="grid">
	<thead>
		<tr>
			<th width="100">影片编码</th>
			<th width="300">影片名称</th>
			<th width="150">公映日期</th>
		</tr>
	</thead>
	<tbody>
		<#list films as film>
		<tr>
			<td align="center">${film.code}</td>
			<td align="center">${film.name}</td>
			<td align="center">${film.publishDate?date}</td>
		</tr>
		</#list>
	</tbody>
</table>
<p class="content">以上影片为新增影片，请及时进行影片最低价设置，感谢您的配合。</p>
</@mail.mail_send>
