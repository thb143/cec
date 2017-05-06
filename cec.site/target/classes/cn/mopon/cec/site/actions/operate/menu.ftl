<div class="accordion" fillSpace="sidebar">
    <div class="accordionHeader">
        <h2><span class="a33">Folder</span>运营管理</h2>
    </div>
    <div class="accordionContent">
        <ul class="tree expand">
            <li>
                <a>基础信息</a>
                <ul>
                    <@sec.any name="CIRCUITSETTINGS_MANAGE">
                    	<li><@dwz.a href="/operate/accessType-list">接入类型</@dwz.a></li>
                    </@sec.any>
                    <@sec.any name="CINEMA_VIEW">
                    	<li><@dwz.a href="/operate/cinema-list">影院管理</@dwz.a></li>
                    </@sec.any>
                    <@sec.any name="FILM_VIEW">
                    	<li><@dwz.a href="/operate/film-list">影片管理</@dwz.a></li>
                    </@sec.any>
                    <@sec.any name="CIRCUITSETTINGS_MANAGE">
                    	<li><@dwz.a href="/operate/circuitSettings-view">参数设置</@dwz.a></li>
                    </@sec.any>
                    <@sec.any name="FILM_VIEW">
                    	<li><@dwz.a href="/operate/cityGroup-list">城市分组</@dwz.a></li>
                    </@sec.any>
                    <@sec.any name="CINEMA_MANAGE">
                    <li><@dwz.a href="/operate/hallType-list">影厅类型</@dwz.a></li>
                    </@sec.any>
                </ul>
            </li>
        	<@sec.any name="CHANNEL_VIEW">
	            <li>
	                <a>渠道管理</a>
	                <ul>
	                    <li><@dwz.a href="/operate/channel-list">渠道管理</@dwz.a></li>
	                </ul>
	            </li>
        	</@sec.any>
        </ul>
    </div>
</div>

