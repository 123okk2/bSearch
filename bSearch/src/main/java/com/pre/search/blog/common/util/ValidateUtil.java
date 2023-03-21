package com.pre.search.blog.common.util;

import java.util.List;

/**
 * ������ ��ȿ���� �˻��ϱ� ���� ���� �Լ� ���� Ŭ����
 * 
 * @author �̹ο�
 *
 */
public class ValidateUtil {

	/**
	 * Ư�� �����Ͱ� �� ������(null Ȥ�� "")���� Ȯ��
	 * 
	 * @param object Ȯ���� ������
	 * @return �� �������� ��� true
	 */
	public static boolean isEmptyData(Object object) {
		if(object == null || "".equals(String.valueOf(object).trim())) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Ư�� �����Ͱ� String ���������� Ȯ��
	 * 
	 * @param object Ȯ���� ������
	 * @return String �������� ��� true
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
	 * Ư�� �����Ͱ� Integer ���������� Ȯ��
	 * 
	 * @param object Ȯ���� ������
	 * @return Integer �������� ��� true
	 */
	public static boolean isValidInteger(Object object) {
		if(isEmptyData(object)) {
			return false;
		}
		
		// �� ���������� ���� Ȯ��
		try {
			Integer intObject = new Integer(String.valueOf(object));
		}
		catch(NumberFormatException e) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Ư�� List�� ������� Ȯ��
	 * 
	 * @param object Ȯ���� List
	 * @return ����� ��� true
	 */
	public static boolean isEmptyList(List<?> object) {
		if(object == null || object.size() == 0) {
			return true;
		}
		
		return false;
	}
}
