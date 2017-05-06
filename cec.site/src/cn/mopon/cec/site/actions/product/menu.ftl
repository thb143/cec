<div class="accordion" fillSpace="sidebar">
    <div class="accordionHeader">
        <h2><span class="a14">Folder</span>产品管理</h2>
    </div>
    <div class="accordionContent">
        
	        <ul class="tree expand">
	        	<@sec.any "PRODUCT_VIEW">
		            <li>
		                <a>选座票</a>
		                <ul>
		                	<li><@dwz.a href="/product/show-main">影院排期管理</@dwz.a></li>
		                	<li><@dwz.a href="/product/channelShow-main">渠道排期管理</@dwz.a></li>
		                </ul>
		            </li>
		            <li>
		                <a>卖品</a>
		                <ul>
		                	<li><@dwz.a href="/product/snackGroup-list">卖品类型</@dwz.a></li>
		                	<li><@dwz.a href="/product/snack-main">影院卖品</@dwz.a></li>
		                </ul>
		            </li>
	            </@sec.any>
	            <@sec.any "PRODUCT_VIEW,BENEFITCARDTYPE_AUDIT,BENEFITCARDTYPE_APPROVE">
		            <li>
		                <a>权益卡</a>
		                <ul>
		                	<@sec.any "PRODUCT_VIEW">
		                		<li><@dwz.a href="/product/benefitCardType-main">卡类管理</@dwz.a></li>
		                	</@sec.any>
		                	<@sec.any "BENEFITCARDTYPE_AUDIT">
		                		<li><@dwz.a href="/product/benefitCardType-audit-list">卡类审核</@dwz.a></li>
		                	</@sec.any>
		                	<@sec.any "BENEFITCARDTYPE_APPROVE">
		                		<li><@dwz.a href="/product/benefitCardType-approve-list">卡类审批</@dwz.a></li>
		                	</@sec.any>
		                	<@sec.any "PRODUCT_VIEW">
			                	<li><@dwz.a href="/product/benefitCardUser-list">用户管理</@dwz.a></li>
			                	<li><@dwz.a href="/product/benefitCard-list">卡管理</@dwz.a></li>
		                	</@sec.any>
		                </ul>
		            </li>
	            </@sec.any>
	        </ul>
       
    </div>
</div>