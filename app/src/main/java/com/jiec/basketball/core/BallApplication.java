package com.jiec.basketball.core;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.jiec.basketball.bean.UserInfoBean;
import com.jiec.basketball.dao.UserDao;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.wangcj.common.utils.ToastUtil;

/**
 * Created by Administrator on 2017/1/6.
 */
public class BallApplication extends MultiDexApplication {

    private static final String TAG = "BallApplication";
    public static UserInfoBean userInfo;
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();

        sContext = this;
        Utils.init(this);
        ToastUtil.init(sContext);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        MultiDex.install(this);

        UMConfigure.init(getApplicationContext(), "587875f965b6d64f8f000e28", "Umeng",
                UMConfigure.DEVICE_TYPE_PHONE, "11986fc6b303d395b8be8a3c61ece144");

        PushAgent mPushAgent = PushAgent.getInstance(this);
        //注册推送服务，每次调用register方法都会回调该接口
        mPushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
                Log.i(TAG, "注册成功：deviceToken：-------->  " + deviceToken);
                UserManager.instance().setDeviceToke(deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e(TAG, "注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });

        initUserInfo(getApplicationContext());
    }

    /**
     * 初始化用户信息
     * @param applicationContext
     */
    private void initUserInfo(Context applicationContext) {
        userInfo = UserDao.getInstance().decodeUserInfo(applicationContext);
    }

    public static Context getContext() {
        return sContext;
    }
}
