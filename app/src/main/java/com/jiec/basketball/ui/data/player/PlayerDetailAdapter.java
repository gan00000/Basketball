package com.jiec.basketball.ui.data.player;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiec.basketball.R;
import com.jiec.basketball.entity.AllPlayerData;
import com.jiec.basketball.utils.NumberUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Description : 列表适配器
 * Author : jiec
 * Date   : 17-1-6
 */
public class PlayerDetailAdapter extends RecyclerView.Adapter<PlayerDetailAdapter.ItemViewHolder> {


    List<AllPlayerData.PlayersRankBean> mData = new ArrayList<>();

    int mType;

    public PlayerDetailAdapter(int type) {
        mType = type;
    }

    public void setData(List<AllPlayerData.PlayersRankBean> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_player_rank, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (mData != null && mData.size() > position) {
            AllPlayerData.PlayersRankBean playerInfo = mData.get(position);
            if (playerInfo != null) {
                holder.mTvRank.setText((position + 1) + "、");
                holder.mTvName.setText(playerInfo.getFirstname().substring(0, 1) + "." + playerInfo.getLastname());
                holder.mTvTeam.setText(playerInfo.getTeamName());
                holder.mTvValue.setText(NumberUtils.formatAmount(playerInfo.getType_avg()));
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_rank)
        TextView mTvRank;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_team)
        TextView mTvTeam;
        @BindView(R.id.tv_value)
        TextView mTvValue;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
