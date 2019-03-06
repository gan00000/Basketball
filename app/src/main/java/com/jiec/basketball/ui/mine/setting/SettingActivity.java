package com.jiec.basketball.ui.mine.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseActivity;
import com.jiec.basketball.core.DataCleanManager;
import com.jiec.basketball.core.UserManager;
import com.jiec.basketball.utils.BallPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.cb_receive_notify)
    CheckBox mCbReceiveNotify;
    @BindView(R.id.tv_cache_size)
    TextView mTvCacheSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        mCbReceiveNotify.setChecked(BallPreferencesUtils.getInstance().getAutoReceiveMsgNotify());
        mCbReceiveNotify.setOnCheckedChangeListener(
                (buttonView, isChecked) -> BallPreferencesUtils.getInstance().setAutoReceiveMsgNotify(isChecked));

        updataCache();
    }

    private void updataCache() {
        try {
            mTvCacheSize.setText(DataCleanManager.getTotalCacheSize(this.getApplicationContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.rl_setting_cache, R.id.rl_setting_service, R.id.rl_setting_about, R.id.rl_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_setting_cache:
                DataCleanManager.clearAllCache(SettingActivity.this);
                updataCache();
                break;
            case R.id.rl_setting_service:
                startActivity(new Intent(SettingActivity.this, CustomerServiceActivity.class));
                break;
            case R.id.rl_setting_about:
                startActivity(new Intent(SettingActivity.this, AboutActivity.class));
                break;
            case R.id.rl_logout:
                UserManager.instance().logout();
                break;
        }
    }
}
