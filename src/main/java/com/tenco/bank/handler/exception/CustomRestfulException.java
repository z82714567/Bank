package com.tenco.bank.handler.exception;

import org.springframework.http.HttpStatus;

//이 클래스는 RESTful API에서 발생하는 예외를 표현하기 위해 사용됩니다.
public class CustomRestfulException extends RuntimeException {

	private HttpStatus httpStatus;
	
	public CustomRestfulException(String message, HttpStatus httpStatus) {
		super(message);
		this.httpStatus = httpStatus;
	}
	
}
