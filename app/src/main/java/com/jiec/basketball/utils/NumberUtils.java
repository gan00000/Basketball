package com.jiec.basketball.utils;

import java.math.BigDecimal;

/**
 * Created by jiec on 2017/6/12.
 */

public class NumberUtils {

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }

    /**
     * 格式化百分比
     * 保持两位小数点，如果尾数是0则舍掉
     *
     * @param rate
     * @return
     */
    public static String formatRate(float rate) {
        return formatAmount(String.valueOf(rate), BigDecimal.ROUND_HALF_UP, 1);
    }

    public static String formatRate(double rate) {
        return formatAmount(String.valueOf(rate), BigDecimal.ROUND_HALF_UP, 1);
    }


    /**
     * 格式化金额,每三位数加一个逗号
     *
     * @param arg
     * @param mode
     * @param quality 精度（保留小数点位数）
     * @return
     */
    public static String formatAmount(String arg, int mode, int quality) {
        BigDecimal bd = new BigDecimal(arg);
        double fAmount = bd.setScale(quality, mode).doubleValue();
        String format = String.format("%1$,f", fAmount);
        String formatAmount = format.substring(0, format.lastIndexOf(".") + quality + 1);

        return formatAmount;
    }

    /**
     * 获取保存两位小数点的字符串，每三位添加逗号
     * 用于金额的显示四舍五进
     *
     * @param arg
     * @return
     */
    public static String formatAmount(float arg) {
        return formatAmount(String.valueOf(arg), BigDecimal.ROUND_HALF_UP, 1);
    }

    public static String formatAmount(double arg) {
        return formatAmount(String.valueOf(arg), BigDecimal.ROUND_HALF_UP, 1);
    }

    public static String formatAmount(String arg) {
        return formatAmount(arg, BigDecimal.ROUND_HALF_UP, 1);
    }

    /**
     * 格式化金额，去掉尾数0
     *
     * @param arg
     * @return
     */
    public static String formatAmountSubZero(float arg) {
        return subZeroAndDot(formatAmount(arg));
    }

    public static String formatAmountSubZero(double arg) {
        return subZeroAndDot(formatAmount(arg));
    }

    /**
     * 获取保存两位小数点的字符串，每三位添加逗号
     * 用于金额的显示收尾
     *
     * @param arg
     * @return
     */
    public static String formatAmount4Abandon(float arg) {
        return formatAmount(String.valueOf(arg), BigDecimal.ROUND_DOWN, 2);
    }

    public static String formatAmount4Abandon(double arg) {
        return formatAmount(String.valueOf(arg), BigDecimal.ROUND_DOWN, 2);
    }

    /**
     * 返回数量，每三位一个逗号
     *
     * @param count
     * @return
     */
    public static String formatCount(int count) {
        return String.format("%1$,d", count);
    }


    /**
     * 抹掉小数2位之后数值
     *
     * @param count
     * @return
     */
    public static String formatAmountWithUnit(double count) {
        if (count < 10000) {
            return formatAmountSubZero(count);
        }

        String unit = "";
        if (count >= 100000000) {
            count = count / 100000000;
            unit = "亿";
        } else if (count >= 10000) {
            count = count / 10000;
            unit = "万";
        }

        return subZeroAndDot(formatAmount4Abandon(count)) + unit;
    }
}
