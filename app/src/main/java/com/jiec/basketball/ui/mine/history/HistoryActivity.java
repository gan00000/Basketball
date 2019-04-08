package com.jiec.basketball.ui.mine.history;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseActivity;
import com.jiec.basketball.ui.mine.collection.CollectionActivity;
import com.wangcj.common.widget.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jiec on 2019/1/13.
 */
public class HistoryActivity extends BaseActivity {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;

    public static void show(Context context) {
        context.startActivity(new Intent(context, CollectionActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment);
        ButterKnife.bind(this);

        mTitleBar.setTitle(R.string.mine_history);

        HistoryFragment fragment = (HistoryFragment) getSupportFragmentManager()
                .findFragmentById(R.id.layout_fragment);
        ;
        if (fragment == null) {
            fragment = HistoryFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.layout_fragment, fragment);
            transaction.commit();
        }
    }
}
