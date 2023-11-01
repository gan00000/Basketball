package com.jiec.basketball.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jiec.basketball.R;

import java.util.ArrayList;
import java.util.List;


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
//            webSettings.setAppCacheEnabled(true);
            webView.setWebViewClient(new WebViewClient());
        }
    }
}
