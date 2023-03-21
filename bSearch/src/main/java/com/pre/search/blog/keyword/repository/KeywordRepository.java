package com.pre.search.blog.keyword.repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pre.search.blog.common.code.CommonCode;
import com.pre.search.blog.keyword.vo.Keyword;

/**
 * �α�˻���(keyword) Ž���� ���� �������丮
 * @author �̹ο�
 *
 */
@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
	
	/**
	 * ����ڰ� Query Parameter�� startDate�� endDate�� �������� �ʾ��� ��� ��� ������ ��ȸ
	 * 
	 * @param pageable LIMIT�� �������� �ʾ� ���. LIMIT 10�� ������ ȿ��
	 * @return
	 */
	@Query("SELECT "
			+ "COUNT(keyword) AS " + CommonCode.KEYWORD_COUNT + ", "
			+ "keyword.word AS "+ CommonCode.KEYWORD_WORD + " "
			+ "FROM Keyword keyword "
			+ "GROUP BY keyword.word "
			+ "ORDER BY cnt DESC")
	public List<Map<String, Object>> findByDefault(Pageable pageable);
	
	/**
	 * ����ڰ� Query Parameter�� startDate�� endDate�� ��� �������� ��� ���.
	 * 
	 * @param startDate ���۱���
	 * @param endDate �������
	 * @param pageable
	 * @return
	 */
	@Query("SELECT "
			+ "COUNT(keyword) AS " + CommonCode.KEYWORD_COUNT + ", "
			+ "keyword.word AS "+ CommonCode.KEYWORD_WORD + " "
			+ "FROM Keyword keyword "
			+ "WHERE keyword.searchDate BETWEEN :startDate AND :endDate "
			+ "GROUP BY keyword.word "
			+ "ORDER BY cnt DESC")
	public List<Map<String, Object>> findByDateBetween(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable pageable);

	/**
	 * ����ڰ� Query Parameter�� startDate�� �������� ��� ���
	 * 
	 * @param startDate ���۱���
	 * @param pageable
	 * @return
	 */
	@Query("SELECT "
			+ "COUNT(keyword) AS " + CommonCode.KEYWORD_COUNT + ", "
			+ "keyword.word AS "+ CommonCode.KEYWORD_WORD + " "
			+ "FROM Keyword keyword "
			+ "WHERE keyword.searchDate >= :startDate "
			+ "GROUP BY keyword.word "
			+ "ORDER BY cnt DESC")
	public List<Map<String, Object>> findByDateAfter(@Param("startDate") Date startDate, Pageable pageable);
	
	/**
	 * ����ڰ� Query Parameter�� endDate�� �������� ��� ���
	 * 
	 * @param endDate �������
	 * @param pageable
	 * @return
	 */
	@Query("SELECT "
			+ "COUNT(keyword) AS " + CommonCode.KEYWORD_COUNT + ", "
			+ "keyword.word AS "+ CommonCode.KEYWORD_WORD + " "
			+ "FROM Keyword keyword "
			+ "WHERE keyword.searchDate <= :endDate "
			+ "GROUP BY keyword.word "
			+ "ORDER BY cnt DESC")
	public List<Map<String, Object>> findByDateBefore(@Param("endDate") Date endDate, Pageable pageable);
}
