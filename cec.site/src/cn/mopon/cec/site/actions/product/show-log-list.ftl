<div class="page">
    <div class="pageContent">
    	<div class="pageFormContent" layoutH="60">
    		<#list logs as log>
    			<div class="d-line"></div>
				<@sys.log_view log />
			</#list>
		</div>
    </div>
</div>