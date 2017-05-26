package cn.account.utils;



import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.account.bean.vo.BindCarVo;
import cn.account.bean.vo.BindDriverLicenseVo;
import cn.account.bean.vo.ReadilyShootVo;
import cn.account.bean.vo.RegisterVo;
import cn.account.bean.vo.UserBasicVo;
import cn.sdk.util.Base64;
import cn.sdk.webservice.WebServiceClient;

@SuppressWarnings(value="all")
public class NozzleMeans {
	
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 
	 * @Title: addVehicle 
	 * @author liuminkang
	 * @Description: TODO(调用绑定车辆外部接口) 
	 * @param bindCarVo
	 * @param url
	 * @param method
	 * @param userId
	 * @param userPwd
	 * @param key
	 * @return
	 * @throws Exception    设定文件 
	 * @return JSONObject    返回类型 
	 * @date 2017年4月18日 下午5:24:28
	 */
	public static JSONObject addVehicle(BindCarVo bindCarVo,String url,String method,String userId,String userPwd,String key) throws Exception{
		String xml = null;		
		if(bindCarVo.getBindType()==0){ //0-绑定他人车辆		
			xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><REQUEST><LOGIN_NAME>"+bindCarVo.getUserIdCard()+"</LOGIN_NAME><YHLY>"+bindCarVo.getCertifiedSource()+"</YHLY><HPHM>"+bindCarVo.getLicensePlateNumber()+"</HPHM>"
		            +"<HPZL>"+bindCarVo.getLicensePlateType()+"</HPZL><SFJC>"+bindCarVo.getProvinceAbbreviation()+"</SFJC><CJH4>"+bindCarVo.getFrameNumber()+"</CJH4><CZXM>"+bindCarVo.getOwnerName()+"</CZXM>"
                    +"<CZSFZMHM>"+bindCarVo.getOwnerIdCard()+"</CZSFZMHM><SFBR>0</SFBR><LRIP>"+bindCarVo.getInputIP()+"</LRIP>"
                    +"<BIND_DEPARTMENT>"+bindCarVo.getCertifiedSource()+"</BIND_DEPARTMENT><CZSFZMHMTPA>"+bindCarVo.getIdCardImgPositive()+"</CZSFZMHMTPA>"
                    +"<CZSFZMHMTP>"+bindCarVo.getIdCardImgHandHeld()+"</CZSFZMHMTP></REQUEST>";
		}else if(bindCarVo.getBindType()==1){//1-绑定个人车辆信息
			xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><REQUEST><LOGIN_NAME>"+bindCarVo.getUserIdCard()+"</LOGIN_NAME><YHLY>"+bindCarVo.getCertifiedSource()+"</YHLY><HPHM>"+bindCarVo.getLicensePlateNumber()+"</HPHM>"
		            +"<HPZL>"+bindCarVo.getLicensePlateType()+"</HPZL><SFJC>"+bindCarVo.getProvinceAbbreviation()+"</SFJC><CJH4></CJH4><CZXM></CZXM>"
                    +"<CZSFZMHM></CZSFZMHM><SFBR>1</SFBR><LRIP>"+bindCarVo.getInputIP()+"</LRIP>"
                    +"<BIND_DEPARTMENT>"+bindCarVo.getCertifiedSource()+"</BIND_DEPARTMENT><CZSFZMHMTPA></CZSFZMHMTPA><CZSFZMHMTP></CZSFZMHMTP></REQUEST>";
		}
		String interfaceNumber = "xxcj10";
		JSONObject json = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber,xml,userId,userPwd,key);
		return json;
	}
	
	/**
	 * 
	 * @Title: updateUser 
	 * @author liuminkang
	 * @Description: TODO(修改个人资料) 
	 * @param userBasicVo
	 * @param url
	 * @param method
	 * @param userId
	 * @param userPwd
	 * @param key
	 * @return
	 * @throws Exception    设定文件 
	 * @return JSONObject    返回类型 
	 * @date 2017年4月18日 下午5:29:46
	 */
	public static JSONObject updateUser(UserBasicVo userBasicVo,String url,String method,String userId,String userPwd,String key)throws Exception{
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?><REQUEST><USERNAME>"+userBasicVo.getIdentityCard()+"</USERNAME><NICKNAME>"+userBasicVo.getNickname()+"</NICKNAME>"
				+ "<TXDZ>"+userBasicVo.getMailingAddress()+"</TXDZ><PHOTO9>"+userBasicVo.getIdCardImgPositive()+"</PHOTO9><PHOTO6>"+userBasicVo.getIdCardImgHandHeld()+"</PHOTO6>"
						+ "<SFZYXQ>"+userBasicVo.getIdCardValidityDate()+"</SFZYXQ><YHLY>"+userBasicVo.getUserSource()+"</YHLY></REQUEST>";
		String interfaceNumber = "xxcj05";
		JSONObject json = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber,xml,userId,userPwd,key);
		return json;
	}
	
	/**
	 * 
	 * @Title: updateMobile 
	 * @author liuminkang
	 * @Description: TODO(修改手机号) 
	 * @param userBasicVo
	 * @param url
	 * @param method
	 * @param userId
	 * @param userPwd
	 * @param key
	 * @return
	 * @throws Exception    设定文件 
	 * @return JSONObject    返回类型 
	 * @date 2017年4月18日 下午5:31:05
	 */
	public static JSONObject updateMobile(UserBasicVo userBasicVo,String url,String method,String userId,String userPwd,String key)throws Exception{
		String xml ="<?xml version=\"1.0\" encoding=\"utf-8\"?><REQUEST><LOGIN_USER>"+userBasicVo.getIdentityCard()+"</LOGIN_USER><LXDH>"+userBasicVo.getOldMobile()+"</LXDH>"
				+ "<NEWLXDH>"+userBasicVo.getNewMobile()+"</NEWLXDH><RZJS>"+userBasicVo.getUserSource()+"</RZJS></REQUEST>";
		String interfaceNumber = "xxcj17";
		JSONObject json = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber,xml,userId,userPwd,key);
		return json;
	}
	
	/**
	 * 
	 * @Title: updatePwd 
	 * @author liuminkang
	 * @Description: TODO(修改密码) 
	 * @param userBasicVo
	 * @param url
	 * @param method
	 * @param userId
	 * @param userPwd
	 * @param key
	 * @return
	 * @throws Exception    设定文件 
	 * @return JSONObject    返回类型 
	 * @date 2017年4月18日 下午5:32:44
	 */
	public static JSONObject updatePwd(UserBasicVo userBasicVo,String url,String method,String userId,String userPwd,String key)throws Exception{
		String xml ="<?xml version=\"1.0\" encoding=\"utf-8\"?><REQUEST><USERNAME>"+userBasicVo.getIdentityCard()+"</USERNAME><OLDPWD>"+userBasicVo.getOldPwd()+"</OLDPWD>"
				+ "<NEWPWD>"+userBasicVo.getNewPwd()+"</NEWPWD><YHLY>"+userBasicVo.getUserSource()+"</YHLY></REQUEST>";
		String interfaceNumber = "xxcj04";
		JSONObject json = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber,xml,userId,userPwd,key);
		return json;
	}
	
	/**
	 * 
	 * @Title: readilyShoot 
	 * @author liuminkang
	 * @Description: TODO(随手拍举报) 
	 * @param readilyShootVo
	 * @param url
	 * @param method
	 * @param userId
	 * @param userPwd
	 * @param key
	 * @return
	 * @throws Exception    设定文件 
	 * @return JSONObject    返回类型 
	 * @date 2017年4月18日 下午7:46:40
	 */
	public  static JSONObject readilyShoot(ReadilyShootVo readilyShootVo,String url,String method,String userId,String userPwd,String key) throws Exception {
		String xml=null;	
		if("C".equals(readilyShootVo.getUserSource())){
			xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><request><body><hphm>"+readilyShootVo.getLicensePlateNumber()+"</hphm><hpzl>"+readilyShootVo.getLicensePlateType()+"</hpzl><wfxw1>"+readilyShootVo.getIllegalActivitieOne()+"</wfxw1><wfxw2></wfxw2>"
					+ "<wfxw3></wfxw3><wfdd>"+readilyShootVo.getIllegalSections()+"</wfdd><wfsj>"+readilyShootVo.getIllegalTime()+"</wfsj><lrr>"+readilyShootVo.getInputMan()+"</lrr><lrrxm>"+readilyShootVo.getInputManName()+"</lrrxm>"
					+ "<lrrlxdh>"+readilyShootVo.getInputManPhone()+"</lrrlxdh><lrly>"+"WX02"+"</lrly><jbtp1>"+readilyShootVo.getReportImgOne()+"</jbtp1><jbtp2>"+readilyShootVo.getReportImgTwo()+"</jbtp2><jbtp3>"+readilyShootVo.getReportImgThree()+"</jbtp3>"
					+ "<sfzmhm>"+readilyShootVo.getUserIdCard()+"</sfzmhm><wxopenid>"+readilyShootVo.getOpenId()+"</wxopenid></body></request>";
		}
		if("Z".equals(readilyShootVo.getUserSource())){
			xml="<?xml version=\"1.0\" encoding=\"UTF-8\"?><request><body><hphm>"+readilyShootVo.getLicensePlateNumber()+"</hphm><hpzl>"+readilyShootVo.getLicensePlateType()+"</hpzl><wfxw1>"+readilyShootVo.getIllegalActivitieOne()+"</wfxw1><wfxw2></wfxw2>"
					+ "<wfxw3></wfxw3><wfdd>"+readilyShootVo.getIllegalSections()+"</wfdd><wfsj>"+readilyShootVo.getIllegalTime()+"</wfsj><lrr>"+readilyShootVo.getInputMan()+"</lrr><lrrxm>"+readilyShootVo.getInputManName()+"</lrrxm>"
					+ "<lrrlxdh>"+readilyShootVo.getInputManPhone()+"</lrrlxdh><lrly>"+"Z"+"</lrly><jbtp1>"+readilyShootVo.getReportImgOne()+"</jbtp1><jbtp2>"+readilyShootVo.getReportImgTwo()+"</jbtp2><jbtp3>"+readilyShootVo.getReportImgThree()+"</jbtp3>"
					+ "<sfzmhm>"+readilyShootVo.getUserIdCard()+"</sfzmhm><wxopenid>"+readilyShootVo.getOpenId()+"</wxopenid></body></request>";
		}
		String interfaceNumber = "1003";
		JSONObject json = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber,xml,userId,userPwd,key);
		return json;
	}

	/**
	 * 
	 * @Title: iAmTheOwner 
	 * @author liuminkang
	 * @Description: TODO(车主认证) 
	 * @param registerVo
	 * @param url
	 * @param method
	 * @param userId
	 * @param userPwd
	 * @param key
	 * @return
	 * @throws Exception    设定文件 
	 * @return JSONObject    返回类型 
	 * @date 2017年4月18日 下午7:47:03
	 */
	public static JSONObject iAmTheOwner(RegisterVo registerVo,String url,String method,String userId,String userPwd,String key) throws Exception{
		String xml ="<?xml version=\"1.0\" encoding=\"utf-8\"?><REQUEST><SFZMHM>"+registerVo.getUserIdCard()+"</SFZMHM><LXDH>"+registerVo.getMobilephone()+"</LXDH><LXDZ>"+registerVo.getLinkAddress()+"</LXDZ><HPHM>"+registerVo.getLicensePlateNumber()+"</HPHM>"
				+ "<HPZL>"+registerVo.getLicensePlateType()+"</HPZL><RZLX>1</RZLX><RZLY>C</RZLY><JSRSZD>"+registerVo.getDriverLicenseIssuedAddress()+"</JSRSZD><SFJC>"+registerVo.getProvinceAbbreviation()+"</SFJC>"
				+ "<RZJS>"+registerVo.getCertifiedRole()+"</RZJS><LRR>"+registerVo.getCallAccount()+"</LRR><PHOTO6>"+registerVo.getIdCardImgPositive()+"</PHOTO6><PHOTO9>"+registerVo.getIdCardImgHandHeld()+"</PHOTO9><OPENID>"+registerVo.getOpenId()+"</OPENID></REQUEST>";
		String interfaceNumber = "xxcj15";
		JSONObject json = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber,xml,userId,userPwd,key);
		return json;
	}
	
	/**
	 * 
	 * @Title: iamALongtimeUser 
	 * @author liuminkang
	 * @Description: TODO(长期使用人认证) 
	 * @param registerVo
	 * @param url
	 * @param method
	 * @param userId
	 * @param userPwd
	 * @param key
	 * @return
	 * @throws Exception    设定文件 
	 * @return JSONObject    返回类型 
	 * @date 2017年4月18日 下午7:47:08
	 */
	public static JSONObject iamALongtimeUser(RegisterVo registerVo,String url,String method,String userId,String userPwd,String key) throws Exception {	
		String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?><REQUEST><SFZMHM>"+registerVo.getUserIdCard()+"</SFZMHM><LXDH>"+registerVo.getMobilephone()+"</LXDH><LXDZ>"+registerVo.getLinkAddress()+"</LXDZ>"
					+ "<HPHM>"+registerVo.getLicensePlateNumber()+"</HPHM><HPZL>"+registerVo.getLicensePlateType()+"</HPZL><CZXM>"+registerVo.getOwnerName()+"</CZXM><CZSFZMMC>"+registerVo.getOwnerIdName()+"</CZSFZMMC><CZSFZMHM>"+registerVo.getOwnerIdCard()+"</CZSFZMHM>"
					+ "<CZLXDH>"+registerVo.getOwnerMobilephone()+"</CZLXDH><RZLX>"+registerVo.getCertifiedType()+"</RZLX><RZLY>"+registerVo.getCertifiedSource()+"</RZLY><RZJS>"+registerVo.getCertifiedRole()+"</RZJS>"
					+ "<LRR>"+registerVo.getCallAccount()+"</LRR><JSRSZD>"+registerVo.getDriverLicenseIssuedAddress()+"</JSRSZD><SFJC>"+registerVo.getProvinceAbbreviation()+"</SFJC><PHOTO6>"+registerVo.getIdCardImgHandHeld()+"</PHOTO6>"
					+ "<PHOTO9>"+registerVo.getIdCardImgPositive()+"</PHOTO9><PHOTO16>"+registerVo.getOwnerIdCardImgPositive()+"</PHOTO16><PHOTO18>"+registerVo.getOwnerIdCardImgHandHeld()+"</PHOTO18><OPENID>"+registerVo.getOpenId()+"</OPENID></REQUEST>";
			String interfaceNumber = "xxcj15";
			JSONObject json = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber,xml,userId,userPwd,key);

		return json;
	}
	
	/**
	 * 
	 * @Title: haveDriverLicenseNotCar 
	 * @author liuminkang
	 * @Description: TODO(有驾驶证无固定车认证) 
	 * @param registerVo
	 * @param url
	 * @param method
	 * @param userId
	 * @param userPwd
	 * @param key
	 * @return
	 * @throws Exception    设定文件 
	 * @return JSONObject    返回类型 
	 * @date 2017年4月18日 下午7:47:12
	 */
	public static JSONObject haveDriverLicenseNotCar(RegisterVo registerVo,String url,String method,String userId,String userPwd,String key) throws Exception {				
		String xml="<?xml version=\"1.0\" encoding=\"utf-8\"?><REQUEST><SFZMHM>"+registerVo.getUserIdCard()+"</SFZMHM><LXDH>"+registerVo.getMobilephone()+"</LXDH><LXDZ>"+registerVo.getLinkAddress()+"</LXDZ>"
				+ "<RZLX>"+registerVo.getCertifiedType()+"</RZLX><RZLY>"+registerVo.getCertifiedSource()+"</RZLY><RZJS>"+registerVo.getCertifiedRole()+"</RZJS><LRR>"+registerVo.getCallAccount()+"</LRR>"
				+ "<JSRSZD>"+registerVo.getDriverLicenseIssuedAddress()+"</JSRSZD ><PHOTO6>"+registerVo.getIdCardImgHandHeld()+"</PHOTO6><PHOTO9>"+registerVo.getIdCardImgPositive()+"</PHOTO9><OPENID>"+registerVo.getOpenId()+"</OPENID></REQUEST>";
		String interfaceNumber = "xxcj15";
		JSONObject json = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber,xml,userId,userPwd,key);
		return json;
	}
	
	/**
	 * 
	 * @Title: isPedestrianNotDriver 
	 * @author liuminkang
	 * @Description: TODO(行人认证) 
	 * @param registerVo
	 * @param url
	 * @param method
	 * @param userId
	 * @param userPwd
	 * @param key
	 * @return
	 * @throws Exception    设定文件 
	 * @return JSONObject    返回类型 
	 * @date 2017年4月18日 下午7:47:16
	 */
	public static JSONObject isPedestrianNotDriver(RegisterVo registerVo,String url,String method,String userId,String userPwd,String key)throws Exception {
		String xml ="<?xml version=\"1.0\" encoding=\"utf-8\"?><REQUEST><SFZMHM>"+ registerVo.getUserIdCard()+"</SFZMHM><LXDH>"+registerVo.getMobilephone()+"</LXDH>"
				+ "<RZLX>"+registerVo.getCertifiedType()+"</RZLX><RZLY>C</RZLY><PHOTO6>"+registerVo.getIdCardImgHandHeld()+"</PHOTO6>"
				+ "<PHOTO9>"+registerVo.getIdCardImgPositive()+"</PHOTO9><OPENID>"+registerVo.getOpenId()+"</OPENID></REQUEST>";
		String interfaceNumber = "xxcjzrr";
		JSONObject json = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber,xml,userId,userPwd,key);
		return json;
	}
	
	/**
	 * 根据关键字模糊查询违法地点
	 * @param keyword
	 * @param url
	 * @param method
	 * @param userId
	 * @param userPwd
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getPositioningAddress(String keyword,String url,String method,String userId,String userPwd,String key)throws Exception {		
		String xml ="<?xml version=\"1.0\" encoding=\"utf-8\"?><request><wfdd>"+keyword+"</wfdd></request>";
		String interfaceNumber = "1002";
		JSONObject json = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber,xml,userId,userPwd,key);
		return json;
		
	}
	
	/**
	 * 违法行为的选择项目
	 * @param interfaceNumber
	 * @param url
	 * @param method
	 * @param userId
	 * @param userPwd
	 * @param key
	 * @return
	 * @throws Exception
	 */
	public static JSONObject getTheChoiceOfIllegalActivities(String keyword,String url,String method,String userId,String userPwd,String key)throws Exception {		
		String xml ="<?xml version=\"1.0\" encoding=\"utf-8\"?><request><wfnr>"+keyword+"</wfnr></request>";
		String interfaceNumber = "1001";
		JSONObject json = WebServiceClient.getInstance().requestWebService(url, method, interfaceNumber,xml,userId,userPwd,key);
		return json;
		
	}
	
	
	
}
