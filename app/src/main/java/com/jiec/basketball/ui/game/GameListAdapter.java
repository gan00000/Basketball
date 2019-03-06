package com.jiec.basketball.ui.game;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseListAdapter;
import com.jiec.basketball.core.BallApplication;
import com.jiec.basketball.entity.GameInfo;
import com.jiec.basketball.entity.GameProgress;
import com.jiec.basketball.utils.ImageLoaderUtils;
import com.wangcj.common.widget.irecyclerview.IViewHolder;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangchuangjie on 2018/4/21.
 */

public class GameListAdapter extends BaseListAdapter<GameInfo> {

    Map<String, GameProgress.MatchProgress> startedGame = new HashMap();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_list, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final GameInfo gameInfo = getItem(position);

        if (holder instanceof ItemViewHolder && gameInfo != null) {

            ImageLoaderUtils.display(BallApplication.getContext(), ((ItemViewHolder) holder).mIvTeamHome, gameInfo.getHomeLogo());
            ((ItemViewHolder) holder).mTvTeamHome.setText(gameInfo.getHomeName());
            ((ItemViewHolder) holder).mTvScoreHome.setText(gameInfo.getHome_pts());

            ImageLoaderUtils.display(BallApplication.getContext(), ((ItemViewHolder) holder).mIvTeamAway, gameInfo.getAwayLogo());
            ((ItemViewHolder) holder).mTvTeamAway.setText(gameInfo.getAwayName());
            ((ItemViewHolder) holder).mTvScoreAway.setText(gameInfo.getAway_pts());


            if (Integer.parseInt(gameInfo.getHome_pts()) > Integer.parseInt(gameInfo.getAway_pts())) {
                ((ItemViewHolder) holder).mTvScoreHome.setTextColor(BallApplication.getContext().getResources().getColor(R.color.black));
                ((ItemViewHolder) holder).mTvScoreAway.setTextColor(BallApplication.getContext().getResources().getColor(R.color.gray_light));
            } else if (Integer.parseInt(gameInfo.getHome_pts()) == Integer.parseInt(gameInfo.getAway_pts())) {
                ((ItemViewHolder) holder).mTvScoreHome.setTextColor(BallApplication.getContext().getResources().getColor(R.color.gray));
                ((ItemViewHolder) holder).mTvScoreAway.setTextColor(BallApplication.getContext().getResources().getColor(R.color.gray));
            } else {
                ((ItemViewHolder) holder).mTvScoreHome.setTextColor(BallApplication.getContext().getResources().getColor(R.color.gray_light));
                ((ItemViewHolder) holder).mTvScoreAway.setTextColor(BallApplication.getContext().getResources().getColor(R.color.black));
            }

            if (getItem(position - 1) != null && getItem(position - 1).getDate().equals(gameInfo.getDate())) {
                ((ItemViewHolder) holder).mTvDate.setVisibility(View.GONE);
            } else {
                ((ItemViewHolder) holder).mTvDate.setText(gameInfo.getDate());
                ((ItemViewHolder) holder).mTvDate.setVisibility(View.VISIBLE);
            }


            if (gameInfo.getScheduleStatus().contains("F")) {
                ((ItemViewHolder) holder).mTvStatus.setText(R.string.game_state_final);
                ((ItemViewHolder) holder).mTvTime.setText("");
            } else if (gameInfo.getScheduleStatus().equals("InProgress")) {
                if (startedGame.containsKey(gameInfo.getId())) {
                    ((ItemViewHolder) holder).mTvStatus.setText(
                            "第" + startedGame.get(gameInfo.getId()).getQuarter() + "節");
                    ((ItemViewHolder) holder).mTvTime.setText(startedGame.get(gameInfo.getId()).getTime());
                } else {
                    ((ItemViewHolder) holder).mTvStatus.setText(R.string.game_state_playing);
                    ((ItemViewHolder) holder).mTvTime.setText(gameInfo.getGametime());
                }
            } else {
                ((ItemViewHolder) holder).mTvStatus.setText(R.string.game_state_unstart);
                ((ItemViewHolder) holder).mTvTime.setText(gameInfo.getGametime());
            }

            ((ItemViewHolder) holder).mLayoutItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickedListener != null) {
                        mOnItemClickedListener.onClick(gameInfo);
                    }
                }
            });
        }
    }


    public void refreshGameInfo(GameProgress.MatchProgress matchProgress) {
        if (mData == null) return;

        for (GameInfo gameInfo : mData) {
            if (gameInfo.getId().equals(matchProgress.getGame_id())) {
                startedGame.put(gameInfo.getId(), matchProgress);
            }
        }
        notifyDataSetChanged();
    }


    static class ItemViewHolder extends IViewHolder {
        @BindView(R.id.iv_team_home)
        ImageView mIvTeamHome;
        @BindView(R.id.tv_team_home)
        TextView mTvTeamHome;
        @BindView(R.id.layout_home)
        RelativeLayout mLayoutHome;
        @BindView(R.id.tv_score_home)
        TextView mTvScoreHome;
        @BindView(R.id.tv_score_away)
        TextView mTvScoreAway;
        @BindView(R.id.iv_team_away)
        ImageView mIvTeamAway;
        @BindView(R.id.tv_team_away)
        TextView mTvTeamAway;
        @BindView(R.id.layout_away)
        RelativeLayout mLayoutAway;
        @BindView(R.id.tv_status)
        TextView mTvStatus;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_date)
        TextView mTvDate;
        @BindView(R.id.layout_item)
        RelativeLayout mLayoutItem;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
