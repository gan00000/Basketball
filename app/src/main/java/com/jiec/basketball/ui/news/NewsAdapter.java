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
import com.jiec.basketball.entity.NewsBean;
import com.jiec.basketball.ui.news.detail.DetaillWebActivity;
import com.jiec.basketball.utils.AppUtil;
import com.jiec.basketball.utils.GlideImageLoader;
import com.jiec.basketball.utils.ImageLoaderUtils;
import com.jiec.basketball.utils.ThumbnailUtils;
import com.wangcj.common.utils.LogUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerClickListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Description : 新闻列表适配器
 * Author : jiec
 * Date   : 17-1-6
 */
public class NewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ADMOB = 2;
    private static final int TYPE_BANNER = 3;

    private List<NewsBean> mData = new ArrayList<>();
    private List<NewsBean> mBannerData = new ArrayList<>();
    private boolean mShowFooter = true;
    private Context mContext;

    private OnItemClickListener mOnItemClickListener;

    public NewsAdapter(Context context) {
        this.mContext = context;
    }

    public void setmDate(List<NewsBean> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }

    public void setBannerData(List<NewsBean> list) {
        this.mBannerData = list;
        this.notifyDataSetChanged();
    }

    private int getBannerCount() {
        if (mBannerData != null && mBannerData.size() > 0) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        // 最后一个item设置为footerView
        if (!mShowFooter) {
            return getType(position);
        }

        if (position + 1 == getItemCount()) {
            return TYPE_FOOTER;
        } else {
            return getType(position);
        }
    }

    /**
     * 根据位置获取当前view的类型
     *
     * @param position
     * @return
     */
    public int getType(int position) {
        if (position == 0 && mBannerData.size() > 0) {
            return TYPE_BANNER;
        }

        if (position == 3 || (position - 3) % 8 == 0) {
            return TYPE_ADMOB;
        }

        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_news, parent, false);
            ItemViewHolder vh = new ItemViewHolder(v);
            return vh;
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.footer, null);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            if (this.mShowFooter) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }

            return new FooterViewHolder(view);
        } else if (viewType == TYPE_ADMOB) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_ad, parent, false);
            AdViewHolder vh = new AdViewHolder(v);
            return vh;
        } else {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_banner, parent, false);
            BannerViewHolder vh = new BannerViewHolder(v);
            return vh;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            final NewsBean news = mData.get(position - getAdSize(position) - getBannerCount());
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
        } else if (holder instanceof AdViewHolder) {
            AdRequest adRequestBottom = new AdRequest.Builder().build();
            ((AdViewHolder) holder).adView.loadAd(adRequestBottom);

        } else if (holder instanceof BannerViewHolder) {
            List<String> images = new ArrayList<>();
            List<String> titles = new ArrayList<>();
            for (NewsBean bean : mBannerData) {
                images.add(bean.getImgsrc());
                titles.add(bean.getTitle());
            }

            ((BannerViewHolder) holder).banner.setVisibility(View.VISIBLE);
            ((BannerViewHolder) holder).banner.setImages(images);
            ((BannerViewHolder) holder).banner.setBannerTitles(titles);
            ((BannerViewHolder) holder).banner.setOnBannerClickListener(new OnBannerClickListener() {
                @Override
                public void OnBannerClick(int position) {
                    if (position < 1 && position > mBannerData.size()) return;

                    NewsBean newsBean = mBannerData.get(position - 1);
                    DetaillWebActivity.show(mContext, newsBean.getId(), newsBean.getTotal_comment());
                }
            });
            ((BannerViewHolder) holder).banner.start();
        }
    }

    @Override
    public int getItemCount() {
        int begin = mShowFooter ? 1 : 0;
        if (mData == null) {
            return begin;
        }

        int adSize = mData.size() > 2 ? ((mData.size() - 3) / 7 + 1) : 0;

        int sumSize = mData.size() + begin + adSize + getBannerCount();

        return sumSize;
    }

    public NewsBean getItem(int position) {
        return mData == null ? null : mData.get(position);
    }

    public void isShowFooter(boolean showFooter) {
        this.mShowFooter = showFooter;
    }

    public boolean isShowFooter() {
        return this.mShowFooter;
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


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {

        public AdView adView;

        public AdViewHolder(View itemView) {
            super(itemView);

            adView = (AdView) itemView.findViewById(R.id.adView_bottom);
        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {

        public Banner banner;

        public BannerViewHolder(View itemView) {
            super(itemView);

            banner = (Banner) itemView.findViewById(R.id.banner);
            //设置banner样式
            banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE);
            //设置图片加载器
            banner.setImageLoader(new GlideImageLoader());
            //设置轮播时间
            banner.setDelayTime(3000);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view,
                        this.getPosition() - getAdSize(this.getPosition()) - getBannerCount());
            }
        }
    }

}
