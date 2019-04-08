package com.jiec.basketball.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.blankj.utilcode.constant.TimeConstants;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.TimeUtils;
import com.jiec.basketball.core.ServerTimeManager;
import com.wangcj.common.utils.LogUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by chuangjiewang on 2016/10/17.
 */

public class AppUtil {

    /**
     * 解析获取视频播放地址
     * @param content
     * @return
     */
    public static String getVideoId(String content) {
        Document doc = Jsoup.parse(content);
        String videoUrl = "";
        if(content.indexOf("iframe") != -1){
            //iframe标签的直接获取src值
            Elements links = doc.select("iframe[src]");
            videoUrl = links.attr("src");
        }else {
            //twitter视频获取第三个超链接
            Element link = doc.select("a").get(2);//查找第2个a元素
            videoUrl = link.attr("href");
        }
        if(!EmptyUtils.emptyOfString(videoUrl)){
//            LogUtils.e("Jsoup解析视频地址："+videoUrl);
        }
        return videoUrl;



        //找到视频标签
     /*   int index = content.indexOf("https://www.youtube.com");
        if (index < 0) return null;
        content = content.substring(index);
        //找到视频结束位置
        index = content.indexOf("\"");
        if (index > content.indexOf("?") && content.indexOf("?") > 0) {
            index = content.indexOf("?");
        }
        if (index < 0) return null;
        content = content.substring(0, index);
        LogUtil.d("file content = " + content);
        return getId(content);*/
    }

    private static String getId(String url) {
        if (TextUtils.isEmpty(url)) return null;
        String[] strs = url.split("/");
        if (strs != null && strs.length > 0) {
            return strs[strs.length - 1];
        }
        return null;
    }

    public static int getVersionCode(Context context) {//获取版本号(内部识别号)
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return pi.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }


    public static String getVersionName(Context context) {
        try {
            PackageInfo pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return "v " + pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     *
     * @param timeStr 时间格式化字符串
     * @return
     */
    public static String getStandardDate(String timeStr) {
        StringBuffer sb = new StringBuffer();
        long serverTime = Timestamp.valueOf(timeStr).getTime();
        long offsetTime = System.currentTimeMillis() + ServerTimeManager.getServertimeOffset() - serverTime;
        long mill = (long) Math.ceil(offsetTime / 1000);//秒前
        long minute = (long) Math.ceil(offsetTime / 60 / 1000.0f);// 分钟前
        long hour = (long) Math.ceil(offsetTime / 60 / 60 / 1000.0f);// 小时
        long day = (long) Math.ceil(offsetTime / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0) {
            sb.append((day - 1) + "天");
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append((hour - 1) + "小時");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小時");
            } else {
                sb.append((minute - 1) + "分鐘");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分鐘");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("剛剛");
        }
        if (!sb.toString().equals("剛剛")) {
            sb.append("前");
        }

//        LogUtil.e("文章时间 = " + timeStr + "，距离现在 ：" + sb.toString()
//                + ", 服务器时间：" + serverTime + ",android时间：" + System.currentTimeMillis()
//                + ", 服务器差距：" + ServerTimeManager.getServertimeOffset());

        return sb.toString();
    }

    /**
     * 将时间戳转为代表"距现在多久之前"的字符串
     *評論時間轉化
     * @param timeStr 时间格式化字符串
     * @return
     */
    public static String getCommentTime(String timeStr) {
        StringBuffer sb = new StringBuffer();
        long serverTime = Timestamp.valueOf(timeStr).getTime();
        long offsetTime = System.currentTimeMillis() + ServerTimeManager.getServertimeOffset() - serverTime;
        long mill = (long) Math.ceil(offsetTime / 1000);//秒前
        long minute = (long) Math.ceil(offsetTime / 60 / 1000.0f);// 分钟前
        long hour = (long) Math.ceil(offsetTime / 60 / 60 / 1000.0f);// 小时
        long day = (long) Math.ceil(offsetTime / 24 / 60 / 60 / 1000.0f);// 天前

        if (day - 1 > 0) {
            return  TimeUtils.getString(timeStr, new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()), 0, TimeConstants.SEC);
        } else if (hour - 1 > 0) {
            if (hour >= 24) {
                sb.append("1天");
            } else {
                sb.append((hour - 1) + "小時");
            }
        } else if (minute - 1 > 0) {
            if (minute == 60) {
                sb.append("1小時");
            } else {
                sb.append((minute - 1) + "分鐘");
            }
        } else if (mill - 1 > 0) {
            if (mill == 60) {
                sb.append("1分鐘");
            } else {
                sb.append(mill + "秒");
            }
        } else {
            sb.append("剛剛");
        }
        if (!sb.toString().equals("剛剛")) {
            sb.append("前");
        }

        return sb.toString();
    }

    public static String name(String name) {
        if (!TextUtils.isEmpty(name) && name.contains(" ")) {
            String[] names = name.split(" ");
            return names[0].substring(0, 1) + "." + names[1];
        }
        return name;
    }

}
