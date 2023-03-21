package com.pre.search.blog.common.code;

/**
 * 
 * bSearch Application ������ ���������� ���Ǵ� Ű������� �����ϰ� �����ϱ� ���� Ŭ����
 * @author �̹ο�
 *
 */
public class CommonCode {
	
	
	// 1. DATASOURCE
	
	public final static String DATASOURCE_PROTOCOL = "protocol";
	public final static String DATASOURCE_HOST = "host";
	
	
	// 2. BLOG �˻� (īī�� API�� �������� �ʱ� ����)
	
	// 2-0. �˻� ��� datasource
	public final static String DATAOUSRCE_ID = "datasourceId";
	// 2-1. REQUEST QUERY PARAMETER
	// �˻��� ���ϴ� ���Ǿ�.	Ư�� ��α� �۸� �˻��ϰ� ���� ���, ��α� url�� �˻�� ����(' ') �����ڷ� ���� �� ���� (NOT NULL)
	public final static String QUERY = "query";
	// ��� ���� ���� ���, accuracy(��Ȯ����) �Ǵ� recency(�ֽż�), �⺻ �� accuracy
	public final static String SORT = "sort";
	// ��� ���� ���� ��� - recency / accuracy
	public final static String SORT_REC = "sortRec";
	public final static String SORT_ACC = "sortAcc";
	// ��� ������ ��ȣ
	public final static String OFFSET = "offset";
	// �� �������� ������ ���� ��
	public final static String LIMIT = "limit";
	
	// 2-2. RESPONSE ������
	public final static String META = "meta";
	// �˻��� ���� ��
	public final static String META_TOTAL_COUNT = "metaCount";
	// total_count �� ���� ���� ���� ��
	public final static String META_PAGEABLE_COUNT = "metaPageCount";
	// ���� �������� ������ ���������� ����, ���� false�� page�� �������� ���� �������� ��û�� �� ����
	public final static String META_IS_END = "metaIsEnd";
	
	public final static String DOCUMENTS = "document";
	// 	��α� �� ����
	public final static String DOCUMENTS_TITLE = "documentTitle";
	// 	��α� �� ���
	public final static String DOCUMENTS_CONTENTS = "documentContents";
	// 	��α� �� URL
	public final static String DOCUMENTS_URL = "documentUrl";
	// ��α��� �̸�
	public final static String DOCUMENTS_BLOG_NAME = "documentBlogname";
	// �˻� �ý��ۿ��� ������ ��ǥ �̸����� �̹��� URL, �̸����� ũ�� �� ȭ���� ����� �� ����
	public final static String DOCUMENTS_THUMBNAIL = "documentThumbnail";
	// ��α� �� �ۼ��ð�, ISO 8601	[YYYY]-[MM]-[DD]T[hh]:[mm]:[ss].000+[tz]
	public final static String DOCUMENTS_DATETIME = "documentDatetime";
	
	// TODO: ���� Ÿ API�� �����ϰ� �Ǹ� �Ʒ� ĭ�� �߰� �����ؼ� Ȯ��
	//////////////////////////////////////////////
	//////////////////////////////////////////////
	
	
	// 3. Keyword ���� Query
	// 3-1. Keyword get �� ������
	public final static String START_DATE = "startDate";
	public final static String END_DATE = "endDate";
	
	// 3-2. DB���� RETURN�� Ű��
	public final static String KEYWORD_COUNT = "cnt";
	public final static String KEYWORD_WORD = "word";
}
