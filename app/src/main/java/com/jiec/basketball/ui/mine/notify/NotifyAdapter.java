package com.jiec.basketball.ui.mine.notify;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseListAdapter;
import com.jiec.basketball.entity.response.NotifyResponse;
import com.jiec.basketball.utils.AppUtil;
import com.jiec.basketball.utils.InputCheckUtils;


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
                if (InputCheckUtils.compareIsEqual(bean.getType(), "like")) {
                    //點讚消息通知
                    ((ItemViewHolder) holder).mTitle.setText(bean.getDisplay_name()
                            + "點贊了你的評論："+bean.getComment_content());
                    ((ItemViewHolder) holder).tvReplyContent.setVisibility(View.GONE);
                }else if(InputCheckUtils.compareIsEqual(bean.getType(), "reply")){
                    //評論回復消息通知
                    ((ItemViewHolder) holder).mTitle.setText(bean.getDisplay_name()
                            + "回覆了你的評論："+bean.getComment_content());
                    ((ItemViewHolder) holder).tvReplyContent.setVisibility(View.VISIBLE);
                    ((ItemViewHolder) holder).tvReplyContent.setText(bean.getReply_msg());
                }
                ((ItemViewHolder) holder).tvTitle.setText(bean.getPost_title());
                ((ItemViewHolder) holder).mTime.setText(AppUtil.getStandardDate(bean.getCreated_on()));

                ((ItemViewHolder) holder).tvTitle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mOnItemClickedListener != null) {
                            mOnItemClickedListener.onClick(bean);
                        }
                    }
                });
            }
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mTime;
        private TextView tvTitle;
        private TextView tvReplyContent;

        public ItemViewHolder(View v) {
            super(v);
            mTitle = v.findViewById(R.id.tv_title);
            mTime = v.findViewById(R.id.tv_time);
            tvTitle = (TextView)v.findViewById( R.id.tv_news_title );
            tvReplyContent = v.findViewById(R.id.tv_reply_content);
        }
    }

}
