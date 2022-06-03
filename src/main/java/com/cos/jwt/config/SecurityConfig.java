package com.cos.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.web.filter.CorsFilter;

import com.cos.jwt.config.jwt.JwtAuthenticationFilter;
import com.cos.jwt.config.jwt.JwtAuthorizationFilter;
import com.cos.jwt.filter.MyFilter3;
import com.cos.jwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity // 시큐리티 활성화
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	private final CorsFilter corsFilter;
	private final UserRepository userRepository;
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		// 내가 만든 필터가 시큐리티 필터 체인보다 먼저 걸리게 하고 싶을 때 
		// http.addFilterBefore(new MyFilter3(), SecurityContextPersistenceFilter.class); // 해당 필터가 걸리기 전에 직접 생성한 필터(시큐리티 chain에 등록x된)를 걸기
		
		http.csrf().disable();
		
		http
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // STATELESS: 세션 미사용 설정 
		.and()
		
		// 인증 x일 때: 컨트롤러에 @CrossOrigin / 인증 O일 때: 시큐리티 필터에 등록
		.addFilter(corsFilter) // 모든 요청은 이 필터를 거친다.(CorsConfig에서 설정한 corsFilter)
		
		.formLogin().disable() // formLogin 미사용
		
		// jwt사용시 여기까지는 기본 
		
		.httpBasic().disable() // 기본 로그인 방식 미사용
		
		.addFilter(new JwtAuthenticationFilter(authenticationManager())) // UsernamePasswordAuthenticationFilter 재활성화
		.addFilter(new JwtAuthorizationFilter(authenticationManager(), userRepository));
		
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
