package cn.account.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.DocumentHelper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;

import cn.account.bean.ReadilyShoot;
import cn.account.bean.ResultOfReadilyShoot;
import cn.account.bean.WechatUserInfoBean;
import cn.account.bean.vo.BindCarVo;
import cn.account.bean.vo.BindDriverLicenseVo;
import cn.account.bean.vo.BindTheVehicleVo;
import cn.account.bean.vo.LoginReturnBeanVo;
import cn.account.bean.vo.MyDriverLicenseVo;
import cn.account.bean.vo.ResultOfBIndDriverLicenseVo;
import cn.account.bean.vo.UnbindTheOtherDriverUseMyCarVo;
import cn.account.bean.vo.UnbindVehicleVo;
import cn.account.bean.vo.UserBasicVo;
import cn.sdk.webservice.Xml2Json;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:junit-test.xml" })
public class TestAccountService {

	@Autowired
	@Qualifier("accountService")
	private IAccountService accountService;
	
	@Test
	public void bindsTheMotorVehicleQuery() throws Exception{
		accountService.getBndTheVehicles("44022319900518184X", "13632744667", "Z");
	}
	/**
	 * 车主解绑车辆其他驾驶人
	 * @throws Exception 
	 */
	@Test
	public void loginTest() throws Exception{
		accountService.login("13502899383", "189981", "C", "==", "C");
	}
	/**
	 * 车主解绑车辆其他驾驶人
	 * @throws Exception 
	 */
	@Test
	public void testUnbindTheOtherDriverUseMyCar() throws Exception{
		UnbindTheOtherDriverUseMyCarVo uv = new UnbindTheOtherDriverUseMyCarVo();
		uv.setIDcard("445222199209020034");
		uv.setLoginUser("445222199209020034");
		uv.setNumberPlateNumber("B6A42E");
		uv.setSourceOfCertification("C");
		uv.setUserSource("C");
		uv.setPlateType("02");
		Map<String, String> map = accountService.unbindTheOtherDriverUseMyCar(uv);
		System.out.println(map);
	}
	
	/**
	 * 路况查询
	 * @throws Exception
	 */
	@Test
	public void testTrafficQuery() throws Exception{
		String sourceOfCertification = "C";
		accountService.trafficQuery(sourceOfCertification);
	}
	
	/**
	 * 单条路况查询
	 * @throws Exception
	 */
	@Test
	public void testDetailsTrafficQuery() throws Exception{
		String zjz = "537468";
		String sourceOfCertification = "C";
		accountService.detailsTrafficQuery(zjz, sourceOfCertification);
	}
	/**
	 * 车主查询车辆其他驾驶人
	 * @throws Exception
	 */
	@Test
	public void testGetBindTheOtherDriversUseMyCar() throws Exception{
		String IDcard = "445222199209020034";
		String plateType = "02";
		String sourceOfCertification = "C";
		String numberPlateNumber = "粤B6A42E";
		Map<String, Object> map = accountService.getBindTheOtherDriversUseMyCar(IDcard, numberPlateNumber, plateType, sourceOfCertification);
		System.out.println(map);
	}
	
	/**
	 * 查询已绑定车辆
	 * @throws Exception
	 */
	@Test
	public void testgetBndTheVehicles() throws Exception{
		String IDcard = "445222197912152216";
		String sourceOfCertification = "C";
		String mobilephone = "13502899383";
		List<BindTheVehicleVo> map = accountService.getBndTheVehicles(IDcard, mobilephone, sourceOfCertification);
		System.out.println(map);
	}
	
