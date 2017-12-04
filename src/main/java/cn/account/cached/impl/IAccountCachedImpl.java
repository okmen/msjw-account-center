package cn.account.cached.impl;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.account.bean.Token;
import cn.account.bean.UserRegInfo;
import cn.account.bean.WechatUserInfoBean;
import cn.account.cached.IAccountCached;
import cn.account.cached.ICacheKey;
import cn.account.config.IConfig;
import cn.sdk.cache.ICacheManger;
import cn.sdk.serialization.ISerializeManager;



@Service
public class IAccountCachedImpl implements IAccountCached{
	protected Logger log = Logger.getLogger(this.getClass());
	/**
	 * 用户id
	 */
	@Value("${useridGd}")
    private String useridGd;
	/**
	 * 用户密码
	 */
    @Value("${userpwdGd}")
    private String userpwdGd;
    /**
     * 请求地址
     */
    @Value("${urlGd}")
    private String urlGd;
    /**
     * 方法
     */
    @Value("${methodGd}")
    private String methodGd;
    /**
     * 秘钥
     */
    @Value("${keyGd}")
    private String keyGd;
	/**
	 * 用户id
	 */
	@Value("${useridApp}")
    private String useridApp;
	/**
	 * 用户密码
	 */
    @Value("${userpwdApp}")
    private String userpwdApp;
    /**
     * 请求地址
     */
    @Value("${urlApp}")
    private String urlApp;
    /**
     * 方法
     */
    @Value("${methodApp}")
    private String methodApp;
    /**
     * 秘钥
     */
    @Value("${keyApp}")
    private String keyApp;
	 /**
     * 请求地址
     */
    @Value("${urlAlipay2}")
    private String urlAlipay2;
	/**
	 * 用户id
	 */
	@Value("${useridAlipay}")
    private String useridAlipay;
	/**
	 * 用户密码
	 */
    @Value("${userpwdAlipay}")
    private String userpwdAlipay;
    /**
     * 请求地址
     */
    @Value("${urlAlipay}")
    private String urlAlipay;
    /**
     * 方法
     */
    @Value("${methodAlipay}")
    private String methodAlipay;
    /**
     * 秘钥
     */
    @Value("${keyAlipay}")
    private String keyAlipay;
    
	/**
	 * 用户id
	 */
	@Value("${userid}")
    private String userid;
	/**
	 * 用户密码
	 */
    @Value("${userpwd}")
    private String userpwd;
    /**
     * 请求地址
     */
    @Value("${url}")
    private String url;
    /**
     * 方法
     */
    @Value("${method}")
    private String method;
    /**
     * 秘钥
     */
    @Value("${key}")
    private String key;
    
    @Value("${refreshTokenTime}")
    private int refreshTokenTime;
    
    @Value("${encyptAccessTokenTime}")
    private int encyptAccessTokenTime;
    
    @Value("${accessTokentime}")
    private int accessTokenTime;
    
    /**
     * 芝麻信用参数
     */
    @Value("${zmxygatewayUrl}")
    private String gatewayUrl;
    @Value("${zmxyappId}")
    private String appId;
    @Value("${zmxyprivateKey}")
    private String privateKey;
    @Value("${zmxyzhimaPublicKey}")
    private String zhimaPublicKey;
    
	public String getGatewayUrl() {
		return gatewayUrl;
	}
	public String getAppId() {
		return appId;
	}
	public String getPrivateKey() {
		return privateKey;
	}
	public String getZhimaPublicKey() {
		return zhimaPublicKey;
	}


	@Autowired
	@Qualifier("jedisCacheManagerImpl")
	private ICacheManger<String> cacheManger;
	
	@Autowired
	@Qualifier("jedisCacheManagerImpl")
	private ICacheManger<Object> objectcacheManger;
	
	@Autowired
	private ISerializeManager< Map<String, String> > serializeManager;
	
