package com.pre.search.blog.keyword.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * ����ڿ��� �����ϱ� ���� Keyword VO
 * 
 * keyword : �˻�Ƚ�� �� ����
 * 
 * @author �̹ο�
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
