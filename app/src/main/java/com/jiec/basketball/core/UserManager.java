package com.jiec.basketball.core;

import android.text.TextUtils;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.jiec.basketball.R;
import com.jiec.basketball.entity.LoginResult;
import com.jiec.basketball.entity.UserProfile;
import com.jiec.basketball.event.LoginEvent;
import com.jiec.basketball.event.LogoutEvent;
import com.jiec.basketball.event.UserProfileRefreshEvent;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.network.UserApi;
import com.jiec.basketball.utils.BallPreferencesUtils;
import com.jiec.basketball.utils.Constants;
import com.linecorp.linesdk.api.LineApiClient;
import com.linecorp.linesdk.api.LineApiClientBuilder;
import com.wangcj.common.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Jiec on 2019/1/6.
 */
public class UserManager {

    private LoginResult mLoginResult;

    private UserProfile mUserProfile;

    private String mDeviceToke;

    private static class Holder {
        private static final UserManager Instance = new UserManager();
    }

    public static final UserManager instance() {
        return UserManager.Holder.Instance;
    }

    public UserProfile getUserProfile() {
        return mUserProfile;
    }

    public String getDeviceToke() {
        return mDeviceToke;
    }

    public void setDeviceToke(String deviceToke) {
        mDeviceToke = deviceToke;
    }

    public String getToken() {
        if (mLoginResult != null) {
            return mLoginResult.getResult().getUser_token();
        }

        return null;
    }

    public boolean isLogin() {
        return mLoginResult != null;
    }

    public boolean checkLogin() {
        if (isLogin()) return true;

        int loginType = BallPreferencesUtils.getInstance().getLoginType();

        if (isFacebookLogin() && loginType == UserApi.LOGIN_TYPE_FACEBOOK) {
            autoLogin();
            return true;
        }

        if (isLineLogined() && loginType == UserApi.LOGIN_TYPE_LINE) {
            autoLogin();
            return true;
        }

        return false;
    }

    public void logout() {
        LineApiClientBuilder apiClientBuilder = new LineApiClientBuilder(BallApplication.getContext(), Constants.LINE_CHANNEL_ID);
        LineApiClient lineApiClient = apiClientBuilder.build();

        if (lineApiClient != null) {
            lineApiClient.logout();
        }

        LoginManager.getInstance().logOut();

        mUserProfile = null;
        mLoginResult = null;

        EventBus.getDefault().post(new LogoutEvent());

        ToastUtil.showMsg("退出登陸成功");
    }

    /**
     * 自动登录
     */
    public void autoLogin() {
        int loginType = BallPreferencesUtils.getInstance().getLoginType();

        if (isFacebookLogin() && loginType == UserApi.LOGIN_TYPE_FACEBOOK) {
            login(UserApi.LOGIN_TYPE_FACEBOOK, getFacebookAccessToken());
        } else if (isLineLogined() && loginType == UserApi.LOGIN_TYPE_LINE) {
            login(UserApi.LOGIN_TYPE_LINE, getLineToken());
        }
    }

    /**
     * 登录
     *
     * @param type
     * @param token
     */
    public void login(int type, String token) {
        BallPreferencesUtils.getInstance().setLoginType(type);

        UserApi userApi = RetrofitClient.getInstance().create(UserApi.class);
        userApi.login(type, token, mDeviceToke, 2)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<LoginResult>() {
                    @Override
                    protected void onSuccess(LoginResult result) {
                        mLoginResult = result;
                        EventBus.getDefault().post(new LoginEvent());

                        refreshProfile();
                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        mLoginResult = null;
                        ToastUtil.showMsg(R.string.login_fail);
                        EventBus.getDefault().post(new LoginEvent());
                    }
                });
    }

    /**
     * 刷新个人信息
     */
    public void refreshProfile() {
        UserApi userApi = RetrofitClient.getInstance().create(UserApi.class);
        userApi.getProfile(mLoginResult.getResult().getUser_token())
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<UserProfile>() {
                    @Override
                    protected void onSuccess(UserProfile result) {
                        updateProfile(result);
                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        mLoginResult = null;
                        ToastUtil.showMsg(R.string.login_fail);
                        EventBus.getDefault().post(new LoginEvent());
                    }
                });
    }

    public void updateProfile(UserProfile result) {
        mUserProfile = result;
        EventBus.getDefault().post(new UserProfileRefreshEvent());
    }


    private boolean isFacebookLogin() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        return accessToken != null && !accessToken.isExpired();
    }

    private String getFacebookAccessToken() {
        AccessToken accessToken = AccessToken.getCurrentAccessToken();

        if (accessToken != null) {
            return accessToken.getToken();
        }

        return null;
    }

    private boolean isLineLogined() {
        return !TextUtils.isEmpty(getLineToken());
    }

    private String getLineToken() {
        LineApiClientBuilder apiClientBuilder = new LineApiClientBuilder(BallApplication.getContext(), Constants.LINE_CHANNEL_ID);
        LineApiClient lineApiClient = apiClientBuilder.build();

        if (lineApiClient != null
                && lineApiClient.getCurrentAccessToken() != null
                && lineApiClient.getCurrentAccessToken().isSuccess()
                && lineApiClient.getCurrentAccessToken().getResponseData() != null) {

            return lineApiClient.getCurrentAccessToken().getResponseData().getTokenString();
        }

        return null;
    }
}
