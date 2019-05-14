<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>

<head>
<title>管理员登录</title>
<script src="js/jquery/2.0.0/jquery.min.js"></script>
<link href="css/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
<script src="js/bootstrap/3.3.6/bootstrap.min.js"></script>
<link href="css/back/style.css" rel="stylesheet">
<link rel="icon" type="image/x-icon" href="img/site/admin.ico">

<script>
$(function(){
    <c:if test="${!empty msg}">
    $("span.errorMessage").html("${msg}");
    $("div.loginErrorMessageDiv").css("visibility","visible");
    </c:if>
    
    var msgLength=$("span.errorMessage").html().length;
    if(msgLength==0 && "${empty msg}") 
    	$("div.loginErrorMessageDiv").css("visibility","hidden"); 
    
    $("form.loginForm").submit(function(){
        if(0==$("#name").val().length||0==$("#password").val().length){
            $("span.errorMessage").html("请输入账号和密码");
            $("div.loginErrorMessageDiv").css("visibility","visible");
            return false;
        }
        return true;
    });

    $("form.loginForm input").keyup(function(){
        $("div.loginErrorMessageDiv").css("visibility","hidden");
    });

    var left = window.innerWidth/2+162;
    $("div.loginSmallDiv").css("left",left);
})
</script>

<style>
#background {
	width: 100%;
	height: 100%;
	position: fixed;
	z-index: -1;
	top: 0;
    left: 0;
}
</style>
</head>
<body>
	<img id="background" src="img/site/adminBackGround.png">
	<form method="post" action="adminLogin" class="loginForm">
		<div class="loginDiv" style="text-align: center">
			<div class="loginErrorMessageDiv">
				<div class="alert alert-danger" role="alert">
					<button type="button" class="close" data-dismiss="alert"
						aria-label="Close"></button>
					<span class="errorMessage"></span>
				</div>
			</div>
			<div class="loginTableDiv" align="center">
				<table class="loginTable">
					<tr>
						<td class="loginTip loginTableLeftTD"><font size="+2"><strong>管理员登录</strong></font></td>
					</tr>
					<tr height="10" />
					<tr>
						<td class="loginTableLeftTD"><strong>登录名</strong></td>
						<td class="loginTableRightTD"><input id="name" name="name"
							class="form-control" placeholder="你的登录名"></td>
					</tr>
					<tr height="5" />
					<tr>
						<td class="loginTableLeftTD"><strong>登录密码</strong></td>
						<td class="loginTableRightTD"><input id="password"
							class="form-control" name="password" type="password"
							placeholder="你的登录密码"></td>
					</tr>
					<tr height="15" />
					<tr>

						<td colspan="2" class="loginButtonTD" align="center">
							<button type="submit" class="btn btn-primary">登 录</button>
						</td>
						<td><a href="adminRegisterPage" class="pull-right">没有账号?注册一个吧</a>
						</td>
					</tr>

				</table>
			</div>

		</div>
	</form>
</body>
</html>


















