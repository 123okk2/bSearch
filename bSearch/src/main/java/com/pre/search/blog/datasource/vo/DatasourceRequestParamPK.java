package com.pre.search.blog.datasource.vo;

import java.io.Serializable;

/**
 * 
 * 식별관계로 인해 DatasourceRequestParam이 복합키가 되어 PK 클래스 지정
 * @author 이민우
 *
 */
public class DatasourceRequestParamPK implements Serializable {

	private static final long serialVersionUID = -2929789292155268166L;
	
	private String column;
	private String id;
	
}
