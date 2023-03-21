package com.pre.search.blog.keyword.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import com.pre.search.blog.common.vo.QueryVO;
import com.pre.search.blog.common.vo.ResponseVO;
import com.pre.search.blog.keyword.service.KeywordService;

/**
 * �α� �˻��� ��� ��ȯ�� ���� ��Ʈ�ѷ�
 * @author �̹ο�
 *
 */
@RestController
public class KeywordController {
	
	@Autowired KeywordService keywordService;
	
	/**
	 * TOP10 �α� �˻��� ��ȯ ��Ʈ�ѷ�
	 * @param queryVO startDate, endDate�� �����ؼ� Ư�� �Ⱓ �� �α� �˻��� ��ȸ ����
	 * @return
	 */
	@GetMapping("/keyword")
	public ResponseVO selectKeywordList(@ModelAttribute QueryVO queryVO) {
		return keywordService.select(queryVO);
	}
}
