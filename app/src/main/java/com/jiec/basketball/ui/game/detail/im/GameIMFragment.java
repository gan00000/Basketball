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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseUIFragment;
import com.jiec.basketball.core.UserManager;
import com.jiec.basketball.ui.game.detail.GameDetailActivity;
import com.messages.UserMessage;
import com.wangcj.common.utils.ToastUtil;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

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

    private List<ChatData>  chatDataList;
    private CommonAdapter commonAdapter;


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
                hideSoftInput(requireContext());
                if (!UserManager.instance().isLogin() || !StringUtils.isNotEmpty(UserManager.instance().getToken())){//app帳號還沒登入
                    ToastUtil.showMsg("請先登入帳號");
                    return;
                }
                if (IMManager.getInstance().loginFinish){ //聊天服務還沒登陸成功

                    IMManager.getInstance().sendChatMessage(msg);
                    appCompatEditText.setText("");
                }else{
                    ToastUtil.showMsg("重新連接中...");
                }
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_im, container, false);
        unbinder = ButterKnife.bind(this, view);



        IMManager.getInstance().mHandler = new Handler(){

            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                if (msg.what == 1){
                    List<UserMessage.MsgChatContent> msgChatContents = (List<UserMessage.MsgChatContent> ) msg.obj;
                    if (msgChatContents == null){
                        return;
                    }
                    for (UserMessage.MsgChatContent mMsgChatContent : msgChatContents) {

                        Log.i(TAG,"mMsgChatContent content =" + mMsgChatContent.getContent());
                        Log.i(TAG,"mMsgChatContent userName =" + mMsgChatContent.getFromUserName());
                        Log.i(TAG,"mMsgChatContent image =" + mMsgChatContent.getFromUserImg());
                        ChatData mChatData = new ChatData();
                        mChatData.setMsg(mMsgChatContent.getContent());
                        mChatData.setUserName(mMsgChatContent.getFromUserName());
                        //mChatData.setMsg(mMsgChatContent.getContent());
                        chatDataList.add(mChatData);
                    }

                    if (commonAdapter != null){
                        commonAdapter.notifyDataSetChanged();
                        imRecyclerView.scrollToPosition(chatDataList.size() - 1);
                    }
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
        return view;
    }

    private void initWebSocket() {

        IMManager.getInstance().startConnect();
        Log.i(TAG, "webSocketHelper start connect");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        IMManager.getInstance().release();
        chatDataList.clear();
    }
}
