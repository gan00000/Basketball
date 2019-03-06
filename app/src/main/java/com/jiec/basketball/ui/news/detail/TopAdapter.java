package com.jiec.basketball.ui.news.detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiec.basketball.R;
import com.jiec.basketball.entity.NewsBean;
import com.jiec.basketball.utils.AppUtil;
import com.jiec.basketball.utils.ImageLoaderUtils;
import com.jiec.basketball.utils.ThumbnailUtils;
import com.wangcj.common.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Description : 推荐列表适配器
 * Author : jiec
 * Date   : 17-1-6
 */
public class TopAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NewsBean> mData = new ArrayList<>();

    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    public TopAdapter(Context context) {
        this.mContext = context;
    }

    public void setData(List<NewsBean> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news, parent, false);
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
            ((ItemViewHolder) holder).mTitle.setText(Html.fromHtml(news.getTitle()));
            ((ItemViewHolder) holder).mAuthor.setText(news.getAuthorName());
            ((ItemViewHolder) holder).mKind.setText(news.getKind());
            ((ItemViewHolder) holder).mTime.setText(AppUtil.getStandardDate(news.getDate()));
            ((ItemViewHolder) holder).mViews.setText(news.getSumViews() + "");

            if (TextUtils.isEmpty(news.getImagesrc())) {
                ThumbnailUtils.getThumbnail(news.getId(), new ThumbnailUtils.OnLoadSuccessLisener() {
                    @Override
                    public void onSuccess(String url) {
                        LogUtil.e("load thumbnail url = " + url);
                        ImageLoaderUtils.display(mContext, ((ItemViewHolder) holder).mNewsImg, url);
                    }
                });
            } else {
                ImageLoaderUtils.display(mContext, ((ItemViewHolder) holder).mNewsImg, news.getImgsrc());
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(news);
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (mData != null) {
            return mData.size();
        }

        return 0;
    }

    public NewsBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(NewsBean newsBean);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public ImageView mNewsImg;
        public TextView mAuthor;
        public TextView mKind;
        public TextView mTime;
        public TextView mViews;

        public ItemViewHolder(View v) {
            super(v);
            mTitle = (TextView) v.findViewById(R.id.tv_title);
            mNewsImg = (ImageView) v.findViewById(R.id.iv_news);
            mAuthor = (TextView) v.findViewById(R.id.tv_author);
            mKind = (TextView) v.findViewById(R.id.tv_kind);
            mTime = (TextView) v.findViewById(R.id.tv_time);
            mViews = (TextView) v.findViewById(R.id.tv_views);
        }
    }

}
