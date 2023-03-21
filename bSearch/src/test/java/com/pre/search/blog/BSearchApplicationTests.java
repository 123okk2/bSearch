package com.pre.search.blog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;

import com.pre.search.blog.common.code.CommonCode;
import com.pre.search.blog.keyword.repository.KeywordRepository;
import com.pre.search.blog.keyword.vo.Keyword;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class BSearchApplicationTests {

	@Test
	void contextLoads() {
	}
	
	
	
	@Autowired KeywordRepository keywordRepo;
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
	@Test
	void keywordTest() throws Exception {
		Keyword keyword1 = new Keyword();
		keyword1.setSearchDate(formatter.parse("2022-12-12"));
		keyword1.setWord("word01");
		keywordRepo.save(keyword1);
		
		Keyword keyword2 = new Keyword();
		keyword2.setSearchDate(formatter.parse("2023-01-01"));
		keyword2.setWord("word02");
		keywordRepo.save(keyword2);
		
		Keyword keyword3 = new Keyword();
		keyword3.setSearchDate(formatter.parse("2023-02-01"));
		keyword3.setWord("word03");
		keywordRepo.save(keyword3);
		
		Keyword keyword4 = new Keyword();
		keyword4.setSearchDate(formatter.parse("2023-03-01"));
		keyword4.setWord("word01");
		keywordRepo.save(keyword4);
		
		Keyword keyword5 = new Keyword();
		keyword5.setSearchDate(formatter.parse("2023-04-01"));
		keyword5.setWord("word02");
		keywordRepo.save(keyword5);
		

		String startDateStr = "2023-01-01";
		Date startDate = formatter.parse(startDateStr);
		String endDateStr = "2023-03-19";
		Date endDate = formatter.parse(endDateStr);
		endDate.setHours(23);
		endDate.setMinutes(59);
		endDate.setSeconds(59);
		
		/*
		 * date			word
		 * 2022-12-12 	word01
		 * 2023-01-01 	word02
		 * 2023-02-01	word03
		 * 2023-03-01	word01
		 * 2023-04-01	word02
		 */
		
		
		/*
		 * word 	count
		 * word01	2
		 * word02	2
		 * word03	1
		 */
		log.info("========================");
		List<Map<String, Object>> findAll = keywordRepo.findByDefault(PageRequest.of(0, 10));
		for(Map<String, Object> find : findAll) {
			log.info(find.get(CommonCode.KEYWORD_WORD) + " / " + find.get(CommonCode.KEYWORD_COUNT));
		}
		
		/*
		 * word		count
		 * word01	2
		 * word02	1
		 * word03	1
		 */
		log.info("========================");
		List<Map<String, Object>> findBefore = keywordRepo.findByDateBefore(endDate, PageRequest.of(0, 10));
		for(Map<String, Object> find : findBefore) {
			log.info(find.get(CommonCode.KEYWORD_WORD) + " / " + find.get(CommonCode.KEYWORD_COUNT));
		}

		/*
		 * word		count
		 * word02	2
		 * word01	1
		 * word03	1
		 */
		log.info("========================");
		List<Map<String, Object>> findAfter = keywordRepo.findByDateAfter(startDate, PageRequest.of(0, 10));
		for(Map<String, Object> find : findAfter) {
			log.info(find.get(CommonCode.KEYWORD_WORD) + " / " + find.get(CommonCode.KEYWORD_COUNT));
		}

		/*
		 * word 	count
		 * word01	1
		 * word02	1
		 * word03	1
		 */
		log.info("========================");
		List<Map<String, Object>> findBetween = keywordRepo.findByDateBetween(startDate, endDate, PageRequest.of(0, 10));
		for(Map<String, Object> find : findBetween) {
			log.info(find.get(CommonCode.KEYWORD_WORD) + " / " + find.get(CommonCode.KEYWORD_COUNT));
		}
		
		
	}

}
