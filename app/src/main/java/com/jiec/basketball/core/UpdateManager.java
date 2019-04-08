package com.jiec.basketball.core;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.view.View;
import android.webkit.MimeTypeMap;

import com.jiec.basketball.R;
import com.jiec.basketball.ui.dialog.UpdateDialog;
import com.jiec.basketball.utils.AppUtil;
import com.wangcj.common.utils.LogUtil;
import com.wangcj.common.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * Created by Administrator on 2017/1/18.
 */

public class UpdateManager {

    private Activity mContext;

    private long mDownloadId;

    private DownloadManager mDownloadManager;

    public UpdateManager(Activity context) {
        mContext = context;
    }

    public void checkUpdate() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String url_path = "http://www.ballgametime.com/android_version.json";
                try {
                    final JSONObject updateJson = new JSONObject(readServerFile(url_path));
                    LogUtil.d("update info = " + updateJson.toString());

                    if (updateJson.optInt("versionCode") > AppUtil.getVersionCode(mContext)) {

                        new Handler(mContext.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                final UpdateDialog dialog = new UpdateDialog(mContext);
                                dialog.setTitle(mContext.getString(R.string.update_title));
                                dialog.setMessage(mContext.getString(R.string.update));
                                dialog.setUPdateListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        downloadFile(updateJson.optString("url"));
                                        dialog.dismiss();
                                    }
                                });
                                dialog.show();
                            }
                        });


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    /**
     * 读取文件
     *
     * @param sourcePath 文件所在的网络路径
     */
    private String readServerFile(String sourcePath) {
        StringBuffer sb = new StringBuffer();
        String line;
        BufferedReader reader = null;
        try {
            URL url = new URL(sourcePath);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception ie) {
            ie.printStackTrace();
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    private void downloadFile(String url) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        //设置文件类型，可以在下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(url));
        request.setMimeType(mimeString);

        //下载完成安装APK
        String downloadPath = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "hater.apk";
        deleteFile(new File(downloadPath));
        //指定下载路径和下载文件名
        request.setDestinationInExternalPublicDir("/download/", "hater.apk");
        //获取下载管理器
        mDownloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //将下载任务加入下载队列，否则不会进行下载
        mDownloadId = mDownloadManager.enqueue(request);

        //注册广播接收者，监听下载状态
        mContext.registerReceiver(receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    //广播接受者，接收下载状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.d("ACTION_DOWNLOAD_COMPLETE");
            checkDownloadStatus();//检查下载状态
        }
    };

    //检查下载状态
    private void checkDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(mDownloadId);//筛选下载任务，传入任务ID，可变参数
        Cursor c = mDownloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {

                case DownloadManager.STATUS_SUCCESSFUL:
                    LogUtil.d(">>>下载完成");
                    //下载完成安装APK
                    String downloadPath = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + "hater.apk";
                    installAPK(new File(downloadPath));
                    break;
                case DownloadManager.STATUS_FAILED:
                    LogUtil.d(">>>下载失败");
                    ToastUtil.showMsg("下載失敗，請檢查網絡");
                    break;
            }
        }
    }

    //下载到本地后执行安装
    protected void installAPK(File file) {
        if (!file.exists()) return;
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("file://" + file.toString());
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        //在服务中开启activity必须设置flag,后面解释
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    private void deleteFile(File file) {
        if (file != null && file.exists()) {
            file.delete();
        }
    }

}
