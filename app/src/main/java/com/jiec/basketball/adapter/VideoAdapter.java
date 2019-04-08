package com.jiec.basketball.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;
import com.jiec.basketball.R;
import com.jiec.basketball.core.DeveloperKey;
import com.jiec.basketball.entity.NewsBean;
import com.jiec.basketball.utils.AppUtil;
import com.jiec.basketball.utils.ImageLoaderUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Description : 列表适配器
 * Author : jiec
 * Date   : 17-1-6
 */
public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> mData = new ArrayList<>();
    private Context mContext;

    public VideoAdapter(Context context) {
        this.mContext = context;
    }

    public void setmDate(List<String> data) {
        this.mData = data;
        this.notifyDataSetChanged();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_video, parent, false);
        ItemViewHolder vh = new ItemViewHolder(v);
        return vh;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {

            String[] str = mData.get(position).split("/");
            String videoUrl = str[str.length - 1];

            if (videoUrl == null) {
                return;
            }

            DisplayMetrics metrics = mContext.getApplicationContext().getResources().getDisplayMetrics();
            int w1 = (int) ((metrics.widthPixels / metrics.density) * 9 / 10), h1 = w1 * 3 / 5;

            String html = "<iframe src=\"" + mData.get(position) + "\" width=\"" +
                    w1 + "\" height=\"" + h1 + "\" " +
                    "frameborder=\"0\"  webkitallowfullscreen mozallowfullscreen allowfullscreen></iframe>";

            ((ItemViewHolder) holder).webView.loadData(html, "text/html", "utf-8");

        }
    }

    @Override
    public int getItemCount() {
        if (mData == null) {
            return 0;
        }
        return mData.size();
    }

    public String getItem(int position) {
        return mData == null ? null : mData.get(position);
    }


    public class ItemViewHolder extends RecyclerView.ViewHolder {

        public WebView webView;

        public ItemViewHolder(View v) {
            super(v);
            webView = (WebView) v.findViewById(R.id.webview_video);
            WebSettings webSettings = webView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setSupportZoom(false);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setAppCacheEnabled(true);
            webView.setWebViewClient(new WebViewClient());
        }
    }
}
