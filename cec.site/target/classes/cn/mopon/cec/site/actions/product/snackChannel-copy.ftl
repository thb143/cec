<div class="page">
    <div class="pageContent">
        <@dwz.form action="/product/snackChannel-copy-save">
        <@s.hidden path="snackChannelCopyModel.origSnackChannelId" />
        <div class="pageFormContent" layoutH="60">
         	<dl class="nowrap">
				<dt>复制到渠道：</dt>
				<dd>
	                <@dwz.checkboxs path="snackChannelCopyModel.channels" items=snackChannelListModel.switchChannels itemValue="id" itemLabel="name" />
				</dd>
			</dl>
		</div>
        <@dwz.formBar />
       </@dwz.form>
    </div>
</div>