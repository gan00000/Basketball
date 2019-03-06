package com.jiec.basketball.utils;

import com.jiec.basketball.entity.response.PostResponse;
import com.jiec.basketball.network.GameApi;
import com.jiec.basketball.network.RetrofitClient;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by jiec on 2017/2/22.
 */

public class ThumbnailUtils {

    public interface OnLoadSuccessLisener {
        void onSuccess(String url);
    }

    public static void getThumbnail(String id, final OnLoadSuccessLisener lisener ) {
        GameApi service = RetrofitClient.getInstance().create(GameApi.class);
        Observable<PostResponse> observable = service.getPost(id);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PostResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(PostResponse response) {
                        lisener.onSuccess(response.getUrl());
                    }
                });
    }
}