	/**
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testXml() throws Exception{
		JSONObject json2 = new JSONObject();
		String respJson = "<?xml version=\"1.0\" encoding=\"utf-8\"?><return><code>0000</code><msg><response><code>0000</code><msg>查询成功</msg><body><ret><id>537247</id><road_name>北环大道</road_name><section_name>侨香段</section_name><start_unit_name>北环广深立交西</start_unit_name><end_unit_name>铜鼓人行天桥</end_unit_name><event_level>车多</event_level><event_reason>车流量过大</event_reason><summary>北环大道 在北环广深立交西 西向东方向上，由于车流量过大造成从北环广深立交西到北环大道铜鼓人行天桥车多</summary><modify_date>2017-06-19 18:05:46.0</modify_date><start_date>2017-06-19 12:00:05.0</start_date><pic>null</pic><gps_x>0</gps_x><gps_y>0</gps_y></ret></body></response></msg></return>";
		org.dom4j.Document doc1 = DocumentHelper.parseText(respJson);
		Xml2Json.dom4j2Json(doc1.getRootElement(),json2);
		System.out.println(json2);
	}
	
	/**
	 *修改手机号
	 * @throws Exception 
	 *420881198302280017
	 */
	@Test
	public void testupdateM() throws Exception{
		UserBasicVo userBasicVo = new UserBasicVo();
		userBasicVo.setIdentityCard("");
		userBasicVo.setOldMobile("13652311206");
		userBasicVo.setNewMobile("18601174358");
		userBasicVo.setUserSource("C");
		accountService.updateMobile(userBasicVo);
	}
	/**
	 * 我的驾驶证
	 * @throws Exception 
	 */
	@Test
	public void testMyDriverLicense() throws Exception{
		String IDcard = "71272119880226103";
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
	 * @throws Exception 
	 * 
	 */
	@Test
	public void testUnbindVehicle() throws Exception{
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
	 * 查询机动车信息单
	 * @throws Exception 
	 */
	@Test
	public void testQueryScheduleOfMotorVehicleInformationList() throws Exception{
		String applyType = "2";
		String identityCard = "445222199209020034";
		String sourceOfCertification = "C";
		Map<String, Object> map = new HashMap<>();
		map = accountService.queryScheduleOfMotorVehicleInformationList(applyType, identityCard, sourceOfCertification);
		System.out.println(map);
	}
	
	/**
	 * 查询驾驶人信息单
	 * @throws Exception 
	 */
	@Test
	public void testQueryScheduleOfDriverInformationList() throws Exception{
		String applyType = "1";
		String identityCard = "445222199209020034";
		String sourceOfCertification = "C";
		Map<String, Object> map = new HashMap<>();
		map = accountService.queryScheduleOfDriverInformationList(applyType, identityCard, sourceOfCertification);
		System.out.println(map);
	}
	/**
	 * 申请驾驶人信息单
	 * @throws Exception 
	 */
	@Test
	public void testSubmitApplicationForDriverInformation() throws Exception{
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
	 * @throws Exception 
	 */
	@Test
	public void testSubmitApplicationForMotorVehicleInformation() throws Exception{
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
	 * @throws Exception 
	 */
	@Test
	public void  testqueryResultOfBindDriverLicense() throws Exception{
		String identityCard = "42138119910422133X";
		String userSource = "C";
		ResultOfBIndDriverLicenseVo resultOfBIndDriverLicenseVo = null;
		resultOfBIndDriverLicenseVo = accountService.queryResultOfBindDriverLicense(identityCard, userSource);
		System.out.println(resultOfBIndDriverLicenseVo.getJSZHM());
	}
	
	/**
	 * 绑定驾驶证
	 * @throws Exception 
	 */
	@Test
	public void testBindDriverLicense() throws Exception{
		
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
	public void testAlipayLogin() {
		try {
			LoginReturnBeanVo loginReturnBeanVo = accountService.alipayLogin("13902930139", "C", "cccc");
			System.out.println(loginReturnBeanVo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 测试getWechatUserInfoById
	 */
	@Test
	public void testGetElectronicPolicy() {
		try {
			Map<String, Object> map = accountService.getElectronicPolicy("440301196501254159", "13902930139", "粤B11L13", "02", "C");
			System.out.println(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
