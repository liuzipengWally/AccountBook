package com.accountbook.tools;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import com.avos.avoscloud.AVException;

import java.util.Calendar;
import java.util.Locale;

/**
 * 工具类
 */
public class Util {


    /**
     * 翻译leanCloud异常
     * @param e 异常对象
     * @return 中文的错误信息
     */
    public static String getLocalizeLeanCloudError(AVException e) {
        switch (e.getCode()) {
            case AVException.USERNAME_PASSWORD_MISMATCH:
                return "用户名密码不匹配";
            case AVException.CONNECTION_FAILED:
                return "连接失败";
            case AVException.USER_DOESNOT_EXIST:
                return "用户不存在";
            case AVException.USERNAME_TAKEN:
                return "用户名已被占用";
            default:
                return e.getMessage();
        }
    }

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

    public static float dp2px(int dp, DisplayMetrics metrics) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public static String numWeek2strWeek(int week) {
        Calendar calendar = Calendar.getInstance();
        int currWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if (currWeek == week) {
            return "今";
        }

        switch (week) {
            case 1:
                return "日";
            case 2:
                return "一";
            case 3:
                return "二";
            case 4:
                return "三";
            case 5:
                return "四";
            case 6:
                return "五";
            case 7:
                return "六";
            default:
                return "";
        }
    }
}
