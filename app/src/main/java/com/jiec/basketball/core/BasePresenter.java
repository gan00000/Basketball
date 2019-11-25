package com.jiec.basketball.core;

import com.wangcj.common.base.mvp.IModel;
import com.wangcj.common.base.mvp.IPresenter;

public abstract class BasePresenter<M extends IModel, V extends IView> implements IPresenter {
    public M mModel;
    public V mView;

    public BasePresenter(M model, V view) {
        this.mModel = model;
        this.mView = view;
        this.mView.setPresenter(this);
    }

    public BasePresenter(V view) {
        this.mView = view;
        this.mView.setPresenter(this);
    }

    public void start() {
    }

    public void destroy() {
    }
}
