/*
 *  Copyright 2015 GoIn Inc. All rights reserved.
 */

package com.jiec.basketball.network;

import com.github.aurae.retrofit2.LoganSquareConverterFactory;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * A helper of retrofit2 for creating restful service easier.
 * <p/>
 * Created by Eric on 15/10/18.
 */
public class RetrofitClient {


    public static String HOST_NAME = "http://www.ballgametime.com";

    private static final RetrofitClient instance = new RetrofitClient();

    public static RetrofitClient getInstance() {
        return instance;
    }

    private Retrofit retrofit;

    public RetrofitClient() {
        createRetrofit();
    }

    private void createRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit = new Retrofit.Builder().baseUrl(HOST_NAME).client(okHttpClient)
                .addConverterFactory(LoganSquareConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
    }

    public <T> T create(Class<?> clazz) {
        return (T) retrofit.create(clazz);
    }

}
