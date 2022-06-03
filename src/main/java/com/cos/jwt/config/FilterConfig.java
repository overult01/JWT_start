package com.cos.jwt.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cos.jwt.filter.MyFilter1;
import com.cos.jwt.filter.MyFilter2;

// 시큐리티 필터 체인 외부에서 필터 걸기 (시큐리티 필터가 모두 실행된 후 직접 만든 필터가 걸린다)
@Configuration
public class FilterConfig {

	@Bean
	public FilterRegistrationBean<MyFilter1> filter1(){
		FilterRegistrationBean<MyFilter1> bean = new FilterRegistrationBean<>(new MyFilter1());
		bean.addUrlPatterns("/*"); // 모든 request 요청에서 다 적용 
		bean.setOrder(0); // 낮은 번호가 필터중에서 가장 먼저 실행 
		return bean;
	}

	@Bean
	public FilterRegistrationBean<MyFilter2> filter2(){
		FilterRegistrationBean<MyFilter2> bean = new FilterRegistrationBean<>(new MyFilter2());
		bean.addUrlPatterns("/*"); // 모든 request 요청에서 다 적용 
		bean.setOrder(1); // 낮은 번호가 필터중에서 가장 먼저 실행 
		return bean;
	}	
}
