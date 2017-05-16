package cn.account.cached;

public interface ICacheKey {
	public int exprieTime = 15 * 24 * 3600;        // redis过期时间全部为15天
	public static final int USER_INFO_REDIS_CACHE_TIME = 15 * 24 * 3600;  //用户信息 redis过期时间全部为15天
	public int maxExpireTime = 30 * 24 * 3600; 
	public static String cachePrefix = "USER_ACCOUNT_";
	public static String USER_INFO_REDIS_KEY = cachePrefix + "userInfo_";
	public static String USER_REDIS_KEY = cachePrefix + "user_";
	public static String USER_WECHAT_INFO_REDIS_KEY = cachePrefix + "wechat_";
	public static int USER_VALIDATE_CODE = 300; //短信验证码有效期
	public static int SEND_FREQ_LIMIT = 5; //短信发送频率限制5秒发一条
	public static String ACCOUNT_DOC = "ACCOUNT_DOC_"; //须知
}
