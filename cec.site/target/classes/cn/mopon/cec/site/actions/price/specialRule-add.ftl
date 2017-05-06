<div class="page">
   <div class="pageContent">
        <@dwz.form action="/price/specialRule-save">
        <@s.hidden path="specialRule.policy" />    
        <div class="pageFormContent" layoutH="60" >
            <dl class="nowrap">
                <dt>规则名称：</dt>
                <dd>
                    <@s.input path="specialRule.name" class="required m-input" maxlength="20"/>
                </dd>
            </dl>
            <dl class="nowrap">
				<dt>放映类型：</dt>
				<dd>
                  	<@s.radios path="specialRule.showType" items=specialRule.policy.showTypes itemValue="value" itemLabel="text" prefix="<label class='dd-span'>" suffix="</label>"/>
				</dd>
			</dl>
			<#if specialRule.policy.type == SpecialPolicyType.FILM>
	            <dl class="nowrap">
					<dt>选择影片：</dt>
					<dd>
	                  	<@dwz.checkboxs path="specialRule.films" items=specialRule.policy.films itemValue="id" itemLabel="name" />
					</dd>
				</dl>
			</#if>
            <@pri.select_hall path="specialRule.halls" policyId=specialRule.policy.id/>
            <@ass.periodRule path="specialRule.periodRule" />
            <dl class="nowrap">
                <dt>上报类型：</dt>
                <dd><@s.radios path="specialRule.submitType" items=SubmitType?values itemValue="value" itemLabel="text" prefix="<label class='dd-span'>" suffix="</label>"  class="required"/></dd>
            </dl>
            <dl>
                <dt>加减值：</dt>
                <dd><@s.input path="specialRule.amount" class="required" min="-100" max="100" customvalid="regexCanNegativePrice(element)" title="请输入-100~100的数字，格式：0.00,如：1.00。"/></dd>
	        </dl>
            <@ass.settleRule path="specialRule.settleRule" ruleType="specialRule" />
        </div>
         <@dwz.formBar />
        </@dwz.form>
    </div>
</div>
<script type="text/javascript">
$(function(){
	$(".page").find("input[name='showType']:radio").eq(0).attr("checked","checked");
	$(".page").find("input[name='settleRule.basisType']:radio").eq(2).parent().hide();
	$(".page").find("input[name='submitType']:first").attr("checked","checked");
});
</script>