package com.jiec.basketball.ui.game.detail;

import com.wangcj.common.base.mvp.BasePresenter;
import com.wangcj.common.base.mvp.IModel;

public class GameDetailPresenter extends BasePresenter<IModel, GameDetailContract.View> implements GameDetailContract.Presenter {

    public GameDetailPresenter(GameDetailContract.View view) {
        super(view);
    }

    @Override
    public void start() {

    }
}
