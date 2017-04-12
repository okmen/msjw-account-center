package cn.account.service.impl;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.account.bean.WechatUserInfoBean;
import cn.account.bean.vo.AuthenticationBasicInformationVo;
import cn.account.bean.vo.BindTheVehicleVo;
import cn.account.bean.vo.DriverLicenseInformationSheetVo;
import cn.account.bean.vo.DrivingLicenseVo;
import cn.account.bean.vo.ElectronicDriverLicenseVo;
import cn.account.bean.vo.IdentityVerificationAuditResultsVo;
import cn.account.bean.vo.LoginReturnBeanVo;
import cn.account.bean.vo.MotorVehicleInformationSheetVo;
import cn.account.bean.vo.MyDriverLicenseVo;
import cn.account.bean.vo.queryclassservice.CertificationProgressQueryVo;
import cn.account.bean.vo.queryclassservice.DriverLicenseBusinessVo;
import cn.account.bean.vo.queryclassservice.MakeAnAppointmentVo;
import cn.account.bean.vo.queryclassservice.MotorVehicleBusinessVo;
import cn.account.cached.impl.IAccountCachedImpl;
import cn.account.dao.IAccountDao;
import cn.account.service.IAccountService;
import cn.sdk.webservice.DESCorder;
import cn.sdk.webservice.WebServiceClient;
import cn.sdk.webservice.Xml2Json;
/**
 * 个人中心
 * @author Mbenben
 *
 */
@Service("accountService")
@SuppressWarnings(value="all")
public class IAccountServiceImpl implements IAccountService {
    private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IAccountDao accountDao;

	@Autowired
	private IAccountCachedImpl iAccountCached;
	
	
	@Override
	public int insertWechatUserInfo(WechatUserInfoBean wechatUserInfo) {
		int result = 0;	
		
		try {
			result = accountDao.insertWechatUserInfo(wechatUserInfo);
		} catch (Exception e) {
			logger.error("插入wechatUserInfo失败，错误 ＝ ", e);
			throw e;
		}
		
		return result;
	}


	@Override
	public WechatUserInfoBean getWechatUserInfoById(int id) {
		WechatUserInfoBean result = null;
		
		try {
			logger.debug("debug");
			logger.info("info");
			logger.error("error");
			result = accountDao.getWechatUserInfoById(id);
		} catch(Exception e) {
			logger.error("获取wechatUserInfoById失败， 错误 ＝ ", e);
			throw e;
		}
		return result;
	}
	
	@Override
	public List<WechatUserInfoBean> getAllWechatUserInfoBeanList(){
		List<WechatUserInfoBean> list = null;
		logger.debug("debug");
		logger.info("info");
		logger.error("error");
        try {
        	list = accountDao.getAllWechatUserInfoBeanList();
        } catch (Exception e) {
        	logger.error("获取列表失败, 错误  = ", e);
            throw e;
        }
        return list;
	}

