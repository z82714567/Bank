package com.tenco.bank.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.tenco.bank.dto.BoardDto;

//HTTP통신 PUT(UPDATE)
@RestController // data 내려줄 때 사용함
public class RestControllerTest3 {

	// 클라이언트에서 접근하는 주소 설계
	@GetMapping("my-test3")
	public ResponseEntity<?> myTest3() {
		// 다른 서버로 자원을 요청한 다음
		// 다시 클라이언트에게 자원을 내려 줌

		// 1. URI 객체 만들기 (http통신을 하려면)
		// https://jsonplaceholder.typicode.com/posts
		URI uri = UriComponentsBuilder.fromUriString("https://jsonplaceholder.typicode.com").path("/posts").path("/1")
				.encode().build().toUri();

		// 2 객체 생성
		RestTemplate restTemplate = new RestTemplate();

		// exchange(RestTemplate 대표적 메서드) 사용 방법 - http 통신을 위해
		// 1. HttpHeaders 객체를 만들고 Header 메세지 구성
		// 2. body 데이터를 key=value 구조로 만들기
		// 3. HttpEntity 객체를 생성해서 Header 와 결합 후 요청

		// 헤더 구성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/json; charset=UTF-8");

		// 바디 구성
		//MultiValueMap은 키에 여러 개의 값을 저장 반면, HashMap은 키에 하나의 값만 저장
		Map<String, String> params = new HashMap<>();
		params.put("id", "1");
		params.put("title", "foo");
		params.put("body", "bar");
		params.put("userId", "1");

		// 헤더와 바디 결합
		HttpEntity<Map<String, String>> requestMessage = new HttpEntity<>(params, headers);

		// HTTP 요청 처리
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.PUT, requestMessage, String.class); // PUT(UPDATE)방식 요청
		System.out.println("headers " + response.getHeaders());	

		return ResponseEntity.status(HttpStatus.OK).body(response.getBody());

	}

}
