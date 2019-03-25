package com.jiec.basketball.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseUIActivity;
import com.jiec.basketball.bean.NotifyCounterModel;
import com.jiec.basketball.core.UserManager;
import com.jiec.basketball.entity.LoginResult;
import com.jiec.basketball.entity.UserProfile;
import com.jiec.basketball.event.LoginEvent;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.network.UserApi;
import com.jiec.basketball.ui.mine.collection.CollectionActivity;
import com.jiec.basketball.ui.mine.comment.CommentActivity;
import com.jiec.basketball.ui.mine.history.HistoryActivity;
import com.jiec.basketball.ui.mine.like.LikeActivity;
import com.jiec.basketball.ui.mine.login.LoginActivity;
import com.jiec.basketball.ui.mine.notify.NotifyActivity;
import com.jiec.basketball.ui.mine.setting.SettingActivity;
import com.jiec.basketball.utils.ConstantUtils;
import com.jiec.basketball.utils.EventBusEvent;
import com.jiec.basketball.utils.EventBusUtils;
import com.jiec.basketball.utils.ImageLoaderUtils;
import com.wangcj.common.utils.ToastUtil;
import com.wangcj.common.widget.CircleSImageView;
import com.wangcj.common.widget.ItemLayout;
import com.wangcj.common.widget.PressRelativeLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

import static com.jiec.basketball.core.BallApplication.userInfo;


public class MineActivity extends BaseUIActivity {

    @BindView(R.id.iv_head)
    CircleSImageView mIvHead;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.rl_info)
    PressRelativeLayout rlInfo;
    @BindView(R.id.item_notify)
    ItemLayout rlNotify;
    private QBadgeView qBadgeView;

    public static void show(Context context) {
        Intent intent = new Intent(context, MineActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        qBadgeView =  new QBadgeView(MineActivity.this);

//        if (!UserManager.instance().checkLogin()) {
//            mTvName.setText("未登錄");
//            tvLogin.setVisibility(View.VISIBLE);
//            rlInfo.setVisibility(View.GONE);
//        } else if (UserManager.instance().isLogin()) {
//            tvLogin.setVisibility(View.GONE);
//            rlInfo.setVisibility(View.VISIBLE);
//            getNotifyCounter();
//        }

        if (userInfo == null) {
            mTvName.setText("未登錄");
            tvLogin.setVisibility(View.VISIBLE);
            rlInfo.setVisibility(View.GONE);
        } else {
            UserManager.instance().refreshProfile(userInfo.user_token);
            tvLogin.setVisibility(View.GONE);
            rlInfo.setVisibility(View.VISIBLE);
            update();
            getNotifyCounter();
        }
    }



    /**
     * 获取未读消息数量
     */
    private void getNotifyCounter(){
        UserApi userApi = RetrofitClient.getInstance().create(UserApi.class);
        userApi.getNotifyCounter(userInfo.user_token)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<NotifyCounterModel>() {
                    @Override
                    protected void onSuccess(NotifyCounterModel result) {
                        qBadgeView.bindTarget(rlNotify.findViewById(com.wangcj.common.R.id.tv_title))
                                .setBadgeNumber(Integer.parseInt(result.getResult().getTotal_unread()));

                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                    }
                });
    }



    @Override
    protected void onResume() {
        super.onResume();
        //重新回到頁面時再次檢查一下登錄狀態
//        if (UserManager.instance().isLogin()) {
//            tvLogin.setVisibility(View.GONE);
//            rlInfo.setVisibility(View.VISIBLE);
//            update();
//        }else {
//            mTvName.setText("未登錄");
//            tvLogin.setVisibility(View.VISIBLE);
//            rlInfo.setVisibility(View.GONE);
//        }

        if (userInfo == null) {
            mTvName.setText("未登錄");
            tvLogin.setVisibility(View.VISIBLE);
            rlInfo.setVisibility(View.GONE);
        } else {
            tvLogin.setVisibility(View.GONE);
            rlInfo.setVisibility(View.VISIBLE);
            update();
            getNotifyCounter();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.rl_info, R.id.layout_collection, R.id.item_notify, R.id.tv_login,
            R.id.layout_comment, R.id.layout_zan, R.id.layout_history})
    public void onViewClicked(View view) {
//        if (!UserManager.instance().checkLogin()) {
//            LoginActivity.show(this);
//            return;
//        }

        if (userInfo == null) {
            LoginActivity.show(this);
            return;
        }


        switch (view.getId()) {
            case R.id.rl_info:
                startActivity(new Intent(this, UserInfoActivity.class));
                break;
            case R.id.layout_collection:
                startActivity(new Intent(this, CollectionActivity.class));
                break;
            case R.id.item_notify:
                startActivity(new Intent(this, NotifyActivity.class));
                break;
            case R.id.layout_comment:
                startActivity(new Intent(this, CommentActivity.class));
                break;
            case R.id.layout_zan:
                startActivity(new Intent(this, LikeActivity.class));
                break;
            case R.id.layout_history:
                startActivity(new Intent(this, HistoryActivity.class));
                break;
        }
    }

    @OnClick({R.id.item_setting})
    public void onSettingClick(View view) {
//        if (!UserManager.instance().isLogin()) {
//            LoginActivity.show(this);
//            return;
//        }

        if (userInfo == null) {
            LoginActivity.show(this);
            return;
        }
        switch (view.getId()) {
            case R.id.item_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
        }
    }

    /**
     * 更新用戶信息
     */
    private void update() {
//        if (UserManager.instance().getUserProfile() == null) return;
//        tvLogin.setVisibility(View.GONE);
//        rlInfo.setVisibility(View.VISIBLE);
//        UserProfile userProfile = UserManager.instance().getUserProfile();
//        ImageLoaderUtils.display(this, mIvHead, userProfile.getResult().getUser_img(),
//                R.drawable.img_default_head, R.drawable.img_default_head);
//        mTvName.setText(userProfile.getResult().getDisplay_name());

        if (userInfo == null) return;
        tvLogin.setVisibility(View.GONE);
        rlInfo.setVisibility(View.VISIBLE);
        ImageLoaderUtils.display(this, mIvHead, userInfo.user_img,
                R.drawable.img_default_head, R.drawable.img_default_head);
        mTvName.setText(userInfo.display_name);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(LoginEvent event) {
        hideLoading();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(EventBusEvent event) {
        hideLoading();
        switch (event.status){
            case ConstantUtils.EVENT_LOGIN_OUT:
                mIvHead.setImageResource(R.drawable.img_default_head);
                mTvName.setText("用戶");
                qBadgeView.setBadgeNumber(0);
                LogUtils.e("登出EventBus");
                break;

                case ConstantUtils.EVENT_HAS_READ:
                    qBadgeView.setBadgeNumber(0);
                    LogUtils.e("消息已讀EventBus");
                    break;

                case ConstantUtils.EVENT_LOGIN:
                    update();
                    getNotifyCounter();
                    break;
        }
    }


}
