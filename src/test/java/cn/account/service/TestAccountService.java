package cn.account.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;
import javax.swing.text.Document;
import javax.xml.ws.WebServiceContext;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.account.bean.DeviceBean;
import cn.account.bean.IssuingLicenceAuthority;
import cn.account.bean.ReadilyShoot;
import cn.account.bean.ResultOfReadilyShoot;
import cn.account.bean.Token;
import cn.account.bean.UserBind;
import cn.account.bean.UserBindAlipay;
import cn.account.bean.UserOpenidBean;
import cn.account.bean.UserRegInfo;
import cn.account.bean.WechatUserInfoBean;
import cn.account.bean.vo.BindCarVo;
import cn.account.bean.vo.BindDriverLicenseVo;
import cn.account.bean.vo.DriverChangeContactVo;
import cn.account.bean.vo.DriverLicenseAnnualVerificationVo;
import cn.account.bean.vo.DriverLicenseIntoVo;
import cn.account.bean.vo.DriverLicenseVoluntaryDemotionVo;
import cn.account.bean.vo.MyDriverLicenseVo;
import cn.account.bean.vo.ReadilyShootVo;
import cn.account.bean.vo.RenewalDriverLicenseVo;
import cn.account.bean.vo.RepairOrReplaceDriverLicenseVo;
import cn.account.bean.vo.ResultOfBIndDriverLicenseVo;
import cn.account.bean.vo.UnbindVehicleVo;
import cn.account.service.IAccountService;
import cn.sdk.util.AESNewUtils;
import cn.sdk.util.DESUtils;


import cn.sdk.util.HttpClientUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.InputStreamRequestEntity;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:junit-test.xml" })
public class TestAccountService {

	@Autowired
	@Qualifier("accountService")
	private IAccountService accountService;
	
	/**
	 * 我的驾驶证
	 * @throws Exception 
	 */
	@Test
	public void testMyDriverLicense() throws Exception{
		String IDcard = "420881198302280017";
		String userSource = "C";
		MyDriverLicenseVo myDriverLicense = accountService.getMyDriverLicense(IDcard, userSource);
		System.out.println(myDriverLicense);
	}
	/**
	 * 绑定车辆
	 * @throws Exception 
	 */
	@Test
	public void testBindVehicle() throws Exception{
		BindCarVo vo = new BindCarVo();
		vo.setBindType(1);
		vo.setCertifiedSource("C");
		vo.setInputIP("192.168.1.245");
		vo.setLicensePlateNumber("B6F7M1");
		vo.setLicensePlateType("02");
		vo.setProvinceAbbreviation("粤");
		vo.setUserSource("C");
		vo.setUserIdCard("622822198502074110");
		JSONObject addVehicle = accountService.addVehicle(vo);
		System.out.println(addVehicle);
	}
	/**
	 * 解绑车辆
	 * 
	 */
	@Test
	public void testUnbindVehicle(){
		UnbindVehicleVo unbindVehicleVo = new UnbindVehicleVo();
		unbindVehicleVo.setJblx("1");
		unbindVehicleVo.setLicensePlateNumber("B6F7M1");
		unbindVehicleVo.setLicensePlateType("02");
		unbindVehicleVo.setLoginUser("622822198502074110");
		unbindVehicleVo.setIdentificationNO("A");
		unbindVehicleVo.setIDcard("622822198502074110");
		unbindVehicleVo.setSourceOfCertification("C");
		Map<String, String> map = accountService.unbindVehicle(unbindVehicleVo);
		System.out.println(map);
	}
	
	/**
	 * 驾驶人联系方式变更
	 */
	@Test
	public void testDriverChangeContact(){
		//http://192.168.1.245:8080/web/user/driverChangeContact.html?name=张宇帆&gender=1&identificationNO=A&IDcard=622822198502074110&driverLicense=622822198502074110&mailingAddress=深圳市宝安区&mobilephone=15920050177&loginUser=15920050177&userSource=C&IDCardPhoto1=111&IDCardPhoto2=222&driverLicensePhoto=111 
		DriverChangeContactVo dv = new DriverChangeContactVo();
		dv.setBusinessType("L");
		dv.setDriverLicensePhoto("ddd");
		dv.setGender("n");
		dv.setIDcard("622822198502074110");
		dv.setDriverLicense("622822198502074110");
		dv.setIDCardPhoto1("111");
		dv.setIDCardPhoto2("222");
		dv.setIdentificationNO("A");
		dv.setIp("11");
		dv.setLoginUser("15920050177");
		dv.setMailingAddress("深圳市宝安区");
		dv.setMobilephone("15920050177");
		dv.setName("王玉璞");
		dv.setUserSource("C");
		Map< String, String> map = new HashMap<>();
		map = accountService.driverChangeContact(dv);
		System.out.println(map);
	}
	
