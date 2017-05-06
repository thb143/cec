<div class="accordion" fillSpace="sidebar">
    <div class="accordionHeader">
        <h2><span class="a15">Folder</span>价格管理</h2>
    </div>
    <div class="accordionContent">
    	<@sec.any "POLICY_VIEW">
	        <ul class="tree expand">
	            <li>
	                <a>影院定价</a>
	                <ul>
	                	<@sec.any "POLICY_VIEW">
	                    	<li><@dwz.a href="/price/cinemaPolicy-list">影院结算策略</@dwz.a></li>
	                    </@sec.any>
	                    <@sec.any "POLICY_AUDIT">
                    		<li><@dwz.a href="/price/cinemaPolicy-audit-list">影院结算策略审核</@dwz.a></li>
                    	</@sec.any>
                    	<@sec.any "POLICY_APPROVE">
                    		<li><@dwz.a href="/price/cinemaPolicy-approve-list">影院结算策略审批</@dwz.a></li>
                    	</@sec.any>
                    	<@sec.any "POLICY_VIEW">
	                   		<li><@dwz.a href="/price/cinemaPolicy-refuse-list">退回的影院结算策略</@dwz.a></li>
	                   	</@sec.any>
	                </ul>
	            </li>
	            <li>
	                <a>渠道定价</a>
	                <ul>
	                	<@sec.any "POLICY_VIEW">
		                    <li><@dwz.a href="/price/channelPolicy-list">渠道结算策略</@dwz.a></li>
		                </@sec.any>
	                    <@sec.any "POLICY_AUDIT">
	                    	<li><@dwz.a href="/price/channelPolicy-audit-list">渠道结算策略审核</@dwz.a></li>
	                    </@sec.any>
                    	<@sec.any "POLICY_APPROVE">
	                    	<li><@dwz.a href="/price/channelPolicy-approve-list">渠道结算策略审批</@dwz.a></li>
		                </@sec.any>
                    	<@sec.any "POLICY_VIEW">
		                   	<li><@dwz.a href="/price/channelPolicy-refuse-list">退回的渠道结算策略</@dwz.a></li>
		                </@sec.any>
	                </ul>
	            </li>
	            <li>
	                <a>特殊定价</a>
	                <ul>
	                	<@sec.any "POLICY_VIEW">
	                    	<li><@dwz.a href="/price/specialPolicy-list">特殊定价策略</@dwz.a></li>
	                    </@sec.any>
	                    <@sec.any "POLICY_AUDIT">
	                    	<li><@dwz.a href="/price/specialPolicy-audit-list">特殊定价策略审核</@dwz.a></li>
	                    </@sec.any>
	                    <@sec.any "POLICY_APPROVE">
	                    	<li><@dwz.a href="/price/specialPolicy-approve-list">特殊定价策略审批</@dwz.a></li>
	                    </@sec.any>
	                    <@sec.any "POLICY_VIEW">
	                   		<li><@dwz.a href="/price/specialPolicy-refuse-list">退回的特殊定价策略</@dwz.a></li>
	                   	</@sec.any>
	                </ul>
	            </li>
	        </ul>
		</@sec.any>
    </div>
</div>