package cn.account.dao.mapper;

import org.springframework.stereotype.Repository;
/**
 * 须知文档
 * @author Mbenben
 *
 */

import cn.account.orm.DocumentationORM;
/**
 * 须知文档
 * @author Mbenben
 *
 */
@Repository
public interface DocumentationMapper {
	/**
	 * 根据须知key查询须知文档
	 * @param noticeKey 须知key
	 * @return
	 * @throws Exception
	 */
	public DocumentationORM getDocumentationORMByNoticeKey(String noticeKey)throws Exception;
	/**
	 * 添加须知文档
	 * @param documentationORM
	 * @return
	 */
	public int addDocumentationORM(DocumentationORM documentationORM);
}
