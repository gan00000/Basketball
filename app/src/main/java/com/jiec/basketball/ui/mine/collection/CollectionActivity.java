package com.jiec.basketball.ui.mine.collection;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseActivity;
import com.wangcj.common.widget.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jiec on 2019/1/13.
 */
public class CollectionActivity extends BaseActivity {

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

        mTitleBar.setTitle(R.string.mine_collection);

        CollectionFragment fragment = (CollectionFragment) getSupportFragmentManager()
                .findFragmentById(R.id.layout_fragment);
        ;
        if (fragment == null) {
            fragment = CollectionFragment.newInstance();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.layout_fragment, fragment);
            transaction.commit();
        }
    }
}
