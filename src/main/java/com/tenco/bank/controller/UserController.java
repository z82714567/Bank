package com.tenco.bank.controller;

import java.io.File;
import java.io.IOException;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.tenco.bank.dto.KakaoProfile;
import com.tenco.bank.dto.OAuthToken;
import com.tenco.bank.dto.SignInFormDto;
import com.tenco.bank.dto.SignUpFormDto;
import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.repository.entity.User;
import com.tenco.bank.service.UserService;
import com.tenco.bank.utils.Define;

import jakarta.servlet.http.HttpSession;

@RequestMapping("/user")
@Controller
public class UserController {

	@Autowired
	private HttpSession httpSession;

	@Autowired
	private UserService userService;

	/*
	 * 회원 가입 페이지요청
	 * 
	 * @return SignUp.jsp 파일 리턴
	 * 
	 */

	// 화면을 반환
	// http://localhost:80/user/sign-up
	@GetMapping("/sign-up")
	public String signUpPage() {
		// prefix: /WEB-INF/view/
		// suffix: .jsp

		return "user/signUp";
	}

	/*
	 * 회원 가입 요청 처리 주소 설계 http://localhost:80/user/sign-up
	 *
	 */
	@PostMapping("/sign-up")
	public String singUpProc(SignUpFormDto dto) {

		System.out.println("dto : " + dto.toString());
		System.out.println("dto : " + dto.getCustomFile().getOriginalFilename());

		/* SignUpFormDto에 jsp에서 보낸 데이터 파라미터에 바인딩 한다. */

		// 1. 인증검사 X
		// 2. 유효성 검사 필요
		if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
			throw new CustomRestfulException("username을 입력 하세요", HttpStatus.BAD_REQUEST);
		}

