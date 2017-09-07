package cn.account.dao.mapper;

import java.util.List;

import org.springframework.stereotype.Repository;

import cn.account.bean.UserBind;
/**
 * 用户中心-支付宝
 * @author Mbenben
 *
 */
import cn.account.bean.UserBindAlipay;
@Repository
public interface UserBindAlipayMapper {
	/**
	 * 添加或者更新支付宝用户信息
	 * @param userBind
	 * @return
	 */
	public int addOrUpdateLoginInfo(UserBindAlipay userBindAlipay);
	/**
	 * 解绑支付宝
	 * @param userBind
	 * @return
	 */
	public int unbindVehicle(UserBindAlipay userBindAlipay);
}
