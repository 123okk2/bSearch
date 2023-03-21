package com.pre.search.blog.datasource.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * datasource�� request query parameter�� �������� ����Ǵ� Domain Ŭ����.
 * API Ȯ�强�� �����, ���� �����͸� �ٸ� key�� ������ �� �����Ƿ� ������ �����͸� ��ȯ�ϴ� ���δٸ� key���� �����ϱ� ����.
 * 
 *	column	id(f)	sourceColumn
 * 	query	test1	query
 * 	query	test2	queryParam
 * 
 * @author �̹ο�
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="datasource_req_param")
@ToString
@IdClass(DatasourceRequestParamPK.class)
public class DatasourceRequestParam {
	
	/*
	 * Ȯ�强�� ���� QUERY PARAMETER���� Ű���� ������ ����.
	 * API���� QUERY, SORT, PAGE, SIZE�� �ٸ� �̸����� ������ �� �����Ƿ�, �������� �����ϵ��� ��.
	 */
	
	@Id
	@Column(name="column", nullable=false, columnDefinition="VARCHAR(50)")
	private String column;
	
	@Id
	@Column(name="id", nullable=false, columnDefinition="VARCHAR(50)")
	private String id;
	
	@Column(name="sourceColumn", nullable=false, columnDefinition="VARCHAR(50)")
	private String sourceColumn;

	@ManyToOne(fetch=FetchType.LAZY)
	@MapsId
	@JoinColumn(name="id")
	private Datasource datasource;
	
	/**
	 * vo�� ���� domain�� �����ϴ� ������
	 * @param vo
	 */
	public DatasourceRequestParam(DatasourceRequestParamVO vo, Datasource datasource) {
		this.column = vo.getColumn();
		this.sourceColumn = vo.getSourceColumn();
		this.id = datasource.getId();
		this.datasource = datasource;
	}

}
