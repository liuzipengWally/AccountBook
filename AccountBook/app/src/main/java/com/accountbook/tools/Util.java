package com.accountbook.tools;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 工具类
 */
public class Util {

    private Context context;        //上下文

    private static final Util instance = new Util();

    private Util(){}

    public static Util getInstance(){
        return instance;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    /**
     * 判断网络可不可用
     * @return true为可用
     */
    public boolean isNetworkAvailable(){
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
