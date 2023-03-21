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
 * �˻� ����
 * @author �̹ο�
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
	 * �˻� ����
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
			// �߸��� ������ ���޵� : 400
			log.warn("@@ SearchService.search failed : {}", NOT_VALID_DATA);
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessage(NOT_VALID_DATA);
			
			return response;
		}

		try {
			
			/*
			 * Ȯ�强�� ������� �� ���� �� ������ ����.
			 * 
			 * 1. query�� �����ϰ� �ش� query�� �ش�Ǵ� datasource������ �����͸� �������� ��.
			 * 2. ����� ��� datasource���� �����͸� �������� ��.
			 * 
			 * Ȯ�强�̶�� 2���� �翬�� ���� ��������, "��Ȯ��"�� �������� SORT�� �ؾ��ϴ� ������ ����.
			 * "�ֽż�"�� �����̶�� SearchResult�� Comparable�� ������ �� ��� datasource���� ������ ������ SORT�ϸ� ������,
			 * API���� ��Ȯ���� ���� ��ġ�� ��Ȯ�� ���� �ʾ� ���������� 2�� ���� ���������� �Ұ�����.
			 * 
			 * �׷��Ƿ� 1�� ���� ä���ؼ� ������ datasource������ �˻� ����� ���������� ����.
			 */
			

			// ĳ�ÿ��� �����ͼҽ� ������ ���� �ҷ�����
			DatasourceVO datasourceVO = datasourceCacheService.getDatasource(datasourceCacheService.getDatasourceList(), queryVO);
			if(datasourceVO == null) {
				// �������� �ʴ� �����ͼҽ� �ε� : 400
				log.warn("@@ SearchService.search failed : {}", NO_DATASOURCE_EXISTS);
				response.setStatus(HttpStatus.BAD_REQUEST);
				response.setMessage(NO_DATASOURCE_EXISTS);
				
				return response;
			}
			
			// �α� ���
			log.info("@@ SearchService.search make connection to : {}", datasourceVO.toString());
				
			// datasource�� ������ QueryParamete�� ���� ��� ��� URL �����.
			StringBuffer targetUrl = new StringBuffer();
			targetUrl.append(datasourceVO.getProtocol()); // http
			targetUrl.append("://").append(datasourceVO.getHost()); // http://www.test.com
			// port�� nullable�̹Ƿ� Ȯ�� �� �߰�
			if (datasourceVO.getPort() != null && 0 < datasourceVO.getPort()) {
				targetUrl.append(":").append(datasourceVO.getPort()); // http://www.test.com:8080
			}
			// PATH �߰�
			if (ValidateUtil.isValidString(datasourceVO.getPath())) {
				targetUrl.append(datasourceVO.getPath());
			}
			// RQEUIRED�̱� ������ ? �� ���⿡ ����
			if (ValidateUtil.isValidString(queryVO.getQuery())
					&& datasourceVO.getRequestColumn(CommonCode.QUERY) != null) {

				targetUrl.append("?").append(datasourceVO.getRequestColumn(CommonCode.QUERY)) // �ش� datasource�� query
																								// �Ķ���� ȣ��
						.append("=").append(queryVO.getQuery());

			}
			// SORT ����
			if (ValidateUtil.isValidString(queryVO.getSort())
					&& datasourceVO.getRequestColumn(CommonCode.SORT) != null) {

				String sortBy = null;
				// SORT BY ACCURACY�� ���
				if (queryVO.getSort().equals(CommonCode.SORT_ACC)
						&& null != datasourceVO.getRequestColumn(CommonCode.SORT_ACC)) {
					sortBy = datasourceVO.getRequestColumn(CommonCode.SORT_ACC);
				} 
				// SORT BY RECENCEY�� ���
				else if (queryVO.getSort().equals(CommonCode.SORT_REC)
						&& null != datasourceVO.getRequestColumn(CommonCode.SORT_REC)) {
					sortBy = datasourceVO.getRequestColumn(CommonCode.SORT_REC);
				}

				if (sortBy != null) {
					targetUrl.append("&").append(datasourceVO.getRequestColumn(CommonCode.SORT)).append("=")
							.append(sortBy);
				}
			}
			
			// OFFSET & LIMIT ����
			if (ValidateUtil.isValidInteger(queryVO.getOffset())
					&& null != datasourceVO.getRequestColumn(CommonCode.OFFSET)) {
				
				// OFFSET_MAX, OFFSET_MIN�� �����ϸ� ��ȿ�� ���� ������
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

				// LIMIT_MAX, LIMIT_MIN�� �����ϸ� ��ȿ�� ���� ������
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

			// Headers ���� 
			// ������ �����ص� Header�� key:value�� ����
			HttpHeaders headers = new HttpHeaders();
			if(!ValidateUtil.isEmptyData(datasourceVO.getRequestHeaders())) {
				for(DatasourceHeaderVO header : datasourceVO.getRequestHeaders()) {
					headers.add(header.getHeader(), header.getVal());
				}
			}

			// ��� ����
			// GET, POST �� ���� ����� ���� ������, ��ǻ� GET�� ����ϰ� �� ���̹Ƿ� HttpBody ������ ������� ����.
			JsonNode httpResult = sendHttpProtocol(targetUrl.toString(), datasourceVO.getMethod(), headers);
			
			log.info("@@ SearchService.search connected datasource successfully : {}", httpResult.toString());

			// ����ڿ��� ��ȯ�� �˻� ��� ����
			SearchResultVO searchResult = new SearchResultVO(httpResult, datasourceVO);
			
			response.setData(searchResult);
			
			// �˻��� ����� Ű���� ����.
			// ������ GET /search �� ������ �Է��� Ű���常 �����ϸ� �ǹǷ�, ���� AOP�� ���� ���� ���⼭ Ű���� ����
			String splitBase = " ";
			// encode �Ǿ������� " " > "%20"
			if(queryVO.getQuery().contains("%20")) {
				splitBase = "%20";
			}
			String[] keyword = queryVO.getQuery().split(splitBase);
			for(String word : keyword) {
				keywordService.save(word);
			}
			
			
		}
		catch(RestClientException e) {
			// �ܼ� ���� �����̹Ƿ� 500 (�Ʒ��Ŷ� ���ĵ� �ɵ�.)
			log.warn("@@ SearchService.search failed : {}", e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage(INTERNAL_ERROR);
		}
		catch(Exception e) {
			// �˼� ���� �����̹Ƿ� 500
			log.warn("@@ SearchService.search failed : {}", e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage(INTERNAL_ERROR);
		}
		
		
		return response;
	}
	
	/**
	 * QueryVO ��ȿ�� �˻�
	 * 
	 * @param queryVO
	 * @return
	 */
	public Boolean checkValidQueryVO(QueryVO queryVO) {
	
		if(ValidateUtil.isEmptyData(queryVO)) {
			return false;
		}
		// REQUIRED �׸� �˻�
		if(!ValidateUtil.isValidString(queryVO.getDatasourceId())) {
			return false;
		}
		if(!ValidateUtil.isValidString(queryVO.getQuery())) {
			return false;
		}
		
		// NOT-REQUIRED �׸� �˻�
		// CommonCode�� acc, rec �� �ϳ�.
		if(!ValidateUtil.isValidString(queryVO.getSort())) {
			if(!queryVO.getSort().equals(CommonCode.SORT_ACC) && !queryVO.getSort().equals(CommonCode.SORT_REC)) {
				return false;
			}
		}
		// LIMIT�� 0�� �� �� ����.
		if(queryVO.getLimit() != null && (!ValidateUtil.isValidInteger(queryVO.getLimit()) || queryVO.getLimit() <= 0)) {
			return false;
		}
		// OFFSET�� 0�� �� �� ����.
		if(queryVO.getOffset() != null && (!ValidateUtil.isValidInteger(queryVO.getOffset()) || queryVO.getOffset() <= 0)) {
			return false;
		}
		
		
		return true;
	}

	/**
	 * HTTP ��� �Լ�
	 * 
	 * @param targetUrl ����� �ּ� (Query Parameter�� ���Ե� ����)
	 * @param httpMethod GET or POST
	 * @param authorization ����Ű (������ NULL)
	 * @return
	 */
	public JsonNode sendHttpProtocol(String targetUrl, String httpMethod, HttpHeaders headers) {

		log.info("@@ Send HTTP Request. targetUri={}, httpMethod={}, Headers={}", targetUrl,
				httpMethod.toString(), headers);

		// http �޼ҵ� ����
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
