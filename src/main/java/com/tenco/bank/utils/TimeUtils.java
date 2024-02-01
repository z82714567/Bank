package com.tenco.bank.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TimeUtils {

	//Timestamp --> String 변경 코드 작성
	//static --> 모든 인스턴스 공유 가능
	public static String timestampToString(Timestamp timestamp) {
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		//yyyy-MM-dd HH:mm:ss
		
		return sdf.format(timestamp);
	}
}
