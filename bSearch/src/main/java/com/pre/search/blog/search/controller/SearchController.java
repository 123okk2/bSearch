package com.pre.search.blog.search.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.pre.search.blog.common.vo.QueryVO;
import com.pre.search.blog.common.vo.ResponseVO;
import com.pre.search.blog.search.service.SearchService;

/**
 * �˻��� ���� Controller
 * 
 * @author �̹ο�
 *
 */
@RestController
public class SearchController {
	
	@Autowired SearchService searchService;
	
	
	/**
	 * Query�� �޾� �˻� ����
	 * 
	 * @param queryVO
	 * @return
	 */
	@GetMapping("/search")
	public ResponseVO searchBlog(@ModelAttribute QueryVO queryVO) {
		
		return searchService.search(queryVO);
		
	}
}
