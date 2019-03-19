package com.jiec.basketball.ui.film;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.jiec.basketball.R;
import com.jiec.basketball.entity.NewsBean;
import com.jiec.basketball.ui.MainActivity;
import com.jiec.basketball.ui.news.detail.DetaillWebActivity;
import com.jiec.basketball.utils.AppUtil;
import com.jiec.basketball.utils.EmptyUtils;
import com.jiec.basketball.utils.ImageLoaderUtils;
import com.jiec.basketball.utils.ThumbnailUtils;
import com.wangcj.common.utils.LogUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Description : 列表适配器
 * Author : jiec
 * Date   : 17-1-6
 */
public class FilmListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ADMOB = 2;

    private List<NewsBean> mData = new ArrayList<>();
    private boolean mShowFooter = true;
    private Context mContext;
    private MainActivity mActivity;

    private OnItemClickListener mOnItemClickListener;

    public FilmListAdapter(MainActivity mActivity) {
        this.mActivity = mActivity;
        this.mContext = mActivity;
    }

    /**
     * 设置数据
     * @param data
     */
    public void setmDate(List<NewsBean> data) {
        if (data != null && data.size() > 0) {
            for (NewsBean bean : data) {
                if (AppUtil.getVideoId(bean.getContent()) != null) {
                    bean.setVideoId(AppUtil.getVideoId(bean.getContent()));
                    this.mData.add(bean);
                }
            }
        } else {
            this.mData.clear();
        }
        this.notifyDataSetChanged();
    }

    /**
     * 添加列表数据
     * @param data
     */
    public void addData(List<NewsBean>data) {
        if (this.mData == null) {
            setmDate(data);
        } else {
            if(EmptyUtils.emptyOfList(data)){
                return;
            }
            this.mData.addAll(data);
            this.notifyDataSetChanged();
        }
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
        if (position == 3 || (position - 3) % 8 == 0) {
            return TYPE_ADMOB;
        }
        return TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_film, parent, false);
            ItemViewHolder vh = new ItemViewHolder(v);
            return vh;
        } else if (viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.footer, null);
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
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ItemViewHolder) {
            final NewsBean news = mData.get(position - getAdSize(position));
            if (news == null) {
                return;
            }
            ((ItemViewHolder) holder).mTitle.setText(Html.fromHtml(news.getTitle()));
            ((ItemViewHolder) holder).mAuthor.setText(news.getAuthorName());
            ((ItemViewHolder) holder).mKind.setText(news.getKind());
            ((ItemViewHolder) holder).mTime.setText(AppUtil.getStandardDate(news.getDate()));
            ((ItemViewHolder) holder).mViews.setText(news.getSumViews() + "");

            String playUrl = "https://www.youtube.com/embed/"+news.getVideoId();
            Log.e("視頻播放地址", playUrl);
            ((ItemViewHolder) holder).wvFilm.loadUrl(playUrl);

            if (TextUtils.isEmpty(news.getImagesrc())) {
                ThumbnailUtils.getThumbnail(news.getId(), new ThumbnailUtils.OnLoadSuccessLisener() {
                    @Override
                    public void onSuccess(String url) {
                        LogUtil.e("load thumbnail url = " + url);
                        ImageLoaderUtils.display(mContext, ((ItemViewHolder) holder).youTubeThumbnailView, url);
                    }
                });
            } else {
                ImageLoaderUtils.display(mContext, ((ItemViewHolder) holder).youTubeThumbnailView, news.getImgsrc());
            }

            //Item点击处理
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewsBean newsBean = mData.get(position - getAdSize(position));
                    DetaillWebActivity.show(mContext, newsBean.getId());
                }
            });

            //播放按钮点击处理
            ((ItemViewHolder) holder).ivPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(1, news, position - getAdSize(position));
                    }
                }
            });

            //播放区域点击处理
            ((ItemViewHolder) holder).youTubeThumbnailView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(1, news, position - getAdSize(position));
                    }
                }
            });

            ((ItemViewHolder) holder).tvShare.setOnClickListener(v -> {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(2, news, position - getAdSize(position));
                }
            });

        } else if (holder instanceof AdViewHolder) {
            AdRequest adRequestBottom = new AdRequest.Builder().build();
            ((AdViewHolder) holder).adView.loadAd(adRequestBottom);
        }
    }

    @Override
    public int getItemCount() {
        int begin = mShowFooter ? 1 : 0;
        if (mData == null) {
            return begin;
        }
        int adSize = mData.size() > 2 ? ((mData.size() - 3) / 7 + 1) : 0;
        int sumSize = mData.size() + begin + adSize;
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
        //clickType点击类型：1=播放按钮点击，2=分享按钮点击
        void onItemClick(int clickType, NewsBean bean, int position);
    }

    public class AdViewHolder extends RecyclerView.ViewHolder {
        public AdView adView;
        public AdViewHolder(View itemView) {
            super(itemView);
            adView = (AdView) itemView.findViewById(R.id.adView_bottom);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private  TextView mTitle;
        private TextView mAuthor;
        private TextView mKind;
        private  TextView mTime;
        private  TextView tvShare;
        private TextView mViews;
        private ImageView youTubeThumbnailView;
        private ImageView ivPlay;
        private WebView wvFilm;

        public ItemViewHolder(View v) {
            super(v);
            mTitle = v.findViewById(R.id.tv_title);
            mAuthor = v.findViewById(R.id.tv_author);
            mKind = v.findViewById(R.id.tv_kind);
            mTime = v.findViewById(R.id.tv_time);
            tvShare = v.findViewById(R.id.tv_share);
            mViews = v.findViewById(R.id.tv_views);
            youTubeThumbnailView = v.findViewById(R.id.youtube_thumbnail);
            ivPlay = v.findViewById(R.id.iv_play);
            wvFilm = v.findViewById(R.id.wv_film);

            WebSettings mSettings = wvFilm.getSettings();
            mSettings.setJavaScriptEnabled(true);
            mSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
            mSettings.setLoadWithOverviewMode(true); //打开页面时， 自适应屏幕
            wvFilm.setWebChromeClient(new BaseWebChromeClient(new VideoImpl(mActivity, wvFilm)));
        }
    }

}
