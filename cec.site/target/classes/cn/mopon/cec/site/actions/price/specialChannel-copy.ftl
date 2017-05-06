<div class="page">
    <div class="pageContent">
        <@dwz.form action="/price/specialChannel-copy-save">
        <@s.hidden path="specialChannelCopyModel.origSpecialChannelId" />
        <div class="pageFormContent" layoutH="60">
         	<dl class="nowrap">
				<dt>复制到渠道：</dt>
				<dd>
	                <@dwz.checkboxs path="specialChannelCopyModel.channels" items=specialChannelListModel.switchChannels itemValue="id" itemLabel="name" />
				</dd>
			</dl>
		</div>
        <@dwz.formBar />
       </@dwz.form>
    </div>
</div>