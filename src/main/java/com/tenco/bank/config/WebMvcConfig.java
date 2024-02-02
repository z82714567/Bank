package com.tenco.bank.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.tenco.bank.handler.AuthInterceptor;

@Configuration //IoC 대상(2개 이상의 IoC (빈 객체 만들 때) 사용됨) , 스프링부트 설정 클래스
public class WebMvcConfig implements WebMvcConfigurer{

	@Autowired //DI 처리
	private AuthInterceptor authInterceptor;
	
	//요청 올 때 마다 도메인URI 검사 할 예정(유저는 로그인,회원가입이니까 인증검사 필요 없, /account/xxx으로 들어오는 도메인을 다 검사)
	//도메인 = 어떠한 범주 
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		
		 registry.addInterceptor(authInterceptor)
		 		.addPathPatterns("/account/**")
		 		.addPathPatterns("/auth/**");
	}
	
	//암호화 (회원가입 처리 때 필요)
	@Bean // IoC 대상, 싱글톤 처리
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
