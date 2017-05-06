<div class="page">
    <div class="pageContent">
        <div class="panelBar">
            <ul class="toolBar">
                <li><@dwz.a href="/product/snackType-add?groupId=${groupId}" target="dialog"  title="新增卖品类型" height="S" width="M"><span class="a09">新增卖品类型</span></@dwz.a></li>
            </ul>
        </div>
        <div class="pageFormContent" layoutH="48">
        	<#if snackTypes?size gt 0>
	    		<div style="width: 99%;word-wrap:break-word;">
	            	<#list snackTypes as snackType>
	                	<div class="uploadify-queue-item" style="width:550px;float:left;margin:5px 90px 5px 10px;word-wrap:break-word;">
	                  		<div style="float:left;">
	                  			<span><img src="${imagePath}${snackType.image.path}_190x190.jpg" width="190" height="190" title="${snackType.name}"/></span>
	                  		</div>
	                  		<div style="float:left;width:130px;">
	                  			<div style="margin:20px 0px 0px 10px;word-wrap:break-word;"><h3>${snackType.name}</h3></div>
		                  		<div style="margin:40px 0px 0px 10px;word-wrap:break-word;" title="${snackType.remark}"><span><#if snackType.remark?length gt 18>${snackType.remark?substring(0,18)}...<#else>${snackType.remark}</#if></span></div>
		                  		<div style="margin:40px 0px 0px 20px;word-wrap:break-word;" class="buttonContent">
					            	<@dwz.a href="/product/snackType-edit?snackType=${snackType.id}" class="button" target="dialog" title="编辑卖品类型" height="S" width="M"><span>编辑</span></@dwz.a>
		                  		</div>
	                  		</div>
	             	  	</div>
	            	</#list>
	            </div>
	    	</#if>
	    </div>
    </div>
</div>