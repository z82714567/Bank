<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- header -->
<%@ include file="/WEB-INF/view/layout/header.jsp"%>

<div class="col-sm-8">
	<h2>나의 계좌 목록</h2>
	<h5>어서오세요 환영 합니다.</h5>
	<!-- 만약 accountList null 이거나 not null 이면 -->
	<div class="bg-light">
	<c:choose>
		<c:when test="${accountList != null}">
				<table class="table">
					<thead>
						<tr>
							<th>계좌 번호</th>
							<th>잔액</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach var="account" items="${accountList}">
							<tr>
								<td><a href="/account/detail/${account.id}">${account.number}</a></td>
								<td>${account.formatBalance()}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
		</c:when>
		<c:otherwise>
			<p>아직 생성된 계좌가 없습니다.</p>
		</c:otherwise>
	</c:choose>
	</div>	
</div>
</div>
</div>




<!-- footer -->
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>
