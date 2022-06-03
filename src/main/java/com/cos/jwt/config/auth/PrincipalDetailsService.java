package com.cos.jwt.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

// (원래) http://localhost:8080/login 요청이 올 때 동작 => formLogin().disable()했기 때문에 동작안한다. 그래서 얘를 실행시켜줄 필터를 만들기
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService{
	
	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		System.out.println("PrincipalDetailsService의 loadUserByUsername 호출");
		User userEntity = userRepository.findByUsername(username);
		System.out.println("userEntity:  "+ userEntity);
		return new PrincipalDetails(userEntity);
	}
	
}
