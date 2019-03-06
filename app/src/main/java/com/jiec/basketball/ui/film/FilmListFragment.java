package com.jiec.basketball.ui.film;

import android.content.Intent;
import android.content.pm.ResolveInfo;
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

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeStandalonePlayer;
import com.jiec.basketball.R;
import com.jiec.basketball.entity.NewsBean;
import com.jiec.basketball.entity.response.NewListResponse;
import com.jiec.basketball.network.GameApi;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.ui.youtube.PlayerViewDemoActivity;
import com.wangcj.common.utils.LogUtil;
import com.wangcj.common.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.app.Activity.RESULT_OK;

/**
 * Description : 影片列表Fragment
 * Author : jiec
 * Date   : 18-12-26
 */
public class FilmListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final int REQ_START_STANDALONE_PLAYER = 1;
    private static final int REQ_RESOLVE_SERVICE_MISSING = 2;


    private static final String TAG = "FilmListFragment";

    private SwipeRefreshLayout mSwipeRefreshWidget;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private FilmListAdapter mAdapter;
    private List<NewsBean> mData;

    private int mPageIndex = 1;
    private int mPageCounts = 1;

    private boolean mIsLoadingData = false;

    public static FilmListFragment newInstance() {
        FilmListFragment fragment = new FilmListFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadData();
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
        mAdapter = new FilmListAdapter(getActivity().getApplicationContext());
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

    private FilmListAdapter.OnItemClickListener mOnItemClickListener = new FilmListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(NewsBean bean, int position) {

            PlayerViewDemoActivity.show(getContext(), bean.getVideoId());
//            Intent intent = YouTubeStandalonePlayer.createVideoIntent(getActivity(),
//                    YoutubeKey.KEY,
//                    bean.getVideoId(),
//                    0,
//                    true,
//                    false);
//            if (intent != null) {
//                if (canResolveIntent(intent)) {
//                    startActivityForResult(intent, REQ_START_STANDALONE_PLAYER);
//                } else {
//                    // Could not resolve the intent - must need to install or update the YouTube API service.
//                    YouTubeInitializationResult.SERVICE_MISSING
//                            .getErrorDialog(getActivity(), REQ_RESOLVE_SERVICE_MISSING).show();
//                }
//            }
        }
    };

    private boolean canResolveIntent(Intent intent) {
        List<ResolveInfo> resolveInfo = getActivity().getPackageManager().queryIntentActivities(intent, 0);
        return resolveInfo != null && !resolveInfo.isEmpty();
    }

    @Override
    public void onRefresh() {
        mPageIndex = 1;
        if (mData != null) {
            mData.clear();
        }
        showProgress();

        loadData();
    }

    private void loadData() {
        mIsLoadingData = true;

        GameApi server = RetrofitClient.getInstance().create(GameApi.class);
        server.getFilms(mPageIndex).subscribeOn(Schedulers.io())
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_START_STANDALONE_PLAYER && resultCode != RESULT_OK) {
            YouTubeInitializationResult errorReason =
                    YouTubeStandalonePlayer.getReturnedInitializationResult(data);
            if (errorReason.isUserRecoverableError()) {
                errorReason.getErrorDialog(getActivity(), 0).show();
            } else {
                String errorMessage =
                        String.format(getString(R.string.error_player), errorReason.toString());
                ToastUtil.showMsg(errorMessage);
            }
        }
    }

}
