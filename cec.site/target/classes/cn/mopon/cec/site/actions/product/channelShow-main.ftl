<div class="page">
	<div class="tree-l-box" style="width:18%;">
		<div class="pageContent" layoutH="0">
			<#if channels?size gt 0>
				<ul class="tree expand">
					<#list channels as channel>
						<li><@dwz.a href="/product/channelShow-list?channelId=${channel.id}" target="ajax" rel="channelShowListBox">${channel.name}</@dwz.a></li>
			        </#list>
			    </ul>
		    <#else>
		    	<div class="tree-msg">
		    		<h3>没有符合条件的渠道。</h3>
		    	</div>
		    </#if>
		</div>
	</div>
	<div id="channelShowListBox" class="tree-r-box" style="width:81.8%"></div>
</div>
<script>
    $(function() {
        setTimeout(function() {
            $("a[rel=channelShowListBox]").eq(0).click();
        }, 200);
    });
</script>