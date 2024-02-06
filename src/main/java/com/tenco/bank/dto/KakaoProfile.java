
package com.tenco.bank.dto;

import lombok.Data;

@Data
public class KakaoProfile {

	private Long id;
	private String connectedAt;
	private Properties properties;
	private KakaoAccount kakaoAccount;

	// 안쓸거면 내부 클래스 사용 가능한데 이번에는 외부 클래스로 사용 

}
