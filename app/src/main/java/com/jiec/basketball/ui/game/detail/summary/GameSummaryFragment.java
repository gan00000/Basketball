package com.jiec.basketball.ui.game.detail.summary;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.gan.ctools.tool.BarChartUtil2;
import com.gan.ctools.tool.BarChartUtils;
import com.gan.ctools.tool.CombinedChartUtil;
import com.gan.widget.CompareIndicatorView2;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseUIFragment;
import com.jiec.basketball.entity.GameLiveInfo;
import com.jiec.basketball.entity.GamePlayerData;
import com.jiec.basketball.entity.MatchSummary;
import com.jiec.basketball.utils.NumberUtils;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wangchuangjie on 2018/5/24.
 */

public class GameSummaryFragment extends BaseUIFragment {
    @BindView(R.id.tv_home)
    TextView mTvHome;
    @BindView(R.id.tv_home_q1)
    TextView mTvHomeQ1;
    @BindView(R.id.tv_home_q2)
    TextView mTvHomeQ2;
    @BindView(R.id.tv_home_q3)
    TextView mTvHomeQ3;
    @BindView(R.id.tv_home_q4)
    TextView mTvHomeQ4;
    @BindView(R.id.tv_away)
    TextView mTvAway;
    @BindView(R.id.tv_away_q1)
    TextView mTvAwayQ1;
    @BindView(R.id.tv_away_q2)
    TextView mTvAwayQ2;
    @BindView(R.id.tv_away_q3)
    TextView mTvAwayQ3;
    @BindView(R.id.tv_away_q4)
    TextView mTvAwayQ4;

    Unbinder unbinder;
    @BindView(R.id.tv_home_sum)
    TextView mTvHomeSum;
    @BindView(R.id.tv_away_sum)
    TextView mTvAwaySum;


    @BindView(R.id.tv_home_pts)
    TextView mTvHomePts;
    @BindView(R.id.tv_away_pts)
    TextView mTvAwayPts;
    @BindView(R.id.tv_home_reb)
    TextView mTvHomeReb;
    @BindView(R.id.tv_away_reb)
    TextView mTvAwayReb;
    @BindView(R.id.tv_home_ass)
    TextView mTvHomeAss;
    @BindView(R.id.tv_away_ass)
    TextView mTvAwayAss;
    @BindView(R.id.tv_home_blk)
    TextView mTvHomeBlk;
    @BindView(R.id.tv_away_blk)
    TextView mTvAwayBlk;
    @BindView(R.id.tv_home_stl)
    TextView mTvHomeStl;
    @BindView(R.id.tv_away_stl)
    TextView mTvAwayStl;
    @BindView(R.id.tv_home_rate_shoot)
    TextView mTvHomeRateShoot;
    @BindView(R.id.tv_away_rate_shoot)
    TextView mTvAwayRateShoot;
    @BindView(R.id.tv_home_rate_3shoot)
    TextView mTvHomeRate3shoot;
    @BindView(R.id.tv_away_rate_3shoot)
    TextView mTvAwayRate3shoot;
    @BindView(R.id.tv_home_rate_fshoot)
    TextView mTvHomeRateFshoot;
    @BindView(R.id.tv_away_rate_fshoot)
    TextView mTvAwayRateFshoot;
    @BindView(R.id.tv_home_max_pts)
    TextView mTvHomeMaxPts;
    @BindView(R.id.tv_away_max_pts)
    TextView mTvAwayMaxPts;
    @BindView(R.id.tv_home_max_pts_name)
    TextView mTvHomeMaxPtsName;
    @BindView(R.id.tv_away_max_pts_name)
    TextView mTvAwayMaxPtsName;
    @BindView(R.id.tv_home_max_ass)
    TextView mTvHomeMaxAss;
    @BindView(R.id.tv_away_max_ass)
    TextView mTvAwayMaxAss;
    @BindView(R.id.tv_home_max_ass_name)
    TextView mTvHomeMaxAssName;
    @BindView(R.id.tv_away_max_ass_name)
    TextView mTvAwayMaxAssName;
    @BindView(R.id.tv_home_max_reb)
    TextView mTvHomeMaxReb;
    @BindView(R.id.tv_away_max_reb)
    TextView mTvAwayMaxReb;
    @BindView(R.id.tv_home_max_reb_name)
    TextView mTvHomeMaxRebName;
    @BindView(R.id.tv_away_max_reb_name)
    TextView mTvAwayMaxRebName;
    @BindView(R.id.tv_ot1)
    TextView mTvOt1;
    @BindView(R.id.tv_ot2)
    TextView mTvOt2;
    @BindView(R.id.tv_ot3)
    TextView mTvOt3;
    @BindView(R.id.tv_home_ot1)
    TextView mTvHomeOt1;
    @BindView(R.id.tv_home_ot2)
    TextView mTvHomeOt2;
    @BindView(R.id.tv_home_ot3)
    TextView mTvHomeOt3;
    @BindView(R.id.tv_away_ot1)
    TextView mTvAwayOt1;
    @BindView(R.id.tv_away_ot2)
    TextView mTvAwayOt2;
    @BindView(R.id.tv_away_ot3)
    TextView mTvAwayOt3;

