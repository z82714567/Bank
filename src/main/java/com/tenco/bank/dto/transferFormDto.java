package com.tenco.bank.dto;

import lombok.Data;

@Data //파싱을 위해 setter 해줘야 함
public class transferFormDto {
	
	 private Long amount;
	    private String wAccountNumber; // 출금 계좌
	    private String dAccountNumber; // 입금 계좌
	    private String password;
	
	

}
