package com.tenco.bank.handler.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class CustomPageException extends RuntimeException{
	
	//상태코드 변수 선언
	private HttpStatus httpStatus;
	
	//생성자 정의
	public CustomPageException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}
	
	//예를들어 사용하는 시점에 활용 가능 
	//new CustomPageException("바보야", HttpStatus.ok);

}