    @BindView(R.id.game_chart_jiashi_tv)
    TextView mJiaShiTv;

    @BindView(R.id.compareIndicator2_score)
    CompareIndicatorView2 compareIndicator2Score;
    @BindView(R.id.compareIndicator_leftValue_score)
    TextView tvLeftScore;
    @BindView(R.id.compareIndicator_rightValue_score)
    TextView tvRightScore;

    @BindView(R.id.compareIndicator2_lanban)
    CompareIndicatorView2 compareIndicator2Lanban;
    @BindView(R.id.compareIndicator_leftValue_lanban)
    TextView tvLeftLanban;
    @BindView(R.id.compareIndicator_rightValue_lanban)
    TextView tvRightLanban;


    @BindView(R.id.compareIndicator2_zhugong)
    CompareIndicatorView2 compareIndicator2Zhugong;
    @BindView(R.id.compareIndicator_leftValue_zhugong)
    TextView tvLeftZhugong;
    @BindView(R.id.compareIndicator_rightValue_zhugong)
    TextView tvRightZhugong;


    @BindView(R.id.compareIndicator2_qiangduan)
    CompareIndicatorView2 compareIndicator2Qiangduan;
    @BindView(R.id.compareIndicator_leftValue_qiangduan)
    TextView tvLeftQiangduan;
    @BindView(R.id.compareIndicator_rightValue_qiangduan)
    TextView tvRightQiangduan;

    @BindView(R.id.compareIndicator2_fenggai)
    CompareIndicatorView2 compareIndicator2Fenggai;
    @BindView(R.id.compareIndicator_leftValue_fenggai)
    TextView tvLeftFenggai;
    @BindView(R.id.compareIndicator_rightValue_fenggai)
    TextView tvRightFenggai;


    @BindView(R.id.compareIndicator2_toulan)
    CompareIndicatorView2 compareIndicator2_Toulan;
    @BindView(R.id.compareIndicator_leftValue_toulan)
    TextView tvLeftToulan;
    @BindView(R.id.compareIndicator_rightValue_toulan)
    TextView tvRightToulan;

    @BindView(R.id.compareIndicator2_sanfen)
    CompareIndicatorView2 compareIndicator2_Sanfen;
    @BindView(R.id.compareIndicator_leftValue_sanfen)
    TextView tvLeftSanfen;
    @BindView(R.id.compareIndicator_rightValue_sanfen)
    TextView tvRightSanfen;


    @BindView(R.id.compareIndicator2_faqiu)
    CompareIndicatorView2 compareIndicator2_Faqiu;
    @BindView(R.id.compareIndicator_leftValue_faqiu)
    TextView tvLeftFaqiu;
    @BindView(R.id.compareIndicator_rightValue_faqiu)
    TextView tvRightFaqiu;

    @BindView(R.id.compare_player_lefticon)
    ImageView compare_player_lefticon;
    @BindView(R.id.compare_player_righticon)
    ImageView compare_player_righticon;
    @BindView(R.id.compare_player_leftname)
    TextView compare_player_leftname;
    @BindView(R.id.compare_player_rightname)
    TextView compare_player_rightname;

    @BindView(R.id.compare_player_reb_lefticon)
    ImageView compare_player_reb_lefticon;
    @BindView(R.id.compare_player_reb_righticon)
    ImageView compare_player_reb_righticon;
    @BindView(R.id.compare_player_reb_leftname)
    TextView compare_player_reb_leftname;
    @BindView(R.id.compare_player_reb_rightname)
    TextView compare_player_reb_rightname;

