package com.jiec.basketball.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.jiec.basketball.R;


/**
 * Created by Administrator on 2016/10/15.
 */
public class UpdateDialog extends Dialog {

    TextView mUpdateTV, mTitleTv, mMessageTv;

    Activity mContext;

    public UpdateDialog(Activity context) {
        super(context);

        mContext = context;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_update);

        mTitleTv = (TextView) findViewById(R.id.tv_title);
        mMessageTv = (TextView) findViewById(R.id.tv_message);
        mUpdateTV = (TextView) findViewById(R.id.tv_update);
        setDialogDefaultSize();

        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
    public void setDialogDefaultSize() {
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
    }

    public void setTitle(String title) {
        mTitleTv.setText(title);
    }

    public void setMessage(String message) {
        mMessageTv.setText(message);
    }


    public void setUPdateListener(View.OnClickListener listener) {
            mUpdateTV.setOnClickListener(listener);
    }
}