	/**
	 * 驾驶证补证
	 */
	@Test
	public void testRepairDriverLicense(){
		//http://192.168.1.245:8080/web/user/repairDriverLicense.html?repairReason=1&identificationNO=A&IDcard=445222199209020034&name=张宇帆&mobilephone=15920050177&IDCardPhoto1=111&IDCardPhoto2=222&photoReturnNumberString=111&foreignersLiveTable=111&placeOfDomicile=深圳&postalcode=1&receiverName=111&receiverNumber=15920050177&mailingAddress=深圳市宝安区&livePhoto1=111&livePhoto2=222&loginUser=445222199209020034&sourceOfCertification=C&userSource=C                    
		RepairOrReplaceDriverLicenseVo rv = new RepairOrReplaceDriverLicenseVo();
		rv.setBusinessType("B");
		rv.setForeignersLiveTable("111");
		rv.setIDcard("445222199209020034");
		rv.setIDCardPhoto1("111");
		rv.setIDCardPhoto2("222");
		rv.setIdentificationNO("A");
		rv.setIp("11");
		rv.setLivePhoto1("111");
		rv.setLivePhoto2("222");
		rv.setLoginUser("445222199209020034");
		rv.setMailingAddress("深圳市宝安区");
		rv.setName("张宇帆");
		rv.setPhotoReturnNumberString("123456");
		rv.setPlaceOfDomicile("深圳");
		rv.setPostalcode("");
		rv.setReceiverName("333");
		rv.setReceiverNumber("15920050177");
		rv.setRepairReason("");
		rv.setSourceOfCertification("C");
		rv.setUserSource("C");
		Map<String , String> map = new HashMap<>();
		map = accountService.repairDriverLicense(rv);
		System.out.println(map);
	} 
	
	
	/**
	 * 驾驶证换证
	 */
	@Test
	public void testReplaceDriverLicense(){
		//http://192.168.1.245:8080/web/user/replaceDriverLicense.html?identificationNO=A&IDcard=445222199209020034&name=张宇帆&mobilephone=15920050177&IDCardPhoto1=111&IDCardPhoto2=222&photoReturnNumberString=111&foreignersLiveTable=111&placeOfDomicile=深圳&receiverName=111&receiverNumber=15920050177&mailingAddress=深圳市宝安区&livePhoto1=111&livePhoto2=222&loginUser=445222199209020034&sourceOfCertification=C&userSource=C                    
		RepairOrReplaceDriverLicenseVo rv = new RepairOrReplaceDriverLicenseVo();
		rv.setBusinessType("H");
		rv.setForeignersLiveTable("111");
		rv.setIDcard("445222199209020034");
		rv.setIDCardPhoto1("111");
		rv.setIDCardPhoto2("222");
		rv.setIdentificationNO("A");
		rv.setIp("11");
		rv.setLivePhoto1("111");
		rv.setLivePhoto2("222");
		rv.setLoginUser("445222199209020034");
		rv.setMailingAddress("深圳市宝安区");
		rv.setName("张宇帆");
		rv.setPhotoReturnNumberString("123456");
		rv.setPlaceOfDomicile("深圳");
		rv.setReceiverName("333");
		rv.setReceiverNumber("15920050177");
		rv.setSourceOfCertification("C");
		rv.setUserSource("C");
		Map<String , String> map = new HashMap<>();
		map = accountService.replaceDriverLicense(rv);
		System.out.println(map);
	} 
	
	
	/**
	 * 驾驶证自愿降级
	 */
	@Test
	public void testDriverLicenseVoluntaryDemotion(){
		//http://192.168.1.245:8080/web/user/driverLicenseVoluntaryDemotion.html?identificationNO=A&loginUser=445222199209020034&IDcard=445222199209020034&driverLicense=445222199209020034&name=张宇帆&photoReturnNumberString=11111&placeOfDomicile=深圳&receiverName=张宇帆&receiverNumber=15920050177&mailingAddress=深圳市宝安区&sourceOfCertification=C&userSource=C&IDCardPhoto1=111&IDCardPhoto2=222&driverLicensePhoto=111      
		DriverLicenseVoluntaryDemotionVo dv = new DriverLicenseVoluntaryDemotionVo();
		dv.setBusinessType("J");
		dv.setDriverLicensePhoto("111");
		dv.setIDcard("445222199209020034");
		dv.setDriverLicense("445222199209020034");
		dv.setIDCardPhoto1("111");
		dv.setIDCardPhoto2("222");
		dv.setIdentificationNO("A");
		dv.setIp("11");
		dv.setLoginUser("445222199209020034");
		dv.setMailingAddress("深圳市宝安区");
		dv.setName("张宇帆");
		dv.setPhotoReturnNumberString("111");
		dv.setReceiverName("张宇帆");
		dv.setReceiverNumber("15920050177");
		dv.setSourceOfCertification("C");
		dv.setUserSource("C");
		dv.setPlaceOfDomicile("1");
		Map<String, String > map = new HashMap<>();
		map = accountService.driverLicenseVoluntaryDemotion(dv);
		System.out.println(map);
	}
	
	
	
