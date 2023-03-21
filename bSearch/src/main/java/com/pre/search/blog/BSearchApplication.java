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
	 * 초기 세팅 : DB에 저장된 datasource 정보들을 캐시에 담아 언제든 활용 가능하게 설정
	 */
	@PostConstruct
	public void setDatasourceCache() {
		datasourceCacheService.getDatasourceList();
	}

}
