package cn.account.dao;

import java.util.List;

import cn.account.bean.UserBind;
import cn.account.bean.UserBindAlipay;
/**
 * 用户中心-支付宝
 * @author Mbenben
 *
 */
public interface IUserBindAlipayDao {
	/**
	 * 添加或更新支付宝用户信息
	 * @param userBind
	 * @return
	 */
	public int addOrUpdateLoginInfo(UserBind userBind);
	/**
	 * 解绑支付宝
	 * @param userBind
	 * @return
	 */
	public int unbindVehicle(UserBind userBind);
}
