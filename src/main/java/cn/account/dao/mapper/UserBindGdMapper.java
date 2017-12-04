package cn.account.dao.mapper;

import org.springframework.stereotype.Repository;

import cn.account.bean.UserBindGd;
@Repository
public interface UserBindGdMapper {
	/**
	 * 添加或者更新高德用户信息
	 * @param userBind
	 * @return
	 */
	public int addOrUpdateLoginInfo(UserBindGd userBindGd);
}
