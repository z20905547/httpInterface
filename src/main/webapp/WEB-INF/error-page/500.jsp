<%@ page language="java" session="false" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>

    <title>basic curd admin : 500</title>
    <meta http-equiv="Expires" content="0">
    <meta http-equiv="Pragma" content="no-cache">
    <meta http-equiv="Cache-Control" content="no-store">
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <link rel="stylesheet" type="text/css" href="http://cdn.staticfile.org/twitter-bootstrap/3.2.0/css/bootstrap.min.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/style.css">
    <link rel="stylesheet" type="text/css" href="<%= request.getContextPath() %>/resource/css/bootstrap-theme.css">

</head>
<body>
	<div class="main-container container">
	
		<div class="panel panel-default">
							
			<div class="panel-heading">
				<h3 class="panel-title"><span class="glyphicon glyphicon-remove-circle"></span> 500</h3>
			</div>
			
			<div class="panel-body">
		        <div class="server-exception"> 
		    		<p><center><h1><span class="glyphicon glyphicon-warning-sign"></span></h1></p></center>
		    		<h3>操作失败</h3>
		    		<hr>
			   		<p>服务器忙碌，请稍后再试。</p> 
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