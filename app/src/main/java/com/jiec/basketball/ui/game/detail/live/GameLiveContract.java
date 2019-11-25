package com.jiec.basketball.ui.game.detail.live;

import com.jiec.basketball.core.IView;
import com.jiec.basketball.entity.GameLiveInfo;
import com.jiec.basketball.entity.response.GameLivePostResponse;
import com.wangcj.common.base.mvp.IPresenter;


public class GameLiveContract {
    interface View extends IView<Presenter> {
        void loadLiveSuccess(GameLiveInfo gameLiveInfo);

        void loadLiveVideoSuccess(GameLivePostResponse info);
    }


    interface Presenter extends IPresenter {
        void loadLive(String gameId);

        void getLivePost(String gameId);
    }
}
