package com.pre.search.blog.datasource.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.pre.search.blog.datasource.vo.Datasource;

/**
 * datasource CRUE를 위한 REPOSITORY
 * @author 이민우
 *
 */
public interface DatasourceRepository extends JpaRepository<Datasource, String>, JpaSpecificationExecutor<Datasource> {
	
}
