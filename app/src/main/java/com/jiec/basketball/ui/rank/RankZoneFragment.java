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
import com.bin.david.form.data.format.grid.BaseGridFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.style.LineStyle;
import com.bin.david.form.utils.DensityUtils;
import com.jiec.basketball.R;
import com.jiec.basketball.entity.ZoneTeamRank;
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
public class RankZoneFragment extends Fragment {


    Unbinder unbinder;
    @BindView(R.id.st_eastern_atlantic)
    SmartTable mStEasternAtlantic;
    @BindView(R.id.st_eastern_central)
    SmartTable mStEasternCentral;
    @BindView(R.id.st_eastern_southeast)
    SmartTable mStEasternSoutheast;
    @BindView(R.id.st_western_northwest)
    SmartTable mStWesternNorthwest;
    @BindView(R.id.st_western_pacific)
    SmartTable mStWesternPacific;
    @BindView(R.id.st_western_southwest)
    SmartTable mStWesternSouthwest;


    public static RankZoneFragment newInstance() {
        RankZoneFragment fragment = new RankZoneFragment();
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_zone_rank, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView();
        loadData();

        return view;
    }

    private void loadData() {
        GameApi mService = RetrofitClient.getInstance().create(GameApi.class);
        Observable<ZoneTeamRank> observable = mService.getZoneRank();
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ZoneTeamRank>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        ToastUtil.showMsg(e.getLocalizedMessage());
                    }

                    @Override
                    public void onNext(ZoneTeamRank commResponse) {
                        if (commResponse != null && commResponse.getStatus().equals("ok")) {
                            mStEasternAtlantic.setData(commResponse.getEasternAtlantic());
                            mStEasternCentral.setData(commResponse.getEasternCentral());
                            mStEasternSoutheast.setData(commResponse.getEasternSoutheast());

                            mStWesternNorthwest.setData(commResponse.getWesternNorthwest());
                            mStWesternPacific.setData(commResponse.getWesternPacific());
                            mStWesternSouthwest.setData(commResponse.getWesternSouthwest());

                        } else {
                            ToastUtil.showMsg(R.string.network_error);
                        }
                    }
                });
    }

    private void initView() {
        initTableview(mStEasternAtlantic);
        initTableview(mStEasternCentral);
        initTableview(mStEasternSoutheast);
        initTableview(mStWesternNorthwest);
        initTableview(mStWesternPacific);
        initTableview(mStWesternSouthwest);
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


        smartTable.getConfig().setContentCellBackgroundFormat(new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
                if (cellInfo.row % 2 == 0) {
                    return ContextCompat.getColor(getActivity(), R.color.gray_light);
                } else {
                    return TableConfig.INVALID_COLOR;
                }
            }
        });

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
