package com.cos.jwt.config.jwt;

import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties.Credential;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cos.jwt.config.auth.PrincipalDetails;
import com.cos.jwt.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

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
		try {
			
//			BufferedReader br = request.getReader();
//			String input = null;
//			while ((input = br.readLine()) != null) {
//				System.out.println(input);
//			}

			ObjectMapper om = new ObjectMapper();
			User user = om.readValue(request.getInputStream(), User.class);
			System.out.println(user);
			
			// 토큰 생성
			UsernamePasswordAuthenticationToken authenticationToken
			= new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
			
			// PrincipalDetailsService의 loadUserByUsername()가 실행된 후 정상이면 authentication리턴됨.
			// DB에 있는 Username, pw가 일치한다는 것.
			// authenticationManager에 토큰을 넣으면 인증 정보 리턴. 이 authentication에 내 로그인 정보가 담김.
			Authentication authentication =
					authenticationManager.authenticate(authenticationToken);
			
			// 아래가 출력이 된다는 건 로그인이 되었다는 뜻.
			PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
			System.out.println(principalDetails.getUser().getUsername()); // 인증이 정상적으로 됐다는 의미 
			
			System.out.println("======1========");			
			return authentication; // Authentication객체가 session영역에 저장해야해서 return해줌.
			// 리턴의 이유: 권한 관리를 security가 대신 해주기 때문에 편하려고(jwt토큰을 사용하면서 세션을 사용할 필요x지만, 권한 처리 해주려고 사용)
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("======2========");
		
		// 2. 정상인지 로그인시도. AuthenticationManager로 로그인시도를 하면 PrincipalDetailsService가 호출. 그러면 loadUserByUsername가 자동실행된다.
		
		// 3. PrincipalDetails를 세션에 담고(목적: 권한관리)
		
		// 4. JWT토큰을 만들어서 응답해주면 된다.

		return null;		
	}
	
	// attemptAuthentication 실행 후 인증이 정상적으로 되었으면 successfulAuthentication가 실행된다.
	// jwt토큰을 만들어서 request요청한 사용자에게 jwt토큰을 response.
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		System.out.println("successfulAuthentication 실행: 즉, 인증 완료 상태");
		super.successfulAuthentication(request, response, chain, authResult);
	}
	
}
