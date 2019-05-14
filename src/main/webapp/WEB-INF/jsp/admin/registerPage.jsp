<!DOCTYPE html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<html>
<head>
<title>管理员注册</title>
<script src="js/jquery/2.0.0/jquery.min.js"></script>
<link href="css/bootstrap/3.3.6/bootstrap.min.css" rel="stylesheet">
<script src="js/bootstrap/3.3.6/bootstrap.min.js"></script>
<link href="css/back/style.css" rel="stylesheet">
<link rel="icon" type="image/x-icon" href="img/site/admin.ico">

<script>
$(function(){		
    <c:if test="${!empty msg}">
    $("span.errorMessage").html("${msg}");
    $("div.registerErrorMessageDiv").css("visibility","visible");      
    </c:if> 
    
    var msgLength=$("span.errorMessage").html().length;
    if(msgLength==0 && "${empty msg}") 
    	$("div.registerErrorMessageDiv").css("visibility","hidden"); 
     
    $(".registerForm").submit(function(){
    	if(0==$("#special").val().length){
            $("span.errorMessage").html("请输入注册码");
            $("div.registerErrorMessageDiv").css("visibility","visible");          
            return false;
        }  
        if(0==$("#name").val().length){
            $("span.errorMessage").html("请输入登录名");
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
    
    $("form.registerForm input").keyup(function(){
        $("div.registerErrorMessageDiv").css("visibility","hidden");
    });
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
	<form method="post" action="adminRegister" class="registerForm">
		<div class="registerDiv" style="text-align: center">
			<div class="registerErrorMessageDiv">
				<div class="alert alert-danger" role="alert">
					<button type="button" class="close" data-dismiss="alert"
						aria-label="Close"></button>
					<span class="errorMessage"></span>
				</div>
			</div>
			<div class="mainRegisterDiv" align="center">
				<table class="registerTable">
					<tr>
						<td class="registerTip registerTableLeftTD"><font size="+2"><strong>管理员注册</strong></font></td>
					</tr>
					<tr height="10" />
					<tr>
						<td class="registerTableLeftTD"><strong>管理员注册码</strong></td>
						<td class="registerTableRightTD"><input id="special"
							name="special" class="form-control" placeholder="请输入注册码"></td>
					</tr>
					<tr height="5" />
					<tr>
						<td class="registerTableLeftTD"><strong>登录名</strong></td>
						<td class="registerTableRightTD"><input id="name" name="name"
							class="form-control" placeholder="设置成功后无法修改"></td>
					</tr>
					<tr height="5" />
					<tr>
						<td class="registerTableLeftTD"><strong>登录密码</strong></td>
						<td class="registerTableRightTD"><input id="password"
							class="form-control" name="password" type="password"
							placeholder="设置你的登录密码"></td>
					</tr>
					<tr height="5" />
					<tr>
						<td class="registerTableLeftTD"><strong>密码确认</strong></td>
						<td class="registerTableRightTD"><input id="repeatpassword"
							class="form-control" type="password" placeholder="请再次输入你的密码"></td>
					</tr>
					<tr height="5" />
					<tr>
						<td colspan="2" class="registerButtonTD" align="center">
							<button type="submit" class="btn btn-primary">注 册</button>
						</td>
					</tr>
				</table>
			</div>

		</div>
	</form>
</body>
</html>

