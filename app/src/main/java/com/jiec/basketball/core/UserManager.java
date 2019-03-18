package com.jiec.basketball.core;

import android.text.TextUtils;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.jiec.basketball.R;
import com.jiec.basketball.bean.UserInfoBean;
import com.jiec.basketball.dao.UserDao;
import com.jiec.basketball.entity.LoginResult;
import com.jiec.basketball.entity.UserProfile;
import com.jiec.basketball.event.LoginEvent;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.network.UserApi;
import com.jiec.basketball.utils.BallPreferencesUtils;
import com.jiec.basketball.utils.ConstantUtils;
import com.jiec.basketball.utils.Constants;
import com.jiec.basketball.utils.EventBusEvent;
import com.linecorp.linesdk.api.LineApiClient;
import com.linecorp.linesdk.api.LineApiClientBuilder;
import com.wangcj.common.utils.ToastUtil;

import org.greenrobot.eventbus.EventBus;

import static com.facebook.FacebookSdk.getApplicationContext;

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

    /**
     * 注销登录
     */
    public void logout() {
        LineApiClientBuilder apiClientBuilder = new LineApiClientBuilder(BallApplication.getContext(), Constants.LINE_CHANNEL_ID);
        LineApiClient lineApiClient = apiClientBuilder.build();

        if (lineApiClient != null) {
            lineApiClient.logout();
        }

        LoginManager.getInstance().logOut();
        mUserProfile = null;
        mLoginResult = null;
        UserDao.getInstance().exitLogin(getApplicationContext());
        EventBus.getDefault()
                .post(new EventBusEvent(ConstantUtils.EVENT_LOGIN_OUT, UserManager.class, null));

        ToastUtil.showMsg("退出登錄成功");
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
     * @param token  facebook或者line授权token
     */
    public void login(int type, String token) {
        BallPreferencesUtils.getInstance().setLoginType(type);
        //测试账号？？？？？？？？？？？
//        token = "EAAG0bZCueI9wBAAAZAVMKBVs4MjonsjInVeR3NeHZBz53BIig6ZCT" +
//                "DEFshuNOOJdor1KcV5kjC4a0ZAPoarQMNc8HFHD19xzbmV6O9FYm" +
//                "1Rpu4dbTJPQDiayDNfwNoLoiT5ztC3dm4ZBARxqXZCekb2EzUiZCecINb4ZD";

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

    /**
     * 更新用户信息
     * @param result
     */
    public void updateProfile(UserProfile result) {
        mUserProfile = result;

        UserInfoBean userInfoBean = new UserInfoBean();
        userInfoBean.user_token = mLoginResult.getResult().getUser_token();
        userInfoBean.user_id = result.getResult().getUser_id();
        userInfoBean.display_name = result.getResult().getDisplay_name();
        userInfoBean.user_email = result.getResult().getUser_email();
        userInfoBean.user_status = result.getResult().getUser_status();
        userInfoBean.user_img = result.getResult().getUser_img();
        userInfoBean.change_name = result.getResult().getChange_name();
        BallApplication.userInfo = userInfoBean;
        UserDao.getInstance().encodeAndSaveUserInfo(getApplicationContext(), userInfoBean);
        EventBus.getDefault()
                .post(new EventBusEvent(ConstantUtils.EVENT_LOGIN, UserManager.class, null));
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
