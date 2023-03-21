package com.pre.search.blog.common.vo;

import java.io.Serializable;

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
public class ResponseVO implements Serializable {
	private static final long serialVersionUID = -2929789292155268166L;
	// http ��� status
	private HttpStatus status;
	// http ��� �޽��� ��ȯ
	private String message;
	// �����͸� ��ȯ�ؾ� �ϴ� ��� ������ ž��
	private Object data;
}