package com.gan.http;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class WebSocketHelper {

    private final String TAG = WebSocketHelper.class.getSimpleName();

    private OkHttpClient okHttpClient;
    private WebSocket mWebSocket;

    private static final WebSocketHelper instance = new WebSocketHelper();

    public static WebSocketHelper getInstance() {
        return instance;
    }

    private WebSocketHelper() {

        okHttpClient = new OkHttpClient.Builder()
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();

    }

    /*
    服务器协议：websocket
    服务器连接地址：ws://app.ballgametime.com/chat
    端口：80
    */
    public void connect(String url){
        if (mWebSocket != null) {
            mWebSocket.cancel();
        }
        Request request = new Request.Builder()
                .url(url)
                .build();
        mWebSocket = okHttpClient.newWebSocket(request,new MyWebSocketListener());
    }

    public boolean sendMessage(String message){
        return mWebSocket.send(message);
    }

    public boolean sendMessage(byte... data){
        ByteString bs = ByteString.of(data);
       return mWebSocket.send(bs);
    }

    public boolean sendMessage(ByteString bs){
        return mWebSocket.send(bs);
    }

    public void close(int code, String reason){
        mWebSocket.close(code,reason);
    }

    class MyWebSocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);
            Log.i(TAG,"onOpen response="+response);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);
            Log.i(TAG,"onMessage text="+text);
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            super.onMessage(webSocket, bytes);
            Log.i(TAG,"onMessage bytes="+bytes);
        }

        @Override
        public void onClosing(WebSocket webSocket, int code, String reason) {
            super.onClosing(webSocket, code, reason);
            Log.i(TAG,"onClosing code="+code);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            Log.i(TAG,"onClosed code="+code);
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            super.onFailure(webSocket, t, response);
            Log.i(TAG,"onFailure t="+t.getMessage());
        }
    }

}
