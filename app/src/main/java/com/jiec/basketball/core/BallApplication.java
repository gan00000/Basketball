package com.jiec.basketball.core;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.Utils;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.jiec.basketball.R;
import com.jiec.basketball.bean.UserInfoBean;
import com.jiec.basketball.dao.UserDao;
import com.jiec.basketball.ui.news.detail.DetaillWebActivity;
import com.jiec.basketball.utils.ConstantUtils;
import com.jiec.basketball.utils.EventBusEvent;
import com.jiec.basketball.utils.EventBusUtils;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.message.inapp.UmengSplashMessageActivity;
import com.wangcj.common.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

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
        initUserInfo(getApplicationContext());

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        MultiDex.install(this);

        UMConfigure.init(getApplicationContext(), "5c9f28fb61f56499e7000bf4", "Umeng",
                UMConfigure.DEVICE_TYPE_PHONE, "ddccf36528b2f5a842ba15f4fae2d327");

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

        UmengMessageHandler messageHandler = new UmengMessageHandler() {

            @Override
            public void dealWithNotificationMessage(Context context, UMessage uMessage) {
                super.dealWithNotificationMessage(context, uMessage);
            }

            @Override
            public Notification getNotification(Context context, UMessage msg) {

                switch (msg.builder_id) {
                    case 1:
                        LogUtils.e("4478889");
                        Notification.Builder builder = new Notification.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
                                R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setImageViewResource(R.id.notification_large_icon, R.mipmap.ic_launcher);
                        builder.setContent(myNotificationView);
                        Notification   motification = builder.build();
                        motification.contentView = myNotificationView;
                        return motification;
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
            }

            @Override
            public void launchApp(Context context, UMessage uMessage) {
//                super.launchApp(context, uMessage);
                LogUtils.e(uMessage.text);
                if(AppUtils.isAppForeground()){
                    EventBus.getDefault()
                            .post(new EventBusEvent(ConstantUtils.EVENT_NOTIFICATION, BallApplication.class, uMessage.text));
                }else {
                    DetaillWebActivity.show(context, uMessage.text, 10);
                }
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

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
