package com.tenco.bank.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller //요청과 응답을 위한, view만 보이게 근데 data리턴도 가능(@Responsdata 사용해서)
@RequestMapping("test") //대문 달기
public class TestController {
	
	//주소 설계(http://localhost:80/test/main)
	@GetMapping("/main")
	public String mainPage() {
		//인증 검사
		//유효성 검사
		
		//뷰 리졸브 -> 해당하는 파일 찾아서 필요하다면 데이터도 같이 보냄
		  //전체경로: /WEB-INF/view/layout/main.jsp
	      //prefix: /WEB-INF/view/ 생략
	      //suffix: .jsp 생략
		return "layout/main";
	}
	

	
}
