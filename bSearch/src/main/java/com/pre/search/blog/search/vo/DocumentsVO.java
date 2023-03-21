package com.pre.search.blog.search.vo;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * Blog �˻� ��� ��� VO
 * @author �̹ο�
 *
 */
@Getter
@Setter
@ToString
public class DocumentsVO implements Serializable {

	private static final long serialVersionUID = -2929789292155268166L;

	private String title;
	private String contents;
	private String url;
	private String blogname;
	private String thumbnail;
	
	// ����� Dateformat�� ������ ������,
	// Ȯ�强�� ����ϸ� � format�� Date�� ���� �� �𸣴�
	// String���� ��ȯ.
	private String datetime;
}
