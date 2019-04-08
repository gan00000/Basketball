package com.jiec.basketball.ui.game.detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseUIActivity;
import com.jiec.basketball.entity.GamePlayerData;
import com.jiec.basketball.entity.MatchSummary;
import com.jiec.basketball.entity.Matches;
import com.jiec.basketball.ui.game.detail.live.GameLiveFragment;
import com.jiec.basketball.ui.game.detail.statistic.GameStatisticMainFragment;
import com.jiec.basketball.ui.game.detail.summary.GameSummaryFragment;
import com.jiec.basketball.utils.ImageLoaderUtils;
import com.wangcj.common.widget.TitleBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GameDetailActivity extends BaseUIActivity implements GameDetailContract.View {


    private static final String[] TITLES = {"文字直播", "對陣", "數據統計"};

    GameDetailContract.Presenter mPresenter;
    @BindView(R.id.iv_team_1)
    ImageView mIvTeam1;
    @BindView(R.id.tv_score)
    TextView mTvScore;
    @BindView(R.id.tv_status)
    TextView mTvStatus;
    @BindView(R.id.iv_team_2)
    ImageView mIvTeam2;
    @BindView(R.id.tab_layout)
    CommonTabLayout mTabLayout;
    @BindView(R.id.fl_layout)
    FrameLayout mFlLayout;
    @BindView(R.id.titleBar)
    TitleBar mTitleBar;
    @BindView(R.id.tv_time)
    TextView mTvTime;

    private ArrayList<Fragment> mFragments;

    GameLiveFragment mGameLiveFragment;
    GameSummaryFragment mGameSummaryFragment;
    GameStatisticMainFragment mGameStatisticMainFragment;

    public static void show(Context context, String game_id) {
        Intent intent = new Intent(context, GameDetailActivity.class);
        intent.putExtra("game_id", game_id);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game_detail);
        ButterKnife.bind(this);

        String game_id = getIntent().getStringExtra("game_id");

        mFragments = new ArrayList<>();

        mGameLiveFragment = GameLiveFragment.newInstance(game_id);
        mGameLiveFragment.setGameLiveUpdateListener(new GameLiveFragment.GameLiveUpdateListener() {
            @Override
            public void onUpdate(MatchSummary matchSummary, Matches matches) {

                mGameSummaryFragment.setSummary(matchSummary);

                ImageLoaderUtils.display(GameDetailActivity.this, mIvTeam1, matchSummary.getHomeLogo());
                ImageLoaderUtils.display(GameDetailActivity.this, mIvTeam2, matchSummary.getAwayLogo());
                mTvScore.setText(matchSummary.getHome_pts() + "-" + matchSummary.getAway_pts());

                if (matchSummary.getScheduleStatus().equals("Final")) {
                    mTvStatus.setText(R.string.game_state_final);
                    mTvTime.setVisibility(View.GONE);
                } else if (matchSummary.getScheduleStatus().equals("InProgress")) {
                    mTvStatus.setText("");
                    mTvTime.setVisibility(View.VISIBLE);
                    if (matchSummary.getQuarter().contains("OT")) {
                        mTvTime.setText(matchSummary.getQuarter());
                    } else {
                        mTvTime.setText("第" + matchSummary.getQuarter() + "节\n" + matchSummary.getTime());
                    }

                } else {
                    mTvStatus.setText(R.string.game_state_unstart);
                    mTvTime.setVisibility(View.GONE);
                }

                mTitleBar.setTitle(matchSummary.getHomeName() + " vs " + matchSummary.getAwayName());
            }
        });

        mGameSummaryFragment = GameSummaryFragment.newInstance();

        mGameStatisticMainFragment = GameStatisticMainFragment.newInstance(game_id);
        mGameStatisticMainFragment.setOnDataUpdateListener(new GameStatisticMainFragment.OnDataUpdateListener() {
            @Override
            public void onUpdate(List<GamePlayerData> homeData, List<GamePlayerData> awayData) {
                mGameSummaryFragment.refreshData(homeData, awayData);
            }
        });

        mFragments.add(mGameLiveFragment);
        mFragments.add(mGameSummaryFragment);
        mFragments.add(mGameStatisticMainFragment);

        ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();

        for (int i = 0; i < TITLES.length; i++) {
            tabEntities.add(createTabEntity(TITLES[i]));
        }

        mTabLayout.setTabData(tabEntities, this, R.id.fl_layout, mFragments);

    }

    /**
     * 默认tab图标
     *
     * @param title
     * @return
     */
    private CustomTabEntity createTabEntity(final String title) {
        return new CustomTabEntity() {
            @Override
            public String getTabTitle() {
                return title;
            }

            @Override
            public void setSelectedIcon(ImageView view) {
            }

            @Override
            public void setUnSelectedIcon(ImageView view) {
            }
        };
    }

    @Override
    public GameDetailContract.Presenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new GameDetailPresenter(this);
        }
        return mPresenter;
    }

    @Override
    public void setPresenter(GameDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
