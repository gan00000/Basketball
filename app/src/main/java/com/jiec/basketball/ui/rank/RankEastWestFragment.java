package com.jiec.basketball.ui.rank;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.core.TableConfig;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.format.grid.BaseGridFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.style.LineStyle;
import com.bin.david.form.utils.DensityUtils;
import com.jiec.basketball.R;
import com.jiec.basketball.entity.EastWestTeamRank;
import com.jiec.basketball.network.GameApi;
import com.jiec.basketball.network.RetrofitClient;
import com.wangcj.common.utils.ToastUtil;

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
public class RankEastWestFragment extends Fragment {


    Unbinder unbinder;

    @BindView(R.id.st_team_rank_east)
    SmartTable mStTeamRankEast;
    @BindView(R.id.st_team_rank_west)
    SmartTable mStTeamRankWest;


    public static RankEastWestFragment newInstance() {
        RankEastWestFragment fragment = new RankEastWestFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_east_west_rank, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView();
        loadData();

        return view;
    }

    private void loadData() {
        GameApi mService = RetrofitClient.getInstance().create(GameApi.class);
        Observable<EastWestTeamRank> observable = mService.getEastWestRank();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<EastWestTeamRank>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtil.showMsg(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(EastWestTeamRank commResponse) {
                        if (commResponse != null && commResponse.getStatus().equals("ok")) {
                            mStTeamRankEast.setData(commResponse.getEastern());
                            mStTeamRankWest.setData(commResponse.getWestern());

                        } else {
                            ToastUtil.showMsg(R.string.network_error);
                        }
                    }
                });
    }

    private void initView() {
        initTableview(mStTeamRankEast);
        initTableview(mStTeamRankWest);
    }

    private void initTableview(SmartTable smartTable) {
        FontStyle.setDefaultTextSize(DensityUtils.sp2px(getContext(), 15));

        smartTable.getConfig().setShowXSequence(false);
        smartTable.getConfig().setShowYSequence(false);
        smartTable.getConfig().setShowTableTitle(false);

        smartTable.getConfig().setContentGridStyle(new LineStyle() {
            @Override
            public LineStyle setColor(int color) {
                return super.setColor(ContextCompat.getColor(getActivity(), R.color.black));
            }
        });


        smartTable.getConfig().setTableTitleStyle(new FontStyle(getContext(), 18,
                getResources().getColor(R.color.black)));
        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(getActivity(), R.color.gray_light);
                } else {
                    return TableConfig.INVALID_COLOR;
                }
            }
        };


        smartTable.getConfig().setContentCellBackgroundFormat(backgroundFormat);


        smartTable.getConfig().setTableGridFormat(new BaseGridFormat() {
            @Override
            protected boolean isShowColumnTitleVerticalLine(int col, Column column) {
                return false;
            }

            @Override
            protected boolean isShowVerticalLine(int col, int row, CellInfo cellInfo) {
                return false;
            }

            @Override
            protected boolean isShowHorizontalLine(int col, int row, CellInfo cellInfo) {
                return true;
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


}
