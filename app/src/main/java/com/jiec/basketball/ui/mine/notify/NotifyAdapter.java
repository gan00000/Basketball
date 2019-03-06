package com.jiec.basketball.ui.mine.notify;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseListAdapter;
import com.jiec.basketball.entity.response.NotifyResponse;


/**
 * Description : 列表适配器
 * Author : jiec
 * Date   : 17-1-6
 */
public class NotifyAdapter extends BaseListAdapter<NotifyResponse.Result.NotificationBean> {

    private Context mContext;

    public NotifyAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notify, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            NotifyResponse.Result.NotificationBean bean = getItem(position);
            if (bean != null) {
                if (bean.getType().equals("like")) {
                    ((ItemViewHolder) holder).mTitle.setText(bean.getDisplay_name() + "點贊了你的評論");
                }

                ((ItemViewHolder) holder).mTime.setText(bean.getCreated_on());
            }
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mTime;

        public ItemViewHolder(View v) {
            super(v);
            mTitle = v.findViewById(R.id.tv_title);
            mTime = v.findViewById(R.id.tv_time);
        }
    }

}
