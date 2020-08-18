package com.jiec.basketball.ui.game.detail.statistic;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

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
import com.bin.david.form.listener.OnColumnItemClickListener;
import com.bin.david.form.utils.DensityUtils;
import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseUIFragment;
import com.jiec.basketball.entity.GamePlayerData;
import com.jiec.basketball.entity.MatchSummary;
import com.jiec.basketball.ui.game.detail.GameDetailActivity;
import com.jiec.basketball.ui.game.detail.PlayerMatchSummaryActivity;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Comparator;
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
    SmartTable mStStatisticSmartTable;

    boolean isHomeTeam = true;

    boolean isLiving = false;

    public boolean isHomeTeam() {
        return isHomeTeam;
    }

    public void setHomeTeam(boolean homeTeam) {
        isHomeTeam = homeTeam;
    }

    Unbinder unbinder;

    private List<GamePlayerData> gamePlayerDatas;

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

        FontStyle.setDefaultTextSize(DensityUtils.sp2px(getContext(), 14));
        FontStyle.setDefaultTextColor(getContext().getResources().getColor(R.color.black));

        smartTable.getConfig().setShowXSequence(false);
        smartTable.getConfig().setShowYSequence(false);
        smartTable.getTableTitle().setSize(DensityUtils.sp2px(getContext(), 16));

        FontStyle titleFontStyle = new FontStyle();
        titleFontStyle.setTextSpSize(getContext(),16);
//        titleFontStyle.setTextColor(Color.GREEN);
        smartTable.getConfig().setColumnTitleStyle(titleFontStyle);//设置顶部列标题文字样式

