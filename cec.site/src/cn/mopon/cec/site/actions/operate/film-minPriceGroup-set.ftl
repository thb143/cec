<div class="page">
    <div class="pageContent">
        <@dwz.form action="/operate/minPriceGroup-save">
            <@s.hidden path="minPriceGroup.film.id" value="${film.id}"/>
            <div>
				<div class="page">
					<div class="pageContent">
						<div class="pageFormContent" layoutH="60" >
							<dl class="nowrap">
					        	<dt>分类名称：</dt>
								<dd><@s.input path="minPriceGroup.name" class="required" maxlength="20"/></dd>
							</dl>
							<dl class="nowrap">
					        	<dt>选择城市：</dt>
								<dd>
									<@dwz.a class="button" href="/operate/city-select" rel="city-select" target="dialog"><span>选择城市</span></@dwz.a>
								</dd>
							</dl>
							<dl class="nowrap" display="none">
					        	<dt>&nbsp;</dt>
								<dd class="checkedCities">不选择城市，则代表该影片其他分类所包含的城市之外的所有城市及地区。</dd>
							</dl>
							<input type="hidden" id="cityValues" name="cityCode"/>
							<dl class="nowrap">
					        	<dt>时间规则：</dt>
							    <dd>
							        <table class="list itemDetail" addButton="添加时间" width="100%">
							            <thead>
							            <tr>
							                <th type="date" align="center" size="13" format="yyyy-MM-dd" name="minPrices[#index#].startDate"
							                    fieldClass="required hideError" fieldAttrs={id:"startDate#index#",readonly:"true"} style="width:98px">开始日期
							                </th>
							                <th type="date" align="center" size="13" format="yyyy-MM-dd" name="minPrices[#index#].endDate"
							                    fieldClass="required hideError" style="width:98px"
							                    fieldAttrs={minRelation:"#startDate#index#",customvalid:"geDate(element,'#startDate#index#')",title:"结束日期不能小于开始日期。",readonly:"true"}>结束日期
							                </th>
							                <th type="text" size="5" align="center" fieldClass="required hideError" style="width:39px" name="minPrices[#index#].normal2d">2D</th>
							                <th type="text" size="5" align="center" fieldClass="required hideError" style="width:39px" name="minPrices[#index#].normal3d">3D</th>
							                <th type="text" size="5" align="center" fieldClass="required hideError" style="width:39px" name="minPrices[#index#].max2d">MAX2D</th>
							                <th type="text" size="5" align="center" fieldClass="required hideError" style="width:39px" name="minPrices[#index#].max3d">MAX3D</th>
							                <th type="del" width="60" align="center" style="width:77px">操作</th>
							            </tr>
							            </thead>
							            <tbody>
							            </tbody>
							        </table>
							    </dd>
							</dl>
						</div>
						<@dwz.formBar />
					</div>
				</div>
			</div>
        </@dwz.form>
    </div>
</div>
<script>
</script>