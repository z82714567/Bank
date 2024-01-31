package com.tenco.bank.repository.entity;

import java.sql.Timestamp;
import java.text.DecimalFormat;

import org.springframework.http.HttpStatus;

import com.tenco.bank.handler.exception.CustomRestfulException;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Account {

	private Integer id;
	private String number;
	private String password;
	private Long balance;
	private Integer userId;
	private Timestamp createdAt;
	
	//출금기능
	public void withdraw(Long amount) {
		//방어적 코드 작성 - (balance보다 큰 금액으로 출금 불가하게)
		this.balance -= amount;
	}
	
	//입금기능
	public void deposit(Long amount) {
		this.balance += amount;
	}
	
	//패스워드 체크 기능
	public void checkPassword(String password) {
		if(this.password.equals(password) == false) {
			throw new CustomRestfulException("계좌 비밀번호가 틀렸습니다.", HttpStatus.BAD_REQUEST);
		}
	}
	
	//잔액 여부 확인 기능
	public void checkBlance(Long amount) {
		if(this.balance < amount) {
			throw new CustomRestfulException("출금 잔액이 부족합니다.", HttpStatus.BAD_REQUEST);
		}
	}
	
	//계좌 소유자 확인 기능
	public void checkOwner(Integer principalId) {
		if(this.userId != principalId) {
			throw new CustomRestfulException("계좌 소유자가 아닙니다.", HttpStatus.BAD_REQUEST);
		}
	}
	
	//포메터 기능 (1000 -> 1,000원) DecimalFormat
	public String formatBalance() {
		DecimalFormat df = new DecimalFormat("#,###");
		String formatNumber = df.format(balance);
		return formatNumber+"원";
	}
	
}


