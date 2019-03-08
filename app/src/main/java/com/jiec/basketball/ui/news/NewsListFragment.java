package com.jiec.basketball.ui.news;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiec.basketball.R;
import com.jiec.basketball.entity.NewsBean;
import com.jiec.basketball.entity.response.HomeResponse;
import com.jiec.basketball.entity.response.NewListResponse;
import com.jiec.basketball.network.GameApi;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.ui.news.detail.DetaillWebActivity;
import com.wangcj.common.utils.LogUtil;
import com.wangcj.common.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description : 列表Fragment
 * Author : jiec
 * Date   : 17-1-6
 */
public class NewsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public static final int NEWS_TYPE_NEWS = 0;

    private static final String TAG = "NewsListFragment";

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private NewsAdapter mAdapter;
    private List<NewsBean> mData;

    private int mType = NEWS_TYPE_NEWS;
    private int mPageIndex = 1;
    private int mPageCounts = 1;

    private boolean mIsLoadingData = false;

    private List<NewsBean> mBannerNews;

    public static NewsListFragment newInstance() {
        Bundle args = new Bundle();
        NewsListFragment fragment = new NewsListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();

        loadBanner();
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newslist, null);

        mSwipeRefreshWidget = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_widget);
        mSwipeRefreshWidget.setColorSchemeResources(R.color.primary,
                R.color.primary_dark, R.color.primary_light,
                R.color.accent);
        mSwipeRefreshWidget.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new NewsAdapter(getActivity().getApplicationContext());
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(mOnScrollListener);

        return view;
    }

    private RecyclerView.OnScrollListener mOnScrollListener = new RecyclerView.OnScrollListener() {

        private int lastVisibleItem;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();
        }

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == RecyclerView.SCROLL_STATE_IDLE
                    && lastVisibleItem + 1 == mAdapter.getItemCount()
                    && mPageCounts >= mPageIndex
                    && !mIsLoadingData) {
                //加载更多
                Log.d(TAG, "loading more data");

                showLoadingMore();
                loadData();
            }
        }
    };

    private NewsAdapter.OnItemClickListener mOnItemClickListener = new NewsAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            NewsBean newsBean = mAdapter.getItem(position);
            DetaillWebActivity.show(getActivity(), newsBean.getId());
        }
    };

    @Override
    public void onRefresh() {
        mPageIndex = 1;
        if (mData != null) {
            mData.clear();
        }
        showProgress();

        loadData();
        if (mType == NEWS_TYPE_NEWS) {
            loadBanner();
        }
    }

    private void loadBanner() {
        GameApi mService = RetrofitClient.getInstance().create(GameApi.class);
        Observable<HomeResponse> observable = mService.getHomePage();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<HomeResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(HomeResponse commResponse) {
                        loadBanner(commResponse.getTopslideposts().getPosts());
                    }
                });
    }

    private void loadBanner(List<NewsBean> newsBeanList) {
        mBannerNews = newsBeanList;
        if (mBannerNews == null || mBannerNews.size() == 0) {
            return;
        }

        mAdapter.setBannerData(mBannerNews);
    }

    private void loadData() {
        mIsLoadingData = true;

        GameApi mService = RetrofitClient.getInstance().create(GameApi.class);
        Observable<NewListResponse> observable = mService.getNews(mPageIndex);
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<NewListResponse>() {
                    @Override
                    public void onCompleted() {
                        Log.e("test", "onComleted");
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        Log.e("test", "onError");
                        showLoadFailMsg();
                        mIsLoadingData = false;
                        loadData();
                    }

                    @Override
                    public void onNext(NewListResponse response) {
                        Log.e("test", "onNext" + response.toString());

                        mPageCounts = response.getPages();

                        addNews(response.getPosts(), mPageCounts <= mPageIndex);

                        hideProgress();
                        mIsLoadingData = false;
                    }
                });
    }

    public void showProgress() {
        mSwipeRefreshWidget.setRefreshing(true);
    }

    public void hideProgress() {
        mSwipeRefreshWidget.setRefreshing(false);
    }


    public void addNews(List<NewsBean> newsList, boolean isEnd) {
        if (newsList == null || newsList.size() == 0) {
            ToastUtil.showMsg("没有更多数据！");
            return;
        }

        if (mData == null) {
            mData = new ArrayList<NewsBean>();
        }

        mData.addAll(newsList);
        if (mPageIndex == 1) {
            mAdapter.setmDate(mData);
        }

        hideLoadingMore();

        mPageIndex++;
    }


    public void showLoadFailMsg() {
        hideLoadingMore();
        LogUtil.d("数据加载失败，请重试！！");
    }

    private void showLoadingMore() {
        mAdapter.isShowFooter(true);
        mAdapter.notifyDataSetChanged();
    }

    public void hideLoadingMore() {
        mAdapter.isShowFooter(false);
        mAdapter.notifyDataSetChanged();
    }

}
