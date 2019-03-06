package com.jiec.basketball.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.jiec.basketball.R;
import com.jiec.basketball.entity.NewsBean;

/**
 * Created by Administrator on 2017/1/19.
 */

public class ShareUrlDialog extends Dialog {

    CallbackManager callbackManager;
    ShareDialog shareDialog;

    public ShareUrlDialog(final Activity activity, final NewsBean newsBean) {
        super(activity);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_share);

        setDialogDefaultSize(activity);

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(activity);


        ImageView facebook = (ImageView) findViewById(R.id.iv_facebook);
        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                ShareLinkContent content = new ShareLinkContent.Builder()
                        .setContentUrl(Uri.parse(newsBean.getUrl()))
                        .setImageUrl(Uri.parse(newsBean.getImgsrc()))
                        .build();
                shareDialog.show(content);
            }
        });

        ImageView line = (ImageView) findViewById(R.id.iv_line);
        line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();

                try {
                    String url = "http://line.me/R/msg/text/?" + newsBean.getUrl();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent .setData(Uri.parse(url));
                    activity.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

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
