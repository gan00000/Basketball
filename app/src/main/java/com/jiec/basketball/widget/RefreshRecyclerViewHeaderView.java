package com.jiec.basketball.widget;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiec.basketball.R;
import com.wangcj.common.widget.irecyclerview.RefreshTrigger;


/**
 * Created by aspsine on 16/3/14.
 */
public class RefreshRecyclerViewHeaderView extends RelativeLayout implements RefreshTrigger {
    private static final String TAG = "RefreshHeaderView";

    private TextView mTvRefresh;

    private ProgressBar mPbLoading;

    private boolean mRefreshing = false;

    private int mHeight;

    public RefreshRecyclerViewHeaderView(Context context) {
        this(context, null);
    }

    public RefreshRecyclerViewHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshRecyclerViewHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mTvRefresh = (TextView) findViewById(R.id.tvRefresh);

        mPbLoading = (ProgressBar) findViewById(R.id.progress_loading_bar);

    }

    @Override
    public void onStart(boolean automatic, int headerHeight, int finalHeight) {
        this.mHeight = headerHeight;
    }

    @Override
    public void onMove(boolean isComplete, boolean automatic, int moved) {
        if (!isComplete && !mRefreshing) {
            mTvRefresh.setVisibility(VISIBLE);
            mPbLoading.setVisibility(VISIBLE);
            if (moved <= mHeight) {
                mTvRefresh.setText("下拉刷新");
            } else {
                mTvRefresh.setText("释放刷新");
            }
        }
    }

    @Override
    public void onRefresh() {
        mRefreshing = true;

        mTvRefresh.setText("正在加载");
    }

    @Override
    public void onRelease() {
        onRefresh();
    }

    @Override
    public void onComplete() {
        mRefreshing = false;
        mPbLoading.setVisibility(GONE);
        mTvRefresh.setText("加载完成");
    }

    @Override
    public void onReset() {
        mRefreshing = false;
        mPbLoading.setVisibility(GONE);
        mTvRefresh.setVisibility(View.GONE);
    }
}
