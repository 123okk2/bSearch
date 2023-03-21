package com.pre.search.blog.common.filter;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

/**
 * 에러를 ResponseVO처럼 반환하기위한 클래스
 * 통일성을 위함
 * 
 * @author 이민우
 *
 */
@Component
public class ErrorHandler extends DefaultErrorAttributes {

	@Override
	public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
		
		Map<String, Object> ownResult = super.getErrorAttributes(webRequest, options);
		// 순서 유지를 위해 Linked 사용
		Map<String, Object> newResult = new LinkedHashMap<>();
		
		newResult.put("status", HttpStatus.valueOf((Integer) ownResult.get("status")));
		newResult.put("message", ownResult.get("error"));
		newResult.put("data", null);
		
		return newResult;
	}
	
}
