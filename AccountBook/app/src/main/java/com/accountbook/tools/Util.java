package com.accountbook.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 工具类
 */
public class Util {

    /**
     * 判断网络可不可用
     * @return true为可用
     */
    public static boolean isNetworkAvailable(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if(info!=null&&info.isAvailable()){
            return true;
        }
        else{
            return false;
        }
    }

}
