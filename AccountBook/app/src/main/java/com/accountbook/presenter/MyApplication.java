package com.accountbook.presenter;

import android.app.Application;

import com.accountbook.entity.UserForLeanCloud;
import com.accountbook.tools.Util;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;

/**
 * Created by Grady on 2016.2.21.
 */
public class MyApplication extends Application{

    //LeanCloud的访问ID和KEY
    private String APP_ID = "uYUp3gVHVqHKWjxayqYEwJY9-gzGzoHsz";
    private String APP_KEY = "HICgky71weK7ucIgeBhO2Fri";


    @Override
    public void onCreate() {
        super.onCreate();

        //初始化LeanCloud
        AVUser.alwaysUseSubUserClass(UserForLeanCloud.class);       //使查询 AVUser 所得到的对象自动转化为用户子类化的对象User
        AVOSCloud.initialize(MyApplication.this, APP_ID, APP_KEY);

        //设置工具类的上下文
        Util.getInstance().setContext(MyApplication.this);
    }
}
