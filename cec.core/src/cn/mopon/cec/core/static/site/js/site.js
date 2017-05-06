/** 校验参数格式 */
function regexParams(element) {
	return regex(
			element,
			/^\s*[^\s=]+[ ]*=[ ]*[^\s=]+\s*$|^\s*[^\s=]+[ ]*=[ ]*[^\s=]+\s*\n+(\s*[^\s=]+[ ]*=[ ]*[^\s=]+\s*\n)*(\s*[^\s=]+[ ]*=[ ]*[^\s=]+\s*)$/g);
}
/** 校验空格 */
function regexSpace(element){
	return regex(element, /^\S+$/);
}

/** 校验价格格式 */
function regexPrice(element) {
	return regex(element, /^\d+[\.]?\d{0,2}$/g);
}

/** 校验价格格式 */
function regexCanNegativePrice(element) {
	return regex(element, /^[\-]{0,1}\d+[\.]?\d{0,2}$/g);
}

/** 校验时间格式 */
function regexTime(element) {
	return regex(element, "^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])$");
}

/** 停售时间验证 */
function regexStopTime(element) {
	return regex(element,/^(-?[1-9]\d*|0)$/);
}

/** 扩展DWZ自定义验证函数，日期区间验证结束时间必须大于等于开始时间 */
function gtDate(element,geToTime) {
    var endTime = $(element);
    var startTime = $(geToTime, $(element).closest("div"));
    if(endTime.val() && startTime.val()) {
        return new Date((endTime.val()).replace(/\-/g, "\/")) >= new Date((startTime.val()).replace(/\-/g, "\/"));
    }
    return false;
}

/** 扩展DWZ自定义验证函数，日期区间验证结束时间必须大于开始时间 */
function geTime(element, geToTime) {
	if(regex(element, "^(([0-1]?[0-9])|([2][0-3])):([0-5]?[0-9])$")){
		var endTime = $(element);
		var startTime = $(geToTime, $(element).closest("tr"));
		if(endTime.val() && startTime.val()) {
			return endTime.val().parseDate("HH:mm") > startTime.val().parseDate("HH:mm");
		}
		return true;
	}
	return false;
}

/** 扩展DWZ自定义验证函数,数字只能小于等于另外一个 */
function leNumber(element, geToElement) {
	var begin = $(element).val();
	var end = $(geToElement).val();
	if (parseFloat(begin) <= parseFloat(end)) {
		return true;
	}
	return false;
}

function submitJsonQuery(btn) {
	var form = $(btn).closest("form"); 
	var pre = $(btn).closest(".page").find("div:last"); 
	var queryType = $(btn).attr("queryType");
	if(queryType == "json") {
		$.ajax({
	        type: "POST",
	        url: form.attr("action") + ".json",
	        data: form.serialize(),
	        dataType: "text",
	        success: function(data) {
                data = formatJson(data);
                console.log(data);
	        	pre.empty();
	        	pre.html("<xmp>"+data+"</xmp>");
	        }
	    });
	}
	if(queryType == "xml") {
		$.ajax({
	        type: "POST",
	        url: form.attr("action") + ".xml",
	        data: form.serialize(),
	        dataType: "text",
	        success: function(data) {
	        	data = formatXml(data);
	        	pre.empty();
	        	pre.html("<xmp>"+data+"</xmp>");
	        }
	    });
	}
}

function formatXml(text)
{
    //去掉多余的空格
    text = '\n' + text.replace(/(<\w+)(\s.*?>)/g,function($0, name, props)
    {
        return name + ' ' + props.replace(/\s+(\w+=)/g," $1");
    }).replace(/>\s*?</g,">\n<");
    
    //把注释编码
    text = text.replace(/\n/g,'\r').replace(/<!--(.+?)-->/g,function($0, text)
    {
        var ret = '<!--' + escape(text) + '-->';
        return ret;
    }).replace(/\r/g,'\n');
    
    //调整格式
    var rgx = /\n(<(([^\?]).+?)(?:\s|\s*?>|\s*?(\/)>)(?:.*?(?:(?:(\/)>)|(?:<(\/)\2>)))?)/mg;
    var nodeStack = [];
    var output = text.replace(rgx,function($0,all,name,isBegin,isCloseFull1,isCloseFull2 ,isFull1,isFull2){
        var isClosed = (isCloseFull1 == '/') || (isCloseFull2 == '/' ) || (isFull1 == '/') || (isFull2 == '/');
        var prefix = '';
        if(isBegin == '!')
        {
            prefix = getPrefix(nodeStack.length);
        }
        else 
        {
            if(isBegin != '/')
            {
                prefix = getPrefix(nodeStack.length);
                if(!isClosed)
                {
                    nodeStack.push(name);
                }
            }
            else
            {
                nodeStack.pop();
                prefix = getPrefix(nodeStack.length);
            }

        
        }
            var ret =  '\n' + prefix + all;
            return ret;
    });
    
    var outputText = output.substring(1);
    
    //把注释还原并解码，调格式
    outputText = outputText.replace(/\n/g,'\r').replace(/(\s*)<!--(.+?)-->/g,function($0, prefix,  text)
    {
        if(prefix.charAt(0) == '\r')
            prefix = prefix.substring(1);
        text = unescape(text).replace(/\r/g,'\n');
        var ret = '\n' + prefix + '<!--' + text.replace(/^\s*/mg, prefix ) + '-->';
        return ret;
    });
    
    return outputText.replace(/\s+$/g,'').replace(/\r/g,'\r\n');

}

function getPrefix(prefixIndex)
{
    var span = '    ';
    var output = [];
    for(var i = 0 ; i < prefixIndex; ++i)
    {
        output.push(span);
    }
    
    return output.join('');
}


