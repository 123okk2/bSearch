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
 * ����� ���� �α� ��Ͽ� ����
 * ��踦 ������ ���� �����̶� ������ DB�� ������ ���� ����.
 * �ܼ��� �α׸� ���
 * 
 * @author �̹ο�
 *
 */
@Slf4j
@Component
public class CommonFilter implements Filter {
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		// IP �ĺ�
		String clientIP = ((HttpServletRequest) request).getHeader("X-FORWARDED-FOR");
		if(clientIP == null || "".equals(clientIP.trim())) {
			clientIP = request.getRemoteAddr();
		}
		
		// uri �ĺ�
		String requestURI = ((HttpServletRequest) request).getRequestURI();
		
		
		log.info("@@ {} : {} > {}", (new Date()).toString(), clientIP, requestURI);

		chain.doFilter(request, response);
		
	}
	
}
