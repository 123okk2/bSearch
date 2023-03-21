package com.pre.search.blog.search.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * ��α� �˻� ��� ��ȯ�� ��Ÿ ������ vo
 * @author �̹ο�
 *
 */
@Getter
@Setter
@ToString
public class MetaDataVO implements Serializable {

	private static final long serialVersionUID = -2929789292155268166L;

	private int totalCount;
	private int pageableCount;
	private Boolean isEnd;
}
