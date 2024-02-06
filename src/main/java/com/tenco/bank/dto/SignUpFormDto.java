package com.tenco.bank.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpFormDto {

	private String username;
	private String password;
	private String fullname;

	// 파일 업로드 처리 담기([]여러개 담을 때는 배열로)
	private MultipartFile customFile; //name 속성값과 동일 해야함
	
	// insert 추가
	private String originFileName;
	private String uploadFileName;
	
}
