package com.jiec.basketball.ui.post;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.jiec.basketball.R;
import com.jiec.basketball.adapter.PostCommentAdapter;
import com.jiec.basketball.base.BaseUIFragment;
import com.jiec.basketball.core.BallApplication;
import com.jiec.basketball.entity.response.NewsCommentResponse;
import com.jiec.basketball.entity.response.NewsCommentResponse.ResultBean.CommentsBean;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.NewsApi;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.network.base.CommResponse;
import com.jiec.basketball.utils.EmptyUtils;
import com.wangcj.common.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jiec.basketball.core.BallApplication.userInfo;

/**
 * 評論跟帖fragment
 */
public class PostReplyFragment extends BaseUIFragment  {

    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout mRefreshLayout;

    @BindView(R.id.tv_hot_comment)
    TextView tvHot;

    @BindView(R.id.rv_hot_comment)
    RecyclerView rvHot;

    @BindView(R.id.tv_all_comment)
    TextView tvAll;

    @BindView(R.id.rv_all_comment)
    RecyclerView rvAll;

    @BindView(R.id.tv_comment)
    TextView tvComment;

    @BindView(R.id.layout_write_comment)
    LinearLayout mLayoutWriteComment;

    @BindView(R.id.et_comment)
    EditText mEtComment;

    private Context mContext;
    private String postId;
    private String reply_comment_id; //評論id
    private PostCommentAdapter hotAdapter;
    private PostCommentAdapter allAdapter;
    private int hotOffset = 0;
    private int allOffset = 0;
    private int pageSize = 10; //每次分頁加載10條數據
    private int commentPosition; //回復評論所在位置
    private int commentType = 1; //發佈評論類型：1=評論，2=熱門跟帖，3=全部跟帖


    public static PostReplyFragment newInstance(String postId) {

        Bundle args = new Bundle();
        args.putString("postId", postId);
        PostReplyFragment fragment = new PostReplyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this.getActivity();
        postId = this.getArguments().getString("postId");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRefreshLayout.setEnabled(false);
        rvHot.setLayoutManager(new LinearLayoutManager(mContext));
        rvAll.setLayoutManager(new LinearLayoutManager(mContext));
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
                commentType = 2;
                commentPosition = position;
                CommentsBean commentsBean = hotAdapter.getItem(position);
                reply_comment_id = commentsBean.getComment_id();

                mLayoutWriteComment.setVisibility(View.VISIBLE);
                mEtComment.setText("");
                mEtComment.setHint("回覆  "+commentsBean.getComment_author());
                mEtComment.setFocusableInTouchMode(true);
                mEtComment.setFocusable(true);
                mEtComment.requestFocus();
                showSoftInput();
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
                commentType = 3;
                commentPosition = position;
                CommentsBean commentsBean = allAdapter.getItem(position);
                reply_comment_id = commentsBean.getComment_id();

                mLayoutWriteComment.setVisibility(View.VISIBLE);
                mEtComment.setText("");
                mEtComment.setHint("回覆  "+commentsBean.getComment_author());
                mEtComment.setFocusableInTouchMode(true);
                mEtComment.setFocusable(true);
                mEtComment.requestFocus();
                showSoftInput();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showLoading();
        getHotComment();
    }

    @OnClick({R.id.tv_comment,  R.id.tv_send})
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

