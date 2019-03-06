package com.jiec.basketball.ui.mine.notify;

import android.os.Bundle;

import com.jiec.basketball.base.BaseListAdapter;
import com.jiec.basketball.base.BaseListFragment;
import com.jiec.basketball.core.UserManager;
import com.jiec.basketball.entity.response.NotifyResponse;
import com.jiec.basketball.network.NetSubscriber;
import com.jiec.basketball.network.NetTransformer;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.network.UserApi;

/**
 * Created by Jiec on 2019/2/16.
 */
public class NotifyFragment extends BaseListFragment {

    public static NotifyFragment newInstance() {

        Bundle args = new Bundle();

        NotifyFragment fragment = new NotifyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected void loadData(int page, int num) {
        UserApi userApi = RetrofitClient.getInstance().create(UserApi.class);
        userApi.getNotify(UserManager.instance().getToken(), page)
                .compose(new NetTransformer<>())
                .subscribe(new NetSubscriber<NotifyResponse>() {
                    @Override
                    protected void onSuccess(NotifyResponse result) {
                        showData(result.getResult().getPages(), result.getResult().getNotification());
                    }

                    @Override
                    protected void onFailed(int code, String reason) {
                        showError(reason);
                    }
                });
    }

    @Override
    protected BaseListAdapter createAdapter() {
        return new NotifyAdapter(getContext());
    }

    @Override
    protected BaseListAdapter.OnItemClickedListener createItemClickedListener() {
        return null;
    }
}
