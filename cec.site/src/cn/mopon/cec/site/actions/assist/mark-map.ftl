<script type="text/javascript">
window.closeThisWindow = function(){
	$.pdialog.close("mark-map");
};
var type="${type}".replace(/[ ]/g,"");
$(document).ready(function() {
	if($(window.parent.document).find("#"+ type +" a[name='county']").attr("value")== "") {
		alertMsg.error("请选择所属辖区。");
		$.pdialog.close("mark-map");
	}
	var curPage = $("div[class*=unitBox]",$.pdialog.getCurrent());
	curPage.append("<iframe src=<@s.url '/assist/baidu-map?type=+"+type+"' /> width='100%' height='100%' id='baiduMap' />");
});
</script>

