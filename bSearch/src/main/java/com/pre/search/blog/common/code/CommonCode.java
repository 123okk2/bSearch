package com.pre.search.blog.common.code;

/**
 * 
 * bSearch Application 내에서 공통적으로 사용되는 키워드들을 저장하고 관리하기 위한 클래스
 * @author 이민우
 *
 */
public class CommonCode {
	
	
	// 1. DATASOURCE
	
	public final static String DATASOURCE_PROTOCOL = "protocol";
	public final static String DATASOURCE_HOST = "host";
	
	
	// 2. BLOG 검색 (카카오 API를 기준으로 초기 세팅)
	
	// 2-0. 검색 대상 datasource
	public final static String DATAOUSRCE_ID = "datasourceId";
	// 2-1. REQUEST QUERY PARAMETER
	// 검색을 원하는 질의어.	특정 블로그 글만 검색하고 싶은 경우, 블로그 url과 검색어를 공백(' ') 구분자로 넣을 수 있음 (NOT NULL)
	public final static String QUERY = "query";
	// 결과 문서 정렬 방식, accuracy(정확도순) 또는 recency(최신순), 기본 값 accuracy
	public final static String SORT = "sort";
	// 결과 문서 정렬 방식 - recency / accuracy
	public final static String SORT_REC = "sortRec";
	public final static String SORT_ACC = "sortAcc";
	// 결과 페이지 번호
	public final static String OFFSET = "offset";
	// 한 페이지에 보여질 문서 수
	public final static String LIMIT = "limit";
	
	// 2-2. RESPONSE 데이터
	public final static String META = "meta";
	// 검색된 문서 수
	public final static String META_TOTAL_COUNT = "metaCount";
	// total_count 중 노출 가능 문서 수
	public final static String META_PAGEABLE_COUNT = "metaPageCount";
	// 현재 페이지가 마지막 페이지인지 여부, 값이 false면 page를 증가시켜 다음 페이지를 요청할 수 있음
	public final static String META_IS_END = "metaIsEnd";
	
	public final static String DOCUMENTS = "document";
	// 	블로그 글 제목
	public final static String DOCUMENTS_TITLE = "documentTitle";
	// 	블로그 글 요약
	public final static String DOCUMENTS_CONTENTS = "documentContents";
	// 	블로그 글 URL
	public final static String DOCUMENTS_URL = "documentUrl";
	// 블로그의 이름
	public final static String DOCUMENTS_BLOG_NAME = "documentBlogname";
	// 검색 시스템에서 추출한 대표 미리보기 이미지 URL, 미리보기 크기 및 화질은 변경될 수 있음
	public final static String DOCUMENTS_THUMBNAIL = "documentThumbnail";
	// 블로그 글 작성시간, ISO 8601	[YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
	public final static String DOCUMENTS_DATETIME = "documentDatetime";
	
	// TODO: 만약 타 API도 지원하게 되면 아래 칸에 추가 기재해서 확장
	//////////////////////////////////////////////
	//////////////////////////////////////////////
	
	
	// 3. Keyword 관련 Query
	// 3-1. Keyword get 시 기준일
	public final static String START_DATE = "startDate";
	public final static String END_DATE = "endDate";
	
	// 3-2. DB에서 RETURN된 키값
	public final static String KEYWORD_COUNT = "cnt";
	public final static String KEYWORD_WORD = "word";
}
