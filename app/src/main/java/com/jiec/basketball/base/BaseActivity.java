package com.jiec.basketball.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.inputmethod.InputMethodManager;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.jiec.basketball.ui.dialog.LoadingDialog;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.android.ActivityEvent;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;
import com.umeng.message.PushAgent;
import com.wangcj.common.utils.ToastUtil;

/**
 * 实现基本方法：加载对话框，获取生命周期转换器，友盟统计
 * Created by jiec on 2017/5/31.
 */

public class BaseActivity extends RxAppCompatActivity {

    private LoadingDialog mLoadingDialog;

    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "1");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "onResume");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "image");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
    }


    @Override
    protected void onDestroy() {
        hideSoftInput(this);
        super.onDestroy();
    }


    /**
     * show img_loading
     */
    public void showLoading() {
        showLoading(null);
    }

    public void showLoading(String text) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this, text);
        }

        mLoadingDialog.setLoadingTips(text);
        mLoadingDialog.show();
    }

    /**
     * hide img_loading
     */
    public void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    public void showError(String error) {
        hideLoading();
        ToastUtil.showMsg(error);
    }

    public LifecycleTransformer getBindToLifecycle() {
        return bindUntilEvent(ActivityEvent.DESTROY);
    }

    /**
     * 隐藏软键盘
     */
    protected void hideSoftInput(Context context) {
        InputMethodManager manager = (InputMethodManager) context
                .getSystemService(INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null && manager != null) {
            manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }

    }

    /**
     * 显示软键盘
     */
    protected void showSoftInput() {
        InputMethodManager manager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }
}
