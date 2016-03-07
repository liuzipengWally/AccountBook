package com.accountbook.application;

import android.app.Application;

import com.accountbook.biz.impl.SQLite;
import com.accountbook.entity.User;
import com.accountbook.entity.UserForLeanCloud;
import com.accountbook.tools.QuickSimpleIO;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVUser;


public class MyApplication extends Application {
    //LeanCloud的访问ID和KEY
    private final String APP_ID = "uYUp3gVHVqHKWjxayqYEwJY9-gzGzoHsz";
    private final String APP_KEY = "HICgky71weK7ucIgeBhO2Fri";

    private SQLite mSqLite;

    //全局User对象，好像没什么用，暂留
    private User user;

    @Override
    public void onCreate() {
        super.onCreate();

        init();
    }

    private void init() {
        //初始化LeanCloud
        AVUser.alwaysUseSubUserClass(UserForLeanCloud.class);       //使查询 AVUser 所得到的对象自动转化为用户子类化的对象User
        AVOSCloud.initialize(MyApplication.this, APP_ID, APP_KEY);

        //初始化数据库
        mSqLite = SQLite.getInstance();
        mSqLite.createDataBase("accountBook.db", this);

        QuickSimpleIO io = new QuickSimpleIO(this, "isFirstRun_sp");
        if (io.getBoolean("isFirstRun")) {
            initTable();
            io.setBoolean("isFirstRun", false);
        }
    }

    private void initTable() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mSqLite.createUserTable();
                mSqLite.createRoleTable();

                mSqLite.initDefaultRole();
            }
        }).start();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
