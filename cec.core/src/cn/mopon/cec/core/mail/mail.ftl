<#-- 发送邮件模板 -->
<#macro mail_send>
<!DOCTYPE HTML>
<html>
	<head>
		<meta http-equiv="content-type" content="text/html;charset=UTF-8">
		<style type="text/css">
			* {	font-size: 12px; font-family: "Microsoft YaHei", sans-serif !important; }
			body { padding: 10px;}
			.title { font-size: 24px; font-weight: bold; text-align: center; margin-bottom: 10px; padding-bottom: 10px; border-bottom: 1px solid #aaa}
			.header {}
			.content {text-indent: 28px;}
			.footer {margin-top: 10px; padding-top: 10px; border-top: 1px solid #aaa}
			strong {padding: 0px 5px; color: blue}
			table strong {padding: 0px;}
			table { margin-left: 30px; border-collapse: collapse; border: 1px solid #aaa;}
			th {vertical-align: baseline; padding: 5px 15px 5px 6px; background-color: #d5d5d5; border: 1px solid #aaa;}
			td {vertical-align: text-top; padding: 6px 15px 6px 6px; background-color: #efefef; border: 1px solid #aaa;}
			table.grid {}
			table.list th {width: 120px; text-align: left;}
			table.list td {width: 480px}
			pre {margin-left: 30px; border: 1px solid #aaa;}
		</style>
		<title>${appName}-${subject}</title>
	</head>
	<body>
		<p class="title">${subject}</p>
		<p class="header">${appName}提醒您：</p>
		<#nested>
		<p class="footer">
			${appName}<br/>
			${sendTime?datetime}
		</p>
	</body>
</html>
</#macro>