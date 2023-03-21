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
 * 인기검색어(keyword) 탐색을 위한 리포지토리
 * @author 이민우
 *
 */
@Repository
public interface KeywordRepository extends JpaRepository<Keyword, Long> {
	
	/**
	 * 사용자가 Query Parameter에 startDate와 endDate을 지정하지 않았을 경우 모든 데이터 조회
	 * 
	 * @param pageable LIMIT을 지원하지 않아 사용. LIMIT 10과 동일한 효과
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
	 * 사용자가 Query Parameter에 startDate와 endDate을 모두 지정했을 경우 사용.
	 * 
	 * @param startDate 시작기준
	 * @param endDate 종료기준
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
	 * 사용자가 Query Parameter에 startDate만 지정했을 경우 사용
	 * 
	 * @param startDate 시작기준
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
	 * 사용자가 Query Parameter에 endDate만 지정했을 경우 사용
	 * 
	 * @param endDate 종료기준
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
