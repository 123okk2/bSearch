package com.pre.search.blog.search.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 블로그 검색 결과 반환된 메타 데이터 vo
 * @author 이민우
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
