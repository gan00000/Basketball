package com.jiec.basketball.ui.data.team;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiec.basketball.R;
import com.jiec.basketball.core.BallApplication;
import com.jiec.basketball.entity.AllTeamData;
import com.jiec.basketball.ui.data.DataUtils;
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
public class TeamDetailAdapter extends RecyclerView.Adapter<TeamDetailAdapter.ItemViewHolder> {


    List<AllTeamData.TeamsRankBean> mData = new ArrayList<>();

    int mType = DataUtils.TYPE_PTS;

    public TeamDetailAdapter(int type) {
        mType = type;
    }

    public void setData(List<AllTeamData.TeamsRankBean> data) {
        mData = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_team, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        if (mData != null && mData.size() > position) {
            AllTeamData.TeamsRankBean playerInfo = mData.get(position);
            if (playerInfo != null) {
                holder.mTvRank.setText((position + 1) + "、");
                ImageLoaderUtils.display(BallApplication.getContext(), holder.mIvHead, playerInfo.getTeam_logo(),
                        R.drawable.img_default_team, R.drawable.img_default_team);
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
        @BindView(R.id.iv_head)
        ImageView mIvHead;
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
