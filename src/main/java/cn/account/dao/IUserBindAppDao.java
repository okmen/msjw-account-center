package cn.account.dao;

import cn.account.bean.UserBindApp;
/**
 * 用户中心-支付宝
 * @author Mbenben
 *
 */
public interface IUserBindAppDao {
	/**
	 * 添加或更新支付宝用户信息
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
