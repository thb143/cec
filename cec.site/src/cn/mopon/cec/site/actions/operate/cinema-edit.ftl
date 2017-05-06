<div class="page">
    <div class="pageContent" id="cinemaEdit">
        <@dwz.form action="/operate/cinema-update" >
        	<@s.hidden path="cinema.id" />
	        <div class="pageFormContent" layoutH="55">
	        	<div style="width:800px;">
		            <dl>
		                <dt>影院编码：</dt>
		                <dd>
		                	<#if cinema.halls?size gt 0>
		                		<@s.input path="cinema.code" maxlength="20" class="required" readonly="readonly" />
		                	<#else>
		                		<@s.input path="cinema.code" maxlength="20" class="required" customvalid="regexSpace(element)" title="影院编码不能包含空格。"/>
		                	</#if>
		                </dd>
		            </dl>
		            <dl>
		            	<dt>接入商类型：</dt>
		            	<dd>
		            		<@s.select path="cinema.provider" items=Provider?values itemValue="value" itemLabel="text" class="combox" />
		            	</dd>
		            </dl>
		            <dl>
		                <dt>影院名称：</dt>
		                <dd><@s.input path="cinema.name" maxlength="120" class="required" /></dd>             
		            </dl>
		            <dl>
		                <dt>影厅数量：</dt>
		                <dd><@s.input path="cinema.hallCount" min="0" max="1000" maxlength="4" class="required digits"/></dd>
		            </dl>
		            <dl>
		                <dt>客服电话：</dt>
		                <dd><@s.input path="cinema.tel" maxlength="120" class="required" /></dd>
		            </dl>
		            <dl>
		                <dt>综合评分：</dt>
		                <dd><@s.input path="cinema.grade" class="number" max="100" /></dd>
		            </dl>
		            <dl>
		                <dt>影院排序：</dt>
		                <dd><@s.input path="cinema.ordinal" min="0" max="1000" maxlength="4" class="required digits" /></dd>
		            </dl>
		            <dl class="nowrap">
		                <dt>所属辖区：</dt>
		                <dd><@ass.countyCombox path="cinema.county" /></dd>
		            </dl>
		            <dl class="nowrap">
		                <dt>影院地址：</dt>
		                <dd><@s.input path="cinema.address" maxlength="120" class="required l-input" /></dd>
		            </dl>
		            <dl class="nowrap">
		                <dt>影院网址：</dt>
		                <dd><@s.input path="cinema.url" maxlength="120" class="url l-input" /></dd>
		            </dl>
		            <dl class="nowrap">
		                <dt>经度/纬度：</dt>
		                <dd>
		                	<@s.input path="cinema.longitude" maxlength="20" readonly="true"/><@s.input path="cinema.latitude" maxlength="20" readonly="true" />
		                	<@dwz.a href="/assist/mark-map?type=cinemaEdit" width="L" height="L" target="dialog" title="标注"><span class="dd-span">标注</span></@dwz.a>
		                </dd>
		            </dl>
		            <dl class="nowrap">
		                <dt>影院LOGO：</dt>
		                <dd><@ass.singleImageUpload path="cinema.logo" url="${url}" mark="logo"/></dd>
		            </dl>
		            <dl class="nowrap">
		                <dt>影院简介：</dt>
		                <dd><@s.textarea path="cinema.intro" maxlength="4000" rows="5" class="l-input" /></dd>
		            </dl>
		            <dl class="nowrap">
		                <dt>影院特色：</dt>
		                <dd><@s.textarea path="cinema.feature" maxlength="2000" rows="5" class="l-input" /></dd>
		            </dl>
		            <dl class="nowrap">
		                <dt>公交线路：</dt>
		                <dd><@s.textarea path="cinema.busLine" maxlength="800" rows="5" class="l-input" /></dd>
		            </dl>
		            <dl class="nowrap">
		                <dt>终端位置说明：</dt>
		                <dd><@s.textarea path="cinema.devicePos" maxlength="120" rows="5" class="l-input" /></dd>
		            </dl>
		            <dl class="nowrap">
		                <dt>终端位置图：</dt>
		                <dd><@ass.singleImageUpload path="cinema.deviceImg" url="${url}" mark="device"/></dd>
		            </dl>
	            </div>
	        </div>
	        <@sec.any name="CINEMA_MANAGE">
	        	<@dwz.formBar showCancelBtn=false />
	        </@sec.any>
        </@dwz.form>
    </div>
</div>