package com.jiec.basketball.ui.post;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.LogUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jiec.basketball.R;
import com.jiec.basketball.adapter.MyCommentAdapter;
import com.jiec.basketball.adapter.PostCommentAdapter;
import com.jiec.basketball.base.BaseListAdapter;
import com.jiec.basketball.base.BaseListFragment;
import com.jiec.basketball.base.BaseUIFragment;
import com.jiec.basketball.core.UserManager;
import com.jiec.basketball.entity.NewsBean;
import com.jiec.basketball.entity.response.CommentResponse;
import com.jiec.basketball.entity.response.NewsCommentResponse;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.NewsApi;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.network.UserApi;
import com.jiec.basketball.ui.news.detail.DetaillWebActivity;
import com.jiec.basketball.utils.EmptyUtils;
import com.wangcj.common.utils.ToastUtil;
import com.wangcj.common.widget.PressImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.jiec.basketball.entity.response.NewsCommentResponse.ResultBean.CommentsBean;

import java.util.List;

/**
 * 評論跟帖fragment
 */
public class PostReplyFragment extends BaseUIFragment implements BaseQuickAdapter.RequestLoadMoreListener {

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

    private Context mContext;
    private String postId;
    private PostCommentAdapter hotAdapter;
    private PostCommentAdapter allAdapter;
    private int hotOffset = 0;
    private int allOffset = 0;
    private int pageSize = 10; //每次分頁加載10條數據


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
        rvHot.setLayoutManager(new LinearLayoutManager(mContext));

        rvAll.setLayoutManager(new LinearLayoutManager(mContext));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getHotComment();

    }

    /**
     * 獲取熱門評論
     * 測試：使用全部評論接口
     */
    private void getHotComment() {
        //getHotCommnet
        NewsApi newsApi = RetrofitClient.getInstance().create(NewsApi.class);
        newsApi.getNewsCommnet(UserManager.instance().getToken(), postId, hotOffset)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<NewsCommentResponse>() {
                    @Override
                    protected void onSuccess(NewsCommentResponse result) {
                        hotOffset++;
                        List<CommentsBean> hotList = result.getResult().getComments();
                        if(hotAdapter == null){
                           LogUtils.e("首次加載熱門評論");
                            hotAdapter = new PostCommentAdapter(hotList);
                            rvHot.setAdapter(hotAdapter);

                            hotAdapter.setEnableLoadMore(true);
                            hotAdapter.setOnLoadMoreListener(PostReplyFragment.this, rvHot);

                            if (hotList.size() < pageSize) {
                                getAllComment();
//                                mPullRefreshLayout.onLoadMoreEmptyCompelete();
                            }
                        }else{
                            LogUtils.e("加載更多熱門評論");
                            if (hotList.size() < pageSize) {
                                hotAdapter.loadMoreEnd();
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
                        ToastUtil.showMsg(reason);
                    }
                });
    }


    /**
     * 獲取所有評論
     */
    private void getAllComment() {
        NewsApi newsApi = RetrofitClient.getInstance().create(NewsApi.class);
        newsApi.getNewsCommnet(UserManager.instance().getToken(), postId, allOffset)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<NewsCommentResponse>() {
                    @Override
                    protected void onSuccess(NewsCommentResponse result) {
                        allOffset++;
                        List<CommentsBean> commentList = result.getResult().getComments();
                        if(allAdapter == null){
                            LogUtils.e("首次加載所有評論555");
                            allAdapter = new PostCommentAdapter(commentList);
                            rvAll.setAdapter(allAdapter);

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

    @Override
    public void onLoadMoreRequested() {
        getHotComment();
    }




}
