package com.jiec.basketball.base;


import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiec.basketball.R;
import com.jiec.basketball.widget.TipsLayout;
import com.wangcj.common.utils.NetWorkUtil;
import com.wangcj.common.utils.ToastUtil;
import com.wangcj.common.widget.irecyclerview.IRecyclerView;
import com.wangcj.common.widget.irecyclerview.OnLoadMoreListener;
import com.wangcj.common.widget.irecyclerview.OnRefreshListener;
import com.wangcj.common.widget.irecyclerview.footer.LoadMoreFooterView;

import java.util.List;

import butterknife.ButterKnife;


/**
 * 列表fragment，实现分页功能，下拉刷新，上拉加载更多
 * Created by jiec on 2017/2/22.
 */

public abstract class BaseListFragment extends BaseUIFragment {

    public static final int DEFLAUT_NUMS = 10;

    IRecyclerView mRecyclerView;

    TipsLayout mTipsLayout;

    LoadMoreFooterView mLoadMoreFooterView;

    private int mCurrentPage = 0;

    private int mPageNum = DEFLAUT_NUMS;

    private int mTotolPage = 0;

    BaseListAdapter mBaseListAdapter;

    /**
     * 加载数据
     *
     * @param page 页数
     * @param num  每页数量
     */
    protected abstract void loadData(int page, int num);

    protected abstract BaseListAdapter createAdapter();

    protected abstract BaseListAdapter.OnItemClickedListener createItemClickedListener();

    protected int getLayoutResourceID() {
        return -1;
    }

    protected int getPageNum() {
        return mPageNum;
    }

    public void setPageNum(int num) {
        mPageNum = num;
    }


    public int getCurrentPage() {
        return mCurrentPage;
    }

    public boolean isFirstPage() {
        return mCurrentPage == 0;
    }

    private int initCurrentPage() {
        this.mCurrentPage = 0;
        return mCurrentPage;
    }

    /**
     * 更新数据，加载过程会有loading动画
     */
    public void updateData() {
        if (mBaseListAdapter == null || mBaseListAdapter.getItemCount() <= 0) {
            loading();
        }

        initCurrentPage();
        setLoadMoreEnabled(false);
        loadData(mCurrentPage, getPageNum());
    }

    private void setLoadMoreEnabled(boolean enabled) {
        if (mRecyclerView != null) {
            mRecyclerView.setLoadMoreEnabled(enabled);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layoutid = getLayoutResourceID();
        View view = inflater.inflate((layoutid > 0 ?
                layoutid : R.layout.fragment_base_list), container, false);
        ButterKnife.bind(this, view);
        initView(view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        updateData();
    }

    /**
     * 倒序 消息聊天界面的模式
     *
     * @return
     */
    protected boolean isReverseLayout() {
        return false;
    }

    protected void initView(View view) {
        mTipsLayout = (TipsLayout) view.findViewById(R.id.tipsLayout);
        hideTipsLayout();
        mRecyclerView = (IRecyclerView) view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(isReverseLayout());
        mRecyclerView.setLayoutManager(layoutManager);
        mLoadMoreFooterView = (LoadMoreFooterView) mRecyclerView.getLoadMoreFooterView();
        mLoadMoreFooterView.setOnRetryListener(new LoadMoreFooterView.OnRetryListener() {
            @Override
            public void onRetry(LoadMoreFooterView view) {
                //加载更多尾部重试逻辑
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData(mCurrentPage, getPageNum());
                    }
                }, 1000);
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
            }
        });

        mRecyclerView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetWorkUtil.isNetworkConnected(getContext())) {
                    updateData();
                } else {
                    //网络异常情况下，下拉刷新弹toast提示错误
                    ToastUtil.showMsg("请检测你的网络，确保网络正常");
                    stopRefreshing();
                }
            }
        });

        mBaseListAdapter = createAdapter();
        mRecyclerView.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mCurrentPage >= mTotolPage) {
                    return;
                }

                mRecyclerView.setLoadMoreEnabled(false);
                mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.LOADING);
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData(++mCurrentPage, getPageNum());
                    }
                }, 1000);

            }
        });
        if (mBaseListAdapter != null) {
            mBaseListAdapter.setOnItemClickedListener(createItemClickedListener());
            mRecyclerView.setIAdapter(mBaseListAdapter);
        }
    }

    /**
     * 设置recycleview的LinearManager
     */
    protected void setLayoutManager(RecyclerView.LayoutManager layoutManage) {
        mRecyclerView.setLayoutManager(layoutManage);
        mRecyclerView.setIAdapter(mBaseListAdapter);
    }

    /**
     * 添加头部
     *
     * @param head
     */
    protected void addHeaderView(View head) {
        mRecyclerView.addHeaderView(head);
    }

    /**
     * 设置不可下拉刷新
     */
    public void setNoRefresh() {
        if (mRecyclerView != null) {
            mRecyclerView.setRefreshEnabled(false);
        }
    }

    private void loading() {
        if (mTipsLayout != null) {
            mTipsLayout.show(TipsLayout.TYPE_LOADING);
        }
    }

    private void hideTipsLayout() {
        if (mTipsLayout != null) {
            mTipsLayout.hide();
        }
    }

    /**
     * 显示数据
     *
     * @param totalPage
     * @param data
     */
    public void showData(int totalPage, List<?> data) {

        int dataSize = 0;
        if (data != null) dataSize = data.size();

        stopRefreshing();

        mTotolPage = totalPage;

        if (mLoadMoreFooterView != null) {
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.GONE);
        }

        hideTipsLayout();

        if (totalPage <= mCurrentPage) {
            setLoadMoreEnabled(false);
            //mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.THE_END);
        } else {
            setLoadMoreEnabled(true);
        }

        if (isFirstPage()) {
            mBaseListAdapter.setData(data);
        } else {
            mBaseListAdapter.addData(data);
        }


        if (dataSize == 0 && isFirstPage()) {
            showEmpty();
        }
    }

    /**
     * 没数据
     */
    protected void showEmpty() {
        stopRefreshing();
        setLoadMoreEnabled(false);

        if (mTipsLayout != null) {
            mTipsLayout.show(TipsLayout.TYPE_EMPTY_CONTENT);
            mTipsLayout.setOnRefreshButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateData();
                }
            });
        }
    }

    /**
     * 设置空界面的图标和文案
     * @param iconId
     */
    protected void setEmptyIcon(@DrawableRes int iconId) {
        if (mTipsLayout != null) {
            mTipsLayout.setEmptyIcon(iconId);
        }
    }

    /**
     * 设置空界面文案
     * @param tips
     */
    protected void setEmptyTips(String tips) {
        if (mTipsLayout != null) {
            mTipsLayout.setEmptyText(tips);
        }
    }


    /**
     * 显示错误页面
     *
     * @param reason
     */
    protected void showFailed(String reason) {
        stopRefreshing();
        setLoadMoreEnabled(false);

        if (mTipsLayout != null && isFirstPage()) {
            mTipsLayout.show(TipsLayout.TYPE_FAILED);
            mTipsLayout.setErrorText(reason);
            mTipsLayout.setOnRetryButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateData();
                }
            });
        }

        if (!isFirstPage() && mLoadMoreFooterView != null) {
            mLoadMoreFooterView.setStatus(LoadMoreFooterView.Status.ERROR);
        }
    }

    private void stopRefreshing() {
        if (mRecyclerView != null) {
            mRecyclerView.setRefreshing(false);
        }
    }
}
