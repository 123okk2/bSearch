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
 * datasource ���� ����Ͻ� ������ ����ϴ� ���� Ŭ����
 * @author �̹ο�
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

	// ����ڿ��� ��ȯ�� �޽��� ��������
	private static String NOT_VALID_DATA = "Datasource resource is not valid";
	private static String ALREADY_EXISTS = "Datasource resource is already existed";
	private static String NOT_EXISTS = "Datasource resource is not exists";
	private static String INTERNAL_ERROR = "Some error occured";
	private static String SUCCESS_CREATE = "Datasource resource created successfully";
	private static String SUCCESS_UPDATE = "Datasource resource updated successfully";
	private static String SUCCESS_DELETE = "Datasource resource deleted successfully";
	private static String SUCCESS_SELECT = "Datasource resource loaded successfully";

	/**
	 * �Է¹��� datasource �������� ��ȿ�� Ȯ��
	 * 
	 * @param datasource
	 * @return ��ȿ�� datasource�� ��� true ��ȯ
	 */
	public boolean isValidDatasource(DatasourceVO datasource) {
		
		// object ��ü ��ȿ�� �˻�
		if(ValidateUtil.isEmptyData(datasource)) {
			return false;
		}
		// id ��ȿ�� �˻�
		if(!ValidateUtil.isValidString(datasource.getId())) {
			return false;
		}
		// protocol ��ȿ�� �˻�
		if (!ValidateUtil.isValidString(datasource.getProtocol())) {
			return false;
		}
		else {
			Boolean isValidProtocol = false;
			// String�̶� contains�� ���۵��ؼ� ���� ���� : http/https�� �޵��� ����
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
		// ȣ��Ʈ ��ȿ�� �˻�
		if(!ValidateUtil.isValidString(datasource.getHost())) {
			return false;
		}
		// HTTP METHOD ��ȿ�� �˻�
		if(!ValidateUtil.isValidString(datasource.getMethod())) {
			return false;
		}
		else {
			Boolean isValidMethod = false;
			// String�̶� contains�� ���۵��ؼ� ���� ���� : GET/POST�� �޵��� ����
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
		// ��Ʈ ��ȿ�� �˻�
		if(!ValidateUtil.isEmptyData(datasource.getPort()) && !ValidateUtil.isValidInteger(datasource.getPort())) {
			return false;
		}
		// PATH ��ȿ�� �˻� (NULLABLE)
		if(!ValidateUtil.isEmptyData(datasource.getPath()) && !ValidateUtil.isValidString(datasource.getPath()) && 
				datasource.getPath().startsWith("/")) {
			return false;
		}
		// RequestParams ��ȿ�� �˻�
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
		// ResponseParams ��ȿ�� �˻�
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
		
		// RequestHeaders ��ȿ�� �˻�
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
		
		// OFFSET MIN/MAX ����
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
		// LIMIT MIN/MAX ����
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
		
		// �̻����
		return true;
	}

	/**
	 * datasource create ����
	 * 
	 * @param datasource
	 * @return ResponseVO HttpStatus, ��� Message, ����� ���
	 */
	public ResponseVO create(DatasourceVO datasourceVO) {
		
		// ��ȯ�� response �޽��� ����
		ResponseVO response = new ResponseVO();
		response.setStatus(HttpStatus.CREATED);
		response.setMessage(SUCCESS_CREATE);
		
		log.info("@@ DatasourceService.create started : {}", datasourceVO.toString());
		
		// ��ȿ���� ���� �����͸� �Է¹��� ���¸� 400
		if(!isValidDatasource(datasourceVO)) {
			log.warn("@@ DatasourceService.create failed : {}", NOT_VALID_DATA);
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessage(NOT_VALID_DATA);
			
			return response;
		}
		
		try {
			// upsert�� �ƴ� create�̱⿡ ���� ������ ���翩�� Ȯ��
			// �����Ѵٸ� 409 ��ȯ
			if(datasourceRepository.existsById(datasourceVO.getId())) {
				log.warn("@@ DatasourceService.create failed : {}", ALREADY_EXISTS);
				response.setStatus(HttpStatus.CONFLICT);
				response.setMessage(ALREADY_EXISTS);
			}
			
			// �������� �ʴ´ٸ� CREATE ����
			else {
				Datasource datasource = new Datasource(datasourceVO);
				datasourceRepository.save(datasource);

				// request ���� �Ķ���� ����
				for(DatasourceRequestParamVO reqParamVO : datasourceVO.getRequestParams()) {
					DatasourceRequestParam reqParam = new DatasourceRequestParam(reqParamVO, datasource);
					datasourceReqParamRepository.save(reqParam);
				}
				// response Ű�� ����
				for(DatasourceResponseParamVO resParamVO : datasourceVO.getResponseParams()) {
					DatasourceResponseParam resParam = new DatasourceResponseParam(resParamVO, datasource);
					datasourceResParamRepository.save(resParam);
				}
				// request header ����
				if(!ValidateUtil.isEmptyList(datasourceVO.getRequestHeaders())) {
					for(DatasourceHeaderVO headerVO : datasourceVO.getRequestHeaders()) {
						DatasourceHeader header = new DatasourceHeader(headerVO, datasource);
						datasourceHeaderRepository.save(header);
					}
				}
				
				response.setData(datasourceVO);
				log.info("@@ DatasourceService.create success : {}", SUCCESS_CREATE);
				
				// ĳ�õ� �����Ϳ����� �߰�
				datasourceCacheService.addDatasourceCache(datasourceCacheService.getDatasourceList(), datasourceVO);
			}
			
		}
		catch(Exception e) {
			// DB ���� 500 ��ȯ
			log.error("@@ DatasourceService.create failed : {}", e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage(INTERNAL_ERROR);
		}

		
		return response;
	}
	
	/**
	 * datasource ��ü ��ȸ
	 * 
	 * @param queryVO host�� port ��� ��ȸ ����
	 * @return
	 */
	public ResponseVO selectAll(QueryVO queryVO) {

		// ��ȯ�� response �޽��� ����
		ResponseVO response = new ResponseVO();
		response.setStatus(HttpStatus.OK);
		response.setMessage(SUCCESS_SELECT);
		
		log.info("@@ DatasourceService.selectAll started : {}", queryVO.toString());
		
		try {
			// CACHE�� ����Ǿ� ������, ���� DB Ž������ ���� ĳ�ÿ��� Ž��.
			List<DatasourceVO> datasourceVOList = datasourceCacheService.getDatasourceList(datasourceCacheService.getDatasourceList(), queryVO);
			
			if(datasourceVOList == null) {
				// �� �迭 ��ȯ�� new ����
				datasourceVOList = new ArrayList<>();
			}
			
			response.setData(datasourceVOList);
		}
		catch(Exception e) {
			// ĳ�� ���� 500 ��ȯ
			log.error("@@ DatasourceService.selectAll failed : {}", e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage(INTERNAL_ERROR);
		}
		
		
		return response;
	}
	
	/**
	 * Ư�� id�l datasource ��ȸ
	 * 
	 * @param id
	 * @return
	 */
	public ResponseVO selectOne(QueryVO queryVO) {

		// ��ȯ�� response �޽��� ����
		ResponseVO response = new ResponseVO();
		response.setStatus(HttpStatus.OK);
		response.setMessage(SUCCESS_SELECT);
		
		log.info("@@ DatasourceService.selectOne started : {}", queryVO);
		
		try {
			// CACHE�� ����Ǿ� ������, ���� DB Ž������ ���� ĳ�ÿ��� Ž��.
			DatasourceVO datasourceList = datasourceCacheService.getDatasource(datasourceCacheService.getDatasourceList(), queryVO);
			
			response.setData(datasourceList);
		}
		catch(Exception e) {
			// ĳ�� ���� 500 ��ȯ
			log.error("@@ DatasourceService.selectOne failed : {}", e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage(INTERNAL_ERROR);
		}
		
		
		return response;
	}

	/**
	 * Ư�� id�� ���� datasource ����
	 * PUT �����̹Ƿ� �������.
	 * 
	 * @param datasourceVO
	 * @return
	 */
	public ResponseVO update(DatasourceVO datasourceVO) {
		
		// ��ȯ�� response �޽��� ����
		ResponseVO response = new ResponseVO();
		response.setStatus(HttpStatus.CREATED);
		response.setMessage(SUCCESS_UPDATE);
		
		log.info("@@ DatasourceService.update started : {}", datasourceVO.toString());
		
		// ��ȿ���� ���� �����͸� �Է¹��� ���¸� 400
		if(!isValidDatasource(datasourceVO)) {
			log.warn("@@ DatasourceService.update failed : {}", NOT_VALID_DATA);
			response.setStatus(HttpStatus.BAD_REQUEST);
			response.setMessage(NOT_VALID_DATA);
			
			return response;
		}
		
		try {
			// upsert�� �ƴ� UPDATE�̱⿡ ���� ������ ���翩�� Ȯ��
			// �������� �ʴ´ٸ� 409 ��ȯ
			if(!datasourceRepository.existsById(datasourceVO.getId())) {
				log.warn("@@ DatasourceService.update failed : {}", NOT_EXISTS);
				response.setStatus(HttpStatus.NOT_FOUND);
				response.setMessage(NOT_EXISTS);
			}
			
			// �����Ѵٸ� UPDATE ����
			else {
				Datasource updatedDataSource = new Datasource(datasourceVO);
				datasourceRepository.save(updatedDataSource);
				
				// PUT�̱� ������ ���� ������ ����
				if(datasourceReqParamRepository.existsByDatasource(updatedDataSource)) {
					datasourceReqParamRepository.deleteByDatasource(updatedDataSource);
				}
				if(datasourceResParamRepository.existsByDatasource(updatedDataSource)) {
					datasourceResParamRepository.deleteByDatasource(updatedDataSource);
				}
				if(datasourceHeaderRepository.existsByDatasource(updatedDataSource)) {
					datasourceHeaderRepository.deleteByDatasource(updatedDataSource);
				}

				// ���� �Ǿ����� �����
				// request ���� �Ķ���� ����
				for(DatasourceRequestParamVO reqParamVO : datasourceVO.getRequestParams()) {
					DatasourceRequestParam reqParam = new DatasourceRequestParam(reqParamVO, updatedDataSource);
					datasourceReqParamRepository.save(reqParam);
				}
				// response Ű�� ����
				for(DatasourceResponseParamVO resParamVO : datasourceVO.getResponseParams()) {
					DatasourceResponseParam resParam = new DatasourceResponseParam(resParamVO, updatedDataSource);
					datasourceResParamRepository.save(resParam);
				}
				// request header ����
				if(!ValidateUtil.isEmptyList(datasourceVO.getRequestHeaders())) {
					for(DatasourceHeaderVO headerVO : datasourceVO.getRequestHeaders()) {
						DatasourceHeader header = new DatasourceHeader(headerVO, updatedDataSource);
						datasourceHeaderRepository.save(header);
					}
				}
				
				response.setData(datasourceVO);
				log.info("@@ DatasourceService.update success : {}", SUCCESS_UPDATE);

				// ĳ�õ� �����Ϳ����� ������Ʈ
				datasourceCacheService.modDatasourceCache(datasourceCacheService.getDatasourceList(), datasourceVO);
			}
			
		}
		catch(Exception e) {
			// DB ���� 500 ��ȯ
			log.error("@@ DatasourceService.update failed : {}", e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage(INTERNAL_ERROR);
		}

		return response;
	}

	/**
	 * Ư�� id�� ���� datasource ����
	 * 
	 * @param id
	 * @return
	 */
	public ResponseVO delete(String id) {

		// ��ȯ�� response �޽��� ����
		ResponseVO response = new ResponseVO();
		response.setStatus(HttpStatus.NO_CONTENT);
		response.setMessage(SUCCESS_DELETE);
		
		log.info("@@ DatasourceService.delete started : {}", id);
		
		try {
			// ������ �ռ� �����ϴ��� Ȯ�� �� �������� �ʴ´ٸ� ���� ��ȯ
			if(!datasourceRepository.existsById(id)) {
				log.warn("@@ DatasourceService.delete failed : {}", NOT_EXISTS);
				response.setStatus(HttpStatus.NOT_FOUND);
				response.setMessage(NOT_EXISTS);
			}
			// �����Ѵٸ� ����
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

				// ĳ�õ� �����Ϳ����� ����
				datasourceCacheService.delDatasourceCache(datasourceCacheService.getDatasourceList(), id);
			}
			
		}
		catch(Exception e) {
			// DB ���� 500 ��ȯ
			log.error("@@ DatasourceService.delete failed : {}", e.getMessage());
			response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
			response.setMessage(INTERNAL_ERROR);
		}
		
		
		return response;
	}
	
}