	/**
	 * 登录
	 * @return
	 * @throws Exception 
	 */
	@Override
	public LoginReturnBeanVo login(String loginName,String password) throws Exception {
		loginName = "15920071829";
		password = "168321";
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><REQUEST><USERNAME>"+loginName+"</USERNAME><PWD>"+password+"</PWD><YHLY>WX_XCX</YHLY><SFZMHM></SFZMHM><XM></XM></REQUEST>";
		
		String interfaceNumber = "xxcj03";
		
		String respStr = WebServiceClient.getInstance().requestWebService(iAccountCached.getUrl(), iAccountCached.getMethod(), 
				interfaceNumber,xml,iAccountCached.getUserid(),iAccountCached.getUserpwd(),iAccountCached.getKey());
		
        
        
		AuthenticationBasicInformationVo authenticationBasicInformation = new AuthenticationBasicInformationVo();
    	authenticationBasicInformation.setMobilephone("13666666666");
    	authenticationBasicInformation.setMyAvatar("http://gaoboy.com/tpl/polo/Public/images/my_wechat_qrcode.jpg");
    	authenticationBasicInformation.setIdentityCard("431225199122222112");
    	authenticationBasicInformation.setMyNumberPlate("粤B868686");
    	authenticationBasicInformation.setTrueName("张小龙");
    	
    	
    	IdentityVerificationAuditResultsVo identityVerificationAuditResults = new IdentityVerificationAuditResultsVo();
    	identityVerificationAuditResults.setAuthenticationType(1);
    	identityVerificationAuditResults.setStatus(3);
    	identityVerificationAuditResults.setRetirementResult("身份证正面不清晰");
    	identityVerificationAuditResults.setReviewDate("2017-1-3 19:00:01");
    	
    	LoginReturnBeanVo loginReturnBean = new LoginReturnBeanVo();
    	loginReturnBean.setAuthenticationBasicInformation(authenticationBasicInformation);
    	loginReturnBean.setIdentityVerificationAuditResults(identityVerificationAuditResults);
		return loginReturnBean;
	}
	/**
	 * 获取机动车信息单
	 * @param identityCard 身份证号
	 * @return
	 */
	@Override
	public MotorVehicleInformationSheetVo getMotorVehicleInformationSheet(String identityCard) {
		
		return null;
	}
	/**
     * 提交机动车信息单
     * @param userName 姓名
     * @param identityCard 身份证号
     * @param mobilephone 联系电话 
     * @param provinceAbbreviation 车牌核发省简称 例如：粤
     * @param numberPlateNumber 号牌号码 例如：B701NR
     * @param plateType 车辆类型 例如:小型汽车
     */
	@Override
	public void commitMotorVehicleInformationSheet(String userName, String identityCard, String mobilephone,
			String provinceAbbreviation, String numberPlateNumber, String plateType) {
		
	}
	/**
	 * 获取驾驶证信息单
	 * @param identityCard 身份证号
	 * @return
	 */
	@Override
	public DriverLicenseInformationSheetVo getDriverLicenseInformationSheet(String identityCard) {
		
		return null;
	}
	/**
	 * 提交驾驶证信息单
	 * @param userName 姓名
	 * @param identityCard 身份证号
	 * @param mobilephone 联系电话
	 * @return
	 */
	@Override
	public DriverLicenseInformationSheetVo commitDriverLicenseInformationSheet(String userName, String identityCard,
			String mobilephone) {
		
		return null;
	}


