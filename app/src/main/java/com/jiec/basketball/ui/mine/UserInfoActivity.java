package com.jiec.basketball.ui.mine;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseActivity;
import com.jiec.basketball.core.UserManager;
import com.jiec.basketball.entity.UserProfile;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.network.UserApi;
import com.jiec.basketball.utils.EmptyUtils;
import com.jiec.basketball.utils.ImageLoaderUtils;
import com.jiec.basketball.utils.ImageUtil;
import com.jiec.basketball.utils.InputCheckUtils;
import com.jiec.basketball.utils.Lg;
import com.jiec.basketball.widget.CircleSImageView;
import com.wangcj.common.utils.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
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

//    private String mHeadBase64;
    private boolean isModify;  //是否可以更改
    private  String selectImagePath;

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
        setHeadImage();
    }

    private void setHeadImage() {
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
                String mmselectImagePath = data.getStringExtra("data");//返回真是路径
                //String outputUriStr = data.getStringExtra("outputUri");
                //mHeadBase64 = encodeBase64File(imagePath);
                if (StringUtils.isEmpty(mmselectImagePath)){
                    ToastUtil.showMsg("圖片選擇出錯");
                    return;
                }
                File imageFile = new File(mmselectImagePath);
                if (!imageFile.exists()){
                    Lg.e("图片不存在");
                    ToastUtil.showMsg("圖片選擇出錯");
                    return;
                }
//                ThreadUtils.postMainThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        //ImageLoaderUtils.loadBase64(mIvHead, mHeadBase64);
//                        Bitmap selectBitmap = BitmapFactory.decodeFile(mmselectImagePath);
//                        if (selectBitmap != null){
//                            selectImagePath = mmselectImagePath;
//                            mIvHead.setImageBitmap(selectBitmap);
//                        }else {
//                            ToastUtil.showMsg("圖片選擇出錯");
//                        }
//                       // mIvHead.setImageURI(Uri.parse(selectImagePath));
//                    }
//                });
                selectImagePath = mmselectImagePath;
                Glide.with(UserInfoActivity.this).load(imageFile).into(mIvHead);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static String encodeBase64File(String path) throws Exception {
//        Bitmap bitmap = getCompressPhoto(path);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.WEBP, 80, byteArrayOutputStream);
//        byte[] byteArray = byteArrayOutputStream.toByteArray();
        byte[] byteArray = ImageUtil.compressImage(path,512);
        if (byteArray != null){

            return Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        return "";
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
        if (!EmptyUtils.emptyOfString(selectImagePath)) {
            try {
                String imageBase64Str = encodeBase64File(selectImagePath);
                Lg.d("imageBase64Str:" + imageBase64Str);
                paramsMap.put("file", imageBase64Str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (isModify) {
            if (EmptyUtils.emptyOfString(name)) {
//                ToastUtil.showMsg("請輸入用戶名");
//                return;
            }else {
                paramsMap.put("display_name", name);
            }
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
                        //setHeadImage();
                        hideLoading();
                    }
                });
    }



}
