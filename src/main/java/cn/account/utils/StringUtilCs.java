package cn.account.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class StringUtilCs {
	public static boolean isNeedExpire() {
		int rand = (int) (Math.random() * 10);
		return rand < 1;
	}
	
	
}
