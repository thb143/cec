<div class="page">
	<div class="pageContent">
		<@dwz.form action="/price/channelRuleGroup-save-ordinal" >
		<table id="sort" class="list" width="100%" layoutH="50">
			<thead>
				<tr>
					<th width="50" align="center">序号</th>
					<th align="center">影院名称</th>
					<th width="180" align="center">操作</th>
				</tr>
			</thead>
			<tbody>
				 <#list channelPolicy.groups as group>
					  <tr class="group">
						  <td  align="center">${group.ordinal}</td>
						  <td><span class="cinema" style="cursor:move;display:block;width:450px"><input type="hidden" value="${group.id}" name="groupIds"/>${group.cinema.name}</span></td>
					      <td></td>
					  </tr>
				</#list>
			</tbody>
		</table>
		<@dwz.formBar />
		</@dwz.form>
	</div>
</div>
<script type="text/javascript">
function render(size) {
	$("#sort tbody tr").each(function(){
		var tr = $(this);
		var td = tr.find("td").eq(0);
		var index = tr.index();
		td.find("input").val(index+1);
		td.text("");
		td.text(index+1);
		
		var td = tr.find("td").eq(2);
		td.empty();
		if (tr.find("td").eq(0).text() == 1 && tr.find("td").eq(0).text() == size) {
			return;
		}
		if (tr.find("td").eq(0).text() == 1) {
			td.append("<a href='#' class='down' onclick='moveDown(this,"+size+")'>下移</a>");
		} else if (tr.find("td").eq(0).text() == size) {
			td.append("<a href='#' class='top' onclick='moveTop(this,"+size+")'>置顶</a>&nbsp;&nbsp;");
			td.append("<a href='#' class='up' onclick='moveUp(this,"+size+")'>上移</a>");
		} else {
			td.append("<a href='#' class='top' onclick='moveTop(this,"+size+")'>置顶</a>&nbsp;&nbsp;");
			td.append("<a href='#' class='up' onclick='moveUp(this,"+size+")'>上移</a>&nbsp;&nbsp;");
			td.append("<a href='#' class='down' onclick='moveDown(this,"+size+")'>下移</a>");
		}
	});
}
//置顶
function moveTop(obj,size){
	var tr = $(obj).closest("tr");
	var first = tr.closest("tbody").find("tr").first();
	first.before(tr);
	render(size);
}
//上移
function moveUp(obj,size){
	var tr = $(obj).closest("tr");
	var prev = tr.prev();
	tr.after(prev);
	render(size);
}
//下移
function moveDown(obj,size){
	var tr = $(obj).closest("tr");
	var next = tr.next();
	tr.before(next);
	render(size);
}

$(function(){
	var size = "${channelPolicy.groups?size}";
	render(size);
	$("#sort tbody").on("mousedown", "tr", function() {
		$("#sort tbody .selected").not(this).removeClass("selected");
		$(this).toggleClass("selected");
	});
	
	$("#sort tbody .cinema").draggable({
		helper: "clone",
		opacity: .75,
		refreshPositions: true,
		revert: "invalid",
		revertDuration: 300,
		containment: "#sort",
		scroll: true,
		start:function(e,ui){},
		drag:function(e,ui){}, //拖动时执行的函数
		stop:function(e,ui){} 
	});

	$("#sort tbody tr").each(function() {
		$(this).droppable({
			drop: function(e, ui) {
				if ($(this).index() != $(ui.draggable).closest("tr").index()) {
					$(this).after($(ui.draggable).closest("tr"));
					$(ui.draggable).closest("tr").addClass("selected");
					render(size);
				}
			},
			hoverClass: "selected",
			over: function(e, ui) {
				var droppedEl = ui.draggable.parents("tr");
			}
		});
	});
});
</script>