    @BindView(R.id.compare_player_ass_lefticon)
    ImageView compare_player_ass_lefticon;
    @BindView(R.id.compare_player_ass_righticon)
    ImageView compare_player_ass_righticon;
    @BindView(R.id.compare_player_ass_leftname)
    TextView compare_player_ass_leftname;
    @BindView(R.id.compare_player_ass_rightname)
    TextView compare_player_ass_rightname;


    @BindView(R.id.barChart_compare_team_score)
    BarChart barChart_compare_team_score;

    @BindView(R.id.smmaryBarChartComparePlayer)
    BarChart smmaryBarChartComparePlayer;

    @BindView(R.id.smmaryBarChartComparePlayerReb)
    BarChart smmaryBarChartComparePlayerReb;

    @BindView(R.id.smmaryBarChartComparePlayerAss)
    BarChart smmaryBarChartComparePlayerAss;

    BarChartUtil2 mBarChartUtil2;
    BarChartUtils mBarChartUtils_smmaryBarChartComparePlayer;//得分
    BarChartUtils mBarChartUtils_smmaryBarChartComparePlayer_reb;//籃板
    BarChartUtils mBarChartUtils_smmaryBarChartComparePlayer_ass;//助攻

    @BindView(R.id.score_grap_lineChart)
    CombinedChart scoreGrapCombinedChart;

    //LineChartUtil scoreLineChartUtil;
    CombinedChartUtil combinedChartUtil;

    public static GameSummaryFragment newInstance() {

        Bundle args = new Bundle();

        GameSummaryFragment fragment = new GameSummaryFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_summary, container, false);

        unbinder = ButterKnife.bind(this, view);

        mBarChartUtil2 = new BarChartUtil2(requireContext(), barChart_compare_team_score);
        mBarChartUtils_smmaryBarChartComparePlayer = new BarChartUtils(requireContext(),smmaryBarChartComparePlayer);
        mBarChartUtils_smmaryBarChartComparePlayer_reb = new BarChartUtils(requireContext(),smmaryBarChartComparePlayerReb);
        mBarChartUtils_smmaryBarChartComparePlayer_ass = new BarChartUtils(requireContext(),smmaryBarChartComparePlayerAss);

//        compareIndicator2Score.setBigCountColor(getResources().getColor(R.color.af3138));
//        compareIndicator2Score.setLessCountColor(getResources().getColor(R.color.c_939aa0));
//
//        compareIndicator2Lanban.setBigCountColor(getResources().getColor(R.color.af3138));
//        compareIndicator2Lanban.setLessCountColor(getResources().getColor(R.color.c_939aa0));
//
//        compareIndicator2Fenggai.setBigCountColor(getResources().getColor(R.color.af3138));
//        compareIndicator2Fenggai.setLessCountColor(getResources().getColor(R.color.c_939aa0));
//
//        compareIndicator2Qiangduan.setBigCountColor(getResources().getColor(R.color.af3138));
//        compareIndicator2Qiangduan.setLessCountColor(getResources().getColor(R.color.c_939aa0));
//
//        compareIndicator2Zhugong.setBigCountColor(getResources().getColor(R.color.af3138));
//        compareIndicator2Zhugong.setLessCountColor(getResources().getColor(R.color.c_939aa0));

//        scoreLineChartUtil = new LineChartUtil(requireContext(),scoreGrapLineChart);