	/**
	 * 驾驶证转入
	 */
	@Test
	public void testDriverLicenseInto(){
		//http://192.168.1.245:8080/web/user/driverLicenseInto.html?name=张宇帆&identificationNO=A&IDcard=445222199209020034&driverLicense=445222199209020034&fileNumber=123456&issuingLicenceAuthority=藏A:拉萨市公安局&photoReturnNumberString=111&receiverName=张宇帆&receiverNumber=15920050177&mailingAddress=深圳市宝安区&sourceOfCertification=C&loginUser=445222199209020034&IDCardPhoto1=111&IDCardPhoto2=222&driverLicensePhoto=111&bodyConditionForm=222    
		DriverLicenseIntoVo dv = new DriverLicenseIntoVo();
		dv.setBodyConditionForm("111");
		dv.setBusinessType("Z");
		dv.setDriverLicensePhoto("111");
		dv.setFileNumber("440304166612");
		dv.setIDcard("445222199209020034");
		dv.setDriverLicense("445222199209020034");
		dv.setIDCardPhoto1("111");
		dv.setIDCardPhoto2("222");
		dv.setIdentificationNO("A");
		dv.setIp("11");
		dv.setIssuingLicenceAuthority("藏A:拉萨市公安局");
		dv.setLoginUser("445222199209020034");
		dv.setMailingAddress("深圳市宝安区");
		dv.setName("张宇帆");
		dv.setPhotoReturnNumberString("111");
		dv.setReceiverName("张宇帆");
		dv.setReceiverNumber("15920050177");
		dv.setSourceOfCertification("C");
		Map<String, String > map = new HashMap<>();
		map = accountService.driverLicenseInto(dv);
		System.out.println(map);
	}
	
	
	/**
	 * 驾驶证延期换证
	 */
	@Test
	public void testRenewalDriverLicense(){
		//http://192.168.1.245:8080/web/user/renewalDriverLicense.html?name=张宇帆&identificationNO=A&IDcard=445222199209020034&driverLicense=445222199209020034&fileNumber=123456&delayDate=20170701&delayReason=gg&sourceOfCertification=C&loginUser=445222199209020034&IDCardPhoto1=111&IDCardPhoto2=222&driverLicensePhoto=111&delayPhoto=111&receiverName=张宇帆&receiverNumber=15920050177&mailingAddress=深圳市宝安区       
		RenewalDriverLicenseVo rd = new RenewalDriverLicenseVo();
		rd.setBusinessType("Y");
		rd.setDelayDate("20170712");
		rd.setDelayPhoto("111");
		rd.setDelayReason("服兵役");
		rd.setDriverLicensePhoto("111");
		rd.setFileNumber("111");
		rd.setIDcard("622822198502074110");
		rd.setDriverLicense("622822198502074110");
		rd.setIDCardPhoto1("111");
		rd.setIDCardPhoto2("222");
		rd.setIdentificationNO("A");
		rd.setIp("11");
		rd.setLoginUser("622822198502074110");
		rd.setName("王玉璞");
		rd.setSourceOfCertification("C");
		Map<String, String > map = new HashMap<>();
		map = accountService.renewalDriverLicense(rd);
		System.out.println(map);
	}
	
	
	/**
	 * 驾驶证年审
	 */
	@Test
	public void testDriverLicenseAnnualVerification(){
		//http://192.168.1.245:8080/web/user/driverLicenseAnnualVerification.html?identificationNO=A&name=张宇帆&IDcard=445222199209020034&mobilephone=15920050177&placeOfDomicile=深圳&receiverName=11&receiverNumber=15920050177&mailingAddress=深圳市宝安区&IDCardPhoto1=111&IDCardPhoto2=222&livePhoto1=111&livePhoto2=222 &educationDrawingtable=111&foreignersLiveTable=222&postalcode=1&loginUser=445222199209020034&sourceOfCertification=C&userSource=C
		DriverLicenseAnnualVerificationVo dv = new DriverLicenseAnnualVerificationVo();
		dv.setBusinessType("N");
		dv.setEducationDrawingtable("111");
		dv.setForeignersLiveTable("22");
		dv.setIDcard("445222199209020034");
		dv.setIDCardPhoto1("111");
		dv.setIDCardPhoto2("222");
		dv.setIdentificationNO("A");
		dv.setIp("192.168.1.243");
		dv.setLivePhoto1("");
		dv.setLivePhoto2("");
		dv.setLoginUser("445222199209020034");
		dv.setMailingAddress("深圳市宝安区");
		dv.setMobilephone("15920050177");
		dv.setName("张宇帆");
		dv.setPlaceOfDomicile("深圳");
		dv.setPostalcode("1");
		dv.setReceiverName("111");
		dv.setReceiverNumber("222");
		dv.setSourceOfCertification("C");
		dv.setUserSource("C");
		
		Map<String, String > map = new HashMap<>();
		map = accountService.driverLicenseAnnualVerification(dv);
		System.out.println(map);
	}
	
	
	/**
	 * 查询机动车信息单
	 */
	@Test
	public void testQueryScheduleOfMotorVehicleInformationList(){
		String applyType = "2";
		String identityCard = "445222199209020034";
		String sourceOfCertification = "C";
		Map<String, Object> map = new HashMap<>();
		map = accountService.queryScheduleOfMotorVehicleInformationList(applyType, identityCard, sourceOfCertification);
		System.out.println(map);
	}
	
