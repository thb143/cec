<div class="page">
    <div class="pageContent">
        <@dwz.form action="/price/specialPolicy-audit-refuse-save">
        <@s.hidden path="specialPolicyLog.id" value="${specialPolicyLog.id}"/>
        <div class="pageFormContent s-cols" layoutH="60">
            <dl>
                <dt>退回意见：</dt>
                <dd>
                    <@s.textarea path="specialPolicyLog.refuseNote" class="required m-input" rows="6" maxlength="800"/>
                </dd>
            </dl>
        </div>
        <@dwz.formBar />
        </@dwz.form>
    </div>
</div>
