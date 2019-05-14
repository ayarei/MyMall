<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" import="java.util.*"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@include file="../include/admin/adminHeader.jsp"%>
<%@include file="../include/admin/adminNavigator.jsp"%>

<c:if test="${delete_check == 'property_delete' }">
	<title>分类管理</title>
	<div class="workingArea">

		<ol class="breadcrumb">
			<li><a href="admin_category_list">所有分类</a></li>
			<li><a href="admin_property_list?cid=${category.id}">${category.name}</a></li>
			<li class="active">属性${delete_instance.name}删除错误</li>
		</ol>
		<div class="warning" align="center">
			<font size="+4"><span class="text-danger">警告</span></font>
		</div>
		<br>
		<div align="center">
			<font size="+2"><strong>你刚才执行了一个删除属性操作，删除的属性是分类</strong></font>
			<font color = "#483D8B" size="+2"><strong>“${category.name}”</strong></font>
			<font size="+2"><strong>下的</strong><strong><font color = "#FF69B4" size="+3">“${delete_instance.name}”</font></strong></font><br><br> 
			<font size="+2"><strong>然而分类</strong></font>
			<font color = "#483D8B" size="+2"><strong>${category.name}</strong></font>
			<font size="+2"><strong>下还有其他产品，不可直接此属性删除。请删除分类</strong></font>
			<font color = "#483D8B" size="+2"><strong>${category.name}</strong></font>
			<font size="+2"><strong>下所有</strong></font>
			<font color="red" size="+4"><strong>产品</strong></font> 
			<font size="+2"><strong>后再删除此属性！</strong></font>
			<br> 
			<br> 
			<a href="admin_product_list?cid=${delete_instance.cid}">
				<button type="button" class="btn btn-info">查看此属性对应分类下产品信息</button>
			</a> 
			<a href="admin_property_list?cid=${delete_instance.cid}">
				<button type="button" class="btn btn-info">查看此分类下属性信息</button>
			</a> 
			<br> 
			<br> 
			<a href="admin_category_list">
				<button type="button" class="btn btn-primary">返回分类列表</button>
			</a>
		</div>
	</div>
</c:if>

<c:if test="${delete_check == 'category_delete' }">
	<title>分类管理</title>
	<div class="workingArea">

		<ol class="breadcrumb">
			<li><a href="admin_category_list">所有分类</a></li>
			<li class="active">分类${delete_instance.name}删除错误</li>
		</ol>
		<div class="warning" align="center">
			<font size="+4"><span class="text-danger">警告</span></font>
		</div>
		<br>
		<div align="center">
			<font size="+2"><strong>你刚才执行了一个删除分类操作，删除的分类是<font color = "#FF69B4" size="+3">“${delete_instance.name}”</font></strong></font><br />
			<br /> <font size="+2"><strong>然而此分类下还有其他实例，不可直接删除。请删除此分类下所有</strong></font>
			<font color="red" size="+4"><strong>产品及属性</strong></font> <font
				size="+2"><strong>后再删除此分类！</strong></font><br> <br> <a
				href="admin_product_list?cid=${delete_instance.id}">
				<button type="button" class="btn btn-info">查看此分类下产品信息</button>
			</a> <a href="admin_property_list?cid=${delete_instance.id}">
				<button type="button" class="btn btn-info">查看此分类下属性信息</button>
			</a> <br> <br> <a href="admin_category_list">
				<button type="button" class="btn btn-primary">返回分类列表</button>
			</a>
		</div>
	</div>
</c:if>












