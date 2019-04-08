package com.jiec.basketball.ui.game;

import com.jiec.basketball.entity.GameList;
import com.jiec.basketball.entity.GameProgress;
import com.wangcj.common.base.mvp.IPresenter;
import com.wangcj.common.base.mvp.IView;


public class GameMainContract {
    interface View extends IView<Presenter> {
        void showGameList(GameList list);

        void loadGameProgressSuccess(GameProgress gameProgress);
    }


    interface Presenter extends IPresenter {
        void getGameInfo(String start, String end);

        void getMatchProgress(String id);
    }
}
