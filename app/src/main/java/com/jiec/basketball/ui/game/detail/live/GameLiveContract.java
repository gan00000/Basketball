package com.jiec.basketball.ui.game.detail.live;

import com.jiec.basketball.entity.GameLiveInfo;
import com.wangcj.common.base.mvp.IPresenter;
import com.wangcj.common.base.mvp.IView;


public class GameLiveContract {
    interface View extends IView<Presenter> {
        void loadLiveSuccess(GameLiveInfo gameLiveInfo);
    }


    interface Presenter extends IPresenter {
        void loadLive(String gameId);
    }
}
