package com.jiec.basketball.ui.game.detail.statistic;

import com.jiec.basketball.entity.GameDataInfo;
import com.jiec.basketball.network.GameApi;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.RetrofitClient;
import com.wangcj.common.base.mvp.BasePresenter;
import com.wangcj.common.base.mvp.IModel;
import com.wangcj.common.utils.LogUtil;

public class GameStatisticMainPresenter extends BasePresenter<IModel, GameStatisticMainContract.View> implements GameStatisticMainContract.Presenter {

    public GameStatisticMainPresenter(GameStatisticMainContract.View view) {
        super(view);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadData(String gameId) {
        GameApi gameApi = RetrofitClient.getInstance().create(GameApi.class);

        gameApi.getGameDataInfo(String.valueOf(gameId))
                .compose(mView.getBindToLifecycle())
                .compose(new NetTransformer())
                .subscribe(new NetSubscriber<GameDataInfo>() {
                    @Override
                    protected void onSuccess(GameDataInfo result) {
                        if (result.getStatus().equals("ok")) {
                            mView.loadDataSuccess(result);
                        }
                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        LogUtil.e(reason);
                    }
                });
    }
}
