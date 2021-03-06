package com.jiec.basketball.core;

import android.content.Context;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.blankj.utilcode.util.Utils;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.ads.MobileAds;
import com.jiec.basketball.bean.UserInfoBean;
import com.jiec.basketball.dao.UserDao;
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

        MobileAds.initialize(this);

        Utils.init(this);
        ToastUtil.init(sContext);
        initUserInfo(getApplicationContext());

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        MultiDex.install(this);

//        UMConfigure.init(getApplicationContext(), "5c9f28fb61f56499e7000bf4", "Umeng",
//                UMConfigure.DEVICE_TYPE_PHONE, "ddccf36528b2f5a842ba15f4fae2d327");
//
//        PushAgent mPushAgent = PushAgent.getInstance(this);
//        //注册推送服务，每次调用register方法都会回调该接口
//        mPushAgent.register(new IUmengRegisterCallback() {
//            @Override
//            public void onSuccess(String deviceToken) {
//                //注册成功会返回deviceToken deviceToken是推送消息的唯一标志
//                Log.i(TAG, "注册成功：deviceToken：-------->  " + deviceToken);
//                UserManager.instance().setDeviceToke(deviceToken);
//            }
//
//            @Override
//            public void onFailure(String s, String s1) {
//                Log.e(TAG, "注册失败：-------->  " + "s:" + s + ",s1:" + s1);
//            }
//        });
//
//        UmengMessageHandler messageHandler = new UmengMessageHandler() {
//
//            @Override
//            public void dealWithNotificationMessage(Context context, UMessage uMessage) {
//                super.dealWithNotificationMessage(context, uMessage);
//            }
//
//            @Override
//            public void dealWithCustomMessage(Context context, UMessage uMessage) {
//                super.dealWithCustomMessage(context, uMessage);
//            }
//
//            @Override
//            public Notification getNotification(Context context, UMessage msg) {
//                switch (msg.builder_id) {
//                    case 1:
//                        LogUtils.e("4478889");
//                        Notification.Builder builder = new Notification.Builder(context);
//                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
//                                R.layout.notification_view);
//                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
//                        myNotificationView.setImageViewResource(R.id.notification_large_icon, R.mipmap.ic_launcher);
//                        builder.setContent(myNotificationView);
//                        Notification   motification = builder.build();
//                        motification.contentView = myNotificationView;
//                        return motification;
//                    default:
//                        //默认为0，若填写的builder_id并不存在，也使用默认。
//                        return super.getNotification(context, msg);
//                }
//            }
//        };
//        mPushAgent.setMessageHandler(messageHandler);
//
//        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
//            @Override
//            public void dealWithCustomAction(Context context, UMessage msg) {
//            }
//
//            @Override
//            public void launchApp(Context context, UMessage uMessage) {
////                super.launchApp(context, uMessage);
//                String postId = "";
//                for (Map.Entry entry : uMessage.extra.entrySet()) {
//                    String key = (String) entry.getKey();
//                    postId = (String) entry.getValue();
//                }
//                LogUtils.e(postId);
//                if( !EmptyUtils.emptyOfString(postId)){
//                    if(AppUtils.isAppForeground()){
//                        EventBus.getDefault()
//                                .post(new EventBusEvent(ConstantUtils.EVENT_NOTIFICATION, BallApplication.class, postId));
//                    }else {
//                        DetaillWebActivity.show(context, postId, 10);
//                    }
//                }
//
//            }
//        };
//        mPushAgent.setNotificationClickHandler(notificationClickHandler);

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
