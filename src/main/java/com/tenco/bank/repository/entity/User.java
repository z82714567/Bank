package com.tenco.bank.repository.entity;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

	private Integer id;
	private String username;
	private String password;
	private String fullname;
	private Timestamp createdAt;

	// 파일 업로드
	private String originFileName;
	private String uploadFileName;
	
	// 업로드된 파일 프로필에 사용 - 사용자가 회원가입 시 이미지 넣는 경우, 안 넣는 경우 (header.jsp)
	public String setupUserImage() {
		return uploadFileName == null ? "https://picsum.photos/id/1/350" : "/images/upload/" + uploadFileName;
	}
}
