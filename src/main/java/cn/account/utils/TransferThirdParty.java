package cn.account.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils.Null;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.account.bean.vo.AuthenticationBasicInformationVo;
import cn.account.bean.vo.BindTheVehicleVo;
import cn.account.bean.vo.DriverLicenseToSupplementThePermitBusinessVo;
import cn.account.bean.vo.DrivingLicenseVo;
import cn.account.bean.vo.ElectronicDriverLicenseVo;
import cn.account.bean.vo.IdentificationOfAuditResultsVo;
import cn.account.bean.vo.InformationSheetVo;
import cn.account.bean.vo.MotorVehicleBusiness;
import cn.account.bean.vo.MyDriverLicenseVo;
import cn.account.bean.vo.ZT_STATUS;
import cn.sdk.util.DateUtil;
import cn.sdk.webservice.WebServiceClient;
/**
 * 调用第三方封装
 * @author Mbenben
 *
 */
@SuppressWarnings(value="all")
public class TransferThirdParty {
	/**
	 * 重置密码
	 * @param idCard 身份证
	 * @param userName 用户名
	 * @param mobile 手机号
	 * @param sourceOfCertification 认证来源
	 * @param url 请求url
	 * @param method 请求方法
	 * @param userId 用户id
	 * @param userPwd 用户密码
	 * @param key 秘钥
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> resetPwd(String idCard,String userName,String mobile,String sourceOfCertification,String url,String method,String userId,String userPwd,String key) throws Exception{
		String xxcj03 = "xxcj09";
		String xxcj03ReqXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><REQUEST><SFZMHM>"+idCard+"</SFZMHM><XM>"+userName+"</XM><SJHM>"+mobile+"</SJHM><YHLY>"+sourceOfCertification+"</YHLY></REQUEST>";
		JSONObject xxcj03RespJson = WebServiceClient.getInstance().requestWebService(url,method,xxcj03,xxcj03ReqXml,userId,userPwd,key);
		
		//返回的数据
        String xxcj03Msg =(String) xxcj03RespJson.get("MSG");
        //返回的状态码
        String xxcj03Code = (String) xxcj03RespJson.get("CODE");
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", xxcj03Code);
        map.put("msg", xxcj03Msg);
		return map;
	}
	/**
	 * 登录接口 xxcj03
	 * @param loginName 登录账号
	 * @param password 登录密码
	 * @param url 请求url
	 * @param method 请求方法
	 * @param userId 用户id
	 * @param userPwd 用户密码
	 * @param key 秘钥
	 * @return 认证来源
	 * @throws Exception
	 */
	public static Map<String, String> login(String loginName,String password,String url,String method,String userId,String userPwd,String key,String sourceOfCertification) throws Exception{
		String xxcj03 = "xxcj03";
		//String xxcj03ReqXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><REQUEST><USERNAME>"+loginName+"</USERNAME><PWD>"+password+"</PWD><YHLY>WX_XCX</YHLY><SFZMHM></SFZMHM><XM></XM></REQUEST>";
		String xxcj03ReqXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><REQUEST><USERNAME>"+loginName+"</USERNAME><PWD>"+password+"</PWD><YHLY>"+sourceOfCertification+"</YHLY><SFZMHM></SFZMHM><XM></XM></REQUEST>";
		JSONObject xxcj03RespJson = WebServiceClient.getInstance().requestWebService(url,method,xxcj03,xxcj03ReqXml,userId,userPwd,key);
		
		//返回的数据
        String xxcj03Msg =(String) xxcj03RespJson.get("MSG");
        //返回的状态码
        String xxcj03Code = (String) xxcj03RespJson.get("CODE");
        Map<String, String> map = new HashMap<String, String>();
        map.put("code", xxcj03Code);
        map.put("msg", xxcj03Msg);
		return map;
	}
	/**
	 * 认证基本信息查询接口
	 * @param loginName 登录账号
	 * @param sourceOfCertification 认证来源 现在只有微信C
	 * @param url 请求url
	 * @param method 请求方法
	 * @param userId 用户id
	 * @param userPwd 用户密码
	 * @param key 秘钥
	 * @return AuthenticationBasicInformationVo
	 * @throws Exception
	 */
	public static AuthenticationBasicInformationVo authenticationBasicInformationQuery(String loginName,String sourceOfCertification,String url,String method,String userId,String userPwd,String key) throws Exception{
		String xxcj08 = "xxcj08";
		String xxcj08ReqXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><REQUEST><USERNAME>"+loginName+"</USERNAME><RZLY>"+sourceOfCertification+"</RZLY></REQUEST>";
		JSONObject xxcj08RespJson = WebServiceClient.getInstance().requestWebService(url, method, xxcj08,xxcj08ReqXml,userId,userPwd,key);
		String code = xxcj08RespJson.getString("CODE");
		AuthenticationBasicInformationVo authenticationBasicInformationVo = new AuthenticationBasicInformationVo();
		if("0000".equals(code)){
			JSONObject jsonObject = (JSONObject) xxcj08RespJson.get("BODY");
			//真实姓名
			String trueName = (String) jsonObject.get("LOGIN_TRUENAME");
			//身份证号
			String identityCard = (String) jsonObject.get("SFZMHM");
			//手机号
			String mobilephone = (String) jsonObject.get("YDDH");
			authenticationBasicInformationVo.setTrueName(trueName);
			authenticationBasicInformationVo.setIdentityCard(identityCard);
			authenticationBasicInformationVo.setMobilephone(mobilephone);
			//authenticationBasicInformationVo.setMyAvatar("等微信开发获取");
		}
		return authenticationBasicInformationVo;
	}
	/**
	 * 已绑定机动车查询接口
	 * @param mobilephone 手机号
	 * @param identityCard 身份证号
	 * @param sourceOfCertification 认证来源
	 * @param url  请求url
	 * @param method  请求方法
	 * @param userId  用户id
	 * @param userPwd  用户密码
	 * @param key  秘钥
	 * @return BindTheVehicleVo
	 * @throws Exception 
	 */
	public static List<BindTheVehicleVo> bindsTheMotorVehicleQuery(String mobilephone,String identityCard,String sourceOfCertification,String url,String method,String userId,String userPwd,String key) throws Exception{
		List<BindTheVehicleVo> bindTheVehicleVos = new ArrayList<BindTheVehicleVo>();
		String xxcj06 = "xxcj06";
		String xxcj06ReqXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><REQUEST><LOGIN_USER>"+identityCard+"</LOGIN_USER><YHLY>"+sourceOfCertification+"</YHLY></REQUEST>";
		JSONObject xxcj06RespJson = WebServiceClient.getInstance().requestWebService(url, method, xxcj06,xxcj06ReqXml,userId,userPwd,key);
		String code = xxcj06RespJson.getString("CODE");
		if("0000".equals(code)){
			xxcj06RespJson = (JSONObject) xxcj06RespJson.get("BODY");
			String xString = xxcj06RespJson.toString();
			if(StringUtils.isNotBlank(xString)){
				if(xString.contains("[")){
					//绑定多辆车
					JSONArray moreC = xxcj06RespJson.getJSONArray("ROW");
					Iterator iterator = moreC.iterator();
					while (iterator.hasNext()) {
						BindTheVehicleVo bindTheVehicleVo = new BindTheVehicleVo();
						JSONObject jsonObject = (JSONObject) iterator.next();
						String CJH4 = jsonObject.getString("CJH4"); //车架后4位
						String CZSFZMHM = jsonObject.getString("CZSFZMHM"); //车主身份证明号码
						String CZXM = jsonObject.getString("CZXM"); //车主姓名
						String HPHM = jsonObject.getString("HPHM"); //号牌号码
						String HPZL = jsonObject.getString("HPZL"); //号牌种类
						String QZBFQZ = jsonObject.getString("QZBFQZ"); //强制报废期止提醒
						String SFBR = jsonObject.getString("SFBR"); //是否本人
						String SFJC = jsonObject.getString("SFJC"); //车牌核发省简称
						String SYRQ = jsonObject.getString("SYRQ"); //审验日期
						String ZT = jsonObject.getString("ZT"); //状态 A正常B转出C被盗抢D停驶E注销G违法未处理H海关监管I事故未处理J嫌疑车K查封L暂扣M强制注销N事故逃逸O锁定P到达报废标准公告牌证作废Q逾期未检验
						
						bindTheVehicleVo.setNumberPlateNumber(SFJC + HPHM);
						bindTheVehicleVo.setPlateType(HPZL);
						
						bindTheVehicleVo.setName(CZXM);
						bindTheVehicleVo.setIdentityCard(CZSFZMHM);
						bindTheVehicleVo.setIsMyself(SFBR);
						bindTheVehicleVo.setBehindTheFrame4Digits(CJH4);
						
						if(StringUtils.isNotBlank(SYRQ)){
							bindTheVehicleVo.setAnnualReviewDate(SYRQ);
							String dateFormat = "yyyy-MM-dd";
							Date date1 = AccountDateUtil.StringToDate(SYRQ, dateFormat);
							String dateStr = AccountDateUtil.DateToString(new Date(), dateFormat);
							Date date2 = AccountDateUtil.StringToDate(dateStr, dateFormat);
							int haveDateStr = AccountDateUtil.differentDaysByMillisecond(date1, date2);
							if(haveDateStr >= 0){
								bindTheVehicleVo.setAnnualReviewDateRemind("距离年审时间还有" + haveDateStr + "天");
							}else{
								//年审过期
								bindTheVehicleVo.setAnnualReviewDateRemind("您的车辆过期未审");
							}
						}else{
							//没有年审日期
							bindTheVehicleVo.setAnnualReviewDate("");
							bindTheVehicleVo.setAnnualReviewDateRemind("");
						}
						
						bindTheVehicleVo.setMobilephone(mobilephone);
						bindTheVehicleVo.setIllegalNumber("这里需要调用该车辆的未处理的违法总数");
						bindTheVehicleVo.setOtherPeopleUse("车辆其他使用人");
						bindTheVehicleVos.add(bindTheVehicleVo);
					}
					
				}else{
					//绑定一辆车
					JSONObject oneC = xxcj06RespJson.getJSONObject("ROW");
					
					BindTheVehicleVo bindTheVehicleVo = new BindTheVehicleVo();
					String CJH4 = oneC.getString("CJH4"); //车架后4位
					String CZSFZMHM = oneC.getString("CZSFZMHM"); //车主身份证明号码
					String CZXM = oneC.getString("CZXM"); //车主姓名
					String HPHM = oneC.getString("HPHM"); //号牌号码
					String HPZL = oneC.getString("HPZL"); //号牌种类
					String QZBFQZ = oneC.getString("QZBFQZ"); //强制报废期止提醒
					String SFBR = oneC.getString("SFBR"); //是否本人
					String SFJC = oneC.getString("SFJC"); //车牌核发省简称
					String SYRQ = oneC.getString("SYRQ"); //审验日期
					String ZT = oneC.getString("ZT"); //状态 A正常B转出C被盗抢D停驶E注销G违法未处理H海关监管I事故未处理J嫌疑车K查封L暂扣M强制注销N事故逃逸O锁定P到达报废标准公告牌证作废Q逾期未检验
					
					bindTheVehicleVo.setNumberPlateNumber(SFJC + HPHM);
					bindTheVehicleVo.setPlateType(HPZL);
					bindTheVehicleVo.setAnnualReviewDate(SYRQ);
					bindTheVehicleVo.setName(CZXM);
					bindTheVehicleVo.setIdentityCard(CZSFZMHM);
					bindTheVehicleVo.setIsMyself(SFBR);
					bindTheVehicleVo.setBehindTheFrame4Digits(CJH4);
					
					if(StringUtils.isNotBlank(SYRQ)){
						String dateFormat = "yyyy-MM-dd";
						Date date1 = AccountDateUtil.StringToDate(SYRQ, dateFormat);
						String dateStr = AccountDateUtil.DateToString(new Date(), dateFormat);
						Date date2 = AccountDateUtil.StringToDate(dateStr, dateFormat);
						int haveDateStr = AccountDateUtil.differentDaysByMillisecond(date1, date2);
						if(haveDateStr >= 0){
							bindTheVehicleVo.setAnnualReviewDateRemind("距离年审时间还有 " + haveDateStr + "天");
						}else{
							//年审过期
							bindTheVehicleVo.setAnnualReviewDateRemind("您的车辆过期未审");
						}
					}else{
						//没有年审日期
						bindTheVehicleVo.setAnnualReviewDate("");
						bindTheVehicleVo.setAnnualReviewDateRemind("");
					}
					bindTheVehicleVo.setMobilephone(mobilephone);
					bindTheVehicleVo.setIllegalNumber("这里需要调用该车辆的未处理的违法总数");
					bindTheVehicleVo.setOtherPeopleUse("车辆其他使用人");
					bindTheVehicleVos.add(bindTheVehicleVo);
				}
			}else{
				//没有绑定车辆
			}
		}
		return bindTheVehicleVos;
	}
	/**
	 * 查询我的驾驶证信息 xxcj07
	 * @param identityCard 身份证号
	 * @param sourceOfCertification 认证来源
	 * @return
	 * @throws Exception 
	 */
	public static MyDriverLicenseVo getMyDriverLicense(String identityCard,String sourceOfCertification,String url,String method,String userId,String userPwd,String key) throws Exception{
		MyDriverLicenseVo myDriverLicenseVo = new MyDriverLicenseVo();
		
		String xxcj07 = "xxcj07";
		String xxcj07ReqXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><REQUEST><LOGIN_USER>"+identityCard+"</LOGIN_USER><YHLY>"+sourceOfCertification+"</YHLY></REQUEST>";
		JSONObject xxcj07RespJson = WebServiceClient.getInstance().requestWebService(url, method, xxcj07,xxcj07ReqXml,userId,userPwd,key);
		String code = xxcj07RespJson.getString("CODE");
		String msg = xxcj07RespJson.getString("MSG");
		myDriverLicenseVo.setCode(code);
		myDriverLicenseVo.setCode(msg);
		if("0000".equals(code)){
			xxcj07RespJson = (JSONObject) xxcj07RespJson.get("BODY");
			//档案编号
			String DABH =  (String) xxcj07RespJson.get("DABH");
			//被扣记分
			String LJJF =  (String) xxcj07RespJson.get("LJJF");
			//身份证号
			String SFZMHM =  (String) xxcj07RespJson.get("SFZMHM");
			//审验日期
			String SYRQ =  (String) xxcj07RespJson.get("SYRQ");
			//性别 1-男
			String XB =  (String) xxcj07RespJson.get("XB");
			//姓名
			String XM =  (String) xxcj07RespJson.get("XM");
			//有效日期
			String YXQZ =  (String) xxcj07RespJson.get("YXQZ");
			//准驾车型
			String ZJCX =  (String) xxcj07RespJson.get("ZJCX");
			//状态 	A正常	B超分	C转出	D暂扣	E撤销	F吊销	G注销	H违法未处理	I事故未处理	J停止使用	K抵押	L锁定	M逾期未换证	N延期换证	P延期体检	R注销可恢复	S逾期未审验状态	T延期审验	U扣留
			String ZT =  (String) xxcj07RespJson.get("ZT");
			
			Integer jf = Integer.valueOf(LJJF);
			jf = 12 - jf;
			myDriverLicenseVo.setAvailableScore(jf + "分");
			myDriverLicenseVo.setEffectiveDate(SYRQ);
			myDriverLicenseVo.setFileNumber(DABH);
			//myDriverLicenseVo.setIsReceive(0); //由于涉及到微信卡包是否领取判断，这版本微信卡包不开发，此字段无法获取
			myDriverLicenseVo.setPhysicalExaminationDate(SYRQ); //体检日期
			
			//多个状态处理
			char[] cs = ZT.toCharArray();
			StringBuffer sb = new StringBuffer();
	    	for(char c : cs){
	    		String ztStr = ZT_STATUS.valueOf(String.valueOf(c)).getZtStatus();
	    		sb.append(ztStr).append(",");
	    	}
	    	String zString = sb.toString();
	    	zString = zString.substring(0, zString.length() - 1);
			myDriverLicenseVo.setStatus(zString);
		}
		return myDriverLicenseVo;
	}
	
