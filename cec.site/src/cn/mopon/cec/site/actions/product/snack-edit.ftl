<div class="page">
	<div class="pageContent">
		<@dwz.form action="/product/snack-update">
			<@s.hidden path="snack.id" />
			<@s.hidden path="snack.code" />
			<@s.hidden path="snack.cinema" />
			<@s.hidden path="snack.type" />
			<div class="pageFormContent" layoutH="60">
				<dl>
					<dt>卖品编码：</dt>
		            <dd>${snack.code}</dd>
				</dl>
				<dl>
					<dt>影院名称：</dt>
		            <dd>${snack.cinema.name}</dd>
				</dl>
				<dl class="nowrap">
					<dt>卖品名称：</dt>
		            <dd style="width:200px;">${snack.type.name}</dd>
				</dl>
				<dl class="nowrap">
					<dt>卖品内容：</dt>
		            <dd style="width:200px;">${snack.type.remark}</dd>
				</dl>
				<dl>
					<dt>门市价：</dt>
		            <dd><@s.input path="snack.stdPrice" range="0,1000" class="required number" customvalid="regexPrice(element)" title="请输入一个0到1000之间的两位小数。"/><span class="dd-span">元</span></dd>
		        </dl>
	            <dl>
					<dt>结算价：</dt>
		            <dd><@s.input path="snack.submitPrice" range="0,1000" class="required number" customvalid="regexPrice(element)" title="请输入一个0到1000之间的两位小数。"/><span class="dd-span">元</span></dd>
		        </dl>
			</div>
			<@dwz.formBar />
		</@dwz.form>
    </div>
</div>