package com.tenco.bank.handler;

import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.handler.exception.UnAuthorizedException;

@Order(1) //빈(Bean)의 우선순위를 지정하는 어노테이션
//스프링은 여러 개의 빈이 동일한 타입으로 등록되어 있을 때, 
//어떤 빈을 우선적으로 선택해야 하는지 결정해야 합니다. 
//이때 @Order 어노테이션을 사용하여 각 빈에 우선순위를 부여할 수 있습니다.
@RestControllerAdvice //RESTful API 예외 처리를 담당 
public class MyRestfulExceptionHandler {

	//모든 예외 클래스 설정
	@ExceptionHandler(Exception.class)
	public void exception(Exception e) {
		System.out.println("----------------");
		System.out.println(e.getClass().getName());
		System.out.println(e.getMessage());
		System.out.println("----------------");
	}
	
	@ExceptionHandler(CustomRestfulException.class)
	public String basicException(CustomRestfulException e) {
		StringBuffer sb = new StringBuffer();
		sb.append("<script>");
		sb.append("alert('"+ e.getMessage() +"');");
		sb.append("</script>");
		return sb.toString();
	}
	
	@ExceptionHandler(UnAuthorizedException.class)
	public String unAuthrizedException(UnAuthorizedException e) {
		StringBuffer sb = new StringBuffer();
		sb.append("<script>");
		sb.append("alert('" + e.getMessage() + "');");
		sb.append("location.href='/user/sign-in';");  // 기본 세션 유지 기간이 30분 이라 유저가 아무것도 안하고 30분 지나서 하면 계속 로딩이 돌 수 있어서 location을 사용
		sb.append("</script>");
		return sb.toString();
	}
	
}
