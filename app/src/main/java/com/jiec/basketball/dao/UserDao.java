package com.jiec.basketball.dao;

import android.content.Context;
import android.content.SharedPreferences;

import com.blankj.utilcode.util.SPUtils;
import com.google.gson.Gson;
import com.jiec.basketball.bean.UserInfoBean;
import com.jiec.basketball.core.BallApplication;
import com.jiec.basketball.utils.AesEncryUtils;


/**
 * Created by Deng
 */
public class UserDao {

    private static final String SP_UINFO = "SP_USER_INFO";
    private static final String A_KEY = "asdoiausdfhsasdjaoidsjaiospdajispodg";
    private static final String P_KEY = "@2q34345$%^*(*%*(jduipogndklbhfcljkrfsdlkjf.smkdf;21893ql243nqrlu8";
    private static final String U_KEY = "q34345$%^*(*%*(jduipogndklbhfcljkrfsdlkjf.smkdf;21893";


    private static UserDao instance;

    private UserDao() {
    }


    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }


    /**
     * 注销登录
     */
    public void exitLogin(Context mContext) {
        SharedPreferences sp_login = mContext.getSharedPreferences(SP_UINFO, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp_login.edit();
        editor.clear();
        editor.commit();
        BallApplication.userInfo = null;
    }


    /**
     * 简单加密
     *
     * @param encodeContent
     * @return
     * @throws Exception
     */
    private String simpleEncodeString(String encodeContent) throws Exception {
        if (encodeContent != null) {
            return AesEncryUtils.encrypt(U_KEY, encodeContent);
        }

        return encodeContent;
    }


    /**
     * 解密
     *
     * @param decodeContent
     * @return
     * @throws Exception
     */
    private String simpleDecodeString(String decodeContent) throws Exception {
        if (decodeContent != null) {
            return AesEncryUtils.decrypt(U_KEY, decodeContent);
        }
        return decodeContent;
    }


    /**
     * 保存加密用户信息
     *
     * @param mContext 上下文对象
     */
    public void encodeAndSaveUserInfo(Context mContext, UserInfoBean userInfo) {
        try {
            SharedPreferences sp = mContext.getSharedPreferences(SP_UINFO, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("user_token", simpleEncodeString(userInfo.user_token));
            editor.putString("user_id", simpleEncodeString(userInfo.user_id));
            editor.putString("display_name", simpleEncodeString(userInfo.display_name));
            editor.putString("user_email", simpleEncodeString(userInfo.user_email));
            editor.putString("user_status", simpleEncodeString(userInfo.user_status));
            editor.putString("user_img", simpleEncodeString(userInfo.user_img));
            editor.putString("change_name", simpleEncodeString(userInfo.change_name));
            editor.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取解密本地用户信息
     *
     * @param mContext 上下文对象
     */
    public UserInfoBean decodeUserInfo(Context mContext) {
        try {
            UserInfoBean userInfo = new UserInfoBean();
            SharedPreferences sp = mContext.getSharedPreferences(SP_UINFO, Context.MODE_PRIVATE);
            String user_token =  simpleDecodeString(sp.getString("user_token", null));
            String user_id = simpleDecodeString(sp.getString("user_id", null));
            if(user_token == null || user_id == null){
                return  null;
            }
            userInfo.user_token = user_token;
            userInfo.user_id = user_id;
            userInfo.display_name = simpleDecodeString(sp.getString("display_name", ""));
            userInfo.user_email = simpleDecodeString(sp.getString("user_email", ""));
            userInfo.user_status = simpleDecodeString(sp.getString("user_status", ""));
            userInfo.user_img = simpleDecodeString(sp.getString("user_img", ""));
            userInfo.change_name = simpleDecodeString(sp.getString("change_name", ""));

            return userInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }













}
