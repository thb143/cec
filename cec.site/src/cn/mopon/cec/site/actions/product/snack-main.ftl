<div class="page">
	<div class="tree-l-box">
		<div class="pageHeader">
			<@dwz.pageForm action="/product/snack-main" buttonText="筛选" alt="可输入影院名称、行政区划、接入商检索" />
	    </div>
		<div class="pageContent" layoutH="35">
			<#if cinemaPage.contents?size gt 0>
				<ul class="tree expand" layoutH="62">
					<#list cinemaPage.contents as cinema>
						<li>
					        <@dwz.a href="/product/snack-list?cinema=${cinema.id}" target="ajax" rel="snackListBox">[${cinema.county.city.name?substring(0,2)}]${cinema.name}</@dwz.a>
				        </li>
			        </#list>
			    </ul>
			    <div class="panelBar">
		            <@sys.pageNav pageModel=cinemaPage />
		        </div>
		    <#else>
		    	<div class="tree-msg" layoutH="72">
		    		<h3>没有符合条件的影院。</h3>
		    	</div>
		    </#if>
		</div>
	</div>
	<div id="snackListBox" class="tree-r-box"></div>
</div>
<script>
    $(function() {
        setTimeout(function() {
            $("a[rel=snackListBox]").eq(0).click();
        }, 200);
    });
</script>