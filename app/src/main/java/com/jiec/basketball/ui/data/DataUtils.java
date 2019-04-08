package com.jiec.basketball.ui.data;

/**
 * Created by wangchuangjie on 2018/5/13.
 */

public class DataUtils {

    /**
     * 得分
     */
    public static final int TYPE_PTS = 0;
    /**
     * 篮板
     */
    public static final int TYPE_REB = 1;
    /**
     * 助攻
     */
    public static final int TYPE_AST = 2;
    /**
     * 抢断
     */
    public static final int TYPE_STL = 3;
    /**
     * 盖帽
     */
    public static final int TYPE_BLK = 4;
    /**
     * 失误
     */
    public static final int TYPE_TURNOVER = 5;

    public static String getTypeStr(int type) {
        switch (type) {
            case DataUtils.TYPE_PTS:
                return "pts";
            case DataUtils.TYPE_REB:
                return "reb";
            case DataUtils.TYPE_AST:
                return "ast";
            case DataUtils.TYPE_STL:
                return "stl";
            case DataUtils.TYPE_BLK:
                return "blk";
            case DataUtils.TYPE_TURNOVER:
                return "turnover";
        }

        return "pts";
    }
}
