package com.pre.search.blog.datasource.service;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.pre.search.blog.common.util.ValidateUtil;
import com.pre.search.blog.common.vo.QueryVO;
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

/**
 * datasource�� ������ ��ȸ�ϱ� ���� ĳ�� ��Ʈ�� Ŭ����.
 * datasource�� search api�� �۵��� ������ �ҷ��� �����ε�, �� ������ DB���� ��ȸ�� �ϰ� �ϰ���� �ʾ� ĳ�÷� ����.
 * 
 * @author �̹ο�
 *
 */
@Service
public class DatasourceCacheService {
	
	@Autowired DatasourceRepository datasourceRepository;
	@Autowired DatasourceReqParamRepository datasourceReqParamRepository;
	@Autowired DatasourceResParamRepository datasourceResParamRepository;
	@Autowired DatasourceHeaderRepository datasourceHeaderRepository;
	
	//private static List<String> validateProtocols = Arrays.asList(new String[] {"http", "https"});
	
	/**
	 * ����� datasource ��� ��ȸ
	 * @return ĳ�õ� ������ �ҽ� ���
	 */
	@Cacheable("datasourceVOList")
	public List<DatasourceVO> getDatasourceList() {
		List<Datasource> datasourceList = datasourceRepository.findAll();
		
		List<DatasourceVO> datasourceVOList = new ArrayList<>();
		
		for(Datasource datasource : datasourceList) {
			// datasource ������� datasourceVO Ŭ���� ����
			DatasourceVO cachedDatasourceVO = new DatasourceVO(datasource);
			
			// RequestParam�� ResponseParam�� ���� ��ȸ�ؼ� datasourceVO�� ž��.
			List<DatasourceRequestParam> datasourceRequestParamList = datasourceReqParamRepository.findAllByDatasource(datasource);
			List<DatasourceResponseParam> datasourceResponseParamList = datasourceResParamRepository.findAllByDatasource(datasource);
			List<DatasourceHeader> datasourceHeaderList = datasourceHeaderRepository.findAllByDatasource(datasource);
			
			List<DatasourceRequestParamVO> datasourceRequestParamVOList = new ArrayList<>();
			List<DatasourceResponseParamVO> datasourceResponseParamVOList = new ArrayList<>();
			List<DatasourceHeaderVO> datasourceHeaderVOList = new ArrayList<>();
			
			for(DatasourceRequestParam requestParam : datasourceRequestParamList) {
				DatasourceRequestParamVO reqParamVO = new DatasourceRequestParamVO(requestParam);
				datasourceRequestParamVOList.add(reqParamVO);
			}
			for(DatasourceResponseParam responseParam : datasourceResponseParamList) {
				DatasourceResponseParamVO resParamVO = new DatasourceResponseParamVO(responseParam);
				datasourceResponseParamVOList.add(resParamVO);
			}
			for(DatasourceHeader datasourceHeader : datasourceHeaderList) {
				DatasourceHeaderVO headerVO = new DatasourceHeaderVO(datasourceHeader);
				datasourceHeaderVOList.add(headerVO);
			}
			
			cachedDatasourceVO.setRequestParams(datasourceRequestParamVOList);
			cachedDatasourceVO.setResponseParams(datasourceResponseParamVOList);
			cachedDatasourceVO.setRequestHeaders(datasourceHeaderVOList);
			
			datasourceVOList.add(cachedDatasourceVO);
		}
		
		// ĳ�ÿ� ����
		return datasourceVOList;
	}	
	
	/**
	 * ������ datasourceVO�� ��� �ȿ��� Ư�� ���ǿ� �´� datasourceVO�� ����� �˻�
	 * 
	 * @param datasourceVOList �˻� ����� �� datasourceVO ���
	 * @param queryVO �˻� ������ ��� Ŭ����. host�� protocol ������� �˻�.
	 * @return
	 */
	public List<DatasourceVO> getDatasourceList(List<DatasourceVO> datasourceVOList, QueryVO queryVO) {
		
		List<DatasourceVO> datasourceVOFilteredList = new ArrayList<>();
		
		if(!ValidateUtil.isEmptyData(queryVO)) {
			Boolean isValidHost = false;
			Boolean isValidProtocol = false;
			
			// �˻��� �ռ� host�� protocol�� �̻��� ������ Ȯ��.
			if(ValidateUtil.isValidString(queryVO.getHost())) {
				isValidHost = true;
			}
			if(ValidateUtil.isValidString(queryVO.getProtocol())) {
				isValidProtocol = true;
			}
			
			// �Ѵ� �̻��� �����ϸ� ���� ����.
			if(!isValidHost && !isValidProtocol) {
				return datasourceVOList;
			}
			
			// �� �� �ϳ��� �̻��� ������ ���͸� ����
			for(DatasourceVO datasourceVO : datasourceVOList) {
				if(isValidHost) {
					// LIKE ����
					if(!datasourceVO.getHost().contains(queryVO.getHost())) {
						continue;
					}
				}
				if(isValidProtocol) {
					// EQUALS ����
					if(!datasourceVO.getProtocol().equals(queryVO.getProtocol())) {
						continue;
					}
				}
				
				datasourceVOFilteredList.add(datasourceVO);
			}
			
			return datasourceVOFilteredList;
		}
		
		return datasourceVOList;
	}
	
