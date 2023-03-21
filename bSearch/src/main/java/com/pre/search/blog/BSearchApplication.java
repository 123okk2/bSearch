package com.pre.search.blog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import com.pre.search.blog.datasource.service.DatasourceCacheService;

@SpringBootApplication
@EnableCaching
public class BSearchApplication {
	
	@Autowired DatasourceCacheService datasourceCacheService;

	public static void main(String[] args) {
		SpringApplication.run(BSearchApplication.class, args);
	}
	
	
	/**
	 * �ʱ� ���� : DB�� ����� datasource �������� ĳ�ÿ� ��� ������ Ȱ�� �����ϰ� ����
	 */
	@PostConstruct
	public void setDatasourceCache() {
		datasourceCacheService.getDatasourceList();
	}

}
