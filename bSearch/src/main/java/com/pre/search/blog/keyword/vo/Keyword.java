package com.pre.search.blog.keyword.vo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 검색어를 저장하는 keyword Domain 클래스
 * 
 * id	searchDate				word
 * 1	2023-03-20T00:00:00		JPA
 * 2	2023-03-20T01:02:03		JAVA
 * 
 * @author 이민우
 *
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="keyword")
@ToString
public class Keyword {
	
	// 식별자
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private Long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="searchDate", nullable=false, columnDefinition="TIMESTAMP")
	private Date searchDate;
	
	@Column(name="word", nullable=false, columnDefinition="VARCHAR(100)")
	private String word;
}
