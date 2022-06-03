package com.cos.jwt.config.jwt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.RequiredArgsConstructor;

// UsernamePasswordAuthenticationFilter: 스프링 시큐리티 내 필터
// /login 요청해서 Username, pw 전송하면(post)
// UsernamePasswordAuthenticationFilter가 동작함.
// 근데 우리는 formLogin을 disabled 한 상태여서 미작동. -> 우리는 이 필터를 시큐리티 필터에 등록해주면 된다.

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	// /login 요청을 하면 로그인 시도를 위해서 실행되는 함수
	// AuthenticationManager를 통해서 로그인 시도할 때 실행되는 함수 
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		System.out.println("JwtAuthenticationFilter: 로그인 시도중");
		
		// 1. username, pw받아서
		
		// 2. 정상인지 로그인시도. AuthenticationManager로 로그인시도를 하면 PrincipalDetailsService가 호출. 그러면 loadUserByUsername가 자동실행된다.
		
		// 3. PrincipalDetails를 세션에 담고(목적: 권한관리)
		
		// 4. JWT토큰을 만들어서 응답해주면 된다.
		
		return super.attemptAuthentication(request, response);
	}
	
}
