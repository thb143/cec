<div class="page">
    <div class="pageContent">
    	<div class="pageFormContent" layoutH="60">
    		<#if errorMessage??>
    			<dl class="nowrap ${StatusColor.RED}">
    				<dt>同步订单失败：</dt>
    				<dd>${errorMessage}</dd>
    			</dl>
    		</#if>
            <dl>
            	<dt>影院名称： </dt>
            	<dd>${localOrder.cinema.name}</dd>
            </dl>
            <dl>
                <dt>影院编码： </dt>
                <dd>${localOrder.cinema.code}</dd>
            </dl>
            <dl>
                <dt>订单号：</dt>
                <dd>${localOrder.code}</dd>
            </dl>
            <#if remoteOrder??>
	            <dl>
	                <dt>影院订单号：</dt>
	                <dd>${remoteOrder.cinemaOrderCode}</dd>
	            </dl>
	            <dl>
	                <dt>取票号：</dt>
	                <dd>${remoteOrder.voucher.printCode}</dd>
	            </dl>
	            <dl>
	                <dt>取票验证码：</dt>
	                <dd>${remoteOrder.voucher.verifyCode}</dd>
	            </dl>
	            <dl>
	            	<dt>订单状态：</dt>
	            	<dd>${remoteOrder.status}</dd>
	            </dl>
            </#if>
        </div>
        <@dwz.formBar showSubmitBtn=false>
	    	<#if localOrder.isMarkFailable()>
                <li>
                    <div class="buttonContent">
                        <@dwz.a href="/order/ticketOrder-mark-failed?ticketOrder=${localOrder.id}" class="button" target="ajaxTodo" title="您是否确定要标记出票失败？" callback="dialogAjaxDone"><span>标记出票失败</span></@dwz.a>
                    </div>
                </li>
            </#if>
            <#if remoteOrder?? && remoteOrder.status == TicketOrderStatus.SUCCESS && localOrder.isExpired()>
                 <li>
                     <div class="buttonContent">
                         <@dwz.a href="/order/ticketOrder-revoke?ticketOrder=${localOrder.id}" class="button" target="ajaxTodo" title="您是否确定要退票？" callback="dialogAjaxDone"><span>退票</span></@dwz.a>
                     </div>
                 </li>
            </#if>
            <#if localOrder.isRevokeable()>
                <li>
                    <div class="buttonContent">
                        <@dwz.a href="/order/ticketOrder-mark-revoked?ticketOrder=${localOrder.id}" class="button" target="ajaxTodo" title="您是否确定要标记退票？" callback="dialogAjaxDone"><span>标记退票</span></@dwz.a>
                    </div>
                </li>
         	</#if>
	    </@dwz.formBar>
    </div>
</div>