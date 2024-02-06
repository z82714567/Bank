<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- header -->
<%@ include file="/WEB-INF/view/layout/header.jsp"%>

<div class="col-sm-8">
	<h2>로그인</h2>
	<h5>어서오세요 환영 합니다.</h5>
	<form action="/user/sign-in" method="post"> <!-- 자원의 요청이지만 로그인만 예외적으로 post로 요청 --> <!-- 각 데이터(내가 적는 값)를 name(username)으로(dto와 비교) 바인딩하고 액션주소로 post방식으로 요청함-->
		<div class="form-group">
			<label for="username">username: </label> <input type="text"
				name="username" class="form-control" placeholder="Enter username"
				id="username" value="수현">
		</div>
		<div class="form-group">
			<label for="pwd">password: </label> <input type="password"
				name="password" class="form-control" placeholder="Enter password"
				id="pwd" value="1234">
		</div>
		<button type="submit" class="btn btn-primary">로그인</button>
		<a href="https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=e8462fc1117472d26560e8166e67e3e9&redirect_uri=http://localhost/user/kakao-callback">
			<img alt ="" src="/images/kakao_login_small.png" width="75" height="40">
		</a>
	</form>
</div>
</div>
</div>


<!-- footer -->
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>