            case R.id.tv_send:
                hideSoftInput(mContext);
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
     * 獲取熱門評論
     * 測試：使用全部評論接口
     */
    private void getHotComment() {
        //getHotCommnet
        NewsApi newsApi = RetrofitClient.getInstance().create(NewsApi.class);
        newsApi.getHotCommnet(BallApplication.userInfo.user_token, postId, hotOffset)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<NewsCommentResponse>() {
                    @Override
                    protected void onSuccess(NewsCommentResponse result) {
                        hideLoading();
                        hotOffset++;
                        List<CommentsBean> hotList = result.getResult().getComments();
                        if(hotAdapter == null){
                           LogUtils.e("首次加載熱門評論");
                            if(!EmptyUtils.emptyOfList(hotList)){
                                tvHot.setVisibility(View.VISIBLE);
                                rvHot.setVisibility(View.VISIBLE);
                            }
                            hotAdapter = new PostCommentAdapter(2, hotList);
                            rvHot.setAdapter(hotAdapter);
                            hotAdapter.setEnableLoadMore(true);
                            hotAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                @Override
                                public void onLoadMoreRequested() {
                                    getHotComment();
                                }
                            }, rvHot);

                            if (hotList.size() < pageSize) {
                                getAllComment();
//                                mPullRefreshLayout.onLoadMoreEmptyCompelete();
                            }
                        }else{
                            LogUtils.e("加載更多熱門評論");
                            if (hotList.size() < pageSize) {
                                hotAdapter.loadMoreEnd(true);
                                getAllComment();
//                                mPullRefreshLayout.onLoadMoreEmptyCompelete();
                            } else {
                                hotAdapter.addData(hotList);
//                                mPullRefreshLayout.onSuccessCompelete();
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
        newsApi.getNewsCommnet(BallApplication.userInfo.user_token, postId, allOffset)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<NewsCommentResponse>() {
                    @Override
                    protected void onSuccess(NewsCommentResponse result) {
                        allOffset++;
                        List<CommentsBean> commentList = result.getResult().getComments();
                        if(allAdapter == null){
                            LogUtils.e("首次加載所有評論555");
                            allAdapter = new PostCommentAdapter(3, commentList);
                            allAdapter.bindToRecyclerView(rvAll);
                            if(EmptyUtils.emptyOfList(commentList)){
                                allAdapter.setEmptyView(R.layout.tipslayout_load_empty);
                            }

                            allAdapter.setEnableLoadMore(true);
                            allAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
                                @Override
                                public void onLoadMoreRequested() {
                                    getAllComment();
                                }
                            }, rvHot);

                        }else{
                            LogUtils.e("加載更多所有評論555");
                            if (commentList.size() < pageSize) {
                                allAdapter.loadMoreEnd();

//                                mPullRefreshLayout.onLoadMoreEmptyCompelete();
                            } else {
                                allAdapter.addData(commentList);
//                                mPullRefreshLayout.onSuccessCompelete();
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

                            CommentsBean commentsBean = new CommentsBean();
                            commentsBean.setPost_id(postId);
                            commentsBean.setUser_id(userInfo.user_id);
                            commentsBean.setComment_author(userInfo.display_name);
                            commentsBean.setUser_img(userInfo.user_img);
                            commentsBean.setComment_content(comment);
                            commentsBean.setComment_date(TimeUtils.getNowString());
                            commentsBean.setTotal_like("0");
                            commentsBean.setMy_like(0);
                            commentsBean.setTotal_reply("0");
                            allAdapter.addData(commentsBean);
//                            rvAll.scrollToPosition(allAdapter.getItemCount()-1);
                        }else {
                            ToastUtil.showMsg("回覆成功");
                            List<CommentsBean.ReplyBean> replyList = null;
                            CommentsBean.ReplyBean replyBean = new CommentsBean.ReplyBean();
                            replyBean.setPost_id(postId);
                            replyBean.setUser_id(userInfo.user_id);
                            replyBean.setComment_author(userInfo.display_name);
                            replyBean.setUser_img(userInfo.user_img);
                            replyBean.setComment_content(comment);
                            replyBean.setComment_date(TimeUtils.getNowString());
                            replyBean.setTotal_like("0");
                            replyBean.setMy_like(0);
                            switch (commentType){
                                case 2:
                                    replyList = hotAdapter.getItem(commentPosition).getReply();
                                    if(EmptyUtils.emptyOfList(replyList)){
                                        replyList = new ArrayList<>();
                                    }
                                    replyList.add(replyBean);
                                    hotAdapter.getItem(commentPosition).setReply(replyList);
                                    hotAdapter.notifyItemChanged(commentPosition);
                                    break;

                                case 3:
                                    replyList = allAdapter.getItem(commentPosition).getReply();
                                    if(EmptyUtils.emptyOfList(replyList)){
                                        replyList = new ArrayList<>();
                                    }
                                    replyList.add(replyBean);
                                    allAdapter.getItem(commentPosition).setReply(replyList);
                                    allAdapter.notifyItemChanged(commentPosition);
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



}
