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
	/**
	 * 根据手机号获取UserBindGd
	 * @param mobileNumber
	 * @return
	 */
	public UserBindGd getUserBindGdByPhone(String mobileNumber);
	/**
	 * 根据十分钟解绑高德
	 * @param idCard
	 * @return
	 */
	public int unBindGdByIdCard(String idCard);
}
