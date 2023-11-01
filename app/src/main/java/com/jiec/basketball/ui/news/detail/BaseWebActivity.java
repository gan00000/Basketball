package com.jiec.basketball.ui.news.detail;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import androidx.core.view.ViewCompat;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.gan.ctools.other.MJavascriptInterface;
import com.jiec.basketball.base.BaseActivity;
import com.wangcj.common.utils.LogUtil;
import com.wangcj.common.utils.ToastUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Jiec on 2019/1/12.
 */
public class BaseWebActivity extends BaseActivity {

    private static final String TAG = "BaseWebActivity";

    WebView mWebView;

    WebSettings mWebSettings;

    /**
     * 加载页面是否出错
     */
    private boolean mLoadPageError = false;

    public void initWebView() {
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

    /**
     * 显示加载失败提示页面
     */
    private void showLoadFailLayout() {
        ToastUtil.showMsg("加载页面失败");
    }

    protected void parseHtml(final String urlpath) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    showLoading();
                    URL url = new URL(urlpath);
                    URLConnection URLconnection = url.openConnection();
                    HttpURLConnection httpConnection = (HttpURLConnection) URLconnection;
                    httpConnection.setRequestProperty("Accept-Charset", "UTF-8");
                    int responseCode = httpConnection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        InputStream in = httpConnection.getInputStream();
                        InputStreamReader isr = new InputStreamReader(in, "UTF-8");
                        BufferedReader bufr = new BufferedReader(isr);
                        String content = "";
                        String temp;
                        while ((temp = bufr.readLine()) != null) {
                            content += temp;
                        }
                        bufr.close();
                        content = processData(content);

                        final String finalContent = content;
//                        LogUtils.e("HTML： " + finalContent);
//                        String sdCardDir = Environment.getExternalStorageDirectory().getAbsolutePath();
//                        File saveFile = new File(sdCardDir, "news.txt");
//                        FileOutputStream outStream = new FileOutputStream(saveFile);
//                        outStream.write(finalContent.getBytes());
//                        outStream.close();

                        loadData(urlpath, finalContent);
                    } else {
                        ToastUtil.showMsg("失败");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    protected void loadData(final String url, final String finalContent) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideLoading();
                if (mWebView != null){

//                    String[] imageUrls = HtmlUtil.returnImageUrlsFromHtml(finalContent);
                    mWebView.addJavascriptInterface(new MJavascriptInterface(BaseWebActivity.this,null), "imagelistener");
                    mWebView.loadDataWithBaseURL(url, finalContent, "text/html", "UTF-8", null);
                }
            }
        });
    }

    /**
     * html代码重构
     *
     * @param content
     * @return
     */
    protected String processData(String content) {
        LogUtil.d("start parse :" + System.currentTimeMillis());
        content = content.replaceAll("class=\"onelist\"", " style=\" display: none\"");
        content = content.replace("class=\"next-prev-posts clearfix\"", "  style=\" display: none\"");
        content = content.replace("id=\"footer\"", "  style=\" display: none\"");
        content = content.replace("id=\"header\"", "  style=\" display: none\"");
        content = content.replace("class=\"sidebar sidebar-right\"", "  style=\" display: none\"");
        content = content.replaceAll("//pagead2.googlesyndication.com/pagead/js/adsbygoogle.js", "#");
        content = content.replaceAll("class=\"heateorSssSharingRound\"", "style=\" display: none\"");
        content = content.replaceAll("class=\"adsbygoogle\"", "style=\" display: none\"");
        content = content.replace("class=\"to-top\"", "style=\" display: none\"");
        content = content.replace("class=\"post_icon\"", "style=\" display: none\"");
        content = content.replace("class=\"post-content\"", "class=\"post-content app-hidden-ads\"");
        content = content.replace("class=\"post-entry-categories\"", "style=\" display: none\"");
        content = content.replace("class=\"post-title\"", "style=\" display: none\"");
        content = content.replace("id=\"recommendedrPosts\"", "id=\"recommendedrPostsApps\"");
        content = content.replace("plugins/wp-polls", "#");
        content = content.replace("plugins/popups", "#");
        content = content.replace("plugins/adrotate", "#");
        content = content.replace("content-cjtz-mini", "app_ad_hidden");
        LogUtil.d("end parse" + System.currentTimeMillis());
        LogUtil.d(content);
        return content;
    }

    /**
     * 处理请求和响应
     */
    class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            LogUtil.d(TAG, "shouldOverrideUrlLoading()--->" + url);
            parseHtml((String) url);
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
            LogUtil.d(TAG, "onPageStarted()---开始加载页面");
            view.getSettings().setJavaScriptEnabled(true);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            LogUtil.d(TAG, "onPageFinished()---加载页面结束");
            view.getSettings().setJavaScriptEnabled(true);
            if (!mWebSettings.getLoadsImagesAutomatically()) {
                mWebSettings.setLoadsImagesAutomatically(true);
            }

            if (mLoadPageError) {
                showLoadFailLayout();
            }
            super.onPageFinished(view, url);
            addImageClickListener(view);
        }


        private void addImageClickListener(WebView webView) {
            webView.loadUrl("javascript:(function(){" +
                    "var objs = document.getElementsByTagName(\"img\"); " +
                    "for(var i=0;i<objs.length;i++)  " +
                    "{"
                    + "    objs[i].onclick=function()  " +
                    "    {  "
                    + "        window.imagelistener.openImage(this.src);  " +//通过js代码找到标签为img的代码块，设置点击的监听方法与本地的openImage方法进行连接
                    "    }  " +
                    "}" +
                    "})()");
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
