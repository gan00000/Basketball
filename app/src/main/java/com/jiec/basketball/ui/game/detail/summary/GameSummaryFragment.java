package com.jiec.basketball.ui.game.detail.summary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseUIFragment;
import com.jiec.basketball.entity.GamePlayerData;
import com.jiec.basketball.entity.MatchSummary;
import com.jiec.basketball.utils.NumberUtils;

import java.math.BigDecimal;
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
        return view;
    }

    public void setSummary(MatchSummary matchSummary) {
        if (matchSummary == null) return;

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
    }

    public void refreshData(List<GamePlayerData> homeData, List<GamePlayerData> awayDat) {
        updateHomeDataView(homeData);
        updateAwayDataView(awayDat);
    }

    private void updateHomeDataView(List<GamePlayerData> homeData) {
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

        mTvHomeRateShoot.setText(NumberUtils.formatAmount(String.valueOf(
                (float) (homemake * 100) / homeatt), BigDecimal.ROUND_HALF_UP, 2) + "%");
        mTvHomeRate3shoot.setText(NumberUtils.formatAmount(String.valueOf(
                (float) (home3make * 100) / home3att), BigDecimal.ROUND_HALF_UP, 2) + "%");
        mTvHomeRateFshoot.setText(NumberUtils.formatAmount(String.valueOf(
                (float) (homeftmake * 100) / homeftatt), BigDecimal.ROUND_HALF_UP, 2) + "%");

        mTvHomeMaxPts.setText(maxPts.getPts() + "");
        mTvHomeMaxPtsName.setText(maxPts.getFirstname() + maxPts.getLastname() + "\n位置：" + maxPts.getPosition());

        mTvHomeMaxAss.setText(maxAss.getAst() + "");
        mTvHomeMaxAssName.setText(maxAss.getFirstname() + maxAss.getLastname() + "\n位置：" + maxAss.getPosition());

        mTvHomeMaxReb.setText(maxReb.getReb() + "");
        mTvHomeMaxRebName.setText(maxReb.getFirstname() + maxReb.getLastname() + "\n位置：" + maxReb.getPosition());
    }

    private void updateAwayDataView(List<GamePlayerData> homeData) {
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

        mTvAwayRateShoot.setText(NumberUtils.formatAmount(String.valueOf(
                (float) (homemake * 100) / homeatt), BigDecimal.ROUND_HALF_UP, 2) + "%");
        mTvAwayRate3shoot.setText(NumberUtils.formatAmount(String.valueOf(
                (float) (home3make * 100) / home3att), BigDecimal.ROUND_HALF_UP, 2) + "%");
        mTvAwayRateFshoot.setText(NumberUtils.formatAmount(String.valueOf(
                (float) (homeftmake * 100) / homeftatt), BigDecimal.ROUND_HALF_UP, 2) + "%");

        mTvAwayMaxPts.setText(maxPts.getPts() + "");
        mTvAwayMaxPtsName.setText(maxPts.getFirstname() + maxPts.getLastname() + "\n位置：" + maxPts.getPosition());

        mTvAwayMaxAss.setText(maxAss.getAst() + "");
        mTvAwayMaxAssName.setText(maxAss.getFirstname() + maxAss.getLastname() + "\n位置：" + maxAss.getPosition());

        mTvAwayMaxReb.setText(maxReb.getReb() + "");
        mTvAwayMaxRebName.setText(maxReb.getFirstname() + maxReb.getLastname() + "\n位置：" + maxReb.getPosition());
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
