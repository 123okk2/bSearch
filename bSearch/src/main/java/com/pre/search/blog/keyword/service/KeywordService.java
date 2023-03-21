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
 * 사용자의 블로그 검색 이후 검색어를 저장하고, 저장된 데이터를 기준으로 인기 검색어 TOP10을 출력하는 서비스 클래스.
 * @author 이민우
 *
 */
@Slf4j
@Service
public class KeywordService {
	
	@Autowired KeywordRepository keywordRepository;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	// 안내문구 사전에 저장
	private static String SUCCESS_SELECT = "keyword resource loaded successfully";
	private static String NOT_VALID_DATA = "query resource is not valid";
	private static String INTERNAL_ERROR = "Some error occured";
	
	/**
	 * 블로그 검색 이후 사용된 QueryVO의 query 저장
	 * 
	 * @param keyword 사용자가 입력했던 query
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
			// 사용자와 직접 통신하는 부분이 아니고 백에서 돌아가는 로직이므로 에러를 반환하거나 하지는 않음.
			// 그냥 기록.
			log.error("@@ KeywordService.save failed : {}", e.getMessage());
		}
	}
	
	/**
	 * TOP10 인기 검색어 출력
	 * 
	 * @param queryVO startDate과 endDate을 입력해 특정 기간 내 인기 검색어 조회 지원
	 * @return
	 */
	public ResponseVO select(QueryVO queryVO) {
		log.info("@@ KeywordService.select started : {}", queryVO);
		
		// 반환할 response 메시지 설정
		ResponseVO response = new ResponseVO();
		response.setStatus(HttpStatus.OK);
		response.setMessage(SUCCESS_SELECT);
		
		// queryVO 유효성 검사
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
				// JPQL은 limit이 안되니까 Pageable로 대신 구현
				result = keywordRepository.findByDefault(PageRequest.of(0, 10));
			}
			else if(queryVO.getStartDate() != null && queryVO.getEndDate() != null) {
				// BETWEEN 연산
				Date startDate = formatter.parse(queryVO.getStartDate());
				Date endDate = formatter.parse(queryVO.getEndDate());
				// endDate은 23:59:59로 설정
				endDate.setHours(23);
				endDate.setMinutes(59);
				endDate.setSeconds(59);

				result = keywordRepository.findByDateBetween(startDate, endDate, PageRequest.of(0, 10));
			}
			else if(queryVO.getStartDate() == null && queryVO.getEndDate() != null) {
				// SMALLER_OR_SAME 연산
				Date endDate = formatter.parse(queryVO.getEndDate());
				// endDate은 23:59:59로 설정
				endDate.setHours(23);
				endDate.setMinutes(59);
				endDate.setSeconds(59);
				
				result = keywordRepository.findByDateBefore(endDate, PageRequest.of(0, 10));
			}
			else {
				// GREATER_OR_SAME 연산
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
			// DB 에러 500 반환
			log.error("@@ KeywordService.select failed : {}", e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage(INTERNAL_ERROR);
		}
		
		return response;
		
	}
	
	/**
	 * queryVO가 유효한지 확인하는 함수.
	 * 
	 * @param queryVO
	 * @return
	 */
	public boolean isValidQuery(QueryVO queryVO) {
		
		// object 자체 유효성 검사
		if(ValidateUtil.isEmptyData(queryVO)) {
			return false;
		}
		// start_date가 있을 경우 포멧에 맞는지
		if(queryVO.getStartDate() != null) {
			try {
				formatter.parse(queryVO.getStartDate());
			}
			catch(ParseException e) {
				return false;
			}
		}
		// end_date가 있을 경우 포멧에 맞는지
		if(queryVO.getEndDate() != null) {
			try {
				formatter.parse(queryVO.getEndDate());
			}
			catch(ParseException e) {
				return false;
			}
		}
		
		// start_date이 end_date보다 미래일 경우
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
