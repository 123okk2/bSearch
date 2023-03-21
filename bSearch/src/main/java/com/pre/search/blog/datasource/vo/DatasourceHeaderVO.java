package com.pre.search.blog.datasource.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DatasourceHeader �� ������� �� ����ڿ��� ������ �����͸� �߸� VO
 * @author mkjh9
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DatasourceHeaderVO implements Serializable {
	private static final long serialVersionUID = -2929789292155268166L;

	private String header;
	private String val;
	
	/**
	 * domain Ŭ������ vo�� ��ȯ�ϴ� ������
	 * @param domain
	 */
	public DatasourceHeaderVO(DatasourceHeader domain) {
		this.header = domain.getHeader();
		this.val = domain.getVal();
	}
}
