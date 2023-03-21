package com.pre.search.blog.search.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Blog 검색 결과 목록 VO
 * @author 이민우
 *
 */
@Getter
@Setter
@ToString
public class DocumentsVO implements Serializable {

	private static final long serialVersionUID = -2929789292155268166L;

	private String title;
	private String contents;
	private String url;
	private String blogname;
	private String thumbnail;
	
	// 현재는 Dateformat이 정해져 있지만,
	// 확장성을 고려하면 어떤 format의 Date가 들어올 지 모르니
	// String으로 반환.
	private String datetime;
}
