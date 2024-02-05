package com.tenco.bank.controller;

import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

//HTTP통신 POST(insert)
@RestController // data 내려줄 때 사용함
public class RestControllerTest {

	// 클라이언트에서 접근하는 주소 설계
	@GetMapping("my-test1")
	public ResponseEntity<?> myTest1() {
		// 다른 서버로 자원을 요청한 다음
		// 다시 클라이언트에게 자원을 내려 줌

		// 자원 등록 요청 --> POST 방식 사용법
		// 1. URI 객체 만들기 (http통신을 하려면)
		// https://jsonplaceholder.typicode.com/posts
		URI uri = UriComponentsBuilder.fromUriString("https://jsonplaceholder.typicode.com").path("/posts").encode()
				.build().toUri(); //HTTP 요청을 보낼 타겟 URL을 표현,지정

		// 2. RestTemplate 객체 생성
		RestTemplate restTemplate = new RestTemplate();

		// exchange(RestTemplate 대표적 메서드) 사용 방법 - http 통신을 위해
		// 1. HttpHeaders 객체를 만들고 Header 메세지 구성
		// 2. body 데이터를 key=value 구조로 만들기
		// 3. HttpEntity 객체를 생성해서 Header 와 결합 후 요청

		// 헤더 구성
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-type", "application/json; charset=UTF-8");

		// 바디 구성
		// MultiValueMap<K, V> = {"title" : "[블로그 포스트1]"} 
		// {"title" : "블로그 포스트1"}
		//MultiValueMap은 키에 여러 개의 값을 저장 반면, HashMap은 키에 하나의 값만 저장
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("title", "블로그 포스트 1");
		params.add("title", "블로그 포스트 2");
		params.add("body", "후미진 어느 언덕에서 도시락 소풍");
		params.add("userId", "1");

		// 헤더와 바디 결합
		HttpEntity<MultiValueMap<String, String>> requestMessage = new HttpEntity<>(params, headers);

		// HTTP 요청 처리
		ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.POST, requestMessage, String.class); // POST방식 요청
		System.out.println("headers " + response.getHeaders());																												
																														
		// http://localhost:80/my-test1 HTTP 상태 코드를 OK(200)로 설정하고, 응답 본문을 반환
		return ResponseEntity.status(HttpStatus.OK).body(response.getBody());

	}

	// HTTP통신 GET(select)
	// http://localhost:80/todos/1
	public ResponseEntity<?> test2(@PathVariable Integer id) {
		// URI uri = new URI("https://jsonplaceholder.typicode.com/" + id);
		URI uri = UriComponentsBuilder.fromUriString("https://jsonplaceholder.typicode.com").path("/todos")
				.path("/" + id).encode().build().toUri();

		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.getForEntity(uri, String.class); // GET(select)방식 요청

		return ResponseEntity.status(HttpStatus.OK).body(response.getBody());
	}

}
