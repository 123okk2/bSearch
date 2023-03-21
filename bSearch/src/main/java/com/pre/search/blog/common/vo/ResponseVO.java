package com.pre.search.blog.common.vo;

import java.io.Serializable;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * 사용자의 모든 HTTP 요청에 공통된 RESPONSE를 반환하기 위한 클래스
 * @author 이민우
 *
 */
@Data
@Getter
@Setter
public class ResponseVO implements Serializable {
	private static final long serialVersionUID = -2929789292155268166L;
	// http 결과 status
	private HttpStatus status;
	// http 결과 메시지 반환
	private String message;
	// 데이터를 반환해야 하는 경우 데이터 탑재
	private Object data;
}