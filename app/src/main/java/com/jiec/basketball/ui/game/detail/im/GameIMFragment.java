package com.jiec.basketball.ui.game.detail.im;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseUIFragment;
import com.jiec.basketball.core.UserManager;
import com.jiec.basketball.entity.GameInfo;
import com.jiec.basketball.entity.MatchSummary;
import com.jiec.basketball.ui.game.detail.GameDetailActivity;
import com.messages.UserMessage;
import com.wangcj.common.utils.ToastUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.apache.commons.lang3.StringUtils;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wangchuangjie on 2018/5/20.
 */

public class GameIMFragment extends BaseUIFragment {

    public static final String TAG = "GameIMFragment";

    Unbinder unbinder;


    @BindView(R.id.st_im_rv)
    RecyclerView imRecyclerView;

    @BindView(R.id.match_status_tv)
    TextView gameStatus;

    private List<ChatData>  chatDataList;
    private CommonAdapter commonAdapter;

    boolean isSetGameInfo = false;
    public boolean canTalk = false;

    MatchSummary currentMatchSummary;
    GameInfo gameInfo;

    public void setGameInfo(String xxGameTime, MatchSummary matchSummary, GameInfo gameInfo){

        GameDetailActivity mGameDetailActivity = (GameDetailActivity) getActivity();

        if (isSetGameInfo){
            return;
        }
        this.currentMatchSummary = matchSummary;
        this.gameInfo = gameInfo;
        isSetGameInfo = true;

        String data = gameInfo.getGamedate();
        String gameTime = gameInfo.getGametime();
       String mmTime = gameInfo.getTime().toUpperCase();//.replace("AM","上午").replace("PM","下午");

        String formatTime = "";

        if (StringUtils.isEmpty(mmTime) || StringUtils.isEmpty(data)){
            return;
        }

        //获取指定时间的时间戳，除以1000说明得到的是秒级别的时间戳（10位）

        long xxxTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm aa", Locale.ENGLISH)).parse(data + " " + mmTime, new ParsePosition(0)).getTime();

        if (xxxTime > (System.currentTimeMillis() + 1 * 60 * 60 * 1000)){
            gameStatus.setText("比賽未開始");
            canTalk = false;

        }else if ((xxxTime + 5 * 60 * 60 * 1000) < System.currentTimeMillis()){
            gameStatus.setText("比賽已結束");
            canTalk = false;
        }else{
            gameStatus.setVisibility(View.GONE);
            imRecyclerView.setVisibility(View.VISIBLE);
            mGameDetailActivity.getTalkInputView().setVisibility(View.VISIBLE);
            initIm();
            canTalk = true;
        }
//        if (matchSummary.getScheduleStatus().equals("Final")) {
//
//
//        } else if (matchSummary.getScheduleStatus().equals("InProgress")) {
//
//        } else {
//
//        }

    }


    public static GameIMFragment newInstance() {
        GameIMFragment fragment = new GameIMFragment();
        return fragment;
    }

    @Override
    public void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_im, container, false);
        unbinder = ButterKnife.bind(this, view);

        return view;
    }

    @SuppressLint("HandlerLeak")
    private void initIm() {

        IMManager.getInstance().mHandler = new Handler(){

            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == IMManager.IM_MESSAGE_ON){
                    List<UserMessage.MsgChatContent> msgChatContents = (List<UserMessage.MsgChatContent> ) msg.obj;
                    if (msgChatContents == null){
                        return;
                    }
                    for (UserMessage.MsgChatContent mMsgChatContent : msgChatContents) {

                        Log.i(TAG,"mMsgChatContent content =" + mMsgChatContent.getContent());
                        Log.i(TAG,"mMsgChatContent userName =" + mMsgChatContent.getFromUserName());
                        Log.i(TAG,"mMsgChatContent image =" + mMsgChatContent.getFromUserImg());
                        Log.i(TAG,"mMsgChatContent gameId =" + mMsgChatContent.getGameId());
                        if (gameInfo != null && gameInfo.getId().equals(String.valueOf(mMsgChatContent.getGameId()))){ //属于本赛程id的 才显示

                            ChatData mChatData = new ChatData();
                            mChatData.setMsg(mMsgChatContent.getContent());
                            mChatData.setUserName(mMsgChatContent.getFromUserName());
                            //mChatData.setMsg(mMsgChatContent.getContent());
                            if (chatDataList.size() > 1000){ //最多1000条数据
                                chatDataList.remove(chatDataList.size() - 1);
                            }
                            chatDataList.add(mChatData);
                        }
                    }

                    if (commonAdapter != null){
                        commonAdapter.notifyDataSetChanged();
                        imRecyclerView.scrollToPosition(chatDataList.size() - 1);
                    }
                }else if (msg.what == IMManager.IM_LOGINED){

                }else if (msg.what == IMManager.IM_OPENED){

                }else if (msg.what == IMManager.IM_CLOSE){

                }else if (msg.what == IMManager.IM_RECONNECT_SUCCESS){

                }

            }
        };
        initWebSocket();

        chatDataList = new ArrayList<>();

        imRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        commonAdapter = new CommonAdapter<ChatData>(getContext(),R.layout.fragment_game_im_item,chatDataList) {


            @Override
            protected void convert(ViewHolder holder, ChatData chatData, int position) {
                holder.setText(R.id.talk_im_show_et, chatData.getMsg() + "  - " + chatData.getUserName());
            }
        };

        imRecyclerView.setAdapter(commonAdapter);



        GameDetailActivity mGameDetailActivity = (GameDetailActivity)requireActivity();
        EditText appCompatEditText = mGameDetailActivity.getAppCompatEditText();
        mGameDetailActivity.getSendMsgBtn().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = appCompatEditText.getText().toString();
                if (StringUtils.isEmpty(msg)){
                    ToastUtil.showMsg("請輸入內容");
                    return;
                }
                if (msg.length() > 200){
                    ToastUtil.showMsg("輸入內容太長，最大200個字符");
                    return;
                }
                hideSoftInput(requireContext());
                if (!UserManager.instance().isLogin() || !StringUtils.isNotEmpty(UserManager.instance().getToken())){//app帳號還沒登入
                    ToastUtil.showMsg("請先登入帳號");
                    return;
                }
                if (IMManager.getInstance().loginFinish){ //聊天服務還沒登陸成功

                    try {
                        IMManager.getInstance().sendChatMessage(Long.parseLong(gameInfo.getId()), msg);
                        appCompatEditText.setText("");
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        ToastUtil.showMsg("發送失敗");
                    }
                }else{
                    ToastUtil.showMsg("重新連接中...");
                }
            }
        });

    }

    private void initWebSocket() {

        IMManager.getInstance().startConnect();
        Log.i(TAG, "webSocketHelper start connect");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IMManager.getInstance().release();
        if (chatDataList != null) {
            chatDataList.clear();
        }
        isSetGameInfo = false;
    }
}
