package com.jiec.basketball.ui.game.detail.live;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseListAdapter;
import com.jiec.basketball.entity.GameLiveInfo;
import com.wangcj.common.widget.irecyclerview.IViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangchuangjie on 2018/4/21.
 */

public class GameLiveAdapter extends BaseListAdapter<GameLiveInfo.LiveFeedBean> {
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

            ((ItemViewHolder) holder).mTvTime.setText(gameInfo.getTime());
            ((ItemViewHolder) holder).mTvTeam.setText(gameInfo.getTeamName());
            ((ItemViewHolder) holder).mTvEvent.setText(gameInfo.getDescription());
            ((ItemViewHolder) holder).mTvScore.setText(gameInfo.getPts());


            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickedListener != null) {
                        mOnItemClickedListener.onClick(gameInfo);
                    }
                }
            });
        }
    }

    static class ItemViewHolder extends IViewHolder {
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_team)
        TextView mTvTeam;
        @BindView(R.id.tv_event)
        TextView mTvEvent;
        @BindView(R.id.tv_score)
        TextView mTvScore;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
