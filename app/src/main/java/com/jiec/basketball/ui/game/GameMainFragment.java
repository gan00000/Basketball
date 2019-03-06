package com.jiec.basketball.ui.game;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseListAdapter;
import com.jiec.basketball.base.BaseListFragment;
import com.jiec.basketball.entity.GameInfo;
import com.jiec.basketball.entity.GameList;
import com.jiec.basketball.entity.GameProgress;
import com.jiec.basketball.ui.game.detail.GameDetailActivity;
import com.jiec.basketball.utils.TimeUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.OnClick;


public class GameMainFragment extends BaseListFragment implements GameMainContract.View {

    private static final int MSG_REFRESH_PROGRESS = 10000;
    private static final int MSG_REFRESH_LIST = 10001;

    private static final int REFRESH_PROGRESS_TIME = 25000;
    private static final int REFRESH_LIST_TIME = 25000;

    GameMainContract.Presenter mPresenter;

    String mStartDate, mEndDate;

    GameListAdapter mGameListAdapter;

    @BindView(R.id.tv_time)
    TextView mTvTime;

    public static GameMainFragment newInstance() {

        Bundle args = new Bundle();

        GameMainFragment fragment = new GameMainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void handleUiMessage(Message msg) {
        if (msg.what == MSG_REFRESH_PROGRESS) {
            getPresenter().getMatchProgress((String) (msg.obj));
        } else if (msg.what == MSG_REFRESH_LIST) {
            getPresenter().getGameInfo(mStartDate, mEndDate);
        }
    }

    @Override
    protected void initView(View view) {
        super.initView(view);

        mStartDate = TimeUtil.getToday();
        mEndDate = TimeUtil.getFetureDate(mStartDate, 7);

        setShowTime();
    }

    private void setShowTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatView = new SimpleDateFormat("MM-dd");

        try {
            mTvTime.setText(formatView.format(format.parse(mStartDate)) +
                    "至" + formatView.format(format.parse(mEndDate)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void loadData(int page, int num) {
        getPresenter().getGameInfo(mStartDate, mEndDate);
    }

    @Override
    protected BaseListAdapter createAdapter() {
        mGameListAdapter = new GameListAdapter();
        return mGameListAdapter;
    }

    @Override
    protected BaseListAdapter.OnItemClickedListener<GameInfo> createItemClickedListener() {
        return new BaseListAdapter.OnItemClickedListener<GameInfo>() {
            @Override
            public void onClick(GameInfo data) {
                GameDetailActivity.show(getContext(), ((GameInfo) data).getId());
            }
        };
    }

    @Override
    protected int getLayoutResourceID() {
        return R.layout.fragment_gamemain;
    }

    @Override
    public GameMainContract.Presenter getPresenter() {
        if (mPresenter == null) {
            mPresenter = new GameMainPresenter(this);
        }
        return mPresenter;
    }

    @Override
    public void setPresenter(GameMainContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showGameList(GameList list) {
        if (list == null) showEmpty();

        for (GameInfo gameInfo : list.getMatches()) {
            if (gameInfo.getScheduleStatus().equals("InProgress")) {
                getPresenter().getMatchProgress(gameInfo.getId());
            }
        }

        mGameListAdapter.setData(list.getMatches());
        showData(1, list.getMatches());


        removeUiMessage(MSG_REFRESH_LIST);
        sendEmptyUiMessageDelayed(MSG_REFRESH_LIST, REFRESH_LIST_TIME);
    }

    @Override
    public void loadGameProgressSuccess(GameProgress gameProgress) {
        if (gameProgress == null) return;

        if (gameProgress.getMatch_progress().getScheduleStatus().equals("InProgress")) {
            mGameListAdapter.refreshGameInfo(gameProgress.getMatch_progress());
            Message msg = Message.obtain();
            msg.what = MSG_REFRESH_PROGRESS;
            msg.obj = gameProgress.getMatch_progress().getGame_id();

            removeUiMessage(MSG_REFRESH_PROGRESS);
            sendUiMessageDelayed(msg, REFRESH_PROGRESS_TIME);
        }
    }

    @OnClick({R.id.layout_past, R.id.layout_next})
    public void onViewClicked(View view) {
        removeUiAllMsg();

        switch (view.getId()) {
            case R.id.layout_past:
                mEndDate = TimeUtil.getPastDate(mStartDate, 1);
                mStartDate = TimeUtil.getPastDate(mEndDate, 7);
                updateData();
                break;
            case R.id.layout_next:
                mStartDate = TimeUtil.getFetureDate(mEndDate, 1);
                mEndDate = TimeUtil.getFetureDate(mStartDate, 7);
                updateData();
                break;
        }

        setShowTime();
    }

    @Override
    public void onPause() {
        super.onPause();
        removeUiAllMsg();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateData();
    }
}
