package com.pre.search.blog.datasource.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.pre.search.blog.datasource.vo.Datasource;
import com.pre.search.blog.datasource.vo.DatasourceHeader;
import com.pre.search.blog.datasource.vo.DatasourceHeaderPK;

public interface DatasourceHeaderRepository extends JpaRepository<DatasourceHeader, DatasourceHeaderPK> {

	/**
	 * 외래키 기반 데이터 삭제
	 * @param datasource
	 */
	@Transactional
	@Query
	public void deleteByDatasource(Datasource datasource);
	/**
	 * 외래키 기반 데이터 검색
	 * @param datasource
	 * @return
	 */
	@Query
	public List<DatasourceHeader> findAllByDatasource(Datasource datasource);
	/**
	 * 외래키 기반 데이터 존재여부 확인
	 * @param datasource
	 * @return
	 */
	@Query
	public Boolean existsByDatasource(Datasource datasource);
}
