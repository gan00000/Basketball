package com.jiec.basketball.ui.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseActivity;
import com.jiec.basketball.core.UserManager;
import com.jiec.basketball.entity.UserProfile;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.network.UserApi;
import com.jiec.basketball.utils.BallPreferencesUtils;
import com.jiec.basketball.utils.EmptyUtils;
import com.jiec.basketball.utils.ImageLoaderUtils;
import com.jiec.basketball.utils.InputCheckUtils;
import com.jiec.basketball.widget.CircleSImageView;
import com.wangcj.common.utils.ThreadUtils;
import com.wangcj.common.utils.ToastUtil;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jiec.basketball.core.BallApplication.userInfo;

/**
 * Created by Jiec on 2019/1/13.
 * 个人中心，修改用户资料页面
 */
public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.iv_head)
    CircleSImageView mIvHead;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.et_email)
    EditText mEtEmail;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_email)
    TextView mTvEmail;
    @BindView(R.id.tv_oneTime)
    TextView tvOneTime;

    private String mHeadBase64;
    private boolean isModify;  //是否可以更改

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);

        isModify = InputCheckUtils.compareIsEqual("0", userInfo.change_name);
        if (!isModify) {
            mEtName.setVisibility(View.GONE);
        }else {
            tvOneTime.setVisibility(View.VISIBLE);
        }
        mTvName.setText("用戶名（" + userInfo.display_name + "）");
        if (!TextUtils.isEmpty(userInfo.user_email)) {
            mTvEmail.setText("郵件（" + userInfo.user_email + "）");
        }
        ImageLoaderUtils.display(this, mIvHead, EmptyUtils.emptyOfString(userInfo.user_img) ? " " : userInfo.user_img,
                R.drawable.img_default_head, R.drawable.img_default_head);
    }

    @OnClick({R.id.tv_save, R.id.iv_head, R.id.rl_head})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_save:
                updateInfo();
                break;
            case R.id.iv_head:
            case R.id.rl_head:
                startActivityForResult(new Intent(this, HeadActivity.class), 10000);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 10000 && resultCode == RESULT_OK) {
            try {
                mHeadBase64 = encodeBase64File(data.getStringExtra("data"));
                ThreadUtils.postMainThread(new Runnable() {
                    @Override
                    public void run() {
                        ImageLoaderUtils.loadBase64(mIvHead, mHeadBase64);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String encodeBase64File(String path) throws Exception {
        Bitmap bitmap = getCompressPhoto(path);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.WEBP, 80, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    public static Bitmap getCompressPhoto(String path) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inSampleSize = 2;
        Bitmap bmp = BitmapFactory.decodeFile(path, options);
        return bmp;
    }

    /**
     * 更新用户信息
     */
    public void updateInfo() {
        String email = mEtEmail.getText().toString().trim();
        String name = mEtName.getText().toString().trim();

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("token", userInfo.user_token);
        if (!EmptyUtils.emptyOfString(email)) {
            if (!InputCheckUtils.checkInputIsEmail(email)) {
                ToastUtil.showMsg("請填寫正確的郵箱");
                return;
            }
            paramsMap.put("email", email);
        }
        if (!EmptyUtils.emptyOfString(mHeadBase64)) {
            paramsMap.put("file", mHeadBase64);
        }
        if (isModify) {
            if (EmptyUtils.emptyOfString(name)) {
                ToastUtil.showMsg("請輸入用戶名");
                return;
            }
            paramsMap.put("display_name", name);
        }

        showLoading();
        UserApi userApi = RetrofitClient.getInstance().create(UserApi.class);

//        userApi.updateProfile(UserManager.instance().getToken(), email, name, mHeadBase64)
        userApi.updateProfile(paramsMap)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<UserProfile>() {
                    @Override
                    protected void onSuccess(UserProfile result) {
                        UserManager.instance().updateProfile(result);
                        hideLoading();
                        ToastUtil.showMsg("更新成功");
                        finish();
                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        ToastUtil.showMsg("更新失敗");
                        hideLoading();
                    }
                });
    }
}
