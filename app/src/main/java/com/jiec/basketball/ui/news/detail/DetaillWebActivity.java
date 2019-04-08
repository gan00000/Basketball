package com.jiec.basketball.ui.news.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jiec.basketball.R;
import com.jiec.basketball.adapter.PostCommentAdapter;
import com.jiec.basketball.bean.EventReplyBean;
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
import com.jiec.basketball.ui.post.PostReplyActivity;
import com.jiec.basketball.utils.AppUtil;
import com.jiec.basketball.utils.ConstantUtils;
import com.jiec.basketball.utils.EmptyUtils;
import com.jiec.basketball.utils.EventBusEvent;
import com.jiec.basketball.utils.EventBusUtils;
import com.jiec.basketball.widget.RvDividerItemDecoration;
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

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import static com.jiec.basketball.core.BallApplication.userInfo;

/**
 * 新聞詳情頁面
 */
public class DetaillWebActivity extends BaseWebActivity {

    private static final String KEY_NEWS = "key_news";
    private static final String KEY_POSTID = "postId";
    private static final String KEY_TOTALCOMMENT = "totalComment";
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

    @BindView(R.id.tv_hot_comment)
    TextView tvHot;
    @BindView(R.id.rv_hot_comment)
    RecyclerView rvHot;
    @BindView(R.id.tv_all_comment)
    TextView tvAll;
    @BindView(R.id.rv_all_comment)
    RecyclerView rvAll;
    @BindView(R.id.tv_empty_comment)
    TextView tvEmptyComment;
    @BindView(R.id.ll_comment)
    LinearLayout llComment;
    @BindView(R.id.mNestedScrollView)
    NestedScrollView mNestedScrollView;

    private String mUrl;

    private LinearLayout mLoadingLayout;
    private AdView mAdViewBottom;
    private  TextView titleTv;
    private TextView kindTv;
    private TextView timeTv ;
    private TextView tvViews;

    private String postId; //新聞id
    private int totalComment;
    private String reply_comment_id; //評論id
    private NewsBean mNewsBean;
    boolean mIsCollect;
    private int commentPosition; //回復評論所在位置

    private RecyclerView mRecyclerView;
    private TopAdapter mAdapter;


    private Context mContext;
    private PostCommentAdapter hotAdapter;
    private PostCommentAdapter allAdapter;
    private int hotOffset = 0;
    private int allOffset = 0;
    private int pageSize = 10; //每次分頁加載10條數據
    private int commentType = 1; //發佈評論類型：1=評論，2=熱門跟帖，3=全部跟帖
    private boolean isHotRefresh = true;
    private boolean isAllRefresh = true;



