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
 * ����� datasource DB ��ȸ �� ���ǹ� ���
 * 
 * @deprecated ĳ�ÿ��� ������ ������� GET�ϴ� ������� ����
 * @author �̹ο�
 *
 */
public class DatasourceSpecification {
	public static Specification<Datasource> searchDatasource(QueryVO queryVO) {
		return (Specification<Datasource>) ((datasource, query, builder) -> {
			
			List<Predicate> predicate = new ArrayList<>();
			
			
			if(!ValidateUtil.isEmptyData(queryVO)) {

				// ������ HOST�� �����ϸ� LIKE ���� �߰�
				if(ValidateUtil.isValidString(queryVO.getHost())) {
					Predicate hostPredicate = builder.like(datasource.get("host"), "%"+queryVO.getHost()+"%");
					predicate.add(hostPredicate);
				}
				// ������ PROTOCOL�� �����ϸ� ���� �߰�
				if(ValidateUtil.isValidString(queryVO.getProtocol())) {
					Predicate protocolPredicate = builder.equal(datasource.get("protocol"), queryVO.getProtocol());
					predicate.add(protocolPredicate);
				}
			}
			
			return builder.and(predicate.toArray(new Predicate[0]));
			
		});
	}
}
