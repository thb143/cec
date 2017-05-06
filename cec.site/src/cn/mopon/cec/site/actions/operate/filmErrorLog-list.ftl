<div class="page">
    <div class="pageContent">
    	<table class="table" width="100%" layoutH="112">
            <thead>
                <tr>
                    <th width="30" align="center">序号</th>
                    <th width="130" align="center">影片编码</th>
                    <th align="center">影片名称</th>
                    <th width="100" align="center">异常信息</th>
                </tr>
            </thead>
            <tbody>
                <#list filmSyncLog.errorLogs as filmErrorMsg>
	                <tr>
	                    <td>${filmErrorMsg_index+1}</td>
	                    <td>${filmErrorMsg.code}</td>
	                    <td class="l-text">${filmErrorMsg.name}</td>
	                    <td>${filmErrorMsg.msg}</td>
	                </tr>
              	</#list>
            </tbody>
        </table>
        <@dwz.formBar showSubmitBtn=false />
    </div>
</div>