package cn.account.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.account.bean.UserBind;
import cn.account.bean.UserBindAlipay;
import cn.account.dao.IUserBindAlipayDao;
import cn.account.dao.mapper.UserBindAlipayMapper;
@Repository
public class UserBindAlipayDaoImpl implements IUserBindAlipayDao {
	protected Logger log = Logger.getLogger(this.getClass());
	
	@Autowired
	private UserBindAlipayMapper userBindAlipayMapper;
	
	@Override
	public int addOrUpdateLoginInfo(UserBind userBind) {
		return userBindAlipayMapper.addOrUpdateLoginInfo(userBind);
	}
	/**
	 * 解绑支付宝
	 * @param userBind
	 * @return
	 */
	public int unbindVehicle(UserBind userBind){
		return userBindAlipayMapper.unbindVehicle(userBind);
	}
	
}
