package com.jiec.basketball.adapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiec.basketball.base.BaseListAdapter;
import com.jiec.basketball.core.BallApplication;
import com.jiec.basketball.entity.NewsBean;
import com.jiec.basketball.utils.AppUtil;
import com.jiec.basketball.utils.ImageLoaderUtils;
import com.wangcj.common.widget.CircleSImageView;
import com.jiec.basketball.R;


/**
 * 我的評論列表适配器
 */
public class MyCommentAdapter extends BaseListAdapter<NewsBean> {

    private Context mContext;

    public MyCommentAdapter(Context context) {
        this.mContext = context;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_comment, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            final NewsBean news = mData.get(position);
            if (news == null) {
                return;
            }
            ((ItemViewHolder) holder).tvAuthor.setText(BallApplication.userInfo.display_name);
            ImageLoaderUtils.display(mContext, ((ItemViewHolder) holder).ivHead,
                    BallApplication.userInfo.user_img,
                    R.drawable.img_default_head, R.drawable.img_default_head);
            ((ItemViewHolder) holder).tvTime.setText(AppUtil.getStandardDate(news.getComment_date()));
            ((ItemViewHolder) holder).tvComment.setText(news.getComment_content());
            ((ItemViewHolder) holder).tvTitle.setText(news.getTitle());
//            ((ItemViewHolder) holder).tvTitle.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
            ((ItemViewHolder) holder).tvZan.setText("("+news.getTotal_like()+")");

            if (news.getTotal_like() == 0) {
                ((ItemViewHolder) holder).mIvLike.setImageResource(R.drawable.icon_great_normal);
                ((ItemViewHolder) holder).tvZan.setTextColor(mContext.getResources().getColor(R.color.gray));
            } else {
                ((ItemViewHolder) holder).mIvLike.setImageResource(R.drawable.icon_great_pressed);
                ((ItemViewHolder) holder).tvZan.setTextColor(mContext.getResources().getColor(R.color.red));
            }

            ((ItemViewHolder) holder).tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickedListener != null) {
                        mOnItemClickedListener.onClick(news);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public NewsBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private CircleSImageView ivHead;
        private TextView tvAuthor;
        private TextView tvTime;
        private TextView tvZan;
        private TextView tvComment;
        private TextView tvTitle;
        private ImageView mIvLike;

        public ItemViewHolder(View v) {
            super(v);
            ivHead = (CircleSImageView)v.findViewById( R.id.iv_head );
            tvAuthor = (TextView)v.findViewById( R.id.tv_author );
            tvTime = (TextView)v.findViewById( R.id.tv_time );
            tvZan = (TextView)v.findViewById( R.id.tv_zan );
            tvComment = (TextView)v.findViewById( R.id.tv_comment );
            tvTitle = (TextView)v.findViewById( R.id.tv_news_title );
            mIvLike = (ImageView)v.findViewById(R.id.iv_zan);
        }
    }

}