	/**
	 * 查询驾驶人信息单
	 */
	@Test
	public void testQueryScheduleOfDriverInformationList(){
		String applyType = "1";
		String identityCard = "445222199209020034";
		String sourceOfCertification = "C";
		Map<String, Object> map = new HashMap<>();
		map = accountService.queryScheduleOfDriverInformationList(applyType, identityCard, sourceOfCertification);
		System.out.println(map);
	}
	/**
	 * 申请驾驶人信息单
	 */
	@Test
	public void testSubmitApplicationForDriverInformation(){
		String applyType = "1";
		String userName = "张宇帆";
		String identityCard = "4452221992090";
		String mobilephone = "15920050177";
		String sourceOfCertification = "C";
		Map<String, String> map = new HashMap<>();
		map = accountService.submitApplicationForDriverInformation(applyType, userName, identityCard, mobilephone, sourceOfCertification);
		System.out.println(map);
	}
	
	
	/**
	 * 申请机动车信息单
	 */
	@Test
	public void testSubmitApplicationForMotorVehicleInformation(){
		String applyType = "1";
		String userName = "张宇帆";
		String identityCard = "445222199209020034";
		String mobilephone = "15920050177";
		String sourceOfCertification = "C";
		String licensePlateNumber = "粤B6A42E";
		String plateType = "02";
		Map<String, String> map = new HashMap<>();
		map = accountService.submitApplicationForMotorVehicleInformation(applyType, userName, identityCard, mobilephone,licensePlateNumber,plateType, sourceOfCertification);
		System.out.println(map);
	}
	
	/**
	 * 绑定驾驶证结果查询
	 */
	@Test
	public void  testqueryResultOfBindDriverLicense(){
		String identityCard = "420881198302280017";
		String userSource = "C";
		ResultOfBIndDriverLicenseVo resultOfBIndDriverLicenseVo = null;
		resultOfBIndDriverLicenseVo = accountService.queryResultOfBindDriverLicense(identityCard, userSource);
		System.out.println(resultOfBIndDriverLicenseVo.getJSZHM());
	}
	
