package cn.account.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import cn.account.bean.DrivingLicense;
import cn.account.bean.vo.ReadilyShootVo;
import cn.account.cached.impl.IAccountCachedImpl;
import cn.account.service.IThirdPartyInformationService;
import cn.account.utils.NozzleMeans;
@Service("thirdPartyInformationService")
@SuppressWarnings(value="all")
public class ThirdPartyInformationServiceImpl implements IThirdPartyInformationService{

    private final Logger logger = LoggerFactory.getLogger(getClass());
    
	@Autowired
	private IAccountCachedImpl iAccountCached;
	
	@Override
	public JSONObject readilyShoot(ReadilyShootVo readilyShootVo) throws Exception {
		JSONObject json= null;
		try {
			String string = readilyShootVo.getUserSource();
			 String url = iAccountCached.getUrl(string); //webservice请求url
			 String method = iAccountCached.getMethod(string); //webservice请求方法名称
			 String userId = iAccountCached.getUserid(string); //webservice登录账号
			 String userPwd = iAccountCached.getUserpwd(string); //webservice登录密码
			 String key = iAccountCached.getKey(string); //秘钥
			 json = NozzleMeans.readilyShoot(readilyShootVo, url, method, userId, userPwd, key);
		} catch (Exception e) {
			logger.error("ReadilyShootVo出错，错误="+ readilyShootVo.toString(),e);
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
	public List<DrivingLicense> getLicensePlateTypes() {
		 List<DrivingLicense> drivingLicenses = new ArrayList<DrivingLicense>();
		 DrivingLicense drivingLicense1 = new DrivingLicense("02", "蓝牌");
		 DrivingLicense drivingLicense2 = new DrivingLicense("01", "黄牌");
		 DrivingLicense drivingLicense3 = new DrivingLicense("06", "黑牌");
		 DrivingLicense drivingLicense4 = new DrivingLicense("02", "个性牌");
		 DrivingLicense drivingLicense5 = new DrivingLicense("02", "小型新能源车号牌");
		 DrivingLicense drivingLicense6 = new DrivingLicense("02", "大型新能源车号牌");
		 drivingLicenses.add(drivingLicense1);
		 drivingLicenses.add(drivingLicense2);
		 drivingLicenses.add(drivingLicense3);
		 drivingLicenses.add(drivingLicense4);
		 drivingLicenses.add(drivingLicense5);
		 drivingLicenses.add(drivingLicense6);
		 return drivingLicenses;
	}
	
}
