package cn.account.service.impl;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.account.bean.Car;
import cn.account.bean.Documentation;
import cn.account.bean.ReadilyShoot;
import cn.account.bean.ResultOfReadilyShoot;
import cn.account.bean.UserBind;
import cn.account.bean.UserBindAlipay;
import cn.account.bean.WechatUserInfoBean;
import cn.account.bean.vo.AuthenticationBasicInformationVo;
import cn.account.bean.vo.BindCarVo;
import cn.account.bean.vo.BindCompanyCarVo;
import cn.account.bean.vo.BindDriverLicenseVo;
import cn.account.bean.vo.BindTheVehicleVo;
import cn.account.bean.vo.BrushFaceVo;
import cn.account.bean.vo.CompanyRegisterVo;
import cn.account.bean.vo.DriverLicenseInformationSheetVo;
import cn.account.bean.vo.DriverLicenseToSupplementThePermitBusinessVo;
import cn.account.bean.vo.DrivingLicenseVo;
import cn.account.bean.vo.ElectronicDriverLicenseVo;
import cn.account.bean.vo.IdentificationOfAuditResultsVo;
import cn.account.bean.vo.InformationCollectionVo;
import cn.account.bean.vo.InformationSheetVo;
import cn.account.bean.vo.LoginReturnBeanVo;
import cn.account.bean.vo.MotorVehicleBusiness;
import cn.account.bean.vo.MotorVehicleInformationSheetVo;
import cn.account.bean.vo.MyBusinessVo;
import cn.account.bean.vo.MyDriverLicenseVo;
import cn.account.bean.vo.ReadilyShootVo;
import cn.account.bean.vo.ReauthenticationVo;
import cn.account.bean.vo.RegisterVo;
import cn.account.bean.vo.ResultOfBIndDriverLicenseVo;
import cn.account.bean.vo.UnbindTheOtherDriverUseMyCarVo;
import cn.account.bean.vo.UnbindVehicleVo;
import cn.account.bean.vo.UserBasicVo;
import cn.account.bean.vo.VehicleBindAuditResultVo;
import cn.account.bean.vo.queryclassservice.CertificationProgressQueryVo;
import cn.account.bean.vo.queryclassservice.DriverLicenseBusinessVo;
import cn.account.bean.vo.queryclassservice.MakeAnAppointmentVo;
import cn.account.bean.vo.queryclassservice.MotorVehicleBusinessVo;
import cn.account.cached.ICacheKey;
import cn.account.cached.impl.IAccountCachedImpl;
import cn.account.dao.IAccountDao;
import cn.account.dao.IDocumentDao;
import cn.account.dao.IReadilyShootDao;
import cn.account.dao.IUserBindAlipayDao;
import cn.account.dao.IUserValidateCodeDao;
import cn.account.dao.mapper.AccountMapper;
import cn.account.dao.mapper.UserBindAlipayMapper;
import cn.account.orm.DocumentationORM;
import cn.account.orm.UserValidateCodeORM;
import cn.account.service.IAccountService;
import cn.account.utils.NozzleMeans;
import cn.account.utils.TransferThirdParty;
import cn.sdk.bean.BaseBean;
import cn.sdk.util.MsgCode;
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
	private IReadilyShootDao readilyShootDao;
	
	@Autowired
	private IUserBindAlipayDao userBindAlipayDao;
	@Autowired
	private IAccountCachedImpl iAccountCached;
	
	@Autowired
	private IUserValidateCodeDao userValidateCodeDao;
	@Autowired
	private IDocumentDao documentDao;
	
	@Autowired
	private UserBindAlipayMapper userBindAlipayMapper;
	
	@Autowired
	private AccountMapper accountMapper;
	
	
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
	@Override
	public Documentation getDocumentationByNoticeKey(String noticeKey) throws Exception {
		Documentation documentation = new Documentation();
		try {
			//根据key查询redis，没有数据则查询mysql并把数据存到redis
			String documentJson = iAccountCached.getDocumentByKey(ICacheKey.ACCOUNT_DOC + noticeKey);
			if(StringUtils.isBlank(documentJson)){
				DocumentationORM documentationORM = documentDao.getDocumentationORMByNoticeKey(noticeKey);
				if(null != documentationORM){
					BeanUtils.copyProperties(documentationORM, documentation);
					documentJson = JSON.toJSONString(documentation);
					iAccountCached.setDucoment(ICacheKey.ACCOUNT_DOC + noticeKey, documentJson);
				}
			}else{
				documentation = JSON.parseObject(documentJson, Documentation.class);
			}
		} catch (Exception e) {
			logger.error("查询须知文档错误，参数noticeKey = " + noticeKey, e);
			throw e;
		}
		return documentation;
	}
	@Override
	public Map<String, String> resetPwd(String idCard, String userName, String mobile, String sourceOfCertification)throws Exception {
		Map<String, String> map = null; 
		try {
			String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			map = TransferThirdParty.resetPwd(idCard, userName, mobile, sourceOfCertification, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("重置密码错误" + "idCard=" + idCard + ",sourceOfCertification=" + sourceOfCertification + ",mobilephone" + mobile + ",userName=" + userName, e);
			throw e;
		}
		return map;
	}
	/*====基础数据服务=====*/
	/**
	 * 查询用户基础数据
	 * @param identityCard
	 * @param sourceOfCertification
	 * @param mobilephone
	 * @return
	 * @throws Exception 
	 */
	@Override
	public AuthenticationBasicInformationVo getAuthenticationBasicInformation(String identityCard,String sourceOfCertification,String mobilephone) throws Exception{
		AuthenticationBasicInformationVo authenticationBasicInformationVo = new AuthenticationBasicInformationVo();
		try {
			String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			
			//认证基本信息查询接口
			authenticationBasicInformationVo = TransferThirdParty.authenticationBasicInformationQuery(identityCard,sourceOfCertification, url, method,userId,userPwd,key);
			
			//我绑定的车辆信息
			List<BindTheVehicleVo> bindTheVehicleVos = TransferThirdParty.bindsTheMotorVehicleQuery(mobilephone,identityCard, sourceOfCertification, url, method, userId, userPwd, key);
			if(null != bindTheVehicleVos && bindTheVehicleVos.size() > 0){
				for(BindTheVehicleVo bindTheVehicleVo : bindTheVehicleVos){
					String isMyself = bindTheVehicleVo.getIsMyself();
					if("本人".equals(isMyself)){
						authenticationBasicInformationVo.setMyNumberPlate(bindTheVehicleVo.getNumberPlateNumber());
						authenticationBasicInformationVo.setBehindTheFrame4Digits(bindTheVehicleVo.getBehindTheFrame4Digits());
						authenticationBasicInformationVo.setPlateType(bindTheVehicleVo.getPlateType());
					}
				}
			}
		} catch (Exception e) {
			logger.error("查询用户基础数据错误" + "identityCard=" + identityCard + ",sourceOfCertification=" + sourceOfCertification + ",mobilephone" + mobilephone, e);
			throw e;
		}
		return authenticationBasicInformationVo;
	}
	
	/*====基础数据服务=====*/
	/**
	 * 登录
	 * @return
	 * @throws Exception 
	 */
	@Override
	public LoginReturnBeanVo login(String loginName,String password,String sourceOfCertification,String openId,String loginClient) throws Exception {
		LoginReturnBeanVo loginReturnBean = new LoginReturnBeanVo();
		
		String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
		String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
		String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
		String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
		String key = iAccountCached.getKey(sourceOfCertification); //秘钥
		String identityCard = "";
		String mobilephone = "";
		AuthenticationBasicInformationVo authenticationBasicInformationVo = null;
		List<Car> cars = new ArrayList<Car>();
		try {
			//用户登录接口
			Map<String, String> map = TransferThirdParty.login(loginName, password, url, method, userId, userPwd, key,sourceOfCertification);
			String code = map.get("code");
			String msg = map.get("msg");
			//已绑定机动车查询接口
			List<BindTheVehicleVo> bindTheVehicleVos = null;
			if(null != map && "0000".equals(map.get("code"))){
				//认证基本信息查询接口
				authenticationBasicInformationVo = TransferThirdParty.authenticationBasicInformationQuery(loginName,sourceOfCertification, url, method,userId,userPwd,key);
				identityCard = authenticationBasicInformationVo.getIdentityCard();
				mobilephone = authenticationBasicInformationVo.getMobilephone();
				//我绑定的车辆信息
				bindTheVehicleVos = TransferThirdParty.bindsTheMotorVehicleQuery(mobilephone,identityCard, sourceOfCertification, url, method, userId, userPwd, key);
				if(null != bindTheVehicleVos && bindTheVehicleVos.size() > 0){
					for(BindTheVehicleVo bindTheVehicleVo : bindTheVehicleVos){
						String isMyself = bindTheVehicleVo.getIsMyself();
						//绑定的车的信息
						Car car = new Car();
						if("本人".equals(isMyself)){
							authenticationBasicInformationVo.setMyNumberPlate(bindTheVehicleVo.getNumberPlateNumber());
							authenticationBasicInformationVo.setBehindTheFrame4Digits(bindTheVehicleVo.getBehindTheFrame4Digits());
							authenticationBasicInformationVo.setPlateType(bindTheVehicleVo.getPlateType());
							car.setIsMySelf(0);
						}else{
							car.setIsMySelf(1);
						}
						car.setBehindTheFrame4Digits(bindTheVehicleVo.getBehindTheFrame4Digits());
						car.setMyNumberPlate(bindTheVehicleVo.getNumberPlateNumber());
						car.setPlateType(bindTheVehicleVo.getPlateType());
						car.setName(bindTheVehicleVo.getName());
						String identityCardv = bindTheVehicleVo.getIdentityCard();
						AuthenticationBasicInformationVo authenticationBasicInformationVo2 = TransferThirdParty.authenticationBasicInformationQuery(identityCardv, sourceOfCertification, url, method, userId, userPwd, key);
						car.setIdentityCard(identityCardv);
						if(null != authenticationBasicInformationVo2 && StringUtils.isNotBlank(authenticationBasicInformationVo2.getMobilephone()) ){
							car.setMobilephone(authenticationBasicInformationVo2.getMobilephone());
						}else{
							car.setMobilephone("");
						}
						cars.add(car);
					}
				}
				loginReturnBean.setCode(code);
				loginReturnBean.setMsg(msg);
				
				UserBind userBind = new UserBind();
				userBind.setBindDate(new Date());
				userBind.setIdCard(identityCard);
				userBind.setMobileNumber(mobilephone);
				userBind.setIsBind(0);
				userBind.setClientType(sourceOfCertification);
				if("C".equals(sourceOfCertification)){
					userBind.setOpenId(openId);
					accountDao.addOrUpdateLoginInfo(userBind);
				}else if("Z".equals(sourceOfCertification)){
					userBind.setUserId(openId);
					userBindAlipayDao.addOrUpdateLoginInfo(null);
				}
				//登录成功绑定，已经绑定就改下状态为isBind=1,没有则绑定
				//UserBind userBind = accountDao.getLoginInfo(identityCard, openId, sourceOfCertification);
				/*if(null == userBind){
					
				}else{
					accountDao.updateUserBind(identityCard,openId,loginClient);
				}*/
				MyDriverLicenseVo myDriverLicenseVo = getMyDriverLicense(identityCard, sourceOfCertification);
				if(null != myDriverLicenseVo){
					String fileNumber = myDriverLicenseVo.getFileNumber();
					if(StringUtils.isBlank(fileNumber)){
						loginReturnBean.setFileNumber("");
					}else{
						loginReturnBean.setFileNumber(fileNumber);
					}
				}else{
					loginReturnBean.setFileNumber("");
				}
			}else {
				//登录失败
				loginReturnBean.setCode(code);
				loginReturnBean.setMsg(msg);
			}
	    	loginReturnBean.setAuthenticationBasicInformation(authenticationBasicInformationVo);
	    	loginReturnBean.setCars(cars);
		} catch (Exception e) {
			logger.error("login 错误,  loginName=" + loginName + ",password=" + password + ",sourceOfCertification=" + sourceOfCertification + ",openId="+openId + ",loginClient=" + loginClient, e);
			throw e;
		}
    	//登录信息入库
    	return loginReturnBean;
	}
	
	@Override
	public LoginReturnBeanVo alipayLogin(String loginName, String sourceOfCertification, String openId) throws Exception {
		LoginReturnBeanVo loginReturnBean = new LoginReturnBeanVo();
		
		String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
		String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
		String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
		String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
		String key = iAccountCached.getKey(sourceOfCertification); //秘钥
		
		String identityCard = "";
		String mobilephone = "";
		AuthenticationBasicInformationVo authenticationBasicInformationVo = null;
		List<Car> cars = new ArrayList<Car>();
		try {
			//已绑定机动车查询接口
			List<BindTheVehicleVo> bindTheVehicleVos = null;
				//认证基本信息查询接口
			authenticationBasicInformationVo = TransferThirdParty.authenticationBasicInformationQuery(loginName,sourceOfCertification, url, method,userId,userPwd,key);
			if(null != authenticationBasicInformationVo){
				if(!"已激活".equals(authenticationBasicInformationVo.getZt())){
					return null;
				}
				identityCard = authenticationBasicInformationVo.getIdentityCard();
				/*if("Z".equals(sourceOfCertification)){
					List<IdentificationOfAuditResultsVo> identificationOfAuditResultsVos = null;
					if(StringUtils.isNotBlank(identityCard)){
						identificationOfAuditResultsVos = getIdentificationOfAuditResults(identityCard, sourceOfCertification);
						if(null != identificationOfAuditResultsVos && identificationOfAuditResultsVos.size() > 0){
							List<String> shzts = new ArrayList<>();
							for(IdentificationOfAuditResultsVo identificationOfAuditResultsVo : identificationOfAuditResultsVos){
								String SHZT = identificationOfAuditResultsVo.getSHZT();
								shzts.add(SHZT);
							}
							if(!shzts.contains("1") && !shzts.contains("-1")){
								return null;
							}
						}else{
							return null;
						}
					}
				}*/
				
				mobilephone = authenticationBasicInformationVo.getMobilephone();
				String trueName = authenticationBasicInformationVo.getTrueName();
				//我绑定的车辆信息
				bindTheVehicleVos = TransferThirdParty.bindsTheMotorVehicleQuery(mobilephone,identityCard, sourceOfCertification, url, method, userId, userPwd, key);
				if(null != bindTheVehicleVos && bindTheVehicleVos.size() > 0){
					for(BindTheVehicleVo bindTheVehicleVo : bindTheVehicleVos){
						String isMyself = bindTheVehicleVo.getIsMyself();
						//绑定的车的信息
						Car car = new Car();
						if("本人".equals(isMyself)){
							authenticationBasicInformationVo.setMyNumberPlate(bindTheVehicleVo.getNumberPlateNumber());
							authenticationBasicInformationVo.setBehindTheFrame4Digits(bindTheVehicleVo.getBehindTheFrame4Digits());
							authenticationBasicInformationVo.setPlateType(bindTheVehicleVo.getPlateType());
							car.setIsMySelf(0);
						}else{
							car.setIsMySelf(1);
						}
						car.setBehindTheFrame4Digits(bindTheVehicleVo.getBehindTheFrame4Digits());
						car.setMyNumberPlate(bindTheVehicleVo.getNumberPlateNumber());
						car.setPlateType(bindTheVehicleVo.getPlateType());
						car.setName(bindTheVehicleVo.getName());
						String identityCardv = bindTheVehicleVo.getIdentityCard();
						AuthenticationBasicInformationVo authenticationBasicInformationVo2 = TransferThirdParty.authenticationBasicInformationQuery(identityCardv, sourceOfCertification, url, method, userId, userPwd, key);
						car.setIdentityCard(identityCardv);
						if(null != authenticationBasicInformationVo2 && StringUtils.isNotBlank(authenticationBasicInformationVo2.getMobilephone()) ){
							car.setMobilephone(authenticationBasicInformationVo2.getMobilephone());
						}else{
							car.setMobilephone("");
						}
						cars.add(car);
					}
				}
				/*if(null != bindTheVehicleVos && bindTheVehicleVos.size() > 0){
					for(BindTheVehicleVo bindTheVehicleVo : bindTheVehicleVos){
						String isMyself = bindTheVehicleVo.getIsMyself();
						//绑定的车的信息
						Car car = new Car();
						if("本人".equals(isMyself)){
							authenticationBasicInformationVo.setMyNumberPlate(bindTheVehicleVo.getNumberPlateNumber());
							authenticationBasicInformationVo.setBehindTheFrame4Digits(bindTheVehicleVo.getBehindTheFrame4Digits());
							authenticationBasicInformationVo.setPlateType(bindTheVehicleVo.getPlateType());
							car.setIsMySelf(0);
						}else{
							car.setIsMySelf(1);
						}
						car.setBehindTheFrame4Digits(bindTheVehicleVo.getBehindTheFrame4Digits());
						car.setMyNumberPlate(bindTheVehicleVo.getNumberPlateNumber());
						car.setPlateType(bindTheVehicleVo.getPlateType());
						car.setMobilephone(mobilephone);
						cars.add(car);
					}
				}*/
				loginReturnBean.setCode("0000");
				loginReturnBean.setMsg("登录成功");
				
				UserBindAlipay userBindAlipay = new UserBindAlipay();
				userBindAlipay.setBindDate(new Date());
				userBindAlipay.setIdCard(identityCard);
				userBindAlipay.setMobileNumber(mobilephone);
				userBindAlipay.setIsBind(0);
				userBindAlipay.setClientType(sourceOfCertification);
				userBindAlipay.setUserId(openId);
				userBindAlipay.setRealName(trueName);
				userBindAlipayDao.addOrUpdateLoginInfo(userBindAlipay);
				
				MyDriverLicenseVo myDriverLicenseVo = getMyDriverLicense(identityCard, sourceOfCertification);
				if(null != myDriverLicenseVo){
					String fileNumber = myDriverLicenseVo.getFileNumber();
					if(StringUtils.isBlank(fileNumber)){
						loginReturnBean.setFileNumber("");
					}else{
						loginReturnBean.setFileNumber(fileNumber);
					}
				}else{
					loginReturnBean.setFileNumber("");
				}
				loginReturnBean.setAuthenticationBasicInformation(authenticationBasicInformationVo);
				loginReturnBean.setCars(cars);
			}else{
				return null;
			}	
		} catch (Exception e) {
			logger.error("login 错误,  loginName=" + loginName + ",sourceOfCertification=" + sourceOfCertification + ",openId="+openId,e);
			throw e;
		}
    	//登录信息入库
    	return loginReturnBean;
	}
	@Override
	public LoginReturnBeanVo getLoginInfoByLoginName(String loginName, String sourceOfCertification) throws Exception {
		LoginReturnBeanVo loginReturnBean = new LoginReturnBeanVo();
		
		String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
		String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
		String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
		String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
		String key = iAccountCached.getKey(sourceOfCertification); //秘钥
		String identityCard = "";
		String mobilephone = "";
		AuthenticationBasicInformationVo authenticationBasicInformationVo = null;
		List<Car> cars = new ArrayList<Car>();
		try {
			//已绑定机动车查询接口
			List<BindTheVehicleVo> bindTheVehicleVos = null;
				//认证基本信息查询接口
			authenticationBasicInformationVo = TransferThirdParty.authenticationBasicInformationQuery(loginName,sourceOfCertification, url, method,userId,userPwd,key);
			if(null != authenticationBasicInformationVo){
				identityCard = authenticationBasicInformationVo.getIdentityCard();
				
				if("Z".equals(sourceOfCertification)){
					List<IdentificationOfAuditResultsVo> identificationOfAuditResultsVos = null;
					if(StringUtils.isNotBlank(identityCard)){
						identificationOfAuditResultsVos = getIdentificationOfAuditResults(identityCard, sourceOfCertification);
						if(null != identificationOfAuditResultsVos && identificationOfAuditResultsVos.size() > 0){
							List<String> shzts = new ArrayList<>();
							for(IdentificationOfAuditResultsVo identificationOfAuditResultsVo : identificationOfAuditResultsVos){
								String SHZT = identificationOfAuditResultsVo.getSHZT();
								shzts.add(SHZT);
							}
							if(!shzts.contains("1") && !shzts.contains("-1")){
								return null;
							}
						}else{
							return null;
						}
					}
				}
				
				mobilephone = authenticationBasicInformationVo.getMobilephone();
				//我绑定的车辆信息
				bindTheVehicleVos = TransferThirdParty.bindsTheMotorVehicleQuery(mobilephone,identityCard, sourceOfCertification, url, method, userId, userPwd, key);
				if(null != bindTheVehicleVos && bindTheVehicleVos.size() > 0){
					for(BindTheVehicleVo bindTheVehicleVo : bindTheVehicleVos){
						String isMyself = bindTheVehicleVo.getIsMyself();
						//绑定的车的信息
						Car car = new Car();
						if("本人".equals(isMyself)){
							authenticationBasicInformationVo.setMyNumberPlate(bindTheVehicleVo.getNumberPlateNumber());
							authenticationBasicInformationVo.setBehindTheFrame4Digits(bindTheVehicleVo.getBehindTheFrame4Digits());
							authenticationBasicInformationVo.setPlateType(bindTheVehicleVo.getPlateType());
							car.setIsMySelf(0);
						}else{
							car.setIsMySelf(1);
						}
						car.setBehindTheFrame4Digits(bindTheVehicleVo.getBehindTheFrame4Digits());
						car.setMyNumberPlate(bindTheVehicleVo.getNumberPlateNumber());
						car.setPlateType(bindTheVehicleVo.getPlateType());
						car.setName(bindTheVehicleVo.getName());
						String identityCardv = bindTheVehicleVo.getIdentityCard();
						AuthenticationBasicInformationVo authenticationBasicInformationVo2 = TransferThirdParty.authenticationBasicInformationQuery(identityCardv, sourceOfCertification, url, method, userId, userPwd, key);
						car.setIdentityCard(identityCardv);
						if(null != authenticationBasicInformationVo2 && StringUtils.isNotBlank(authenticationBasicInformationVo2.getMobilephone()) ){
							car.setMobilephone(authenticationBasicInformationVo2.getMobilephone());
						}else{
							car.setMobilephone("");
						}
						cars.add(car);
					}
				}
				loginReturnBean.setCode("0000");
				loginReturnBean.setMsg("登录成功");
				MyDriverLicenseVo myDriverLicenseVo = getMyDriverLicense(identityCard, sourceOfCertification);
				if(null != myDriverLicenseVo){
					String fileNumber = myDriverLicenseVo.getFileNumber();
					if(StringUtils.isBlank(fileNumber)){
						loginReturnBean.setFileNumber("");
					}else{
						loginReturnBean.setFileNumber(fileNumber);
					}
				}else{
					loginReturnBean.setFileNumber("");
				}
				loginReturnBean.setAuthenticationBasicInformation(authenticationBasicInformationVo);
				loginReturnBean.setCars(cars);
			}else{
				return null;
			}	
		} catch (Exception e) {
			logger.error("getLoginInfoByLoginName 错误,  loginName=" + loginName + ",sourceOfCertification=" + sourceOfCertification,e);
			throw e;
		}
    	//登录信息入库
    	return loginReturnBean;
	}
	
	/**
	 * 根据数据和状态过滤
	 * @param myBusinessVos
	 * @param status
	 * @return
	 */
	private List<MyBusinessVo> getMyBusinessVoByStatus(List<MyBusinessVo> myBusinessVos,int status){
		List<MyBusinessVo> returnMyBusinessVo = new ArrayList<MyBusinessVo>();
		try {
			for(MyBusinessVo myBusinessVo : myBusinessVos){
				if(null != myBusinessVo.getStatus() && status == myBusinessVo.getStatus()){
					returnMyBusinessVo.add(myBusinessVo);
				}
			}
		} catch (Exception e) {
			logger.error("getMyBusinessVoByStatus 错误， myBusinessVos=" + myBusinessVos.toString() + ",status=" + status, e);
			throw e;
		}
		return returnMyBusinessVo;
	}
	/**
	 * 认证基本信息查询接口
	 * @param idCard 身份证
	 * @param sourceOfCertification 认证来源
	 * @return
	 * @throws Exception
	 */
	public AuthenticationBasicInformationVo authenticationBasicInformationQuery(String idCard,String sourceOfCertification) throws Exception{
		AuthenticationBasicInformationVo authenticationBasicInformationVo = new AuthenticationBasicInformationVo();
		try {
			String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			
			authenticationBasicInformationVo = TransferThirdParty.authenticationBasicInformationQuery(idCard,sourceOfCertification, url, method,userId,userPwd,key);
		} catch (Exception e) {
			logger.error("authenticationBasicInformationQuery 错误,idCard=" + idCard + ",sourceOfCertification=" + sourceOfCertification,  e);
			throw e;
		}
		return authenticationBasicInformationVo;
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
	 * 
	 * @param applyType
	 * @param identityCard
	 * @param sourceOfCertification
	 * @return
	 * @throws Exception 
	 */
	public Map<String, Object> queryMachineInformationSheet(String applyType, String identityCard,String sourceOfCertification) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			map = TransferThirdParty.queryMachineInformationSheet(applyType, identityCard, sourceOfCertification, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("查询机动车信息单进度 错误，applyType=" + applyType + ",identityCard="+identityCard+",sourceOfCertification="+sourceOfCertification, e);
			throw e;
		}
		return map;
	}
	
	/**
     * 提交机动车信息单
     * @param userName 姓名
     * @param identityCard 身份证号
     * @param mobilephone 联系电话 
     * @param provinceAbbreviation 车牌核发省简称 例如：粤
     * @param numberPlateNumber 号牌号码 例如：B701NR
     * @param plateType 车辆类型 例如:小型汽车
     * @param sourceOfCertification 认证来源 微信-C
	 * @throws Exception 
     */
	@Override
	public Map<String, String> commitMotorVehicleInformationSheet(String userName, String identityCard, String mobilephone,
			String provinceAbbreviation, String numberPlateNumber, String plateType,String sourceOfCertification) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		try {
			String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			map = TransferThirdParty.commitAAingleApplicationForMotorVehicleInformation(userName, identityCard, mobilephone,
					numberPlateNumber, plateType, sourceOfCertification, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("commitMotorVehicleInformationSheet错误，userName="+userName+",identityCard="+identityCard+",mobilephone="+mobilephone+",provinceAbbreviation="+provinceAbbreviation
					+",numberPlateNumber="+numberPlateNumber+",plateType="+plateType+",sourceOfCertification="+sourceOfCertification, e);
			throw e;
		}
		return map;
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
	 * 提交 代表驾驶人信息单/无车证明申请/驾驶人安全事故信用表
	 * @param applyType 申请类型（1代表驾驶人信息单；2代表机动车信息单 3代表无车证明申请；4代表驾驶人安全事故信用表）
	 * @param userName 姓名
	 * @param identityCard 身份证号
	 * @param mobilephone 联系电话
	 * @return
	 * @throws Exception 
	 */
	@Override
	public Map<String, String> commitDriverLicenseInformationSheet(String applyType, String userName, String identityCard,String mobilephone,String sourceOfCertification) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		try {
			String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			map = TransferThirdParty.commitDriverInformationSinglePrintApplicationInterface(applyType, userName, identityCard, mobilephone, sourceOfCertification, url, method, userId, userPwd, key);
			
		} catch (Exception e) {
			logger.error("commitDriverLicenseInformationSheet 错误,applyType="+applyType + ",userName="+userName + ",identityCard=" + identityCard + ",mobilephone=" + mobilephone + ",sourceOfCertification="+sourceOfCertification, e);
			throw e;
		}
		return map;
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
	public ElectronicDriverLicenseVo getElectronicDriverLicense(String driverLicenseNumber, String userName,String mobileNumber,String sourceOfCertification) throws Exception {
		ElectronicDriverLicenseVo electronicDriverLicenseVo = new ElectronicDriverLicenseVo();
		try {
			 String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			 String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			 String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			 electronicDriverLicenseVo = TransferThirdParty.getElectronicDriverLicense(driverLicenseNumber, userName, mobileNumber, sourceOfCertification, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("用户中心-电子驾驶证 错误,driverLicenseNumber=" + driverLicenseNumber + ",userName=" + userName + ",mobileNumber=" + mobileNumber+",sourceOfCertification=" + sourceOfCertification, e);
			throw e;
		}
		return electronicDriverLicenseVo;
	}
	@Override
	public DrivingLicenseVo getDrivingLicense(String numberPlatenumber, String plateType, String mobileNumber,String sourceOfCertification)throws Exception {
		 DrivingLicenseVo drivingLicenseVo = new DrivingLicenseVo();
		try {
			 String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			 String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			 String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			 drivingLicenseVo = TransferThirdParty.getDrivingLicense(numberPlatenumber, plateType, mobileNumber, sourceOfCertification, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("用户中心-电子行驶证 错误,numberPlatenumber="+ numberPlatenumber + ",plateType=" + plateType + ",mobileNumber=" +mobileNumber+",sourceOfCertification="+sourceOfCertification, e);
			throw e;
		}
		return drivingLicenseVo;
	}
	@Override
	public MyDriverLicenseVo getMyDriverLicense(String identityCard,String sourceOfCertification) throws Exception {
		MyDriverLicenseVo myDriverLicenseVo = new MyDriverLicenseVo();
		try {
			String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			 String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			 String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			 myDriverLicenseVo = TransferThirdParty.getMyDriverLicense(identityCard, sourceOfCertification, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("用户中心-我的驾驶证错误,identityCard=" + identityCard + ",sourceOfCertification="+sourceOfCertification, e);
			throw e;
		}
		return myDriverLicenseVo;
	}
	@Override
	public List<BindTheVehicleVo> getBndTheVehicles(String identityCard,String mobilephone,String sourceOfCertification) throws Exception {
		 List<BindTheVehicleVo> bindTheVehicleVos = new ArrayList<BindTheVehicleVo>();
		try {
			 String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			 String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			 String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			 bindTheVehicleVos = TransferThirdParty.bindsTheMotorVehicleQuery(mobilephone, identityCard, sourceOfCertification, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("查询已绑车辆错误,identityCard="+identityCard+",sourceOfCertification="+sourceOfCertification, e);
			throw e;
		}
		return bindTheVehicleVos;
	}
	@Override
	public MotorVehicleInformationSheetVo getMotorVehicleInformationSheet(String identityCard,String sourceOfCertification) throws Exception {
		 MotorVehicleInformationSheetVo motorVehicleInformationSheetVo = new MotorVehicleInformationSheetVo(); 
		try {
			 String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			 String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			 String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			 //获取 认证基本信息
			 AuthenticationBasicInformationVo authenticationBasicInformationVo = TransferThirdParty.authenticationBasicInformationQuery(identityCard, sourceOfCertification, url, method, userId, userPwd, key);
			 //获取驾驶证信息
			 List<BindTheVehicleVo> bindTheVehicleVos = TransferThirdParty.bindsTheMotorVehicleQuery(authenticationBasicInformationVo.getMobilephone(), identityCard, sourceOfCertification, url, method, userId, userPwd, key);
			 //车牌号码
			 List<String> numberPlateNumbers = new ArrayList<String>();
			
			 for(BindTheVehicleVo bindTheVehicleVo : bindTheVehicleVos){
				 String numberPlateNumber = bindTheVehicleVo.getNumberPlateNumber();
				 if(StringUtils.isNotBlank(numberPlateNumber)){
					 numberPlateNumber = numberPlateNumber.substring(1, numberPlateNumber.length());
				 }
				 numberPlateNumbers.add(numberPlateNumber);
			 }
			 motorVehicleInformationSheetVo.setIdentityCard(authenticationBasicInformationVo.getIdentityCard());
			 motorVehicleInformationSheetVo.setMobilephone(authenticationBasicInformationVo.getMobilephone());
			 motorVehicleInformationSheetVo.setUserName(authenticationBasicInformationVo.getTrueName());
			 motorVehicleInformationSheetVo.setNumberPlateNumbers(numberPlateNumbers);
			 //车辆类型(客户端写死 例如 key-value)
			 //motorVehicleInformationSheetVo.setPlateTypes(plateTypes);
		} catch (Exception e) {
			logger.error("获取机动车信息单错误,identityCard=" + identityCard + ",sourceOfCertification=" + sourceOfCertification, e);
			throw e;
		}
		return motorVehicleInformationSheetVo;
	};
	/**
	 * 驾驶证业务，包括(驾驶证业务查询、驾驶人信息单、驾驶人安全事故信用表)
	 * @return
	 */
	private List<MyBusinessVo> getMyBusinessVos11(List<DriverLicenseToSupplementThePermitBusinessVo> driverLicenseToSupplementThePermitBusinessVos){
		List<MyBusinessVo> myBusinessVos11 = new ArrayList<MyBusinessVo>(); //驾驶证业务，包括(驾驶证业务查询、驾驶人信息单、驾驶人安全事故信用表)
		try {
			for(DriverLicenseToSupplementThePermitBusinessVo driverLicenseToSupplementThePermitBusinessVo : driverLicenseToSupplementThePermitBusinessVos){
				 MyBusinessVo myBusinessVo = new MyBusinessVo();
				 //去掉申请时间
				 //myBusinessVo.setApplicationTime(driverLicenseToSupplementThePermitBusinessVo.getWSLRSJ());//申请时间
				 myBusinessVo.setDetailedBusiness(6);
				 // B：补证 H换证
				 if("N".equals(driverLicenseToSupplementThePermitBusinessVo.getHBLX())){
					 myBusinessVo.setBusinessTitle("驾驶证年审");//业务名称
				 }
				 if("H".equals(driverLicenseToSupplementThePermitBusinessVo.getHBLX())){
					 myBusinessVo.setBusinessTitle("驾驶证换证");//业务名称
				 }
				 if("B".equals(driverLicenseToSupplementThePermitBusinessVo.getHBLX())){
					 myBusinessVo.setBusinessTitle("驾驶证补证");//业务名称
				 }
				 if("Z".equals(driverLicenseToSupplementThePermitBusinessVo.getHBLX())){
					 myBusinessVo.setBusinessTitle("驾驶证转入");//业务名称
				 }
				 if("Y".equals(driverLicenseToSupplementThePermitBusinessVo.getHBLX())){
					 myBusinessVo.setBusinessTitle("驾驶证延期换证");//业务名称
				 }
				 if("L".equals(driverLicenseToSupplementThePermitBusinessVo.getHBLX())){
					 myBusinessVo.setBusinessTitle("驾驶证联系方式变更");//业务名称
				 }
				 myBusinessVo.setIdentityCard(driverLicenseToSupplementThePermitBusinessVo.getSFZMHM()); //身份证
				//myBusinessVo.setPlateType(plateType);//号牌种类例如 蓝牌
				 
				 if("0".equals(driverLicenseToSupplementThePermitBusinessVo.getZHCLZT())){
					 //0待初审  -->办理中
					 myBusinessVo.setStatus(1); //0-全部，1-办理中，2-已完结
					 myBusinessVo.setStatusStr("待初审");
				 }
				 if("1".equals(driverLicenseToSupplementThePermitBusinessVo.getZHCLZT())){
					 //1初审通过，待制证  -->办理中
					 myBusinessVo.setStatus(1); //0-全部，1-办理中，2-已完结
					 myBusinessVo.setStatusStr("初审通过，待制证");
					 //myBusinessVo.setReceptionTime(driverLicenseToSupplementThePermitBusinessVo.getWSLRSJ()); //申请时间
					myBusinessVo.setReceptionTime(driverLicenseToSupplementThePermitBusinessVo.getZHCLSJ()); //受理时间
				 }
				 if("2".equals(driverLicenseToSupplementThePermitBusinessVo.getZHCLZT())){
					 //2车管已制证  -->办理中
					 myBusinessVo.setStatus(2); //0-全部，1-办理中，2-已完结
					 myBusinessVo.setStatusStr("车管已制证");
					 // myBusinessVo.setReceptionTime(driverLicenseToSupplementThePermitBusinessVo.getWSLRSJ()); //申请时间
					 myBusinessVo.setReceptionTime(driverLicenseToSupplementThePermitBusinessVo.getZHCLSJ()); //受理时间
					 
				 }
				 if("3".equals(driverLicenseToSupplementThePermitBusinessVo.getZHCLZT())){
					 //3待初审  -->办理中
					 myBusinessVo.setStatus(1); //0-全部，1-办理中，2-已完结
					 myBusinessVo.setStatusStr("待初审");
					 // myBusinessVo.setReceptionTime(driverLicenseToSupplementThePermitBusinessVo.getWSLRSJ()); //申请时间
					 myBusinessVo.setReceptionTime(driverLicenseToSupplementThePermitBusinessVo.getZHCLSJ()); //受理时间
				 }
				 if("TB".equals(driverLicenseToSupplementThePermitBusinessVo.getZHCLZT())){
					 //TB退办  -->办理中
					 myBusinessVo.setStatus(2); //0-全部，1-办理中，2-已完结
					 myBusinessVo.setStatusStr("退办");
					 // myBusinessVo.setReceptionTime(driverLicenseToSupplementThePermitBusinessVo.getWSLRSJ()); //申请时间
					 myBusinessVo.setReceptionTime(driverLicenseToSupplementThePermitBusinessVo.getZHCLSJ()); //受理时间
				 }
				 myBusinessVo.setUserName(driverLicenseToSupplementThePermitBusinessVo.getXM());//姓名
				//myBusinessVo.setVehicleNumber(vehicleNumber);//车辆号码 例如 粤B701NR
				 myBusinessVos11.add(myBusinessVo);
			 }
		} catch (Exception e) {
			logger.error("驾驶证业务，包括(驾驶证业务查询、驾驶人信息单、驾驶人安全事故信用表) 错误,driverLicenseToSupplementThePermitBusinessVos=" + driverLicenseToSupplementThePermitBusinessVos, e);
			throw e;
		}
		return myBusinessVos11;
	}
	/**
	 * //机动车业务，包括(机动车业务查询、机动车信息单、无车证明申请)
	 * @return
	 */
	private List<MyBusinessVo> getMyBusinessVos22(List<MotorVehicleBusiness> motorVehicleBusinesses){
		List<MyBusinessVo> myBusinessVos22 = new ArrayList<MyBusinessVo>(); //机动车业务，包括(机动车业务查询、机动车信息单、无车证明申请)
		try {
			for(MotorVehicleBusiness motorVehicleBusiness : motorVehicleBusinesses){
				MyBusinessVo myBusinessVo = new MyBusinessVo();
				 myBusinessVo.setVehicleNumber(motorVehicleBusiness.getHPHM());
				 myBusinessVo.setDetailedBusiness(5);
				 myBusinessVo.setReceptionTime(motorVehicleBusiness.getZHCLSJ()); //受理时间
				 //YWLX=1换  YWLX=5 补 
				 if("1".equals(motorVehicleBusiness.getYWLX())){
					 myBusinessVo.setBusinessTitle("补领机动车行驶证");//业务名称
				 }
				 if("2".equals(motorVehicleBusiness.getYWLX())){
					 myBusinessVo.setBusinessTitle("检验合格标志");//业务名称
				 }
				 
				 if("3".equals(motorVehicleBusiness.getYWLX())){
					 myBusinessVo.setBusinessTitle("补领机动车牌号码");//业务名称
				 }
				 if("4".equals(motorVehicleBusiness.getYWLX())){
					 myBusinessVo.setBusinessTitle("申请机动车临牌");//业务名称
				 }
				 if("5".equals(motorVehicleBusiness.getYWLX())){
					 myBusinessVo.setBusinessTitle("换领机动车行驶证");//业务名称
				 }
				 if("6".equals(motorVehicleBusiness.getYWLX())){
					 myBusinessVo.setBusinessTitle("机动车变更联系方式");//业务名称
				 }
				 
				 if("0".equals(motorVehicleBusiness.getZHCLZT())){
					 //0待初审  -->办理中
					 myBusinessVo.setStatus(1); //0-全部，1-办理中，2-已完结
					 myBusinessVo.setStatusStr("待初审");
				 }
				 if("1".equals(motorVehicleBusiness.getZHCLZT())){
					 //1初审通过，待制证  -->办理中
					 myBusinessVo.setStatus(1); //0-全部，1-办理中，2-已完结
					 myBusinessVo.setStatusStr("初审通过，待制证");
				 }
				 if("2".equals(motorVehicleBusiness.getZHCLZT())){
					 //2车管已制证  -->办理中
					 myBusinessVo.setStatus(2); //0-全部，1-办理中，2-已完结
					 myBusinessVo.setStatusStr("车管已制证");
				 }
				 if("3".equals(motorVehicleBusiness.getZHCLZT())){
					 //3待初审  -->办理中
					 myBusinessVo.setStatus(1); //0-全部，1-办理中，2-已完结
					 myBusinessVo.setStatusStr("待初审");
				 }
				 if("TB".equals(motorVehicleBusiness.getZHCLZT())){
					 //TB退办  -->办理中
					 myBusinessVo.setStatus(2); //0-全部，1-办理中，2-已完结
					 myBusinessVo.setStatusStr("退办");
				 }
				 myBusinessVos22.add(myBusinessVo);
			 }
		} catch (Exception e) {
			logger.error("//机动车业务，包括(机动车业务查询、机动车信息单、无车证明申请)错误,motorVehicleBusinesses=" + motorVehicleBusinesses.toString(), e);
			throw e;
		}
		return myBusinessVos22;
	}
	/**
	 * 机动车信息单--机动车业务
	 * @return
	 */
	private void getQueryMachineInformationSheet2(Map<String, Object> map, List<MyBusinessVo> myBusinessVos22){
		List<InformationSheetVo> informationSheetVos = (List<InformationSheetVo>) map.get("data");
		List<MyBusinessVo> myBusinessVos = new ArrayList<MyBusinessVo>();
		try {
			String code = (String) map.get("code");
			if("0000".equals(code)){
				for(InformationSheetVo informationSheetVo : informationSheetVos){
					MyBusinessVo myBusinessVo = new MyBusinessVo();
					myBusinessVo.setBusinessTitle("机动车信息单");
					myBusinessVo.setUserName(informationSheetVo.getName());
					myBusinessVo.setIdentityCard(informationSheetVo.getIdCard());
					myBusinessVo.setApplicationTime(informationSheetVo.getApplicationTime());
					// 状态代码  0-待审核 1-审核通过，信息单查询结果图片尚未同步出来，请耐心等待 2-审核通过，信息单查询结果图片已同步，可选择电脑打印或手机图片保存
					if("0".equals(informationSheetVo.getStatusCode())){
						myBusinessVo.setStatus(1);
						myBusinessVo.setStatusStr("待审核");
					}
					if("1".equals(informationSheetVo.getStatusCode())){
						myBusinessVo.setStatus(1);
						myBusinessVo.setStatusStr("审核通过 ");
					}
					if("2".equals(informationSheetVo.getStatusCode())){
						myBusinessVo.setStatus(2);
						myBusinessVo.setStatusStr("已制证");
					}
					myBusinessVo.setVehicleNumber(informationSheetVo.getNumberPlate());
					myBusinessVo.setPlateType(informationSheetVo.getPlateType());
					//具体业务 1、驾驶人信息单；2、机动车信息单；3、无车证明申请；4、驾驶人安全事故信用表；5、机动车行驶证；6、驾驶证
					myBusinessVo.setDetailedBusiness(2);
					myBusinessVos.add(myBusinessVo);
				}
			}
		} catch (Exception e) {
			logger.error("机动车信息单--机动车业务错误,map=" + map + ",myBusinessVos22=" + myBusinessVos22.toString(), e);
			throw e;
		}
		myBusinessVos22.addAll(myBusinessVos);
	}
	/**
	 * 无车证明申请--机动车业务
	 * @return
	 */
	private void getQueryMachineInformationSheet3(Map<String, Object> map, List<MyBusinessVo> myBusinessVos22){
		List<InformationSheetVo> informationSheetVos = (List<InformationSheetVo>) map.get("data");
		List<MyBusinessVo> myBusinessVos = new ArrayList<MyBusinessVo>();
		try {
			String code = (String) map.get("code");
			if("0000".equals(code)){
				for(InformationSheetVo informationSheetVo : informationSheetVos){
					MyBusinessVo myBusinessVo = new MyBusinessVo();
					myBusinessVo.setBusinessTitle("无车证明申请");
					myBusinessVo.setUserName(informationSheetVo.getName());
					myBusinessVo.setIdentityCard(informationSheetVo.getIdCard());
					// 状态代码  0-待审核 1-审核通过，信息单查询结果图片尚未同步出来，请耐心等待 2-审核通过，信息单查询结果图片已同步，可选择电脑打印或手机图片保存
					if("0".equals(informationSheetVo.getStatusCode())){
						myBusinessVo.setStatus(1);
						myBusinessVo.setStatusStr("待审核");
					}
					if("1".equals(informationSheetVo.getStatusCode())){
						myBusinessVo.setStatus(1);
						myBusinessVo.setStatusStr("审核通过");
					}
					if("2".equals(informationSheetVo.getStatusCode())){
						myBusinessVo.setStatus(2);
						myBusinessVo.setStatusStr("已制证");
					}
					if("TB".equals(informationSheetVo.getStatusCode())){
						 //TB退办  -->办理中
						 myBusinessVo.setStatus(2); //0-全部，1-办理中，2-已完结
						 myBusinessVo.setStatusStr("退办");
					 }
					myBusinessVo.setApplicationTime(informationSheetVo.getApplicationTime());
					//具体业务 1、驾驶人信息单；2、机动车信息单；3、无车证明申请；4、驾驶人安全事故信用表；5、机动车行驶证；6、驾驶证
					myBusinessVo.setDetailedBusiness(3);
					myBusinessVos.add(myBusinessVo);
				}
			}
		} catch (Exception e) {
			logger.error("无车证明申请--机动车业务 错误,map="+ map + ",myBusinessVos22=" + myBusinessVos22, e);
			throw e;
		}
		myBusinessVos22.addAll(myBusinessVos);
	}
	/**
	 * 驾驶人信息单--驾驶证业务
	 * @return
	 */
	private void getQueryMachineInformationSheet1(Map<String, Object> map, List<MyBusinessVo> myBusinessVos11){
		List<InformationSheetVo> informationSheetVos = (List<InformationSheetVo>) map.get("data");
		List<MyBusinessVo> myBusinessVos = new ArrayList<MyBusinessVo>();
		try {
			String code = (String) map.get("code");
			if("0000".equals(code)){
				for(InformationSheetVo informationSheetVo : informationSheetVos){
					MyBusinessVo myBusinessVo = new MyBusinessVo();
					myBusinessVo.setBusinessTitle("驾驶人信息单");
					myBusinessVo.setUserName(informationSheetVo.getName());
					myBusinessVo.setIdentityCard(informationSheetVo.getIdCard());
					// 状态代码  0-待审核 1-审核通过，信息单查询结果图片尚未同步出来，请耐心等待 2-审核通过，信息单查询结果图片已同步，可选择电脑打印或手机图片保存
					if("0".equals(informationSheetVo.getStatusCode())){
						myBusinessVo.setStatus(1);
						myBusinessVo.setStatusStr("待审核");
					}
					if("1".equals(informationSheetVo.getStatusCode())){
						myBusinessVo.setStatus(1);
						myBusinessVo.setStatusStr("审核通过");
					}
					if("2".equals(informationSheetVo.getStatusCode())){
						myBusinessVo.setStatus(2);
						myBusinessVo.setStatusStr("审核通过");
					}
					myBusinessVo.setApplicationTime(informationSheetVo.getApplicationTime());
					//具体业务 1、驾驶人信息单；2、机动车信息单；3、无车证明申请；4、驾驶人安全事故信用表；5、机动车行驶证；6、驾驶证
					myBusinessVo.setDetailedBusiness(1);
					myBusinessVos.add(myBusinessVo);
				}
			}
		} catch (Exception e) {
			logger.error("驾驶人信息单--驾驶证业务 错误,map=" + map.toString() + ",myBusinessVos11="+myBusinessVos11.toString(), e);
			throw e;
		}
		myBusinessVos11.addAll(myBusinessVos);
	}
	/**
	 * 驾驶人安全事故信用表--驾驶证业务
	 * @return
	 */
	private void getQueryMachineInformationSheet4(Map<String, Object> map, List<MyBusinessVo> myBusinessVos11){
		List<InformationSheetVo> informationSheetVos = (List<InformationSheetVo>) map.get("data");
		List<MyBusinessVo> myBusinessVos = new ArrayList<MyBusinessVo>();
		try {
			String code = (String) map.get("code");
			if("0000".equals(code)){
				for(InformationSheetVo informationSheetVo : informationSheetVos){
					MyBusinessVo myBusinessVo = new MyBusinessVo();
					myBusinessVo.setBusinessTitle("驾驶人安全事故信用表");
					myBusinessVo.setUserName(informationSheetVo.getName());
					myBusinessVo.setIdentityCard(informationSheetVo.getIdCard());
					// 状态代码  0-待审核 1-审核通过，信息单查询结果图片尚未同步出来，请耐心等待 2-审核通过，信息单查询结果图片已同步，可选择电脑打印或手机图片保存
					if("0".equals(informationSheetVo.getStatusCode())){
						myBusinessVo.setStatus(1);
						myBusinessVo.setStatusStr("待审核");
					}
					if("1".equals(informationSheetVo.getStatusCode())){
						myBusinessVo.setStatus(1);
						myBusinessVo.setStatusStr("审核通过");
					}
					if("2".equals(informationSheetVo.getStatusCode())){
						myBusinessVo.setStatus(2);
						myBusinessVo.setStatusStr("审核通过");
					}
					myBusinessVo.setApplicationTime(informationSheetVo.getApplicationTime());
					//具体业务 1、驾驶人信息单；2、机动车信息单；3、无车证明申请；4、驾驶人安全事故信用表；5、机动车行驶证；6、驾驶证
					myBusinessVo.setDetailedBusiness(4);
					myBusinessVos.add(myBusinessVo);
				}
			}
		} catch (Exception e) {
			logger.error("驾驶人安全事故信用表--驾驶证业务 错误，map="+map.toString() + "myBusinessVos11=" + myBusinessVos11.toString(), e);
			throw e;
		}
		myBusinessVos11.addAll(myBusinessVos);
	}
	
	@Override
	public List<MyBusinessVo> getMyBusiness(Integer businessType, Integer businessStatus, String identityCard,String sourceOfCertification) throws Exception {
		List<MyBusinessVo> returnMyBusinessVo = new ArrayList<MyBusinessVo>();
		try {
			String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			 String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			 String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			 List<MyBusinessVo> myBusinessVos11 = new ArrayList<MyBusinessVo>(); //驾驶证业务，包括(驾驶证业务查询、驾驶人信息单、驾驶人安全事故信用表)
			 List<MyBusinessVo> myBusinessVos22 = new ArrayList<MyBusinessVo>(); //机动车业务，包括(机动车业务查询、机动车信息单、无车证明申请)
			 
			 //驾驶证业务查询
			 List<DriverLicenseToSupplementThePermitBusinessVo> driverLicenseToSupplementThePermitBusinessVos = (List<DriverLicenseToSupplementThePermitBusinessVo>) TransferThirdParty.getDriverLicenseToReplenishBusinessInquiriesInterface(identityCard, sourceOfCertification, url, method, userId, userPwd, key);
			 myBusinessVos11 = getMyBusinessVos11(driverLicenseToSupplementThePermitBusinessVos);
			 
			 //机动车业务查询接口
			 List<MotorVehicleBusiness> motorVehicleBusinesses = (List<MotorVehicleBusiness>) TransferThirdParty.getMotorVehicleBusiness(identityCard, sourceOfCertification, url, method, userId, userPwd, key);
			 myBusinessVos22 = getMyBusinessVos22(motorVehicleBusinesses);
			 
			 //4中类型信息单查询接口	申请类型(1、驾驶人信息单；2、机动车信息单；3、无车证明申请；4、驾驶人安全事故信用表)
			 //驾驶人信息单
			 Map<String, Object> map1 = TransferThirdParty.queryMachineInformationSheet("1", identityCard, sourceOfCertification, url, method, userId, userPwd, key);
			 //机动车信息单
			 Map<String, Object> map2 = TransferThirdParty.queryMachineInformationSheet("2", identityCard, sourceOfCertification, url, method, userId, userPwd, key);
			 //无车证明申请
			 Map<String, Object> map3 = TransferThirdParty.queryMachineInformationSheet("3", identityCard, sourceOfCertification, url, method, userId, userPwd, key);
			 //驾驶人安全事故信用表
			 Map<String, Object> map4 = TransferThirdParty.queryMachineInformationSheet("4", identityCard, sourceOfCertification, url, method, userId, userPwd, key);
			 getQueryMachineInformationSheet1(map1,myBusinessVos11);
			 getQueryMachineInformationSheet2(map2,myBusinessVos22);
			 getQueryMachineInformationSheet3(map3,myBusinessVos22);
			 getQueryMachineInformationSheet4(map4,myBusinessVos11);
			 
			//业务类型	 0-全部
			if(0 == businessType){
				returnMyBusinessVo.addAll(myBusinessVos11);
				returnMyBusinessVo.addAll(myBusinessVos22);
				//业务状态 0-全部、1-办理中、2-已完结
				if(0 == businessStatus){
					
				}else if(1 == businessStatus){
					return getMyBusinessVoByStatus(returnMyBusinessVo, 1);
				}else if(2 == businessStatus) {
					return getMyBusinessVoByStatus(returnMyBusinessVo, 2);
				}
			}
			//业务类型 	1-机动车业务
			if(1 == businessType){
				//业务状态 0-全部、1-办理中、2-已完结
				if(0 == businessStatus){
					returnMyBusinessVo.addAll(myBusinessVos22);
				}else if(1 == businessStatus){
					return getMyBusinessVoByStatus(myBusinessVos22, 1);
				}else if(2 == businessStatus) {
					return getMyBusinessVoByStatus(myBusinessVos22, 2);
				}
			}
			//业务类型	2-驾驶证业务
			if(2 == businessType){
				//业务状态 0-全部、1-办理中、2-已完结
				if(0 == businessStatus){
					returnMyBusinessVo.addAll(myBusinessVos11);
				}else if(1 == businessStatus){
					return getMyBusinessVoByStatus(myBusinessVos11, 1);
				}else if(2 == businessStatus) {
					return getMyBusinessVoByStatus(myBusinessVos11, 2);
				}
			}
		} catch (Exception e) {
			logger.error("我的业务(切换查询-机动车业务、驾驶证业务)  错误,businessType="+businessType + ",businessStatus=" + businessStatus + ",identityCard="+identityCard+",sourceOfCertification="+sourceOfCertification , e);
			throw e;
		}
		return returnMyBusinessVo;
	}
	@Override
	public void sendSMSVerificatioCode(String mobilephone,String valideteCode) {
		UserValidateCodeORM userValidateCodePo = new UserValidateCodeORM();
		try {
			//插入数据库
			userValidateCodePo.setGenDate(new Date());
			userValidateCodePo.setMobilephone(mobilephone);
			userValidateCodePo.setValidateCode(valideteCode);
			int result = userValidateCodeDao.addUserValidateCode(userValidateCodePo);
			if(1 == result){
				//插入redis,为5分钟有效期
				iAccountCached.insertUserValidateCode(mobilephone, valideteCode);
			}
		} catch (Exception e) {
			logger.error("发送验证码错误，mobilephone = " + mobilephone + ",validateCode" + valideteCode);
			throw e;
		}
	}
	@Override
	public int verificatioCode(String mobilephone, String validateCode) {
		// 0-验证成功，1-验证失败，2-验证码失效
		int result = 0;
		try {
			//取redis验证码
			String code= iAccountCached.getUserValidateCode(mobilephone);
			if(StringUtils.isNotBlank(code)){
				if(validateCode.equals(code)){
					result = 0;
				}else{
					result = 1;
				}
			}else{
				result = 2;
			}
		} catch (Exception e) {
			logger.error("验证验证码是否正确错误，mobilephone = " + mobilephone + ",validateCode" + validateCode);
			throw e;
		}
		return result;
	}
	/**
	 * 手机号码在redis是否存在
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	public String isExistMobile(String mobilephone)throws Exception{
		String temp = iAccountCached.getSendSmsFreqLimit(mobilephone);
		return temp;
	}
	
	@Override
	public int unbindVehicle(UserBind userBind) {
		int cancelSuccess = 0;
	      if(userBind == null) {
	          logger.error("unbindVehicle接收参数有误");
	          return cancelSuccess;
	      }
	      try {
	    	  if("C".equalsIgnoreCase(userBind.getClientType())){
	    		  cancelSuccess = accountDao.unbindVehicle(userBind);
	    	  }else if("Z".equalsIgnoreCase(userBind.getClientType())){
	    		  //支付宝没有解绑操作
	    		  //cancelSuccess = userBindAlipayDao.unbindVehicle(null);
	    	  }
	      } catch (Exception e) {
	          logger.error("unbindVehicle出错，错误="+ userBind.toString(),e);
	      }
	      if(cancelSuccess != 1){
	    	  logger.info("解绑方法参数" + userBind.toString());
	      }
	      return cancelSuccess;
	}
	@Override
	public JSONObject addVehicle(BindCarVo bindCarVo) throws Exception{
		JSONObject json= null;
		String certifiedSource = bindCarVo.getCertifiedSource();
		try {
			 String url = iAccountCached.getUrl(certifiedSource); //webservice请求url
			 String method = iAccountCached.getMethod(certifiedSource); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(certifiedSource); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(certifiedSource); //webservice登录密码
			 String key = iAccountCached.getKey(certifiedSource); //秘钥
			 json = NozzleMeans.addVehicle(bindCarVo, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("addVehicle出错，错误="+ bindCarVo.toString(),e);
			throw e;
		}
		return json;
	}
	@Override
	public JSONObject updateUser(UserBasicVo userBasicVo)throws Exception {
		JSONObject json= null;
		String userSource = userBasicVo.getUserSource();
		logger.info("userSource：" + userSource);
		try {
			 String url = iAccountCached.getUrl(userSource); //webservice请求url
			 String method = iAccountCached.getMethod(userSource); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(userSource); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(userSource); //webservice登录密码
			 String key = iAccountCached.getKey(userSource); //秘钥
			 json = NozzleMeans.updateUser(userBasicVo, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("updateUser出错，错误="+ userBasicVo.toString(),e);
			throw e;
		}
		return json;
	}
	@Override
	public JSONObject updateMobile(UserBasicVo userBasicVo)throws Exception {		
			JSONObject json= null;
			String userSource = userBasicVo.getUserSource();
			try {
				 String url = iAccountCached.getUrl(userSource); //webservice请求url
				 String method = iAccountCached.getMethod(userSource); //webservice请求方法名称
				 String userId = iAccountCached.getUserid(userSource); //webservice登录账号
				 String userPwd = iAccountCached.getUserpwd(userSource); //webservice登录密码
				 String key = iAccountCached.getKey(userSource); //秘钥
				 json = NozzleMeans.updateMobile(userBasicVo, url, method, userId, userPwd, key);
			} catch (Exception e) {
				logger.error("updateMobile出错，错误="+ userBasicVo.toString(),e);
				throw e;
			}
			return json;
	}
	
	@Override
	public JSONObject updatePwd(UserBasicVo userBasicVo)throws Exception {
		JSONObject json= null;
		try {
			String userSource = userBasicVo.getUserSource();
			 String url = iAccountCached.getUrl(userSource); //webservice请求url
			 String method = iAccountCached.getMethod(userSource); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(userSource); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(userSource); //webservice登录密码
			 String key = iAccountCached.getKey(userSource); //秘钥
			 json = NozzleMeans.updatePwd(userBasicVo, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("updatePwd出错，错误="+ userBasicVo.toString(),e);
			throw e;
		}
		return json;
	}
	
	
	@Override
	public JSONObject readilyShoot(ReadilyShootVo readilyShootVo) throws Exception {
		JSONObject json= null;
		String userSource = readilyShootVo.getUserSource();
		try {
			 String url = iAccountCached.getUrl(userSource); //webservice请求url
			 String method = iAccountCached.getMethod(userSource); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(userSource); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(userSource); //webservice登录密码
			 String key = iAccountCached.getKey(userSource); //秘钥
			 json = NozzleMeans.readilyShoot(readilyShootVo, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("ReadilyShootVo出错，错误="+ readilyShootVo.toString(),e);
			throw e;
		}
		return json;
	}
	
	@Override
	public JSONObject iAmTheOwner(RegisterVo registerVo) throws Exception{
		JSONObject json= null;
		String certifiedSource = registerVo.getCertifiedSource();
		try {
			 String url = iAccountCached.getUrl(certifiedSource); //webservice请求url
			 String method = iAccountCached.getMethod(certifiedSource); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(certifiedSource); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(certifiedSource); //webservice登录密码
			 String key = iAccountCached.getKey(certifiedSource); //秘钥
			 json = NozzleMeans.iAmTheOwner(registerVo, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("iAmTheOwner出错，错误="+ registerVo.toString(),e);
			throw e;
		}
		return json;
	}
	
	
	
	@Override
	public JSONObject iamALongtimeUser(RegisterVo registerVo) throws Exception {
		JSONObject json= null;
		String certifiedSource = registerVo.getCertifiedSource();
		try {
			 String url = iAccountCached.getUrl(certifiedSource); //webservice请求url
			 String method = iAccountCached.getMethod(certifiedSource); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(certifiedSource); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(certifiedSource); //webservice登录密码
			 String key = iAccountCached.getKey(certifiedSource); //秘钥
			 json = NozzleMeans.iamALongtimeUser(registerVo, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("iamALongtimeUser出错，错误="+ registerVo.toString(),e);
			throw e;
		}
		return json;
	}
	
	
	
	@Override
	public JSONObject haveDriverLicenseNotCar(RegisterVo registerVo) throws Exception {
		JSONObject json= null;
		String certifiedSource = registerVo.getCertifiedSource();
		try {
			 String url = iAccountCached.getUrl(certifiedSource); //webservice请求url
			 String method = iAccountCached.getMethod(certifiedSource); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(certifiedSource); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(certifiedSource); //webservice登录密码
			 String key = iAccountCached.getKey(certifiedSource); //秘钥
			 json = NozzleMeans.haveDriverLicenseNotCar(registerVo, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("haveDriverLicenseNotCar出错，错误="+ registerVo.toString(),e);
			throw e;
		}
		return json;
	}
	
	
	
	
	
	@Override
	public JSONObject isPedestrianNotDriver(RegisterVo registerVo)throws Exception {
		JSONObject json= null;
		String certifiedSource = registerVo.getCertifiedSource();
		try {
			 String url = iAccountCached.getUrl(certifiedSource); //webservice请求url
			 String method = iAccountCached.getMethod(certifiedSource); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(certifiedSource); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(certifiedSource); //webservice登录密码
			 String key = iAccountCached.getKey(certifiedSource); //秘钥
			 json = NozzleMeans.isPedestrianNotDriver(registerVo, url, method, userId, userPwd, key);
			 logger.info("日志打印：json="+json + "对象参数=" + registerVo + "url=" + url + "method=" + method + "userId=" + userId + "userPwd=" + userPwd + "key=" + key);
		} catch (Exception e) {
			logger.error("isPedestrianNotDriver出错，错误="+ registerVo.toString(),e);
			throw e;
		}
		return json;
	}
	@Override
	public JSONObject getPositioningAddress(String keyword) throws Exception {
		JSONObject json= null;
		try {
			String string = "";
			 String url = iAccountCached.getUrl(string); //webservice请求url
			 String method = iAccountCached.getMethod(string); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(string); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(string); //webservice登录密码
			 String key = iAccountCached.getKey(string); //秘钥
			 json = NozzleMeans.getPositioningAddress(keyword, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("getPositioningAddress出错，错误="+ keyword,e);
			throw e;
		}
		return json;
	}
	@Override
	public JSONObject getTheChoiceOfIllegalActivities(String keyword) throws Exception {
		JSONObject json= null;
		try {
			String string = "";
			 String url = iAccountCached.getUrl(string); //webservice请求url
			 String method = iAccountCached.getMethod(string); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(string); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(string); //webservice登录密码
			 String key = iAccountCached.getKey(string); //秘钥
			 json = NozzleMeans.getTheChoiceOfIllegalActivities(keyword, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("getPositioningAddress出错，错误="+ keyword,e);
			throw e;
		}
		return json;
	}
	
	@Override
	public Map<String, Object> getElectronicPolicy(String idCard, String mobileNumber, String licensePlateNumber,String licensePlateType, String sourceOfCertification) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		try {
			 String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			 String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			 String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			 map = TransferThirdParty.getElectronicPolicy(idCard, mobileNumber, licensePlateNumber, licensePlateType, sourceOfCertification, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("电子保单查询错误，idCard = " + idCard + ",mobileNumber=" + mobileNumber + ",licensePlateNumber=" + licensePlateNumber + ",licensePlateType=" + licensePlateType + ",sourceOfCertification=" + sourceOfCertification);
			throw e;
		}
		return map;
	}
	@Override
	public void sendSmsFreqLimit(String mobilephone) {
		try {
			iAccountCached.sendSmsFreqLimit(mobilephone);
		} catch (Exception e) {
			logger.error("sendSmsFreqLimit",e);
			throw e;
		}
	}
	@Override
	public String getSendSmsFreqLimit(String mobilephone) {
		String key = iAccountCached.getSendSmsFreqLimit(mobilephone);
		return key;
	}
	@Override
	public ResultOfReadilyShoot queryResultOfReadilyShoot(String reportSerialNumber, String password) throws Exception {
		ResultOfReadilyShoot resultOfReadilyShoot = null;
		try {
			ReadilyShoot readilyShoot = readilyShootDao.queryByReportSerialNumberAndPassword(reportSerialNumber, password);
			
			if (null != readilyShoot ) {
				String string = "";
				String url = iAccountCached.getUrl(string); //webservice请求url
				String method = iAccountCached.getMethod(string); //webservice请求方法名称
				String userId = iAccountCached.getUserid(string); //webservice登录账号
				String userPwd = iAccountCached.getUserpwd(string); //webservice登录密码
				String key = iAccountCached.getKey(string); //秘钥
				resultOfReadilyShoot = TransferThirdParty.queryResultOfReadilyShoot(reportSerialNumber, password, url, method, userId, userPwd, key);
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
				String date = sdf.format(readilyShoot.getIllegalTime()) ;
				if (null!=date) {
					resultOfReadilyShoot.setIllegalTime(date);
				}
				String illegaSections = readilyShoot.getIllegalSections();
				if (null!=illegaSections) {
					resultOfReadilyShoot.setIllegalSections(illegaSections);
				}
				String situationStatement = readilyShoot.getSituationStatement();
				if (null!=situationStatement) {
					resultOfReadilyShoot.setSituationStatement(situationStatement);
				}
				String illegalImg1 = readilyShoot.getIllegalImg1();
				if (null != illegalImg1) {
					resultOfReadilyShoot.setIllegalImg1(illegalImg1);
				}
				String illegalImg2 = readilyShoot.getIllegalImg2();
				if (null != illegalImg2) {
					resultOfReadilyShoot.setIllegalImg2(illegalImg2);
				}
				String illegalImg3 = readilyShoot.getIllegalImg3();
				if (null != illegalImg3) {
					resultOfReadilyShoot.setIllegalImg3(illegalImg3);
				}
			}
			
			 
		} catch (Exception e) {
			logger.error("违法信息查询错误，reportSerialNumber = " + reportSerialNumber + ",password=" + password );
			throw e;
		}
		
		return resultOfReadilyShoot;
	}
	@Override
	public int saveReadilyShoot(ReadilyShoot readilyShoot) {
		return readilyShootDao.saveReadilyShoot(readilyShoot);
	}
	
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
	@Override
	public Map<String, Object> cancelReservation(String sourceOfCertification, String reservationNo) throws Exception {
		
		return null;
	}
	@Override
	public Map<String, Object> getReservation(String sourceOfCertification, String mobilephone, String validateCode)
			throws Exception {
		
		return null;
	}
	@Override
	public Map<String, Object> Reservation(String sourceOfCertification, String mobilephone, String validateCode,
			String plateNumber, String plateType, String vehicleType, String fourDigitsAfterTheEngine, String time,
			String date, String address) throws Exception {
		
		return null;
	}
	@Override
	public List<UserBind> getBetweenAndId(String startId, String endId) {
		List<UserBind> userBinds = null;
		try {
			userBinds =	accountDao.getBetweenAndId(startId, endId);
		} catch (Exception e) {
			logger.error("getBetweenAndId 错误", e);
			throw e;
		}
		return userBinds;
	}
	@Override
	public List<UserBind> getBetweenAndBindDate(String startDate, String endDate) {
		List<UserBind> userBinds = null;
		try {
			userBinds =	accountDao.getBetweenAndBindDate(startDate, endDate);
		} catch (Exception e) {
			logger.error("getBetweenAndId 错误", e);
			throw e;
		}
		return userBinds;
	}
	
	@Override
	public JSONObject bindDriverLicense(BindDriverLicenseVo bindDriverLicenseVo)  throws Exception{
		JSONObject json= null;
		try {
			String sourceOfCertification = bindDriverLicenseVo.getSourceOfCertification();
			 String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			 String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			 String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			 json = TransferThirdParty.bindDriverLicense(bindDriverLicenseVo, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("bindDriverLicense出错，错误="+ bindDriverLicenseVo.toString(),e);
			throw e;
		}
		return json;
	}
	@Override
	public ResultOfBIndDriverLicenseVo queryResultOfBindDriverLicense(String identityCard, String userSource)  throws Exception{
		ResultOfBIndDriverLicenseVo resultOfBIndDriverLicenseVo= null;
		try {
			String string = "";
			 String url = iAccountCached.getUrl(string); //webservice请求url
			 String method = iAccountCached.getMethod(string); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(string); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(string); //webservice登录密码
			 String key = iAccountCached.getKey(string); //秘钥
			 resultOfBIndDriverLicenseVo = TransferThirdParty.queryResultOfBindDriverLicense(identityCard, userSource, url, method, userId, userPwd, key);
			 
		} catch (Exception e) {
			logger.error("bindDriverLicense出错，错误="+ "identityCard=" + identityCard+"userSource=" + userSource,e);
			throw e;
		}
		
		return resultOfBIndDriverLicenseVo;
	}
	@Override
	public Map<String, String> submitApplicationForDriverInformation(String applyType, String applyName,
			String identityCard, String applyPhone, String sourceOfCertification)  throws Exception{
		Map<String, String> map = new HashMap<>();
		try {
			 String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			 String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			 String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			 map = TransferThirdParty.submitApplicationForDriverInformation(applyType, applyName, identityCard, applyPhone, sourceOfCertification, url, method, userId, userPwd, key);
			 
		} catch (Exception e) {
			logger.error("submitApplicationForDriverInformation出错，错误="+ "applyType=" + applyType+"applyName=" + applyName+"identityCard=" + identityCard+"sourceOfCertification=" + sourceOfCertification+"applyPhone=" + applyPhone,e);
			throw e;
		}
		
		return map;
	}
	@Override
	public Map<String, String> submitApplicationForMotorVehicleInformation(String applyType, String applyName,
			String identityCard, String applyPhone, String licensePlateNumber,
			String plateType, String sourceOfCertification)  throws Exception{
		Map<String, String> map = new HashMap<>();
		try {
			
			 String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			 String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			 String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			 map = TransferThirdParty.submitApplicationForMotorVehicleInformation(applyType, applyName, identityCard, applyPhone,licensePlateNumber,plateType, sourceOfCertification, url, method, userId, userPwd, key);
			 
		} catch (Exception e) {
			logger.error("submitApplicationForMotorVehicleInformation出错，错误="+ "applyType=" + applyType+"applyName=" + applyName+"identityCard=" + identityCard+"applyPhone=" + applyPhone+"sourceOfCertification=" + sourceOfCertification+"licensePlateNumber=" + licensePlateNumber+"plateType=" + plateType,e);
			throw e;
		}
		
		return map;
	}
	@Override
	public Map<String, Object> queryScheduleOfDriverInformationList(String applyType, String identityCard,
			String sourceOfCertification)  throws Exception{
		Map<String, Object> map = new HashMap<>();
		try {
			
			 String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			 String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			 String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			 map = TransferThirdParty.queryScheduleOfDriverInformationList(applyType, identityCard, sourceOfCertification, url, method, userId, userPwd, key);
			 
		} catch (Exception e) {
			logger.error("queryScheduleOfDriverInformationList出错，错误="+ "applyType=" + applyType+"identityCard=" + identityCard+"sourceOfCertification=" + sourceOfCertification,e);
			throw e;
		}
		
		return map;
	}
	@Override
	public Map<String, Object> queryScheduleOfMotorVehicleInformationList(String applyType, String identityCard,
			String sourceOfCertification)  throws Exception{
		Map<String, Object> map = new HashMap<>();
		try {
			
			 String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			 String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			 String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			 map = TransferThirdParty.queryScheduleOfMotorVehicleInformationList(applyType, identityCard, sourceOfCertification, url, method, userId, userPwd, key);
			 
		} catch (Exception e) {
			logger.error("queryScheduleOfMotorVehicleInformationList出错，错误="+ "applyType=" + applyType+"identityCard=" + identityCard+"sourceOfCertification=" + sourceOfCertification,e);
			throw e;
		}
		
		return map;
	}
	/**
	 * 提交无车证明申请
	 * @Description: TODO(提交无车证明申请)
	 * @param applyType 申请类型
	 * @param applyName 申请人姓名
	 * @param identityCard 申请人身份证号
	 * @param applyPhone 申请人联系电话
	 * @param sourceOfCertification 申请来源
	 * @return
	 * @throws Exception 
	 */
	public BaseBean addNoneCarCertification(String applyType, String applyName, String identityCard, String applyPhone, String sourceOfCertification) throws Exception {
		logger.info("提交无车证明申请采集WebService...");
		
		BaseBean baseBean = new BaseBean();		//创建返回信息
		
		try {
			String url = iAccountCached.getUrl(sourceOfCertification); 			//webservice请求url
			String method = iAccountCached.getMethod(sourceOfCertification); 	//webservice请求方法名称
			String userId = iAccountCached.getUserid(sourceOfCertification); 	//webservice登录账号
			String userPwd = iAccountCached.getUserpwd(sourceOfCertification); 	//webservice登录密码
			String key = iAccountCached.getKey(sourceOfCertification);			//秘钥
			
			//调用第三方接口
			JSONObject respJson = TransferThirdParty.addNoneCarCertification(applyType, applyName, identityCard, applyPhone, sourceOfCertification, url, method, userId, userPwd, key);
			
			String code = respJson.get("code").toString();	//返回状态码 
			String msg = respJson.get("msg").toString();	//返回消息描述
			
			baseBean.setCode(code);		
			baseBean.setMsg(msg);		
			
			logger.info("提交无车证明申请采集返回结果:" + respJson);
		} catch (Exception e) {
			logger.error("提交无车证明申请采集失败！", e);
			throw e;
		}
		
		return baseBean;
	}
	/**
	 * 提交驾驶人安全事故信用表申请
	 * @Description: TODO(提交驾驶人安全事故信用表申请)
	 * @param applyType 申请类型
	 * @param applyName 申请人姓名
	 * @param identityCard 申请人身份证号
	 * @param applyPhone 申请人联系电话
	 * @param sourceOfCertification 申请来源
	 * @return
	 * @throws Exception 
	 */
	public BaseBean addSafeAccidentCredit(String applyType, String applyName, String identityCard, String applyPhone, String sourceOfCertification) throws Exception {
		logger.info("提交驾驶人安全事故信用表申请采集WebService...");
		
		BaseBean baseBean = new BaseBean();	//创建返回信息
		
		try {
			String url = iAccountCached.getUrl(sourceOfCertification); 			//webservice请求url
			String method = iAccountCached.getMethod(sourceOfCertification); 	//webservice请求方法名称
			String userId = iAccountCached.getUserid(sourceOfCertification); 	//webservice登录账号
			String userPwd = iAccountCached.getUserpwd(sourceOfCertification); 	//webservice登录密码
			String key = iAccountCached.getKey(sourceOfCertification);			//秘钥
			
			//调用第三方接口
			JSONObject respJson = TransferThirdParty.addSafeAccidentCredit(applyType, applyName, identityCard, applyPhone, sourceOfCertification, url, method, userId, userPwd, key);
			
			String code = respJson.get("code").toString();	//返回状态码 
			String msg = respJson.get("msg").toString();	//返回消息描述
			
			baseBean.setCode(code);		
			baseBean.setMsg(msg);		
			
			logger.info("提交驾驶人安全事故信用表申请采集返回结果:" + respJson);
		} catch (Exception e) {
			logger.error("提交驾驶人安全事故信用表申请采集失败！", e);
			throw e;
		}
		
		return baseBean;
	}
	@Override
	public Map<String, String> unbindVehicle(UnbindVehicleVo unbindVehicleVo) throws Exception {
		Map<String, String> map = new HashMap<>();
		try {
			String string = unbindVehicleVo.getSourceOfCertification();
			 String url = iAccountCached.getUrl(string); //webservice请求url
			 String method = iAccountCached.getMethod(string); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(string); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(string); //webservice登录密码
			 String key = iAccountCached.getKey(string); //秘钥
			 map = TransferThirdParty.unbindVehicle(unbindVehicleVo, url, method, userId, userPwd, key);
			 
		} catch (Exception e) {
			logger.error("unbindVehicle出错，错误="+ "unbindVehicleVo=" + unbindVehicleVo,e);
			throw e;
		}
		
	
		return map;
	}
	@Override
	public Map<String, Object> getBindTheOtherDriversUseMyCar(String identityCard, String numberPlateNumber,
			String plateType, String sourceOfCertification) throws Exception{
		Map<String, Object> map = new HashMap<>();
		try {
			 String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			 String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			 String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			 map = TransferThirdParty.getBindTheOtherDriversUseMyCar(identityCard,numberPlateNumber,plateType,sourceOfCertification, url, method, userId, userPwd, key);
			 
		} catch (Exception e) {
			logger.error("getBindTheOtherDriversUseMyCar出错，错误="+ "identityCard=" + identityCard+ "numberPlateNumber=" + numberPlateNumber+ "plateType=" + plateType+ "sourceOfCertification=" + sourceOfCertification,e);
			throw e;
		}
		
		return map;
	}
	@Override
	public Map<String, Object> trafficQuery(String sourceOfCertification) throws Exception{
		Map<String, Object> map = new HashMap<>();
		try {
			
			 String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			 String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			 String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			 String comUserId = iAccountCached.getUserid(sourceOfCertification);
			 String comUserPwd = iAccountCached.getUserpwd(sourceOfCertification);
			 String jkId = "event_list";
			 map = TransferThirdParty.trafficQuery(comUserId,comUserPwd,
					 jkId,sourceOfCertification, url, method, userId, userPwd, key);
			 
		} catch (Exception e) {
			logger.error("trafficQuery出错，错误="+ "sourceOfCertification=" + sourceOfCertification,e);
			throw e;
		}
		
		return map;
	}
	@Override
	public Map<String, String> detailsTrafficQuery(String zjz, String sourceOfCertification) throws Exception{
		Map<String, String> map = new HashMap<>();
		try {
			
			 String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			 String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			 String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			 String comUserId = iAccountCached.getUserid(sourceOfCertification);
			 String comUserPwd = iAccountCached.getUserpwd(sourceOfCertification);
			 String jkId = "event_msg";
			 map = TransferThirdParty.detailsTrafficQuery(comUserId,comUserPwd,
					 jkId,zjz ,sourceOfCertification, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("trafficQuery出错，错误="+ "sourceOfCertification=" + sourceOfCertification,e);
			throw e;
		}
		
		return map;
	}
	@Override
	public Map<String, String> unbindTheOtherDriverUseMyCar(
			UnbindTheOtherDriverUseMyCarVo unbindTheOtherDriverUseMyCarVo) throws Exception {
		Map<String, String> map = new HashMap<>();
		try {
			String string = unbindTheOtherDriverUseMyCarVo.getSourceOfCertification();
			 String url = iAccountCached.getUrl(string); //webservice请求url
			 String method = iAccountCached.getMethod(string); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(string); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(string); //webservice登录密码
			 String key = iAccountCached.getKey(string); //秘钥
			 map = TransferThirdParty.unbindTheOtherDriverUseMyCar(unbindTheOtherDriverUseMyCarVo, url, method, userId, userPwd, key);
			 
		} catch (Exception e) {
			logger.error("unbindTheOtherDriverUseMyCar出错，错误="+ "unbindTheOtherDriverUseMyCarVo=" + unbindTheOtherDriverUseMyCarVo,e);
			throw e;
		}
		
	
		return map;
	}
	@Override
	public Map<String, String> reauthentication(ReauthenticationVo reauthenticationVo) throws Exception {
		Map<String, String> map = new HashMap<>();
		try {
			 String sourceOfCertification = reauthenticationVo.getSourceOfCertification();
			 String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			 String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			 String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			 map = TransferThirdParty.reauthentication(reauthenticationVo, url, method, userId, userPwd, key);
			 
		} catch (Exception e) {
			logger.error("reauthentication出错，错误="+ "reauthenticationVo=" + reauthenticationVo,e);
			throw e;
		}
		
	
		return map;
	}
	
	public List<IdentificationOfAuditResultsVo> getIdentificationOfAuditResults(String idCard,String sourceOfCertification)throws Exception{
		List<IdentificationOfAuditResultsVo> identificationOfAuditResultsVos = null;
		try {
			String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			identificationOfAuditResultsVos = TransferThirdParty.identificationOfAuditResults("", idCard, sourceOfCertification, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("getIdentificationOfAuditResults出错，idCard="+ idCard + "sourceOfCertification=" + sourceOfCertification,e);
			throw e;
		}
		return identificationOfAuditResultsVos;
	}
	@Override
	public Map<String, Object> alipayAKeyRegister(String userName, String identityCard, String mobilephone,String sourceOfCertification) throws Exception {
		Map<String, Object> map = null;
		try {
			String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			map = TransferThirdParty.alipayAKeyRegister(userName, identityCard, mobilephone, sourceOfCertification, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("alipayAKeyRegister出错，userName="+ userName + "identityCard=" + identityCard + "mobilephone=" + mobilephone + "sourceOfCertification=" + sourceOfCertification,e);
			throw e;
		}
		return map;
	}
	@Override
	public BaseBean accessAuthorization(String mobilephone, String identityCard, String userSource) throws Exception {
		BaseBean baseBean = new BaseBean();
		try{
			String url = iAccountCached.getUrl(userSource); //webservice请求url
			String method = iAccountCached.getMethod(userSource); //webservice请求方法名称
			String userId = iAccountCached.getUserid(userSource); //webservice登录账号
			String userPwd = iAccountCached.getUserpwd(userSource); //webservice登录密码
			String key = iAccountCached.getKey(userSource); //秘钥
			baseBean = TransferThirdParty.accessAuthorization(mobilephone, identityCard, userSource, url, method, userId, userPwd, key);
		}catch(Exception e){
			logger.error("接入授权异常 ， mobilephone = " + mobilephone +"identityCard = "+identityCard +"userSource = "+userSource);
			throw e;
		}
		return baseBean;
	}
	@Override
	public BaseBean weChatBrushFaceAuthentication(BrushFaceVo brushFaceVo) throws Exception {
		BaseBean baseBean = new BaseBean();
		try{
			String url = iAccountCached.getUrl(brushFaceVo.getUserSource()); //webservice请求url
			String method = iAccountCached.getMethod(brushFaceVo.getUserSource()); //webservice请求方法名称
			String userId = iAccountCached.getUserid(brushFaceVo.getUserSource()); //webservice登录账号
			String userPwd = iAccountCached.getUserpwd(brushFaceVo.getUserSource()); //webservice登录密码
			String key = iAccountCached.getKey(brushFaceVo.getUserSource()); //秘钥
			baseBean = TransferThirdParty.weChatBrushFaceAuthentication(brushFaceVo, url, method, userId, userPwd, key);
		}catch(Exception e){
			logger.error("接入授权异常 ， brushFaceVo = " + brushFaceVo);
			throw e;
		}
		return baseBean;
	}
	/**
	 * 车辆绑定审核结果查询
	 * @param identityCardNo
	 * @param sourceOfCertification
	 * @return
	 * @throws Exception
	 */
	public BaseBean queryVehicleBindAuditResult(String identityCardNo, String sourceOfCertification) throws Exception {
		BaseBean baseBean = new BaseBean();
		List<VehicleBindAuditResultVo> list = new ArrayList<>();
		try{
			String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			
			JSONObject jsonObject = TransferThirdParty.queryVehicleBindAuditResult(identityCardNo, sourceOfCertification, url, method, userId, userPwd, key);
			String code = jsonObject.getString("CODE");
			String msg = jsonObject.getString("MSG");
			baseBean.setCode(code);
			baseBean.setMsg(msg);
			if (MsgCode.success.equals(code)) {
				JSONObject body = jsonObject.getJSONObject("BODY");
				Object rowObj = body.get("ROW");
				if(rowObj instanceof JSONArray){
					JSONArray jsonArray = (JSONArray) rowObj;
					for(int i = 0; i < jsonArray.size(); i++){
						VehicleBindAuditResultVo vo = new VehicleBindAuditResultVo();
						JSONObject jsonObj = jsonArray.getJSONObject(i);
						vo.setSerialNo(jsonObj.getString("CID"));
						vo.setPlateNo(jsonObj.getString("HPHM"));
						vo.setPlateType(jsonObj.getString("HPZL"));
						vo.setAuditStatus(jsonObj.getString("SHZT"));
						vo.setCancelReason(jsonObj.getString("TBYY"));
						list.add(vo);
					}
				}else if(rowObj instanceof JSONObject){
					JSONObject jsonObj = (JSONObject) rowObj;
					VehicleBindAuditResultVo vo = new VehicleBindAuditResultVo();
					vo.setSerialNo(jsonObj.getString("CID"));
					vo.setPlateNo(jsonObj.getString("HPHM"));
					vo.setPlateType(jsonObj.getString("HPZL"));
					vo.setAuditStatus(jsonObj.getString("SHZT"));
					vo.setCancelReason(jsonObj.getString("TBYY"));
					list.add(vo);
				}
				baseBean.setData(list);
			}
			logger.info("车辆绑定审核结果查询结果：" + JSON.toJSONString(baseBean));
		}catch(Exception e){
			logger.error("车辆绑定审核结果查询异常 ， identityCardNo = " + identityCardNo + " ， sourceOfCertification = " + sourceOfCertification);
			throw e;
		}
		return baseBean;
	}
	
	@Override
	public List<UserBindAlipay> getUserBindAlipays(int page, int pageSize) {
		page = (page - 1) * pageSize;
		return userBindAlipayMapper.getUserBindAlipays(page, pageSize);
	}
	@Override
	public List<UserBind> getUserBinds(int page, int pageSize) {
		page = (page - 1) * pageSize;
		return accountMapper.getUserBinds(page, pageSize);
	}
	
	@Override
	public BaseBean companyRegister(CompanyRegisterVo companyRegisterVo) throws Exception {
		BaseBean baseBean = new BaseBean();
		try{
			String url = iAccountCached.getUrl(companyRegisterVo.getSourceOfCertification()); //webservice请求url
			String method = iAccountCached.getMethod(companyRegisterVo.getSourceOfCertification()); //webservice请求方法名称
			String userId = iAccountCached.getUserid(companyRegisterVo.getSourceOfCertification()); //webservice登录账号
			String userPwd = iAccountCached.getUserpwd(companyRegisterVo.getSourceOfCertification()); //webservice登录密码
			String key = iAccountCached.getKey(companyRegisterVo.getSourceOfCertification()); //秘钥
			baseBean = TransferThirdParty.companyRegister(companyRegisterVo, url, method, userId, userPwd, key);
		}catch(Exception e){
			logger.error("公车注册异常 ， companyRegisterVo = " + companyRegisterVo);
			throw e;
		}
		return baseBean;
	}


	@Override
	public BaseBean queryCompanyRegister(String organizationCodeNumber, String sourceOfCertification) throws Exception {
		BaseBean baseBean = new BaseBean();
		try{
			String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			baseBean = TransferThirdParty.queryCompanyRegister(organizationCodeNumber,sourceOfCertification , url, method, userId, userPwd, key);
		}catch(Exception e){
			logger.error("公车注册查询异常 ， organizationCodeNumber = " + organizationCodeNumber +", sourceOfCertification = " +sourceOfCertification);
			throw e;
		}
		return baseBean;
	}


	@Override
	public BaseBean companyUserLogin(String loginUser, String loginPwd, String sourceOfCertification) throws Exception {
		BaseBean baseBean = new BaseBean();
		try{
			String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			String key = iAccountCached.getKey(sourceOfCertification); //秘钥			
			baseBean = TransferThirdParty.companyUserLogin(loginUser, loginPwd, sourceOfCertification, url, method, userId, userPwd, key);
		}catch(Exception e){
			logger.error("单位用户登录异常 ， loginUser = " + loginUser +", loginPwd = " + loginPwd + ", sourceOfCertification = " +sourceOfCertification);
			throw e;
		}
		return baseBean;
	}


	@Override
	public BaseBean companyUserChangePwd (String loginUser, String oldPwd, String newPwd, String sourceOfCertification)
			throws Exception {
		BaseBean baseBean = new BaseBean();
		try{
			String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			baseBean = TransferThirdParty.companyUserChangePwd(loginUser, oldPwd, newPwd, sourceOfCertification, url, method, userId, userPwd, key);
		}catch(Exception e){
			logger.error("单位用户修改密码异常 ， loginUser = " + loginUser +", oldPwd = " +oldPwd + ", newPwd = " + newPwd + ", sourceOfCertification = " + sourceOfCertification);
			throw e;
		}
		return baseBean;
	}


	@Override
	public BaseBean bindCompanyCar(BindCompanyCarVo bindCompanyCarVo) throws Exception {
		BaseBean baseBean = new BaseBean();
		try{
			String url = iAccountCached.getUrl(bindCompanyCarVo.getSourceOfCertification()); //webservice请求url
			String method = iAccountCached.getMethod(bindCompanyCarVo.getSourceOfCertification()); //webservice请求方法名称
			String userId = iAccountCached.getUserid(bindCompanyCarVo.getSourceOfCertification()); //webservice登录账号
			String userPwd = iAccountCached.getUserpwd(bindCompanyCarVo.getSourceOfCertification()); //webservice登录密码
			String key = iAccountCached.getKey(bindCompanyCarVo.getSourceOfCertification()); //秘钥
			baseBean = TransferThirdParty.bindCompanyCar(bindCompanyCarVo, url, method, userId, userPwd, key);
		}catch(Exception e){
			logger.error("公车绑定异常 ， bindCompanyCarVo = " + bindCompanyCarVo);
			throw e;
		}
		return baseBean;
	}


	@Override
	public BaseBean getMyCompanyCars(String loginUser, String sourceOfCertification) throws Exception {
		BaseBean baseBean = new BaseBean();
		try{
			String url = iAccountCached.getUrl(sourceOfCertification); //webservice请求url
			String method = iAccountCached.getMethod(sourceOfCertification); //webservice请求方法名称
			String userId = iAccountCached.getUserid(sourceOfCertification); //webservice登录账号
			String userPwd = iAccountCached.getUserpwd(sourceOfCertification); //webservice登录密码
			String key = iAccountCached.getKey(sourceOfCertification); //秘钥
			baseBean = TransferThirdParty.getMyCompanyCars(loginUser, sourceOfCertification, url, method, userId, userPwd, key);
		}catch(Exception e){
			logger.error("个人名下所有车辆查询异常 ， loginUser = " + loginUser +", sourceOfCertification = " +sourceOfCertification);
			throw e;
		}
		return baseBean;
	}


	@Override
	public BaseBean informationCollection(InformationCollectionVo informationCollectionVo) throws Exception {
		BaseBean baseBean = new BaseBean();
		try{
			String url = iAccountCached.getUrl2(informationCollectionVo.getSourceOfCertification()); //webservice请求url
			String method = iAccountCached.getMethod(informationCollectionVo.getSourceOfCertification()); //webservice请求方法名称
			String userId = iAccountCached.getUserid(informationCollectionVo.getSourceOfCertification()); //webservice登录账号
			String userPwd = iAccountCached.getUserpwd(informationCollectionVo.getSourceOfCertification()); //webservice登录密码
			String key = iAccountCached.getKey(informationCollectionVo.getSourceOfCertification()); //秘钥
			informationCollectionVo.setUserId(userId);
			informationCollectionVo.setPassword(userPwd);
			baseBean = TransferThirdParty.informationCollection(informationCollectionVo, url, method, userId, userPwd, key);
		}catch(Exception e){
			logger.error("信息采集异常 ， informationCollectionVo = " + informationCollectionVo );
			throw e;
		}
		return baseBean;
	}


	@Override
	public BaseBean queryInformationCollection(InformationCollectionVo informationCollectionVo) throws Exception {
		BaseBean baseBean = new BaseBean();
		try{
			String url = iAccountCached.getUrl2(informationCollectionVo.getSourceOfCertification()); //webservice请求url
			String method = iAccountCached.getMethod(informationCollectionVo.getSourceOfCertification()); //webservice请求方法名称
			String userId = iAccountCached.getUserid(informationCollectionVo.getSourceOfCertification()); //webservice登录账号
			String userPwd = iAccountCached.getUserpwd(informationCollectionVo.getSourceOfCertification()); //webservice登录密码
			String key = iAccountCached.getKey(informationCollectionVo.getSourceOfCertification()); //秘钥
			informationCollectionVo.setUserId(userId);
			informationCollectionVo.setPassword(userPwd);
			baseBean = TransferThirdParty.queryInformationCollection(informationCollectionVo, url, method, userId, userPwd, key);
		}catch(Exception e){
			logger.error("信息采集查询异常 ， informationCollectionVo = " + informationCollectionVo );
			throw e;
		}
		return baseBean;
	}
	
}
	