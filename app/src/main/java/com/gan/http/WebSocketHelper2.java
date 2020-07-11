package com.gan.http;

import android.util.Log;

import com.gan.ctools.tool.DataConvertUtils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.nio.ByteBuffer;

public class WebSocketHelper2 extends WebSocketClient {

    private final String TAG = WebSocketHelper2.class.getSimpleName();


    private static WebSocketHelper2 instance;

    public static WebSocketHelper2 getInstance() {
        if (instance == null){
            instance = new WebSocketHelper2(URI.create("ws://app.ballgametime.com/chat"));
        }
        return instance;
    }

    /*
    服务器协议：websocket
    服务器连接地址：ws://app.ballgametime.com/chat
    端口：80
    */
//    public void connect(String url){
//    }

    public boolean sendMessage(byte[] msg) throws IOException {


        //消息id
        int id = 0;
        //消息id int转byte数组
        byte[] ba_cmdid = DataConvertUtils.intToBytes(id);
        //转成byte后数组的长度
        int cid_len = ba_cmdid.length;

        //消息内容protobuf 数据结构转byte数组后的长度
        int msg_len = msg.length + 2;
        //消息长度转byte数组
        byte[] ba_msglen = DataConvertUtils.intToBytes(msg_len);
        //转成byte数组后的长度
        int len2 = ba_msglen.length;

        //serverId：服务器id
        byte[] ba_serverid = DataConvertUtils.intToBytes(1);

        //依次写入bytearray中
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(ba_cmdid);
        outputStream.write(ba_msglen);
        outputStream.write((byte)101);
        outputStream.write((byte)1);
        outputStream.write(ba_serverid);
        outputStream.write(msg);

        send(outputStream.toByteArray());

        return false;
    }


    public void close(int code, String reason){
    }

    public WebSocketHelper2(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Log.i(TAG,"onOpen response=" + handshakedata.getHttpStatusMessage());
    }

    @Override
    public void onMessage(String message) {
        Log.i(TAG,"onMessage message="+message);
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        Log.i(TAG,"onMessage");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.i(TAG,"onOpen onClose code=" + code + ",reason="+reason + ",remote="+remote);
    }

    @Override
    public void onError(Exception ex) {
        ex.printStackTrace();
        Log.i(TAG,"onError response="+ ex.getMessage());
    }
}
