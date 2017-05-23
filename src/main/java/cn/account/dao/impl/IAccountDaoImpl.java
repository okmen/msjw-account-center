package cn.account.dao.impl;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import cn.account.bean.ReadilyShoot;
import cn.account.bean.UserBind;
import cn.account.bean.UserOpenidBean;
import cn.account.bean.UserRegInfo;
import cn.account.bean.WechatUserInfoBean;
import cn.account.dao.IAccountDao;
import cn.account.dao.mapper.AccountMapper;
import cn.account.orm.DeviceORM;
import cn.account.orm.UsernameORM;
import cn.account.orm.WechatUserInfoORM;

@Repository
public class IAccountDaoImpl implements IAccountDao {

	protected Logger log = Logger.getLogger(this.getClass());
	@Autowired
	private AccountMapper userMapper;
	
	
	@Override
	public int insertWechatUserInfo(WechatUserInfoBean wechatUserInfo) {
		userMapper.insertWechatUserInfo(wechatUserInfo);
		return wechatUserInfo.getId();
	}

	@Override
	public WechatUserInfoBean getWechatUserInfoById(int id) {
		WechatUserInfoORM orm = userMapper.getWechatUserInfoById(id);
	
		if (orm != null) {
			WechatUserInfoBean bean = new WechatUserInfoBean();
			BeanUtils.copyProperties(orm, bean);
			return bean;
		}
	
	return null;
	}
	
	public List<WechatUserInfoBean> getAllWechatUserInfoBeanList(){
		return userMapper.getAllWechatUserInfoBeanList();
	}

	@Override
	public long addNewUser(UserRegInfo userRegInfo) {
		return userMapper.addNewUser(userRegInfo);
	}

	@Override
	public long getMaxUsername() {
		return Long.valueOf(userMapper.getMaxUsername());
	}

    @Override
    public UsernameORM createUsername() {
        long addTime = System.currentTimeMillis()/1000;
        UsernameORM usernameORM = new UsernameORM();
        usernameORM.setAddTime(addTime);
        
        userMapper.createUsername(usernameORM);
        return usernameORM;
    }

    @Override
    public long addBindOpenid(UserOpenidBean userOpenidBean) {
        return userMapper.addBindOpenid(userOpenidBean);
    }

    @Override
    public long updateBindOpenidStatus(UserOpenidBean userOpenidBean) {
        return userMapper.updateBindOpenidStatus(userOpenidBean);
    }

    @Override
	public int unbindVehicle(UserBind userBind) {
    	 return userMapper.unbindVehicle(userBind);
	}
    
    @Override
    public Long getUserIdByOpenid(String openid) {
        return userMapper.getUserIdByOpenid(openid);
    }

    @Override
    public String getOpenidByUserId(long userId) {
        return userMapper.getOpenidByUserId(userId);
    }

    @Override
    public DeviceORM getDevice(String deviceUuid,int osType) {
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("deviceUuid",deviceUuid );
        map.put("osType",osType);
        return userMapper.getDevice(map);
    }

    @Override
    public void addDevice(String deviceUuid,int osType,long userId,long addTime) {
        DeviceORM deviceORM = new DeviceORM();
        deviceORM.setDeviceUuid(deviceUuid);
        deviceORM.setOsType(osType);
        deviceORM.setUserId(userId);
        deviceORM.setAddTime(addTime);
        userMapper.addDevice(deviceORM);
    }

    @Override
    public boolean updateDevice(String deviceUuid,int osType,long userId) {
        DeviceORM deviceORM = new DeviceORM();
        deviceORM.setDeviceUuid(deviceUuid);
        deviceORM.setOsType(osType);
        deviceORM.setUserId(userId);
        return userMapper.updateDevice(deviceORM);
    }

	@Override
	public int addLoginInfo(UserBind userBind) {
		return userMapper.addLoginInfo(userBind);
	}

	@Override
	public UserBind getLoginInfo(String identityCard, String openId, String clientType) {
		return userMapper.getLoginInfo(identityCard, openId, clientType);
	}

	@Override
	public int updateUserBind(String identityCard, String openId, String clientType) {
		return userMapper.updateUserBind(identityCard, openId, clientType);
	}
	
	@Override
	public int addOrUpdateLoginInfo(UserBind userBind){
		return userMapper.addOrUpdateLoginInfo(userBind);
	}

	@Override
	public List<UserBind> getBetweenAndId(String startId, String endId) {
		return userMapper.getBetweenAndId(startId, endId);
	}

	@Override
	public List<UserBind> getBetweenAndBindDate(String startDate, String endDate) {
		return userMapper.getBetweenAndBindDate(startDate, endDate);
	}

}
