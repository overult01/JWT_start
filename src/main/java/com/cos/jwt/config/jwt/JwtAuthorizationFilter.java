package com.cos.jwt.config.jwt;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.cos.jwt.repository.UserRepository;

// 시큐리티가 가지고 있는 필터 중에 BasicAuthenticationFilter가 있음.
// 권한, 인증이 필요한 특정 주소를 요청하면 위 필터를 무조건 타게 되어있다.
// 만약에 권한, 인증이 필요한 주소가 아니면 이 필터를 안탄다.
public class JwtAuthorizationFilter extends BasicAuthenticationFilter{

	private UserRepository userRepository;
	
	public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
		super(authenticationManager);
		this.userRepository = userRepository;
	}
	
	// 인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 타게 된다.
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		System.out.println("인증이나 권한이 필요한 주소가 요청 됨");
		
		String jwtHeader = request.getHeader("Authorization");
		System.out.println("jwtHeader : " + jwtHeader);
		
		// header가 있는지 확인 
		if (jwtHeader == null || !jwtHeader.startsWith("Bearer")) {
			System.out.println("==");
			chain.doFilter(request, response);
			return;
		}
		
		// jwt 토큰을 검증해서 정상적인 사용자인지 확인
		String jwtToken = request.getHeader("Authoriaztion").replace("Bearer", "");
		
		// 서명 
		String username = 
				JWT.require(Algorithm.HMAC512("cos")).build().verify(jwtToken).getClaim("username").asString();
		
		// 서명이 정상적이면
		if (username != null) {
			System.out.println("username 정상");
			User userEntity = userRepository.findByUsername(username);
			
			PrincipalDetails principalDetails = new PrincipalDetails(userEntity);
			// jwt 토큰 서명을 통해서 서명이 정상이면 Authentication 객체를 강제로 만들기(username이 null이 아니기 때문) 
			Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());
			
			// 시큐리티를 저장할 수 있는 세션에 접근하여 Authentication 객체를 저장.
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
		}
		chain.doFilter(request, response);
	}
}
