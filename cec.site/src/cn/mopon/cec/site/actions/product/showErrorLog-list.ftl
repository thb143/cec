<div class="page">
    <div class="pageContent">
        <table class="table" width="100%" layoutH="60">
            <thead>
                <tr>
                   <th width="30" align="center">序号</th>
                   <th width="130" align="center">排期编码</th>
                   <th width="70" align="center">影院编码</th>
                   <th width="130" align="center">影厅编码</th>
                   <th width="100" align="center">影片编码</th>
                   <th width="120" align="center">放映时间</th>
                   <th width="60" align="right">最低票价</th>
                   <th width="60" align="right">标准票价</th>
                   <th>异常信息</th>
                </tr>
            </thead>
            <tbody>
                <#list showSyncLog.errorLogs as showErrorMsg>
                <tr>
                    <td>${showErrorMsg_index+1}</td>
                    <td>${showErrorMsg.showCode}</td>
                    <td>${showErrorMsg.cinemaCode}</td>
                    <td>${showErrorMsg.hallCode}</td>
                    <td>${showErrorMsg.filmCode}</td>
                    <td>${showErrorMsg.showTime?string('yyyy-MM-dd HH:mm')}</td>
                    <td>${showErrorMsg.minPrice?string(',##0.00')}</td>
                    <td>${showErrorMsg.stdPrice?string(',##0.00')}</td>
                    <td>${showErrorMsg.msg}</td>
                </tr>
                </#list>
            </tbody>
        </table>
        <@dwz.formBar showSubmitBtn=false/>
    </div>
</div>