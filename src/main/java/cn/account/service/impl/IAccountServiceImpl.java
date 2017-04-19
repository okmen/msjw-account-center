package cn.account.service.impl;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.account.bean.Documentation;
import cn.account.bean.UserBind;
import cn.account.bean.WechatUserInfoBean;
import cn.account.bean.vo.AuthenticationBasicInformationVo;
import cn.account.bean.vo.BindCarVo;
import cn.account.bean.vo.BindTheVehicleVo;
import cn.account.bean.vo.DriverLicenseInformationSheetVo;
import cn.account.bean.vo.DriverLicenseToSupplementThePermitBusinessVo;
import cn.account.bean.vo.DrivingLicenseVo;
import cn.account.bean.vo.ElectronicDriverLicenseVo;
import cn.account.bean.vo.InformationSheetVo;
import cn.account.bean.vo.LoginReturnBeanVo;
import cn.account.bean.vo.MotorVehicleBusiness;
import cn.account.bean.vo.MotorVehicleInformationSheetVo;
import cn.account.bean.vo.MyBusinessVo;
import cn.account.bean.vo.MyDriverLicenseVo;
import cn.account.bean.vo.ReadilyShootVo;
import cn.account.bean.vo.RegisterVo;
import cn.account.bean.vo.UserBasicVo;
import cn.account.bean.vo.queryclassservice.CertificationProgressQueryVo;
import cn.account.bean.vo.queryclassservice.DriverLicenseBusinessVo;
import cn.account.bean.vo.queryclassservice.MakeAnAppointmentVo;
import cn.account.bean.vo.queryclassservice.MotorVehicleBusinessVo;
import cn.account.cached.impl.IAccountCachedImpl;
import cn.account.dao.IAccountDao;
import cn.account.dao.IDocumentDao;
import cn.account.dao.IUserValidateCodeDao;
import cn.account.orm.DocumentationORM;
import cn.account.orm.UserValidateCodeORM;
import cn.account.service.IAccountService;
import cn.account.utils.TransferThirdParty;

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
	
	@Autowired
	private IUserValidateCodeDao userValidateCodeDao;
	@Autowired
	private IDocumentDao documentDao;
	
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
		DocumentationORM documentationORM = documentDao.getDocumentationORMByNoticeKey(noticeKey);
		BeanUtils.copyProperties(documentationORM, documentation);
		return documentation;
	}
	/**
	 * 登录
	 * @return
	 * @throws Exception 
	 */
	@Override
	public LoginReturnBeanVo login(String loginName,String password,String sourceOfCertification,String openId,String loginClient) throws Exception {
		LoginReturnBeanVo loginReturnBean = new LoginReturnBeanVo();
		
		String url = iAccountCached.getUrl(); //webservice请求url
		String method = iAccountCached.getMethod(); //webservice请求方法名称
		String userId = iAccountCached.getUserid(); //webservice登录账号
		String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
		String key = iAccountCached.getKey(); //秘钥
		String identityCard = "";
		String mobilephone = "";
		AuthenticationBasicInformationVo authenticationBasicInformationVo = null;
		//用户登录接口
		Map<String, String> map = TransferThirdParty.login(loginName, password, url, method, userId, userPwd, key);
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
					if("本人".equals(isMyself)){
						authenticationBasicInformationVo.setMyNumberPlate(bindTheVehicleVo.getNumberPlateNumber());
						authenticationBasicInformationVo.setBehindTheFrame4Digits(bindTheVehicleVo.getBehindTheFrame4Digits());
						authenticationBasicInformationVo.setPlateType(bindTheVehicleVo.getPlateType());
					}
				}
			}
			loginReturnBean.setCode(code);
			loginReturnBean.setMsg(msg);
			
			//登录成功绑定，已经绑定就改下状态为isBind=1,没有则绑定
			UserBind userBind = accountDao.getLoginInfo(identityCard, openId, loginClient);
			if(null == userBind){
				userBind = new UserBind();
				userBind.setClientType(loginClient);
				userBind.setBindDate(new Date());
				userBind.setIdCard(identityCard);
				userBind.setMobileNumber(mobilephone);
				userBind.setIsBind(0);
				userBind.setOpenId(openId);
				accountDao.addLoginInfo(userBind);
			}else{
				accountDao.updateUserBind(identityCard,openId,loginClient);
			}
		}else {
			//登录失败
			loginReturnBean.setCode(code);
			loginReturnBean.setMsg(msg);
		}
    	loginReturnBean.setAuthenticationBasicInformation(authenticationBasicInformationVo);
    	//登录信息入库
    	return loginReturnBean;
	}
	/**
	 * 认证基本信息查询接口
	 * @param idCard 身份证
	 * @param sourceOfCertification 认证来源
	 * @return
	 * @throws Exception
	 */
	public AuthenticationBasicInformationVo authenticationBasicInformationQuery(String idCard,String sourceOfCertification) throws Exception{
		String url = iAccountCached.getUrl(); //webservice请求url
		String method = iAccountCached.getMethod(); //webservice请求方法名称
		String userId = iAccountCached.getUserid(); //webservice登录账号
		String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
		String key = iAccountCached.getKey(); //秘钥
		
		AuthenticationBasicInformationVo authenticationBasicInformationVo = TransferThirdParty.authenticationBasicInformationQuery(idCard,sourceOfCertification, url, method,userId,userPwd,key);
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
		
		String url = iAccountCached.getUrl(); //webservice请求url
		String method = iAccountCached.getMethod(); //webservice请求方法名称
		String userId = iAccountCached.getUserid(); //webservice登录账号
		String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
		String key = iAccountCached.getKey(); //秘钥
		Map<String, Object> map = TransferThirdParty.queryMachineInformationSheet(applyType, identityCard, sourceOfCertification, url, method, userId, userPwd, key);
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
		String url = iAccountCached.getUrl(); //webservice请求url
		String method = iAccountCached.getMethod(); //webservice请求方法名称
		String userId = iAccountCached.getUserid(); //webservice登录账号
		String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
		String key = iAccountCached.getKey(); //秘钥
		Map<String, String> map = TransferThirdParty.commitAAingleApplicationForMotorVehicleInformation(userName, identityCard, mobilephone,
				numberPlateNumber, plateType, sourceOfCertification, url, method, userId, userPwd, key);
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
		String url = iAccountCached.getUrl(); //webservice请求url
		String method = iAccountCached.getMethod(); //webservice请求方法名称
		String userId = iAccountCached.getUserid(); //webservice登录账号
		String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
		String key = iAccountCached.getKey(); //秘钥
		Map<String, String> map = TransferThirdParty.commitDriverInformationSinglePrintApplicationInterface(applyType, userName, identityCard, mobilephone, sourceOfCertification, url, method, userId, userPwd, key);
		
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
		 String url = iAccountCached.getUrl(); //webservice请求url
		 String method = iAccountCached.getMethod(); //webservice请求方法名称
		 String userId = iAccountCached.getUserid(); //webservice登录账号
		 String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
		 String key = iAccountCached.getKey(); //秘钥
		ElectronicDriverLicenseVo electronicDriverLicenseVo = TransferThirdParty.getElectronicDriverLicense(driverLicenseNumber, userName, mobileNumber, 
				sourceOfCertification, url, method, userId, userPwd, key);
		return electronicDriverLicenseVo;
	}


	@Override
	public DrivingLicenseVo getDrivingLicense(String numberPlatenumber, String plateType, String mobileNumber,String sourceOfCertification)throws Exception {
		 String url = iAccountCached.getUrl(); //webservice请求url
		 String method = iAccountCached.getMethod(); //webservice请求方法名称
		 String userId = iAccountCached.getUserid(); //webservice登录账号
		 String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
		 String key = iAccountCached.getKey(); //秘钥
		 DrivingLicenseVo drivingLicenseVo = TransferThirdParty.getDrivingLicense(numberPlatenumber, plateType, mobileNumber, sourceOfCertification, url, method, userId, userPwd, key);
		return drivingLicenseVo;
	}


	@Override
	public MyDriverLicenseVo getMyDriverLicense(String identityCard,String sourceOfCertification) throws Exception {
		String url = iAccountCached.getUrl(); //webservice请求url
		 String method = iAccountCached.getMethod(); //webservice请求方法名称
		 String userId = iAccountCached.getUserid(); //webservice登录账号
		 String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
		 String key = iAccountCached.getKey(); //秘钥
		 MyDriverLicenseVo myDriverLicenseVo = TransferThirdParty.getMyDriverLicense(identityCard, sourceOfCertification, url, method, userId, userPwd, key);
		
		return myDriverLicenseVo;
	}


	@Override
	public List<BindTheVehicleVo> getBndTheVehicles(String identityCard,String mobilephone,String sourceOfCertification) throws Exception {
		 String url = iAccountCached.getUrl(); //webservice请求url
		 String method = iAccountCached.getMethod(); //webservice请求方法名称
		 String userId = iAccountCached.getUserid(); //webservice登录账号
		 String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
		 String key = iAccountCached.getKey(); //秘钥
		 List<BindTheVehicleVo> bindTheVehicleVos = TransferThirdParty.bindsTheMotorVehicleQuery(mobilephone, identityCard, sourceOfCertification, url, method, userId, userPwd, key);
		return bindTheVehicleVos;
	}
	@Override
	public MotorVehicleInformationSheetVo getMotorVehicleInformationSheet(String identityCard,String sourceOfCertification) throws Exception {
		 String url = iAccountCached.getUrl(); //webservice请求url
		 String method = iAccountCached.getMethod(); //webservice请求方法名称
		 String userId = iAccountCached.getUserid(); //webservice登录账号
		 String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
		 String key = iAccountCached.getKey(); //秘钥
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
		 MotorVehicleInformationSheetVo motorVehicleInformationSheetVo = new MotorVehicleInformationSheetVo();
		 motorVehicleInformationSheetVo.setIdentityCard(authenticationBasicInformationVo.getIdentityCard());
		 motorVehicleInformationSheetVo.setMobilephone(authenticationBasicInformationVo.getMobilephone());
		 motorVehicleInformationSheetVo.setUserName(authenticationBasicInformationVo.getTrueName());
		
		 motorVehicleInformationSheetVo.setNumberPlateNumbers(numberPlateNumbers);
		 //车辆类型(客户端写死 例如 key-value)
		 //motorVehicleInformationSheetVo.setPlateTypes(plateTypes);
		return motorVehicleInformationSheetVo;
	};
	/**
	 * 驾驶证业务，包括(驾驶证业务查询、驾驶人信息单、驾驶人安全事故信用表)
	 * @return
	 */
	private List<MyBusinessVo> getMyBusinessVos11(List<DriverLicenseToSupplementThePermitBusinessVo> driverLicenseToSupplementThePermitBusinessVos){
		List<MyBusinessVo> myBusinessVos11 = new ArrayList<MyBusinessVo>(); //驾驶证业务，包括(驾驶证业务查询、驾驶人信息单、驾驶人安全事故信用表)
		for(DriverLicenseToSupplementThePermitBusinessVo driverLicenseToSupplementThePermitBusinessVo : driverLicenseToSupplementThePermitBusinessVos){
			 MyBusinessVo myBusinessVo = new MyBusinessVo();
			 myBusinessVo.setApplicationTime(driverLicenseToSupplementThePermitBusinessVo.getWSLRSJ());//申请时间
			 myBusinessVo.setDetailedBusiness(6);
			 // B：补证 H换证
			 if("B".equals(driverLicenseToSupplementThePermitBusinessVo.getHBLX())){
				 myBusinessVo.setBusinessTitle("驾驶证补证");//业务名称
			 }
			 if("Z".equals(driverLicenseToSupplementThePermitBusinessVo.getHBLX())){
				 myBusinessVo.setBusinessTitle("驾驶证转入");//业务名称
			 }
			 myBusinessVo.setIdentityCard(driverLicenseToSupplementThePermitBusinessVo.getSFZMHM()); //身份证
			//myBusinessVo.setPlateType(plateType);//号牌种类例如 蓝牌
			 myBusinessVo.setReceptionTime(driverLicenseToSupplementThePermitBusinessVo.getWSLRSJ()); //受理时间
			 if("0".equals(driverLicenseToSupplementThePermitBusinessVo.getZHCLZT())){
				 //0待初审  -->办理中
				 myBusinessVo.setStatus(1); //0-全部，1-办理中，2-已完结
				 myBusinessVo.setStatusStr("待初审");
			 }
			 if("1".equals(driverLicenseToSupplementThePermitBusinessVo.getZHCLZT())){
				 //1初审通过，待制证  -->办理中
				 myBusinessVo.setStatus(1); //0-全部，1-办理中，2-已完结
				 myBusinessVo.setStatusStr("初审通过，待制证");
			 }
			 if("2".equals(driverLicenseToSupplementThePermitBusinessVo.getZHCLZT())){
				 //2车管已制证  -->办理中
				 myBusinessVo.setStatus(2); //0-全部，1-办理中，2-已完结
				 myBusinessVo.setStatusStr("车管已制证");
			 }
			 if("3".equals(driverLicenseToSupplementThePermitBusinessVo.getZHCLZT())){
				 //3待初审  -->办理中
				 myBusinessVo.setStatus(1); //0-全部，1-办理中，2-已完结
				 myBusinessVo.setStatusStr("待初审");
			 }
			 if("TB".equals(driverLicenseToSupplementThePermitBusinessVo.getZHCLZT())){
				 //TB退办  -->办理中
				 myBusinessVo.setStatus(2); //0-全部，1-办理中，2-已完结
				 myBusinessVo.setStatusStr("退办");
			 }
			 myBusinessVo.setUserName(driverLicenseToSupplementThePermitBusinessVo.getXM());//姓名
			//myBusinessVo.setVehicleNumber(vehicleNumber);//车辆号码 例如 粤B701NR
			 myBusinessVos11.add(myBusinessVo);
		 }
		return myBusinessVos11;
	}
	/**
	 * //机动车业务，包括(机动车业务查询、机动车信息单、无车证明申请)
	 * @return
	 */
	private List<MyBusinessVo> getMyBusinessVos22(List<MotorVehicleBusiness> motorVehicleBusinesses){
		List<MyBusinessVo> myBusinessVos22 = new ArrayList<MyBusinessVo>(); //机动车业务，包括(机动车业务查询、机动车信息单、无车证明申请)
		for(MotorVehicleBusiness motorVehicleBusiness : motorVehicleBusinesses){
			MyBusinessVo myBusinessVo = new MyBusinessVo();
			 myBusinessVo.setVehicleNumber(motorVehicleBusiness.getHPHM());
			 myBusinessVo.setDetailedBusiness(5);
			 //YWLX=1换  YWLX=5 补 
			 if("1".equals(motorVehicleBusiness.getYWLX())){
				 myBusinessVo.setBusinessTitle("换领机动车行驶证");//业务名称
			 }
			 if("5".equals(motorVehicleBusiness.getYWLX())){
				 myBusinessVo.setBusinessTitle("补领机动车行驶证");//业务名称
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
		return myBusinessVos22;
	}
	/**
	 * 机动车信息单--机动车业务
	 * @return
	 */
	private void getQueryMachineInformationSheet2(Map<String, Object> map, List<MyBusinessVo> myBusinessVos22){
		List<InformationSheetVo> informationSheetVos = (List<InformationSheetVo>) map.get("data");
		List<MyBusinessVo> myBusinessVos = new ArrayList<MyBusinessVo>();
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
		myBusinessVos22.addAll(myBusinessVos);
	}
	/**
	 * 无车证明申请--机动车业务
	 * @return
	 */
	private void getQueryMachineInformationSheet3(Map<String, Object> map, List<MyBusinessVo> myBusinessVos22){
		List<InformationSheetVo> informationSheetVos = (List<InformationSheetVo>) map.get("data");
		List<MyBusinessVo> myBusinessVos = new ArrayList<MyBusinessVo>();
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
			myBusinessVo.setApplicationTime(informationSheetVo.getApplicationTime());
			//具体业务 1、驾驶人信息单；2、机动车信息单；3、无车证明申请；4、驾驶人安全事故信用表；5、机动车行驶证；6、驾驶证
			myBusinessVo.setDetailedBusiness(3);
			myBusinessVos.add(myBusinessVo);
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
		myBusinessVos11.addAll(myBusinessVos);
	}
	/**
	 * 驾驶人安全事故信用表--驾驶证业务
	 * @return
	 */
	private void getQueryMachineInformationSheet4(Map<String, Object> map, List<MyBusinessVo> myBusinessVos11){
		List<InformationSheetVo> informationSheetVos = (List<InformationSheetVo>) map.get("data");
		List<MyBusinessVo> myBusinessVos = new ArrayList<MyBusinessVo>();
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
		myBusinessVos11.addAll(myBusinessVos);
	}
	
	@Override
	public List<MyBusinessVo> getMyBusiness(Integer businessType, Integer businessStatus, String identityCard,String sourceOfCertification) throws Exception {
		List<MyBusinessVo> returnMyBusinessVo = new ArrayList<MyBusinessVo>();
		
		String url = iAccountCached.getUrl(); //webservice请求url
		 String method = iAccountCached.getMethod(); //webservice请求方法名称
		 String userId = iAccountCached.getUserid(); //webservice登录账号
		 String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
		 String key = iAccountCached.getKey(); //秘钥
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
		 getQueryMachineInformationSheet4(map4,myBusinessVos11);
		 
		 getQueryMachineInformationSheet2(map1,myBusinessVos22);
		 getQueryMachineInformationSheet3(map4,myBusinessVos22);
		 
		//业务类型	 0-全部
		if(0 == businessType){
			//业务状态 0-全部、1-办理中、2-已完结
			if(0 == businessStatus){
				returnMyBusinessVo.addAll(myBusinessVos11);
				returnMyBusinessVo.addAll(myBusinessVos22);
			}else if(1 == businessStatus){
				
			}else if(2 == businessStatus) {
				
			}
		}
		//业务类型 	1-机动车业务
		if(1 == businessType){
			//业务状态 0-全部、1-办理中、2-已完结
			if(0 == businessStatus){
				returnMyBusinessVo.addAll(myBusinessVos22);
			}else if(1 == businessStatus){
				
			}else if(2 == businessStatus) {
				
			}
		}
		//业务类型	2-驾驶证业务
		if(2 == businessType){
			//业务状态 0-全部、1-办理中、2-已完结
			if(0 == businessStatus){
				returnMyBusinessVo.addAll(myBusinessVos11);
			}else if(1 == businessStatus){
				
			}else if(2 == businessStatus) {
				
			}
		}
		return returnMyBusinessVo;
	}
	@Override
	public void sendSMSVerificatioCode(String mobilephone,String valideteCode) {
		//插入数据库
		UserValidateCodeORM userValidateCodePo = new UserValidateCodeORM();
		userValidateCodePo.setGenDate(new Date());
		userValidateCodePo.setMobilephone(mobilephone);
		userValidateCodePo.setValidateCode(valideteCode);
		
		int result = userValidateCodeDao.addUserValidateCode(userValidateCodePo);
		if(1 == result){
			//插入redis,为5分钟有效期
			iAccountCached.insertUserValidateCode(mobilephone, valideteCode);
		}
	}


	@Override
	public int verificatioCode(String mobilephone, String validateCode) {
		// 0-验证成功，1-验证失败，2-验证码失效
		int result = 0;
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
		return result;
	}

	@Override
	public int unbindVehicle(UserBind userBind) {
		int cancelSuccess = 0;
	      if(userBind == null) {
	          logger.error("unbindVehicle接收参数有误");
	          return cancelSuccess;
	      }
	      try {
	          cancelSuccess = accountDao.unbindVehicle(userBind);
	      } catch (Exception e) {
	          logger.error("更新绑定状态失败，cancelSuccess = " + cancelSuccess);
	          throw e;
	      }
	      return cancelSuccess;
	}


	@Override
	public JSONObject addVehicle(BindCarVo bindCarVo) throws Exception{
		JSONObject json= null;
		try {
			
			 String url = iAccountCached.getUrl(); //webservice请求url
			 String method = iAccountCached.getMethod(); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
			 String key = iAccountCached.getKey(); //秘钥
			 json = NozzleMeans.addVehicle(bindCarVo, url, method, userId, userPwd, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return json;
	}


	@Override
	public JSONObject updateUser(UserBasicVo userBasicVo)throws Exception {
		JSONObject json= null;
		try {
			
			 String url = iAccountCached.getUrl(); //webservice请求url
			 String method = iAccountCached.getMethod(); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
			 String key = iAccountCached.getKey(); //秘钥
			 json = NozzleMeans.updateUser(userBasicVo, url, method, userId, userPwd, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return json;
	}


	@Override
	public JSONObject updateMobile(UserBasicVo userBasicVo)throws Exception {		
				JSONObject json= null;
				try {
					
					 String url = iAccountCached.getUrl(); //webservice请求url
					 String method = iAccountCached.getMethod(); //webservice请求方法名称
					 String userId = iAccountCached.getUserid(); //webservice登录账号
					 String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
					 String key = iAccountCached.getKey(); //秘钥
					 json = NozzleMeans.updateMobile(userBasicVo, url, method, userId, userPwd, key);
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				return json;
	}
	
	@Override
	public JSONObject updatePwd(UserBasicVo userBasicVo)throws Exception {
		
		JSONObject json= null;
		try {
			
			 String url = iAccountCached.getUrl(); //webservice请求url
			 String method = iAccountCached.getMethod(); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
			 String key = iAccountCached.getKey(); //秘钥
			 json = NozzleMeans.updatePwd(userBasicVo, url, method, userId, userPwd, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	
	@Override
	public JSONObject readilyShoot(ReadilyShootVo readilyShootVo) throws Exception {

		JSONObject json= null;
		try {
			
			 String url = iAccountCached.getUrl(); //webservice请求url
			 String method = iAccountCached.getMethod(); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
			 String key = iAccountCached.getKey(); //秘钥
			 json = NozzleMeans.readilyShoot(readilyShootVo, url, method, userId, userPwd, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return json;
	}

	
	@Override
	public JSONObject iAmTheOwner(RegisterVo registerVo) throws Exception{
		
		JSONObject json= null;
		try {
			
			 String url = iAccountCached.getUrl(); //webservice请求url
			 String method = iAccountCached.getMethod(); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
			 String key = iAccountCached.getKey(); //秘钥
			 json = NozzleMeans.iAmTheOwner(registerVo, url, method, userId, userPwd, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	
	
	@Override
	public JSONObject iamALongtimeUser(RegisterVo registerVo) throws Exception {			

		JSONObject json= null;
		try {
			
			 String url = iAccountCached.getUrl(); //webservice请求url
			 String method = iAccountCached.getMethod(); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
			 String key = iAccountCached.getKey(); //秘钥
			 json = NozzleMeans.iamALongtimeUser(registerVo, url, method, userId, userPwd, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	
	
	@Override
	public JSONObject haveDriverLicenseNotCar(RegisterVo registerVo) throws Exception {

		JSONObject json= null;
		try {
			
			 String url = iAccountCached.getUrl(); //webservice请求url
			 String method = iAccountCached.getMethod(); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
			 String key = iAccountCached.getKey(); //秘钥
			 json = NozzleMeans.haveDriverLicenseNotCar(registerVo, url, method, userId, userPwd, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return json;
	}
	
	
	
	
	
	@Override
	public JSONObject isPedestrianNotDriver(RegisterVo registerVo)throws Exception {

		JSONObject json= null;
		try {
			
			 String url = iAccountCached.getUrl(); //webservice请求url
			 String method = iAccountCached.getMethod(); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(); //webservice登录密码
			 String key = iAccountCached.getKey(); //秘钥
			 json = NozzleMeans.isPedestrianNotDriver(registerVo, url, method, userId, userPwd, key);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return json;
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

}
