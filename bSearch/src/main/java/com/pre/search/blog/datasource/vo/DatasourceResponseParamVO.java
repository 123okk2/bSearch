package com.pre.search.blog.datasource.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DatasourceResponseParam �� ������� �� ����ڿ��� ������ �����͸� �߸� VO
 * @author �̹ο�
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DatasourceResponseParamVO implements Serializable {
	private static final long serialVersionUID = -2929789292155268166L;
	
	private String column;
	private String sourceColumn;
	
	/**
	 * domain Ŭ������ vo�� ��ȯ�ϴ� ������
	 * @param domain
	 */
	public DatasourceResponseParamVO(DatasourceResponseParam domain) {
		this.column = domain.getColumn();
		this.sourceColumn = domain.getSourceColumn();
	}
}