	/**
	 * Ư�� datasource ��ȸ
	 * @param id
	 * @return id�� ��ġ�ϴ� datasource
	 */
	public DatasourceVO getDatasource(List<DatasourceVO> datasourceVOList, QueryVO queryVO) {

		Boolean isValidHost = false;
		Boolean isValidProtocol = false;
		
		if(!ValidateUtil.isEmptyData(queryVO)) {
			
			// �˻��� �ռ� host�� protocol�� �̻��� ������ Ȯ��.
			if(ValidateUtil.isValidString(queryVO.getHost())) {
				isValidHost = true;
			}
			if(ValidateUtil.isValidString(queryVO.getProtocol())) {
				isValidProtocol = true;
			}
			
		}
		
		for(DatasourceVO datasourceVO : datasourceVOList) {
			if(queryVO.getDatasourceId().equals(datasourceVO.getId())) {

				if(isValidHost) {
					// LIKE ����
					if(!datasourceVO.getHost().contains(queryVO.getHost())) {
						// ID�� PK�ε� ���ǿ� ���� ������ �ݺ����� �� �����ص� �ǹ̰� ����.
						return null;
					}
				}
				if(isValidProtocol) {
					// EQUALS ����
					if(!datasourceVO.getProtocol().equals(queryVO.getProtocol())) {
						return null;
					}
				}
				
				return datasourceVO;
			}
		}
		
		return null;
	}
	
	/**
	 * datasource �߰� �̺�Ʈ �߻� �� ĳ�ÿ� �߰�
	 * @param datasource
	 * @return ĳ�õ� ������ �ҽ� ���
	 */
	@CachePut("datasourceVOList")
	public List<DatasourceVO> addDatasourceCache(List<DatasourceVO> datasourceVOList, DatasourceVO datasourceVO) {

		// ������ �ʱ� datasource �Է� �� �ߺ��ԷµǴ� ��찡 ����.
		// �ߺ� �Է� ����
		if(datasourceVOList.size() == 1) {
			if(!datasourceVOList.get(0).getId().equals(datasourceVO.getId())) {
				datasourceVOList.add(datasourceVO);
			}
		}
		// �ʱ⿡�� �׷� ���̹Ƿ� 2�� �̻��� �Էµ� ���¶�� add�� ����
		else {
			datasourceVOList.add(datasourceVO);
		}
		
		return datasourceVOList;
	}
	
	/**
	 * datasource ���� �̺�Ʈ �߻� �� ĳ�� ����
	 * @param datasource
	 * @return ĳ�õ� ������ �ҽ� ���
	 */
	@CachePut("datasourceVOList")
	public List<DatasourceVO> modDatasourceCache(List<DatasourceVO> datasourceVOList, DatasourceVO datasourceVO) {

		for(int i=0; i<datasourceVOList.size(); i++) {
			DatasourceVO cachedDatasourceVO = datasourceVOList.get(i);
			
			if(cachedDatasourceVO.getId().equals(datasourceVO.getId())) {
				datasourceVOList.set(i, datasourceVO);
				break;
			}
		}
		
		return datasourceVOList;
	}
	
	/**
	 * datasource ���� �̺�Ʈ �߻� �� ĳ�ÿ��� ����
	 * @param id
	 * @return ĳ�õ� ������ �ҽ� ���
	 */
	@CachePut("datasourceVOList")
	public List<DatasourceVO> delDatasourceCache(List<DatasourceVO> datasourceVOList, String id) {

		for(int i=0; i<datasourceVOList.size(); i++) {
			DatasourceVO cachedDatasourceVO = datasourceVOList.get(i);
			
			if(cachedDatasourceVO.getId().equals(id)) {
				datasourceVOList.remove(i);
				break;
			}
		}
		
		return datasourceVOList;
	}
	
}
