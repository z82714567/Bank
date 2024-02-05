package com.tenco.bank.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.tenco.bank.repository.entity.CustomHistoryEntity;
import com.tenco.bank.repository.entity.History;

@Mapper
public interface HistoryRepository {

	// 출금,입금,이체 기능 거래 내역 저장
	public int insert(History history);

	public int updateById(History history);

	public int deleteById(Integer id);

	// 계좌 조회
	public History findById(Integer id);

	public List<History> findAll();

	// 나의 계좌 목록 조회(입출금,입금,출금)
	// 파라미터 갯수가 2개 이상이면 반드시! 파람 어노테이션 사용
	public List<CustomHistoryEntity> findByIdHistoryType(@Param("type") String type, @Param("id") Integer id);

}
