<div class="page">
    <div class="pageContent">
        <@dwz.form action="/price/specialPolicy-submit-save">
        <@s.hidden path="specialPolicyLog.policy" />
        <div class="pageFormContent s-cols" layoutH="60">
            <dl>
                <dt>提交说明：</dt>
                <dd><@s.textarea path="specialPolicyLog.submitRemark" maxlength="800" rows="6" class="required m-input"/></dd>
            </dl>
        </div>
        <@dwz.formBar submitBtnText="提交"/>
        </@dwz.form>
    </div>
</div>