	/**
	 * 电子驾驶证查询 
	 * @param driverLicenseNumber 驾驶证号=身份证号
	 * @param userName 姓名
	 * @param mobileNumber 申请手机号码
	 * @param sourceOfCertification 认证来源
	 * @param url  请求url
	 * @param method  请求方法
	 * @param userId  用户id
	 * @param userPwd  用户密码
	 * @param key  秘钥
	 * @return ElectronicDriverLicenseVo
	 * @throws Exception
	 */
	public static ElectronicDriverLicenseVo getElectronicDriverLicense(String driverLicenseNumber,String userName,String mobileNumber, String sourceOfCertification,
			String url,String method,String userId,String userPwd,String key) throws Exception{
		String DZJSZ = "DZJSZ";
		String DZJSZReqXml = "<?xml version='1.0' encoding='gb2312'?><REQUEST><JSZHM>"+driverLicenseNumber+"</JSZHM><XM>"+userName+"</XM><SQSJHM>"+mobileNumber+"</SQSJHM><SQLY>"+sourceOfCertification+"</SQLY></REQUEST>";
		JSONObject xxcj06RespJson = WebServiceClient.getInstance().requestWebService(url, method, DZJSZ,DZJSZReqXml,userId,userPwd,key);
		ElectronicDriverLicenseVo electronicDriverLicenseVo = new ElectronicDriverLicenseVo();
		String code = xxcj06RespJson.getString("CODE");
		if("0000".equals(code)){
			xxcj06RespJson = (JSONObject) xxcj06RespJson.get("BODY");
			String DZZ = xxcj06RespJson.getString("DZZ");
			String EWM = xxcj06RespJson.getString("EWM");
			
			electronicDriverLicenseVo.setElectronicDriverLicense(DZZ);
			electronicDriverLicenseVo.setElectronicDriverLicenseQRCode(EWM);
		}
		return electronicDriverLicenseVo;
	}
	/**
	 * 电子行驶证查询
	 * @param numberPlatenumber 号牌号码
	 * @param plateType 号牌种类 
	 * @param mobileNumber 手机号
	 * @param sourceOfCertification 认证来源
	 * @param url  请求url
	 * @param method  请求方法
	 * @param userId  用户id
	 * @param userPwd  用户密码
	 * @param key  秘钥
	 * @return DrivingLicenseVo
	 * @throws Exception
	 * http://localhost:8080/web/user/search/getDrivingLicense.html?numberPlatenumber=粤B701NR&plateType=02&mobileNumber=18603017278
	 * 02-蓝牌
	 * 
	 */
	public static DrivingLicenseVo getDrivingLicense(String numberPlatenumber,String plateType,String mobileNumber, String sourceOfCertification,
			String url,String method,String userId,String userPwd,String key) throws Exception{
		String DZXSZ = "DZXSZ";
		String DZXSZReqXml = "<?xml version='1.0' encoding='gb2312'?><REQUEST><HPHM>"+numberPlatenumber+"</HPHM><HPZL>"+plateType+"</HPZL><SQSJHM>"+mobileNumber+"</SQSJHM><SQLY>"+sourceOfCertification+"</SQLY></REQUEST>";
		JSONObject DZXSZRespJson = WebServiceClient.getInstance().requestWebService(url, method, DZXSZ,DZXSZReqXml,userId,userPwd,key);
		String code = DZXSZRespJson.getString("CODE");
		DrivingLicenseVo drivingLicenseVo = new DrivingLicenseVo();
		if("0000".equals(code)){
			DZXSZRespJson = (JSONObject) DZXSZRespJson.get("BODY");
			//车主姓名
			String CZXM = DZXSZRespJson.getString("CZXM");
			//身份证
			String CZSFZMHM = DZXSZRespJson.getString("CZSFZMHM");
			//1为本人，0为其他人
			String SFBR = DZXSZRespJson.getString("SFBR");
			//电子行驶证（base64）
			String DZZ = DZXSZRespJson.getString("DZZ");
			//电子行驶证二维码（加密串）
			String EWM = DZXSZRespJson.getString("EWM");
			
			drivingLicenseVo.setElectronicDriverLicense(CZXM);
			drivingLicenseVo.setIsOwnerName(SFBR);
			drivingLicenseVo.setElectronicDrivingLicense(DZZ);
			drivingLicenseVo.setElectronicDrivingLicenseQRCode(EWM);
		}
		return drivingLicenseVo;
	}
	
