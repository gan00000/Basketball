package com.gan.ctools.activity;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseActivity;

public class MainActivityTest extends BaseActivity {
    private WebView contentWebView = null;

    private String[] imageUrls = null;//HtmlUtil.returnImageUrlsFromHtml();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        contentWebView =new WebView(this);
//        contentWebView.getSettings().setJavaScriptEnabled(true);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
//            contentWebView.getSettings().setAppCacheEnabled(true);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
//            contentWebView.getSettings().setDatabaseEnabled(true);
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
//            contentWebView.getSettings().setDomStorageEnabled(true);
//        }
//        contentWebView.loadUrl("http://www.gdqynews.com/guonaxinwen/20191111/375515.html");
//        contentWebView.addJavascriptInterface(new MJavascriptInterface(this,imageUrls), "imagelistener");
//        contentWebView.setWebViewClient(new MyWebViewClient());


    }


    @Override
    protected void onDestroy() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(MainActivityTest.this).clearDiskCache();//清理磁盘缓存需要在子线程中执行
            }
        }).start();
        Glide.get(this).clearMemory();//清理内存缓存可以在UI主线程中进行
        super.onDestroy();
    }
}
