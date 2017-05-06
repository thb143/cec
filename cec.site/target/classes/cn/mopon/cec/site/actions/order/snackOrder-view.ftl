<div class="page">
	<div class="pageContent">
	    <div class="tabs" eventtype="click" currentindex="0">
	        <div class="tabsHeader">
	            <div class="tabsHeaderContent">
	                <ul>
	                    <li><a href="javascript:;"><span>订单信息</span></a></li>
	                    <#if order.voucher??>
	                    	<li><a href="javascript:;"><span>凭证信息</span></a></li>
	                    </#if>
	                </ul>
	            </div>
	        </div>
			<div class="tabsContent">
				<div>
					<div class="page">
						<div class="pageFormContent" layoutH="60">
							<@ord.snackOrder_view order />
							<@ord.snackOrder_snacks order />
						</div>
					</div>
				</div>
				<#if order.voucher??>
					<div>
						<div class="page">
							<div class="pageContent">
								<div class="pageFormContent" layoutH="60" >
									<@ord.snackVoucher_view order.voucher />
								</div>
							</div>
						</div>
					</div>
				</#if>
		        <div class="tabsFooter">
					<div class="tabsFooterContent"></div>
				</div>
			</div>
		</div>
	</div>
</div>