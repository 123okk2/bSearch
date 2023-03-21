package com.pre.search.blog.datasource.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.pre.search.blog.common.util.ValidateUtil;
import com.pre.search.blog.common.vo.QueryVO;
import com.pre.search.blog.datasource.vo.Datasource;

/**
 * 
 * 저장된 datasource DB 조회 시 조건문 사용
 * 
 * @deprecated 캐시에서 꺼내는 방식으로 GET하는 방식으로 변경
 * @author 이민우
 *
 */
public class DatasourceSpecification {
	public static Specification<Datasource> searchDatasource(QueryVO queryVO) {
		return (Specification<Datasource>) ((datasource, query, builder) -> {
			
			List<Predicate> predicate = new ArrayList<>();
			
			
			if(!ValidateUtil.isEmptyData(queryVO)) {

				// 쿼리에 HOST가 존재하면 LIKE 연산 추가
				if(ValidateUtil.isValidString(queryVO.getHost())) {
					Predicate hostPredicate = builder.like(datasource.get("host"), "%"+queryVO.getHost()+"%");
					predicate.add(hostPredicate);
				}
				// 쿼리에 PROTOCOL이 존재하면 연산 추가
				if(ValidateUtil.isValidString(queryVO.getProtocol())) {
					Predicate protocolPredicate = builder.equal(datasource.get("protocol"), queryVO.getProtocol());
					predicate.add(protocolPredicate);
				}
			}
			
			return builder.and(predicate.toArray(new Predicate[0]));
			
		});
	}
}