	@Override
	public MakeAnAppointmentVo getMakeAnAppointment(int businessType, String reservationNumber, String identityCard) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void bookingCancellation(int businessType, String reservationNumber, String identityCard) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public CertificationProgressQueryVo getCertificationProgressQuery(int businessType, String identityCard,
			String serialNumber, String agencyCode) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<DriverLicenseBusinessVo> getDriverLicenseBusiness(String identityCard) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<MotorVehicleBusinessVo> getMotorVehicleBusiness(String identityCard) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public ElectronicDriverLicenseVo getElectronicDriverLicense(String driverLicenseNumber, String userName,
			String mobileNumber) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public DrivingLicenseVo getDrivingLicense(String numberPlatenumber, String plateType, String mobileNumber) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public MyDriverLicenseVo getMyDriverLicense(String identityCard) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public List<BindTheVehicleVo> getBndTheVehicles(String identityCard) {
		// TODO Auto-generated method stub
		return null;
	};

	
//	@Override
//	public UserRegInfo addNewUser(UserRegInfo userRegInfo) {
//		long addSuccess = 0;
//		try {
//		    UsernameORM usernameORM = accountDao.createUsername();
//		    String username = usernameORM.getId() + "";
//			userRegInfo.setNickname(this.creatNickName());
//			userRegInfo.setUsername(username);
//			addSuccess = accountDao.addNewUser(userRegInfo);
//		} catch (Exception e) {
//			logger.error("添加注册新用户失败，用户 手机号码 = " + userRegInfo.getMobilePhone(), e);
//			throw e;
//		}
//		if (addSuccess > 0) {
//			return userRegInfo;
//		} else {
//			return null;
//		}	
//	}
//
//	public String creatNickName() {
//		String headStr = IConfig.Nickname_Head_List[(int) (Math.random() * IConfig.Nickname_Head_List.length)];
//		String endStr = IConfig.Nickname_End_List[(int) (Math.random() * IConfig.Nickname_End_List.length)];
//		return headStr + endStr;
//	}
//	
//	public String getAccessTokenByUserId(long userId) {
//        Token token = accountCache.getToken(userId + "");
//        String accessToken = token.getAccessToken();
//        if (StringUtils.isBlank(accessToken)) {
//            logger.error("getAccessTf'fokenByUserId,userId=" + userId + ",accessToken is null");
//        }
//        return accessToken;
//    }
//
//    public String getAccessTokenFromEncypt(String encyptAccessToken) {
//        return accountCache.getAccessTokenFromEncypt(encyptAccessToken);
//    }
//
//    public void insertEncyptAccessToken(String encyptAccessToken, String accessToken) {
//        accountCache.insertEncyptAccessToken(encyptAccessToken, accessToken);
//    }
//    
//    public Token getAccessToken(long userId) {
//        String userIdStr = userId + "";
//        Token token = new Token();
//        String accessToken = TokenGenerater.generateAccessToken(userIdStr);
//        token.setAccessToken(accessToken);
//        String refreshToken = TokenGenerater.generateRefreshToken(userIdStr);
//        token.setRefreshToken(refreshToken);
//        token.setUserId(userIdStr);
//        if (isUserHasLogin(userId)) {
//            token.setIsLogin("true");
//            logger.warn(userId + "二次登陆" + "accessToken" + "-" + accessToken);
//        } else {
//            token.setIsLogin("false");
//        }
//        accountCache.insertToken(token);
//        return token;
//    }
//    
//    private boolean isUserHasLogin(long userId) {
//        boolean result = false;
//        Token token = accountCache.getToken(Long.toString(userId));
//        if (token.getAccessToken() != null) {
//            result = true;
//        }
//        return result;
//    }
//    
//    /**
//     * 检查accessToken是否过期
//     */
//    public boolean isAccessTokenValidate(String accessToken, String userId) {
//        if (StringUtils.isBlank(accessToken) || StringUtils.isBlank(userId)) {
//            return false;
//        }
//        boolean result = true;
//        Token token = accountCache.getToken(userId);
//        if (!accessToken.equals(token.getAccessToken())) {
//            logger.error("accessToken验证失败----isAccessTokenValidate----" + "accessToken:" + accessToken + "---" + "userId:" + userId);
//            result = false;
//        } else {
//            if(RandomUtils.isNeedExpire()){
//                accountCache.updateAllToken(userId);
//            }
//        }
//        return result;
//    }
//    
//    /**
//     * 通过refreshToken获取accessToken
//     */
//    public Map<String, String> getAccessTokenByRefreshToken(String userId, String refreshToken) {
//        Map<String, String> resultMap = new HashMap<String, String>();
//        Token token = accountCache.getToken(userId);
//        if (token.getRefreshToken() == null || !token.getRefreshToken().equals(refreshToken)) {
//            logger.error("refreshToken失效----getAccessTokenByRefreshToken----" + "refreshToken:" + refreshToken + "userId:" + userId);
//            resultMap.put("ERR", IConfig.ERR_REFRESH_TOKEN_INVALIDATE);
//        } else {
//            String accessToken = TokenGenerater.generateAccessToken(userId);
//            accountCache.updateAccessToken(userId, accessToken);
//            accountCache.updateRefreshToken(userId, refreshToken);
//            resultMap.put("accessToken", accessToken);
//        }
//        return resultMap;
//    }
//
//    /**
//     * 绑定微信
//     * @param userOpenidBean
//     * @author shengfenglai
//     * @return long
//     */
//    @Override
//    public long addBindOpenid(UserOpenidBean userOpenidBean) {
//        long addSuccess = 0;
//        if(userOpenidBean == null) {
//            logger.error("addBindOpenid接收参数有误");
//            return addSuccess;
//        }
//        try {
//            userOpenidBean.setStatus(1);
//            addSuccess = accountDao.addBindOpenid(userOpenidBean);
//        } catch (Exception e) {
//            logger.error("addBindOpenid插入数据失败，addSuccess=" + addSuccess);
//            throw e;
//        }
//        return addSuccess;
//    }
//
//    /**
//     * 取消绑定微信
//     * @param userOpenidBean
//     * @author shengfenglai
//     * @return long 
//     */
//    @Override
//    public long cancelBindOpenid(UserOpenidBean userOpenidBean) {
//        long cancelSuccess = 0;
//        if(userOpenidBean == null) {
//            logger.error("cancelBindOpenid接收参数有误");
//            return cancelSuccess;
//        }
//        try {
//            userOpenidBean.setStatus(2);
//            cancelSuccess = accountDao.updateBindOpenidStatus(userOpenidBean);
//        } catch (Exception e) {
//            logger.error("更新绑定状态失败，cancelSuccess = " + cancelSuccess);
//            throw e;
//        }
//        return cancelSuccess;
//    }
//
//    /**
//     * 通过openid拿到userId
//     * @param openid
//     * @return userId
//     * @author shengfenglai
//     */
//    @Override
//    public long getUserIdByOpenid(String openid) {
//        Long userId = 0L;
//        if(StringUtil.isBlank(openid)) {
//            logger.error("getUserIdByOpenid接收参数为空 ，openid=" + openid);
//            return userId.longValue();
//        } 
//        try {
//            userId = accountDao.getUserIdByOpenid(openid);
//        } catch (Exception e) {
//            logger.error("通过openid获取userId失败",e);
//            throw e;
//        }
//        return userId == null ? 0 : userId.longValue();
//    }
//
//    @Override
//    public String getOpenidByUserId(long userId) {
//        
//        String openid = null;
//        if(userId < 0) {
//            logger.error("getOpenidByUserId接收到的userId不对,userId=" + userId);
//            return "";
//        }
//        
//        try {
//            openid = accountDao.getOpenidByUserId(userId);
//        } catch (Exception e) {
//            logger.error("通过userId获取openid失败",e);
//            throw e;
//        }
//        
//        return openid == null ? "" : openid;
//    }
//
//    @Override
//    public DeviceBean getDevice(String deviceUuid, int osType) {
//        
//        if(StringUtils.isBlank(deviceUuid) || osType < 0) {
//            logger.error("getDevice：获取的参数不正确 ,deviceUuid:{},osType:{}",deviceUuid,osType);
//            return null;
//        }
//        
//        DeviceBean deviceBean = new DeviceBean();
//        try {
//            DeviceORM deviceORM = accountDao.getDevice(deviceUuid, osType);
//            if(deviceORM == null) {
//                deviceBean = null;
//            } else {
//                BeanUtils.copyProperties(deviceORM, deviceBean);
//            }
//        } catch (Exception e) {
//            logger.error("获取device信息失败",e);
//            throw e;
//        }
//        
//        return deviceBean;
//    }
//
//    @Override
//    public void addDevice(String deviceUuid, int osType, long userId) {
//        
//        if(StringUtils.isBlank(deviceUuid) || osType < 0 || userId < 0) {
//            logger.error("addDevice：获取的参数不正确 ,deviceUuid:{},osType:{}",deviceUuid,osType);
//            return ;
//        }
//        
//        try {
//            long addTime = System.currentTimeMillis() / 1000;//系统当前时间，单位:秒
//            accountDao.addDevice(deviceUuid, osType, userId, addTime);
//        } catch (Exception e) {
//            logger.error("添加设备号失败",e);
//            throw e;
//        }
//    }
//
//    
//    @Override
//    public boolean updateDevice(String deviceUuid,int osType,long userId) {
//        
//        if(StringUtils.isBlank(deviceUuid) || osType < 0 || userId < 0) {
//            logger.error("addDevice：获取的参数不正确 ,deviceUuid:{},osType:{}",deviceUuid,osType);
//            return false;
//        }
//        
//        boolean updateSuccess = false;
//        try {
//            updateSuccess = accountDao.updateDevice(deviceUuid, osType, userId);
//        } catch (Exception e) {
//            logger.error("更新device失败",e);
//            throw e;
//        }
//        return updateSuccess;
//    }

}
