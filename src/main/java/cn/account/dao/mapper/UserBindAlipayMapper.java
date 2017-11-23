package cn.account.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

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
	/**
	 * 查询
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<UserBindAlipay> getUserBindAlipays(@Param("page") int page,@Param("pageSize") int pageSize);
	
	/**
	 * 根据Userid获取UserBindAlipay信息
	 * @param userId
	 * @return
	 */
    public UserBindAlipay queryUserBindAlipayByUserid(String userId);
    
    /**
     * 根据mobileNumber获取UserBindAlipay信息
     * @param userId
     * @return
     */
    public UserBindAlipay getUserBindAlipayByPhone(String mobileNumber);
}
