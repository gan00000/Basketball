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
import com.jiec.basketball.entity.GameLivePost;
import com.jiec.basketball.entity.MatchSummary;
import com.jiec.basketball.entity.Matches;
import com.jiec.basketball.entity.response.GameLivePostResponse;
import com.jiec.basketball.utils.Util;

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
        void onUpdate(MatchSummary matchSummary, Matches matches,  ArrayList<Integer> minScoreGap, GameLiveInfo gameLiveInfo);

        void onUpdateLiveVideo(GameLivePost gameLivePost);
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

    public void loadLiveData(String gameId) {
        getPresenter().getLivePost(gameId);
    }

    public void loadVideoLiveData() {
        getPresenter().getLivePost(mGameId);
    }


    @Override
    protected BaseListAdapter createAdapter() {
        mGameLiveAdapter = new GameLiveAdapter(requireActivity());
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
    public void loadLiveVideoSuccess(GameLivePostResponse info) {

        if (info != null && mGameLiveUpdateListener != null && info.getResult() != null && !info.getResult().isEmpty()){
            mGameLiveUpdateListener.onUpdateLiveVideo(info.getResult().get(0));
        }
    }

    ArrayList<Integer> minScoreGap = new ArrayList<>();

    @Override
    public void loadLiveSuccess(GameLiveInfo gameLiveInfo) {
        if (gameLiveInfo == null) return;

        List<GameLiveInfo.LiveFeedBean> liveFeedBeans = new ArrayList<>();

        if (gameLiveInfo.getLive_feed() != null) {

            GameLiveInfo.LiveFeedBean currentGameInfo = null;

//            Collections.reverse(gameLiveInfo.getLive_feed());
            for (List<GameLiveInfo.LiveFeedBean> beans : gameLiveInfo.getLive_feed()) {
//                Collections.reverse(beans);
                liveFeedBeans.addAll(beans);

                for (GameLiveInfo.LiveFeedBean gameInfo: beans) {//判断是否得分,得分文字颜色改变

                    if (currentGameInfo != null) {
                        if(Util.stringToInt(gameInfo.getHomePts()) >  Util.stringToInt(currentGameInfo.getHomePts()) ||  //比赛结束后，数据升序
                                Util.stringToInt(gameInfo.getAwayPts()) >  Util.stringToInt(currentGameInfo.getAwayPts()) ){
                            gameInfo.setGetPts(true);

                        }else if (Util.stringToInt(gameInfo.getHomePts()) <  Util.stringToInt(currentGameInfo.getHomePts()) || //比赛进行中，数据倒序
                                Util.stringToInt(gameInfo.getAwayPts()) <  Util.stringToInt(currentGameInfo.getAwayPts()) ){
                            currentGameInfo.setGetPts(true);
                        }
                    }
                    currentGameInfo = gameInfo;
                }

               /* for (int i = 10; i >= -1; i--) {//计算每分钟分差

                    if (i == -1){
                        GameLiveInfo.LiveFeedBean liveFeedBean = beans.get(beans.size() - 1);
                        int homePts = Integer.valueOf(liveFeedBean.getHomePts());
                        int awayPts = Integer.valueOf(liveFeedBean.getAwayPts());
                        minScoreGap.add(awayPts - homePts);
                    }else{

                        for (GameLiveInfo.LiveFeedBean liveFeedBean: beans) {
                            String min = liveFeedBean.getMinutes().trim();
                            if (Integer.valueOf(min) == i){
                                int homePts = Integer.valueOf(liveFeedBean.getHomePts());
                                int awayPts = Integer.valueOf(liveFeedBean.getAwayPts());
                                minScoreGap.add(awayPts - homePts);
                                break;

                            }

                        }

                    }

                }*/

            }
            mGameLiveAdapter.setGameLiveInfo(gameLiveInfo);
            mGameLiveAdapter.setData(liveFeedBeans);
            showData(1, liveFeedBeans);
        } else {
            showEmpty();
        }

        if (mGameLiveUpdateListener != null
                && gameLiveInfo.getMatch_summary() != null
                && gameLiveInfo.getMatches() != null) {
            mGameLiveUpdateListener.onUpdate(
                    gameLiveInfo.getMatch_summary().get(0), gameLiveInfo.getMatches().get(0),  minScoreGap,gameLiveInfo );
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
