package com.jiec.basketball.ui.game.detail.statistic;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Message;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bin.david.form.core.SmartTable;
import com.bin.david.form.data.CellInfo;
import com.bin.david.form.data.column.Column;
import com.bin.david.form.data.format.bg.BaseBackgroundFormat;
import com.bin.david.form.data.format.bg.BaseCellBackgroundFormat;
import com.bin.david.form.data.format.bg.ICellBackgroundFormat;
import com.bin.david.form.data.format.count.ICountFormat;
import com.bin.david.form.data.format.draw.FastTextDrawFormat;
import com.bin.david.form.data.format.grid.BaseGridFormat;
import com.bin.david.form.data.style.FontStyle;
import com.bin.david.form.data.style.LineStyle;
import com.bin.david.form.utils.DensityUtils;
import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseUIFragment;
import com.jiec.basketball.entity.GamePlayerData;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wangchuangjie on 2018/5/20.
 */

public class GameStatisticFragment extends BaseUIFragment {

    private static final int MSG_UPDATE_COUNT = 727;

    @BindView(R.id.st_statistic)
    SmartTable mStStatistic;

    Unbinder unbinder;

    public static GameStatisticFragment newInstance() {
        GameStatisticFragment fragment = new GameStatisticFragment();
        return fragment;
    }

    @Override
    public void handleUiMessage(Message msg) {
        super.handleUiMessage(msg);

        if (msg.what == MSG_UPDATE_COUNT) {
            updateCount((List<GamePlayerData>) msg.obj);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gamestatistic, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView();

        return view;
    }

    private void initTable(SmartTable smartTable) {

        FontStyle.setDefaultTextSize(DensityUtils.sp2px(getContext(), 16));
        FontStyle.setDefaultTextColor(getContext().getResources().getColor(R.color.black));

        smartTable.getConfig().setShowXSequence(false);
        smartTable.getConfig().setShowYSequence(false);
        smartTable.getTableTitle().setSize(DensityUtils.sp2px(getContext(), 18));

        FontStyle titleFontStyle = new FontStyle();
        titleFontStyle.setTextSpSize(getContext(),18);
        smartTable.getConfig().setColumnTitleStyle(titleFontStyle);//设置列标题文字样式

//        LineStyle gridLineStyle = new LineStyle();
//        gridLineStyle.setColor(ContextCompat.getColor(getActivity(), R.color.eef0f4));
//        smartTable.getConfig().setColumnTitleGridStyle(gridLineStyle);//设置列标题网格样式

        smartTable.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(ContextCompat.getColor(getActivity(), R.color.eef0f4)));

        smartTable.getConfig().setContentGridStyle(new LineStyle() {
            @Override
            public LineStyle setColor(int color) {
                return super.setColor(ContextCompat.getColor(getActivity(), R.color.black));
            }

        });


        smartTable.getConfig().setTableTitleStyle(new FontStyle(getContext(), 5, //设置表格标题文字样式
                getResources().getColor(R.color.black)));

        ICellBackgroundFormat<CellInfo> backgroundFormat = new BaseCellBackgroundFormat<CellInfo>() {
            @Override
            public int getBackGroundColor(CellInfo cellInfo) {
//                if (cellInfo.row % 2 == 0) {
//                    return ContextCompat.getColor(getActivity(), R.color.white);
//                } else {
//                    return TableConfig.INVALID_COLOR;
//                }

                if (cellInfo.row < 5) {
                    return ContextCompat.getColor(getActivity(), R.color.white);
                } else {
                    return ContextCompat.getColor(getActivity(), R.color.eef0f4);
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

                cellInfo.column.setDrawFormat(new FastTextDrawFormat(){//设置不同列的字体
                    @Override
                    protected void drawText(Canvas c, String value, Rect rect, Paint paint) {
                        if (col == 0) {
                            paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                        }else {
                            paint.setTypeface(Typeface.DEFAULT);
                        }
                        super.drawText(c, value, rect, paint);
                    }
                });

                if (col == 0) {
                    return true;
                }
                return false;
            }

            @Override
            protected boolean isShowHorizontalLine(int col, int row, CellInfo cellInfo) {
//                if (row == 0 || row == 5) {
//                    return true;
//                }
                return true;
            }
        });

        smartTable.getConfig().setFixedCountRow(true);

    }

    private void initView() {
        initTable(mStStatistic);
    }

    public void setData(List<GamePlayerData> gamePlayerData) {
        mStStatistic.setData(gamePlayerData);

        if (mStStatistic.getTableData() != null)
            mStStatistic.getTableData().setShowCount(true);

        Message msg = Message.obtain();
        msg.what = MSG_UPDATE_COUNT;
        msg.obj = gamePlayerData;

        sendUiMessageDelayed(msg, 1000);
    }

    private void updateCount(List<GamePlayerData> gamePlayerData) {
        int ftattSum = 0;
        int ftmakeSum = 0;
        int fgAttSum = 0;
        int fgMakeSum = 0;
        int fg3attSum = 0;
        int fg3makeSum = 0;

        if (gamePlayerData != null) {

            for (GamePlayerData data : gamePlayerData) {
                ftattSum += data.getFtatt();
                ftmakeSum += data.getFtmade();

                fgAttSum += data.getFgatt();
                fgMakeSum += data.getFgmade();

                fg3attSum += data.getFg3ptatt();
                fg3makeSum += data.getFg3ptmade();
            }
        }

        if (mStStatistic.getTableData() != null) {


            final String fgStr = fgMakeSum + " - " + fgAttSum;

            if (mStStatistic.getTableData().getColumnByID(7) != null) {


                mStStatistic.getTableData().getColumnByID(7).setCountFormat(new ICountFormat() {
                    @Override
                    public void count(Object o) {
                    }

                    @Override
                    public Number getCount() {
                        return 1;
                    }

                    @Override
                    public String getCountString() {
                        return fgStr;
                    }

                    @Override
                    public void clearCount() {

                    }
                });
            }


            final String fg3Str = fg3makeSum + " - " + fg3attSum;
            if (mStStatistic.getTableData().getColumnByID(8) != null) {


                mStStatistic.getTableData().getColumnByID(8).setCountFormat(new ICountFormat() {
                    @Override
                    public void count(Object o) {
                    }

                    @Override
                    public Number getCount() {
                        return 1;
                    }

                    @Override
                    public String getCountString() {
                        return fg3Str;
                    }

                    @Override
                    public void clearCount() {

                    }
                });
            }


            final String ftString = ftmakeSum + " - " + ftattSum;

            if (mStStatistic.getTableData().getColumnByID(9) != null) {


                mStStatistic.getTableData().getColumnByID(9).setCountFormat(new ICountFormat() {
                    @Override
                    public void count(Object o) {
                    }

                    @Override
                    public Number getCount() {
                        return 1;
                    }

                    @Override
                    public String getCountString() {
                        return ftString;
                    }

                    @Override
                    public void clearCount() {

                    }
                });
            }
        }
    }
}
