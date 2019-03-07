package com.jiec.basketball.ui.news.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jiec.basketball.R;
import com.jiec.basketball.core.BallApplication;
import com.jiec.basketball.core.UserManager;
import com.jiec.basketball.entity.NewsBean;
import com.jiec.basketball.entity.TopPost;
import com.jiec.basketball.entity.response.HistoryResponse;
import com.jiec.basketball.entity.response.NewsCommentResponse;
import com.jiec.basketball.network.GameApi;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.NewsApi;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.network.UserApi;
import com.jiec.basketball.network.base.CommResponse;
import com.jiec.basketball.ui.dialog.ShareUrlDialog;
import com.jiec.basketball.utils.AppUtil;
import com.wangcj.common.utils.ThreadUtils;
import com.wangcj.common.utils.ToastUtil;
import com.wangcj.common.widget.PressImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 新聞詳情頁面
 */
public class DetaillWebActivity extends BaseWebActivity {

    private static final String TAG = "TopicActivity";

    private static final String KEY_NEWS = "key_news";
    @BindView(R.id.tv_comment)
    TextView mTvComment;
    @BindView(R.id.iv_collect)
    PressImageView mIvCollect;
    @BindView(R.id.iv_comment)
    PressImageView mIvComment;
    @BindView(R.id.tv_comment_num)
    TextView mTvCommentNum;
    @BindView(R.id.layout_write_comment)
    LinearLayout mLayoutWriteComment;
    @BindView(R.id.et_comment)
    EditText mEtComment;

    private String mUrl;

    private LinearLayout mLoadingLayout;

    private AdView mAdViewBottom;

    private NewsBean mNewsBean;

    boolean mIsCollect;


    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private TopAdapter mAdapter;

    private int mPageCounts = 1;

    private boolean mIsLoadingData = false;


    @BindView(R.id.recycle_comment)
    RecyclerView mCommentRecyclerView;
    private NewsCommentAdapter mCommentAdapter;
    private int mCurrentPage = 0;

    public static void show(Context context, NewsBean newsBean) {
        Intent intent = new Intent(context, DetaillWebActivity.class);
        intent.putExtra(KEY_NEWS, newsBean);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_web);
        ButterKnife.bind(this);

        mNewsBean = (NewsBean) getIntent().getSerializableExtra(KEY_NEWS);
        if (mNewsBean == null) {
            ToastUtil.showMsg("数据有误，请刷新列表");
            return;
        }
        mUrl = mNewsBean.getUrl();

        ImageView ivShare = findViewById(R.id.iv_share);
        ivShare.setOnClickListener(v -> {
            ShareUrlDialog dialog = new ShareUrlDialog(DetaillWebActivity.this, mNewsBean);
            dialog.show();
        });

        initView();

        parseHtml(mUrl);

        mAdViewBottom = findViewById(R.id.adView_bottom);
        AdRequest adRequestBottom = new AdRequest.Builder().build();
        mAdViewBottom.loadAd(adRequestBottom);

        loadTopPosts();

        updateBottomInfo();

