package com.pre.search.blog.datasource.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.pre.search.blog.common.util.ValidateUtil;
import com.pre.search.blog.common.vo.QueryVO;
import com.pre.search.blog.common.vo.ResponseVO;
import com.pre.search.blog.datasource.repository.DatasourceHeaderRepository;
import com.pre.search.blog.datasource.repository.DatasourceRepository;
import com.pre.search.blog.datasource.repository.DatasourceReqParamRepository;
import com.pre.search.blog.datasource.repository.DatasourceResParamRepository;
import com.pre.search.blog.datasource.vo.Datasource;
import com.pre.search.blog.datasource.vo.DatasourceHeader;
import com.pre.search.blog.datasource.vo.DatasourceHeaderVO;
import com.pre.search.blog.datasource.vo.DatasourceRequestParam;
import com.pre.search.blog.datasource.vo.DatasourceRequestParamVO;
import com.pre.search.blog.datasource.vo.DatasourceResponseParam;
import com.pre.search.blog.datasource.vo.DatasourceResponseParamVO;
import com.pre.search.blog.datasource.vo.DatasourceVO;

import lombok.extern.slf4j.Slf4j;

/**
 * datasource 관련 비즈니스 로직을 담당하는 서비스 클래스
 * @author 이민우
 *
 */
@Slf4j
@Service
public class DatasourceService {
	@Autowired DatasourceRepository datasourceRepository;
	@Autowired DatasourceReqParamRepository datasourceReqParamRepository;
	@Autowired DatasourceResParamRepository datasourceResParamRepository;
	@Autowired DatasourceHeaderRepository datasourceHeaderRepository;
	@Autowired DatasourceCacheService datasourceCacheService;
	
	private static List<String> validateProtocols = Arrays.asList(new String[] {"http", "https"});
	private static List<String> validateHttpMethods = Arrays.asList(new String[] {"GET", "POST"});

	// 사용자에게 반환될 메시지 사전정의
	private static String NOT_VALID_DATA = "Datasource resource is not valid";
	private static String ALREADY_EXISTS = "Datasource resource is already existed";
	private static String NOT_EXISTS = "Datasource resource is not exists";
	private static String INTERNAL_ERROR = "Some error occured";
	private static String SUCCESS_CREATE = "Datasource resource created successfully";
	private static String SUCCESS_UPDATE = "Datasource resource updated successfully";
	private static String SUCCESS_DELETE = "Datasource resource deleted successfully";
	private static String SUCCESS_SELECT = "Datasource resource loaded successfully";

