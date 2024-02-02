package com.tenco.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bank.dto.SignInFormDto;
import com.tenco.bank.dto.SignUpFormDto;
import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.repository.entity.User;
import com.tenco.bank.repository.interfaces.UserRepository;

import jakarta.servlet.http.HttpSession;

@Service // IoC 대상
public class UserService {

	// db 접근
	// 생성자 의존 주입 DI
	@Autowired       
	private UserRepository userRepository;
	
	@Autowired // 암호화
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private HttpSession httpSession;
	
	/*  
	 * @autowired 와 같은 역할 해당 코드가 있으면 autowired필요 없음
	 * public UserService(UserRepository userRepository) { this.userRepository =
	 * UserRepository; }
	 */
	
	/*
	 * 회원 가입 로직 처리 
	 * @param SignUpFormDto   
	 * return void
	 */
	
	// 회원 가입
	@Transactional //트랜잭션 처리 습관화 할 필요가 있다.
	public void createUser(SignUpFormDto dto) {
		
		// 암호화 처리(추가)
		User user = User.builder() //빌더 이유 : DTO가 DB와 다르기 때문에 엔티티로 변환해줌? 다시 알아봐
				.username(dto.getUsername())
				.password(passwordEncoder.encode(dto.getPassword()))
				.fullname(dto.getFullname())
				.build();
		
		int result = userRepository.insert(user);
		
		if(result != 1 ) {
			throw new CustomRestfulException("회원가입 실패", 
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		//값이 1이면(회원가입 완료) controller의 로그인화면으로 이동
	}
	
	/*
	 * 로그인 처리
	 * @param SignInFormDto
	 * @return User
	 *  
	 */
	
	// 로그인 처리(비번 암호화된 계정만 로그인 가능)
	public User readUser(SignInFormDto dto) {
		
		// 암호화 처리 후 변경함
	
		// 사용자의 username만 받아서 정보를 추출
		User userEntity = userRepository.findByUsername(dto.getUsername());
		if(userEntity == null) {
			throw new CustomRestfulException("존재하지 않는 계정입니다.", 
					HttpStatus.BAD_REQUEST);
		}
		
		// 비번 확인
		boolean isPwdMatched = passwordEncoder.matches(dto.getPassword(), userEntity.getPassword());
		if(isPwdMatched == false) {
			throw new CustomRestfulException("틀린 비밀번호입니다.", 
					HttpStatus.BAD_REQUEST);
		}
				
		/*
		 * User user = User.builder() .username(dto.getUsername())
		 * .password(dto.getPassword()) .build();
		 * 
		 * User userEntity = userRepository.findByUsernameAndPassword(user);
		 */

		return userEntity;
	}
	
	
	
}
