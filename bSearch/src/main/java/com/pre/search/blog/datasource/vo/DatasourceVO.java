package com.pre.search.blog.datasource.vo;

import java.io.Serializable;
import java.util.List;

import com.pre.search.blog.common.util.ValidateUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * datasource, datasourceRequestParam, datasourceResponseParm을 한 데 합쳐
 * 사용자에게 출력하고, 시스템 내에서도 간편하게 사용하기 위한 VO
 * 
 * @author 이민우
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DatasourceVO implements Serializable {
	
	private static final long serialVersionUID = -2929789292155268166L;

	// 식별자
	private String id;
	// 프로토콜 : http/https
	private String protocol;
	// API 주소
	private String host;
	// API 접근 HTTP 메소드
	private String method;
	// API 포트
	private Integer port;
	// API PATH (/로 시작)
	private String path;
	
	// request 파라미터 목록
	private List<DatasourceRequestParamVO> requestParams;
	// response 파라미터 목록
	private List<DatasourceResponseParamVO> responseParams;
	// requset 헤더 목록
	private List<DatasourceHeaderVO> requestHeaders;

	// OFFSET MIN/MAX
	private Integer offsetMin;
	private Integer offsetMax;

	// LIMIT MIN/MAX
	private Integer limitMin;
	private Integer limitMax;
	
	/**
	 * datasource를 기반으로 vo 생성
	 * @param datasource
	 */
	public DatasourceVO(Datasource datasource) {
		this.id = datasource.getId();
		this.protocol = datasource.getProtocol();
		this.host = datasource.getHost();
		this.method = datasource.getMethod();
		this.port = datasource.getPort();
		this.path = datasource.getPath();
		this.offsetMax = datasource.getOffsetMax();
		this.offsetMin = datasource.getOffsetMin();
		this.limitMax = datasource.getLimitMax();
		this.limitMin = datasource.getLimitMin();
	}
	
	/**
	 * 특정 column에 대입되는 datasource의 Request Query Parameter 이름 반환
	 * @param column
	 * @return
	 */
	public String getRequestColumn(String column) {
		if(ValidateUtil.isEmptyList(requestParams)) {
			return null;
		}
		
		for(DatasourceRequestParamVO requestParam : requestParams) {
			if(requestParam.getColumn().equals(column)) {
				return requestParam.getSourceColumn();
			}
		}
			
		return null;
	}
	
	/**
	 * 특정 column에 대입되는 datasource의 Response data의 Key 반환
	 * @param column
	 * @return
	 */
	public String getResponseColumn(String column) {
		if(ValidateUtil.isEmptyList(responseParams)) {
			return null;
		}
		
		for(DatasourceResponseParamVO responseParam : responseParams) {
			if(responseParam.getColumn().equals(column)) {
				return responseParam.getSourceColumn();
			}
		}
			
		return null;
	}
	
}
