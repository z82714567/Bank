package com.tenco.bank.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tenco.bank.repository.entity.Account;

@Mapper //반드시 확인
public interface AccountRepository {
	
	public int insert(Account account);
	public int updateById(Account account);
	public int deleteById(Integer id);
	
	// 계좌 조회 - 하나의 유저는 n개의 계좌를 가질수 있다.
	public List<Account> findAllByUserId(Integer userId);
	public Account findByNumber(String number);

}