<div class="page">
    <div class="pageContent">
    	<@dwz.form action="/operate/film-sync">
            <div class="pageFormContent" layoutH="60">
                <dl>
                    <dt>开始日期：</dt>
                    <dd><@s.input path="searchModel.startDate" class="date required" dateFmt="yyyy-MM-dd" readonly="true" /></dd>
                </dl>
                <dl>
                    <dt>截止日期：</dt>
                    <dd><@s.input path="searchModel.endDate" class="date required" dateFmt="yyyy-MM-dd" readonly="true" minRelation="#startDate" /></dd>
                </dl>
            </div>
            <@dwz.formBar submitBtnText="同步"/>
        </@dwz.form>
    </div>
</div>