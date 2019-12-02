package com.gan.ctools.tool;

import android.content.Context;

public class ViewUtil {

    public static int getStatusBarHeight(Context context){
        int height = 0;
        int resourceId = context.getApplicationContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            height = context.getApplicationContext().getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }
}
