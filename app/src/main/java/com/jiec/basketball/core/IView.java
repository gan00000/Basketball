//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.jiec.basketball.core;

import com.wangcj.common.base.mvp.IPresenter;

public interface IView<T extends IPresenter> {
    void showLoading();

    void hideLoading();

    void showError(String var1);

    void setPresenter(T var1);

    T getPresenter();

//    LifecycleTransformer getBindToLifecycle();
}
