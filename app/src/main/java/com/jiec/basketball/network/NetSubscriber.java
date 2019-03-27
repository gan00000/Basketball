package com.jiec.basketball.network;

import com.jiec.basketball.network.base.CommResponse;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeoutException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * User: jiec
 * Date: 2016-04-08
 * Time: 09:35
 * DESC: 网络回调
 */
public abstract class NetSubscriber<T extends CommResponse> extends Subscriber<T> {

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        String error = e.getLocalizedMessage();
        if (e instanceof HttpException || e instanceof ConnectException) {
            error = "網絡請求異常";
        } else if (e instanceof TimeoutException || e instanceof SocketTimeoutException) {
            error = "網絡請求超時";
        }
        onFailed(-1, error);
    }

    @Override
    public void onNext(T response) {
        if (response != null && response.isSuccess()) {
            onSuccess(response);
        } else {
//            onFailed(-1, "網絡請求異常");
            onFailed(-1, response.getDesc());
        }
    }

    protected abstract void onSuccess(T result);

    /**
     * 失败
     */
    protected abstract void onFailed(int code, String reason);


    @Override
    public void onCompleted() {

    }
}
