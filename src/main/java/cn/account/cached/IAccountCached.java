package cn.account.cached;

import cn.account.bean.Token;
import cn.account.bean.UserRegInfo;
import cn.account.bean.WechatUserInfoBean;

public interface IAccountCached extends ICacheKey {
	
	
	public boolean setWechatUserInfoBean(long id, WechatUserInfoBean wechatUserInfoBean);
	
	public WechatUserInfoBean getWechatUserInfoBean(long id);
	

	public boolean setUser(long userId, UserRegInfo user);

	public UserRegInfo getUser(long userId);
	
	
	/**
     * 插入token信息
     */
    public Token insertToken(Token token);
    /**
     * 删除token信息
     * @param userId
     * @return
     */
    public String deleteToken(String userId);
    /**
     * 获取token
     * @param userId
     * @return
     */
    public Token getToken(String userId);
    /**
     * 更新token，延长token的有效时间
     * @param userId
     * @return
     */
    public Token updateAllToken(String userId);
    /**
     * 更新accessToken
     * @param userId
     * @param accessToken
     * @return
     */
    public void updateAccessToken(String userId, String accessToken);
    /**
     * 更新refreshToken
     * @param userId
     * @param refreshToken
     * @return
     */
    public void updateRefreshToken(String userId, String refreshToken);
    
    /**
     * 插入加密token
     * @param userId
     */
    public void insertEncyptAccessToken(String encyptAccessToken, String AccessToken);
    /**
     * 获取加密token
     * @param userId
     * @return
     */
    public String getAccessTokenFromEncypt(String encyptAccessToken);
    
    /**
     * 添加用户验证码到redis
     * @param mobilephone
     * @param validateCode
     */
    public void insertUserValidateCode(String mobilephone,String validateCode);
    /**
     * 根据手机号从redis取验证码
     * @param mobilephone
     * @return
     */
    public String getUserValidateCode(String mobilephone);
}
