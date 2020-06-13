package com.jiec.basketball.ui.game.detail.statistic;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseUIFragment;
import com.jiec.basketball.entity.GameDataInfo;
import com.jiec.basketball.entity.GamePlayerData;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class GameStatisticMainFragment extends BaseUIFragment implements GameStatisticMainContract.View {

    private static final int MSG_REFRESH = 10000;

    private static final int REFRESH_TIME = 5000;

    GameStatisticMainContract.Presenter mPresenter;

    Unbinder unbinder;

    String mGameId;
    @BindView(R.id.tab_layout)
    CommonTabLayout mTabLayout;
    @BindView(R.id.fl_layout)
    FrameLayout mFlLayout;

    private boolean mIsLiving = false;

    private ArrayList<Fragment> mFragments;

    private static final String[] TITLES = {"主隊", "客隊"};


    GameStatisticFragment mHomeFragment, mAwayFragment;

    public interface OnDataUpdateListener {
        void onUpdate(List<GamePlayerData> homeData, List<GamePlayerData> awayData);
    }

    OnDataUpdateListener mOnDataUpdateListener;

    public void setOnDataUpdateListener(OnDataUpdateListener onDataUpdateListener) {
        mOnDataUpdateListener = onDataUpdateListener;
    }

    public static GameStatisticMainFragment newInstance(String game_id) {

        Bundle args = new Bundle();
        args.putString("game_id", game_id);

        GameStatisticMainFragment fragment = new GameStatisticMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_gamestatistic_main, container, false);
        unbinder = ButterKnife.bind(this, view);

        initView();

        mGameId = getArguments().getString("game_id");

        getPresenter().loadData(mGameId);
        return view;
    }

    @Override
    public void handleUiMessage(Message msg) {
        switch (msg.what) {
            case MSG_REFRESH:
                getPresenter().loadData(mGameId);
                if (mIsLiving) {
                    removeUiMessage(MSG_REFRESH);
                    sendEmptyUiMessageDelayed(MSG_REFRESH, REFRESH_TIME);
                }
                break;
        }
    }

    private void initView() {


        mFragments = new ArrayList<>();

        mAwayFragment = GameStatisticFragment.newInstance();
        mHomeFragment = GameStatisticFragment.newInstance();


        mFragments.add(mHomeFragment);
        mFragments.add(mAwayFragment);

        ArrayList<CustomTabEntity> tabEntities = new ArrayList<>();

        for (int i = 0; i < TITLES.length; i++) {
            tabEntities.add(createTabEntity(TITLES[i]));
        }

        mTabLayout.setTabData(tabEntities, getChildFragmentManager(), R.id.fl_layout, mFragments);

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
    public GameStatisticMainContract.Presenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new GameStatisticMainPresenter(this);
        }
        return mPresenter;
    }

    @Override
    public void setPresenter(GameStatisticMainContract.Presenter presenter) {
        mPresenter = presenter;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void loadDataSuccess(GameDataInfo gameDataInfo) {
        String homeId = gameDataInfo.getMatch_summary().get(0).getHomeTeam();
        String awayId = gameDataInfo.getMatch_summary().get(0).getAwayTeam();

        if (gameDataInfo.getMatch_summary() != null
                && gameDataInfo.getMatch_summary().get(0).getScheduleStatus().equals("InProgress")) {
            if (!mIsLiving) {
                mIsLiving = true;
                removeUiMessage(MSG_REFRESH);
                sendEmptyUiMessageDelayed(MSG_REFRESH, REFRESH_TIME);
            }
        } else {
            mIsLiving = false;
            removeUiMessage(MSG_REFRESH);
        }

        mHomeFragment.setData(gameDataInfo.getMatchDetailsMaps().get(homeId), mIsLiving);
        mAwayFragment.setData(gameDataInfo.getMatchDetailsMaps().get(awayId), mIsLiving);


        if (mOnDataUpdateListener != null) {
            mOnDataUpdateListener.onUpdate(gameDataInfo.getMatchDetailsMaps().get(homeId),
                    gameDataInfo.getMatchDetailsMaps().get(awayId));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mIsLiving) {
            removeUiMessage(MSG_REFRESH);
            sendEmptyUiMessage(MSG_REFRESH);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        removeUiMessage(MSG_REFRESH);
    }
}
