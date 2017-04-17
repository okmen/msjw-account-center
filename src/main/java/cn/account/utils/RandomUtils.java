package cn.account.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtils {
	public static boolean isNeedExpire() {
		int rand = (int) (Math.random() * 10);
		return rand < 1;
	}
	/**
	 * 生成验证码
	 * @return
	 */
	public synchronized static String createValidateCode(){
		String validateCode = RandomStringUtils.randomNumeric(6);
		return validateCode;
	}
	
}
