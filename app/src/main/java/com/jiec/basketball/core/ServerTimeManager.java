package com.jiec.basketball.core;

import android.provider.Settings;

/**
 * Created by jiec on 2017/3/5.
 */

public class ServerTimeManager {

    private static long servertimeOffset;

    public static long getServertimeOffset() {
        return servertimeOffset;
    }

    public static void setServertimeOffset(long serverTime) {
        ServerTimeManager.servertimeOffset =
                serverTime - System.currentTimeMillis();
    }

}
