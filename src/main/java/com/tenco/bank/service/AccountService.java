package com.tenco.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tenco.bank.dto.AccountSaveFormDto;
import com.tenco.bank.dto.DepositFormDto;
import com.tenco.bank.dto.transferFormDto;
import com.tenco.bank.dto.withdrawFormDto;
import com.tenco.bank.handler.exception.CustomRestfulException;
import com.tenco.bank.repository.entity.Account;
import com.tenco.bank.repository.entity.History;
import com.tenco.bank.repository.interfaces.AccountRepository;
import com.tenco.bank.repository.interfaces.HistoryRepository;
import com.tenco.bank.utils.Define;

@Service // IoC 대상 + 싱글톤으로 관리 됨.
public class AccountService {

	// OCP 인터페이스로 관리
	@Autowired
	private AccountRepository accountRepository;

	// 출금 처리 기능에서 필요
	@Autowired
	private HistoryRepository historyRepository;

	// 계좌 생성
	// 사용자 정보 필요

	@Transactional
	public void createAccount(AccountSaveFormDto dto, Integer principalId) {
		System.out.println("createAccount principalId : " + principalId);
		System.out.println("createAccount AccountSaveFormDto.toString :  " + dto.toString());
		System.out.println("createAccount dto.getNumber : " + dto.getNumber());

		// 계좌번호 중복 확인
		if (readAccount(dto.getNumber()) != null) {
			System.out.println("createAccount not null 실행");
			throw new CustomRestfulException(Define.EXIST_ACCOUNT, HttpStatus.INTERNAL_SERVER_ERROR);

		} else {
			System.out.println("넘어오나?");
		}
		System.out.println("이건 실행이 되나? " + dto.toString());

		Account account = new Account();
		account.setNumber(dto.getNumber());
		account.setPassword(dto.getPassword());
		account.setBalance(dto.getBalance());
		account.setUserId(principalId);

		System.out.println("dto To Entity account : " + account.toString());

		int resultRowCount = accountRepository.insert(account);
		if (resultRowCount != 1) {
			throw new CustomRestfulException(Define.FAIL_TO_CREATE_ACCOUNT, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 단일 계좌 검색 기능
	public Account readAccount(String number) {
		System.out.println("number : " + number);
		return accountRepository.findByNumber(number);

	}

	// 계좌 목록 보기 기능
	public List<Account> readAccountListByUserId(Integer principalId) {
		return accountRepository.findAllByUserId(principalId);

	}

	// 1월 31일
	// 출금 기능 만들기
	// 7. 트랜잭션 처리 필!
	@Transactional
	public void updateAccountWithdraw(withdrawFormDto dto, Integer principalId) {
		// 1. 계좌 존재 여부 확인 -- select
		Account accountEntity = accountRepository.findByNumber(dto.getWAccountNumber());
		if (accountEntity == null) {
			throw new CustomRestfulException(Define.NOT_EXIST_ACCOUNT, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		// 2. 본인 계좌 여부 확인 -- select로 조회보다는 accountEntity(메모리에 저장된) 객체 상태로 principalId로
		// 비교 확인이 낫다
		accountEntity.checkOwner(principalId);
		/*
		 * if(accountEntity.getUserId() != principalId) { throw new
		 * CustomRestfulException("본인 소유의 계좌가 아닙니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		 * }
		 */
		// 3. 입력된 계좌 비밀번호 확인 -- String(불변) 문자열 비교 equals
		accountEntity.checkPassword(dto.getWAccountPassword());
		/*
		 * if(accountEntity.getPassword().equals(dto.getWAccountPassword()) == false) {
		 * throw new CustomRestfulException("출금 계좌 비밀번호가 틀렸습니다.",
		 * HttpStatus.INTERNAL_SERVER_ERROR); }
		 */
		// 4. 잔액 여부 확인
		accountEntity.checkBlance(dto.getAmount());
		/*
		 * if(accountEntity.getBalance() < dto.getAmount()) { throw new
		 * CustomRestfulException("계좌 잔액이 부족합니다.", HttpStatus.INTERNAL_SERVER_ERROR); }
		 */
		// 5. 출금 처리 기능 --> 객체 상태값 변경됨
		accountEntity.withdraw(dto.getAmount());
		accountRepository.updateById(accountEntity);

		// 6. 거래 내역 등록 -- insert(history_tb)
		History history = new History();
		history.setAmount(dto.getAmount());
		history.setWBalance(accountEntity.getBalance());
		history.setDBalance(null);
		history.setWAccountId(accountEntity.getId());
		history.setDAccountId(null);

		int rowResultCount = historyRepository.insert(history);
		if (rowResultCount != 1) {
			throw new CustomRestfulException("정상처리 되지 않았습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	// 입금 기능 만들기
	// 5. 트랜잭션 처리
	@Transactional
	public void updateAccountDeposit(DepositFormDto dto, Integer principalId) {
		// 1. 계좌 존재 여부 확인
		Account accountEntity = accountRepository.findByNumber(dto.getDAccountNumber());
		if (accountEntity == null) {
			throw new CustomRestfulException(Define.NOT_EXIST_ACCOUNT, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// 2. 본인 계좌 여부 확인
		accountEntity.checkOwner(principalId);

		// 3. 입금 처리
		accountEntity.deposit(dto.getAmount());
		accountRepository.updateById(accountEntity);

		// 4. history에 거래 내역 등록
		History history = new History();
		history.setAmount(dto.getAmount());
		history.setWBalance(null); // 출금 계좌의 잔액을 가져와야하기 때문에
		history.setDBalance(accountEntity.getBalance());
		history.setWAccountId(null);
		history.setDAccountId(accountEntity.getId());

		int rowResultCount = historyRepository.insert(history);
		if (rowResultCount != 1) {
			throw new CustomRestfulException("정상 처리 되지 않았습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	// 이체 기능 만들기
	// 1. 출금 계좌 존재 여부 확인 -- select
	// 2. 입금 계좌 존재 여부 확인 -- select
	// 3. 출금 계좌 본인 소유 확인
	// 4. 출금 계좌 비번 확인 -- object
	// 5. 출금 계좌 잔액 확인 -- object
	// 6. 출금 계좌 잔액 수정 -- update
	// 7. 입금 계좌 잔액 수정 -- update
	// 8. 거래 내역 등록 처리(이체 내역 쿼리) -- insert
	// 9. 트랜잭션 처리
	@Transactional
	public void updateAccountTransfer(transferFormDto dto, Integer principalId) {
		// 1. 출금 계좌 존재 여부 확인 -- select 
	}

}
