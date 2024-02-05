package com.tenco.bank.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tenco.bank.repository.entity.Account;
import com.tenco.bank.repository.entity.CustomHistoryEntity;

@Mapper //반드시 확인
public interface AccountRepository {
	
	// 계좌 생성
	public int insert(Account account);
	
	// 출금, 입금, 이체 기능
	public int updateById(Account account);
	
	public int deleteById(Integer id);
	
	// 계좌 목록 조회 - 하나의 유저는 n개의 계좌를 가질수 있다.
	public List<Account> findAllByUserId(Integer userId);
	
	// 단일 계좌 검색 기능 - 계좌번호 중복 확인, 출금,입금,이체 기능 - 계좌 존재 여부 확인
	public Account findByNumber(String number);
	
	// 나의 계좌 조회
	public Account findByAccountId(Integer id);

}
