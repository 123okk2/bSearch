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
 * datasource, datasourceRequestParam, datasourceResponseParm�� �� �� ����
 * ����ڿ��� ����ϰ�, �ý��� �������� �����ϰ� ����ϱ� ���� VO
 * 
 * @author �̹ο�
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DatasourceVO implements Serializable {
	
	private static final long serialVersionUID = -2929789292155268166L;

	// �ĺ���
	private String id;
	// �������� : http/https
	private String protocol;
	// API �ּ�
	private String host;
	// API ���� HTTP �޼ҵ�
	private String method;
	// API ��Ʈ
	private Integer port;
	// API PATH (/�� ����)
	private String path;
	
	// request �Ķ���� ���
	private List<DatasourceRequestParamVO> requestParams;
	// response �Ķ���� ���
	private List<DatasourceResponseParamVO> responseParams;
	// requset ��� ���
	private List<DatasourceHeaderVO> requestHeaders;

	// OFFSET MIN/MAX
	private Integer offsetMin;
	private Integer offsetMax;

	// LIMIT MIN/MAX
	private Integer limitMin;
	private Integer limitMax;
	
	/**
	 * datasource�� ������� vo ����
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
	 * Ư�� column�� ���ԵǴ� datasource�� Request Query Parameter �̸� ��ȯ
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
	 * Ư�� column�� ���ԵǴ� datasource�� Response data�� Key ��ȯ
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
