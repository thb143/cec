<div class="page">
    <div class="pageContent">
	    <div class="tabs" currentIndex="0" eventType="click">
	    	<div class="tabsHeader">
	    		<div class="tabsHeaderContent">
	    			<ul>
	    				<li><a href="javascript:;"><span>排期信息</span></a></li>
	    				<li><@s.a href="/product/channelShow-normal-order-list?channelShow=${channelShow.id}" class="j-ajax"><span>正常订单</span></@s.a></li>
	    				<li><@s.a href="/product/channelShow-revoke-order-list?channelShow=${channelShow.id}" class="j-ajax"><span>退票订单</span></@s.a></li>
	    			</ul>
	    		</div>
			</div>
			<div class="tabsContent">
				<div>
					<div class="page">
						<div class="pageFormContent" layoutH="60" >
							<@pro.channelShow_view />
						</div>
					</div>
				</div>
				<div></div>
				<div></div>
			</div>
			<div class="tabsFooter">
				<div class="tabsFooterContent"></div>
			</div>
		</div>
	</div>
</div>