	/**
	 * 입력받은 datasource 데이터의 유효성 확인
	 * 
	 * @param datasource
	 * @return 유효한 datasource일 경우 true 반환
	 */
	public boolean isValidDatasource(DatasourceVO datasource) {
		
		// object 자체 유효성 검사
		if(ValidateUtil.isEmptyData(datasource)) {
			return false;
		}
		// id 유효성 검사
		if(!ValidateUtil.isValidString(datasource.getId())) {
			return false;
		}
		// protocol 유효성 검사
		if (!ValidateUtil.isValidString(datasource.getProtocol())) {
			return false;
		}
		else {
			Boolean isValidProtocol = false;
			// String이라 contains가 미작동해서 따로 구현 : http/https만 받도록 설정
			for(String protocol : validateProtocols) {
				if(protocol.equals(datasource.getProtocol())) {
					isValidProtocol = true;
					break;
				}
			}
			if(!isValidProtocol) {
				return isValidProtocol;
			}
		}
		// 호스트 유효성 검사
		if(!ValidateUtil.isValidString(datasource.getHost())) {
			return false;
		}
		// HTTP METHOD 유효성 검사
		if(!ValidateUtil.isValidString(datasource.getMethod())) {
			return false;
		}
		else {
			Boolean isValidMethod = false;
			// String이라 contains가 미작동해서 따로 구현 : GET/POST만 받도록 설정
			for(String method : validateHttpMethods) {
				if(method.equals(datasource.getMethod())) {
					isValidMethod = true;
					break;
				}
			}
			if(!isValidMethod) {
				return isValidMethod;
			}
		}
		// 포트 유효성 검사
		if(!ValidateUtil.isEmptyData(datasource.getPort()) && !ValidateUtil.isValidInteger(datasource.getPort())) {
			return false;
		}
		// PATH 유효성 검사 (NULLABLE)
		if(!ValidateUtil.isEmptyData(datasource.getPath()) && !ValidateUtil.isValidString(datasource.getPath()) && 
				datasource.getPath().startsWith("/")) {
			return false;
		}
		// RequestParams 유효성 검사
		if(ValidateUtil.isEmptyList(datasource.getRequestParams())) {
			return false;
		}
		else {
			for(DatasourceRequestParamVO param : datasource.getRequestParams()) {
				if(!ValidateUtil.isValidString(param.getColumn())) {
					return false;
				}
				if(!ValidateUtil.isValidString(param.getSourceColumn())) {
					return false;
				}
			}
		}
		// ResponseParams 유효성 검사
		if(ValidateUtil.isEmptyList(datasource.getResponseParams())) {
			return false;
		}
		else {
			for(DatasourceResponseParamVO param : datasource.getResponseParams()) {
				if(!ValidateUtil.isValidString(param.getColumn())) {
					return false;
				}
				if(!ValidateUtil.isValidString(param.getSourceColumn())) {
					return false;
				}
			}
		}
		
		// RequestHeaders 유효성 검사
		if(!ValidateUtil.isEmptyList(datasource.getRequestHeaders())) {
			for(DatasourceHeaderVO header : datasource.getRequestHeaders()) {
				if(!ValidateUtil.isValidString(header.getHeader())) {
					return false;
				}
				if(!ValidateUtil.isValidString(header.getVal())) {
					return false;
				}
			}
		}
		
		// OFFSET MIN/MAX 검증
		if(datasource.getOffsetMax() != null) {
			if(!ValidateUtil.isValidInteger(datasource.getOffsetMax())) {
				return false;
			}
		}
		if(datasource.getOffsetMin() != null) {
			if(!ValidateUtil.isValidInteger(datasource.getOffsetMin())) {
				return false;
			}
		}
		// LIMIT MIN/MAX 검증
		if(datasource.getLimitMax() != null) {
			if(!ValidateUtil.isValidInteger(datasource.getLimitMax())) {
				return false;
			}
		}
		if(datasource.getLimitMin() != null) {
			if(!ValidateUtil.isValidInteger(datasource.getLimitMin())) {
				return false;
			}
		}
		
		// 이상없음
		return true;
	}