	/**
	 * 绑定驾驶证
	 */
	@Test
	public void testBindDriverLicense(){
		
		BindDriverLicenseVo bindDriverLicenseVo = new BindDriverLicenseVo();
		bindDriverLicenseVo.setLoginName("42138119910422133X");
		bindDriverLicenseVo.setUserSource("C");
		bindDriverLicenseVo.setIdentityCard("42138119910422133X");
		bindDriverLicenseVo.setDriverLicenseIssuedAddress("3");
		bindDriverLicenseVo.setSourceOfCertification("C");
		bindDriverLicenseVo.setName("吴帆");
		JSONObject json = accountService.bindDriverLicense(bindDriverLicenseVo);
		System.out.println(json);
	}
	/**
	 * 隨手拍举报
	 */
	@Test
	public void testReadilyShoot(){
		String reportSerialNumber = "W20170522881675";
		String password = "090551";
		try {
			ResultOfReadilyShoot shoot = accountService.queryResultOfReadilyShoot(reportSerialNumber, password);
			System.out.println(shoot.getStatus());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	/**
	 * 随手拍插入数据库
	 */
	@Test
	public void testSaveReadilyShoot(){
		ReadilyShoot readilyShoot = new ReadilyShoot();
		readilyShoot.setAddDate(new Date());
		readilyShoot.setIllegalTime(new Date());
		readilyShoot.setIllegalSections("bb");
		readilyShoot.setSituationStatement("aaa");
		readilyShoot.setReportSerialNumber("W20170522881676");
		readilyShoot.setPassword("090551");
		readilyShoot.setIllegalImg1("111");
		readilyShoot.setIllegalImg2("222");
		readilyShoot.setIllegalImg3("333");
		 
		try {
			 System.out.print(accountService.saveReadilyShoot(readilyShoot));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testxxxxx() {
//		DynamicHttpclientCall dynamicHttpclientCall = new DynamicHttpclientCall("http://szjjapi.chudaokeji.com/yywfcl/services/yywfcl?WSDL",
//				"GetAPACShippingPackage", "http://szjjapi.chudaokeji.com/yywfcl/services/yywfcl?WSDL");
//
//		Map<String, String> patameterMap = new HashMap<String, String>();
//
//		patameterMap.put("TrackCode", "123");
//		patameterMap.put("Version", "123");
//		patameterMap.put("APIDevUserID", "123");
//		patameterMap.put("APIPassword", "123");
//		patameterMap.put("APISellerUserID", "123");
//		patameterMap.put("MessageID", "123");
//		patameterMap.put("TrackCode", "123");
//
//		String soapRequestData = dynamicHttpclientCall.buildRequestData(patameterMap);
//		System.out.println(soapRequestData);
//
//		int statusCode;
//		try {
//			statusCode = dynamicHttpclientCall.invoke(patameterMap);
//			if (statusCode == 200) {
//				System.out.println("调用成功！");
//				System.out.println(dynamicHttpclientCall.soapResponseData);
//			} else {
//				System.out.println("调用失败！错误码：" + statusCode);
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}


	@Test
	public void testInsertWechatInfo() {
		WechatUserInfoBean bean = new WechatUserInfoBean();
		bean.setOpenId("test");
		bean.setNickname("testssdsd");
		bean.setHeadImgUrl("test");
		bean.setUpdateTime(new Date());
		int id = accountService.insertWechatUserInfo(bean);
		Assert.assertTrue(id > 0);
	}

	/**
	 * 测试getWechatUserInfoById
	 */
	@Test
	public void testGetWechatUserInfoById() {
		WechatUserInfoBean result = null;
		result = accountService.getWechatUserInfoById(1);
		Assert.assertTrue(result != null);
	}

	/**
	 * 
	 */
	@Test
	public void testAllWechatUserInfoBeanList() {
		String mobilephone = "13888888888";
		accountService.sendSMSVerificatioCode(mobilephone, "123456");
	}
	

	// // 获取用户信息
	// @Test
	// public void testGetUserinfo() {
	//
	// UserRegInfo userRegInfo = new UserRegInfo();
	// userRegInfo.setMobilePhone("13177813777");
	// userRegInfo.setGrade(0);
	// userRegInfo.setGrowth(0);
	// userRegInfo.setStatus(0);
	// userRegInfo.setPassword("");
	// userRegInfo.setImg("");
	// userRegInfo.setCreatOn(System.currentTimeMillis() / 1000);
	// userRegInfo.setCouponMoney(0);
	// userRegInfo.setArea("222");
	// UserRegInfo info = accountService.addNewUser(userRegInfo);
	// Assert.assertNotNull(info);
	// /*
	// * assertEquals(userInfo.containsKey("user_id"), true);
	// * assertEquals(userInfo.get("user_id"), "100001");
	// */
	//
	// }
	//
	// @Test
	// public void testsynchronized() throws InterruptedException {
	// long mobile = 13977822901L;
	// long time1 = System.currentTimeMillis();
	// for (int i = 0; i < 5; i++) {
	// mobile++;
	// Thread demo1 = new Thread(testGetUserinfo2(String.valueOf(mobile)));
	// demo1.start();
	// }
	// long time2 = System.currentTimeMillis();
	// System.out.println(time2 - time1);
	// }
	//
	// public Runnable testGetUserinfo2(String mobile) throws
	// InterruptedException {
	// UserRegInfo userRegInfo = new UserRegInfo();
	// userRegInfo.setMobilePhone(mobile);
	// userRegInfo.setGrade(0);
	// userRegInfo.setGrowth(0);
	// userRegInfo.setStatus(0);
	// userRegInfo.setPassword("");
	// userRegInfo.setImg("");
	// userRegInfo.setCreatOn(System.currentTimeMillis() / 1000);
	// userRegInfo.setCouponMoney(0);
	// userRegInfo.setArea("222");
	// UserRegInfo info = accountService.addNewUser(userRegInfo);
	// Thread.sleep(2000);
	// return null;
	// }
	//
	// @Test
	// public void testAccessToken() throws Exception {
	// long userId = 1;
	// String xxString = DESUtils.encrypt("1280797", "authorlsptime20141225");
	// System.out.println(xxString);
	// Token token = accountService.getAccessToken(userId);
	// String accessToken = token.getAccessToken();
	//// String accessToken = "fdb853e911899cf3d3dd2c22e2543edb";
	// System.out.println(accessToken);
	// String refreshToken = token.getRefreshToken();
	// System.out.println("refreshToken = "+refreshToken);
	//// String realToken = accountService.getAccessTokenByUserId(userId);
	//// System.out.println(realToken);
	// //Q1LDfgB2YXV65PU4uuxelF5CQTBq6rfy2YWcuWRQMKluQgmSgOVxvbPyFWCWBY6MfbEOWupR8kLf====
	// String jiami = AESNewUtils.encrypt(accessToken,accessToken);
	// System.out.println(accessToken);
	// System.out.println(jiami);
	// String jiemi =
	// AESNewUtils.decrypt("xvkgkqN8f5ZvlawTKvuV0Zp47F0Htj4huzBekaCVkKlGP9SCH1tgBXy/cz1s6I",
	// "1d704567d7eccac648f332258ce000e3");
	//
	// System.out.println(jiemi);
	//// System.out.println(refreshToken);
	//// System.out.println( accountService.isAccessTokenValidate(accessToken,
	// userId+""));
	//// String newToken =
	// accountService.getAccessTokenByRefreshToken(userId+"",
	// refreshToken).get("accessToken");
	//// System.out.println(newToken);
	// }
	//
	//
	// @Test
	// public void testAddBindOpenid() {
	// UserOpenidBean userOpenidBean = new UserOpenidBean();
	// userOpenidBean.setOpenid("qqqqqq");
	// userOpenidBean.setUserId(121223);
	// userOpenidBean.setBindTime(new Date().getTime()/1000);
	// userOpenidBean.setStatus(1);
	// long flag = accountService.addBindOpenid(userOpenidBean);
	// Assert.assertEquals(flag, 1);
	// }
	//
	// @Test
	// public void testCancelBindOpenid() {
	// UserOpenidBean userOpenidBean = new UserOpenidBean();
	// userOpenidBean.setUserId(121223);
	// userOpenidBean.setUnBindTime(new Date().getTime()/1000);
	// userOpenidBean.setStatus(2);
	// long flag = accountService.cancelBindOpenid(userOpenidBean);
	// Assert.assertEquals(flag, 1);
	// }
	//
	//
	// @Test
	// public void testGetUserIdByOpenid() {
	//// Token token = accountService.getAccessToken(1);
	//// String urlString =
	// "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=wxd4c590c3a2dad288&secret=ad5d9e1fd3161923ba4078885a75a6cd";
	//// final String jsonResult = HttpClientUtil.get(urlString);
	//// final JSONObject jsonObject = JSON.parseObject(jsonResult);
	//// String accessT =
	// ObjectUtils.defaultIfNull(jsonObject.get("access_token"),
	// StringUtils.EMPTY).toString();
	//// System.out.println(accessT);
	//// String url =
	// "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+accessT;
	//// System.out.println(url);
	//// String data = "{" + "\"touser\":" + "\"" +
	// "oAj5WtyhEvr0FilsuXw6WiUt2QQw" + "\"" + ","
	//// + "\"template_id\":" + "\"" +
	// "oi6UX8x6nHH7ndXrtoqIVs7VFI6GO7MX6EvpyML0I9M" + "\"" + ","
	//// + "\"url\":" + "\"" + "http://www.choumei.cn/" + "\"" + ","
	//// + "\"topcolor\":" + "\"" + "#FF0000" + "\"" + ","
	//// + "\"data\":" + "{"
	//// + "\"first\":" + "{\"" + "value\":" + "\"" +
	// "现金券已到账！美发不办卡，全城800家品牌店，去哪都是VIP！，使用臭美美发App，享超低折扣价！" + "\"" + "," + "\"" +
	// "color\":" + "\"" + "#173177" + "\"" + "},"
	//// + "\"keyword1\":" + "{\"" + "value\":" + "\"" + "你成功领取了臭美现金券" + "\"" +
	// "," + "\"" + "color\":" + "\"" + "#173177" + "\"" + "},"
	//// + "\"keyword2\":" + "{\"" + "value\":" + "\"" + "10元" + "\"" + "," +
	// "\"" + "color\":" + "\"" + "#173177" + "\"" + "},"
	//// + "\"remark\":" + "{\"" + "value\":" + "\"" +
	// "现金券可直接抵用，消费评价后还可以拿更多红包哦！" + "\"" + "," + "\"" + "color\":" + "\"" +
	// "#173177" + "\"" + "}}}";
	//// String postRet = HttpClientUtil.post(url, data);
	//// final JSONObject retObject = JSON.parseObject(postRet);
	//// String errcode = ObjectUtils.defaultIfNull(retObject.get("errcode"),
	// StringUtils.EMPTY).toString();
	//// if ("0".equals(errcode)) {
	//// System.out.println("true");
	//// }
	//// System.out.println(errcode);
	//// System.out.println(retObject.toJSONString());
	//// postGetLaiseeMsgToUser("现金券已到账！美发不办卡，全城800家品牌店，去哪都是VIP！，使用臭美美发App，享超低折扣价！",
	// "你成功领取了臭美现金券",
	//// 10.1 + "元", "现金券可直接抵用，消费评价后还可以拿更多红包哦！", "oAj5WtyhEvr0FilsuXw6WiUt2QQw",
	// "http://www.choumei.cn/");
	// postGetLaiseeMsgToPostUser("小葡萄领走了您分享的去哪儿红包", "领取金额：1元", "领取时间：2014-10-22
	// 14:00", "为避免您被打扰，只向您推送前3条的领取记录", "oAj5WtyhEvr0FilsuXw6WiUt2QQw",
	// "http://www.choumei.cn/");
	//
	// }
	//
	// private boolean postGetLaiseeMsgToUser(String first, String keyword1,
	// String keyword2, String remark, String openId, String chouMeiUrl) {
	// boolean ret = false;
	// String urlString =
	// "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
	// + "wxd4c590c3a2dad288" + "&secret=" + "ad5d9e1fd3161923ba4078885a75a6cd";
	// final String jsonResult = HttpClientUtil.get(urlString);
	// final JSONObject jsonObject = JSON.parseObject(jsonResult);
	// String accessT =
	// ObjectUtils.defaultIfNull(jsonObject.get("access_token"),
	// StringUtils.EMPTY).toString();
	// String url =
	// "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+accessT;
	// String data = "{" + "\"touser\":" + "\"" + openId + "\"" + ","
	// + "\"template_id\":" + "\"" +
	// "oi6UX8x6nHH7ndXrtoqIVs7VFI6GO7MX6EvpyML0I9M" + "\"" + ","
	// + "\"url\":" + "\"" + chouMeiUrl + "\"" + ","
	// + "\"topcolor\":" + "\"" + "#FF0000" + "\"" + ","
	// + "\"data\":" + "{"
	// + "\"first\":" + "{\"" + "value\":" + "\"" + first + "\"" + "," + "\"" +
	// "color\":" + "\"" + "#173177" + "\"" + "},"
	// + "\"keyword1\":" + "{\"" + "value\":" + "\"" + keyword1 + "\"" + "," +
	// "\"" + "color\":" + "\"" + "#173177" + "\"" + "},"
	// + "\"keyword2\":" + "{\"" + "value\":" + "\"" + keyword2 + "\"" + "," +
	// "\"" + "color\":" + "\"" + "#173177" + "\"" + "},"
	// + "\"remark\":" + "{\"" + "value\":" + "\"" + remark + "\"" + "," + "\""
	// + "color\":" + "\"" + "#173177" + "\"" + "}}}";
	// String postRet = HttpClientUtil.post(url, data);
	// final JSONObject retObject = JSON.parseObject(postRet);
	// String errcode = ObjectUtils.defaultIfNull(retObject.get("errcode"),
	// StringUtils.EMPTY).toString();
	// if ("0".equals(errcode)) {
	// ret = true;
	// }
	// return ret;
	// }
	//
	//
	// private boolean postGetLaiseeMsgToPostUser(String first, String keyword1,
	// String keyword2, String remark, String openId, String chouMeiUrl) {
	// boolean ret = false;
	// String urlString =
	// "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="
	// + "wxd4c590c3a2dad288" + "&secret=" + "ad5d9e1fd3161923ba4078885a75a6cd";
	// final String jsonResult = HttpClientUtil.get(urlString);
	// final JSONObject jsonObject = JSON.parseObject(jsonResult);
	// String accessT =
	// ObjectUtils.defaultIfNull(jsonObject.get("access_token"),
	// StringUtils.EMPTY).toString();
	// String url =
	// "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token="+accessT;
	// String data = "{" + "\"touser\":" + "\"" + openId + "\"" + ","
	// + "\"template_id\":" + "\"" +
	// "v9TJDXoNqpSqac-7DZdvvOU4fB2e8glrdPhB6KLTiuE" + "\"" + ","
	// + "\"url\":" + "\"" + chouMeiUrl + "\"" + ","
	// + "\"topcolor\":" + "\"" + "#FF0000" + "\"" + ","
	// + "\"data\":" + "{"
	// + "\"first\":" + "{\"" + "value\":" + "\"" + first + "\"" + "," + "\"" +
	// "color\":" + "\"" + "#173177" + "\"" + "},"
	// + "\"keyword1\":" + "{\"" + "value\":" + "\"" + keyword1 + "\"" + "," +
	// "\"" + "color\":" + "\"" + "#173177" + "\"" + "},"
	// + "\"keyword2\":" + "{\"" + "value\":" + "\"" + keyword2 + "\"" + "," +
	// "\"" + "color\":" + "\"" + "#173177" + "\"" + "},"
	// + "\"remark\":" + "{\"" + "value\":" + "\"" + remark + "\"" + "," + "\""
	// + "color\":" + "\"" + "#173177" + "\"" + "}}}";
	// String postRet = HttpClientUtil.post(url, data);
	// final JSONObject retObject = JSON.parseObject(postRet);
	// String errcode = ObjectUtils.defaultIfNull(retObject.get("errcode"),
	// StringUtils.EMPTY).toString();
	// if ("0".equals(errcode)) {
	// ret = true;
	// }
	// return ret;
	// }
	//
	// @Test
	// public void testGetOpenidByUserId() {
	// String openid = accountService.getOpenidByUserId(878669);
	// //String openid = accountService.getOpenidByUserId(0);
	// //String openid = accountService.getOpenidByUserId(-1);
	// System.out.println(openid);
	// Assert.assertNotNull(openid);
	// }
	//
	//
	// @Test
	// public void testGetDevice() {
	// DeviceBean deviceBean =
	// accountService.getDevice("w345wrgqery42562346arqgf", 1);
	// System.out.println(deviceBean.getUserId());
	// Assert.assertNotNull(deviceBean);
	// }
	//
	//
	// @Test
	// public void testaddDevice() {
	// //accountService.addDevice("r124wrerjng42562346advdx", -1, 456321);
	// accountService.addDevice("r124wrerjng42562346advdx", 2, 456321);
	// }
	//
	// @Test
	// public void testUpdateDevice() {
	// boolean flag = accountService.updateDevice("r124wrerjng42562346advdx", 2,
	// 789987);
	// System.out.println(flag);
	// Assert.assertTrue(flag);
	// }

}
