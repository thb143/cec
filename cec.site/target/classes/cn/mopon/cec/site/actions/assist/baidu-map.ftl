<script type="text/javascript" src="<@s.url '/std/js/jquery-1.7.2.min.js' />"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=78a11e1d632aedf16cd2a84c52c48f24"></script>
<input type="button" value="标注" id="marker" onclick="marker()" style="float:right;"/>
<div id="container" 
    style="position: absolute;
       width: 99%; 
       top: 35px;
       height: 93%;
       left:1px;
       overflow:hidden;">
</div>
<script type="text/javascript">
	var type="${type}".replace(/[ ]/g,"");
	var lastMarker,curLNG,curLAT;
	var address = $(window.parent.document).find("#"+type+" a[name='county']").text() + " " + $(window.parent.document).find("#"+type+" #address").val();
	var city = $(window.parent.document).find("#"+type+" a[name='city']").text();
	var province = $(window.parent.document).find("#"+type+" a[name='province']").text();
	var map = new BMap.Map("container");
	if(city.indexOf("省直辖")>-1){
		map.centerAndZoom(province, 9);
	}else{
		address=city+address;
	}
	map.enableScrollWheelZoom();    //启用滚轮放大缩小，默认禁用
	map.enableContinuousZoom();    //启用地图惯性拖拽，默认禁用
	map.addControl(new BMap.NavigationControl());  //添加默认缩放平移控件
	map.addControl(new BMap.OverviewMapControl()); //添加默认缩略地图控件
	map.addControl(new BMap.OverviewMapControl({ isOpen: true, anchor: BMAP_ANCHOR_BOTTOM_RIGHT }));   //右下角，打开
	var localSearch = new BMap.LocalSearch(map);
	localSearch.enableAutoViewport(); //允许自动调节窗体大小
	map.addEventListener("click",function(e){
		curLNG = e.point.lng;
		curLAT = e.point.lat;
		curMarker = new BMap.Marker(new BMap.Point(curLNG, curLAT));
		addMarker(curMarker);
	});
	localSearch.setSearchCompleteCallback(function (searchResult) {
		var poi = searchResult.getPoi(0);
		curLNG = poi.point.lng;
		curLAT = poi.point.lat;
		curMarker = new BMap.Marker(new BMap.Point(curLNG, curLAT));
		map.centerAndZoom(poi.point,12);
		localSearch = new BMap.LocalSearch(map);
		addMarker(curMarker);
		
	});
	// 没有标注过经纬度，则通过城市+地址进行搜索。
	if($(window.parent.document).find("#"+type+" input[name='longitude']").val() == "" && 
			$(window.parent.document).find("#"+type+" input[name='latitude']").val() == "") {
			localSearch.search(address);
		
	} else {
		curLNG = $(window.parent.document).find("#"+type+" input[name='longitude']").val();
		curLAT = $(window.parent.document).find("#"+type+" input[name='latitude']").val();
		var point=new BMap.Point(curLNG, curLAT);
		curMarker = new BMap.Marker(point);
		map.centerAndZoom(point,12);
		localSearch = new BMap.LocalSearch(map);
		addMarker(curMarker);
	}
	// 创建标注
	function addMarker(marker){
	  removeMarker();
	  map.addOverlay(marker);
	  lastMarker = marker;
	  
	}
	// 删除标注
	function removeMarker() {
		if(lastMarker) {
			map.removeOverlay(lastMarker);
		}
	}
	// 标注
	function marker() {
		if(typeof(curLNG) != "undefined" && typeof(curLNG) != "undefined") {
			$(window.parent.document).find("#"+type+" input[name='longitude']").val(curLNG);
			$(window.parent.document).find("#"+type+" input[name='latitude']").val(curLAT);
			window.parent.closeThisWindow();
		} else {
			alert("请先标注，再点击。");
		}
	}
</script>