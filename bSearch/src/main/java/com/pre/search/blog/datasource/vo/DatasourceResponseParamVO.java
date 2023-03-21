package com.pre.search.blog.datasource.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DatasourceResponseParam 내 멤버변수 중 사용자에게 보여줄 데이터를 추린 VO
 * @author 이민우
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
	 * domain 클래스를 vo로 변환하는 생성자
	 * @param domain
	 */
	public DatasourceResponseParamVO(DatasourceResponseParam domain) {
		this.column = domain.getColumn();
		this.sourceColumn = domain.getSourceColumn();
	}
}
