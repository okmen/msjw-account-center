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
	/**
	 * 根据手机号获取UserBindApp
	 * @param mobileNumber
	 * @return
	 */
	public UserBindApp getUserBindAppByPhone(String mobileNumber);
	/**
	 * 根据身份证号解绑app
	 * @param idCard
	 * @return
	 */
	public int unBindAppByIdCard(String idCard);
}
