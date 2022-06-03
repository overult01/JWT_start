package com.cos.jwt.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyFilter3 implements Filter{

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// 다운캐스팅
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		
		// 토큰: id, pw가 정상적으로 들어와서 로그인이 완료되면 토큰 생성 후 그걸 응답해준다.
		// 요청할 때 마다 Header의 Authorization에 value값으로 토큰을 가져온다.
		// 그때 토큰이 넘어오면 이 토큰이 내가 만든 토큰이 맞는지 검증만 하면된다.(RSA, HS256)(토큰값이 같은지 보는게 아니라)
		
		// 토큰을 만들었다고 가정 후, 토큰이 넘어오면 인증 되게. (없으면 필터를 더 이상 못하게 해서 컨트롤러 진입 자체를 불가하게.)
		if (req.getMethod().equals("POST")) {
			System.out.println("post요청됨");
			
			String headerAuth = req.getHeader("Authorization");
			System.out.println(headerAuth);
			
			if (headerAuth.equals("cos")) { // 토큰으로 넣어준 값이 정확히 들어와야 인증됨
				chain.doFilter(req, res);
			} else {
				PrintWriter out = res.getWriter();
				out.println("인증안됨");
			}
		}
		
		System.out.println("필터1");
		chain.doFilter(req, res); // 프로그램을 끝내지 말고 체인에 전달하기 
	}
	
}
