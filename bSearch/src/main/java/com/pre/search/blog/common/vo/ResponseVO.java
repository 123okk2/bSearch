package com.pre.search.blog.common.vo;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * ������� ��� HTTP ��û�� ����� RESPONSE�� ��ȯ�ϱ� ���� Ŭ����
 * @author �̹ο�
 *
 */
@Data
@Getter
@Setter
public class ResponseVO {
	// http ��� status
	private HttpStatus status;
	// http ��� �޽��� ��ȯ
	private String message;
	// �����͸� ��ȯ�ؾ� �ϴ� ��� ������ ž��
	private Object data;
}