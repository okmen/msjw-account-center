package cn.account.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import cn.account.bean.UserBind;
import cn.account.bean.UserOpenidBean;
import cn.account.bean.UserRegInfo;
import cn.account.bean.WechatUserInfoBean;
import cn.account.orm.DeviceORM;
import cn.account.orm.UsernameORM;
import cn.account.orm.WechatUserInfoORM;

@Repository
public interface AccountMapper {
	
	/**
	 * 插入微信用户信息
	 * @param wechatUserInfo
	 * @return 成功则返回纪录id，失败返回0
	 */
	int insertWechatUserInfo(WechatUserInfoBean wechatUserInfo);
	
    WechatUserInfoORM getWechatUserInfoById(@Param("id") int id);
	
	List<WechatUserInfoBean> getAllWechatUserInfoBeanList();
	

	public long addNewUser(UserRegInfo userRegInfo);

	public String getMaxUsername();

	/**
	 * 注册用户获取用户名称username
	 * @author nishixiang
	 * @param usernameORM
	 * @return
	 */
    public long createUsername(UsernameORM usernameORM);
    
    
    public long addBindOpenid(UserOpenidBean userOpenidBean);
    
    public long updateBindOpenidStatus(UserOpenidBean userOpenidBean);
    
    public int unbindVehicle(UserBind userBind);
    
    public int addOrUpdateLoginInfo(UserBind userBind);
    /**
     * 登录绑定
     * @param userBind
     * @return
     */
    public int addLoginInfo(UserBind userBind);
    /**
	 * 根据身份证。第三方登录id、客户端类型查询绑定
	 * @param idCard 身份证
	 * @param openId 第三方登录id
	 * @param clientType 客户端类型
	 * @return
	 */
	public UserBind getLoginInfo(@Param("idCard")String idCard,@Param("openId")String openId,@Param("clientType")String clientType);
	/**
	 * 修改登录绑定isBind = 0
	 * @param identityCard 身份证
	 * @param openId 第三方登录id
	 * @param clientType 客户端类型
	 * @return
	 */
	public int updateUserBind(@Param("idCard")String idCard,@Param("openId")String openId,@Param("clientType")String clientType);
    
    public Long getUserIdByOpenid(String openid);
    
    public String getOpenidByUserId(long userId);
    
    public DeviceORM getDevice(Map<String,Object> map);
    
    public void addDevice(DeviceORM deviceORM);
    
    public boolean updateDevice(DeviceORM deviceORM);
    
}
