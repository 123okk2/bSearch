package com.pre.search.blog.common.util;

import java.util.List;

/**
 * 데이터 유효성을 검사하기 위한 공통 함수 모음 클래스
 * 
 * @author 이민우
 *
 */
public class ValidateUtil {

	/**
	 * 특정 데이터가 빈 데이터(null 혹은 "")인지 확인
	 * 
	 * @param object 확인할 데이터
	 * @return 빈 데이터일 경우 true
	 */
	public static boolean isEmptyData(Object object) {
		if(object == null || "".equals(String.valueOf(object).trim())) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 특정 데이터가 String 데이터인지 확인
	 * 
	 * @param object 확인할 데이터
	 * @return String 데이터일 경우 true
	 */
	public static boolean isValidString(Object object) {
		if(isEmptyData(object)) {
			return false;
		}
		
		if(object instanceof String) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * 특정 데이터가 Integer 데이터인지 확인
	 * 
	 * @param object 확인할 데이터
	 * @return Integer 데이터일 경우 true
	 */
	public static boolean isValidInteger(Object object) {
		if(isEmptyData(object)) {
			return false;
		}
		
		// 빈 데이터인지 먼저 확인
		try {
			Integer intObject = new Integer(String.valueOf(object));
		}
		catch(NumberFormatException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * 특정 List가 비었는지 확인
	 * 
	 * @param object 확인할 List
	 * @return 비었을 경우 true
	 */
	public static boolean isEmptyList(List<?> object) {
		if(object == null || object.size() == 0) {
			return true;
		}
		
		return false;
	}
}
