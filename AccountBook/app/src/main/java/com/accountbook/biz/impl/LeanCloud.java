package com.accountbook.biz.impl;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.accountbook.entity.leancloud.User;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;


public class LeanCloud {

    private String APP_ID = "uYUp3gVHVqHKWjxayqYEwJY9-gzGzoHsz";
    private String APP_KEY = "HICgky71weK7ucIgeBhO2Fri";
    private Context context;


    private static final LeanCloud instance = new LeanCloud();

    private LeanCloud(){}

    public static LeanCloud getInstance(){
        return instance;
    }

    /**
     * 判断网络可不可用
     * @return true为可用
     */
    public boolean available(){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();
        if(info!=null&&info.isAvailable()){
            return true;
        }
        else{
            return false;
        }

    }

    /**
     * 初始化LeanCloud
     * @param context 上下文对象
     */
    public void init(Context context){
        AVUser.alwaysUseSubUserClass(User.class);       //使查询 AVUser 所得到的对象自动转化为用户子类化的对象User
        AVOSCloud.initialize(context, APP_ID, APP_KEY);
        this.context = context;
    }
}
