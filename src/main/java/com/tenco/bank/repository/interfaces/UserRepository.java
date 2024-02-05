package com.tenco.bank.repository.interfaces;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.tenco.bank.dto.SignUpFormDto;
import com.tenco.bank.repository.entity.User;

//interface + xml 연결

@Mapper //user.xml <mapper namespace="com.tenco.bank.repository.interfaces.UserRepository">  연결된 위치를 찾을 수 있게 해줌
public interface UserRepository {

	public int insert(User user);
	public int updateById(User user);
	public int deleteById(Integer id);
	public User findById(Integer id);
	public List<User> findAll();
	
	// 사용자 username으로 존재 여부 확인
	public User findByUsername(String username); // UserService의 User userEntity = userRepository.findByUsername(dto.getUsername()); dto의 getUsername 의 타입으로 파라미터 변경해줌
	public User findByUsernameAndPassword(User user); //(파라미터 부분)
}
