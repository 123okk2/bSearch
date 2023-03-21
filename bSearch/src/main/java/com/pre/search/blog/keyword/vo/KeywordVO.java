package com.pre.search.blog.keyword.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 사용자에게 전달하기 위한 Keyword VO
 * 
 * keyword : 검색횟수 로 구성
 * 
 * @author 이민우
 *
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class KeywordVO {
	
	private String keyword;
	private long count;
	
}
