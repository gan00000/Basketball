package com.jiec.basketball.ui.game;

import com.jiec.basketball.entity.GameList;
import com.jiec.basketball.entity.GameProgress;
import com.jiec.basketball.network.GameApi;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.RetrofitClient;
import com.wangcj.common.base.mvp.BasePresenter;
import com.wangcj.common.base.mvp.IModel;
import com.wangcj.common.utils.LogUtil;

public class GameMainPresenter extends BasePresenter<IModel, GameMainContract.View> implements GameMainContract.Presenter {

    public GameMainPresenter(GameMainContract.View view) {
        super(view);
    }

    @Override
    public void start() {

    }

    @Override
    public void getGameInfo(String start, String end) {
        GameApi gameApi = RetrofitClient.getInstance().create(GameApi.class);

//        start = "2020-01-02";
//        end = "2020-01-08";
        gameApi.getGameList(start, end)
                .compose(mView.getBindToLifecycle())
                .compose(new NetTransformer())
                .subscribe(new NetSubscriber<GameList>() {
                    @Override
                    protected void onSuccess(GameList result) {
                        mView.showGameList(result);
                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        LogUtil.e(reason);
                    }
                });
    }

    @Override
    public void getMatchProgress(String id) {
        GameApi gameApi = RetrofitClient.getInstance().create(GameApi.class);

        gameApi.getGameProgress(id)
                .compose(mView.getBindToLifecycle())
                .compose(new NetTransformer())
                .subscribe(new NetSubscriber<GameProgress>() {
                    @Override
                    protected void onSuccess(GameProgress result) {
                        mView.loadGameProgressSuccess(result);
                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        LogUtil.e(reason);
                    }
                });
    }
}
