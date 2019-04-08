package com.jiec.basketball.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by wangchuangjie on 2018/5/17.
 */

public class TimeUtil {


    /**
     * 获取今天的日期
     *
     * @return
     */
    public static String getToday() {
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String result = format.format(today);
        Log.e(null, result);
        return result;
    }

    /**
     * 获取过去第几天的日期
     *
     * @param past
     * @return
     */
    public static String getPastDate(String day, int past) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(day));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);
        Date today = calendar.getTime();
        String result = format.format(today);

        Log.e(null, result);
        return result;
    }

    /**
     * 获取未来 第 past 天的日期
     *
     * @param day  指定日期
     * @param past
     * @return
     */
    public static String getFetureDate(String day, int past) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = Calendar.getInstance();
        try {
            calendar.setTime(format.parse(day));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + past);
        Date today = calendar.getTime();
        String result = format.format(today);
        Log.e(null, result);
        return result;
    }
}
