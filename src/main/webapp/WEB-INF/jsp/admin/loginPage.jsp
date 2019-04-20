<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<head>
<script src="js/jquery/2.0.0/jquery.min.js"></script>
<link href="css/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
<script src="js/bootstrap/3.3.6/bootstrap.min.js"></script>
<link href="css/back/style.css" rel="stylesheet">

<script>
$(function(){
	 
    <c:if test="${!empty msg}">
    $("span.errorMessage").html("${msg}");
    $("div.loginErrorMessageDiv").show();
    </c:if>

    $("form.loginForm").submit(function(){
        if(0==$("#name").val().length||0==$("#password").val().length){
            $("span.errorMessage").html("请输入账号密码");
            $("div.loginErrorMessageDiv").show();
            return false;
        }
        return true;
    });

    $("form.loginForm input").keyup(function(){
        $("div.loginErrorMessageDiv").hide();
    });

    var left = window.innerWidth/2+162;
    $("div.loginSmallDiv").css("left",left);
})
</script>
</head>

<form method="post" action="adminLogin" class="registerForm">

	<div class="registerDiv">
		<div class="registerErrorMessageDiv">
			<div class="alert alert-danger" role="alert">
				<button type="button" class="close" data-dismiss="alert"
					aria-label="Close"></button>
				<span class="errorMessage"></span>
			</div>
		</div>

		<table class="registerTable" align="center">
			<tr>
				<td class="registerTip registerTableLeftTD"><font size="+2">管理员登陆</font></td>
			</tr>
			<tr>
				<td class="registerTableLeftTD">登陆名</td>
				<td class="registerTableRightTD"><input id="name" name="name"
					class="form-control" placeholder="你的登录名"></td>
			</tr>
			<tr height="5"/>
			<tr>
				<td class="registerTableLeftTD">登陆密码</td>
				<td class="registerTableRightTD"><input id="password"
					class="form-control" name="password" type="password"
					placeholder="你的登陆密码"></td>
			</tr>
			<tr height="15"/>
			<tr>
				
				<td colspan="2" class="registerButtonTD" align="center">
					<button type="submit"
							class="btn btn-primary">提 交</button></td>
				<td>
					<a href="adminRegisterPage" class="pull-right">没有账号?注册一个吧</a>
				</td>
			</tr>
			
		</table>
	</div>
</form>
















