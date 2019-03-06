package com.jiec.basketball.ui.game.detail.live;

import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseListAdapter;
import com.jiec.basketball.base.BaseListFragment;
import com.jiec.basketball.entity.GameLiveInfo;
import com.jiec.basketball.entity.MatchSummary;
import com.jiec.basketball.entity.Matches;

import java.util.ArrayList;
import java.util.List;


public class GameLiveFragment extends BaseListFragment implements GameLiveContract.View {

    private static final int MSG_REFRESH = 10000;

    private static final int REFRESH_TIME = 5000;

    GameLiveContract.Presenter mPresenter;

    String mGameId;

    GameLiveAdapter mGameLiveAdapter;

    boolean mIsLiving = false;

    GameLiveUpdateListener mGameLiveUpdateListener;

    public interface GameLiveUpdateListener {
        void onUpdate(MatchSummary matchSummary, Matches matches);
    }

    public void setGameLiveUpdateListener(GameLiveUpdateListener gameLiveUpdateListener) {
        mGameLiveUpdateListener = gameLiveUpdateListener;
    }

    public static GameLiveFragment newInstance(String gameId) {

        Bundle args = new Bundle();
        args.putString("game_id", gameId);

        GameLiveFragment fragment = new GameLiveFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void handleUiMessage(Message msg) {
        switch (msg.what) {
            case MSG_REFRESH:
                updateData();
                if (mIsLiving) {
                    sendEmptyUiMessageDelayed(MSG_REFRESH, REFRESH_TIME);
                }
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mGameId = getArguments().getString("game_id");
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    protected int getLayoutResourceID() {
        return R.layout.fragment_game_live;
    }

    @Override
    protected void loadData(int page, int num) {
        getPresenter().loadLive(mGameId);
    }

    @Override
    protected BaseListAdapter createAdapter() {
        mGameLiveAdapter = new GameLiveAdapter();
        return mGameLiveAdapter;
    }

    @Override
    protected BaseListAdapter.OnItemClickedListener createItemClickedListener() {
        return null;
    }

    @Override
    public GameLiveContract.Presenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new GameLivePresenter(this);
        }
        return mPresenter;
    }

    @Override
    public void setPresenter(GameLiveContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void loadLiveSuccess(GameLiveInfo gameLiveInfo) {
        if (gameLiveInfo == null) return;

        if (gameLiveInfo.getLive_feed() != null) {

            List<GameLiveInfo.LiveFeedBean> liveFeedBeans = new ArrayList<>();
            for (List<GameLiveInfo.LiveFeedBean> beans : gameLiveInfo.getLive_feed()) {
                liveFeedBeans.addAll(beans);
            }
            mGameLiveAdapter.setData(liveFeedBeans);
            showData(1, liveFeedBeans);
        } else {
            showEmpty();
        }

        if (mGameLiveUpdateListener != null
                && gameLiveInfo.getMatch_summary() != null
                && gameLiveInfo.getMatches() != null) {
            mGameLiveUpdateListener.onUpdate(
                    gameLiveInfo.getMatch_summary().get(0), gameLiveInfo.getMatches().get(0));
        }

        if (gameLiveInfo.getMatch_summary() != null
                && gameLiveInfo.getMatch_summary().get(0).getScheduleStatus().equals("InProgress")) {
            if (!mIsLiving) {
                mIsLiving = true;
                sendEmptyUiMessageDelayed(MSG_REFRESH, REFRESH_TIME);
            }
        } else {
            mIsLiving = false;
            removeUiMessage(MSG_REFRESH);
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
