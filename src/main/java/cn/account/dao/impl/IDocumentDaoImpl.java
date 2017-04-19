package cn.account.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.account.dao.IDocumentDao;
import cn.account.dao.mapper.DocumentationMapper;
import cn.account.orm.DocumentationORM;
@Repository
public class IDocumentDaoImpl implements IDocumentDao {

	@Autowired
	private DocumentationMapper documentationMapper;
	
	@Override
	public DocumentationORM getDocumentationORMByNoticeKey(String noticeKey) throws Exception {
		return documentationMapper.getDocumentationORMByNoticeKey(noticeKey);
	}

	@Override
	public int addDocumentationORM(DocumentationORM documentationORM) {
		
		return 0;
	}
	
}
