package com.accountbook.tools;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * 工具类
 */
public class Util {

    /**
     * 判断网络可不可用
     *
     * @return true为可用
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        return info != null && info.isAvailable();
    }

    public static float sp2px(int sp, DisplayMetrics metrics) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, metrics);
    }

    public static final String numWeek2strWeek(int week) {
        switch (week) {
            case 0:
                return "周日";
            case 1:
                return "周一";
            case 2:
                return "周二";
            case 3:
                return "周三";
            case 4:
                return "周四";
            case 5:
                return "周五";
            case 6:
                return "周六";
            default:
                return "";
        }
    }
}
