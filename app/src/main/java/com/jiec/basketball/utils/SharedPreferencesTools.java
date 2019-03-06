package com.jiec.basketball.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.google.gson.Gson;

/**
 * SharedPreferences 存储类
 * @author Single
 * @version 1.2
 */
public class SharedPreferencesTools {

	private final static String DEFAULT_FILE_NAME = SharedPreferencesTools.class.getName();
	private static Context mContext;
	private static boolean saveInDefault = true;
	private static String fileName;
	private static Gson mGson = new Gson();

	/**
	 * 初始化
	 * @param mContext
	 */
	public static void initConfig(Context mContext)
	{
		SharedPreferencesTools.mContext = mContext;
	}


	public static Editor getEditor()
	{
		return getDefault().edit();
	}


	public static SharedPreferences getDefault()
	{
		if(saveInDefault)
		{
		   return mContext.getSharedPreferences(DEFAULT_FILE_NAME,Context.MODE_PRIVATE);
		}
		else
		{
			return mContext.getSharedPreferences(fileName,Context.MODE_PRIVATE);
		}
	}


	public static void putString(String key,String value)
	{
	    if(!EmptyUtils.emptyOfString(key))
	    {
			getEditor().putString(key, value).commit();
	    }
	    else
	    {
	       showKeyNullPointException();
	    }
	}


	public static void putInt(String key,int value)
	{
		if(!EmptyUtils.emptyOfString(key))
		{
			getEditor().putInt(key, value).commit();
		}
		else
		{
			showKeyNullPointException();
		}
	}


	public static void putLong(String key,long value)
	{
		if(!EmptyUtils.emptyOfString(key))
		{
			getEditor().putLong(key, value).commit();
		}
		else
		{
			showKeyNullPointException();
		}
	}


	public static void putBoolean(String key,boolean value)
	{
		if(!EmptyUtils.emptyOfString(key))
		{
			getEditor().putBoolean(key, value).commit();
		}
		else
		{
			showKeyNullPointException();
		}
	}


	public static void putFloat(String key,float value)
	{
		if(!EmptyUtils.emptyOfString(key))
		{
			getEditor().putFloat(key, value).commit();
		}
		else
		{
			showKeyNullPointException();
		}
	}


	public static void putStringArray(String key, String[] values)
	{
		  if(values!=null)
		  {
		      String json = mGson.toJson(values);
		      putString(key,json);
		  }
	}


	public static void putIntArray(String key, Integer[] values) {


	}


	public static void putBooleanArray(String key, Boolean[] values) {


	}


	public static void putLongArray(String key, Long[] values) {


	}


	public static void putFloatArray(String key, Float[] values) {


	}


	public static String getString(String key)
	{
		if(!EmptyUtils.emptyOfString(key))
		{
		   return getDefault().getString(key,null);
		}
		else
		{
			showKeyNullPointException();
		}
		return null;
	}


	public static int getInt(String key) {

		return 0;
	}


	public static boolean getBoolean(String key) {

		return false;
	}


	public static long getLong(String key) {

		return 0;
	}


	public static String[] getStringArray(String key) {

		return null;
	}


	public static int[] getIntArray(String key) {

		return null;
	}


	public static Float[] getFloatArray(String key) {

		return null;
	}


	public static Boolean[] getBooleanArray(String key) {

		return null;
	}


	public static Long[] getLongArray(String key) {

		return null;
	}


	public static void setPriorityByDefault(boolean saveInDefault)
	{
		SharedPreferencesTools.saveInDefault = saveInDefault;
	}


	public static void setFileName(String fileName)
	{
		SharedPreferencesTools.fileName = fileName;
	}

	/**
	 * 抛出Key不能为空的空指针异常
	 */
	private static void showKeyNullPointException()
	{
		throw new NullPointerException("Key must be not null !");
	}

}
