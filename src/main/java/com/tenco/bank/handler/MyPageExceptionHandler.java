package com.tenco.bank.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import com.tenco.bank.handler.exception.CustomPageException;

/**
 * View 렌더링을 위해
 * ModelView 객체를 반환하도록 설정 되어 있다.
 * 예외처리 페이지를 리턴할 때 사용함.
 * 
 * */


@ControllerAdvice //전역 예외 처리를 담당
public class MyPageExceptionHandler {

	//CustomPageException이 발생 하면 처리하는 역할(예외 발생 시 예외 처리 페이지를 보여주는 기능을 수행)
	@ExceptionHandler(CustomPageException.class)
	public ModelAndView handlerRuntimeException(CustomPageException e) {
		
		System.out.println("에러 확인");
		
		//handlerRuntimeException 메서드는 "에러 확인"이라는 메시지를 출력하고, 
		//ModelAndView 객체를 생성하여 "errorPage"라는 뷰를 설정합니다. 
		//이 뷰는 예외 처리 페이지를 나타내며, 예외 처리 과정에서 사용될 데이터를 설정합니다.
		ModelAndView modelAndView = new ModelAndView("errorPage");
		//modelAndView.addObject() 메서드를 사용하여 "statusCode"와 "message"라는 속성을 추가하고,
		//각각 HttpStatus.NOT_FOUND.value()와 e.getMessage() 값을 설정합니다. 
		//이렇게 설정된 데이터는 예외 처리 페이지에서 사용될 수 있습니다.
		modelAndView.addObject("statusCode", HttpStatus.NOT_FOUND.value());
		modelAndView.addObject("message", e.getMessage());
		
		
		return modelAndView; //페이지 반환 + 데이터 내려 줌
	}
	
}
