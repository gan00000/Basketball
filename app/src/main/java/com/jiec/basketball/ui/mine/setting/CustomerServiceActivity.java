package com.jiec.basketball.ui.mine.setting;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import androidx.core.view.ViewCompat;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.jiec.basketball.R;
import com.jiec.basketball.base.BaseActivity;
import com.wangcj.common.utils.LogUtil;
import com.wangcj.common.utils.ToastUtil;

public class CustomerServiceActivity extends BaseActivity {

    private static final String TAG = "TopicActivity";

    private WebView mWebView;

    private WebSettings mWebSettings;

    private String mUrl;

    /**
     * 加载页面是否出错
     */
    private boolean mLoadPageError = false;

    /**
     * 错误页面是否正在显示
     */
    private boolean mShowingErrorTipPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_customer_service);

        mUrl = "https://www.facebook.com/basketballisall";

        initView();

        loadData();
    }

    @Override
    protected void onResume() {
        mWebView.loadUrl(mUrl);
        super.onResume();
    }


    private void initView() {
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.setHorizontalScrollBarEnabled(false);
        mWebView.setBackgroundColor(0);
        // 设置View.LAYER_TYPE_SOFTWARE，不过可能会导致页面的图片切换时会文字跳动，但不设置可能会影响webview的渲染，造成页面内存过大
        // 低配机型不支持代码设置
        // mWeb.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        // 2.3系统，有滚动条时，右侧会出现白边
        if (Build.VERSION.SDK_INT <= 10) {
            mWebView.setVerticalScrollBarEnabled(false);
        }

        mWebSettings = mWebView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);

        // 设置默认编码
        mWebSettings.setDefaultTextEncodingName("utf-8");
        // 设置自适应屏幕
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        // 设置屏幕单列显示，不可左右拖动(<--慎用，可能会导致网页css失效-->)
        // mSet.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);

        mWebSettings.setSupportZoom(false);
        mWebSettings.setBuiltInZoomControls(false);
        // 设置local storage
        mWebSettings.setDomStorageEnabled(true);

        // mSet.setRenderPriority(RenderPriority.HIGH);

//        try {
//            // 设置H5缓存可用
//            mWebSettings.setAppCacheEnabled(true);
//            String appCachePath = getApplication().getDir("cache", MODE_PRIVATE).getAbsolutePath();
//            mWebSettings.setAppCachePath(appCachePath);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            // 应用开启数据库缓存
            mWebSettings.setDatabaseEnabled(true);
            String appDatabasePath = getApplicationContext().getDir("database",
                    Context.MODE_PRIVATE).getAbsolutePath();
            mWebSettings.setDatabasePath(appDatabasePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // WebView先不要自动加载图片，等页面finish后再发起图片加载
        if (Build.VERSION.SDK_INT >= 19) {
            mWebSettings.setLoadsImagesAutomatically(true);
        } else {
            mWebSettings.setLoadsImagesAutomatically(false);
        }

        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        mWebView.setWebViewClient(new CustomWebViewClient());
        mWebView.setWebChromeClient(new CustomWebChromeClient());
        // 配置硬件加速
        ViewCompat.setLayerType(mWebView, ViewCompat.LAYER_TYPE_NONE, null);
        // LogUtil.d(TAG, "硬件加速：" + mWeb.isHardwareAccelerated());
    }

    private void loadData() {
        mLoadPageError = false;
        mShowingErrorTipPage = false;

        // 加载网页
        LogUtil.d(TAG, "url-->" + mUrl);
        if (TextUtils.isEmpty(mUrl)) {
            showLoadFailLayout();
            return;
        }

        mWebView.loadUrl(mUrl);

    }

    /**
     * 显示加载失败提示页面
     */
    @SuppressLint("NewApi")
    private void showLoadFailLayout() {
        if (!mShowingErrorTipPage) {
            ToastUtil.showMsg("加载页面失败");
            mShowingErrorTipPage = true;
        }
    }

    /**
     * 处理请求和响应
     */
    class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.d(TAG, "shouldOverrideUrlLoading()--->" + url);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description,
                                    String failingUrl) {
            LogUtil.d(TAG, "onReceivedError()---->" + errorCode);
            mLoadPageError = true;
        }

        @SuppressLint("NewApi")
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // LogUtil.d(TAG, "onPageStarted()---开始加载页面");
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            // LogUtil.d(TAG, "onPageFinished()---加载页面结束");
            if (!mWebSettings.getLoadsImagesAutomatically()) {
                mWebSettings.setLoadsImagesAutomatically(true);
            }

            if (mLoadPageError) {
                showLoadFailLayout();
            }
            super.onPageFinished(view, url);
        }

    }

    /**
     * 处理与JS的交互
     */
    class CustomWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            LogUtil.d(TAG, "onProgressChanged()--->" + newProgress);
        }

        // 常用出错提示语
        String[] errorTips = {
                "404", "NOT FOUND", "ERROR PAGE", "400", "403", "408", "500", "501", "502", "503"
        };

        @SuppressLint("DefaultLocale")
        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (!TextUtils.isEmpty(title)) {
                String upperTitle = title.toUpperCase();
                LogUtil.d(TAG, "onReceivedTitle()--->" + upperTitle);
                for (String error : errorTips) {
                    if (upperTitle.contains(error)) {
                        mLoadPageError = true;
                        showLoadFailLayout();
                        return;
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mWebView.stopLoading();
        mWebView.removeAllViews();
        mWebView.destroy();
        mWebView = null;
    }

}