        if (BallApplication.userInfo != null) {
            saveHistory();
        }

    }

    /**
     * 保存到歷史記錄
     */
    private void saveHistory() {
        UserApi userApi = RetrofitClient.getInstance().create(UserApi.class);
        userApi.saveHistory(UserManager.instance().getToken(), mNewsBean.getId())
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<HistoryResponse>() {
                    @Override
                    protected void onSuccess(HistoryResponse result) {

                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        showError(reason);
                    }
                });
    }

    private void updateBottomInfo() {
        if (mNewsBean.getTotal_comment() > 0) {
            mTvCommentNum.setText("" + mNewsBean.getTotal_comment());
        } else {
            mTvCommentNum.setVisibility(View.GONE);
        }

        mIsCollect = mNewsBean.getMy_save() > 0;
        updateCollectState();
    }

    private void loadTopPosts() {
        GameApi mService = RetrofitClient.getInstance().create(GameApi.class);
        Observable<TopPost> observable = mService.getTopPosts();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TopPost>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(TopPost commResponse) {
                        mAdapter.setData(commResponse.getPosts());
                    }
                });
    }

    private void initView() {

        mLoadingLayout = findViewById(R.id.layout_loading);

        mWebView = findViewById(R.id.webview);
        initWebView();


        TextView titleTv = findViewById(R.id.tv_news_title);
        titleTv.setText(Html.fromHtml(mNewsBean.getTitle()));

        TextView kindTv = findViewById(R.id.tv_kind);
        kindTv.setText(mNewsBean.getKind());

        TextView timeTv = findViewById(R.id.tv_time);
        timeTv.setText(AppUtil.getStandardDate(mNewsBean.getDate()));

        ((TextView) findViewById(R.id.tv_views)).setText(mNewsBean.getSumViews() + "");


        mRecyclerView = findViewById(R.id.recycle_view);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mAdapter = new TopAdapter(getApplicationContext());
        mAdapter.setOnItemClickListener(mOnItemClickListener);
        mRecyclerView.setAdapter(mAdapter);

        mLayoutManager = new LinearLayoutManager(this);
        mCommentRecyclerView.setLayoutManager(mLayoutManager);
        mCommentRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCommentAdapter = new NewsCommentAdapter(getApplicationContext());
        mCommentRecyclerView.setAdapter(mCommentAdapter);
        mCommentRecyclerView.addOnScrollListener(mOnScrollListener);

        mEtComment.setOnFocusChangeListener((view, b) -> mLayoutWriteComment.setVisibility(
                b ? View.VISIBLE : View.GONE));

        getComments();
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
                    && mPageCounts >= mCurrentPage
                    && !mIsLoadingData) {
                //加载更多
                Log.d(TAG, "loading more data");

                showLoadingMore();
                getComments();
            }
        }
    };

    private TopAdapter.OnItemClickListener mOnItemClickListener =
            newsBean -> DetaillWebActivity.show(DetaillWebActivity.this, newsBean);


    public void showLoading() {
        ThreadUtils.postMainThread(new Runnable() {
            @Override
            public void run() {
                mWebView.setVisibility(View.GONE);
                mAdViewBottom.setVisibility(View.GONE);
                mLoadingLayout.setVisibility(View.VISIBLE);
            }
        });
    }

    public void hideLoading() {
        ThreadUtils.postMainThread(new Runnable() {
            @Override
            public void run() {
                if (mWebView != null)
                    mWebView.setVisibility(View.VISIBLE);
                if (mLoadingLayout != null)
                    mLoadingLayout.setVisibility(View.GONE);
            }
        });

        ThreadUtils.postMainThread(new Runnable() {
            @Override
            public void run() {
                mAdViewBottom.setVisibility(View.VISIBLE);
            }
        }, 4000);
    }

    @OnClick({R.id.tv_comment, R.id.iv_collect, R.id.iv_comment, R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_comment:
                mLayoutWriteComment.setVisibility(View.VISIBLE);
                mEtComment.setFocusableInTouchMode(true);
                mEtComment.setFocusable(true);
                mEtComment.requestFocus();
                showSoftInput();
                break;
            case R.id.iv_collect:
                if (mIsCollect) {
                    uncollect();
                } else {
                    collect();
                }
                break;
            case R.id.iv_comment:
                break;
            case R.id.tv_send:
                hideSoftInput(this);
                mLayoutWriteComment.setVisibility(View.GONE);
                if (TextUtils.isEmpty(mEtComment.getText())) {
                    ToastUtil.showMsg("請輸入評論");
                } else {
                    comment(mEtComment.getText().toString());
                }
                break;
        }
    }

    /**
     * 發表評論
     * @param comment
     */
    private void comment(String comment) {
        NewsApi newsApi = RetrofitClient.getInstance().create(NewsApi.class);
        newsApi.comment(UserManager.instance().getToken(), mNewsBean.getId(), comment)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<CommResponse>() {
                    @Override
                    protected void onSuccess(CommResponse result) {
                        ToastUtil.showMsg("評論成功");
                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        ToastUtil.showMsg(reason);
                    }
                });
    }

    /**
     * 收藏
     */
    private void collect() {
        NewsApi newsApi = RetrofitClient.getInstance().create(NewsApi.class);
        newsApi.collect(UserManager.instance().getToken(), mNewsBean.getId())
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<CommResponse>() {
                    @Override
                    protected void onSuccess(CommResponse result) {
                        mIsCollect = true;
                        updateCollectState();
                        ToastUtil.showMsg("已收藏");
                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        ToastUtil.showMsg(reason);
                    }
                });
    }

    private void updateCollectState() {
        if (mIsCollect) {
            mIvCollect.setImageResource(R.drawable.icon_collect_pressed);
        } else {
            mIvCollect.setImageResource(R.drawable.icon_collect);
        }
    }


    /**
     * 取消收藏
     */
    private void uncollect() {
        NewsApi newsApi = RetrofitClient.getInstance().create(NewsApi.class);
        newsApi.cancleCollect(UserManager.instance().getToken(), mNewsBean.getId())
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<CommResponse>() {
                    @Override
                    protected void onSuccess(CommResponse result) {
                        mIsCollect = false;
                        updateCollectState();
                        ToastUtil.showMsg("已取消收藏");
                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        ToastUtil.showMsg(reason);
                    }
                });
    }

    /**
     * 獲取評論
     */
    private void getComments() {
        NewsApi newsApi = RetrofitClient.getInstance().create(NewsApi.class);
        newsApi.getNewsCommnet(UserManager.instance().getToken(), mNewsBean.getId(), mCurrentPage)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<NewsCommentResponse>() {
                    @Override
                    protected void onSuccess(NewsCommentResponse result) {
                        mCommentAdapter.addData(result.getResult().getComments());

                        hideLoadingMore();
                        mPageCounts = result.getResult().getPages();
                        mCurrentPage++;
                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        ToastUtil.showMsg(reason);
                        hideLoadingMore();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mLayoutWriteComment.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLayoutWriteComment.setVisibility(View.GONE);
    }

    private void showLoadingMore() {
        mCommentAdapter.isShowFooter(true);
        mCommentAdapter.notifyDataSetChanged();
    }

    public void hideLoadingMore() {
        mCommentAdapter.isShowFooter(false);
        mCommentAdapter.notifyDataSetChanged();
    }
}