		if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfulException("password를 입력 하세요", HttpStatus.BAD_REQUEST);
		}

		if (dto.getFullname() == null || dto.getFullname().isEmpty()) {
			throw new CustomRestfulException("fullname을 입력 하세요", HttpStatus.BAD_REQUEST);
		}

		// 파일 업로드
		MultipartFile file = dto.getCustomFile();
		if (file.isEmpty() == false) {
			// 사용자가 이미지를 업로드 했다면 기능 구현
			// 1.파일 사이즈 체크
			if (file.getSize() > Define.MAX_FILE_SIZE) {
				throw new CustomRestfulException("파일 크기는 20MB 이상 클 수 없습니다.", HttpStatus.BAD_REQUEST);
			}
			// 서버 컴퓨터에 파일을 넣을 디렉토리가 있는지 검사, 없다면 생성
			String saveDirectory = Define.UPLOAD_FILE_DERECTORY;
			// 폴더 없다면 오류 발생(파일 업로드 시)
			File dir = new File(saveDirectory);
			if (dir.exists() == false) {
				dir.mkdir(); // 폴더 없으면 폴더 생성
			}
			// 파일 이름(중복 처리 예방)
			UUID uuid = UUID.randomUUID();
			String fileName = uuid + "_" + file.getOriginalFilename();
			System.out.println("fileName : " + fileName);

			// 파일 업로드
			// C:\\work_spring\\upload/ab.png
			String uploadPath = Define.UPLOAD_FILE_DERECTORY + File.separator + fileName;
			File destination = new File(uploadPath);

			try {
				file.transferTo(destination);
			} catch (IllegalStateException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 객체 상태 변경
			dto.setOriginFileName(file.getOriginalFilename());
			dto.setUploadFileName(fileName);
		}

		// 유효성 검사 이후 service로 넘겨주기(회원가입만 파일업로드X)

		userService.createUser(dto); // (바인딩된 dto를 가지고 서비스의 create메서드로 감)

		// todo 로그인 페이지로 변경 예정
		return "redirect:/user/sign-in";
	}

	/*
	 * 로그인 페이지 요청
	 * 
	 * @return
	 * 
	 * 기능과 상관없이 구조나 주석 등을 다는 행위를 리팩토링 이라고 한다.
	 * 
	 */
	@GetMapping("/sign-in")
	public String signInPage() {

		return "/user/signIn";
	}

	/*
	 * 로그인 요청 처리
	 * 
	 * @param SignInFormDto
	 * 
	 * @return 추후 account/list 페이지로 이동 예정 (todo)
	 * 
	 */

	@PostMapping("/sign-in")
	public String signInProc(SignInFormDto dto) {

		// 1. 유효성 검사
		if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
			throw new CustomRestfulException("username을 입력 하시오", HttpStatus.BAD_REQUEST);
		}

		if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfulException("Password를 입력 하시오", HttpStatus.BAD_REQUEST);
		}

		// 서비스 호출 예정
		User user = userService.readUser(dto); // readUser메서드를 User결과가 됨

		httpSession.setAttribute(Define.PRINCIPAL, user); // 세션을 열고 로그인을 유지시키기 위해서 principal에 user값을 넣어줌

		// 로그인 완료시 보낼 페이지
		return "redirect:/account/list";
	}

	// 로그아웃 기능
	@GetMapping("/sign-out")
	public String signOutProc() {

		httpSession.invalidate();

		return "redirect:/user/sign-in";
	}
	
	// 소셜 로그인
	// http://localhost:80/user/kakao-callback?code=""
	@GetMapping("/kakao-callback")
	// @ResponseBody // 데이터를 반환해줌 (로그인 처리 후 지워줌 - 데이터 잘 넘어 오는지 확인용)
	public String kakaoCallback(@RequestParam String code) {
		System.out.println("code : " + code);
		
		// POST방식, 헤더/바디 구성
		RestTemplate rt1 = new RestTemplate();
		
		// 헤더 구성
		HttpHeaders headers1 = new HttpHeaders();
		headers1.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// 바디 구성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "e8462fc1117472d26560e8166e67e3e9");
		params.add("redirect_uri", "http://localhost/user/kakao-callback");
		params.add("code", code);
		
		// 헤더+바디 결합 --> 요청 http 메세지 만들어 짐
		HttpEntity<MultiValueMap<String, String>> reqMsg
			= new HttpEntity<>(params, headers1);
		ResponseEntity<OAuthToken> response 
			= rt1.exchange("https://kauth.kakao.com/oauth/token", HttpMethod.POST, reqMsg, OAuthToken.class);
		
		
		// 사용자 정보 가져오기 : 다시 요청하기 -- 인증 토큰을 가지고 사용자 정보 요청함 -- Rt 만들어서 요청
		RestTemplate rt2 = new RestTemplate();
		
		// 헤더 구성
		HttpHeaders headers2 = new HttpHeaders();
		headers2.add("Authorization", "Bearer " + response.getBody().getAccessToken());
		headers2.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
		
		// 바디 구성 x (쿼리 파라미터 필수 아니라서 제외)
		
		// 결합 --> 요청
		HttpEntity<MultiValueMap<String, String>> kakaoInfo
			= new HttpEntity<>(headers2);
		
		ResponseEntity<KakaoProfile> response2 
			= rt2.exchange("https://kapi.kakao.com/v2/user/me", HttpMethod.POST, kakaoInfo, KakaoProfile.class);
		
		System.out.println(response2.getBody());
		
		
		KakaoProfile kakoProfile = response2.getBody();
		
		// 최초 사용자 판단 여부 --> 사용자 username 존재여부 확인
		// 우리 사이트를 통해 로그인 한 사용자와 카카오로 로그인 한 사용자가 동일한 경우 
		SignUpFormDto dto = SignUpFormDto.builder()
								.username("OAuth_" + kakoProfile.getProperties().getNickname())
								.fullname("Kakao")
								.password("asd1234")
								.build();
		User oldUser = userService.readUserByUserName(dto.getUsername());
		if(oldUser == null) {
			// 최초 회원가입 처리
			userService.createUser(dto);
			
			// 
			oldUser = new User();  
			oldUser.setUsername(dto.getUsername());
			oldUser.setFullname(dto.getFullname());
		}
		
		oldUser.setPassword(null);
		// 존재하면 로그인 처리
		httpSession.setAttribute(Define.PRINCIPAL, oldUser);
		
		
		// 없다면 회원 후 로그인 처리
		
		
		
		
		return "redirect:/account/list";
	}

}