        combinedChartUtil = new CombinedChartUtil(requireContext(),scoreGrapCombinedChart);
        return view;
    }

    ArrayList<Entry> awayPtsValues = new ArrayList<>();
    ArrayList<Entry> homePtsValues = new ArrayList<>();

    public void showLineChatData(MatchSummary matchSummary,GameLiveInfo gameLiveInfo, boolean isGmaeInProgress){

        Log.d("GameDetailActivity","get_live_post_from_game showLineChatData");
        if (gameLiveInfo == null){
            return;
        }
        ArrayList<GameLiveInfo.LiveFeedBean> xxAllBean = new ArrayList<>();

        List<List<GameLiveInfo.LiveFeedBean>> liveData = new ArrayList<>();
        List<List<GameLiveInfo.LiveFeedBean>> tempLiveFeedBeans = gameLiveInfo.getLive_feed();
        if (tempLiveFeedBeans == null){
            Log.d("GameDetailActivity","get_live_post_from_game gameLiveInfo.getLive_feed() null");
            return;
        }
        liveData.addAll(tempLiveFeedBeans);

        int toCount = 0;//加時次數
        if (liveData != null && !liveData.isEmpty()) {
            if (liveData.size() > 4) {
                toCount = liveData.size() - 4;
            }
            awayPtsValues.clear();
            homePtsValues.clear();
            xxAllBean.clear();

            if (isGmaeInProgress) {
                Collections.reverse(liveData);
            }
            int tempTime = 0;
            for (int i = 0; i < liveData.size(); i++) {

                List<GameLiveInfo.LiveFeedBean> liveFeedBeans = liveData.get(i);
                if (isGmaeInProgress) {
                    Collections.reverse(liveFeedBeans);
                }
                xxAllBean.addAll(liveFeedBeans);
                for (GameLiveInfo.LiveFeedBean scoreBean: liveFeedBeans) {
                    int minutes =Integer.parseInt(scoreBean.getMinutes());
                    int seconds =Integer.parseInt(scoreBean.getSeconds());

                    if (i > 3){//加時

                        //int playMinutes = 12 - minutes;
                        int playSeconds = 5 * 60 - (seconds + minutes * 60) + (4 * 12 * 60); //x 数据使用秒
                        if (playSeconds >= tempTime){
                            awayPtsValues.add(new Entry(playSeconds, Integer.parseInt(scoreBean.getAwayPts())));
                            homePtsValues.add(new Entry(playSeconds, Integer.parseInt(scoreBean.getHomePts())));
                            tempTime = playSeconds;
                        }

                    }else{

                        //int playMinutes = 12 - minutes;
                        int playSeconds = 12 * 60 - (seconds + minutes * 60) + (i * 12 * 60); //x 数据使用秒

                        if (playSeconds >= tempTime) { //此处为防止时间数据出现问题，时间突然变小
                            awayPtsValues.add(new Entry(playSeconds, Integer.parseInt(scoreBean.getAwayPts())));
                            homePtsValues.add(new Entry(playSeconds, Integer.parseInt(scoreBean.getHomePts())));

                            tempTime = playSeconds;
                        }
                    }

                }
            }

            if (xxAllBean.isEmpty()){
                return;
            }

            int awayPts_Max = Integer.parseInt(xxAllBean.get(xxAllBean.size() - 1).getAwayPts());
            int homePts_Max = Integer.parseInt(xxAllBean.get(xxAllBean.size() - 1).getHomePts());
            int maxValue = awayPts_Max;
            if (awayPts_Max > homePts_Max){
                maxValue = awayPts_Max;
            }else{
                maxValue = homePts_Max;
            }
            int xx = maxValue / 30;
            maxValue = (xx + 1) * 30;
            if (toCount > 0){
                mJiaShiTv.setVisibility(View.VISIBLE);
            }else{
                mJiaShiTv.setVisibility(View.GONE);
            }
            combinedChartUtil.showData(awayPtsValues,homePtsValues,matchSummary.getAwayName(), matchSummary.getHomeName(),maxValue,toCount);
        }

        /*awayPtsValues.clear();
        homePtsValues.clear();
        xxTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        awayPtsValues.add(new Entry(xxx, xxx + 3));
                        homePtsValues.add(new Entry(xxx, xxx + 8));
                        combinedChartUtil.showData(awayPtsValues,homePtsValues,matchSummary.getAwayName(), matchSummary.getHomeName(),120,0);
                        xxx = xxx + 1;
                    }
                });

            }
        }, 1000, 2000);*/

    }
