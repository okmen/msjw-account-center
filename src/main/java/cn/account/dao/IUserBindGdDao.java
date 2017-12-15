package cn.account.dao;

import cn.account.bean.UserBindGd;
/**
 * 用户中心-支付宝
 * @author Mbenben
 *
 */
public interface IUserBindGdDao {
	/**
	 * 添加或更新支付宝用户信息
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
