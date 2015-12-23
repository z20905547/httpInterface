<%@ page language="java" session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

    <title>basic curd admin : 404</title>
    <meta http-equiv="Expires" content="0">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-store">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" type="text/css" href="http://cdn.staticfile.org/twitter-bootstrap/3.2.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/style.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/bootstrap-theme.css">

    <!--[if lt IE 9]>
    <script src="http://cdn.bootcss.com/html5shiv/3.7.0/html5shiv.min.js"></script>
    <script src="http://cdn.bootcss.com/respond.js/1.3.0/respond.min.js"></script>
    <![endif]-->

</head>
<body>
	<div class="main-container container">
	
		<div class="panel panel-default">
							
			<div class="panel-heading">
				<h3 class="panel-title"><span class="glyphicon glyphicon-trash"></span> 404</h3>
			</div>
			
			<div class="panel-body">
		        <div class="server-exception"> 
		    		<p><center><h1><span class="glyphicon glyphicon-trash"></span></h1></center></p></center>
		    		<h3>找不到页面</h3>
		    		<hr>
			   		<p>你访问的页面不存在或已经被删除.</p> 
			   		<p>
			   			<a class="btn btn-default" href="javascript:history.back()">
							<span class="glyphicon glyphicon-backward"></span> 返回
						</a>
			   		</p>
		    	</div>
    		</div>
    		
    	</div>
    	
	</div>
</body>
</html>