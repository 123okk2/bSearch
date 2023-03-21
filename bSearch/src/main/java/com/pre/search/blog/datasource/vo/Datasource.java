package com.pre.search.blog.datasource.vo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 
 * datasource Domain 클래스
 * 
 * id		protocol	host			method	port	path	
 * test1	http		192.168.0.1		GET		8080	/api/search	
 * test2	https		test.api.site	POST	NULL	/search		
 * 
 * @author 이민우
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="datasource")
@ToString
public class Datasource {

	// 식별자
	@Id
	@Column(name="id", nullable=false, columnDefinition="VARCHAR(20)")
	private String id;
	
	// 프로토콜 : http/https
	@Column(name="protocol", nullable=false, columnDefinition="VARCHAR(10)")
	private String protocol;
	
	// API 주소
	@Column(name="host", nullable=false, columnDefinition="VARCHAR(100)")
	private String host;
	
	// API의 HTTP METHOD
	@Column(name="method", nullable=false, columnDefinition="VARCHAR(10)")
	private String method;
	
	// API 포트
	@Column(name="port", nullable=true, columnDefinition="int")
	private Integer port;
	
	// API PATH (/로 시작)
	@Column(name="path", nullable=true, columnDefinition="VARCHAR(100)")
	private String path;
	
	// OFFSET MIN/MAX
	@Column(name="offsetMin", nullable=true, columnDefinition="int")
	private Integer offsetMin;
	@Column(name="offsetMax", nullable=true, columnDefinition="int")
	private Integer offsetMax;
	
	// LIMIT MIN/MAX
	@Column(name="limitMin", nullable=true, columnDefinition="int")
	private Integer limitMin;
	@Column(name="limitMax", nullable=true, columnDefinition="int")
	private Integer limitMax;
	
	
	/**
	 * 사용자에 입력받은 vo 기반 생성자
	 * 
	 * @param datasourceVO
	 */
	public Datasource(DatasourceVO datasourceVO) {
		this.id = datasourceVO.getId();
		this.protocol = datasourceVO.getProtocol();
		this.host = datasourceVO.getHost();
		this.method = datasourceVO.getMethod();
		this.port = datasourceVO.getPort();
		this.path = datasourceVO.getPath();
		this.offsetMax = datasourceVO.getOffsetMax();
		this.offsetMin = datasourceVO.getOffsetMin();
		this.limitMax = datasourceVO.getLimitMax();
		this.limitMin = datasourceVO.getLimitMin();
	}
	
}
