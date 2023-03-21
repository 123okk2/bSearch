package com.pre.search.blog.keyword.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pre.search.blog.common.code.CommonCode;
import com.pre.search.blog.common.util.ValidateUtil;
import com.pre.search.blog.common.vo.QueryVO;
import com.pre.search.blog.common.vo.ResponseVO;
import com.pre.search.blog.keyword.repository.KeywordRepository;
import com.pre.search.blog.keyword.vo.Keyword;
import com.pre.search.blog.keyword.vo.KeywordVO;

import lombok.extern.slf4j.Slf4j;

/**
 * ������� ��α� �˻� ���� �˻�� �����ϰ�, ����� �����͸� �������� �α� �˻��� TOP10�� ����ϴ� ���� Ŭ����.
 * @author �̹ο�
 *
 */
@Slf4j
@Service
public class KeywordService {
	
	@Autowired KeywordRepository keywordRepository;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	// �ȳ����� ������ ����
	private static String SUCCESS_SELECT = "keyword resource loaded successfully";
	private static String NOT_VALID_DATA = "query resource is not valid";
	private static String INTERNAL_ERROR = "Some error occured";
	
	/**
	 * ��α� �˻� ���� ���� QueryVO�� query ����
	 * 
	 * @param keyword ����ڰ� �Է��ߴ� query
	 */
	public void save(String keyword) {
		log.info("@@ KeywordService.save started : {}", keyword);
		
		try {
			Keyword keywordEntity = new Keyword();
			keywordEntity.setSearchDate(new Date());
			keywordEntity.setWord(keyword);
			
			keywordRepository.save(keywordEntity);
		}
		catch(Exception e) {
			// ����ڿ� ���� ����ϴ� �κ��� �ƴϰ� �鿡�� ���ư��� �����̹Ƿ� ������ ��ȯ�ϰų� ������ ����.
			// �׳� ���.
			log.error("@@ KeywordService.save failed : {}", e.getMessage());
		}
	}
	
	/**
	 * TOP10 �α� �˻��� ���
	 * 
	 * @param queryVO startDate�� endDate�� �Է��� Ư�� �Ⱓ �� �α� �˻��� ��ȸ ����
	 * @return
	 */
	public ResponseVO select(QueryVO queryVO) {
		log.info("@@ KeywordService.select started : {}", queryVO);
		
		// ��ȯ�� response �޽��� ����
		ResponseVO response = new ResponseVO();
		response.setStatus(HttpStatus.OK);
		response.setMessage(SUCCESS_SELECT);
		
		// queryVO ��ȿ�� �˻�
		if(!isValidQuery(queryVO)) {
			log.warn("@@ KeywordService.select failed : {}", NOT_VALID_DATA);
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessage(NOT_VALID_DATA);
			
			return response;
		}
		
		
		try {
			List<Map<String, Object>> result;
			
			if(queryVO.getStartDate() == null && queryVO.getEndDate() == null) {
				// SELECT *
				// JPQL�� limit�� �ȵǴϱ� Pageable�� ��� ����
				result = keywordRepository.findByDefault(PageRequest.of(0, 10));
			}
			else if(queryVO.getStartDate() != null && queryVO.getEndDate() != null) {
				// BETWEEN ����
				Date startDate = formatter.parse(queryVO.getStartDate());
				Date endDate = formatter.parse(queryVO.getEndDate());
				// endDate�� 23:59:59�� ����
				endDate.setHours(23);
				endDate.setMinutes(59);
				endDate.setSeconds(59);

				result = keywordRepository.findByDateBetween(startDate, endDate, PageRequest.of(0, 10));
			}
			else if(queryVO.getStartDate() == null && queryVO.getEndDate() != null) {
				// SMALLER_OR_SAME ����
				Date endDate = formatter.parse(queryVO.getEndDate());
				// endDate�� 23:59:59�� ����
				endDate.setHours(23);
				endDate.setMinutes(59);
				endDate.setSeconds(59);
				
				result = keywordRepository.findByDateBefore(endDate, PageRequest.of(0, 10));
			}
			else {
				// GREATER_OR_SAME ����
				Date startDate = formatter.parse(queryVO.getStartDate());
				
				result = keywordRepository.findByDateAfter(startDate, PageRequest.of(0, 10));
			}
			
			// Map<String, Object> => KeywordVO
			List<KeywordVO> keywordVOList = new ArrayList<>();
			for(Map<String, Object> resultOne : result) {
				KeywordVO keywordVOOne = new KeywordVO();
				keywordVOOne.setKeyword(resultOne.get(CommonCode.KEYWORD_WORD).toString());
				keywordVOOne.setCount(Integer.parseInt(resultOne.get(CommonCode.KEYWORD_COUNT).toString()));
				keywordVOList.add(keywordVOOne);
			}
			
			response.setData(keywordVOList);

			log.info("@@ KeywordService.select success : {}", keywordVOList);

		}
		catch(Exception e) {
			// DB ���� 500 ��ȯ
			log.error("@@ KeywordService.select failed : {}", e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage(INTERNAL_ERROR);
		}
		
		return response;
		
	}
	
	/**
	 * queryVO�� ��ȿ���� Ȯ���ϴ� �Լ�.
	 * 
	 * @param queryVO
	 * @return
	 */
	public boolean isValidQuery(QueryVO queryVO) {
		
		// object ��ü ��ȿ�� �˻�
		if(ValidateUtil.isEmptyData(queryVO)) {
			return false;
		}
		// start_date�� ���� ��� ���信 �´���
		if(queryVO.getStartDate() != null) {
			try {
				formatter.parse(queryVO.getStartDate());
			}
			catch(ParseException e) {
				return false;
			}
		}
		// end_date�� ���� ��� ���信 �´���
		if(queryVO.getEndDate() != null) {
			try {
				formatter.parse(queryVO.getEndDate());
			}
			catch(ParseException e) {
				return false;
			}
		}
		
		// start_date�� end_date���� �̷��� ���
		if(queryVO.getStartDate() != null && queryVO.getEndDate() != null) {
			try {
				if(formatter.parse(queryVO.getStartDate()).getTime() > formatter.parse(queryVO.getEndDate()).getTime()) {
					return false;
				}
			} catch (ParseException e) {
				return false;
			}
		}
		
		return true;
	}
}
