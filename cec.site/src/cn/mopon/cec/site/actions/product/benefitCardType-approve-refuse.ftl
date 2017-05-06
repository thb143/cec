<div class="page">
    <div class="pageContent">
        <@dwz.form action="/product/benefitCardType-approve-refuse-save">
        <@s.hidden path="cardTypeLog.id" value="${cardTypeLog.id}"/>
        <div class="pageFormContent s-cols" layoutH="60">
            <dl>
                <dt>退回意见：</dt>
                <dd>
                    <@s.textarea path="cardTypeLog.refuseNote" class="required m-input" rows="6" maxlength="800"/>
                </dd>
            </dl>
        </div>
        <@dwz.formBar />
        </@dwz.form>
    </div>
</div>
