package com.jiec.basketball.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Window;
import android.widget.TextView;

import com.jiec.basketball.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * function：loading对话框
 * Created by jiec on 2017/3/16.
 */

public class LoadingDialog extends Dialog {
    @BindView(R.id.loading_tips)
    TextView mLoadingTips;

    public LoadingDialog(Context context, String loadingText) {
        super(context, 0);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loading);

        setCanceledOnTouchOutside(false);
        ButterKnife.bind(this);

        if (mLoadingTips != null && loadingText != null) {
            mLoadingTips.setText(loadingText);
        }
    }

    public void setLoadingTips(String text) {
        if (mLoadingTips != null && !TextUtils.isEmpty(text)) {
            mLoadingTips.setText(text);
        }
    }
}
