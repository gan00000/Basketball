package com.jiec.basketball.utils;


import android.content.Context;

import com.jiec.basketball.core.BallApplication;
import com.wangcj.common.utils.BasePreferenceUtils;


public class BallPreferencesUtils extends BasePreferenceUtils {

    private static final String SP_NAME = "preferences_youxin";
    private static BallPreferencesUtils mGlobalInfoHelper;

    private BallPreferencesUtils(Context context, String prefname) {
        super(context, prefname);
    }

    public static BallPreferencesUtils getInstance() {
        if (mGlobalInfoHelper == null) {
            synchronized (BallPreferencesUtils.class) {
                if (mGlobalInfoHelper == null) {
                    Context context = BallApplication.getContext();
                    mGlobalInfoHelper = new BallPreferencesUtils(context, SP_NAME);
                }
            }
        }
        return mGlobalInfoHelper;
    }

    public void setLoginType(int type) {
        putInt(Keys.LOGIN_TYPE, type);
    }

    public int getLoginType() {
        return getInt(Keys.LOGIN_TYPE, 0);
    }

    public void setAutoReceiveMsgNotify(boolean autoReceive) {
        putBoolean(Keys.RECEIVE_MSG_NOTIFY, autoReceive);
    }

    public boolean getAutoReceiveMsgNotify() {
        return getBoolean(Keys.RECEIVE_MSG_NOTIFY, true);
    }

    public interface Keys {
        String LOGIN_TYPE = "is_line_login";

        //是否接受消息通知
        public static String RECEIVE_MSG_NOTIFY = "receive_msg_notify";

    }

}
