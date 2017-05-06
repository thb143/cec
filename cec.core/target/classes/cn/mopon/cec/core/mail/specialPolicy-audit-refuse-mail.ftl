<@mail.mail_send>
<p class="content">您提交的特殊定价策略已被审核退回，具体如下：</p>
		<table class="list">
			<tr>
				<th>策略名称：</th>
				<td><strong>${policyName}</strong></td>
			</tr>
			<tr>
				<th>退回意见：</th>
				<td>${refuseNote}</td>
			</tr>
			<tr>
				<th>退回时间：</th>
				<td>${auditTime?datetime}</td>
			</tr>
		</table>
<p class="content">请及时处理，感谢您的配合。</p>
</@mail.mail_send>

