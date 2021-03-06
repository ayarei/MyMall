<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<script>
	$(function() {
		$("ul.pagination li.disabled a").click(function() {
			return false;
		});
	});
</script>


<nav>
	<ul class="pagination">
		<li <c:if test="${!page.hasPrevious}">class="disabled"</c:if>><a
			href="?${page.param}start=0" aria-label="Previous"> <span
				aria-hidden="true">&laquo;</span>
		</a></li>

		<li <c:if test="${!page.hasPrevious}">class="disabled"</c:if>><a
			href="?${page.param}start=${page.start-page.count}"
			aria-label="Previous"> <span aria-hidden="true">&lsaquo;</span>
		</a></li>
		
		<!-- status.index: 从0开始的计数值
		 	 status.conunt: 从1开始的计数值
	 	-->
		<c:forEach begin="0" end="${page.totalPage-1}" varStatus="status">
			<c:if
				test="${status.count*page.count-page.start<=40 && status.count*page.count-page.start>=-30}">
				<li
				<c:if test="${status.index*page.count==page.start}">class="disabled"</c:if>>
				<a href="?${page.param}start=${status.index*page.count}"
				<c:if test="${status.index*page.count==page.start}">class="current"</c:if>>${status.count}</a>
			</li>
			</c:if>
		</c:forEach>

		<li <c:if test="${!page.hasNext}">class="disabled"</c:if>><a
			href="?${page.param}start=${page.start+page.count}" aria-label="Next">
				<span aria-hidden="true">&rsaquo;</span>
		</a></li>
		<li <c:if test="${!page.hasNext}">class="disabled"</c:if>><a
			href="?${page.param}start=${page.last}" aria-label="Next"> <span
				aria-hidden="true">&raquo;</span>
		</a></li>
	</ul>
</nav>
