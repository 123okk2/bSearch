package com.pre.search.blog.keyword.vo;

import java.io.Serializable;

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
public class KeywordVO implements Serializable {
	private static final long serialVersionUID = -2929789292155268166L;
	
	private String keyword;
	private long count;
	
}
