package com.jiec.basketball.ui.data.player;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseFragment;
import com.jiec.basketball.entity.AllPlayerData;
import com.jiec.basketball.network.GameApi;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.ui.data.DataUtils;
import com.jiec.basketball.widget.TipsLayout;
import com.trello.rxlifecycle.LifecycleTransformer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class PlayerDataDetailFragment extends BaseFragment {

    Unbinder unbinder;
    @BindView(R.id.recycle_view)
    RecyclerView mRecycleView;
    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout mSwipeRefreshWidget;

    PlayerDetailAdapter mPlayerDetailAdapter;
    @BindView(R.id.tipslayout)
    TipsLayout mTipslayout;

    int mType = DataUtils.TYPE_PTS;

    public static PlayerDataDetailFragment newInstance(int type) {
        PlayerDataDetailFragment fragment = new PlayerDataDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player_data_detail, container, false);
        unbinder = ButterKnife.bind(this, view);

        mType = getArguments().getInt("type");

        initView();
        loadData();
        mTipslayout.show(TipsLayout.TYPE_LOADING);

        return view;
    }

    private void loadData() {
        GameApi mService = RetrofitClient.getInstance().create(GameApi.class);
        Observable<AllPlayerData> observable = mService.getAllPlayerData(DataUtils.getTypeStr(mType), "desc");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose((LifecycleTransformer<AllPlayerData>) getBindToLifecycle())
                .subscribe(new Subscriber<AllPlayerData>() {
                    @Override
                    public void onCompleted() {
                        if (mSwipeRefreshWidget != null) {
                            mSwipeRefreshWidget.setRefreshing(false);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        mTipslayout.show(TipsLayout.TYPE_FAILED);
                    }

                    @Override
                    public void onNext(AllPlayerData commResponse) {
                        if (commResponse != null) {
                            mTipslayout.hide();
                            mPlayerDetailAdapter.setData(commResponse.getPlayers_rank());
                        } else {
                            mTipslayout.show(TipsLayout.TYPE_EMPTY_CONTENT);
                        }
                    }
                });
    }

    private void initView() {
        mSwipeRefreshWidget.setColorSchemeResources(R.color.primary,
                R.color.primary_dark, R.color.primary_light,
                R.color.accent);
        mSwipeRefreshWidget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        mRecycleView.setHasFixedSize(true);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecycleView.setLayoutManager(mLayoutManager);

        mRecycleView.setItemAnimator(new DefaultItemAnimator());

        mPlayerDetailAdapter = new PlayerDetailAdapter(mType);
        mRecycleView.setAdapter(mPlayerDetailAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
