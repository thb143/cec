<div class="accordion" fillSpace="sidebar">
    <div class="accordionHeader">
        <h2><span class="a12">Folder</span>订单管理</h2>
    </div>
    <div class="accordionContent">
    	<ul class="tree expand">
        	<li>
            	<a>票务订单</a>
	            <ul>
	                <@sec.any name="ORDER_VIEW">
	                	<li><@dwz.a href="/order/ticketOrder-paid-list">异常订单</@dwz.a></li>
	                	<li><@dwz.a href="/order/ticketOrder-success-list">成功订单</@dwz.a></li>
                    	<li><@dwz.a href="/order/ticketOrder-failed-list">失败订单</@dwz.a></li>
                    	<li><@dwz.a href="/order/ticketOrder-revoked-list">退票订单</@dwz.a></li>
                    	<li><@dwz.a href="/order/ticketOrder-unpaid-list">未支付订单</@dwz.a></li>
                    	<li><@dwz.a href="/order/ticketOrder-canceled-list">已取消订单</@dwz.a></li>
                    	<#--  
                    	<li><@dwz.a href="/order/ticketVoucher-list">选座票凭证</@dwz.a></li>
                    	-->
                    </@sec.any>
	            </ul>
	        </li>
	        <li>
            	<a>权益卡</a>
	            <ul>
	                <@sec.any name="ORDER_VIEW">
	                	<li><@dwz.a href="/order/benefitCardOpenOrder-list">开卡订单</@dwz.a></li>
	                	<li><@dwz.a href="/order/benefitCardRechargeOrder-list">续费订单</@dwz.a></li>
                    	<li><@dwz.a href="/order/benefitCardConsumeOrder-list">票务消费订单</@dwz.a></li>
                    	<li><@dwz.a href="/order/benefitCardConsumeSnackOrder-list">卖品消费订单</@dwz.a></li>
                    </@sec.any>
	            </ul>
	        </li>
	         <li>
            	<a>卖品订单</a>
	            <ul>
	                <@sec.any name="ORDER_VIEW">
	                	<li><@dwz.a href="/order/snackSuccessOrder-list">成功订单</@dwz.a></li>
	                	<li><@dwz.a href="/order/backSnackOrder-list">退卖品订单</@dwz.a></li>
                    </@sec.any>
	            </ul>
	        </li>
	    </ul>
    </div>
</div>