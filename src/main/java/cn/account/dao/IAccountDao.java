package cn.account.dao;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import cn.account.bean.ReadilyShoot;
import cn.account.bean.UserBind;
import cn.account.bean.UserOpenidBean;
import cn.account.bean.UserRegInfo;
import cn.account.bean.WechatUserInfoBean;
import cn.account.orm.DeviceORM;
import cn.account.orm.UsernameORM;

public interface IAccountDao {
	
	
	/**
	 * 通过id获取用户微信信息
	 * 
	 * @author 黄泽铭
	 * @param id
	 * @return
	 */
	WechatUserInfoBean getWechatUserInfoById(int id);
	
	List<WechatUserInfoBean> getAllWechatUserInfoBeanList();
	

	/**
	 * 插入微信用户信息
	 * 
	 * @author 黄泽铭
	 * @param wechatUserInfo
	 * @return 成功则返回纪录id，失败返回0
	 */
	int insertWechatUserInfo(WechatUserInfoBean wechatUserInfo);
	
	public int addOrUpdateLoginInfo(UserBind userBind);
	/**
	 * 添加新用户
	 * 
	 * @param userRegInfo
	 * @return
	 */
	public long addNewUser(UserRegInfo userRegInfo);

	/**
	 * 获取username最大值
	 * 
	 * @return
	 */
	public long getMaxUsername();
	
	/**
	 * 获取当前用户名表最大的id
	 * @author nishixiang
	 * @return
	 */
	public UsernameORM createUsername();
	
	/**
	 * 添加信息到表cm_user_openid
	 * @author shengfenglai
	 * @return long
	 */
	public long addBindOpenid(UserOpenidBean userOpenidBean);
	
	/**
	 * 更新绑定微信的状态
	 * @author shengfenglai
	 * @return long
	 */
	public long updateBindOpenidStatus(UserOpenidBean userOpenidBean);
	
	/**
	 * @author liuminkang
	 * @param userBind
	 * @return
	 */
	public int unbindVehicle(UserBind userBind);
	
	/**
	 * 通过openid拿到userId
	 * @param openid
	 * @return userId
	 */
	public Long getUserIdByOpenid(String openid);
	/**
	 * 登录绑定
	 * @param userBind
	 */
	public int addLoginInfo(UserBind userBind);
	/**
	 * 根据身份证。第三方登录id、客户端类型查询绑定
	 * @param identityCard 身份证
	 * @param openId 第三方登录id
	 * @param clientType 客户端类型
	 * @return
	 */
	public UserBind getLoginInfo(String identityCard,String openId,String clientType);
	/**
	 * 修改登录绑定isBind = 0
	 * @param identityCard 身份证
	 * @param openId 第三方登录id
	 * @param clientType 客户端类型
	 * @return
	 */
	public int updateUserBind(String identityCard,String openId,String clientType);
	
    /**
     * 通过userId拿到openid
     * 
     * @param userId
     * @return
     */
    public String getOpenidByUserId(long userId);
    
    
    /**
     * 获取Device
     * @param deviceUuid 设备号
     * @param osType 系统类型
     * @return
     */
    public DeviceORM getDevice(String deviceUuid,int osType);
    
    /**
     * 记录设备号
     * @param deviceUuid 设备号
     * @param osType 系统类型
     * @param userId 用户id
     */
    public void addDevice(String deviceUuid,int osType,long userId,long addTime);
    
    /**
     * 更新device
     * @param deviceUuid 设备号
     * @param osType 系统类型
     * @param userId 用户id
     * @return
     */
    public boolean updateDevice(String deviceUuid,int osType,long userId);
    
    /**
     * id 范围查询
     * @param startId
     * @param endId
     * @return
     */
	public List<UserBind> getBetweenAndId(String startId,String endId);
	/**
	 * 绑定时间范围查询
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public List<UserBind> getBetweenAndBindDate(String startDate,String endDate);
    
	/**
	 * 根据openid获取UserBind信息
	 * @param openId
	 * @return
	 */
	public UserBind queryUserBindByOpenid(String openId);
	
	/**
	 * 根据mobileNumber获取UserBind信息
	 * @param mobileNumber
	 * @return
	 */
	public List<UserBind> getUserBindByPhone(String mobileNumber);
}
