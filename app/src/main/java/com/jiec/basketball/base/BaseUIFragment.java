
package com.jiec.basketball.base;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.inputmethod.InputMethodManager;

import java.lang.ref.WeakReference;

/**
 * 描述:提供刷新UI的Handler的父Fragment
 *
 *Created by jiec on 2017/5/5.
 */
public abstract class BaseUIFragment extends BaseFragment {

    protected Handler mUiHandler = new UiHandler(this);

    private static class UiHandler extends Handler {
        private final WeakReference<BaseUIFragment> mFragmentReference;

        public UiHandler(BaseUIFragment fragment) {
            mFragmentReference = new WeakReference<BaseUIFragment>(fragment);
        }

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            BaseUIFragment fragment = mFragmentReference.get();
            if (fragment != null && !fragment.isDetached()) {
                fragment.handleUiMessage(msg);
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

    protected void removeUiMessage(int what) {
        mUiHandler.removeMessages(what);
    }

    protected void removeUiAllMsg(){mUiHandler.removeCallbacksAndMessages(null); }

    protected Message obtainUiMessage() {
        return mUiHandler.obtainMessage();
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput(Context context) {
        InputMethodManager manager = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        // manager.toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS, 0);
        if (getActivity().getCurrentFocus() != null && manager != null) {
            manager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    /**
     * 显示软键盘
     */
    protected void showSoftInput() {
        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

}
