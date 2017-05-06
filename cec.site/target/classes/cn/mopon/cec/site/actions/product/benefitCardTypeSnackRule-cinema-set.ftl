 <style type="text/css">
 table,td,th {border:solid 1px #CCC; padding:5px; border-collapse: collapse; font-size:12px; }
 </style>
<@pro.cinemaSnack path="snackRule.snacks"/>
<script type="text/javascript">
$(function(){
	var tree = $("#cinema-treetable");
	tree.treetable({
		expandable : true
	});
	$("#cinema-treetable").treetable('expandAll');
    
   	tree.find("input[type='checkbox'][alias='province']").each(function(){
   		var province = $(this);
   		province.change(function(){
    		tree.find("tr[pid='" +province.attr('id')+ "']").find("input[type='checkbox']").each(function(){
    			if(province.is(":checked")){
    				$(this).attr("checked","true");
    			}else{
    				$(this).removeAttr("checked");
    			}
    			checkCinema($(this));
    		});
    	});
    });
    tree.find("input[type='checkbox'][alias='city']").each(function(){
    	$(this).bind("change",function(){
    		checkCinema($(this));
    	});
    });
    tree.find("input[type='checkbox'][alias='cinema']").each(function(){
    	$(this).bind("change",function(){
    		checkCity($(this));
    	});
    });
    
    tree.find("input[type='checkbox'][alias='hall']").each(function(){
    	$(this).change(function(){
    		invert($(this));
    	});
    });
    
    function invert(hall){
    	var cinema = hall.closest("tr");
			var halls = cinema.find("input[alias='hall']").size();
			var checkHalls = cinema.find("input[alias='hall']:checked").size();
			if (halls == checkHalls) {
				cinema.find("input[alias='cinema']").attr("checked","true");
			}else{
				cinema.find("input[alias='cinema']").removeAttr("checked");
			}
		var city = $("#"+cinema.attr('pid'));
		checked(cinema,"cinema");
		checked(city,"city");
    }
    
    function checked(obj,alias){
    	var flag = true;
    	tree.find("tr[pid='" +obj.attr('pid')+ "']").find("input[alias='"+alias+"']").each(function(){
			if (!$(this).is(":checked")){
				return flag = false;
			}
		});
		if (flag) {
			$("#"+obj.attr("pid")).find("input[type='checkbox']").attr("checked","true");
		} else {
			$("#"+obj.attr("pid")).find("input[type='checkbox']").removeAttr("checked");
		}
    }
    
    function checkCinema(city){
    	tree.find("tr[pid='" +city.attr('pid')+ "']").find("input[type='checkbox']").each(function(){
			if(city.is(":checked")){
				$(this).attr("checked","true");
			}else{
				$(this).removeAttr("checked");
			}
		});
    }
    function checkCity(cinema){
    	var pId = cinema.closest("tr").attr('pid');
		var cineams=tree.find("tr[data-tt-parent-id='" + pId+ "']").find("input[type='checkbox'][alias='cinema']").size();
		var checkCinemas=tree.find("tr[data-tt-parent-id='" + pId+ "']").find("input[type='checkbox'][alias='cinema']:checked").size();
		tree.find("input[type='checkbox'][alias='city'][pId='" + pId + "']").each(function(){
			var pId = $(this).closest("tr").attr("pid");
			if(cinema.is(":checked") && checkCinemas == cineams && checkCinemas > 0){
				$(this).attr("checked","true");
				checkProvince(pId);
			}else{
				$(this).removeAttr("checked");
				$("#" + pId).find("input[type='checkbox']").removeAttr("checked");
			}
		});
    }
    function checkProvince(pId){
    	var cities=tree.find("tr[data-tt-parent-id='" + pId+ "']").find("input[type='checkbox'][alias='city']").size();
		var checkCities=tree.find("tr[data-tt-parent-id='" + pId+ "']").find("input[type='checkbox'][alias='city']:checked").size();
		if(cities == checkCities && checkCities > 0){
			$("#" + pId).find("input[type='checkbox']").attr("checked","true");
		}else{
			$("#" + pId).find("input[type='checkbox']").removeAttr("checked");
		}
    }
    setTimeout(function(){
    	var hallIds = $("#hallIds").val();
    	if(hallIds == ''){
	    	$(".hallids").each(function(){
	    		hallIds += $(this).val() + ",";
	    	});
    	}
    	tree.find('input[type="checkbox"][alias="hall"]').each(function(){
    		if(hallIds.indexOf($(this).attr('id')) != -1){
    			$(this).attr("checked","true");
    		}else{
    			$(this).removeAttr("checked");
    		}
    		tree.find("input[type='checkbox'][alias='hall']:checked").each(function(){
    			invert($(this));
    		});
    	});   	
    },30);
});
</script>