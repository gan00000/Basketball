package com.jiec.basketball.ui.news;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseListAdapter;
import com.jiec.basketball.entity.NewsBean;
import com.jiec.basketball.utils.AppUtil;
import com.jiec.basketball.utils.ImageLoaderUtils;
import com.jiec.basketball.utils.ThumbnailUtils;
import com.wangcj.common.utils.LogUtil;


/**
 * Description : 列表适配器
 * Author : jiec
 * Date   : 17-1-6
 */
public class NewsListAdapter extends BaseListAdapter<NewsBean> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_ADMOB = 1;

    private Context mContext;
    private int adapterType; //适配器类型：1=item中有广告，用于新闻列表页面；2=item无广告，用户收藏、浏览历史==

    public NewsListAdapter(Context context, int adapterType) {
        this.mContext = context;
        this.adapterType = adapterType;
    }

    @Override
    public int getItemViewType(int position) {
        return getType(position);
    }

    /**
     * 根据位置获取当前view的类型
     *
     * @param position
     * @return
     */
    public int getType(int position) {
        if(adapterType == 1){
            if (position == 3 || (position - 3) % 8 == 0) {
                return TYPE_ADMOB;
            }
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_news, parent, false);
            ItemViewHolder vh = new ItemViewHolder(v);
            return vh;
        } else if (viewType == TYPE_ADMOB) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ad, parent, false);
            AdViewHolder vh = new AdViewHolder(v);
            return vh;
        }

        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            if (mData == null || position - getAdSize(position) >= mData.size()) return;

            final NewsBean news = mData.get(position - getAdSize(position));
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
                public void onClick(View view) {
                    if (mOnItemClickedListener != null) {
                        mOnItemClickedListener.onClick(news);
                    }
                }
            });
        } else if (holder instanceof AdViewHolder) {
            AdRequest adRequestBottom = new AdRequest.Builder().build();
            ((AdViewHolder) holder).adView.loadAd(adRequestBottom);

        }
    }

    @Override
    public int getItemCount() {
        int begin = 0;
        if (mData == null) {
            return begin;
        }

        int adSize = mData.size() > 2 ? ((mData.size() - 3) / 7 + 1) : 0;

        return mData.size() + begin + adSize;
    }

    public NewsBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    /**
     * get adsize with list position
     *
     * @param position
     * @return
     */
    private int getAdSize(int position) {
        int adSize = position > 2 ? ((position - 3) / 8 + 1) : 0;
        return adSize;
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {

        public AdView adView;

        public AdViewHolder(View itemView) {
            super(itemView);

            adView = (AdView) itemView.findViewById(R.id.adView_bottom);
        }
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
