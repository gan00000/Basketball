package com.jiec.basketball.ui.game.detail.statistic;

import com.jiec.basketball.core.IView;
import com.jiec.basketball.entity.GameDataInfo;
import com.wangcj.common.base.mvp.IPresenter;


public class GameStatisticMainContract {
    interface View extends IView<Presenter> {
        void loadDataSuccess(GameDataInfo gameDataInfo);
    }


    interface Presenter extends IPresenter {
        void loadData(String gameId);
    }
}