	/**
	 * datasource create 수행
	 * 
	 * @param datasource
	 * @return ResponseVO HttpStatus, 결과 Message, 결과물 출력
	 */
	public ResponseVO create(DatasourceVO datasourceVO) {
		
		// 반환할 response 메시지 설정
		ResponseVO response = new ResponseVO();
		response.setStatus(HttpStatus.CREATED);
		response.setMessage(SUCCESS_CREATE);
		
		log.info("@@ DatasourceService.create started : {}", datasourceVO.toString());
		
		// 유효하지 않은 데이터를 입력받은 상태면 400
		if(!isValidDatasource(datasourceVO)) {
			log.warn("@@ DatasourceService.create failed : {}", NOT_VALID_DATA);
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessage(NOT_VALID_DATA);
			
			return response;
		}
		
		try {
			// upsert가 아닌 create이기에 기존 데이터 존재여부 확인
			// 존재한다면 409 반환
			if(datasourceRepository.existsById(datasourceVO.getId())) {
				log.warn("@@ DatasourceService.create failed : {}", ALREADY_EXISTS);
				response.setStatus(HttpStatus.CONFLICT);
				response.setMessage(ALREADY_EXISTS);
			}
			
			// 존재하지 않는다면 CREATE 수행
			else {
				Datasource datasource = new Datasource(datasourceVO);
				datasourceRepository.save(datasource);

				// request 쿼리 파라미터 저장
				for(DatasourceRequestParamVO reqParamVO : datasourceVO.getRequestParams()) {
					DatasourceRequestParam reqParam = new DatasourceRequestParam(reqParamVO, datasource);
					datasourceReqParamRepository.save(reqParam);
				}
				// response 키값 저장
				for(DatasourceResponseParamVO resParamVO : datasourceVO.getResponseParams()) {
					DatasourceResponseParam resParam = new DatasourceResponseParam(resParamVO, datasource);
					datasourceResParamRepository.save(resParam);
				}
				// request header 저장
				if(!ValidateUtil.isEmptyList(datasourceVO.getRequestHeaders())) {
					for(DatasourceHeaderVO headerVO : datasourceVO.getRequestHeaders()) {
						DatasourceHeader header = new DatasourceHeader(headerVO, datasource);
						datasourceHeaderRepository.save(header);
					}
				}
				
				response.setData(datasourceVO);
				log.info("@@ DatasourceService.create success : {}", SUCCESS_CREATE);
				
				// 캐시된 데이터에서도 추가
				datasourceCacheService.addDatasourceCache(datasourceCacheService.getDatasourceList(), datasourceVO);
			}
			
		}
		catch(Exception e) {
			// DB 에러 500 반환
			log.error("@@ DatasourceService.create failed : {}", e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage(INTERNAL_ERROR);
		}

		
		return response;
	}
	
	/**
	 * datasource 전체 조회
	 * 
	 * @param queryVO host와 port 기반 조회 지원
	 * @return
	 */
	public ResponseVO selectAll(QueryVO queryVO) {

		// 반환할 response 메시지 설정
		ResponseVO response = new ResponseVO();
		response.setStatus(HttpStatus.OK);
		response.setMessage(SUCCESS_SELECT);
		
		log.info("@@ DatasourceService.selectAll started : {}", queryVO.toString());
		
		try {
			// CACHE에 저장되어 있으니, 굳이 DB 탐색하지 말고 캐시에서 탐색.
			List<DatasourceVO> datasourceVOList = datasourceCacheService.getDatasourceList(datasourceCacheService.getDatasourceList(), queryVO);
			
			if(datasourceVOList == null) {
				// 빈 배열 반환용 new 선언
				datasourceVOList = new ArrayList<>();
			}
			
			response.setData(datasourceVOList);
		}
		catch(Exception e) {
			// 캐시 에러 500 반환
			log.error("@@ DatasourceService.selectAll failed : {}", e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage(INTERNAL_ERROR);
		}
		
		
		return response;
	}
	
	/**
	 * 특정 id릐 datasource 조회
	 * 
	 * @param id
	 * @return
	 */
	public ResponseVO selectOne(QueryVO queryVO) {

		// 반환할 response 메시지 설정
		ResponseVO response = new ResponseVO();
		response.setStatus(HttpStatus.OK);
		response.setMessage(SUCCESS_SELECT);
		
		log.info("@@ DatasourceService.selectOne started : {}", queryVO);
		
		try {
			// CACHE에 저장되어 있으니, 굳이 DB 탐색하지 말고 캐시에서 탐색.
			DatasourceVO datasourceList = datasourceCacheService.getDatasource(datasourceCacheService.getDatasourceList(), queryVO);
			
			response.setData(datasourceList);
		}
		catch(Exception e) {
			// 캐시 에러 500 반환
			log.error("@@ DatasourceService.selectOne failed : {}", e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage(INTERNAL_ERROR);
		}
		
		
		return response;
	}

	/**
	 * 특정 id를 가진 datasource 수정
	 * PUT 연산이므로 덮어씌워짐.
	 * 
	 * @param datasourceVO
	 * @return
	 */
	public ResponseVO update(DatasourceVO datasourceVO) {
		
		// 반환할 response 메시지 설정
		ResponseVO response = new ResponseVO();
		response.setStatus(HttpStatus.CREATED);
		response.setMessage(SUCCESS_UPDATE);
		
		log.info("@@ DatasourceService.update started : {}", datasourceVO.toString());
		
		// 유효하지 않은 데이터를 입력받은 상태면 400
		if(!isValidDatasource(datasourceVO)) {
			log.warn("@@ DatasourceService.update failed : {}", NOT_VALID_DATA);
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessage(NOT_VALID_DATA);
			
			return response;
		}
		
		try {
			// upsert가 아닌 UPDATE이기에 기존 데이터 존재여부 확인
			// 존재하지 않는다면 409 반환
			if(!datasourceRepository.existsById(datasourceVO.getId())) {
				log.warn("@@ DatasourceService.update failed : {}", NOT_EXISTS);
				response.setStatus(HttpStatus.NOT_FOUND);
				response.setMessage(NOT_EXISTS);
			}
			
			// 존재한다면 UPDATE 수행
			else {
				Datasource updatedDataSource = new Datasource(datasourceVO);
				datasourceRepository.save(updatedDataSource);
				
				// PUT이기 때문에 기존 데이터 삭제
				if(datasourceReqParamRepository.existsByDatasource(updatedDataSource)) {
					datasourceReqParamRepository.deleteByDatasource(updatedDataSource);
				}
				if(datasourceResParamRepository.existsByDatasource(updatedDataSource)) {
					datasourceResParamRepository.deleteByDatasource(updatedDataSource);
				}
				if(datasourceHeaderRepository.existsByDatasource(updatedDataSource)) {
					datasourceHeaderRepository.deleteByDatasource(updatedDataSource);
				}

				// 삭제 되었으면 재삽입
				// request 쿼리 파라미터 저장
				for(DatasourceRequestParamVO reqParamVO : datasourceVO.getRequestParams()) {
					DatasourceRequestParam reqParam = new DatasourceRequestParam(reqParamVO, updatedDataSource);
					datasourceReqParamRepository.save(reqParam);
				}
				// response 키값 저장
				for(DatasourceResponseParamVO resParamVO : datasourceVO.getResponseParams()) {
					DatasourceResponseParam resParam = new DatasourceResponseParam(resParamVO, updatedDataSource);
					datasourceResParamRepository.save(resParam);
				}
				// request header 저장
				if(!ValidateUtil.isEmptyList(datasourceVO.getRequestHeaders())) {
					for(DatasourceHeaderVO headerVO : datasourceVO.getRequestHeaders()) {
						DatasourceHeader header = new DatasourceHeader(headerVO, updatedDataSource);
						datasourceHeaderRepository.save(header);
					}
				}
				
				response.setData(datasourceVO);
				log.info("@@ DatasourceService.update success : {}", SUCCESS_UPDATE);

				// 캐시된 데이터에서도 업데이트
				datasourceCacheService.modDatasourceCache(datasourceCacheService.getDatasourceList(), datasourceVO);
			}
			
		}
		catch(Exception e) {
			// DB 에러 500 반환
			log.error("@@ DatasourceService.update failed : {}", e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage(INTERNAL_ERROR);
		}

		return response;
	}

	/**
	 * 특정 id를 가진 datasource 삭제
	 * 
	 * @param id
	 * @return
	 */
	public ResponseVO delete(String id) {

		// 반환할 response 메시지 설정
		ResponseVO response = new ResponseVO();
		response.setStatus(HttpStatus.NO_CONTENT);
		response.setMessage(SUCCESS_DELETE);
		
		log.info("@@ DatasourceService.delete started : {}", id);
		
		try {
			// 삭제에 앞서 존재하는지 확인 후 존재하지 않는다면 에러 반환
			if(!datasourceRepository.existsById(id)) {
				log.warn("@@ DatasourceService.delete failed : {}", NOT_EXISTS);
				response.setStatus(HttpStatus.NOT_FOUND);
				response.setMessage(NOT_EXISTS);
			}
			// 존재한다면 삭제
			else {
				Datasource datasource = datasourceRepository.findById(id).orElse(null);
				
				if(datasourceReqParamRepository.existsByDatasource(datasource)) {
					datasourceReqParamRepository.deleteByDatasource(datasource);
				}
				if(datasourceResParamRepository.existsByDatasource(datasource)) {
					datasourceResParamRepository.deleteByDatasource(datasource);
				}
				if(datasourceHeaderRepository.existsByDatasource(datasource)) {
					datasourceHeaderRepository.deleteByDatasource(datasource);
				}
				
				datasourceRepository.delete(datasource);
				
				log.info("@@ DatasourceService.delete success : {}", SUCCESS_DELETE);

				// 캐시된 데이터에서도 삭제
				datasourceCacheService.delDatasourceCache(datasourceCacheService.getDatasourceList(), id);
			}
			
		}
		catch(Exception e) {
			// DB 에러 500 반환
			log.error("@@ DatasourceService.delete failed : {}", e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage(INTERNAL_ERROR);
		}
		
		
		return response;
	}
	
}
