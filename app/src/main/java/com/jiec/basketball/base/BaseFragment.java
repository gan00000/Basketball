package com.jiec.basketball.base;

import com.jiec.basketball.ui.dialog.LoadingDialog;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.trello.rxlifecycle.android.FragmentEvent;
import com.trello.rxlifecycle.components.support.RxFragment;
import com.wangcj.common.utils.ToastUtil;


/**
 * 实现基本功能：加载对话框，获取生命周期转换器
 * Created by jiec on 2017/5/31.
 */

public class BaseFragment extends RxFragment {

    private LoadingDialog mLoadingDialog;

    /**
     * show img_loading
     */
    public void showLoading() {
        showLoading(null);
    }

    public void showLoading(String text) {
        if (!isAdded()) {
            return;
        }

        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(getContext(), text);
        }
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
        return bindUntilEvent(FragmentEvent.DESTROY);
    }
}
