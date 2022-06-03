package com.cos.jwt.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.filter.CorsFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity // 시큐리티 활성화
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	private final CorsFilter corsFilter;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();
		
		http
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // STATELESS: 세션 미사용 설정 
		.and()
		
		// 인증 x일 때: 컨트롤러에 @CrossOrigin / 인증 O일 때: 시큐리티 필터에 등록
		.addFilter(corsFilter) // 모든 요청은 이 필터를 거친다.(CorsConfig에서 설정한 corsFilter)
		
		.formLogin().disable() // formLogin 미사용
		
		// jwt사용시 여기까지는 기본 
		
		.httpBasic().disable(); // 기본 로그인 방식 미사용
		
		http.authorizeRequests()
		.antMatchers("/api/v1/user/**")
		.access("hasRole('Role_USER') or hasRole('Role_MANAGER') or hasRole('Role_ADMIN')")
		.antMatchers("/api/v1/manager/**")
		.access("hasRole('Role_MANAGER') or hasRole('Role_ADMIN')")
		.antMatchers("/api/v1/admin/**")
		.access("hasRole('Role_ADMIN')")
		.anyRequest().permitAll(); // 다른 요청은 전부 권한없이 접근가능 
		
	}
}
