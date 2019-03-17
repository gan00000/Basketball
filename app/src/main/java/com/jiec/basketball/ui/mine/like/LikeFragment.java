package com.jiec.basketball.ui.mine.like;

import android.os.Bundle;

import com.jiec.basketball.adapter.MyCommentAdapter;
import com.jiec.basketball.base.BaseListAdapter;
import com.jiec.basketball.base.BaseListFragment;
import com.jiec.basketball.core.UserManager;
import com.jiec.basketball.entity.NewsBean;
import com.jiec.basketball.entity.response.LikeResponse;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.network.UserApi;
import com.jiec.basketball.ui.news.detail.DetaillWebActivity;

/**
 * 我的点赞
 * Created by Jiec on 2019/2/16.
 */
public class LikeFragment extends BaseListFragment {

    public static LikeFragment newInstance() {

        Bundle args = new Bundle();

        LikeFragment fragment = new LikeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadData(int page, int num) {
        UserApi userApi = RetrofitClient.getInstance().create(UserApi.class);
        userApi.getLike(UserManager.instance().getToken(), page)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<LikeResponse>() {
                    @Override
                    protected void onSuccess(LikeResponse result) {
                        showData(result.getSavedposts().getPages(), result.getSavedposts().getComments());
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

    @Override
    protected BaseListAdapter.OnItemClickedListener createItemClickedListener() {
        return data -> DetaillWebActivity.show(getContext(), ((NewsBean) data).getId());
    }
}
