package com.jiec.basketball.ui.data.player;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jiec.basketball.R;
import com.jiec.basketball.entity.AllPlayerData;
import com.jiec.basketball.utils.ImageLoaderUtils;
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
    private  Context context;

    public PlayerDetailAdapter(Context mContext, int type) {
        mType = type;
        this.context = mContext;
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
                holder.mTvRank.setText((position + 1) + ".");
                holder.mTvName.setText(playerInfo.getFirstname().substring(0, 1) + "." + playerInfo.getLastname());
                holder.mTvTeam.setText(playerInfo.getTeamName());
                holder.mTvValue.setText(NumberUtils.formatAmount(playerInfo.getType_avg()));

                ImageLoaderUtils.display(this.context,holder.mPlayerIcon, playerInfo.getOfficialImagesrc(),R.drawable.img_default_head, R.drawable.img_default_head);
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

        @BindView(R.id.tv_player_icon)
        ImageView mPlayerIcon;

        ItemViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
