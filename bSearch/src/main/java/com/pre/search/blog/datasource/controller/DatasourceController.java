package com.pre.search.blog.datasource.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.pre.search.blog.common.util.ValidateUtil;
import com.pre.search.blog.common.vo.QueryVO;
import com.pre.search.blog.common.vo.ResponseVO;
import com.pre.search.blog.datasource.service.DatasourceService;
import com.pre.search.blog.datasource.vo.DatasourceVO;

/**
 * 사용자에게 datasource 관련 CRUD API를 제공하는 컨트롤러 클래스
 * @author 이민우
 *
 */
@RestController
public class DatasourceController {
	
	@Autowired DatasourceService datasourceService;
	private static final String BASE_URI = "/datasource";
	private static String NOT_VALID_DATA = "Datasource resource is not valid";
	
	/**
	 * 신규 데이터소스 생성
	 * @param datasource
	 * @return
	 */
	@PostMapping(BASE_URI)
	public ResponseVO createDatasource(@RequestBody(required=true) DatasourceVO datasource) {
		return datasourceService.create(datasource);
	}
	
	/**
	 * 모든 데이터소스 조회
	 * @param queryVO host와 protocol을 기반으로 검색 가능 지원
	 * @return
	 */
	@GetMapping(BASE_URI)
	public ResponseVO selectDatasourceList(@ModelAttribute QueryVO queryVO) {
		return datasourceService.selectAll(queryVO);
	}
	
	/**
	 * 특정 데이터소스 조회
	 * @param id 조회하려는 데이터소스의 id
	 * @return
	 */
	@GetMapping(BASE_URI+"/{id}")
	public ResponseVO selectDatasource(@PathVariable(value = "id", required=true) String id, @ModelAttribute QueryVO queryVO) {
		
		if(ValidateUtil.isValidString(id)) {
			queryVO.setDatasourceId(id);
		}
		
		return datasourceService.selectOne(queryVO);
	}
	
	/**
	 * 특정 데이터소스 수정
	 * @param id 수정하려는 데이터소스의 id
	 * @param datasource 해당 데이터소스에 덮어씌워질 새로운 datasource 정보
	 * @return
	 */
	@PutMapping(BASE_URI+"/{id}")
	public ResponseVO updateDatasource(@PathVariable(value = "id", required=true) String id, @RequestBody(required=true) DatasourceVO datasource) {
		
		if(!ValidateUtil.isValidString(datasource.getId())) {
			datasource.setId(id);
		}
		else {
			if(!datasource.getId().equals(id)) {
				// 에러 방지를 위한 예외처리
				ResponseVO responseVO = new ResponseVO();
				responseVO.setStatus(HttpStatus.BAD_REQUEST);
				responseVO.setMessage(NOT_VALID_DATA);
				
				return responseVO;
			}
		}
		
		return datasourceService.update(datasource);
	}
	
	/**
	 * 특정 데이터소스 삭제
	 * @param id 삭제하려는 데이터소스의 id
	 * @return
	 */
	@DeleteMapping(BASE_URI+"/{id}")
	public ResponseVO deleteDatasource(@PathVariable(value = "id", required=true) String id) {
		return datasourceService.delete(id);
	}
	
	
}
