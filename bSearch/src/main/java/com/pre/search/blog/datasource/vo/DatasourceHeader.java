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
 * Ȯ�强�� ���� datasource �� Header�� �����ϴ� datasource_header�� VO
 * @author �̹ο�
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="datasource_header")
@ToString
@IdClass(DatasourceHeaderPK.class)
public class DatasourceHeader {
	
	@Id
	@Column(name="header", nullable=false, columnDefinition="VARCHAR(50)")
	private String header;
	
	@Id
	@Column(name="id", nullable=false, columnDefinition="VARCHAR(20)")
	private String id;
	
	@Column(name="val", nullable=false, columnDefinition="VARCHAR(50)")
	private String val;

	@ManyToOne(fetch=FetchType.LAZY)
	@MapsId
	@JoinColumn(name="id")
	private Datasource datasource;

	/**
	 * vo�� ���� domain�� �����ϴ� ������
	 * @param vo
	 */
	public DatasourceHeader(DatasourceHeaderVO vo, Datasource datasource) {
		this.header = vo.getHeader();
		this.val = vo.getVal();
		this.id = datasource.getId();
		this.datasource = datasource;
	}
}
