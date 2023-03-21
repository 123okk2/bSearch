package com.pre.search.blog.common.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pre.search.blog.common.code.CommonCode;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * 사용자가 쿼리 파라미터를 통해 HTTP 통신을 요청하는 경우,
 * 해당 HTTP 내의 Query Parameter를 가져오기 위한 공통 클래스.
 * 
 * bSearch Application 내 모든 HTTP 통신에 사용되는 Query Parameter들을 미리 선언.
 * (특정 기능에서 사용되지 않는 변수들은 null)
 * 
 * @author 이민우
 *
 */
@Getter
@Setter
@ToString
public class QueryVO {
	
	// datasource 전용 쿼리 파라미터
	@JsonProperty(CommonCode.DATASOURCE_HOST)
	private String host;
	@JsonProperty(CommonCode.DATASOURCE_PROTOCOL)
	private String protocol;
	
	// 블로그 찾기 전용 쿼리 파라미터
	@JsonProperty(CommonCode.DATAOUSRCE_ID)
	private String datasourceId;
	@JsonProperty(CommonCode.QUERY)
	private String query;
	@JsonProperty(CommonCode.SORT)
	private String sort;
	@JsonProperty(CommonCode.OFFSET)
	private Integer offset;
	@JsonProperty(CommonCode.LIMIT)
	private Integer limit;
	// TODO: 검색 소스 추가 시 해당 검색 소스의 쿼리 파라미터 추가.

	
	// Keyword 검색 전용 쿼리 파라미터
	// 일자까지만 받을 예정이므로 String으로 받은 다음 Date로 변환
	@JsonProperty(CommonCode.START_DATE)
	private String startDate;
	@JsonProperty(CommonCode.END_DATE)
	private String endDate;
}
