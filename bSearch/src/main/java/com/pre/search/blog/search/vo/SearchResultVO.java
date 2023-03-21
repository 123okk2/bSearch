package com.pre.search.blog.search.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.pre.search.blog.common.code.CommonCode;
import com.pre.search.blog.datasource.vo.DatasourceVO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 검색 결과 VO
 * @author 이민우
 *
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchResultVO implements Serializable {

	private static final long serialVersionUID = -2929789292155268166L;
	
	private MetaDataVO metaDataVO;
	private List<DocumentsVO> documentsList;
	
	/**
	 * HTTP REQUEST 결과를 VO로 변환하는 생성자
	 * 
	 * @param httpResult HTTP Request 결과
	 * @param datasourceVO HTTP 통신에 사용된 datasource
	 */
	public SearchResultVO(JsonNode httpResult, DatasourceVO datasourceVO) {

		// httpResult에서 metaData 확인 및 로드
		MetaDataVO metaDataVO = new MetaDataVO();
		String metaSourceName = datasourceVO.getResponseColumn(CommonCode.META);
		if(metaSourceName != null && httpResult.has(metaSourceName)) {
			JsonNode metaDataResult = httpResult.get(metaSourceName);
			// total count (총 개수) 꺼내기
			String totalCountName = datasourceVO.getResponseColumn(CommonCode.META_TOTAL_COUNT);
			if(totalCountName != null && metaDataResult.has(totalCountName)) {
				metaDataVO.setTotalCount(metaDataResult.get(totalCountName).asInt());
			}
			// pageable count (총 개수) 꺼내기
			String pageableCountName = datasourceVO.getResponseColumn(CommonCode.META_PAGEABLE_COUNT);
			if(pageableCountName != null && metaDataResult.has(pageableCountName)) {
				metaDataVO.setPageableCount(metaDataResult.get(pageableCountName).asInt());
			}
			// is_end (마지막 페이지 여부) 꺼내기
			String isEndName = datasourceVO.getResponseColumn(CommonCode.META_IS_END);
			if(isEndName != null && metaDataResult.has(isEndName)) {
				metaDataVO.setIsEnd(metaDataResult.get(isEndName).asBoolean());
			}
			// TODO : 데이터소스 확장 시 밑에 추가
			
		}
		else {
			// kakao API는 meta가 별도로 지정되어 있지만, Naver같은 경우는 지정되어 있지 않음.
			// 즉, meta가 별도로 존재하지 않고 httpResulat 내에 그대로 들어있을 경우 사용
			// total count (총 개수) 꺼내기
			String totalCountName = datasourceVO.getResponseColumn(CommonCode.META_TOTAL_COUNT);
			if(totalCountName != null && httpResult.has(totalCountName)) {
				metaDataVO.setTotalCount(httpResult.get(totalCountName).asInt());
			}
			// pageable count (총 개수) 꺼내기
			String pageableCountName = datasourceVO.getResponseColumn(CommonCode.META_PAGEABLE_COUNT);
			if(pageableCountName != null && httpResult.has(pageableCountName)) {
				metaDataVO.setPageableCount(httpResult.get(pageableCountName).asInt());
			}
			// is_end (마지막 페이지 여부) 꺼내기
			String isEndName = datasourceVO.getResponseColumn(CommonCode.META_IS_END);
			if(isEndName != null && httpResult.has(isEndName)) {
				metaDataVO.setIsEnd(httpResult.get(isEndName).asBoolean());
			}
		}
		this.setMetaDataVO(metaDataVO);
		
		// httpResult에서 documents 확인 및 로드
		List<DocumentsVO> documentVOList = new ArrayList<>();
		String documentSourceName = datasourceVO.getResponseColumn(CommonCode.DOCUMENTS);
		if(documentSourceName != null && httpResult.has(documentSourceName)) {
			JsonNode documentResultList = httpResult.get(documentSourceName);
			
			String documentTitleName = datasourceVO.getResponseColumn(CommonCode.DOCUMENTS_TITLE);
			String documentContentsName = datasourceVO.getResponseColumn(CommonCode.DOCUMENTS_CONTENTS);
			String documentUrlName = datasourceVO.getResponseColumn(CommonCode.DOCUMENTS_URL);
			String documentBlognameName = datasourceVO.getResponseColumn(CommonCode.DOCUMENTS_BLOG_NAME);
			String documentThumbnailName = datasourceVO.getResponseColumn(CommonCode.DOCUMENTS_THUMBNAIL);
			String documentDatetimeName = datasourceVO.getResponseColumn(CommonCode.DOCUMENTS_DATETIME);
			
			if(documentResultList.isArray()) {
				for(JsonNode documentResult : documentResultList) {
					
					DocumentsVO documentVO = new DocumentsVO();
					
					if(documentTitleName != null && documentResult.has(documentTitleName)) {
						documentVO.setTitle(documentResult.get(documentTitleName).asText());
					}
					if(documentContentsName != null && documentResult.has(documentContentsName)) {
						documentVO.setContents(documentResult.get(documentContentsName).asText());
					}
					if(documentUrlName != null && documentResult.has(documentUrlName)) {
						documentVO.setUrl(documentResult.get(documentUrlName).asText());
					}
					if(documentBlognameName != null && documentResult.has(documentBlognameName)) {
						documentVO.setBlogname(documentResult.get(documentBlognameName).asText());
					}
					if(documentThumbnailName != null && documentResult.has(documentThumbnailName)) {
						documentVO.setThumbnail(documentResult.get(documentThumbnailName).asText());
					}
					if(documentDatetimeName != null && documentResult.has(documentDatetimeName)) {
						documentVO.setDatetime(documentResult.get(documentDatetimeName).asText());
					}
					
					documentVOList.add(documentVO);
				}
			}
		}
		this.documentsList = documentVOList;
	}
}
