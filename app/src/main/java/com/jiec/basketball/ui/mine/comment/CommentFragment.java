package com.jiec.basketball.ui.mine.comment;

import android.os.Bundle;

import com.jiec.basketball.adapter.MyCommentAdapter;
import com.jiec.basketball.base.BaseListAdapter;
import com.jiec.basketball.base.BaseListFragment;
import com.jiec.basketball.core.UserManager;
import com.jiec.basketball.entity.NewsBean;
import com.jiec.basketball.entity.response.CommentResponse;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.network.UserApi;
import com.jiec.basketball.ui.news.NewsListAdapter;
import com.jiec.basketball.ui.news.detail.DetaillWebActivity;

/**
 * 我的评论fragment
 */
public class CommentFragment extends BaseListFragment {

    public static CommentFragment newInstance() {

        Bundle args = new Bundle();

        CommentFragment fragment = new CommentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadData(int page, int num) {
        UserApi userApi = RetrofitClient.getInstance().create(UserApi.class);
        userApi.getComments(UserManager.instance().getToken(), page)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<CommentResponse>() {
                    @Override
                    protected void onSuccess(CommentResponse result) {
                        showData(result.getPages(), result.getSavedposts().getComments());
                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        showError(reason);
                    }
                });
    }

    @Override
    protected BaseListAdapter createAdapter() {
        return new MyCommentAdapter(getContext());
    }

    /**item點擊事件*/
    @Override
    protected BaseListAdapter.OnItemClickedListener createItemClickedListener() {
        return data -> DetaillWebActivity.show(getContext(), (NewsBean) data);
    }

}
