package com.tenco.bank.controller;

import java.util.List;

import org.eclipse.jdt.internal.compiler.ast.FalseLiteral;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tenco.bank.dto.AccountSaveFormDto;
import com.tenco.bank.dto.DepositFormDto;
import com.tenco.bank.dto.transferFormDto;
import com.tenco.bank.dto.withdrawFormDto;
import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.handler.exception.UnAuthorizedException;
import com.tenco.bank.repository.entity.Account;
import com.tenco.bank.repository.entity.CustomHistoryEntity;
import com.tenco.bank.repository.entity.User;
import com.tenco.bank.service.AccountService;
import com.tenco.bank.utils.Define;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/account") // 대문
//인증검사 코드리팩토링(2/2)
public class AccountController {

	@Autowired // 가독성을 위해 DI 하는거 라고 알려줌
	// final 상수 정의하는게 메모리상에 좋음 private final HttpSession session;
	private HttpSession session;

	@Autowired
	private AccountService accountService;

	/*
	 * 계좌 생성 페이지 요청 http://loacalhost:80/account/save?number=100&user=100;
	 * 
	 * @return saveForm.jsp
	 */
	@GetMapping("/save")
	public String savePage() {

		// 1. 인증 검사
		//AuthInterceptor

		return "account/saveForm";
	}

	// 계좌 생성 로직 만들기
	@PostMapping("/save") // body -> String -> 파싱(DTO)
	public String saveProc(AccountSaveFormDto dto) {
		System.out.println("dto.accoutsavdform------------" + dto.toString());

		// 1. 인증 검사
		User principal = (User) session.getAttribute(Define.PRINCIPAL);

		
		// 2. 유효성 검사
		if (dto.getNumber() == null || dto.getNumber().isEmpty()) {
			throw new CustomRestfulException("계좌번호를 입력하세요.", HttpStatus.UNAUTHORIZED);
		}
		if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfulException("계좌 비밀번호를 입력하세요.", HttpStatus.UNAUTHORIZED);
		}
		if (dto.getBalance() == null || dto.getBalance() < 0) {
			throw new CustomRestfulException("잘못된 금액입니다.", HttpStatus.UNAUTHORIZED);
		}
		// 3. 서비스 호출
		accountService.createAccount(dto, principal.getId());
		System.out.println("sskldfjlsdf " + dto.toString());

