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

//HTTP통신 POST + DTO로 데이터 파싱
@RestController // data 내려줄 때 사용함
public class RestControllerTest2 {

	// 클라이언트에서 접근하는 주소 설계
	@GetMapping("my-test2")
	public ResponseEntity<?> myTest2() {
		// 다른 서버로 자원을 요청한 다음
		// 다시 클라이언트에게 자원을 내려 줌

		// 자원 등록 요청 --> POST 방식 사용법
		// 1. URI 객체 만들기 (http통신을 하려면)
		// https://jsonplaceholder.typicode.com/posts
		URI uri = UriComponentsBuilder.fromUriString("https://jsonplaceholder.typicode.com").path("/posts").encode()
				.build().toUri();

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
		params.put("title", "블로그 포스트 1");
		params.put("body", "후미진 어느 언덕에서 도시락 소풍");
		params.put("userId", "1");

		// 헤더와 바디 결합
		HttpEntity<Map<String, String>> requestMessage = new HttpEntity<>(params, headers);

		// RestTemplate의 exchange 메서드를 이용해 HTTP POST 요청을 보냅니다. 이 메서드는 요청 URL, HTTP 메서드 타입, 요청 메시지, 그리고 응답 본문의 타입을 인자로 받습니다.
		ResponseEntity<BoardDto> response = restTemplate.exchange(uri, HttpMethod.POST, requestMessage, BoardDto.class);
		
		// 파싱 처리 해야 한다. 응답 본문(제이슨형태)을 BoardDto 객체로 변환(필드별로 데이터를 쉽게 처리, 조작 가능).
		BoardDto boardDto = response.getBody();
		System.out.println("TEST : BDTO " + boardDto.toString());
		
		//HTTP 상태 코드를 OK(200)로 설정하고, 응답 본문을 반환합니다.
		return ResponseEntity.status(HttpStatus.OK).body(response.getBody());

	}

}
