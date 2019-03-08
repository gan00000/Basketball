package com.jiec.basketball.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.jiec.basketball.R;
import com.jiec.basketball.entity.NewsBean;
import com.jiec.basketball.ui.news.NewsAdapter;
import com.jiec.basketball.ui.news.detail.DetaillWebActivity;
import com.wangcj.common.widget.TitleBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Jiec on 2019/1/26.
 */
public abstract class BaseListActivity extends BaseActivity {
    private static final String TAG = "CollectionActivity";
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.recycle_view)
    public RecyclerView mRecycleView;
    @BindView(R.id.tv_tips)
    public TextView mTvTips;

    public NewsAdapter mAdapter;


    private NewsAdapter.OnItemClickListener mOnItemClickListener = new NewsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            NewsBean newsBean = mAdapter.getItem(position);
            DetaillWebActivity.show(BaseListActivity.this, newsBean.getId());
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);
        ButterKnife.bind(this);

        init();
        loadData();
    }

    public void init() {
        mRecycleView = findViewById(R.id.recycle_view);

        mRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new NewsAdapter(getApplicationContext());
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mAdapter.isShowFooter(false);
        mRecycleView.setAdapter(mAdapter);
    }

    public void setTitle(String title) {
        mTitleBar.setTitle(title);
    }

    protected abstract void loadData();

    protected void showEmpty() {
        mTvTips.setText("空空如也");
    }

    protected void showError() {
        mTvTips.setVisibility(View.VISIBLE);
        mTvTips.setText("加載失敗");
        hideLoading();
    }

    public void setData(List<NewsBean> newsList) {
        hideLoading();
        if (newsList == null || newsList.size() == 0) {
            mTvTips.setVisibility(View.VISIBLE);
            showEmpty();
            return;
        }

        mTvTips.setVisibility(View.GONE);
        mAdapter.setmDate(newsList);
    }
}
