<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>

<div class="navitagorDiv">
	<nav class="navbar navbar-default navbar-fixed-top navbar-inverse">
		<img style="margin-left: 10px; margin-right: 0px" class="pull-left"
			src="img/site/NERVmall.png" height="45px">
		<div class="admin-navbar">
			<a class="navbar-brand" href="#nowhere">欢迎，${admin.name}</a> <a
				class="navbar-brand" href="admin_category_list">分类管理</a> <a
				class="navbar-brand" href="admin_user_list">用户管理</a> <a
				class="navbar-brand" href="admin_order_list">订单管理</a>
		</div>
		<div class="nav pull-right">
			<a class="navbar-brand" href="adminLogout"> <font size="-1">退出登录</font></a>
		</div>
	</nav>
</div>