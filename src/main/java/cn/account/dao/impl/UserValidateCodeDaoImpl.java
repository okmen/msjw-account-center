package cn.account.dao.impl;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.account.bean.UserBind;
import cn.account.bean.po.UserValidateCodePo;
import cn.account.dao.IUserValidateCodeDao;
import cn.account.dao.mapper.UserValidateCodeMapper;
@Repository
public class UserValidateCodeDaoImpl implements IUserValidateCodeDao {
	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private UserValidateCodeMapper userValidateCodeMapper;
	
	
	@Override
	public int addUserValidateCode(UserValidateCodePo userValidateCodePo) {
		return userValidateCodeMapper.addUserValidateCode(userValidateCodePo);
	}

	@Override
	public UserBind getUserValidateCodeByMobilephone(String mobilephone) {
		return userValidateCodeMapper.getUserValidateCodeByMobilephone(mobilephone);
	}
	
}
