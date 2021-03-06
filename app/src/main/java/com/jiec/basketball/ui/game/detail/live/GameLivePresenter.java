package com.jiec.basketball.ui.game.detail.live;

import com.jiec.basketball.core.BasePresenter;
import com.jiec.basketball.entity.GameLiveInfo;
import com.jiec.basketball.entity.response.GameLivePostResponse;
import com.jiec.basketball.network.GameApi;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.RetrofitClient;
import com.wangcj.common.base.mvp.IModel;
import com.wangcj.common.utils.LogUtil;

public class GameLivePresenter extends BasePresenter<IModel, GameLiveContract.View> implements GameLiveContract.Presenter {

    public GameLivePresenter(GameLiveContract.View view) {
        super(view);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadLive(String gameId) {
        GameApi gameApi = RetrofitClient.getInstance().create(GameApi.class);

        gameApi.getGameLiveInfo(gameId)
               // .compose(mView.getBindToLifecycle())
                .compose(new NetTransformer())
                .subscribe(new NetSubscriber<GameLiveInfo>() {
                    @Override
                    protected void onSuccess(GameLiveInfo info) {
                        if (info.getStatus().equals("ok")) {
                            mView.loadLiveSuccess(info);
                        }
                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        LogUtil.e(reason);
                    }
                });
    }

    @Override
    public void getLivePost(String gameId) {

        GameApi gameApi = RetrofitClient.getInstance().create(GameApi.class);

        gameApi.getLivePost(gameId)
              //  .compose(mView.getBindToLifecycle())
                .compose(new NetTransformer())
                .subscribe(new NetSubscriber<GameLivePostResponse>() {
                    @Override
                    protected void onSuccess(GameLivePostResponse info) {
                        if (info.getStatus().equals("ok")) {
                            mView.loadLiveVideoSuccess(info);
                        }
                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        LogUtil.e(reason);
                    }
                });

    }
}
