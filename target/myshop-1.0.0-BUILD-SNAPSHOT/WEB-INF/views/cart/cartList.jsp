<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<c:set var="path1" value="{pageContext.request.contextPath }" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 목록</title>
<%@ include file="../inc/head.jsp" %>
<link rel="stylesheet" href="../inc/datatables.min.css" />
<script src="../inc/datatables.min.js"></script>
</head>
<body>
<%@ include file="../inc/header.jsp" %>
<div id="contents" class="contents">
	<div class="container px-4 px-lg-5 mt-5">
		<div class="row">
			<br><br>		
			<h2 class="tit">장바구니 목록</h2>
			<br><hr>
			<div class="panel-body">
				<table class="table" id="myTable">
					<thead>
						<tr>
							<th>번호</th>
							<th>상품명</th>
							<th>상품개수</th>
						</tr>
					</thead>
					<tbody>
					<c:choose>
						<c:when test="${cnt gt 0 }">	
							<c:forEach items="${cartList}" var="cart" varStatus="status">
							<tr>
								<td>${status.count }</td>
								<td><a href="../goods/getCartGoods.do?gnum=${cart.gno}&bno=${cart.bno}">${cart.gno }</a></td>
								<td>${cart.amount }</td>
							</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="3">장바구니에 담은 상품이 없습니다.</td>
							</tr>
						</c:otherwise>
					</c:choose>
					</tbody>
				</table>
			</div>
			<script>
			$(function(){
				$("#myTable").DataTable();
			});
			</script>
		</div>
	</div>
	<div class="blank" style="min-height:350px"></div>		
</div>
<%@ include file="../inc/footer.jsp" %>
</body>
</html>