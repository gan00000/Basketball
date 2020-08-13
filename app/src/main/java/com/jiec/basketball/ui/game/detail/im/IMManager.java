package com.jiec.basketball.ui.game.detail.im;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.protobuf.InvalidProtocolBufferException;
import com.jiec.basketball.core.UserManager;
import com.messages.UserMessage;

import org.apache.commons.lang3.StringUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class IMManager {

    public static final int IM_OPENED = 1;
    public static final int IM_LOGINED = 2;
    public static final int IM_CLOSE = 3;//断线
    public static final int IM_RECONNECT_SUCCESS = 4;//重连成功
    public static final int IM_MESSAGE_ON = 5;//重连成功

    public static final String TAG = "IMManager";
    private static final IMManager ourInstance = new IMManager();

    private IMWebSocketClient mWebSocketClient;
    public Handler mHandler;
    Timer heartbeatTimer;



    public boolean loginFinish = false;
    private boolean logining = false;


    static IMManager getInstance() {
        return ourInstance;
    }

    private IMManager() {
    }

    public void startConnect(){
        initSocketClient();
        if (mWebSocketClient != null){
            mWebSocketClient.connect();
        }
    }

    public void release(){
        if (mWebSocketClient != null){
            mWebSocketClient.close();
            mWebSocketClient = null;
        }
        if (heartbeatTimer != null){
            heartbeatTimer.cancel();
            heartbeatTimer = null;
        }
    }

    private boolean reconnect = false;

    public void initSocketClient(){

        if (mWebSocketClient != null){
            mWebSocketClient.close();
            mWebSocketClient = null;
        }

        mWebSocketClient = new IMWebSocketClient(URI.create("ws://app.ballgametime.com/chat"));
        if (heartbeatTimer != null){
            heartbeatTimer.cancel();
            heartbeatTimer = null;
        }
        heartbeatTimer = new Timer();
        scheduleSendHeartbeat();//定时检测心跳
    }

    //定时发送心跳
    private void scheduleSendHeartbeat(){

        heartbeatTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                tryReconnect();

            }
        },30 * 1000,15 * 1000);
    }

    private void tryReconnect() {
        try {
            if (mWebSocketClient != null && mWebSocketClient.isClosed()){
                for (int i = 0; i < 5; i++) {//重连次数
                    Log.i(TAG,"重连中... " + i);
                  boolean success = mWebSocketClient.reconnectBlocking();
                    Log.i(TAG,"重连中... " + i + "  " + success);
                  if (success){
                      if (mHandler != null){
                          Message message = new Message();
                          message.what = IM_RECONNECT_SUCCESS;
                          mHandler.sendMessage(message);
                      }
                      break;
                  }
                }
//                if (!mWebSocketClient.isOpen()){//重连不成功，重置
//                    initSocketClient();
//                    return;
//                }
            }
            if (mWebSocketClient != null && mWebSocketClient.isOpen()){

                sendHeartbeatMessage();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    //发送心跳信息
    public boolean sendHeartbeatMessage() {

        if (mWebSocketClient.isClosed() && !mWebSocketClient.isOpen()){
            return false;
        }

        UserMessage.HeartbeatConnectReq_0 heartbeatConnectReq_0 = UserMessage.HeartbeatConnectReq_0.newBuilder().build();
        byte[] msgArray = heartbeatConnectReq_0.toByteArray();

        ByteBuffer bb = ByteBuffer.allocate(14 + msgArray.length);
        bb.putInt(0);
        bb.putInt(msgArray.length + 2);
        bb.put((byte)101);
        bb.put((byte)1);
        bb.putInt(1);
        bb.put(msgArray);
        bb.flip();
        mWebSocketClient.send(bb);

        return true;
    }





    public boolean sendLoginMessage(String token) {

        if (mWebSocketClient.isClosed() && !mWebSocketClient.isOpen()){
            return false;
        }

        Log.i(TAG,"send login token : " + token);
        UserMessage.LoginReq_1001 loginReq_1001 = UserMessage.LoginReq_1001.newBuilder().setToken(token).build();
        byte[] msgArray = loginReq_1001.toByteArray();

        ByteBuffer bb = ByteBuffer.allocate(14 + msgArray.length);
        bb.putInt(1001);
        bb.putInt(msgArray.length + 2);
        bb.put((byte)101);
        bb.put((byte)1);
        bb.putInt(1);
        bb.put(msgArray);
        bb.flip();
        mWebSocketClient.send(bb);

        return false;
    }

    public boolean sendChatMessage(long gameId, String msg) {

        if (!loginFinish){
            return false;
        }
        Log.i(TAG,"send chat msg : " + msg);
        UserMessage.SendChatReq_1002 sendChatReq_1002 = UserMessage.SendChatReq_1002.newBuilder().setContent(msg).setGameId(gameId).build();
        byte[] msgArray = sendChatReq_1002.toByteArray();

        ByteBuffer bb = ByteBuffer.allocate(14 + msgArray.length);
        bb.putInt(1002);
        bb.putInt(msgArray.length + 2);
        bb.put((byte)101);
        bb.put((byte)1);
        bb.putInt(1);
        bb.put(msgArray);
        bb.flip();
        mWebSocketClient.send(bb);

        return true;
    }


    class IMWebSocketClient extends WebSocketClient {


    /*
    服务器协议：websocket
    服务器连接地址：ws://app.ballgametime.com/chat
    端口：80
    */

        public IMWebSocketClient(URI serverUri) {
            super(serverUri);

        }

        @Override
        public void onOpen(ServerHandshake handshakedata) {
            Log.i(TAG,"onOpen response=" + handshakedata.getHttpStatusMessage());

            if (mHandler != null){
                Message message = new Message();
                message.what = IM_OPENED;
                mHandler.sendMessage(message);
            }

            if (UserManager.instance().isLogin() && StringUtils.isNotEmpty(UserManager.instance().getToken())){
                sendLoginMessage(UserManager.instance().getToken());
            }
        }

        @Override
        public void onMessage(String message) {
            Log.i(TAG,"onMessage message="+message);
        }

        @Override
        public void onMessage(ByteBuffer bytes) {
            Log.i(TAG,"onMessage ByteBuffer bytes");
            int msgId = bytes.getInt();
            int msgSize = bytes.getInt();
            byte key = bytes.get();//101
            byte type = bytes.get();//1
            int serverId = bytes.getInt();

            byte dataByte[] = new byte[msgSize-2];
            ByteBuffer tempByteBuffer = bytes.get(dataByte,0,dataByte.length);

            Log.i(TAG,"onMessage msgId=" + msgId);
            Log.i(TAG,"onMessage msgSize=" + msgSize);
            Log.i(TAG,"onMessage key=" + key);
            Log.i(TAG,"onMessage type=" + type);
            Log.i(TAG,"onMessage serverId=" + serverId);
            if (msgId == 0){

                try {
                    UserMessage.HeartbeatConnectResp_0 heartbeatConnectResp_0 = UserMessage.HeartbeatConnectResp_0.parseFrom(dataByte);
                    long serverTime = heartbeatConnectResp_0.getServerTime();
                    Log.i(TAG,"onMessage serverTime =" + serverTime);
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }else if (msgId == 1001){
                try {
                    UserMessage.LoginResp_1001 loginResp_1001 = UserMessage.LoginResp_1001.parseFrom(dataByte);
                    UserMessage.MsgUser msgUser = loginResp_1001.getMsgUser();
                    String name = msgUser.getName();
                    String token = msgUser.getToken();
                    Log.i(TAG,"im login name =" + name);
                    Log.i(TAG,"im login token =" + token);
                    Log.i(TAG,"im登录完成...");
                    logining = false;
                    loginFinish = true;
                    if (mHandler != null){
                        Message message = new Message();
                        message.what = IM_LOGINED;
                        mHandler.sendMessage(message);
                    }

                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }else if (msgId == 1002){
                try {
                    UserMessage.SendChatResp_1002 sendChatResp_1002 = UserMessage.SendChatResp_1002.parseFrom(dataByte);
                    List<UserMessage.MsgChatContent> msgChatContents = sendChatResp_1002.getMsgChatContentList();

                    if (!msgChatContents.isEmpty() && mHandler != null){
                        Message message = new Message();
                        message.what = IM_MESSAGE_ON;
                        message.obj = msgChatContents;
                        mHandler.sendMessage(message);
                    }

                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
            }

        }

        @Override
        public void onClose(int code, String reason, boolean remote) {
            Log.i(TAG,"onClose code=" + code + ",reason="+reason + ",remote="+remote);
            loginFinish = false;
            if (mHandler != null){
                Message message = new Message();
                message.what = IM_CLOSE;
                mHandler.sendMessage(message);
            }
            tryReconnect();
        }

        @Override
        public void onError(Exception ex) {
            ex.printStackTrace();
            Log.i(TAG,"onError response="+ ex.getMessage());
            loginFinish = false;

        }

    }

}
