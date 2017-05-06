<div class="page">
	<div class="tree-l-box" layoutH="0">
	    <@sys.organTree rootOrgan />
	    <@dwz.formBar showSubmitBtn=false showCancelBtn=false>
    		<li><@dwz.a class="button" href="/system/organ-add" target="dialog"><span>新增机构</span></@dwz.a></li>
	    </@dwz.formBar>
	</div>
	<div id="organBox" class="tree-r-box" layoutH="0"></div>
</div>
<script>
	$(function() {
		setTimeout(function() {
			$("a[organId=${selectedOrganId}]").click();
		}, 300);
	}); 
</script>