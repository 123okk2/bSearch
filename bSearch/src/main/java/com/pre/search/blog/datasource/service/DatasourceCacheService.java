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
 * datasource를 빠르게 조회하기 위한 캐시 컨트롤 클래스.
 * datasource는 search api가 작동될 떄마다 불려올 예정인데, 그 때마다 DB에서 조회를 하게 하고싶지 않아 캐시로 설정.
 * 
 * @author 이민우
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
	 * 저장된 datasource 목록 조회
	 * @return 캐시된 데이터 소스 목록
	 */
	@Cacheable("datasourceVOList")
	public List<DatasourceVO> getDatasourceList() {
		List<Datasource> datasourceList = datasourceRepository.findAll();
		
		List<DatasourceVO> datasourceVOList = new ArrayList<>();
		
		for(Datasource datasource : datasourceList) {
			// datasource 기반으로 datasourceVO 클래스 생성
			DatasourceVO cachedDatasourceVO = new DatasourceVO(datasource);
			
			// RequestParam과 ResponseParam을 전부 조회해서 datasourceVO에 탑재.
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
		
		// 캐시에 저장
		return datasourceVOList;
	}	
	
	/**
	 * 지정된 datasourceVO의 목록 안에서 특정 조건에 맞는 datasourceVO의 목록을 검색
	 * 
	 * @param datasourceVOList 검색 대상이 될 datasourceVO 목록
	 * @param queryVO 검색 조건이 담긴 클래스. host와 protocol 기반으로 검색.
	 * @return
	 */
	public List<DatasourceVO> getDatasourceList(List<DatasourceVO> datasourceVOList, QueryVO queryVO) {
		
		List<DatasourceVO> datasourceVOFilteredList = new ArrayList<>();
		
		if(!ValidateUtil.isEmptyData(queryVO)) {
			Boolean isValidHost = false;
			Boolean isValidProtocol = false;
			
			// 검색에 앞서 host와 protocol에 이상이 없는지 확인.
			if(ValidateUtil.isValidString(queryVO.getHost())) {
				isValidHost = true;
			}
			if(ValidateUtil.isValidString(queryVO.getProtocol())) {
				isValidProtocol = true;
			}
			
			// 둘다 이상이 존재하면 원본 리턴.
			if(!isValidHost && !isValidProtocol) {
				return datasourceVOList;
			}
			
			// 둘 중 하나라도 이상이 없으면 필터링 수행
			for(DatasourceVO datasourceVO : datasourceVOList) {
				if(isValidHost) {
					// LIKE 연산
					if(!datasourceVO.getHost().contains(queryVO.getHost())) {
						continue;
					}
				}
				if(isValidProtocol) {
					// EQUALS 연산
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
	 * 특정 datasource 조회
	 * @param id
	 * @return id가 일치하는 datasource
	 */
	public DatasourceVO getDatasource(List<DatasourceVO> datasourceVOList, QueryVO queryVO) {

		Boolean isValidHost = false;
		Boolean isValidProtocol = false;
		
		if(!ValidateUtil.isEmptyData(queryVO)) {
			
			// 검색에 앞서 host와 protocol에 이상이 없는지 확인.
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
					// LIKE 연산
					if(!datasourceVO.getHost().contains(queryVO.getHost())) {
						// ID는 PK인데 조건에 맞지 않으면 반복문을 더 진행해도 의미가 없음.
						return null;
					}
				}
				if(isValidProtocol) {
					// EQUALS 연산
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
	 * datasource 추가 이벤트 발생 시 캐시에 추가
	 * @param datasource
	 * @return 캐시된 데이터 소스 목록
	 */
	@CachePut("datasourceVOList")
	public List<DatasourceVO> addDatasourceCache(List<DatasourceVO> datasourceVOList, DatasourceVO datasourceVO) {

		// 로직상 초기 datasource 입력 시 중복입력되는 경우가 있음.
		// 중복 입력 방지
		if(datasourceVOList.size() == 1) {
			if(!datasourceVOList.get(0).getId().equals(datasourceVO.getId())) {
				datasourceVOList.add(datasourceVO);
			}
		}
		// 초기에만 그런 것이므로 2개 이상이 입력된 상태라면 add만 수행
		else {
			datasourceVOList.add(datasourceVO);
		}
		
		return datasourceVOList;
	}
	
	/**
	 * datasource 수정 이벤트 발생 시 캐시 수정
	 * @param datasource
	 * @return 캐시된 데이터 소스 목록
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
	 * datasource 삭제 이벤트 발생 시 캐시에서 삭제
	 * @param id
	 * @return 캐시된 데이터 소스 목록
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
