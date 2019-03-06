package com.jiec.basketball.ui.mine.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.jiec.basketball.R;
import com.jiec.basketball.core.UserManager;
import com.jiec.basketball.network.UserApi;
import com.jiec.basketball.utils.Constants;
import com.linecorp.linesdk.LoginDelegate;
import com.linecorp.linesdk.LoginListener;
import com.linecorp.linesdk.Scope;
import com.linecorp.linesdk.auth.LineAuthenticationParams;
import com.linecorp.linesdk.auth.LineLoginResult;
import com.wangcj.common.utils.ToastUtil;

import java.util.Arrays;

/**
 * Created by Jiec on 2019/1/6.
 */
public class LoginActivity extends Activity {

    private CallbackManager mCallbackManager;

    private LoginDelegate mLoginDelegate;

    private static final int REQUEST_CODE = 10001;

    public static void show(Context context) {
        context.startActivity(new Intent(context, LoginActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);


        LoginButton loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions("email");

        mCallbackManager = CallbackManager.Factory.create();

        // Callback registration
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                UserManager.instance().login(
                        UserApi.LOGIN_TYPE_FACEBOOK, loginResult.getAccessToken().getToken());
                finish();
            }

            @Override
            public void onCancel() {
                ToastUtil.showMsg(R.string.login_cancel);
            }

            @Override
            public void onError(FacebookException exception) {
                ToastUtil.showMsg(R.string.login_fail);
            }
        });


        com.linecorp.linesdk.widget.LoginButton lineLoginBtn = findViewById(R.id.btn_line);
        lineLoginBtn.setChannelId(Constants.LINE_CHANNEL_ID);

        lineLoginBtn.enableLineAppAuthentication(true);

        lineLoginBtn.setAuthenticationParams(new LineAuthenticationParams.Builder()
                .scopes(Arrays.asList(Scope.PROFILE))
                .build()
        );


        mLoginDelegate = LoginDelegate.Factory.create();
        lineLoginBtn.setLoginDelegate(mLoginDelegate);
        lineLoginBtn.addLoginListener(new LoginListener() {
            @Override
            public void onLoginSuccess(@NonNull LineLoginResult result) {
                String accessToken = result.getLineCredential().getAccessToken().getTokenString();
                Log.e("ERROR", "LINE Login token : " + accessToken);
                UserManager.instance().login(
                        UserApi.LOGIN_TYPE_LINE, accessToken);
                finish();
            }

            @Override
            public void onLoginFailure(@Nullable LineLoginResult result) {
                ToastUtil.showMsg(R.string.login_fail);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        mLoginDelegate.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);
    }
}
