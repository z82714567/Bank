package com.tenco.bank.dto;

import lombok.Data;

@Data //파싱을 위해 setter 해줘야 함
public class transferFormDto {
	
	private Long amount;
	private String wAccountNumber;
	private String wAccountPassword;
	private String dAccountNumber;
	
	

}
