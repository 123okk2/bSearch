package com.pre.search.blog.datasource.vo;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DatasourceHeader 내 멤버변수 중 사용자에게 보여줄 데이터를 추린 VO
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
	 * domain 클래스를 vo로 변환하는 생성자
	 * @param domain
	 */
	public DatasourceHeaderVO(DatasourceHeader domain) {
		this.header = domain.getHeader();
		this.val = domain.getVal();
	}
}
