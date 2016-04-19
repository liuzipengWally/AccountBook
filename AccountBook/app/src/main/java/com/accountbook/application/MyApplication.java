package com.accountbook.application;

import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteException;

import com.accountbook.biz.impl.SQLite;
import com.accountbook.entity.cloud.BudgetForCloud;
import com.accountbook.entity.cloud.RecordForCloud;
import com.accountbook.entity.cloud.Version;
import com.accountbook.presenter.service.NotificationService;
import com.accountbook.presenter.service.SyncService;
import com.accountbook.tools.QuickSimpleIO;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVObject;


public class MyApplication extends Application {
    //LeanCloud的访问ID和KEY
    private final String APP_ID = "tB1Qtl5NQSv88ij4p4lwr3sN-gzGzoHsz";
    private final String APP_KEY = "dSa0544nB9mTsB32z1Hiz2EE";

    private SQLite mSqLite;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
        showNotification();
    }

    private void showNotification() {
        startService(new Intent(this, NotificationService.class));
    }

    private void init() {
        //注册子leanCloud子类化
        AVObject.registerSubclass(Version.class);
        AVObject.registerSubclass(RecordForCloud.class);
        AVObject.registerSubclass(BudgetForCloud.class);

        //初始化LeanCloud
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
        try {
            mSqLite.createUserTable();
            mSqLite.createRoleTable();
            mSqLite.createClassifyTable();
            mSqLite.createRecordTable();
            mSqLite.createBudgetTable();

            mSqLite.initDefaultRole();
            mSqLite.initDefaultClassify();
        } catch (SQLiteException e) {
            e.printStackTrace();
        }

        initTableVersion();
    }

    private void initTableVersion() {
        QuickSimpleIO io = new QuickSimpleIO(this, "version_sp");
        io.setInt("recordVer", 0);
        io.setInt("budgetVer", 0);
        io.setInt("roleVer", 0);
        io.setInt("classifyVer", 0);
        io.setBoolean("needSync", true);
    }

}
