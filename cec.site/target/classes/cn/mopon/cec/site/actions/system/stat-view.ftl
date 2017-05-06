<div class="page">
    <div class="pageContent">
        <div class="pageFormContent" layoutH="55">

            <fieldset>
            <@dwz.form action="/system/dayStat-save" >
                <legend>报表日结维护<label></legend>
                <dl>
                    <dt>开始日期：</dt>
                    <dd><input name="startDate" id="startDate" maxDate="${beforeDate?date}" class="date required" dateFmt="yyyy-MM-dd"
                               readonly="true"/></dd>
                </dl>
                <dl>
                    <dt>截止日期：</dt>
                    <dd><input name="endDate" id="endDate" maxDate="${beforeDate?date}" class="date required" dateFmt="yyyy-MM-dd"
                               readonly="true" minRelation="#startDate" customvalid="gtDate(element,'#startDate')"
                               title="日期结束时间不能小于日期开始时间。"/></dd>
                </dl>
                <dl class="nowrap">
                    <div class="buttonActive">
                        <div class="buttonContent">
                            <button type="submit">提交</button>
                        </div>
                    </div>
                </dl>
            </@dwz.form>
            </fieldset>

            <fieldset>
            <@dwz.form action="/system/syncSaleStatDay">
                <legend>销售趋势统计维护<label></legend>
                <dl>
                    <dt>开始日期：</dt>
                    <dd><input name="start" id="start" maxDate="${beforeDate?date}" class="date required" dateFmt="yyyy-MM-dd"
                               readonly="true"/></dd>
                </dl>
                <dl>
                    <dt>截止日期：</dt>
                    <dd><input name="end" id="end" maxDate="${beforeDate?date}" class="date required" dateFmt="yyyy-MM-dd"
                               readonly="true" minRelation="#start"  customvalid="gtDate(element,'#start')"
                               title="日期结束时间不能小于日期开始时间。"/></dd>
                </dl>
                <dl class="nowrap">
                    <div class="buttonActive">
                        <div class="buttonContent">
                            <button type="submit">提交</button>
                        </div>
                    </div>
                </dl>
            </@dwz.form>
            </fieldset>


            <fieldset>
            <@dwz.form action="/system/syncSaleStatAll">
                <legend>累计订单信息维护</legend>
                <dl class="nowrap">
                    重新计算：订单总数、出票总数、订单总金。
                </dl>
                <dl class="nowrap">
                    <div class="buttonActive">
                        <div class="buttonContent">
                            <button type="submit">提交</button>
                        </div>
                    </div>
                </dl>
            </@dwz.form>
            </fieldset>

            <fieldset>
            <@dwz.form action="/system/syncTodayRanks">
                <legend>当天销售排行维护</legend>
                <dl class="nowrap">
                    重新计算：今日影院排行、今日渠道排行、今日影片排行、今日特价排行、今日订单、今日出票、今日总金额。
                </dl>
                <dl class="nowrap">
                    <div class="buttonActive">
                        <div class="buttonContent">
                            <button type="submit">提交</button>
                        </div>
                    </div>
                </dl>
            </@dwz.form>
            </fieldset>

            <fieldset>
            <@dwz.form action="/system/resetMaxOrderDay">
                <legend>重置最高订单日</legend>
                <dl class="nowrap">
                    重置最高订单日，重置之后从销售趋势历史数据中重新获取最高订单日。
                </dl>
                <dl class="nowrap">
                    <div class="buttonActive">
                        <div class="buttonContent">
                            <button type="submit">提交</button>
                        </div>
                    </div>
                </dl>
            </@dwz.form>
            </fieldset>
        </div>

    </div>
</div>