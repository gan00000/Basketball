package com.jiec.basketball.ui.data;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiec.basketball.R;
import com.jiec.basketball.entity.TeamData;
import com.jiec.basketball.network.GameApi;
import com.jiec.basketball.network.RetrofitClient;
import com.jiec.basketball.ui.data.team.TeamDataDetailActivity;
import com.jiec.basketball.widget.FiveTeamView;
import com.jiec.basketball.widget.TipsLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 *
 */
public class TeamDataFragment extends Fragment {

    @BindView(R.id.view_pts)
    FiveTeamView mViewPts;
    @BindView(R.id.view_reb)
    FiveTeamView mViewReb;
    @BindView(R.id.view_ast)
    FiveTeamView mViewAst;
    @BindView(R.id.view_stl)
    FiveTeamView mViewStl;
    @BindView(R.id.view_blk)
    FiveTeamView mViewBlk;
    @BindView(R.id.view_turnover)
    FiveTeamView mViewTurnover;
    Unbinder unbinder;
    @BindView(R.id.tipslayout)
    TipsLayout mTipslayout;
    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout mSwipeRefreshWidget;

    public static TeamDataFragment newInstance() {
        TeamDataFragment fragment = new TeamDataFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team_data, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView();
        loadData();
        mTipslayout.show(TipsLayout.TYPE_LOADING);

        return view;
    }

    private void loadData() {
        GameApi mService = RetrofitClient.getInstance().create(GameApi.class);
        Observable<TeamData> observable = mService.getTeamData("get_team_rank");
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TeamData>() {
                    @Override
                    public void onCompleted() {
                        mSwipeRefreshWidget.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mTipslayout.show(TipsLayout.TYPE_FAILED);
                    }

                    @Override
                    public void onNext(TeamData commResponse) {
                        if (commResponse != null) {
                            mViewPts.setData(commResponse.getPts());
                            mViewBlk.setData(commResponse.getBlk());
                            mViewAst.setData(commResponse.getAst());
                            mViewReb.setData(commResponse.getReb());
                            mViewStl.setData(commResponse.getStl());
                            mViewTurnover.setData(commResponse.getTurnover());

                            mTipslayout.hide();
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


        mViewPts.setTitle(getString(R.string.data_tab_pts));
        mViewPts.setMoreListener(new DebouncingOnClickListener() {
            @Override
            public void doClick(View v) {
                TeamDataDetailActivity.show(getContext(), DataUtils.TYPE_PTS);
            }
        });

        mViewReb.setTitle(getString(R.string.data_tab_reb));
        mViewReb.setMoreListener(new DebouncingOnClickListener() {
            @Override
            public void doClick(View v) {
                TeamDataDetailActivity.show(getContext(), DataUtils.TYPE_REB);
            }
        });

        mViewAst.setTitle(getString(R.string.data_tab_ast));
        mViewAst.setMoreListener(new DebouncingOnClickListener() {
            @Override
            public void doClick(View v) {
                TeamDataDetailActivity.show(getContext(), DataUtils.TYPE_AST);
            }
        });

        mViewStl.setTitle(getString(R.string.data_tab_stl));
        mViewStl.setMoreListener(new DebouncingOnClickListener() {
            @Override
            public void doClick(View v) {
                TeamDataDetailActivity.show(getContext(), DataUtils.TYPE_STL);
            }
        });


        mViewBlk.setTitle(getString(R.string.data_tab_blk));
        mViewBlk.setMoreListener(new DebouncingOnClickListener() {
            @Override
            public void doClick(View v) {
                TeamDataDetailActivity.show(getContext(), DataUtils.TYPE_BLK);
            }
        });

        mViewTurnover.setTitle(getString(R.string.data_tab_turnover));
        mViewTurnover.setMoreListener(new DebouncingOnClickListener() {
            @Override
            public void doClick(View v) {
                TeamDataDetailActivity.show(getContext(), DataUtils.TYPE_TURNOVER);
            }
        });

        mTipslayout.setOnRefreshButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });

        mTipslayout.setOnRetryButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
