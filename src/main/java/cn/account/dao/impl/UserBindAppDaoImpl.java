package cn.account.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.account.bean.UserBindApp;
import cn.account.dao.IUserBindAppDao;
import cn.account.dao.mapper.UserBindAppMapper;
@Repository
public class UserBindAppDaoImpl implements IUserBindAppDao {
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private UserBindAppMapper userBindAppMapper;
	
	@Override
	public int addOrUpdateLoginInfo(UserBindApp userBindApp) {
		return userBindAppMapper.addOrUpdateLoginInfo(userBindApp);
	}
	
}
