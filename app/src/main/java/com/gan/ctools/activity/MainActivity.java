package com.gan.ctools.activity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.webkit.WebView;

import com.bumptech.glide.Glide;
import com.gan.ctools.other.MJavascriptInterface;
import com.gan.ctools.other.MyWebViewClient;

@SuppressLint("SetJavaScriptEnabled")
public class MainActivity extends AppCompatActivity {
    private WebView contentWebView = null;

    private String[] imageUrls = null;//HtmlUtil.returnImageUrlsFromHtml();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        contentWebView =new WebView(this);
        contentWebView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
            contentWebView.getSettings().setAppCacheEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR) {
            contentWebView.getSettings().setDatabaseEnabled(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR_MR1) {
            contentWebView.getSettings().setDomStorageEnabled(true);
        }
        contentWebView.loadUrl("http://www.gdqynews.com/guonaxinwen/20191111/375515.html");
        contentWebView.addJavascriptInterface(new MJavascriptInterface(this,imageUrls), "imagelistener");
        contentWebView.setWebViewClient(new MyWebViewClient());

    }

    @Override
    protected void onDestroy() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.get(MainActivity.this).clearDiskCache();//清理磁盘缓存需要在子线程中执行
            }
        }).start();
        Glide.get(this).clearMemory();//清理内存缓存可以在UI主线程中进行
        super.onDestroy();
    }
}
