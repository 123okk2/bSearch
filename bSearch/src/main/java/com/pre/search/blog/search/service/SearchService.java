package com.pre.search.blog.search.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pre.search.blog.common.code.CommonCode;
import com.pre.search.blog.common.util.ValidateUtil;
import com.pre.search.blog.common.vo.QueryVO;
import com.pre.search.blog.common.vo.ResponseVO;
import com.pre.search.blog.datasource.service.DatasourceCacheService;
import com.pre.search.blog.datasource.vo.DatasourceHeaderVO;
import com.pre.search.blog.datasource.vo.DatasourceVO;
import com.pre.search.blog.keyword.service.KeywordService;
import com.pre.search.blog.search.vo.SearchResultVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 검색 서비스
 * @author 이민우
 *
 */
@Service
@Slf4j
public class SearchService {
	
	@Autowired ObjectMapper objectMapper;
	@Autowired DatasourceCacheService datasourceCacheService;
	@Autowired KeywordService keywordService;
	
	private static String SUCCESS_SEARCH = "Blog searched successfully";
	private static String NO_DATASOURCE_EXISTS = "Datasource is not exists. You should make datasource first.";
	private static String NOT_VALID_DATA = "Query is not valid";
	private static String INTERNAL_ERROR = "Some error occured";
	
	
	/**
	 * 검색 진행
	 * 
	 * @param queryVO
	 * @return
	 */
	public ResponseVO search(QueryVO queryVO) {
		ResponseVO response = new ResponseVO();
		response.setStatus(HttpStatus.OK);
		response.setMessage(SUCCESS_SEARCH);
		
		log.info("@@ SearchService.search started : {}", queryVO.toString());
		
		if(!checkValidQueryVO(queryVO)) {
			// 잘못된 쿼리가 전달됨 : 400
			log.warn("@@ SearchService.search failed : {}", NOT_VALID_DATA);
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessage(NOT_VALID_DATA);
			
			return response;
		}

		try {
			
			/*
			 * 확장성을 고려했을 때 안은 두 가지가 나옴.
			 * 
			 * 1. query를 지정하고 해당 query에 해당되는 datasource에서만 데이터를 가져오는 것.
			 * 2. 저장된 모든 datasource에서 데이터를 가져오는 것.
			 * 
			 * 확장성이라면 2번이 당연히 나은 안이지만, "정확도"을 기준으로 SORT를 해야하는 문제가 있음.
			 * "최신순"이 기준이라면 SearchResult를 Comparable로 설정한 후 모든 datasource에서 가져온 정보를 SORT하면 되지만,
			 * API에서 정확도에 대한 수치를 명확히 주지 않아 현실적으로 2번 안은 현실적으로 불가능함.
			 * 
			 * 그러므로 1번 안을 채택해서 지정한 datasource에서만 검색 결과를 가져오도록 설정.
			 */
			

			// 캐시에서 데이터소스 정보들 전부 불러오기
			DatasourceVO datasourceVO = datasourceCacheService.getDatasource(datasourceCacheService.getDatasourceList(), queryVO);
			if(datasourceVO == null) {
				// 존재하지 않는 데이터소스 로드 : 400
				log.warn("@@ SearchService.search failed : {}", NO_DATASOURCE_EXISTS);
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessage(NO_DATASOURCE_EXISTS);
				
				return response;
			}
			
			// 로그 출력
			log.info("@@ SearchService.search make connection to : {}", datasourceVO.toString());
				
			// datasource의 정보와 QueryParamete를 토대로 통신 대상 URL 만들기.
			StringBuffer targetUrl = new StringBuffer();
			targetUrl.append(datasourceVO.getProtocol()); // http
			targetUrl.append("://").append(datasourceVO.getHost()); // http://www.test.com
			// port는 nullable이므로 확인 후 추가
			if (datasourceVO.getPort() != null && 0 < datasourceVO.getPort()) {
				targetUrl.append(":").append(datasourceVO.getPort()); // http://www.test.com:8080
			}
			// PATH 추가
			if (ValidateUtil.isValidString(datasourceVO.getPath())) {
				targetUrl.append(datasourceVO.getPath());
			}
			// RQEUIRED이기 때문에 ? 를 여기에 삽입
			if (ValidateUtil.isValidString(queryVO.getQuery())
					&& datasourceVO.getRequestColumn(CommonCode.QUERY) != null) {

				targetUrl.append("?").append(datasourceVO.getRequestColumn(CommonCode.QUERY)) // 해당 datasource의 query
																								// 파라미터 호출
						.append("=").append(queryVO.getQuery());

			}
			// SORT 수행
			if (ValidateUtil.isValidString(queryVO.getSort())
					&& datasourceVO.getRequestColumn(CommonCode.SORT) != null) {

				String sortBy = null;
				// SORT BY ACCURACY일 경우
				if (queryVO.getSort().equals(CommonCode.SORT_ACC)
						&& null != datasourceVO.getRequestColumn(CommonCode.SORT_ACC)) {
					sortBy = datasourceVO.getRequestColumn(CommonCode.SORT_ACC);
				} 
				// SORT BY RECENCEY일 경우
				else if (queryVO.getSort().equals(CommonCode.SORT_REC)
						&& null != datasourceVO.getRequestColumn(CommonCode.SORT_REC)) {
					sortBy = datasourceVO.getRequestColumn(CommonCode.SORT_REC);
				}

				if (sortBy != null) {
					targetUrl.append("&").append(datasourceVO.getRequestColumn(CommonCode.SORT)).append("=")
							.append(sortBy);
				}
			}
			
			// OFFSET & LIMIT 지정
			if (ValidateUtil.isValidInteger(queryVO.getOffset())
					&& null != datasourceVO.getRequestColumn(CommonCode.OFFSET)) {
				
				// OFFSET_MAX, OFFSET_MIN이 존재하면 유효성 검증 선수행
				Boolean isValidOffset = true;
				if(ValidateUtil.isValidInteger(datasourceVO.getOffsetMax())) {
					if(queryVO.getOffset() > datasourceVO.getOffsetMax()) {
						isValidOffset = false;
					}
				}
				if(isValidOffset && ValidateUtil.isValidInteger(datasourceVO.getOffsetMin())) {
					if(queryVO.getOffset() < datasourceVO.getOffsetMin()) {
						isValidOffset = false;
					}
				}
				
				if(!isValidOffset) {
					response.setStatus(HttpStatus.BAD_REQUEST);
					response.setMessage(NOT_VALID_DATA);
					
					return response;
				}
				
				targetUrl.append("&").append(datasourceVO.getRequestColumn(CommonCode.OFFSET)).append("=")
						.append(queryVO.getOffset());
			}
			if (ValidateUtil.isValidInteger(queryVO.getLimit())
					&& null != datasourceVO.getRequestColumn(CommonCode.LIMIT)) {

				// LIMIT_MAX, LIMIT_MIN이 존재하면 유효성 검증 선수행
				Boolean isValidLimit = true;
				if(ValidateUtil.isValidInteger(datasourceVO.getLimitMax())) {
					if(queryVO.getLimit() > datasourceVO.getLimitMax()) {
						isValidLimit = false;
					}
				}
				if(isValidLimit && ValidateUtil.isValidInteger(datasourceVO.getLimitMin())) {
					if(queryVO.getLimit() < datasourceVO.getLimitMin()) {
						isValidLimit = false;
					}
				}
				
				if(!isValidLimit) {
					response.setStatus(HttpStatus.BAD_REQUEST);
					response.setMessage(NOT_VALID_DATA);
					
					return response;
				}
				
				targetUrl.append("&").append(datasourceVO.getRequestColumn(CommonCode.LIMIT)).append("=")
						.append(queryVO.getLimit());
			}

			// Headers 설정 
			// 사전에 저장해둔 Header를 key:value로 삽입
			HttpHeaders headers = new HttpHeaders();
			if(!ValidateUtil.isEmptyData(datasourceVO.getRequestHeaders())) {
				for(DatasourceHeaderVO header : datasourceVO.getRequestHeaders()) {
					headers.add(header.getHeader(), header.getVal());
				}
			}

			// 통신 수행
			// GET, POST 두 가지 경우의 수가 있으나, 사실상 GET만 사용하게 될 것이므로 HttpBody 전송은 고려하지 않음.
			JsonNode httpResult = sendHttpProtocol(targetUrl.toString(), datasourceVO.getMethod(), headers);
			
			log.info("@@ SearchService.search connected datasource successfully : {}", httpResult.toString());

			// 사용자에게 반환할 검색 결과 생성
			SearchResultVO searchResult = new SearchResultVO(httpResult, datasourceVO);
			
			response.setData(searchResult);
			
			// 검색에 사용한 키워드 저장.
			// 어차피 GET /search 로 들어오는 입력의 키워드만 저장하면 되므로, 굳이 AOP를 걸지 말고 여기서 키워드 저장
			String splitBase = " ";
			// encode 되어있으면 " " > "%20"
			if(queryVO.getQuery().contains("%20")) {
				splitBase = "%20";
			}
			String[] keyword = queryVO.getQuery().split(splitBase);
			for(String word : keyword) {
				keywordService.save(word);
			}
			
			
		}
		catch(RestClientException e) {
			// 단순 연결 실패이므로 500 (아래거랑 합쳐도 될듯.)
			log.warn("@@ SearchService.search failed : {}", e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage(INTERNAL_ERROR);
		}
		catch(Exception e) {
			// 알수 없는 에러이므로 500
			log.warn("@@ SearchService.search failed : {}", e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage(INTERNAL_ERROR);
		}
		
		
		return response;
	}
	
	/**
	 * QueryVO 유효성 검사
	 * 
	 * @param queryVO
	 * @return
	 */
	public Boolean checkValidQueryVO(QueryVO queryVO) {
	
		if(ValidateUtil.isEmptyData(queryVO)) {
			return false;
		}
		// REQUIRED 항목 검사
		if(!ValidateUtil.isValidString(queryVO.getDatasourceId())) {
			return false;
		}
		if(!ValidateUtil.isValidString(queryVO.getQuery())) {
			return false;
		}
		
		// NOT-REQUIRED 항목 검사
		// CommonCode는 acc, rec 중 하나.
		if(!ValidateUtil.isValidString(queryVO.getSort())) {
			if(!queryVO.getSort().equals(CommonCode.SORT_ACC) && !queryVO.getSort().equals(CommonCode.SORT_REC)) {
				return false;
			}
		}
		// LIMIT은 0이 될 수 없음.
		if(queryVO.getLimit() != null && (!ValidateUtil.isValidInteger(queryVO.getLimit()) || queryVO.getLimit() <= 0)) {
			return false;
		}
		// OFFSET도 0이 될 수 없음.
		if(queryVO.getOffset() != null && (!ValidateUtil.isValidInteger(queryVO.getOffset()) || queryVO.getOffset() <= 0)) {
			return false;
		}
		
		
		return true;
	}

	/**
	 * HTTP 통신 함수
	 * 
	 * @param targetUrl 통신할 주소 (Query Parameter가 포함된 상태)
	 * @param httpMethod GET or POST
	 * @param authorization 인증키 (없으면 NULL)
	 * @return
	 */
	public JsonNode sendHttpProtocol(String targetUrl, String httpMethod, HttpHeaders headers) {

		log.info("@@ Send HTTP Request. targetUri={}, httpMethod={}, Headers={}", targetUrl,
				httpMethod.toString(), headers);

		// http 메소드 설정
		HttpMethod method = HttpMethod.GET;
		if(httpMethod.toUpperCase().equals("POST")) {
			method = HttpMethod.POST;
		}
		
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<JsonNode> response = restTemplate.exchange(targetUrl, method, new HttpEntity(headers), JsonNode.class);
		JsonNode map = response.getBody();
		
		return map;
		
	}
}
