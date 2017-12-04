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
}
