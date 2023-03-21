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
 * ����ڿ��� datasource ���� CRUD API�� �����ϴ� ��Ʈ�ѷ� Ŭ����
 * @author �̹ο�
 *
 */
@RestController
public class DatasourceController {
	
	@Autowired DatasourceService datasourceService;
	private static final String BASE_URI = "/datasource";
	private static String NOT_VALID_DATA = "Datasource resource is not valid";
	
	/**
	 * �ű� �����ͼҽ� ����
	 * @param datasource
	 * @return
	 */
	@PostMapping(BASE_URI)
	public ResponseVO createDatasource(@RequestBody(required=true) DatasourceVO datasource) {
		return datasourceService.create(datasource);
	}
	
	/**
	 * ��� �����ͼҽ� ��ȸ
	 * @param queryVO host�� protocol�� ������� �˻� ���� ����
	 * @return
	 */
	@GetMapping(BASE_URI)
	public ResponseVO selectDatasourceList(@ModelAttribute QueryVO queryVO) {
		return datasourceService.selectAll(queryVO);
	}
	
	/**
	 * Ư�� �����ͼҽ� ��ȸ
	 * @param id ��ȸ�Ϸ��� �����ͼҽ��� id
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
	 * Ư�� �����ͼҽ� ����
	 * @param id �����Ϸ��� �����ͼҽ��� id
	 * @param datasource �ش� �����ͼҽ��� ������� ���ο� datasource ����
	 * @return
	 */
	@PutMapping(BASE_URI+"/{id}")
	public ResponseVO updateDatasource(@PathVariable(value = "id", required=true) String id, @RequestBody(required=true) DatasourceVO datasource) {
		
		if(!ValidateUtil.isValidString(datasource.getId())) {
			datasource.setId(id);
		}
		else {
			if(!datasource.getId().equals(id)) {
				// ���� ������ ���� ����ó��
				ResponseVO responseVO = new ResponseVO();
				responseVO.setStatus(HttpStatus.BAD_REQUEST);
				responseVO.setMessage(NOT_VALID_DATA);
				
				return responseVO;
			}
		}
		
		return datasourceService.update(datasource);
	}
	
	/**
	 * Ư�� �����ͼҽ� ����
	 * @param id �����Ϸ��� �����ͼҽ��� id
	 * @return
	 */
	@DeleteMapping(BASE_URI+"/{id}")
	public ResponseVO deleteDatasource(@PathVariable(value = "id", required=true) String id) {
		return datasourceService.delete(id);
	}
	
	
}
