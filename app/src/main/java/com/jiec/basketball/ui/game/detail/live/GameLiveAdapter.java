package com.jiec.basketball.ui.game.detail.live;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseListAdapter;
import com.jiec.basketball.entity.GameLiveInfo;
import com.jiec.basketball.entity.MatchSummary;
import com.wangcj.common.widget.irecyclerview.IViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangchuangjie on 2018/4/21.
 */

public class GameLiveAdapter extends BaseListAdapter<GameLiveInfo.LiveFeedBean> {


    Context context;
    GameLiveInfo gameLiveInfo;
    MatchSummary mMatchSummary;

    public GameLiveAdapter(Context context) {
        this.context = context;
    }

    public void setGameLiveInfo(GameLiveInfo gameLiveInfo) {
        this.gameLiveInfo = gameLiveInfo;
        if (gameLiveInfo != null && gameLiveInfo.getMatch_summary() != null && !gameLiveInfo.getMatch_summary().isEmpty()){
            mMatchSummary = gameLiveInfo.getMatch_summary().get(0);
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_game_live, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final GameLiveInfo.LiveFeedBean gameInfo = getItem(position);

        if (holder instanceof ItemViewHolder && gameInfo != null) {

            if (gameInfo.isGetPts()){//得分字体加粗
                ((ItemViewHolder) holder).mTvTime.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
                ((ItemViewHolder) holder).mTvScore.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
                ((ItemViewHolder) holder).mTvEvent.setTypeface(Typeface.DEFAULT,Typeface.BOLD);
            }else{
                ((ItemViewHolder) holder).mTvTime.setTypeface(Typeface.DEFAULT,Typeface.NORMAL);
                ((ItemViewHolder) holder).mTvScore.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
                ((ItemViewHolder) holder).mTvEvent.setTypeface(Typeface.DEFAULT, Typeface.NORMAL);
            }


            ((ItemViewHolder) holder).mTvTime.setText(gameInfo.getTime());
//            ((ItemViewHolder) holder).mTvTeam.setText(gameInfo.getTeamName());
            ((ItemViewHolder) holder).mTvEvent.setText(gameInfo.getDescription());

            ((ItemViewHolder) holder).mTvScore.setText(gameInfo.getPts());

            if (mMatchSummary != null){
                if (mMatchSummary.getHomeName().equals(gameInfo.getTeamName())){
                    Glide.with(context).load(mMatchSummary.getHomeLogo()).into(((ItemViewHolder) holder).tv_team_iv);
                }else {
                    Glide.with(context).load(mMatchSummary.getAwayLogo()).into(((ItemViewHolder) holder).tv_team_iv);
                }
            }


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickedListener != null) {
                        mOnItemClickedListener.onClick(gameInfo);
                    }
                }
            });

            if (position % 2 == 0){
                ((ItemViewHolder) holder).mView.setBackgroundColor(context.getResources().getColor(R.color.white));
            }else {
                ((ItemViewHolder) holder).mView.setBackgroundColor(context.getResources().getColor(R.color.c_f9f9fb));
            }

        }
    }

    static class ItemViewHolder extends IViewHolder {
        @BindView(R.id.tv_time)
        TextView mTvTime;
//        @BindView(R.id.tv_team)
//        TextView mTvTeam;
        @BindView(R.id.tv_event)
        TextView mTvEvent;
        @BindView(R.id.tv_score)
        TextView mTvScore;
        @BindView(R.id.tv_team_iv)
        ImageView tv_team_iv;

        @BindView(R.id.item_game_live_ll)
        View mView;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