//    int xxx = 0;
//    Timer xxTimer = new Timer();


    public void showBarChatData(ArrayList<Integer> minScoreGap){

        if (minScoreGap == null || minScoreGap.isEmpty()){
            return;
        }
        List<BarEntry> yValuesEntries = new ArrayList<>();
        ArrayList<String> xValuesTest = new ArrayList<>();

        int maxGrap = 0;
        int minGrap = 0;
        List<Integer> colors = new ArrayList<>();
        int endColor4 = ContextCompat.getColor(requireContext(), R.color.c_608FD4_ke);
        int endColor5 = ContextCompat.getColor(requireContext(), R.color.c_F35930_zhu);

        for (int i = 0; i < minScoreGap.size(); i++) {
            int mgrap = minScoreGap.get(i);
            if (mgrap > maxGrap) maxGrap = mgrap;
            if (mgrap < minGrap) minGrap = mgrap;
            BarEntry barEntry = new BarEntry(i + 1, minScoreGap.get(i));
            yValuesEntries.add(barEntry);
            xValuesTest.add(i + 1 + "");

            // specific colors
            if (minScoreGap.get(i) >= 0)
                colors.add(endColor4);
            else
                colors.add(endColor5);
        }

//        maxGrap = maxGrap + 1;
//        minGrap = minGrap - 1;

        String lable = "ScoreGrap";


        mBarChartUtil2.showData(yValuesEntries, lable, maxGrap, minGrap, colors);
    }


    public void setSummary(MatchSummary matchSummary) {//数据来自livefragment
        if (matchSummary == null) return;
        Log.d("GameDetailActivity","get_live_post_from_game setSummary");
        mTvHome.setText(matchSummary.getHomeName());

        if (!TextUtils.isEmpty(matchSummary.getHome_quarter_scores())) {
            String[] homeQs = matchSummary.getHome_quarter_scores().split(",");
            if (homeQs != null) {
                if (homeQs.length > 0)
                    mTvHomeQ1.setText(homeQs[0]);
                if (homeQs.length > 1)
                    mTvHomeQ2.setText(homeQs[1]);
                if (homeQs.length > 2)
                    mTvHomeQ3.setText(homeQs[2]);
                if (homeQs.length > 3)
                    mTvHomeQ4.setText(homeQs[3]);
                if (homeQs.length > 4) {
                    mTvOt1.setVisibility(View.VISIBLE);
                    mTvHomeOt1.setVisibility(View.VISIBLE);
                    mTvHomeOt1.setText(homeQs[4]);
                }

                if (homeQs.length > 5) {
                    mTvOt2.setVisibility(View.VISIBLE);
                    mTvHomeOt2.setVisibility(View.VISIBLE);
                    mTvHomeOt2.setText(homeQs[5]);
                }

                if (homeQs.length > 6) {
                    mTvOt3.setVisibility(View.VISIBLE);
                    mTvHomeOt3.setVisibility(View.VISIBLE);
                    mTvHomeOt3.setText(homeQs[6]);
                }
            }
        }

        mTvHomeSum.setText(matchSummary.getHome_pts());
        mTvHomePts.setText(matchSummary.getHome_pts());


        mTvAway.setText(matchSummary.getAwayName());

        if (!TextUtils.isEmpty(matchSummary.getAway_quarter_scores())) {
            String[] awayQs = matchSummary.getAway_quarter_scores().split(",");
            if (awayQs != null) {
                if (awayQs.length > 0)
                    mTvAwayQ1.setText(awayQs[0]);
                if (awayQs.length > 1)
                    mTvAwayQ2.setText(awayQs[1]);
                if (awayQs.length > 2)
                    mTvAwayQ3.setText(awayQs[2]);
                if (awayQs.length > 3)
                    mTvAwayQ4.setText(awayQs[3]);

                if (awayQs.length > 4) {
                    mTvAwayOt1.setVisibility(View.VISIBLE);
                    mTvAwayOt1.setText(awayQs[4]);
                }

                if (awayQs.length > 5) {
                    mTvAwayOt2.setVisibility(View.VISIBLE);
                    mTvAwayOt2.setText(awayQs[5]);
                }

                if (awayQs.length > 6) {
                    mTvAwayOt3.setVisibility(View.VISIBLE);
                    mTvAwayOt3.setText(awayQs[6]);
                }
            }
        }

        mTvAwaySum.setText(matchSummary.getAway_pts());
        mTvAwayPts.setText(matchSummary.getAway_pts());

//        tvLeftScore.setText(matchSummary.getHome_pts());
//        tvRightScore.setText(matchSummary.getAway_pts());

        tvRightScore.setText(matchSummary.getHome_pts());//主客數據對換
        tvLeftScore.setText(matchSummary.getAway_pts());
        int homePts = stringToInt(matchSummary.getHome_pts());
        int awayPts = stringToInt(matchSummary.getAway_pts());
        compareIndicator2Score.updateView(awayPts, homePts);

    }

    private int stringToInt(String stringValue){

        try {
            if (StringUtils.isEmpty(stringValue)){
                return 0;
            }
            int m = Integer.valueOf(stringValue);
            return m;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void refreshData(List<GamePlayerData> homeData, List<GamePlayerData> awayDat) { //数据来自数据统计fragment
        try {
            updateHomeDataView(homeData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            updateAwayDataView(awayDat);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tvLeftLanban.setText(mTvHomeReb.getText());
        tvRightLanban.setText(mTvAwayReb.getText());
        compareIndicator2Lanban.updateView(stringToInt(mTvHomeReb.getText().toString()), stringToInt(mTvAwayReb.getText().toString()));

        tvLeftZhugong.setText(mTvHomeAss.getText());
        tvRightZhugong.setText(mTvAwayAss.getText());
        compareIndicator2Zhugong.updateView(stringToInt(mTvHomeAss.getText().toString()), stringToInt(mTvAwayAss.getText().toString()));


        tvLeftQiangduan.setText(mTvHomeBlk.getText());
        tvRightQiangduan.setText(mTvAwayBlk.getText());
        compareIndicator2Qiangduan.updateView(stringToInt(mTvHomeBlk.getText().toString()), stringToInt(mTvAwayBlk.getText().toString()));

        tvLeftFenggai.setText(mTvHomeStl.getText());
        tvRightFenggai.setText(mTvAwayStl.getText());
        compareIndicator2Fenggai.updateView(stringToInt(mTvHomeStl.getText().toString()), stringToInt(mTvAwayStl.getText().toString()));


        //球員主柱狀圖對比
        if (homePlayer_maxPts != null) {
            Glide.with(this).load(homePlayer_maxPts.getOfficialImagesrc()).into(compare_player_lefticon);
            compare_player_leftname.setText(homePlayer_maxPts.getFirstname().substring(0,1).toUpperCase() + "." + homePlayer_maxPts.getLastname());
        }
        if (homePlayer_maxReb != null) {
            Glide.with(this).load(homePlayer_maxReb.getOfficialImagesrc()).into(compare_player_reb_lefticon);
            compare_player_reb_leftname.setText(homePlayer_maxReb.getFirstname().substring(0,1).toUpperCase() + "." + homePlayer_maxReb.getLastname());
        }
        if (homePlayer_maxAss != null) {
            Glide.with(this).load(homePlayer_maxAss.getOfficialImagesrc()).into(compare_player_ass_lefticon);
            compare_player_ass_leftname.setText(homePlayer_maxAss.getFirstname().substring(0,1).toUpperCase() + "." + homePlayer_maxAss.getLastname());
        }

        if (awayPlayer_maxPts != null) {
            Glide.with(this).load(awayPlayer_maxPts.getOfficialImagesrc()).into(compare_player_righticon);
            compare_player_rightname.setText(awayPlayer_maxPts.getFirstname().substring(0,1).toUpperCase() + "." + awayPlayer_maxPts.getLastname());
        }
        if (awayPlayer_maxReb != null) {
            Glide.with(this).load(awayPlayer_maxReb.getOfficialImagesrc()).into(compare_player_reb_righticon);
            compare_player_reb_rightname.setText(awayPlayer_maxReb.getFirstname().substring(0,1).toUpperCase() + "." + awayPlayer_maxReb.getLastname());
        }
        if (awayPlayer_maxAss != null) {
            Glide.with(this).load(awayPlayer_maxAss.getOfficialImagesrc()).into(compare_player_ass_righticon);
            compare_player_ass_rightname.setText(awayPlayer_maxAss.getFirstname().substring(0,1).toUpperCase() + "." + awayPlayer_maxAss.getLastname());
        }

        List<BarEntry> yValues = new ArrayList<>();
        BarEntry homePtsBarEntry = new BarEntry(1, homePlayer_maxPts != null ? homePlayer_maxPts.getPts() : 0);
        BarEntry awayPtsBarEntry = new BarEntry(3, awayPlayer_maxPts != null ? awayPlayer_maxPts.getPts() : 0);
        yValues.add(homePtsBarEntry);
        yValues.add(awayPtsBarEntry);
        mBarChartUtils_smmaryBarChartComparePlayer.showData(yValues,"pts");

        List<BarEntry> yRebValues = new ArrayList<>();//籃板
        BarEntry homeRebBarEntry = new BarEntry(1, homePlayer_maxReb != null ? homePlayer_maxReb.getReb() : 0);
        BarEntry awayRebBarEntry = new BarEntry(3, awayPlayer_maxReb != null ? awayPlayer_maxReb.getReb() : 0);
        yRebValues.add(homeRebBarEntry);
        yRebValues.add(awayRebBarEntry);
        mBarChartUtils_smmaryBarChartComparePlayer_reb.showData(yRebValues,"reb");

        List<BarEntry> yAssValues = new ArrayList<>();//助攻
        BarEntry homeAssBarEntry = new BarEntry(1, homePlayer_maxAss != null ? homePlayer_maxAss.getAst() : 0);
        BarEntry awayAssBarEntry = new BarEntry(3, awayPlayer_maxAss != null ? awayPlayer_maxAss.getAst() : 0);
        yAssValues.add(homeAssBarEntry);
        yAssValues.add(awayAssBarEntry);
        mBarChartUtils_smmaryBarChartComparePlayer_ass.showData(yAssValues,"ass");

        //投籃數據
        tvLeftToulan.setText(leftToulan + "%");
        tvRightToulan.setText(rightToulan + "%");
        compareIndicator2_Toulan.updateView(leftToulan, rightToulan);

        tvLeftSanfen.setText(leftSanfen + "%");
        tvRightSanfen.setText(rightSanfen + "%");
        compareIndicator2_Sanfen.updateView(leftSanfen, rightSanfen);

        tvLeftFaqiu.setText(leftFaqiu + "%");
        tvRightFaqiu.setText(rightFaqiu + "%");
        compareIndicator2_Faqiu.updateView(leftFaqiu, rightFaqiu);
    }

    GamePlayerData homePlayer_maxPts, homePlayer_maxAss, homePlayer_maxReb;
    GamePlayerData awayPlayer_maxPts, awayPlayer_maxAss, awayPlayer_maxReb;

    int leftToulan,rightToulan, leftSanfen,rightSanfen,leftFaqiu,rightFaqiu;

    private void updateHomeDataView(List<GamePlayerData> homeData) {
        if (homeData == null || homeData.isEmpty()){
            return;
        }
        int homeAss = 0, homeReb = 0, homeStl = 0, homeblk = 0, homemake = 0, homeatt = 0;
        int home3make = 0, home3att = 0, homeftmake = 0, homeftatt = 0;
        GamePlayerData maxPts = homeData.get(0), maxAss = homeData.get(0), maxReb = homeData.get(0);
        for (GamePlayerData data : homeData) {
            homeAss += data.getAst();
            homeReb += data.getReb();
            homeStl += data.getStl();
            homeblk += data.getBlk();

            homemake += data.getFgmade();
            homeatt += data.getFgatt();
            home3make += data.getFg3ptmade();
            home3att += data.getFg3ptatt();
            homeftmake += data.getFtmade();
            homeftatt += data.getFtatt();

            if (data.getPts() > maxPts.getPts()) maxPts = data;
            if (data.getAst() > maxAss.getAst()) maxAss = data;
            if (data.getReb() > maxReb.getReb()) maxReb = data;
        }
        mTvHomeAss.setText(homeAss + "");
        mTvHomeReb.setText(homeReb + "");
        mTvHomeStl.setText(homeStl + "");
        mTvHomeBlk.setText(homeblk + "");

        homePlayer_maxPts = maxPts;
        homePlayer_maxAss = maxAss;
        homePlayer_maxReb = maxReb;

        if (homeatt != 0) {
            mTvHomeRateShoot.setText(NumberUtils.formatAmount(String.valueOf(
                    (float) (homemake * 100) / homeatt), BigDecimal.ROUND_HALF_UP, 2) + "%");
        }else{
            mTvHomeRateShoot.setText("0%");
        }
        if (home3att != 0) {
            mTvHomeRate3shoot.setText(NumberUtils.formatAmount(String.valueOf(
                    (float) (home3make * 100) / home3att), BigDecimal.ROUND_HALF_UP, 2) + "%");
        }else{
            mTvHomeRate3shoot.setText("0%");
        }
        if (homeftatt != 0) {
            mTvHomeRateFshoot.setText(NumberUtils.formatAmount(String.valueOf(
                    (float) (homeftmake * 100) / homeftatt), BigDecimal.ROUND_HALF_UP, 2) + "%");
        }else{
            mTvHomeRateFshoot.setText("0%");
        }

        mTvHomeMaxPts.setText(maxPts.getPts() + "");
        mTvHomeMaxPtsName.setText(maxPts.getFirstname() + maxPts.getLastname() + "\n位置：" + maxPts.getPosition());

        mTvHomeMaxAss.setText(maxAss.getAst() + "");
        mTvHomeMaxAssName.setText(maxAss.getFirstname() + maxAss.getLastname() + "\n位置：" + maxAss.getPosition());

        mTvHomeMaxReb.setText(maxReb.getReb() + "");
        mTvHomeMaxRebName.setText(maxReb.getFirstname() + maxReb.getLastname() + "\n位置：" + maxReb.getPosition());

        if (homeatt != 0) {
            leftToulan = (homemake * 100) / homeatt;
        }else{
            leftToulan = 0;
        }
        if (home3att == 0) {
            leftSanfen = 0;
        }else {
            leftSanfen = (home3make * 100) / home3att;
        }
        if (homeftatt != 0) {
            leftFaqiu = (homeftmake * 100) / homeftatt;
        }else{
            leftFaqiu = 0;
        }
    }

    private void updateAwayDataView(List<GamePlayerData> homeData) {
        if (homeData == null || homeData.isEmpty()){
            return;
        }
        int ass = 0, reb = 0, stl = 0, blk = 0, homemake = 0, homeatt = 0;
        int home3make = 0, home3att = 0, homeftmake = 0, homeftatt = 0;
        GamePlayerData maxPts = homeData.get(0), maxAss = homeData.get(0), maxReb = homeData.get(0);
        for (GamePlayerData data : homeData) {
            ass += data.getAst();
            reb += data.getReb();
            stl += data.getStl();
            blk += data.getBlk();

            homemake += data.getFgmade();
            homeatt += data.getFgatt();
            home3make += data.getFg3ptmade();
            home3att += data.getFg3ptatt();
            homeftmake += data.getFtmade();
            homeftatt += data.getFtatt();

            if (data.getPts() > maxPts.getPts()) maxPts = data;
            if (data.getAst() > maxAss.getAst()) maxAss = data;
            if (data.getReb() > maxReb.getReb()) maxReb = data;
        }
        mTvAwayAss.setText(ass + "");
        mTvAwayReb.setText(reb + "");
        mTvAwayStl.setText(stl + "");
        mTvAwayBlk.setText(blk + "");

        awayPlayer_maxPts = maxPts;
        awayPlayer_maxAss = maxAss;
        awayPlayer_maxReb = maxReb;

        if (homeatt != 0) {
            mTvAwayRateShoot.setText(NumberUtils.formatAmount(String.valueOf(
                    (float) (homemake * 100) / homeatt), BigDecimal.ROUND_HALF_UP, 2) + "%");
        }else {
            mTvAwayRateShoot.setText("0%");
        }
        if (home3att != 0) {
            mTvAwayRate3shoot.setText(NumberUtils.formatAmount(String.valueOf(
                    (float) (home3make * 100) / home3att), BigDecimal.ROUND_HALF_UP, 2) + "%");
        }else{
            mTvAwayRate3shoot.setText("0%");
        }
        if (homeftatt != 0) {
            mTvAwayRateFshoot.setText(NumberUtils.formatAmount(String.valueOf(
                    (float) (homeftmake * 100) / homeftatt), BigDecimal.ROUND_HALF_UP, 2) + "%");
        }else{
            mTvAwayRateFshoot.setText("0%");
        }

        mTvAwayMaxPts.setText(maxPts.getPts() + "");
        mTvAwayMaxPtsName.setText(maxPts.getFirstname() + maxPts.getLastname() + "\n位置：" + maxPts.getPosition());

        mTvAwayMaxAss.setText(maxAss.getAst() + "");
        mTvAwayMaxAssName.setText(maxAss.getFirstname() + maxAss.getLastname() + "\n位置：" + maxAss.getPosition());

        mTvAwayMaxReb.setText(maxReb.getReb() + "");
        mTvAwayMaxRebName.setText(maxReb.getFirstname() + maxReb.getLastname() + "\n位置：" + maxReb.getPosition());

        if (homeatt != 0) {
            rightToulan = (homemake * 100) / homeatt;
        }else{
            rightToulan = 0;
        }
        if (home3att != 0) {
            rightSanfen = (home3make * 100) / home3att;
        }else{
            rightSanfen = 0;
        }
        if (homeftatt != 0) {
            rightFaqiu = (homeftmake * 100) / homeftatt;
        }else {
            rightFaqiu = 0;
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }
}
