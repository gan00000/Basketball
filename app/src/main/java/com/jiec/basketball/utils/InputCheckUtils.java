package com.jiec.basketball.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 输入检测工具类
 *
 */
public class InputCheckUtils {
	
	/**
	 * 检测输入的文本是否为空
	 * @param input
	 * @return
	 */
	public static boolean isEmpty(String input)
	{
		if(input==null || input.length()<=0)
			return true;
		else
			return false;
	}
	
	/**
	 * 检测输入的文本是否为手机号码
	 * @param input
	 * @return true-是手机号码   false-非手机号码
	 */
	public static boolean checkInputIsPhoneNumber(String input)
	{
		Pattern pattern = Pattern.compile(PatternUtils.CHECK_IS_PHONE);
		Matcher matcher = pattern.matcher(input);

		if(matcher.matches())
			return true;
		else
			return false;
	}

	/**
	 * 匹配两个字符串是否相同
	 * @param input1
	 * @param input2
	 * @return
	 */
	public static boolean compareIsEqual(String input1,String input2)
	{
		if(input1==null || input2 == null)
		{
			return false;
		}
		else
		{
			if(input1.equals(input2))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * 检测字符串长度是否符合范围
	 * @param input
	 * @param minRange
	 * @param maxRange
	 * @return
	 */
	public static boolean checkInputRangeIsConform(String input,int minRange,int maxRange)
	{
		if(input == null)
		{
			return false;
		}

		int length = input.length();

		if(length>=minRange && length <= maxRange)
		{
			return true;
		}

		return false;
	}

	/**
	 * 检测字符串是否等于目标长度
	 * @param input
	 * @param range
	 * @return
	 */
	public static boolean checkInputRangeIsConform(String input,int range)
	{
	     if(input == null)
	     {
	    	 return false;
	     }

	     if(input.length() == range)
	     {
	    	 return true;
	     }

	     return false;
	}

	/**
	 * 检测手机号码是否为移动号码
	 * @param input
	 * @return
	 */
	public static boolean checkPhoneIsChinaMobile(String input)
	{
		if(input == null)
		{
			return false;
		}

		if(input.matches(PatternUtils.CHECK_PHONE_IS_CHINA_MOBILE))
		{
			return true;
		}
		else
		{
			return false;
		}
	}


	/**
	 * 检测是否为邮箱
	 * @param input
	 * @return
	 */
	public static boolean checkInputIsEmail(String input)
	{
		Pattern pattern = Pattern.compile(PatternUtils.CHECK_IS_EMAIL);
		Matcher matcher = pattern.matcher(input);
		if(matcher.matches())
			return true;
		else
			return false;
	}



}
