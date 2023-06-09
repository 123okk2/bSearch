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
 * datasource의 request query parameter의 정보들이 저장되는 Domain 클래스.
 * API 확장성을 고려해, 같은 데이터를 다른 key로 내보낼 수 있으므로 동일한 데이터를 반환하는 서로다른 key들을 저장하기 위함.
 * 
 *	column	id(f)	sourceColumn
 * 	meta	test1	meta
 * 	meta	test2	meta_data
 * 
 * @author 이민우
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="datasource_res_param")
@ToString
@IdClass(DatasourceResponseParamPK.class)
public class DatasourceResponseParam {
	
	@Id
	@Column(name="column", nullable=false, columnDefinition="VARCHAR(50)")
	private String column;
	
	@Id
	@Column(name="id", nullable=false, columnDefinition="VARCHAR(20)")
	private String id;
	
	@Column(name="sourceColumn", nullable=false, columnDefinition="VARCHAR(50)")
	private String sourceColumn;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@MapsId
	@JoinColumn(name="id")
	private Datasource datasource;
	
	/**
	 * vo를 통해 domain을 생성하는 생성자
	 * @param vo
	 */
	public DatasourceResponseParam(DatasourceResponseParamVO vo, Datasource datasource) {
		this.column = vo.getColumn();
		this.sourceColumn = vo.getSourceColumn();
		this.id = datasource.getId();
		this.datasource = datasource;
	}
}
