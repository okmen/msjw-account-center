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

	public String getUserid() {
		return userid;
	}


	public String getUserpwd() {
		return userpwd;
	}


	public String getUrl() {
		return url;
	}


	public String getMethod() {
		return method;
	}


	public String getKey() {
		return key;
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
