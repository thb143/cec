<div class="page">
    <div class="pageContent">
        <@s.form action="/assist/district-test" onsubmit="return navTabSearch(this);">
        <div class="pageFormContent" layoutH="60">
            <div>
                <p>行政区划：</p>
                <p><@ass.countyCombox path="cinema.county" /></p>
            </div>
        </div>
        <div class="formBar">
            <ul>
                <li>
                    <div class="buttonActive">
                        <div class="buttonContent">
                            <button type="submit">保存</button>
                        </div>
                    </div>
                </li>
                <li>
                    <div class="button">
                        <div class="buttonContent">
                            <button type="button" class="close">取消</button>
                        </div>
                    </div>
                </li>
            </ul>
        </div>
        </@s.form>
    </div>
</div>