/** 全部展开/收缩规则树 */
function expanAll(treeId,depth){
	var expan = true,tree = $("#" + treeId),flag =0;
	tree.find("tr").each(function(){
		if($(this).hasClass("collapsed") && $(this).attr("depth") ==1){
			flag ++;
			return expan = false;
		}else{
			expan = false;
		}
		if($(this).attr('depth') == depth-1 && $(this).hasClass("collapsed")){
			flag ++;
		}
	});
	tree.find("tr").each(function(){
		if(expan || flag ==0){
			if($(this).attr("depth") == depth-1){
				tree.treetable('collapseNode',$(this).attr('data-tt-id'));
			}
		}else{
			tree.treetable('expandAll');
		}
	});
}
/**策略审核、审批标记*/
function signRule(tree){
	tree.find("a[alias='signPass']").each(function(){
    	$(this).bind("click",function(){
    		var td = $(this);
			$.ajax({
				type: "POST",
	    		url: '/price/' + td.attr('name'),
	    		data : {
	    			ruleId :td.attr('id')
	    		},
	    		dataType : 'json',
	    		success : function(result) {
	    			if(result.statusCode != 200){
	    				alertMsg.error(result.message);
	    			}else{
	    				td.parent().prev().find("span").removeClass().addClass("a19");
	    			}
	    		}
	    	});
    	});
    });
    tree.find("a[alias='signRefuse']").each(function(){
    	$(this).bind("click",function(){
    		var td = $(this);
			$.ajax({
				type: "POST",
	    		url: '/price/' + td.attr('name'),
	    		data : {
	    			ruleId :td.attr('id')
	    		},
	    		dataType : 'json',
	    		success : function(result) {
	    			if(result.statusCode != 200){
	    				alertMsg.error(result.message);
	    			}else{
	    				td.parent().prev().find("span").removeClass().addClass("a20");
	    			}
	    		}
	    	});
    	});
    });
}

/**点击TR选择行*/
function selectTr(event){
	var event = event? event: window.event;
	var element = event.srcElement ? event.srcElement:event.target;
	var tr = $(element).closest("tr");
	var chk = $(tr).find("input[type='checkbox']");
	if(element.type != 'checkbox'){
		if(chk.is(":checked")){
			chk.attr("checked",false);
		}else{
			chk.attr("checked",true);
		}
	}
	var records = $(tr).closest("tbody").find("tr").length;
	var chks =  $(tr).closest("tbody").find("input[type='checkbox']:checked").length;
	if (chks >0 && records == chks){
		$(".page").find("input[type='checkbox'][group='" + chk.attr('name') +"']").attr("checked",true);
	}else{
		$(".page").find("input[type='checkbox'][group='" + chk.attr('name') +"']").attr("checked",false);
	}
}
/** 面板全部展开、收缩 */
function policyStretch(btnId) {
    var name = $("#" + btnId).attr("name");
    var isExpand = $("div[class=panelHeaderContent]>a[class='panelCollapseBtn expandable']").length;
    if(isExpand >0) {
        $("#"+ btnId).attr("name","close");
        $("div[class=panelHeaderContent]>a[class='panelCollapseBtn expandable']").trigger("click");
    } else {
        $("#"+ btnId).attr("name","open");
        $("div[class=panelHeaderContent]>a[class='panelCollapseBtn collapsable']").trigger("click");
    }
}

var formatJson = function(json, options) {
    var reg = null,
        formatted = '',
        pad = 0,
        PADDING = '    '; // one can also use '\t' or a different number of spaces

    // optional settings
    options = options || {};
    // remove newline where '{' or '[' follows ':'
    options.newlineAfterColonIfBeforeBraceOrBracket = (options.newlineAfterColonIfBeforeBraceOrBracket === true) ? true : false;
    // use a space after a colon
    options.spaceAfterColon = (options.spaceAfterColon === false) ? false : true;

    // begin formatting...
    if (typeof json !== 'string') {
        // make sure we start with the JSON as a string
        json = JSON.stringify(json);
    } else {
        // is already a string, so parse and re-stringify in order to remove extra whitespace
        json = JSON.parse(json);
        json = JSON.stringify(json);
    }

    // add newline before and after curly braces
    reg = /([\{\}])/g;
    json = json.replace(reg, '\r\n$1\r\n');

    // add newline before and after square brackets
    reg = /([\[\]])/g;
    json = json.replace(reg, '\r\n$1\r\n');

    // add newline after comma
    reg = /(\,)/g;
    json = json.replace(reg, '$1\r\n');

    // remove multiple newlines
    reg = /(\r\n\r\n)/g;
    json = json.replace(reg, '\r\n');

    // remove newlines before commas
    reg = /\r\n\,/g;
    json = json.replace(reg, ',');

    // optional formatting...
    if (!options.newlineAfterColonIfBeforeBraceOrBracket) {
        reg = /\:\r\n\{/g;
        json = json.replace(reg, ':{');
        reg = /\:\r\n\[/g;
        json = json.replace(reg, ':[');
    }
    if (options.spaceAfterColon) {
        reg = /\:/g;
        json = json.replace(reg, ': ');
    }

    $.each(json.split('\r\n'), function(index, node) {
        var i = 0,
            indent = 0,
            padding = '';

        if (node.match(/\{$/) || node.match(/\[$/)) {
            indent = 1;
        } else if (node.match(/\}/) || node.match(/\]/)) {
            if (pad !== 0) {
                pad -= 1;
            }
        } else {
            indent = 0;
        }

        for (i = 0; i < pad; i++) {
            padding += PADDING;
        }

        formatted += padding + node + '\r\n';
        pad += indent;
    });

    return formatted;
};