package cn.account.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.account.bean.UserBindGd;
import cn.account.dao.IUserBindGdDao;
import cn.account.dao.mapper.UserBindGdMapper;
@Repository
public class UserBindGdDaoImpl implements IUserBindGdDao {
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private UserBindGdMapper userBindGdMapper;
	
	@Override
	public int addOrUpdateLoginInfo(UserBindGd userBindGd) {
		return userBindGdMapper.addOrUpdateLoginInfo(userBindGd);
	}

	@Override
	public UserBindGd getUserBindGdByPhone(String mobileNumber) {
		return userBindGdMapper.getUserBindGdByPhone(mobileNumber);
	}

	@Override
	public int unBindGdByIdCard(String idCard) {
		return userBindGdMapper.unBindGdByIdCard(idCard);
	}
	
}
