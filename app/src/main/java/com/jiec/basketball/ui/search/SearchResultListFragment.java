package com.jiec.basketball.ui.search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.jiec.basketball.R;
import com.jiec.basketball.entity.NewsBean;
import com.jiec.basketball.entity.response.HomeResponse;
import com.jiec.basketball.entity.response.NewListResponse;
import com.jiec.basketball.network.GameApi;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.ui.news.detail.DetaillWebActivity;
import com.wangcj.common.utils.LogUtil;
import com.wangcj.common.utils.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Description : 搜索结果列表Fragment
 */
public class SearchResultListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "NewsListFragment";
    private static String SearchResultListFragment_searchKey = "SearchResultListFragment_searchKey";

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private SearchResultListAdapter mAdapter;
    private List<NewsBean> mData;

    private int mPageIndex = 1;
    private int mPageCounts = 1;
    private static final int count = 10;

    private boolean mIsLoadingData = false;

    private boolean isOpenLoadMore = true;

//    private List<NewsBean> mBannerNews;
    private String searchKey = "";

    public static SearchResultListFragment newInstance(String searchKey) {
        Bundle args = new Bundle();
        args.putString(SearchResultListFragment_searchKey,searchKey);
        SearchResultListFragment fragment = new SearchResultListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchKey = getArguments().getString(SearchResultListFragment_searchKey,"");

        loadData();
//        loadBanner();
    }

    @Override
    public void onResume() {
        super.onResume();
//        onRefresh();  //???????
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
        mSwipeRefreshWidget.setEnabled(false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new SearchResultListAdapter(getActivity().getApplicationContext());
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
            if (isOpenLoadMore && newState == RecyclerView.SCROLL_STATE_IDLE
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

    private SearchResultListAdapter.OnItemClickListener mOnItemClickListener = new SearchResultListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            NewsBean newsBean = mAdapter.getItem(position);
            DetaillWebActivity.show(getActivity(), newsBean.getId(), newsBean.getTotal_comment());
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
       /* if (mType == NEWS_TYPE_NEWS) {
            loadBanner();
        }*/
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
       /* mBannerNews = newsBeanList;
        if (mBannerNews == null || mBannerNews.size() == 0) {
            return;
        }

        mAdapter.setBannerData(mBannerNews);*/
    }

    /**
     * 加载获取新闻列表
     */
    private void loadData() {
        if (StringUtils.isEmpty(searchKey)){
            return;
        }
        mIsLoadingData = true;
        GameApi mService = RetrofitClient.getInstance().create(GameApi.class);
        Observable<NewListResponse> observable = mService.getSearchResult(searchKey, mPageIndex, count);
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
//                        loadData();
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

        if (mData == null) {
            mData = new ArrayList<NewsBean>();
        }

        if (mPageIndex == 1) {
            mAdapter.setmDate(mData);
        }

        if (newsList == null || newsList.size() == 0) {
            ToastUtil.showMsg("没有更多数据！");
            hideLoadingMore();
            return;
        }
        mData.addAll(newsList);

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
