package com.jiec.basketball.ui.film;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentTransaction;

import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseUIActivity;
import com.wangcj.common.widget.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 視頻播放頁面
 */
public class FilmPlayActivity extends BaseUIActivity {

    @BindView(R.id.titleBar)
    TitleBar mTitleBar;

    private String playUrl;

    public static void show(Context context, String  playUrl) {
        Intent mIntent = new Intent(context, FilmPlayActivity.class);
        mIntent.putExtra("playUrl", playUrl);
        context.startActivity(mIntent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fragment);
        ButterKnife.bind(this);
        playUrl = getIntent().getStringExtra("playUrl");
        mTitleBar.setTitle(R.string.film_play);

        FilmPlayFragment fragment = (FilmPlayFragment) getSupportFragmentManager()
                .findFragmentById(R.id.layout_fragment);
        ;
        if (fragment == null) {
            fragment = FilmPlayFragment.newInstance(playUrl);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.layout_fragment, fragment);
            transaction.commit();
        }
    }
}
