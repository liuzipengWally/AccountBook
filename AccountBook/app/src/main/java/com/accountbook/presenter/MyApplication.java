package com.accountbook.presenter;

import android.app.Application;

import com.accountbook.biz.impl.SQLite;
import com.accountbook.entity.User;
import com.accountbook.entity.UserForLeanCloud;
import com.accountbook.tools.Util;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;


public class MyApplication extends Application{

    //LeanCloud的访问ID和KEY
    private String APP_ID = "uYUp3gVHVqHKWjxayqYEwJY9-gzGzoHsz";
    private String APP_KEY = "HICgky71weK7ucIgeBhO2Fri";

    //全局User对象
    private User user;


    @Override
    public void onCreate() {
        super.onCreate();

        //初始化LeanCloud
        AVUser.alwaysUseSubUserClass(UserForLeanCloud.class);       //使查询 AVUser 所得到的对象自动转化为用户子类化的对象User
        AVOSCloud.initialize(MyApplication.this, APP_ID, APP_KEY);

        //初始化数据库
        SQLite.initLocalDatabase(MyApplication.this);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}
