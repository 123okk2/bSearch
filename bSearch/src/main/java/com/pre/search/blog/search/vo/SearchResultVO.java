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
 * �˻� ��� VO
 * @author �̹ο�
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
	 * HTTP REQUEST ����� VO�� ��ȯ�ϴ� ������
	 * 
	 * @param httpResult HTTP Request ���
	 * @param datasourceVO HTTP ��ſ� ���� datasource
	 */
	public SearchResultVO(JsonNode httpResult, DatasourceVO datasourceVO) {

		// httpResult���� metaData Ȯ�� �� �ε�
		MetaDataVO metaDataVO = new MetaDataVO();
		String metaSourceName = datasourceVO.getResponseColumn(CommonCode.META);
		if(metaSourceName != null && httpResult.has(metaSourceName)) {
			JsonNode metaDataResult = httpResult.get(metaSourceName);
			// total count (�� ����) ������
			String totalCountName = datasourceVO.getResponseColumn(CommonCode.META_TOTAL_COUNT);
			if(totalCountName != null && metaDataResult.has(totalCountName)) {
				metaDataVO.setTotalCount(metaDataResult.get(totalCountName).asInt());
			}
			// pageable count (�� ����) ������
			String pageableCountName = datasourceVO.getResponseColumn(CommonCode.META_PAGEABLE_COUNT);
			if(pageableCountName != null && metaDataResult.has(pageableCountName)) {
				metaDataVO.setPageableCount(metaDataResult.get(pageableCountName).asInt());
			}
			// is_end (������ ������ ����) ������
			String isEndName = datasourceVO.getResponseColumn(CommonCode.META_IS_END);
			if(isEndName != null && metaDataResult.has(isEndName)) {
				metaDataVO.setIsEnd(metaDataResult.get(isEndName).asBoolean());
			}
			// TODO : �����ͼҽ� Ȯ�� �� �ؿ� �߰�
			
		}
		else {
			// kakao API�� meta�� ������ �����Ǿ� ������, Naver���� ���� �����Ǿ� ���� ����.
			// ��, meta�� ������ �������� �ʰ� httpResulat ���� �״�� ������� ��� ���
			// total count (�� ����) ������
			String totalCountName = datasourceVO.getResponseColumn(CommonCode.META_TOTAL_COUNT);
			if(totalCountName != null && httpResult.has(totalCountName)) {
				metaDataVO.setTotalCount(httpResult.get(totalCountName).asInt());
			}
			// pageable count (�� ����) ������
			String pageableCountName = datasourceVO.getResponseColumn(CommonCode.META_PAGEABLE_COUNT);
			if(pageableCountName != null && httpResult.has(pageableCountName)) {
				metaDataVO.setPageableCount(httpResult.get(pageableCountName).asInt());
			}
			// is_end (������ ������ ����) ������
			String isEndName = datasourceVO.getResponseColumn(CommonCode.META_IS_END);
			if(isEndName != null && httpResult.has(isEndName)) {
				metaDataVO.setIsEnd(httpResult.get(isEndName).asBoolean());
			}
		}
		this.setMetaDataVO(metaDataVO);
		
		// httpResult���� documents Ȯ�� �� �ε�
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