    public static final String arrayToString(byte[] bytes)
    {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < bytes.length; i++)
        {
            buff.append(bytes[i] + " ");
        }
        return buff.toString();
    }
    
    
    @Override
    public boolean setWechatUserInfoBean(long id, WechatUserInfoBean wechatUserInfoBean){
    	String userRedisKey = USER_WECHAT_INFO_REDIS_KEY + id;
		return objectcacheManger.setByKryo(userRedisKey, wechatUserInfoBean, exprieTime);
    }
	
    @Override
	public WechatUserInfoBean getWechatUserInfoBean(long id){
    	String userRedisKey = USER_WECHAT_INFO_REDIS_KEY + id;
    	return (WechatUserInfoBean) objectcacheManger.getByKryo(userRedisKey, WechatUserInfoBean.class);
	}
	
	

	@Override
	public boolean setUser(long userId, UserRegInfo user) {
		String userRedisKey = USER_REDIS_KEY + userId;
		return objectcacheManger.setByKryo(userRedisKey, user, exprieTime);
	}

	@Override
	public UserRegInfo getUser(long userId) {
		String userRedisKey = USER_REDIS_KEY + userId;
		return (UserRegInfo) objectcacheManger.getByKryo(userRedisKey, UserRegInfo.class);
	}
	
	public Token insertToken(Token token) {
	    cacheManger.set(IConfig.USER_ACCOUNT_ACCESS_TOKEN_REDIS + token.getUserId(), token.getAccessToken(), accessTokenTime);
        cacheManger.set(IConfig.USER_ACCOUNT_REFRESH_TOKEN_REDIS + token.getUserId(), token.getRefreshToken(), refreshTokenTime);
        return token;
    }
    
    public String deleteToken(String userId) {
        cacheManger.del(IConfig.USER_ACCOUNT_ACCESS_TOKEN_REDIS + userId);
        cacheManger.del(IConfig.USER_ACCOUNT_REFRESH_TOKEN_REDIS + userId);
        return "success";
    }   
    
    public Token getToken(String userId)
    {   
        Token token = new Token();
        String accessToken = cacheManger.get(IConfig.USER_ACCOUNT_ACCESS_TOKEN_REDIS + userId);
        String refreshToken = cacheManger.get(IConfig.USER_ACCOUNT_REFRESH_TOKEN_REDIS + userId);
        token.setUserId(userId);
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        return token;
    }
    
    public Token updateAllToken(String userId)
    {
        Token token = getToken(userId);
        cacheManger.set(IConfig.USER_ACCOUNT_ACCESS_TOKEN_REDIS + token.getUserId(), token.getAccessToken(), accessTokenTime);
        cacheManger.set(IConfig.USER_ACCOUNT_REFRESH_TOKEN_REDIS + token.getUserId(), token.getRefreshToken(), refreshTokenTime);      
        return token;
    }
    
    public void updateAccessToken(String userId, String accessToken)
    {
        cacheManger.set(IConfig.USER_ACCOUNT_ACCESS_TOKEN_REDIS + userId, accessToken, accessTokenTime);
    }
    public void updateRefreshToken(String userId, String refreshToken)
    {
        cacheManger.set(IConfig.USER_ACCOUNT_REFRESH_TOKEN_REDIS + userId, refreshToken, refreshTokenTime);        
    }
    
    @Override
    public void insertEncyptAccessToken(String encyptAccessToken, String AccessToken) {
        if(StringUtils.isNotBlank(encyptAccessToken) && StringUtils.isNotBlank(AccessToken)){
            cacheManger.set(String.format(IConfig.ENCYPT_ACCESS_TOKEN_REDIS_KEY, encyptAccessToken), AccessToken, encyptAccessTokenTime);          
        }
    }

    @Override
    public String getAccessTokenFromEncypt(String encyptAccessToken) {
        if(StringUtils.isNotBlank(encyptAccessToken)){
            return cacheManger.get(String.format(IConfig.ENCYPT_ACCESS_TOKEN_REDIS_KEY, encyptAccessToken), encyptAccessTokenTime);            
        }else{
            return null;
        }
    }
    @Override
	public void insertUserValidateCode(String mobilephone, String validateCode) {
    	cacheManger.set(mobilephone, validateCode, ICacheKey.USER_VALIDATE_CODE);
	}
    @Override
    public void sendSmsFreqLimit(String mobilephone){
    	cacheManger.set(mobilephone + "_limit", mobilephone, ICacheKey.SEND_FREQ_LIMIT);
    }

    @Override
	public String getSendSmsFreqLimit(String mobilephone) {
		return cacheManger.get(mobilephone + "_limit");
	}
	@Override
	public String getUserValidateCode(String mobilephone) {
		return cacheManger.get(mobilephone);
	}

	public String getUserid(String sourceOfCertification) {
		String string = "";
		if("C".equals(sourceOfCertification)){
			string = userid;
		}else if("Z".equals(sourceOfCertification)){
			string = useridAlipay;
		}else if("A".equals(sourceOfCertification)){
			string = useridApp;
		}else if("G".equals(sourceOfCertification)){
			string = useridGd;
		}else {
			string = userid;
		}
		return string;
	}


	public String getUserpwd(String sourceOfCertification) {
		String string = "";
		if("C".equals(sourceOfCertification)){
			string = userpwd;
		}else if("Z".equals(sourceOfCertification)){
			string = userpwdAlipay;
		}else if("A".equals(sourceOfCertification)){
			string = userpwdApp;
		}else if("G".equals(sourceOfCertification)){
			string = userpwdGd;
		}else {
			string = userpwd;
		}
		return string;
	}


	public String getUrl(String sourceOfCertification) {
		String string = "";
		if("C".equals(sourceOfCertification)){
			string = url;
		}else if("Z".equals(sourceOfCertification)){
			string = urlAlipay;
		}else if("A".equals(sourceOfCertification)){
			string = urlApp;
		}else if("A".equals(sourceOfCertification)){
			string = urlGd;
		}else {
			string = url;
		}
		return string;
	}
	public String getUrl2(String sourceOfCertification) {
		String string = "";
		if("C".equals(sourceOfCertification)){
			string = url;
		}else if("Z".equals(sourceOfCertification)){
			string = urlAlipay2;
		}else {
			string = url;
		}
		return string;
	}

	public String getMethod(String sourceOfCertification) {
		String string = "";
		if("C".equals(sourceOfCertification)){
			string = method;
		}else  if("Z".equals(sourceOfCertification)){
			string = methodAlipay;
		}else  if("A".equals(sourceOfCertification)){
			string = methodApp;
		}else  if("G".equals(sourceOfCertification)){
			string = methodGd;
		}else {
			string = method;
		}
		return string;
	}


	public String getKey(String sourceOfCertification) {
		String string = "";
		if("C".equals(sourceOfCertification)){
			string = key;
		}else if("Z".equals(sourceOfCertification)){
			string = keyAlipay;
		}else if("A".equals(sourceOfCertification)){
			string = keyApp;
		}else if("G".equals(sourceOfCertification)){
			string = keyGd;
		}else {
			string = key;
		}
		return string;
	}


	@Override
	public String getDocumentByKey(String key) {
		return cacheManger.get(key);
	}


	@Override
	public boolean setDucoment(String key, String value) {
		return cacheManger.set(key, value);
	}

}
