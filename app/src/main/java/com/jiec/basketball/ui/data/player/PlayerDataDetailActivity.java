package com.jiec.basketball.ui.data.player;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;

import com.flyco.tablayout.SlidingTabLayout;
import com.jiec.basketball.R;
import com.jiec.basketball.adapter.ViewPageAdapter;
import com.jiec.basketball.base.BaseActivity;
import com.jiec.basketball.ui.data.DataUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangchuangjie on 2018/4/15.
 */

public class PlayerDataDetailActivity extends BaseActivity {
    @BindView(R.id.tab_layout)
    SlidingTabLayout mTabLayout;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;

    public static void show(Context context, int type) {
        Intent intent = new Intent(context, PlayerDataDetailActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_player_data_detail);
        ButterKnife.bind(this);

        initView();

        mTabLayout.setCurrentTab(getIntent().getIntExtra("type", 0));
        mViewpager.setCurrentItem(getIntent().getIntExtra("type", 0));

    }

    private void initView() {
        ViewPageAdapter adapter = new ViewPageAdapter(getSupportFragmentManager());

        adapter.addFragment(PlayerDataDetailFragment.newInstance(DataUtils.TYPE_PTS), R.string.data_tab_pts);
        adapter.addFragment(PlayerDataDetailFragment.newInstance(DataUtils.TYPE_REB), R.string.data_tab_reb);
        adapter.addFragment(PlayerDataDetailFragment.newInstance(DataUtils.TYPE_AST), R.string.data_tab_ast);
        adapter.addFragment(PlayerDataDetailFragment.newInstance(DataUtils.TYPE_STL), R.string.data_tab_stl);
        adapter.addFragment(PlayerDataDetailFragment.newInstance(DataUtils.TYPE_BLK), R.string.data_tab_blk);
        adapter.addFragment(PlayerDataDetailFragment.newInstance(DataUtils.TYPE_TURNOVER), R.string.data_tab_turnover);
        mViewpager.setAdapter(adapter);
        mViewpager.setOffscreenPageLimit(1);
        mTabLayout.setViewPager(mViewpager, adapter.getTitles());

    }
}