		// 4. 응답 처리
		// list 페이지로 이동 예정
		return "redirect:/account/list";
	}

	// 계좌 목록 페이지 요청
	// http://localhost:80/account/list or http://localhost:80/account
	@GetMapping({ "/list", "/" })
	public String listPage(Model model) {
		// 필터(보통 시큐리티), 인터셉터(보통 인증 검사), AOP 로 처리 가능
		// 1. 인증 검사
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		
		// if 경우의 수 (유, 무)
		List<Account> accountList = accountService.readAccountListByUserId(principal.getId());

		if (accountList.isEmpty()) {
			model.addAttribute("accountList", null);

		} else {
			model.addAttribute("accountList", accountList);
		}

		return "account/list";
	}

	// 출금 페이지 요청
	@GetMapping("/withdraw")
	public String withdrawPage() {
		// 1. 인증 검사
		//AuthInterceptor
		return "account/withdraw";
	}

	// 출금 기능 로직 
	@PostMapping("/withdraw")
	public String withdrawProc(withdrawFormDto dto) {
		// 1. 인증 검사
		User principal = (User) session.getAttribute(Define.PRINCIPAL);
		
		// 2. 유효성 검사
		if (dto.getAmount() == null) {
			throw new CustomRestfulException("금액을 입력하시오.", HttpStatus.BAD_REQUEST);
		}
		if (dto.getAmount().longValue() <= 0) {
			throw new CustomRestfulException("출금액이 0원 이하일 수 없습니다.", HttpStatus.BAD_REQUEST);
		}
		if (dto.getWAccountNumber() == null || dto.getWAccountNumber().isEmpty()) {
			throw new CustomRestfulException("계좌번호를 입력하시오.", HttpStatus.BAD_REQUEST);
		}
		if (dto.getWAccountPassword() == null || dto.getWAccountPassword().isEmpty()) {
			throw new CustomRestfulException("계좌 비밀번호를 입력하시오.", HttpStatus.BAD_REQUEST);
		}
		// 3. 서비스 호출
		accountService.updateAccountWithdraw(dto, principal.getId());

		return "redirect:/account/list";
	}

	// 입금 페이지 요청
	@GetMapping("/deposit")
	public String depositPage() {
		// 1. 인증 검사
		//AuthInterceptor

		return "account/deposit";
	}

	// 입금 기능 로직
	@PostMapping("/deposit")
	public String depositProc(DepositFormDto dto) {
		// 1. 인증 검사
		User principal = (User) session.getAttribute(Define.PRINCIPAL); // 다운 캐스팅
		
		// 2. 유효성 검사
		if (dto.getAmount() == null) {
			throw new CustomRestfulException(Define.ENTER_YOUR_BALANCE, HttpStatus.BAD_REQUEST);
		}
		if (dto.getAmount().longValue() <= 0) {
			throw new CustomRestfulException(Define.D_BALANCE_VALUE, HttpStatus.BAD_REQUEST);
		}
		if (dto.getDAccountNumber() == null || dto.getDAccountNumber().isEmpty()) {
			throw new CustomRestfulException(Define.ENTER_YOUR_ACCOUNT_NUMBER, HttpStatus.BAD_REQUEST);
		}

		// 서비스 호출
		accountService.updateAccountDeposit(dto, principal.getId());

		return "redirect:/account/list";
	}

	// 이체 페이지 요청
	@GetMapping("/transfer")
	public String transferPage() {
		// 1. 인증 검사
		//AuthInterceptor
		return "account/transfer";
	}

	// 이체 기능 요청 로직
	@PostMapping("/transfer")
	public String transferProc(transferFormDto dto) {
		// 1. 인증 검사
		User principal = (User) session.getAttribute(Define.PRINCIPAL); // 다운 캐스팅
		

		// 2. 유효성 검사
		if (dto.getAmount() == null) {
			throw new CustomRestfulException(Define.ENTER_YOUR_BALANCE, HttpStatus.BAD_REQUEST);
		}
		if (dto.getAmount().longValue() <= 0) {
			throw new CustomRestfulException(Define.D_BALANCE_VALUE, HttpStatus.BAD_REQUEST);
		}
		if (dto.getWAccountNumber() == null || dto.getWAccountNumber().isEmpty()) {
			throw new CustomRestfulException("출금 하실 계좌번호를 입력해주세요.", HttpStatus.BAD_REQUEST);
		}
		if (dto.getDAccountNumber() == null || dto.getDAccountNumber().isEmpty()) {
			throw new CustomRestfulException("이체 하실 계좌번호를 입력해주세요.", HttpStatus.BAD_REQUEST);
		}
		if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
			throw new CustomRestfulException(Define.ENTER_YOUR_PASSWORD, HttpStatus.BAD_REQUEST);
		}

		// 서비스 호출
		accountService.updateAccountTransfer(dto, principal.getId());

		return "redirect:/account/list"; // 리다이렉트 : 새로운 리퀘스트,리스펀스객체 생성됨을 뜻함
	}

	// 나의 계좌 목록 페이지 요청 (입출금, 입금, 출금페이지)
	// http://localhost:80/account/detail/1?type=
	@GetMapping("/detail/{id}")
	public String detail(@PathVariable Integer id,
			@RequestParam(name = "type", defaultValue = "all", required = false) String type, Model model) { //해당 필드가 쿼리스트링에 존재하지 않아도 예외가 발생x
		System.out.println("type : " + type);

		// 1. 인증 검사
		//AuthInterceptor

		// 2-2. 서비스 호출(나의 계좌 조회 기능)
		Account account = accountService.readByAccountId(id);

		// 2-1. 서비스 호출 (나의 계좌 목록 조회)
		List<CustomHistoryEntity> historyList = accountService.readHistoryListByAccount(type, id);
		System.out.println("list : " + historyList.toString());

		model.addAttribute("account", account); // 계좌 model 객체 담기
		model.addAttribute("historyList", historyList); // 내역 model 객체 담기

		// 3. 응답 결과 -> jsp파일로 내려주기

		return "account/detail";
	}

}
