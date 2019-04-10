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
    $("div.registerErrorMessageDiv").css("visibility","visible");      
    </c:if>
     
    $(".registerForm").submit(function(){
        if(0==$("#name").val().length){
            $("span.errorMessage").html("请输入用户名");
            $("div.registerErrorMessageDiv").css("visibility","visible");          
            return false;
        }      
        if(0==$("#password").val().length){
            $("span.errorMessage").html("请输入密码");
            $("div.registerErrorMessageDiv").css("visibility","visible");          
            return false;
        }      
        if(0==$("#repeatpassword").val().length){
            $("span.errorMessage").html("请输入重复密码");
            $("div.registerErrorMessageDiv").css("visibility","visible");          
            return false;
        }      
        if($("#password").val() !=$("#repeatpassword").val()){
            $("span.errorMessage").html("重复密码不一致");
            $("div.registerErrorMessageDiv").css("visibility","visible");          
            return false;
        }      
 
        return true;
    });
})
</script>
</head>

<form method="post" action="adminregister" class="registerForm">

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
				<td class="registerTip registerTableLeftTD">设置管理员名</td>
				<td></td>
			</tr>
			<tr>
				<td class="registerTableLeftTD">管理员注册码</td>
				<td class="registerTableRightTD"><input id="special" name="special"
					class="form-control" placeholder="请输入注册码"></td>
			</tr>
			<tr>
				<td class="registerTableLeftTD">登陆名</td>
				<td class="registerTableRightTD"><input id="name" name="name"
					class="form-control" placeholder="登录名一旦设置成功，无法修改"></td>
			</tr>
			<tr>
				<td class="registerTableLeftTD">登陆密码</td>
				<td class="registerTableRightTD"><input id="password"
					class="form-control" name="password" type="password"
					placeholder="设置你的登陆密码"></td>
			</tr>
			<tr>
				<td class="registerTableLeftTD">密码确认</td>
				<td class="registerTableRightTD"><input id="repeatpassword"
					class="form-control" type="password" placeholder="请再次输入你的密码"></td>
			</tr>

			<tr>
				<td colspan="2" class="registerButtonTD"><a
					href="registerSuccess.jsp"><button type="submit"
							class="btn btn-primary">提 交</button></a></td>
			</tr>
		</table>
	</div>
</form>