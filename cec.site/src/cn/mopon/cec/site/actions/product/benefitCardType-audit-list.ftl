<div class="tree-l-box" layoutH="0">
	<div class="pageHeader">
		<@dwz.pageForm action="/product/benefitCardType-audit-list" buttonText="筛选" alt="可输入卡类名称检索" />
    </div>
	<div class="pageContent" layoutH="40">
		<#if cardTypeLogs?size gt 0>
			<ul class="tree expand">
					<li>
			            <a>待审核权益卡</a>
			            <ul>
			                <#list cardTypeLogs as cardTypeLog>
				                <li>
				                    <@dwz.a href="/product/benefitCardType-audit-view?cardTypeLogId=${cardTypeLog.id}" target="ajax" rel="cardTypeAuditBox">${cardTypeLog.type.name}</@dwz.a>
				                    <ul>
						                <li>
						                    <@dwz.a href="/product/benefitCardTypeLog-rule-view?id=${cardTypeLog.id}" target="ajax" rel="cardTypeAuditBox">票务规则</@dwz.a>
						                </li>
						                <li>
						                    <@dwz.a href="/product/benefitCardTypeLog-snackRule-view?id=${cardTypeLog.id}" target="ajax" rel="cardTypeAuditBox">卖品规则</@dwz.a>
						                </li>
						            </ul>
				                </li>
			                </#list>
			            </ul>
			        </li>
		    </ul>
	    <#else>
	    	<div class="tree-msg">
	    		<h3>没有待审核的权益卡类。</h3>
	    	</div>
	    </#if>
	</div>
</div>
<div id="cardTypeAuditBox" class="tree-r-box" layoutH="0"></div>
<script>
    $(function() {
        setTimeout(function() {
            $("a[rel=cardTypeAuditBox]").eq(0).click();
        }, 30);
    }); 
</script>