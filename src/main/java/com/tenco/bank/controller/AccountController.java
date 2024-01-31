package com.tenco.bank.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tenco.bank.dto.AccountSaveFormDto;
import com.tenco.bank.dto.DepositFormDto;
import com.tenco.bank.dto.withdrawFormDto;
import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.handler.exception.UnAuthorizedException;
import com.tenco.bank.repository.entity.Account;
import com.tenco.bank.repository.entity.User;
import com.tenco.bank.service.AccountService;
import com.tenco.bank.utils.Define;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/account") //대문
public class AccountController {
	
	@Autowired //가독성을 위해 DI 하는거 라고 알려줌
	//final 상수 정의하는게 메모리상에 좋음 private final HttpSession session;
	private HttpSession session;
	
	@Autowired
	private AccountService accountService;
	
	//생성자 의존 (DI)
	//public AccountController(HttpSession session) {
	//	this.session = session;
	//}
	
	//페이지 요청
	//예시 주소 http://loacalhost:80/account/save?number=100&user=100; 
    /*
     * 계좌 생성 페이지 요청
     * @return saveForm.jsp
     * */
	
	@GetMapping("/save")
	public String savePage() {
		
		//인증 검사
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		if(principal == null) {
			throw new UnAuthorizedException("로그인 후 이용해 주세요.", HttpStatus.UNAUTHORIZED);
		}
		
		return "account/saveForm";
	}
	
	
	//계좌 생성 로직 만들기
	@PostMapping("/save") //body -> String -> 파싱(DTO)
	public String saveProc(AccountSaveFormDto dto) {
		System.out.println("dto.accoutsavdform------------" + dto.toString());
		
		//1. 인증 검사
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		
		if(principal == null) {
			throw new UnAuthorizedException("로그인 후 이용해 주세요.", HttpStatus.UNAUTHORIZED);
		}
		
		//2. 유효성 검사
		if(dto.getNumber() == null 	|| dto.getNumber().isEmpty()) {
			throw new CustomRestfulException("계좌번호를 입력하세요.", HttpStatus.UNAUTHORIZED);
		}
		if(dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfulException("계좌 비밀번호를 입력하세요.", HttpStatus.UNAUTHORIZED);
		}
		if(dto.getBalance() == null || dto.getBalance() < 0 ){
			throw new CustomRestfulException("잘못된 금액입니다.", HttpStatus.UNAUTHORIZED);
		}
		//3. 서비스 호출
		accountService.createAccount(dto, principal.getId());
		System.out.println("sskldfjlsdf " + dto.toString());
		
		//4. 응답 처리
		//list 페이지로 이동 예정
		return "redirect:/account/list";
	}
	
		
		//계좌 목록 보기 페이지 생성
		//http://localhost:80/account/list or http://localhost:80/account 
		@GetMapping({"/list", "/"})
		public String listPage(Model model) {
			//필터(보통 시큐리티), 인터셉터(보통 인증 검사), AOP 로 처리 가능
			//1. 인증 검사 
			User principal = (User) session.getAttribute(Define.PRINCIPAL);
			if(principal == null) {
				throw new UnAuthorizedException("로그인 후 이용해 주세요.", HttpStatus.UNAUTHORIZED);
			}
			
			//경우의 수 (유, 무)
			List<Account> accountList = accountService.readAccountListByUserId(principal.getId());
			
			if(accountList.isEmpty()) {
				model.addAttribute("accountList", null);
				
			}else {
				model.addAttribute("accountList", accountList);
			}
			
			return "account/list";
		}
		
		//1월 31일
		//출금 페이지 요청
		@GetMapping("/withdraw")
		public String withdrawPage() {
			//1. 인증 검사 
			User principal = (User) session.getAttribute(Define.PRINCIPAL);
			if(principal == null) {
				throw new UnAuthorizedException("로그인 후 이용해 주세요.", HttpStatus.UNAUTHORIZED);
			}
			return "account/withdraw";
		}
		
		//출금 요청 로직 만들기
		@PostMapping("/withdraw")
		public String withdrawProc(withdrawFormDto dto) {
			//1. 인증 검사 
			User principal = (User) session.getAttribute(Define.PRINCIPAL);
			if(principal == null) {
				throw new UnAuthorizedException("로그인 후 이용해 주세요.", HttpStatus.UNAUTHORIZED);
			}
			//2. 유효성 검사
			if(dto.getAmount() == null) {
				throw new CustomRestfulException("금액을 입력하시오.", HttpStatus.BAD_REQUEST);
			}
			if(dto.getAmount().longValue() <=  0) {
				throw new CustomRestfulException("출금액이 0원 이하일 수 없습니다.", HttpStatus.BAD_REQUEST);
			}
			if(dto.getWAccountNumber() ==  null || dto.getWAccountNumber().isEmpty()) {
				throw new CustomRestfulException("계좌번호를 입력하시오.", HttpStatus.BAD_REQUEST);
			}
			if(dto.getWAccountPassword() == null || dto.getWAccountPassword().isEmpty()) {
				throw new CustomRestfulException("계좌 비밀번호를 입력하시오.", HttpStatus.BAD_REQUEST);
			}
			//3. 서비스 호출
			accountService.updateAccountWithdraw(dto, principal.getId());
			
			return "redirect:/account/list";
		}
		// 입금 페이지 요청
		@GetMapping("/deposit")
		public String depositPage() {
			// 1. 인증 검사
			User principal = (User) session.getAttribute(Define.PRINCIPAL); // 다운 캐스팅
			if (principal == null) {
				throw new UnAuthorizedException(Define.ENTER_YOUR_LOGIN, HttpStatus.UNAUTHORIZED);
			}

			return "account/deposit";
		}

		// 입금 요청 로직
		@PostMapping("/deposit")
		public String depositProc(DepositFormDto dto) {
			// 1. 인증 검사
			User principal = (User) session.getAttribute(Define.PRINCIPAL); // 다운 캐스팅
			if (principal == null) {
				throw new UnAuthorizedException(Define.ENTER_YOUR_LOGIN, HttpStatus.UNAUTHORIZED);
			}
			
			// 2. 유효성 검사
			if(dto.getAmount() == null) {
				throw new CustomRestfulException(Define.ENTER_YOUR_BALANCE, HttpStatus.BAD_REQUEST);
			}
			if(dto.getAmount().longValue() <= 0) {
				throw new CustomRestfulException(Define.D_BALANCE_VALUE, HttpStatus.BAD_REQUEST);
			}
			if(dto.getDAccountNumber() == null || dto.getDAccountNumber().isEmpty()) {
				throw new CustomRestfulException(Define.ENTER_YOUR_ACCOUNT_NUMBER, HttpStatus.BAD_REQUEST);
			}
			
			// 서비스 호출
			accountService.updateAccountDeposit(dto, principal.getId());
			
			return "redirect:/account/list";
			
		}

}
