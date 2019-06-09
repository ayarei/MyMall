<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script>

</script>
<title>商品售罄</title>
<div class="noStockDiv">
	<div class="noStockMessage" style="font-family:arial;color:black;font-size:25px;text-align:center">
		<strong>我们遗憾地通知您，您希望购买的如下商品已经售罄</strong>
	</div>
	<br>
	<br>
    <div class="noStockListItem" style="text-align:center">
    	<table class="noStockListTable">
    		<thead style="text-align:center">
    			<tr>
    			<th width="300px" style="text-align:center">图片</th>
                <th width="350px" style="text-align:center">宝贝</th>
                <th width="220px" style="text-align:center">单价</th>
                <th width="220px" style="text-align:center">库存</th>
                <th width="220px" style="text-align:center">购买数量</th>
            </tr>
    		</thead>
    		<tbody>
    			<c:forEach items="${noStockProductList}" var="no">
    			<tr class="noStockInfoPartTR">
    				
    				<td class="noStockInfoPartTD" style="text-align:center"> <img width="40px" height="40px" src="img/productSingle/${no.noStockProduct.firstProductImage.id}.jpg"></td>
    				<td class="noStockInfoPartTD">
    				<a href="foreproduct?pid=${no.noStockProduct.id}">${no.noStockProduct.name}</a>
    				</td>	
    				<td class="noStockInfoPartTD" style="text-align:center">
    					<span class="text-muted">￥${no.noStockProduct.promotePrice}</span>
    				</td>
    				<td class="noStockInfoPartTD" style="text-align:center">
    					<span class="text-muted" >${no.noStockProduct.stock}个</span>
    				</td>
    				<td class="noStockInfoPartTD" style="text-align:center">
    					<span class="text-muted" >${no.buyNumber}个</span>
    				</td>
    			</tr>
    			</c:forEach>
    		</tbody>	
    	</table>    	
    </div>
    <br>
	<br>
    <div class="backToCart" align="center">
    	<a href="forecart"><font size="+0.5">返回我的购物车</font></a>
    </div>
</div>