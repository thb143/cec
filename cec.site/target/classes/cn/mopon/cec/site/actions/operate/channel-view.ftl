<div class="page">
	<div class="pageContent">
		<div class="pageFormContent" layoutH="30">
			<dl>
				<dt>渠道编号：</dt>
				<dd>${channel.code}</dd>
			</dl>
			<dl>
				<dt>渠道名称：</dt>
				<dd>${channel.name}</dd>
			</dl>
			<dl>
				<dt>渠道类型：</dt>
				<dd>${channel.type}</dd>
			</dl>
			<dl>
				<dt>通讯密钥：</dt>
				<dd>${channel.secKey}</dd>
			</dl>
			<dl>
				<dt>提前停售时间：</dt>
				<dd>${channel.settings.stopSellTime} 分钟</dd>
			</dl>
			<dl>
				<dt>提前停退时间：</dt>
				<dd>${channel.settings.stopRevokeTime} 分钟</dd>
			</dl>
			<dl class="nowrap">
				<dt>接口签名设置：</dt>
				<dd>${channel.settings.verifySign}</dd>
			</dl>
			<dl>
				<dt>开放状态：</dt>
				<dd class="${channel.opened?string('${StatusColor.GREEN}','${StatusColor.GRAY}')} center">${channel.opened?string("开放","关闭")}</dd>
			</dl>
			<dl>
				<dt>销售状态：</dt>
				<dd class="${channel.salable?string('${StatusColor.GREEN}','${StatusColor.GRAY}')} center">${channel.salable?string("开放","关闭")}</dd>
			</dl>
			<dl class="nowrap">
				<dt>开放票务接口：</dt>
				<dd>${channel.settings.ticketApiMethodsTexts}</dd>
			</dl>
			<dl class="nowrap">
				<dt>备注：</dt>
				<dd>${channel.remark}</dd>
			</dl>
		</div>
	</div>
</div>