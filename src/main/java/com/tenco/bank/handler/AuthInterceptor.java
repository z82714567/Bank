package com.tenco.bank.handler;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.tenco.bank.handler.exception.UnAuthorizedException;
import com.tenco.bank.repository.entity.User;
import com.tenco.bank.utils.Define;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// 1.  HandlerInterceptor 구현 하기
// HandlerInterceptor --> new 아니고 컴포넌트 (하나만 Ioc대상할 때)(빈에 올릴 땐 컨피규레이션)
@Component //Ioc 대상
public class AuthInterceptor implements HandlerInterceptor {

	// preHandle - 컨트롤러 들어오기 전에 동작(true --> 컨트롤러 안으로 들어감, false --> 안 들어감)
	@Override // 사용 
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("인터셉터 동작 확인111111111111111");
		
		// 인증 검사
		HttpSession session = request.getSession(); // 세션 꺼내기
		User prinsipal = (User) session.getAttribute(Define.PRINCIPAL); // 세션에서 정보 꺼내기 // 다운캐스팅
		if(prinsipal == null) {
			//response.sendRedirect("/user/sign-in"); // 없으면 로그인 페이지로 이동
			//예외 처리
			throw new UnAuthorizedException("로그인 먼저 해주세요", HttpStatus.UNAUTHORIZED);
		}
		return true;
	}
	
	// postHandle - 컨들롤러 들어 갔다가 뷰(jsp)가 랜더링 되기 전에 호출됨, 요청 처리가 완료된 후 or 뷰가 랜더링 완료 후 동작
	@Override // 사용x
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override //사용x
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
	}
}
