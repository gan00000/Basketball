package com.jiec.basketball.ui.mine.setting;

import android.os.Bundle;
import android.widget.TextView;

import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseActivity;
import com.jiec.basketball.utils.AppUtil;

public class AboutActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView infoTv = findViewById(R.id.tv_app_name);
        infoTv.setText(getString(R.string.app_name) + " " + AppUtil.getVersionName(this));
    }
}
