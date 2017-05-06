<div class="page">
    <div class="pageContent">
    <@dwz.form action="/price/cinemaPolicy-update">
	    <@s.hidden path="cinemaPolicy.id" />
	    <div class="pageFormContent" layoutH="60">
	    	<dl class="nowrap">
	    		<h3>${cinemaPolicy.cinema.name}</h3>
			</dl>
			<div class="divider"></div>
			<dl class="nowrap">
				<dt><span>策略名称：</span></dt>
	            <dd><@s.input path="cinemaPolicy.name" maxlength="40" class="required m-input"/></dd>
	        </dl>
	        <dl>
	         	<dt>起始日期：</dt>
	            <dd><@s.input path="cinemaPolicy.startDate" readonly="true" dateFmt="yyyy-MM-dd" class="required date" /></dd>
	        </dl>
	        <dl>
	        	<dt>截止日期：</dt>
	            <dd><@s.input path="cinemaPolicy.endDate" readonly="true" dateFmt="yyyy-MM-dd" class="required date" minRelation="#startDate" customvalid="geDate(element,'#startDate')" title="截止日期不能小于起始日期。"/></dd>
	        </dl>
	        <dl class="nowrap">
                <dt>上报类型：</dt>
                <dd><@s.radios path="cinemaPolicy.submitType" items=SubmitType?values itemValue="value" itemLabel="text" prefix="<label class='dd-span'>" suffix="</label>"  class="required"/></dd>
	        </dl>
	        <dl>
                <dt>加减值：</dt>
                <dd><@s.input path="cinemaPolicy.amount" class="required" min="-100" max="100" customvalid="regexCanNegativePrice(element)" title="请输入-100~100的数字，格式：0.00,如：1.00。"/></dd>
	        </dl>
	     </div>
	     <@dwz.formBar />
	</div>
    </@dwz.form>
    </div>
</div>
<script type="text/javascript">
$(function(){
	if("${cinemaPolicy.valid}" == "${ValidStatus.VALID}"){
		$(".page").find("#startDate,#endDate").attr("disabled","disabled");
	}
});
</script>