    public static void show(Context context, NewsBean newsBean) {
        Intent intent = new Intent(context, DetaillWebActivity.class);
        intent.putExtra(KEY_NEWS, newsBean);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void show(Context context, String postId, int totalComment) {
        Intent intent = new Intent(context, DetaillWebActivity.class);
        intent.putExtra(KEY_POSTID, postId);
        intent.putExtra(KEY_TOTALCOMMENT, totalComment);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_web);
        mContext = DetaillWebActivity.this;
        ButterKnife.bind(this);
        postId = getIntent().getStringExtra(KEY_POSTID);
        totalComment = getIntent().getIntExtra(KEY_TOTALCOMMENT, 0);

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


        rvHot.setLayoutManager(new LinearLayoutManager(mContext));
        rvHot.addItemDecoration(new RvDividerItemDecoration(getResources().getColor(R.color.divider), 1));
        rvAll.setLayoutManager(new LinearLayoutManager(mContext));
        rvAll.addItemDecoration(new RvDividerItemDecoration(getResources().getColor(R.color.divider), 1));

        rvHot.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                if(userInfo == null){
                    ToastUtil.showMsg("請先登錄");
                    return;
                }
                showReplyEdit(2, position, -1);

            }
        });

        rvAll.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {

            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                super.onItemChildClick(adapter, view, position);
                if(userInfo == null){
                    ToastUtil.showMsg("請先登錄");
                    return;
                }
                showReplyEdit(3, position, -1);

            }
        });

    }

    /**
     * 显示回复子评论编辑试图
     * @param commentType
     * @param parentPosition  commentList索引
     * @param childPosition replyList索引   -1表示没有
     */
    private void showReplyEdit(int commentType, int parentPosition, int childPosition){
        this.commentType = commentType;
        CommentsBean commentsBean ;
        String replyToUser;
        if(commentType == 3){
            commentsBean = allAdapter.getItem(parentPosition);
        }else {
            commentsBean = hotAdapter.getItem(parentPosition);
        }
        if(childPosition != -1){
            ReplyBean replyBean = commentsBean.getReply().get(childPosition);
            reply_comment_id = replyBean.getComment_id();
            replyToUser = replyBean.getComment_author();
        }else {
            reply_comment_id = commentsBean.getComment_id();
            replyToUser = commentsBean.getComment_author();
        }

        mLayoutWriteComment.setVisibility(View.VISIBLE);
        mEtComment.setText("");
        mEtComment.setHint("回復  "+replyToUser);
        mEtComment.setFocusableInTouchMode(true);
        mEtComment.setFocusable(true);
        mEtComment.requestFocus();
        showSoftInput();
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
                            totalComment = mNewsBean.getTotal_comment();
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
        getHotComment();
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
        if (totalComment > 0) {
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


    private TopAdapter.OnItemClickListener mOnItemClickListener =
            newsBean -> DetaillWebActivity.show(DetaillWebActivity.this, newsBean.getId(), newsBean.getTotal_comment());


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
                //屏蔽广告
                mAdViewBottom.setVisibility(View.VISIBLE);
            }
        }, 4000);
    }

    /**
     * 獲取熱門評論
     * 測試：使用全部評論接口
     */
    private void getHotComment() {
        NewsApi newsApi = RetrofitClient.getInstance().create(NewsApi.class);
        newsApi.getHotCommnet(EmptyUtils.emptyOfObject(userInfo) ? "" : userInfo.user_token, postId, hotOffset)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<NewsCommentResponse>() {
                    @Override
                    protected void onSuccess(NewsCommentResponse result) {
                        hideLoading();
                        hotOffset++;
                        List<CommentsBean> hotList = result.getResult().getComments();
                        if(hotAdapter == null){
//                            LogUtils.e("首次加載熱門評論");
                            if(!EmptyUtils.emptyOfList(hotList)){
                                llComment.setVisibility(View.VISIBLE);
                                tvHot.setVisibility(View.VISIBLE);
                                rvHot.setVisibility(View.VISIBLE);
                            }
                            hotAdapter = new PostCommentAdapter(2, hotList);
                            rvHot.setAdapter(hotAdapter);
                            hotAdapter.setEnableLoadMore(true);
                            hotAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                @Override
                                public void onLoadMoreRequested() {
                                    isHotRefresh = false;
                                    getHotComment();
                                }
                            }, rvHot);

                            if (hotList.size() < pageSize) {
                                getAllComment();
                            }
                        }else{
//                            LogUtils.e("加載更多熱門評論");
                            if(isHotRefresh){
                                hotAdapter.setNewData(hotList);
                            }else {
                                if (hotList.size() < pageSize) {
                                    hotAdapter.loadMoreEnd(true);
                                    getAllComment();
                                } else {
                                    hotAdapter.addData(hotList);
                                }
                            }

                        }

                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        hideLoading();
                        ToastUtil.showMsg(reason);
                    }
                });
    }


    /**
     * 獲取所有評論
     */
    private void getAllComment() {
        NewsApi newsApi = RetrofitClient.getInstance().create(NewsApi.class);
        newsApi.getNewsCommnet(EmptyUtils.emptyOfObject(userInfo) ? "" : userInfo.user_token, postId, allOffset)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<NewsCommentResponse>() {
                    @Override
                    protected void onSuccess(NewsCommentResponse result) {
                        allOffset++;
                        List<CommentsBean> commentList = result.getResult().getComments();
                        if(allAdapter == null){
//                            LogUtils.e("首次加載所有評論555");
                            allAdapter = new PostCommentAdapter(3, commentList);
                            allAdapter.bindToRecyclerView(rvAll);
                            if(EmptyUtils.emptyOfList(commentList)){
                                allAdapter.setEmptyView(R.layout.tipslayout_load_empty);
                                tvEmptyComment.setVisibility(View.VISIBLE);
                            }else {
                                llComment.setVisibility(View.VISIBLE);
                            }

                            allAdapter.setEnableLoadMore(true);
                            allAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                @Override
                                public void onLoadMoreRequested() {
                                    isAllRefresh = false;
                                    getAllComment();
                                }
                            }, rvHot);

                        }else{
//                            LogUtils.e("加載更多所有評論555");
                            if(isAllRefresh){
                                allAdapter.setNewData(commentList);
                                tvEmptyComment.setVisibility(View.GONE);
                                llComment.setVisibility(View.VISIBLE);
                            }else {
                                if (commentList.size() < pageSize) {
                                    allAdapter.loadMoreEnd();
                                } else {
                                    allAdapter.addData(commentList);
                                }
                            }

                        }

                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        ToastUtil.showMsg(reason);
                    }
                });
    }


    @OnClick({R.id.tv_comment, R.id.iv_collect, R.id.iv_comment, R.id.tv_send})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_comment:
                if(userInfo == null){
                    ToastUtil.showMsg("請先登錄");
                    return;
                }
                commentType = 1;
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

            case R.id.iv_comment: //跳轉到評論区域
                mNestedScrollView.scrollTo(0, llComment.getTop());
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
        newsApi.comment(userInfo.user_token, postId, comment, reply_comment_id)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<CommResponse>() {
                    @Override
                    protected void onSuccess(CommResponse result) {
                        if(EmptyUtils.emptyOfString(reply_comment_id)){
                            ToastUtil.showMsg("評論成功");
                            isAllRefresh = true;
                            allOffset = 0;
                            mNestedScrollView.scrollTo(0, llComment.getTop());
                            getAllComment();
                        }else {
                            ToastUtil.showMsg("回復成功");
                            switch (commentType){
                                case 2:
                                    isHotRefresh = true;
                                    isAllRefresh = true;
                                    hotOffset = 0;
                                    allOffset = 0;
                                    getHotComment();
                                    break;

                                case 3:
                                    isAllRefresh = true;
                                    allOffset = 0;
                                    getAllComment();
                                    break;
                            }

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusEvent event) {
        switch (event.status){
            case ConstantUtils.EVENT_REPLY:
                if(event.data != null){
                    EventReplyBean eventReplyBean = (EventReplyBean) event.data;
                    LogUtils.e("回复子评论广播通知"+eventReplyBean.adapterType+
                            "=="+eventReplyBean.parentPosition+"=="+eventReplyBean.childPosition);
                    showReplyEdit(eventReplyBean.adapterType, eventReplyBean.parentPosition, eventReplyBean.childPosition);
                }
                break;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        EventBusUtils.registerEvent(this);
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
        EventBusUtils.unRegisteredEvent(this);
    }

}


