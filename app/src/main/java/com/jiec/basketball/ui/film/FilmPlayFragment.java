package com.jiec.basketball.ui.film;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseFragment;


/**
 * 視頻播放Fragment
 */
public class FilmPlayFragment extends BaseFragment {

    private WebView wv_container;
    private String playUrl;
    public static final String KEY_CODE_RESULT = "playUrl";

    /**
     * 单例
     * @return
     */
    public static FilmPlayFragment newInstance(String playUrl){
        FilmPlayFragment mFragment = new FilmPlayFragment();
        Bundle mBundle = new Bundle();
        mBundle.putString(KEY_CODE_RESULT,playUrl);
        mFragment.setArguments(mBundle);
        return mFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        playUrl = getArguments().getString(KEY_CODE_RESULT);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_film_play,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wv_container = (WebView) view.findViewById(R.id.wv_container);
        WebSettings mSettings = wv_container.getSettings();
        mSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        mSettings.setLoadWithOverviewMode(true); //打开页面时， 自适应屏幕
        mSettings.setJavaScriptEnabled(true); //支持js
        mSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口

        wv_container.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
                view.loadUrl(url);
                return true;
            }
        });
        playUrl = "https://www.youtube.com/embed/"+playUrl;
        Log.e("視頻播放地址", playUrl);
        wv_container.loadUrl(playUrl);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