//        LineStyle gridLineStyle = new LineStyle();
//        gridLineStyle.setColor(ContextCompat.getColor(getActivity(), R.color.eef0f4));
//        smartTable.getConfig().setColumnTitleGridStyle(gridLineStyle);//设置列标题网格样式

        smartTable.getConfig().setColumnTitleBackground(new BaseBackgroundFormat(ContextCompat.getColor(getActivity(), R.color.eef0f4)));//设置顶部列标题背景填充颜色

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
                    return ContextCompat.getColor(getActivity(), R.color.white);//设置行背景颜色
                } else {
                    return ContextCompat.getColor(getActivity(), R.color.eef0f4);
                }
            }
        };

        smartTable.getConfig().setContentCellBackgroundFormat(backgroundFormat);

        smartTable.getConfig().setTableGridFormat(new BaseGridFormat() {
            @Override
            protected boolean isShowColumnTitleVerticalLine(int col, Column column) {
                if (col == 0){
                    column.setOnColumnItemClickListener(new OnColumnItemClickListener() {
                        @Override
                        public void onClick(Column column, String value, Object o, int position) {

                            if (gamePlayerDatas != null && gamePlayerDatas.size() >= position + 1){

                                GamePlayerData xxGamePlayerData = gamePlayerDatas.get(position);

                                if (StringUtils.isNotEmpty(xxGamePlayerData.getGameId()) && StringUtils.isNotEmpty(xxGamePlayerData.getPlayerId()) && StringUtils.isNotEmpty(xxGamePlayerData.getTeamId())){

                                    GameDetailActivity gameDetailActivity = (GameDetailActivity) requireActivity();
                                    MatchSummary matchSummary = gameDetailActivity.getMatchSummary();
                                    PlayerMatchSummaryActivity.startActivity(matchSummary,isHomeTeam,gameDetailActivity,xxGamePlayerData.getGameId(),xxGamePlayerData.getTeamId(),xxGamePlayerData.getPlayerId());
                                }
                            }

                        }
                });
                }
                return false;
            }

            @Override
            protected boolean isShowVerticalLine(int col, int row, CellInfo cellInfo) {

                cellInfo.column.setDrawFormat(new FastTextDrawFormat(){//设置不同列的字体
                    @Override
                    protected void drawText(Canvas c, String value, Rect rect, Paint paint) {
                        if (col == 0) {
                            if (isLiving){

                                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
//                                paint.setColor(Color.RED);//根據需求去掉顏色

                            }else {

                                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                            }
                        }else {
                            paint.setTypeface(Typeface.DEFAULT);
                        }
                        if (row == gamePlayerDatas.size() - 1){
                            if (StringUtils.isEmpty(cellInfo.value) || "0".equals(cellInfo.value)){
                                super.drawText(c, "-", rect, paint);
                                return;
                            }
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
        initTable(mStStatisticSmartTable);
    }

    public void setData(List<GamePlayerData> gamePlayerDatas, boolean isLiving) {

        if (gamePlayerDatas == null){
            return;
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//对时间进行排序

            List<GamePlayerData> firstData = new ArrayList<>();
            List<GamePlayerData> asData = new ArrayList<>();
            for (int i = 0; i < gamePlayerDatas.size(); i++) {
                if (i < 5){
                    firstData.add(gamePlayerDatas.get(i));
                }else{
                    asData.add(gamePlayerDatas.get(i));
                }
            }
            firstData.sort(new Comparator<GamePlayerData>() {
                @Override
                public int compare(GamePlayerData o1, GamePlayerData o2) {
                    return Integer.parseInt(o2.getMinseconds()) - Integer.parseInt(o1.getMinseconds());
                }
            });
            asData.sort(new Comparator<GamePlayerData>() {
                @Override
                public int compare(GamePlayerData o1, GamePlayerData o2) {
                    return Integer.parseInt(o2.getMinseconds()) - Integer.parseInt(o1.getMinseconds());
                }
            });

            gamePlayerDatas.clear();

            gamePlayerDatas.addAll(firstData);
            gamePlayerDatas.addAll(asData);
        }

        this.isLiving = isLiving;

        float all_3ptatt = 0;
        float all_3pmade = 0;

        float all_fgatt = 0;//投篮总数
        float all_fgmade = 0;//投篮投中总数

        float all_ftatt = 0;
        float all_ftmade = 0;


        for (GamePlayerData mGamePlayerData: gamePlayerDatas) {

            all_3ptatt = all_3ptatt + mGamePlayerData.getFg3ptatt();
            all_3pmade = all_3pmade + mGamePlayerData.getFg3ptmade();

            all_fgatt += mGamePlayerData.getFgatt();
            all_fgmade += mGamePlayerData.getFgmade();

            all_ftatt += mGamePlayerData.getFtatt();
            all_ftmade += mGamePlayerData.getFtmade();

        }

        GamePlayerData mmGamePlayerData = new GamePlayerData();
        mmGamePlayerData.setName("命中率");

        if (all_3ptatt==0 || all_3pmade == 0){
            mmGamePlayerData.setFg3pt("0");
        }else {
            float m =  (Math.round(all_3pmade/all_3ptatt * 10000) / 10000f) * 100;
            mmGamePlayerData.setFg3pt((int)m + "%");
        }

        if (all_fgatt==0 || all_fgmade == 0){
            mmGamePlayerData.setShoot("0");
        }else {
            float m =  (Math.round(all_fgmade/all_fgatt * 10000) / 10000f) * 100;
            mmGamePlayerData.setShoot((int)m + "%");
        }

        if (all_ftatt==0 || all_ftmade == 0){
            mmGamePlayerData.setFt("0");
        }else {
            float m =  (Math.round(all_ftmade / all_ftatt * 10000) / 10000f) * 100;
            mmGamePlayerData.setFt((int)m + "%");
        }

        gamePlayerDatas.add(mmGamePlayerData);

        this.gamePlayerDatas = gamePlayerDatas;
        mStStatisticSmartTable.setData(gamePlayerDatas);

        if (mStStatisticSmartTable.getTableData() != null)
            mStStatisticSmartTable.getTableData().setShowCount(true);

//        List<GamePlayerData> shootRate = new ArrayList<>();
//        shootRate.add(mmGamePlayerData);
//
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                mStStatisticSmartTable.addData(shootRate, true);
//            }
//        }, 2000);
//        getActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                mStStatisticSmartTable.addData(shootRate, true);
//            }
//        });


        Message msg = Message.obtain();
        msg.what = MSG_UPDATE_COUNT;
        msg.obj = gamePlayerDatas;

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

        if (mStStatisticSmartTable.getTableData() != null) {


            final String fgStr = fgMakeSum + " - " + fgAttSum;

            if (mStStatisticSmartTable.getTableData().getColumnByID(7) != null) {


                mStStatisticSmartTable.getTableData().getColumnByID(7).setCountFormat(new ICountFormat() {
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
            if (mStStatisticSmartTable.getTableData().getColumnByID(8) != null) {


                mStStatisticSmartTable.getTableData().getColumnByID(8).setCountFormat(new ICountFormat() {
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

            if (mStStatisticSmartTable.getTableData().getColumnByID(9) != null) {


                mStStatisticSmartTable.getTableData().getColumnByID(9).setCountFormat(new ICountFormat() {
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
