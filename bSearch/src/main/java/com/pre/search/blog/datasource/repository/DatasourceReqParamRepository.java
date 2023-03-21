package com.pre.search.blog.datasource.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pre.search.blog.datasource.vo.Datasource;
import com.pre.search.blog.datasource.vo.DatasourceRequestParam;
import com.pre.search.blog.datasource.vo.DatasourceRequestParamPK;

/**
 * datasourceRequestParm CRUD�� ���� REPOSITORY
 * @author �̹ο�
 *
 */
public interface DatasourceReqParamRepository extends JpaRepository<DatasourceRequestParam, DatasourceRequestParamPK> {

	/**
	 * �ܷ�Ű ��� ������ ����
	 * @param datasource
	 */
	@Transactional
	@Query
	public void deleteByDatasource(Datasource datasource);
	/**
	 * �ܷ�Ű ��� ������ �˻�
	 * @param datasource
	 * @return
	 */
	@Query
	public List<DatasourceRequestParam> findAllByDatasource(Datasource datasource);
	/**
	 * �ܷ�Ű ��� ������ ���翩�� Ȯ��
	 * @param datasource
	 * @return
	 */
	@Query
	public Boolean existsByDatasource(Datasource datasource);
}
