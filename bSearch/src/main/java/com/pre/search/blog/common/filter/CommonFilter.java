package com.pre.search.blog.common.filter;

import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 사용자 접근 로그 기록용 필터
 * 통계를 내지는 않을 예정이라 별도로 DB에 저장은 하지 않음.
 * 단순히 로그만 기록
 * 
 * @author 이민우
 *
 */
@Slf4j
@Component
public class CommonFilter implements Filter {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// IP 식별
		String clientIP = ((HttpServletRequest) request).getHeader("X-FORWARDED-FOR");
		if(clientIP == null || "".equals(clientIP.trim())) {
			clientIP = request.getRemoteAddr();
		}
		
		// uri 식별
		String requestURI = ((HttpServletRequest) request).getRequestURI();
		
		
		log.info("@@ {} : {} > {}", (new Date()).toString(), clientIP, requestURI);

		chain.doFilter(request, response);
		
	}
	
}
