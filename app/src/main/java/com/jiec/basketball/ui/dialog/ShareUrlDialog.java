package com.jiec.basketball.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.jiec.basketball.R;
import com.jiec.basketball.entity.NewsBean;

/**
 * Created by Administrator on 2017/1/19.
 */

public class ShareUrlDialog extends Dialog {

    private static final int REQUEST_LOCAL_PHOTO_SHARE = 2100;
    private static final int REQUEST_URL_SHARE = 2101;
    private static final String TAG = "ShareUrlDialog";
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    Activity activity;

    private String shareUrl;
    private Bitmap bitmap;

    public ShareUrlDialog(final Activity activity, final NewsBean newsBean) {
        this(activity,newsBean.getUrl());
    }

    public ShareUrlDialog(final Activity activity, final String shareUrl) {
        super(activity);
        this.activity = activity;
        this.shareUrl = shareUrl;
        initShare(activity);

    }

    private void initShare(Activity activity) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_share);
        setDialogDefaultSize(activity);

        callbackManager = CallbackManager.Factory.create();

        shareDialog = new ShareDialog(activity);

       /* shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {//鏈接分享回調
            @Override
            public void onSuccess(Sharer.Result result) {
                Log.i(TAG,"fb url share onSuccess");
            }

            @Override
            public void onCancel() {
                Log.i(TAG,"fb url share onCancel");
            }

            @Override
            public void onError(FacebookException error) {
                Log.i(TAG,"fb url share onError");
            }
        });*/

        ImageView facebook = (ImageView) findViewById(R.id.iv_facebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                if (!TextUtils.isEmpty(shareUrl)){
                    ShareLinkContent content = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse(shareUrl))
                            //.setImageUrl(Uri.parse(newsBean.getImgsrc()))
                            .build();
                    shareDialog.show(content);

                }else if (bitmap != null){
                    shareLocalPhoto();
                }

            }
        });

        ImageView line = (ImageView) findViewById(R.id.iv_line);
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                if (TextUtils.isEmpty(shareUrl)){
                    return;
                }
                try {
                    String url = "http://line.me/R/msg/text/?" + shareUrl;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent .setData(Uri.parse(url));
                    activity.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public void setShareLocalPhoto(Bitmap bitmap){
        this.bitmap = bitmap;
    }
    private void shareLocalPhoto(){

        if (bitmap == null){
            return;
        }
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(bitmap)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();

        if (shareDialog != null && shareDialog.canShow(content)) {
            shareDialog.show(content);
        }
    }

    public void setDialogDefaultSize(Activity activity) {
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        lp.width = dm.widthPixels;
        dialogWindow.setAttributes(lp);
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent);
    }
}
