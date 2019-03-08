package com.jiec.basketball.ui.mine.history;

import android.os.Bundle;

import com.jiec.basketball.base.BaseListAdapter;
import com.jiec.basketball.base.BaseListFragment;
import com.jiec.basketball.core.UserManager;
import com.jiec.basketball.entity.NewsBean;
import com.jiec.basketball.entity.response.HistoryResponse;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.network.UserApi;
import com.jiec.basketball.ui.news.NewsListAdapter;
import com.jiec.basketball.ui.news.detail.DetaillWebActivity;

/**
 * Created by Jiec on 2019/2/16.
 */
public class HistoryFragment extends BaseListFragment {

    public static HistoryFragment newInstance() {

        Bundle args = new Bundle();

        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadData(int page, int num) {
        UserApi userApi = RetrofitClient.getInstance().create(UserApi.class);
        userApi.getHistory(UserManager.instance().getToken(), page)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<HistoryResponse>() {
                    @Override
                    protected void onSuccess(HistoryResponse result) {
                        showData(result.getHistoryposts().getPages(), result.getHistoryposts().getPosts());
                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        showError(reason);
                    }
                });
    }

    @Override
    protected BaseListAdapter createAdapter() {
        return new NewsListAdapter(getContext());
    }

    @Override
    protected BaseListAdapter.OnItemClickedListener createItemClickedListener() {
        return data -> DetaillWebActivity.show(getContext(), ((NewsBean) data).getId());
    }
}