	/**
	 * 提交  驾驶人信息单打印申请接口
	 * @param applyType 申请类型（1代表驾驶人信息单；2代表机动车信息单 3代表无车证明申请；4代表驾驶人安全事故信用表）
	 * @param applyName 申请人姓名（必须是星级用户姓名）
	 * @param applyIdCard 申请人身份证号码（必须是星级用户身份证号码）
	 * @param applyMobileNumber 申请人联系电话（必须是星级用户联系电话）
	 * @param sourceOfCertification 申请来源（APP 传A，微信传C，支付宝传Z）
	 * @param url  请求url
	 * @param method  请求方法
	 * @param userId  用户id
	 * @param userPwd  用户密码
	 * @param key  秘钥
	 * @return 0000 表示申请成功
	 * @throws Exception 
	 */
	public static Map<String, String> commitDriverInformationSinglePrintApplicationInterface(String applyType, String applyName,String applyIdCard, String applyMobileNumber,String sourceOfCertification,
			String url,String method,String userId,String userPwd,String key) throws Exception{
		String EZ1005 = "EZ1005";
		String EZ1005ReqXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><request><sqlx>"+applyType+"</sqlx><xm>"+applyName+"</xm><sfzmhm>"+applyIdCard+"</sfzmhm><lxdh>"+applyMobileNumber+"</lxdh><sqly>"+sourceOfCertification+"</sqly></request>";
		JSONObject EZ1005RespJson = WebServiceClient.getInstance().requestWebService(url, method, EZ1005,EZ1005ReqXml,userId,userPwd,key);
		String code = EZ1005RespJson.getString("code");
		String msg = EZ1005RespJson.getString("msg");
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	/**
	 * 提交  机动车信息单打印申请接口
	 * @param applyName 申请人姓名（必须是星级用户姓名）
	 * @param applyIdCard 申请人身份证号码（必须是星级用户身份证号码）
	 * @param applyMobileNumber 申请人联系电话（必须是星级用户联系电话）
	 * @param numberPlateNumber 号牌号码（必须是星级用户绑定的本人车辆的号牌号码）
	 * @param plateType 号牌种类（必须是星级用户绑定的本人车辆的号牌种类）
	 * @param sourceOfCertification 申请来源（APP 传A，微信传C，支付宝传Z）
	 * @param url  请求url
	 * @param method  请求方法
	 * @param userId  用户id
	 * @param userPwd  用户密码
	 * @param key  秘钥
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> commitAAingleApplicationForMotorVehicleInformation(String applyName,String applyIdCard,String applyMobileNumber,String numberPlateNumber,String plateType
			,String sourceOfCertification,String url,String method,String userId,String userPwd,String key) throws Exception{
		//申请类型（默认传2）
		String applyType = "2";
		String EZ1006 = "EZ1006";
		String EZ1006ReqXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><request><sqlx>"+applyType+"</sqlx><xm>"+applyName+"</xm><sfzmhm>"+applyIdCard+"</sfzmhm><lxdh>"+applyMobileNumber+"</lxdh><hphm>"+numberPlateNumber+"</hphm><hpzl>"+plateType+"</hpzl><sqly>"+sourceOfCertification+"</sqly></request>";
		JSONObject EZ1006RespJson = WebServiceClient.getInstance().requestWebService(url, method, EZ1006,EZ1006ReqXml,userId,userPwd,key);
		String code = EZ1006RespJson.getString("code");
		String msg = EZ1006RespJson.getString("msg");
		Map<String, String> map = new HashMap<String, String>();
		map.put("code", code);
		map.put("msg", msg);
		return map;
	}
	/**
	 * 查询机动车信息单进度
	 * @param applyType 申请类型(1、驾驶人信息单；2、机动车信息单；3、无车证明申请；4、驾驶人安全事故信用表)
	 * @param applyIdCard 身份证
	 * @param sourceOfCertification 认证来源
	 * @param url  请求url
	 * @param method  请求方法
	 * @param userId  用户id
	 * @param userPwd  用户密码
	 * @param key  秘钥
	 * @return 对应的进度信息
	 * @throws Exception
	 */
	public static Map<String, Object> queryMachineInformationSheet(String applyType, String applyIdCard,String sourceOfCertification,String url,String method,String userId,String userPwd,String key) throws Exception{
		Map<String, Object> map = new HashMap<String, Object>();
		
		String EZ1007 = "EZ1007";
		String EZ1007ReqXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><request><sqlx>"+applyType+"</sqlx><sfzmhm>"+applyIdCard+"</sfzmhm></request>";
		JSONObject EZ1007RespJson = WebServiceClient.getInstance().requestWebService(url, method, EZ1007,EZ1007ReqXml,userId,userPwd,key);
		String code = EZ1007RespJson.getString("code");
		String msg = EZ1007RespJson.getString("msg");
		
		if("0000".equals(code)){
			//成功
			EZ1007RespJson = EZ1007RespJson.getJSONObject("body");
			List<InformationSheetVo> informationSheetVos = new ArrayList<InformationSheetVo>();
			if(EZ1007RespJson.toJSONString().contains("[")){
				//多条
				JSONArray jsonArray = EZ1007RespJson.getJSONArray("row");
				Iterator iterator = jsonArray.iterator();
				
				while (iterator.hasNext()) {
					InformationSheetVo informationSheetVo = new InformationSheetVo();
					JSONObject jsonObject = (JSONObject) iterator.next();
					String hphm = jsonObject.getString("hphm"); //车牌号 例如  粤B701NR
					String hpzl = jsonObject.getString("hpzl"); //号牌类型 02-黄牌
					String photo = jsonObject.getString("photo"); //图片
					String sfzmhm = jsonObject.getString("sfzmhm"); //身份证
					String shzt = jsonObject.getString("shzt"); //状态代码
					String sqsj = jsonObject.getString("sqsj"); //申请时间
					String tbyy = jsonObject.getString("tbyy"); //审核结果
					String xh = jsonObject.getString("xh");//申请流水号
					String xm = jsonObject.getString("xm");//姓名
					informationSheetVo.setApplicationTime(sqsj);
					informationSheetVo.setApplyForSerialNumber(xh);
					informationSheetVo.setAuditResults(tbyy);
					informationSheetVo.setIdCard(sfzmhm);
					informationSheetVo.setName(xm);
					informationSheetVo.setNumberPlate(hphm);
					informationSheetVo.setPhoto(photo);
					informationSheetVo.setPlateType(hpzl);
					informationSheetVo.setStatusCode(shzt);
					informationSheetVos.add(informationSheetVo);
				}
				
			}else{
				InformationSheetVo informationSheetVo = new InformationSheetVo();
				JSONObject jsonObject = EZ1007RespJson.getJSONObject("row");
				String hphm = jsonObject.getString("hphm"); //车牌号 例如  粤B701NR
				String hpzl = jsonObject.getString("hpzl"); //号牌类型 02-黄牌
				String photo = jsonObject.getString("photo"); //图片
				String sfzmhm = jsonObject.getString("sfzmhm"); //身份证
				String shzt = jsonObject.getString("shzt"); //状态代码
				String sqsj = jsonObject.getString("sqsj"); //申请时间
				String tbyy = jsonObject.getString("tbyy"); //审核结果
				String xh = jsonObject.getString("xh");//申请流水号
				String xm = jsonObject.getString("xm");//姓名
				informationSheetVo.setApplicationTime(sqsj);
				informationSheetVo.setApplyForSerialNumber(xh);
				informationSheetVo.setAuditResults(tbyy);
				informationSheetVo.setIdCard(sfzmhm);
				informationSheetVo.setName(xm);
				informationSheetVo.setNumberPlate(hphm);
				informationSheetVo.setPhoto(photo);
				informationSheetVo.setPlateType(hpzl);
				informationSheetVo.setStatusCode(shzt);
				//单条
				informationSheetVos.add(informationSheetVo);
			}
			map.put("code", code);
			map.put("data", informationSheetVos);
		}else{
			//失败
			map.put("code", code);
			map.put("msg", msg);
			map.put("data", null);
		}
		return map;
	}
	/**
	 * 驾驶证补证换证业务查询接口
	 * @param idCard 身份证
	 * @param sourceOfCertification 认证来源
	 * @param url  请求url
	 * @param method  请求方法
	 * @param userId  用户id
	 * @param userPwd  用户密码
	 * @param key  秘钥
	 * @return
	 * @throws Exception
	 */
	public static Object getDriverLicenseToReplenishBusinessInquiriesInterface(String idCard,String sourceOfCertification,String url,String method,String userId,String userPwd,String key) throws Exception{
		String xxcj20 = "xxcj20";
		String xxcj20ReqXml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><REQUEST><SFZMHM>"+idCard+"</SFZMHM></REQUEST>";
		JSONObject xxcj20RespJson = WebServiceClient.getInstance().requestWebService(url, method, xxcj20,xxcj20ReqXml,userId,userPwd,key);
		String CODE = xxcj20RespJson.getString("CODE");
		String MSG = xxcj20RespJson.getString("MSG");
		List<DriverLicenseToSupplementThePermitBusinessVo> driverLicenseToSupplementThePermitBusinessVos = new ArrayList<DriverLicenseToSupplementThePermitBusinessVo>();
		if("0000".equals(CODE)){
			//有数据
			xxcj20RespJson = xxcj20RespJson.getJSONObject("BODY");
			if(xxcj20RespJson.toJSONString().contains("[")){
				//多条
				JSONArray jsonArray = xxcj20RespJson.getJSONArray("ROW");
				driverLicenseToSupplementThePermitBusinessVos = JSONObject.parseArray(jsonArray.toJSONString(), DriverLicenseToSupplementThePermitBusinessVo.class);
			}else{
				//一条
				JSONObject jsonObject = xxcj20RespJson.getJSONObject("ROW");
				DriverLicenseToSupplementThePermitBusinessVo driverLicenseToSupplementThePermitBusinessVo = JSONObject.parseObject(jsonObject.toJSONString(), DriverLicenseToSupplementThePermitBusinessVo.class);
				driverLicenseToSupplementThePermitBusinessVos.add(driverLicenseToSupplementThePermitBusinessVo);
			}
			 
		}else {
			//无数据
		}
		return driverLicenseToSupplementThePermitBusinessVos;
	}
	/**
	 * 机动车业务查询接口
	 * @param idCard 身份证
	 * @param sourceOfCertification 认证来源
	 * @param url  请求url
	 * @param method  请求方法
	 * @param userId  用户id
	 * @param userPwd  用户密码
	 * @param key  秘钥
	 * @return
	 * @throws Exception
	 */
	public static Object getMotorVehicleBusiness(String idCard,String sourceOfCertification,String url,String method,String userId,String userPwd,String key) throws Exception{
		String xxcj21 = "xxcj21";
		String xxcj21ReqXml = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><REQUEST><SFZMHM>"+idCard+"</SFZMHM></REQUEST>";
		JSONObject xxcj21RespJson = WebServiceClient.getInstance().requestWebService(url, method, xxcj21,xxcj21ReqXml,userId,userPwd,key);
		String CODE = xxcj21RespJson.getString("CODE");
		String MSG = xxcj21RespJson.getString("MSG");
		List<MotorVehicleBusiness> motorVehicleBusinesses = new ArrayList<MotorVehicleBusiness>();
		if("0000".equals(CODE)){
			//有数据
			xxcj21RespJson = xxcj21RespJson.getJSONObject("BODY");
			
			if(xxcj21RespJson.toJSONString().contains("[")){
				//多条
				JSONArray jsonArray = xxcj21RespJson.getJSONArray("ROW");
				motorVehicleBusinesses = JSONObject.parseArray(jsonArray.toJSONString(), MotorVehicleBusiness.class);
			}else{
				//一条
				JSONObject jsonObject = xxcj21RespJson.getJSONObject("ROW");
				MotorVehicleBusiness motorVehicleBusiness = JSONObject.parseObject(jsonObject.toJSONString(), MotorVehicleBusiness.class);
				motorVehicleBusinesses.add(motorVehicleBusiness);
			}
		}else {
			//无数据
		}
		return motorVehicleBusinesses;
	}
	
	/**
	 * 身份认证审核结果查询
	 * @param collectingSerialNumber 采集流水号（非必填）
	 * @param idCard 身份证号
	 * @param sourceOfCertification 认证来源
	 * @param url 请求url
	 * @param method 请求方法
	 * @param userId 用户id
	 * @param userPwd  用户密码
	 * @param key 秘钥
	 * @throws Exception
	 */
	public static Object identificationOfAuditResults(String collectingSerialNumber,String idCard,String sourceOfCertification,String url,String method,String userId,String userPwd,String key) throws Exception{
		String xxcj14 = "xxcj14";
		String xxcj14ReqXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><REQUEST><CID>"+collectingSerialNumber+"</CID><SFZMHM>"+idCard+"</SFZMHM></REQUEST>";
		JSONObject xxcj14RespJson = WebServiceClient.getInstance().requestWebService(url, method, xxcj14,xxcj14ReqXml,userId,userPwd,key);
		xxcj14RespJson = xxcj14RespJson.getJSONObject("BODY");
		xxcj14RespJson = xxcj14RespJson.getJSONObject("ROW");
		IdentificationOfAuditResultsVo identificationOfAuditResultsVo = new IdentificationOfAuditResultsVo();
		//认证  1车主本人2非车主本人3驾驶人
		String RZLX = xxcj14RespJson.getString("RZLX");
		//信息登记时间
		String SHSJ = xxcj14RespJson.getString("SHSJ");
		//当前状态 	0待审核、1审核通过 、TB退办
		String SHZT = xxcj14RespJson.getString("SHZT");
		//退办原因
		String TBYY = xxcj14RespJson.getString("TBYY");
		identificationOfAuditResultsVo.setRZLX(RZLX);
		identificationOfAuditResultsVo.setSHSJ(SHSJ);
		identificationOfAuditResultsVo.setSHZT(SHZT);
		identificationOfAuditResultsVo.setTBYY(TBYY);
		return identificationOfAuditResultsVo;
	}
	/**
	 * 自然人身份认证审核结果查询
	 * @param collectingSerialNumber 采集流水号（非必填）
	 * @param idCard 身份证号
	 * @param sourceOfCertification 认证来源
	 * @param url 请求url
	 * @param method 请求方法
	 * @param userId 用户id
	 * @param userPwd  用户密码
	 * @param key 秘钥
	 * @throws Exception
	 */
	public static Object searchForNaturaPersonIdentityAuditResults(String collectingSerialNumber,String idCard,String sourceOfCertification,String url,String method,String userId,String userPwd,String key) throws Exception{
		String xxcjzrrcx = "xxcjzrrcx";
		String xxcjzrrcxReqXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><REQUEST><CID>"+collectingSerialNumber+"</CID><SFZMHM>"+idCard+"</SFZMHM></REQUEST>";
		JSONObject xxcjzrrcxRespJson = WebServiceClient.getInstance().requestWebService(url, method, xxcjzrrcx,xxcjzrrcxReqXml,userId,userPwd,key);
		xxcjzrrcxRespJson = xxcjzrrcxRespJson.getJSONObject("BODY");
		xxcjzrrcxRespJson = xxcjzrrcxRespJson.getJSONObject("ROW");
		IdentificationOfAuditResultsVo identificationOfAuditResultsVo = new IdentificationOfAuditResultsVo();
		//认证  1车主本人2非车主本人3驾驶人
		String RZLX = xxcjzrrcxRespJson.getString("RZLX");
		//信息登记时间
		String SHSJ = xxcjzrrcxRespJson.getString("SHSJ");
		//当前状态 	0待审核、1审核通过 、TB退办
		String SHZT = xxcjzrrcxRespJson.getString("SHZT");
		//退办原因
		String TBYY = xxcjzrrcxRespJson.getString("TBYY");
		identificationOfAuditResultsVo.setRZLX(RZLX);
		identificationOfAuditResultsVo.setSHSJ(SHSJ);
		identificationOfAuditResultsVo.setSHZT(SHZT);
		identificationOfAuditResultsVo.setTBYY(TBYY);
		return identificationOfAuditResultsVo;
	}
	/**
	 * 公车注册结果查询
	 * @param organizationCodeNumber  组织机构代码证号
	 * @param sourceOfCertification 认证来源
	 * @param url 请求url
	 * @param method 请求方法
	 * @param userId 用户id
	 * @param userPwd  用户密码
	 * @param key 秘钥
	 * @throws Exception
	 */
	public static Object busRegistrationResultsInquiries(String organizationCodeNumber,String sourceOfCertification,String url,String method,String userId,String userPwd,String key) throws Exception{
		
		String dwc02 = "dwc02";
		String dwc02ReqXml = "<?xml version=\"1.0\"encoding=\"utf-8\"?><request><zzjgdmzh>"+organizationCodeNumber+"</zzjgdmzh><cxly>"+sourceOfCertification+"</cxly></request>";
		JSONObject dwc02RespJson = WebServiceClient.getInstance().requestWebService(url, method, dwc02,dwc02ReqXml,userId,userPwd,key);
		dwc02RespJson = dwc02RespJson.getJSONObject("BODY");
		dwc02RespJson = dwc02RespJson.getJSONObject("ROW");
		IdentificationOfAuditResultsVo identificationOfAuditResultsVo = new IdentificationOfAuditResultsVo();
		//认证  1车主本人2非车主本人3驾驶人
		String RZLX = dwc02RespJson.getString("RZLX");
		//信息登记时间
		String SHSJ = dwc02RespJson.getString("SHSJ");
		//当前状态 	0待审核、1审核通过 、TB退办
		String SHZT = dwc02RespJson.getString("SHZT");
		//退办原因
		String TBYY = dwc02RespJson.getString("TBYY");
		identificationOfAuditResultsVo.setRZLX(RZLX);

		identificationOfAuditResultsVo.setSHZT(SHZT);
		identificationOfAuditResultsVo.setTBYY(TBYY);
		return identificationOfAuditResultsVo;
	}
	/**
	 * 车主查询本人车辆绑定的其他驾驶人
	 * @param idCard 身份证
	 * @param numberPlateNumber 号牌号码
	 * @param plateType 号牌种类
	 * @param sourceOfCertification 认证来源
	 * @param url 请求url
	 * @param method 请求方法
	 * @param userId 用户id
	 * @param userPwd 用户密码
	 * @param key 秘钥
	 * @return
	 * @throws Exception 
	 */
	public static List<BindTheVehicleVo> getBindTheOtherDriversUseMyCar(String idCard,String numberPlateNumber,String plateType,String sourceOfCertification,String url,String method,String userId,String userPwd,String key) throws Exception{
		List<BindTheVehicleVo> bindTheVehicleVos = new ArrayList<BindTheVehicleVo>();
		String dwc02 = "xxcj18";
		String dwc02ReqXml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><REQUEST><LOGIN_USER>"+idCard+"</LOGIN_USER><HPHM>"+numberPlateNumber+"</HPHM><HPZL>"+plateType+"</HPZL><RZJS>"+sourceOfCertification+"</RZJS></REQUEST>";
		JSONObject dwc02RespJson = WebServiceClient.getInstance().requestWebService(url, method, dwc02,dwc02ReqXml,userId,userPwd,key);
		return bindTheVehicleVos;
	}
	/*public static DriverLicenseInformationSheetVo getDriverLicenseInformationSheet(String identityCard,String sourceOfCertification,
			String url,String method,String userId,String userPwd,String key){
		
	}*/
	
	public static void main(String[] args) throws Exception {
		//resetPwd("622822198502074110", "王玉璞", "15920071829", "C", "http://123.56.180.216:19002/xxfbpt/services/xxfbptservice", "xxptSchuding", "WX02", "WX02@168", "94D863D9BE7FB032E6A19430CC892610");
		
		//getBindTheOtherDriversUseMyCar("440301199002101119", "粤B701NR", "02", "C", "http://123.56.180.216:19002/xxfbpt/services/xxfbptservice", "xxptSchuding", "WX02", "WX02@168", "94D863D9BE7FB032E6A19430CC892610");
		//commitAAingleApplicationForMotorVehicleInformation("王玉璞", "622822198502074110", "15920071829", "B6F7M1", "02", "C", "http://123.56.180.216:19002/xxfbpt/services/xxfbptservice", "xxptSchuding", "WX02", "WX02@168", "94D863D9BE7FB032E6A19430CC892610");
		
		//commitDriverInformationSinglePrintApplicationInterface("1", "王玉璞", "622822198502074110", "15920071829", "C", "http://123.56.180.216:19002/xxfbpt/services/xxfbptservice", "xxptSchuding", "WX02", "WX02@168", "94D863D9BE7FB032E6A19430CC892610");
		//System.out.println(queryMachineInformationSheet("1", "420881198302280017", "C", "http://123.56.180.216:19002/xxfbpt/services/xxfbptservice", "xxptSchuding", "WX02", "WX02@168", "94D863D9BE7FB032E6A19430CC892610"));
		//getDriverLicenseToReplenishBusinessInquiriesInterface("42052119890202011X", "C", "http://123.56.180.216:19002/xxfbpt/services/xxfbptservice", "xxptSchuding", "WX02", "WX02@168", "94D863D9BE7FB032E6A19430CC892610");
		//getMotorVehicleBusiness("440301199002101119", "C", "http://123.56.180.216:19002/xxfbpt/services/xxfbptservice", "xxptSchuding", "WX02", "WX02@168", "94D863D9BE7FB032E6A19430CC892610");
		//identificationOfAuditResults("", "440301199002101119", "C", "http://123.56.180.216:19002/xxfbpt/services/xxfbptservice", "xxptSchuding", "WX02", "WX02@168", "94D863D9BE7FB032E6A19430CC892610");
		//searchForNaturaPersonIdentityAuditResults("", "440301199002101119", "C", "http://123.56.180.216:19002/xxfbpt/services/xxfbptservice", "xxptSchuding", "WX02", "WX02@168", "94D863D9BE7FB032E6A19430CC892610");
		
		//busRegistrationResultsInquiries("3SSS", "C", "http://123.56.180.216:19002/xxfbpt/services/xxfbptservice", "xxptSchuding", "WX02", "WX02@168", "94D863D9BE7FB032E6A19430CC892610");
		//getMyDriverLicense("440301199002101119", "C", "http://123.56.180.216:19002/xxfbpt/services/xxfbptservice", "xxptSchuding", "WX02", "WX02@168", "94D863D9BE7FB032E6A19430CC892610");
		//authenticationBasicInformationQuery("13809613859", "C",  "http://123.56.180.216:19002/xxfbpt/services/xxfbptservice", "xxptSchuding", "WX02", "WX02@168", "94D863D9BE7FB032E6A19430CC892610");
		//bindsTheMotorVehicleQuery("13809613859", "350582197810012622", "C",  "http://123.56.180.216:19002/xxfbpt/services/xxfbptservice", "xxptSchuding", "WX02", "WX02@168", "94D863D9BE7FB032E6A19430CC892610");
		//getElectronicDriverLicense("440301199002101119", "杨明畅", "18603017278", "C", "http://123.56.180.216:19002/xxfbpt/services/xxfbptservice", "xxptSchuding", "WX02", "WX02@168", "94D863D9BE7FB032E6A19430CC892610");
		
		login("15016709831", "633420", "http://123.56.180.216:19002/xxfbpt/services/xxfbptservice", "xxptSchuding", "WX02", "WX02@168", "94D863D9BE7FB032E6A19430CC892610", "C");
		
	}
	
	
}
