package com.pre.search.blog.common.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.pre.search.blog.common.code.CommonCode;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * ����ڰ� ���� �Ķ���͸� ���� HTTP ����� ��û�ϴ� ���,
 * �ش� HTTP ���� Query Parameter�� �������� ���� ���� Ŭ����.
 * 
 * bSearch Application �� ��� HTTP ��ſ� ���Ǵ� Query Parameter���� �̸� ����.
 * (Ư�� ��ɿ��� ������ �ʴ� �������� null)
 * 
 * @author �̹ο�
 *
 */
@Getter
@Setter
@ToString
public class QueryVO {
	
	// datasource ���� ���� �Ķ����
	@JsonProperty(CommonCode.DATASOURCE_HOST)
	private String host;
	@JsonProperty(CommonCode.DATASOURCE_PROTOCOL)
	private String protocol;
	
	// ��α� ã�� ���� ���� �Ķ����
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
	// TODO: �˻� �ҽ� �߰� �� �ش� �˻� �ҽ��� ���� �Ķ���� �߰�.

	
	// Keyword �˻� ���� ���� �Ķ����
	// ���ڱ����� ���� �����̹Ƿ� String���� ���� ���� Date�� ��ȯ
	@JsonProperty(CommonCode.START_DATE)
	private String startDate;
	@JsonProperty(CommonCode.END_DATE)
	private String endDate;
}
