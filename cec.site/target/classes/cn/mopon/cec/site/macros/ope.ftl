<#-- 影片信息 -->
<#macro film_view film=film>
	<dl>
		<dt>影片编码：</dt>
	    <dd>${film.code}</dd>
	</dl>
	<dl>
	    <dt>影片名称：</dt>
	    <dd>${film.name}</dd>
	</dl>
	<dl>
	    <dt>放映类型：</dt>
	    <dd>${film.showTypes}</dd>
	</dl>
	<dl>
	    <dt>影片类型：</dt>
	    <dd>${film.type}</dd>
	</dl>
	 <dl>
	    <dt>影片语言：</dt>
	    <dd>${film.language}</dd>
	</dl>
	<dl>
	    <dt>公映日期：</dt>
	    <dd>${film.publishDate?date}</dd>
	</dl>
	<dl>
	    <dt>时长(分钟)：</dt>
	    <dd>${film.duration}</dd>
	</dl>
	 <dl>
	    <dt>导演：</dt>
	    <dd>${film.director}</dd>
	</dl>
	<dl>
	    <dt>国家：</dt>
	    <dd>${film.country}</dd>
	</dl>
	<dl class="nowrap">
	    <dt>发行商：</dt>
	    <dd>${film.publisher}</dd>
	</dl>
	 <dl class="nowrap">
	    <dt>演员：</dt>
	    <dd>${film.cast}</dd>
	</dl>
	<dl class="nowrap">
	    <dt>简介：</dt>
	    <dd>${film.intro}</dd>
	</dl>
	<dl class="nowrap">
	    <dt>精彩看点：</dt>
	    <dd>${film.highlight}</dd>
	</dl>
	<dl class="nowrap">
   		<dt>海报：</dt>
      	<dd><#if film.poster?if_exists><#list film.poster?split(",") as url><img src="${url}_205x150.jpg" width="205" height="150/"></#list></#if></dd>
   	</dl>
   	<dl class="nowrap">
   		<dt>剧照：</dt>
      	<dd><#if film.stills?if_exists><#list film.stills?split(",") as url><img src="${url}_205x150.jpg" width="205" height="150/"></#list></#if></dd>
   	</dl>
</#macro>

<#-- 新增接入类型-->
<#macro accessType_operate path>
	<dl>
       <dt>接入类型名称：</dt>
       <dd><@s.input path="${path}.name" class="required" maxlength="20" /></dd>
   </dl>
   <dl>
       <dt>接入模式：</dt>
       <dd><@s.radios path="${path}.model" items=AccessModel?values  itemValue="value" itemLabel="text" class="required" prefix="<label class='dd-span'>" suffix="</label>" /></dd>
   </dl>
   <dl>
       <dt>接入商类型：</dt>
       <dd><@s.select path="${path}.provider" items=Provider?values itemValue="value" itemLabel="text" /></dd>
   </dl>
   <#nested>
   <div id="param">
	   <dl class="nowrap">
	        <dt>接入地址：</dt>
	        <dd><@s.input path="${path}.url" class="required url l-input" maxlength="120" /></dd>
	    </dl>
	    <dl>
	        <dt>用户名：</dt>
	        <dd><@s.input path="${path}.username" maxlength="20" class="required"/></dd>
	    </dl>
	    <dl>
	        <dt>密码：</dt>
	        <dd><@s.input path="${path}.password" maxlength="60" class="required"/></dd>
	    </dl>
   </div>
   <dl>
        <dt>请求超时(秒)：</dt>
        <dd><@s.input path="${path}.connectTimeout" maxlength="6" class="required digits"/></dd>
    </dl>
   <dl>
        <dt>响应超时(秒)：</dt>
        <dd><@s.input path="${path}.socketTimeout" maxlength="6" class="required digits"/></dd>
    </dl>
    <dl class="nowrap">
        <dt>参数配置：</dt>
        <dd><@s.textarea path="${path}.params" rows="5" maxlength="120" class="l-input" customvalid="regexParams(element)" title="参数=参数值形式，参数不能相同，多个请回车" ></@s.textarea></dd>
    </dl>
</#macro>