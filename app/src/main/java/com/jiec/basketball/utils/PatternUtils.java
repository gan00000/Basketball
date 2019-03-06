package com.jiec.basketball.utils;

public class PatternUtils {
	
	/**检测是否为手机号码*/
	public static final String CHECK_IS_PHONE = "^(13|15|18|14|17)\\d{9}$";
	/**检测该号码是否为中国移动号码*/
	public static final String CHECK_PHONE_IS_CHINA_MOBILE = "^0{0,1}(13[4-9]|15[7-9]|15[0-2]|18[7-8])[0-9]{8}$";

	public static final String CHECK_PASSWORD_ABC_OR_123 = "^[a-zA-Z]{0,1}+[a-zA-Z0-9]{5,13}$";

	/*邮箱正则表达式*/
	public static final String CHECK_IS_EMAIL = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";

}
