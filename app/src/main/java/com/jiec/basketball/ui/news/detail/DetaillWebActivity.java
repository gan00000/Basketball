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

import com.blankj.utilcode.util.TimeUtils;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jiec.basketball.R;
import com.jiec.basketball.bean.NewsDetailModel;
import com.jiec.basketball.core.BallApplication;
import com.jiec.basketball.core.UserManager;
import com.jiec.basketball.entity.NewsBean;
import com.jiec.basketball.entity.TopPost;
import com.jiec.basketball.entity.response.HistoryResponse;
import com.jiec.basketball.entity.response.NewsCommentResponse;
import com.jiec.basketball.entity.response.NewsCommentResponse.ResultBean.CommentsBean.ReplyBean;
import com.jiec.basketball.network.GameApi;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.NewsApi;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.network.UserApi;
import com.jiec.basketball.network.base.CommResponse;
import com.jiec.basketball.ui.dialog.ShareUrlDialog;
import com.jiec.basketball.utils.AppUtil;
import com.jiec.basketball.utils.EmptyUtils;
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
import com.jiec.basketball.entity.response.NewsCommentResponse.ResultBean.CommentsBean;

import java.util.ArrayList;
import java.util.List;

import static com.jiec.basketball.core.BallApplication.userInfo;

/**
 * 新聞詳情頁面
 */
public class DetaillWebActivity extends BaseWebActivity {

    private static final String TAG = "TopicActivity";

    private static final String KEY_NEWS = "key_news";
    private static final String KEY_POSTID = "postId";
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
    private  TextView titleTv;
    private TextView kindTv;
    private TextView timeTv ;
    private TextView tvViews;

    private String postId; //新聞id
    private String reply_comment_id; //評論id
    private NewsBean mNewsBean;
    boolean mIsCollect;
    private int commentPosition; //回復評論所在位置

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

    public static void show(Context context, String postId) {
        Intent intent = new Intent(context, DetaillWebActivity.class);
        intent.putExtra(KEY_POSTID, postId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_web);
        ButterKnife.bind(this);
        postId = getIntent().getStringExtra(KEY_POSTID);
        postId = "125345";

        initView();
        getNewsDetail();

        if (BallApplication.userInfo != null) {
            saveHistory();
        }
    }

    /**
     * 初始化View
     */
    private void initView() {
        mLoadingLayout =  findViewById(R.id.layout_loading);
        mWebView = findViewById(R.id.webview);
        initWebView();

        titleTv = findViewById(R.id.tv_news_title);
        kindTv = findViewById(R.id.tv_kind);
        timeTv = findViewById(R.id.tv_time);
        tvViews = findViewById(R.id.tv_views);

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
        mCommentAdapter.setItemChildClickListener(itemChildClickListener);
        mCommentRecyclerView.setAdapter(mCommentAdapter);
        mCommentRecyclerView.addOnScrollListener(mOnScrollListener);

        mEtComment.setOnFocusChangeListener((view, b) -> mLayoutWriteComment.setVisibility(
                b ? View.VISIBLE : View.GONE));
        mAdViewBottom = findViewById(R.id.adView_bottom);
        AdRequest adRequestBottom = new AdRequest.Builder().build();
        mAdViewBottom.loadAd(adRequestBottom);

        ImageView ivShare = findViewById(R.id.iv_share);
        ivShare.setOnClickListener(v -> {
            ShareUrlDialog dialog = new ShareUrlDialog(DetaillWebActivity.this, mNewsBean);
            dialog.show();
        });

    }


