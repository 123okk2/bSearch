package com.pre.search.blog.keyword.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.pre.search.blog.common.vo.QueryVO;
import com.pre.search.blog.common.vo.ResponseVO;
import com.pre.search.blog.keyword.service.KeywordService;

/**
 * 인기 검색어 목록 반환을 위한 컨트롤러
 * @author 이민우
 *
 */
@RestController
public class KeywordController {
	
	@Autowired KeywordService keywordService;
	
	/**
	 * TOP10 인기 검색어 반환 컨트롤러
	 * @param queryVO startDate, endDate을 지정해서 특정 기간 내 인기 검색어 조회 지원
	 * @return
	 */
	@GetMapping("/keyword")
	public ResponseVO selectKeywordList(@ModelAttribute QueryVO queryVO) {
		return keywordService.select(queryVO);
	}
}
