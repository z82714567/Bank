<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!-- header -->
<%@ include file="/WEB-INF/view/layout/header.jsp"%>

<div class="col-sm-8">
	<h2>회원 가입</h2>
	<h5>어서오세요 환영 합니다.</h5><!--파일업로드 시 반드시 enctype="multipart/form-data" -->
	<form action="/user/sign-up" method="post" enctype="multipart/form-data">
		<!-- 각 데이터(내가 적는 값)를 name(username)으로(dto와 비교) 바인딩하고 액션주소로 post방식으로 요청함-->
		<div class="form-group">
			<label for="username">username: </label> <input type="text"
				name="username" class="form-control" placeholder="Enter username"
				id="username">
		</div>
		<div class="form-group">
			<label for="pwd">password: </label> <input type="password"
				name="password" class="form-control" placeholder="Enter password"
				id="pwd">
		</div>
		<div class="form-group">
			<label for="fullname">fullname: </label> <input type="text"
				name="fullname" class="form-control" placeholder="Enter fullname"
				id="fullname">
		</div>
		<!-- 2/5 파일업로드   -->
		<div class="custom-file"><!-- name속성 : SignUpFormDto 이름과 동일 해야함  -->
			<input type="file" class="custom-file-input" id="customFile" name="customFile">
			<label class="custom-file-label" for="customFile">Choose file</label>
		</div>
		<br>
		<br>
		<br>
		<!-- 이벤트 전파 속성 - 버블링이란? 캡처링이란? -->
		<button type="submit" class="btn btn-primary">회원 가입</button>
	</form>
</div>
</br>
</div>


<script>
// Add the following code if you want the name of the file appear on select
$(".custom-file-input").on("change", function() {
  var fileName = $(this).val().split("\\").pop();
  $(this).siblings(".custom-file-label").addClass("selected").html(fileName);
});
</script>

<!-- footer -->
<%@ include file="/WEB-INF/view/layout/footer.jsp"%>
