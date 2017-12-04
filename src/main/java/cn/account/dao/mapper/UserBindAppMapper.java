package cn.account.dao.mapper;

import org.springframework.stereotype.Repository;

import cn.account.bean.UserBindApp;
@Repository
public interface UserBindAppMapper {
	/**
	 * 添加或者更新app用户信息
	 * @param userBind
	 * @return
	 */
	public int addOrUpdateLoginInfo(UserBindApp userBindApp);
}