    /**
     * 根据id获取新闻详情
     */
    private void getNewsDetail() {
        showLoading();
        GameApi gameApi = RetrofitClient.getInstance().create(GameApi.class);
        gameApi.getPostDetail(postId)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<NewsDetailModel>() {
                    @Override
                    protected void onSuccess(NewsDetailModel result) {
                        hideLoading();
                        if(result.isSuccess()){
                            mNewsBean = result.post;
                            setDetailData();
                        }
                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        hideLoading();
                        showError(reason);
                    }
                });
    }

    /**
     * 设置详情数据
     */
    private void setDetailData(){
        titleTv.setText(Html.fromHtml(mNewsBean.getTitle()));
        kindTv.setText(mNewsBean.getKind());
        timeTv.setText(AppUtil.getStandardDate(mNewsBean.getDate()));
        tvViews.setText(mNewsBean.getSumViews() + "");
        mUrl = mNewsBean.getUrl();
        parseHtml(mUrl);
        loadTopPosts();
        updateBottomInfo();
        getComments();
    }

    /**
     * 保存到歷史記錄
     */
    private void saveHistory() {
        UserApi userApi = RetrofitClient.getInstance().create(UserApi.class);
        userApi.saveHistory(userInfo.user_token, postId)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<HistoryResponse>() {
                    @Override
                    protected void onSuccess(HistoryResponse result) {

                    }

                    @Override
                    protected void onFailed(int code, String reason) {
//                        showError(reason);
                    }
                });
    }

    /**
     * 更新底部UI
     */
    private void updateBottomInfo() {
        if (mNewsBean.getComment_count() > 0) {
            mTvCommentNum.setText("" + mNewsBean.getComment_count());
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
                if(userInfo == null){
                    ToastUtil.showMsg("請先登錄");
                    return;
                }
                mLayoutWriteComment.setVisibility(View.VISIBLE);
                reply_comment_id = "";

                mEtComment.setText("");
                mEtComment.setHint("請輸入你的評論");
                mEtComment.setFocusableInTouchMode(true);
                mEtComment.setFocusable(true);
                mEtComment.requestFocus();
                showSoftInput();
                break;

            case R.id.iv_collect:
                if(userInfo == null){
                    ToastUtil.showMsg("請先登錄");
                    return;
                }
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
     * 評論列表回復按鈕點擊事件處理
     */
    private NewsCommentAdapter.ItemChildClickListener itemChildClickListener
            = new NewsCommentAdapter.ItemChildClickListener() {
        @Override
        public void onItemChildClick(View view, int position) {
            if(userInfo == null){
                ToastUtil.showMsg("請先登錄");
                return;
            }
            commentPosition = position;
            CommentsBean commentsBean = mCommentAdapter.getItem(position);
            reply_comment_id = commentsBean.getComment_id();

            mLayoutWriteComment.setVisibility(View.VISIBLE);
            mEtComment.setText("");
            mEtComment.setHint("回復  "+commentsBean.getComment_author());
            mEtComment.setFocusableInTouchMode(true);
            mEtComment.setFocusable(true);
            mEtComment.requestFocus();
            showSoftInput();
        }
    };

    /**
     * 發表評論
     * @param comment
     */
    private void comment(String comment) {

        NewsApi newsApi = RetrofitClient.getInstance().create(NewsApi.class);
        newsApi.comment(userInfo.user_token, mNewsBean.getId(), comment, reply_comment_id)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<CommResponse>() {
                    @Override
                    protected void onSuccess(CommResponse result) {
                        if(EmptyUtils.emptyOfString(reply_comment_id)){
                            ToastUtil.showMsg("評論成功");

                            CommentsBean commentsBean = new CommentsBean();
                            commentsBean.setPost_id(postId);
                            commentsBean.setUser_id(userInfo.user_id);
                            commentsBean.setComment_author(userInfo.display_name);
                            commentsBean.setUser_img(userInfo.user_img);
                            commentsBean.setComment_content(comment);
                            commentsBean.setComment_date(TimeUtils.getNowString());
                            commentsBean.setTotal_like("0");
                            commentsBean.setMy_like(0);
                            mCommentAdapter.addItemData(commentsBean);
                            mCommentRecyclerView.scrollToPosition(mCommentAdapter.getItemCount()-1);
                        }else {
                            ToastUtil.showMsg("回復成功");
                            List<ReplyBean> replyList = mCommentAdapter.getItem(commentPosition).getReply();
                            if(EmptyUtils.emptyOfList(replyList)){
                                replyList = new ArrayList<>();
                            }
                            ReplyBean replyBean = new ReplyBean();
                            replyBean.setPost_id(postId);
                            replyBean.setUser_id(userInfo.user_id);
                            replyBean.setComment_author(userInfo.display_name);
                            replyBean.setUser_img(userInfo.user_img);
                            replyBean.setComment_content(comment);
                            replyBean.setComment_date(TimeUtils.getNowString());
                            replyBean.setTotal_like("0");
                            replyBean.setMy_like(0);
                            replyList.add(replyBean);
                            mCommentAdapter.getItem(commentPosition).setReply(replyList);
                            mCommentAdapter.notifyDataSetChanged();
//                            mCommentAdapter.notifyItemChanged(commentPosition);
                        }


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
        newsApi.collect(userInfo.user_token, postId)
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
        newsApi.cancleCollect(userInfo.user_token, postId)
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
        newsApi.getNewsCommnet(UserManager.instance().getToken(), postId, mCurrentPage)
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


