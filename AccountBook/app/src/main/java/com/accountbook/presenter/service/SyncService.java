package com.accountbook.presenter.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.accountbook.biz.api.ISyncBiz;
import com.accountbook.biz.impl.SyncBiz;
import com.accountbook.tools.QuickSimpleIO;
import com.avos.avoscloud.AVException;

public class SyncService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        final ISyncBiz postAllBiz = new SyncBiz(this);
        final QuickSimpleIO io = new QuickSimpleIO(this, "version_sp");
        if (io.getBoolean("isFirstSync")) {
            postAllBiz.syncVersion();
            postAllBiz.setOnSyncVersionListener(new SyncBiz.OnSyncVersionListener() {
                @Override
                public void done() {
                    io.setBoolean("isFirstSync", false);
                    postAllBiz.syncRecord();
                    postAllBiz.syncBudget();
                }
            });
        } else {
            postAllBiz.syncRecord();
            postAllBiz.syncBudget();
            Log.i("info", "version同步完毕");
        }

        postAllBiz.setOnPostErrorListener(new SyncBiz.OnSyncErrorListener() {
            @Override
            public void error(String e) {
                Log.e("error", e);
            }
        });

        stopSelf(startId);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("info", "post all service 销毁");
    }
}
