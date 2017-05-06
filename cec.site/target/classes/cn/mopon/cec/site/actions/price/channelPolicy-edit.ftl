<div class="page">
	<div class="pageContent">
		<@dwz.form action="/price/channelPolicy-update">
			<@s.hidden path="channelPolicy.id" />
			<div class="pageFormContent" layoutH="60">
				<dl class="nowrap">
					<h3>${channelPolicy.channel.name}</h3>
				</dl>
				<div class="divider"></div>
				<dl class="nowrap">
					<dt><span>策略名称：</span></dt>
		            <dd><@s.input path="channelPolicy.name" maxlength="40" class="required m-input"/></dd>
		        </dl>
	            <dl>
	                <dt>起始日期：</dt>
	                <dd><@s.input path="channelPolicy.startDate" readonly="true" dateFmt="yyyy-MM-dd" class="required date"/></dd>
	            </dl>
	            <dl>
	                <dt>截止日期：</dt>
	                <dd><@s.input path="channelPolicy.endDate" readonly="true" dateFmt="yyyy-MM-dd" class="required date" minRelation="#startDate" customvalid="geDate(element,'#startDate')" title="截止日期不能小于起始日期。"/></dd>
	            </dl>
			</div>
			<@dwz.formBar />
		</div>
	</@dwz.form>
    </div>
</div>
<script type="text/javascript">
$(function(){
	if("${channelPolicy.valid}" == "${ValidStatus.VALID}"){
		$(".page").find("#startDate,#endDate").attr("disabled","disabled");
	}
});
</script>