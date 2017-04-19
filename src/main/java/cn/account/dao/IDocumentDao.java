package cn.account.dao;

import cn.account.orm.DocumentationORM;

public interface IDocumentDao {
	/**
	 * 根据须知key查询须知文档
	 * @param noticeKey 须知key
	 * @return
	 * @throws Exception
	 */
	public DocumentationORM getDocumentationORMByNoticeKey(String noticeKey)throws Exception;/**
	 * 添加须知文档
	 * @param documentationORM
	 * @return
	 */
	public int addDocumentationORM(DocumentationORM documentationORM);
}
