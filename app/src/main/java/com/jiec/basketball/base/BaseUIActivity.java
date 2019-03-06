package com.jiec.basketball.base;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.wangcj.common.utils.ActivityUtil;

import java.lang.ref.WeakReference;

/**
 * function：提供刷新UI的Handler的父Activity
 * Created by jiec on 2017/3/19.
 */

public abstract class BaseUIActivity extends BaseActivity {
    private Handler mUiHandler = new UiHandler(this);

    private static class UiHandler extends Handler {
        private final WeakReference<BaseUIActivity> mActivityReference;

        public UiHandler(BaseUIActivity activity) {
            mActivityReference = new WeakReference<BaseUIActivity>(activity);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaseUIActivity activity = mActivityReference.get();
            if (activity != null && !ActivityUtil.isFinishingOrDestroyed(activity)) {
                activity.handleUiMessage(msg);
            }
        }
    }

    /**
     * 处理更新UI任务
     *
     * @param msg
     */
    public void handleUiMessage(Message msg) {
    }

    public void handleBroadcast(Context context, Intent intent) {
    }

    /**
     * 发送UI更新操作
     *
     * @param msg
     */
    protected void sendUiMessage(Message msg) {
        mUiHandler.sendMessage(msg);
    }

    protected void sendUiMessageDelayed(Message msg, long delayMillis) {
        mUiHandler.sendMessageDelayed(msg, delayMillis);
    }

    /**
     * 发送UI更新操作
     *
     * @param what
     */
    protected void sendEmptyUiMessage(int what) {
        mUiHandler.sendEmptyMessage(what);
    }

    protected void sendEmptyUiMessageDelayed(int what, long delayMillis) {
        mUiHandler.sendEmptyMessageDelayed(what, delayMillis);
    }

    protected void removeUiMessages(int what) {
        mUiHandler.removeMessages(what);
    }

    protected void removeUiAllMsg(){mUiHandler.removeCallbacksAndMessages(null); }

    protected Message obtainUiMessage() {
        return mUiHandler.obtainMessage();
    }

}
