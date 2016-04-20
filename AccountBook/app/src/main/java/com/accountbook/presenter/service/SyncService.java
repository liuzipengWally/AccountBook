package com.accountbook.presenter.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.accountbook.biz.api.ISyncBiz;
import com.accountbook.biz.impl.SyncBiz;
import com.accountbook.tools.ConstantContainer;
import com.accountbook.tools.QuickSimpleIO;
import com.accountbook.tools.Util;
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
    public int onStartCommand(Intent intent, int flags, final int startId) {
        final ISyncBiz syncBiz = new SyncBiz(this);
        final QuickSimpleIO io = new QuickSimpleIO(this, "version_sp");
        if (Util.isNetworkAvailable(this)) {
            if (io.getBoolean("isFirstSync")) {
                syncBiz.syncVersion();
                syncBiz.setOnSyncVersionListener(new SyncBiz.OnSyncVersionListener() {
                    @Override
                    public void done() {
                        io.setBoolean("isFirstSync", false);
                        sync(syncBiz);
                    }
                });
            } else {
                sync(syncBiz);
            }

            syncBiz.setOnPostErrorListener(new SyncBiz.OnSyncErrorListener() {
                @Override
                public void error(String e) {
                    Log.e("error", e);
                    stopSelf(startId);
                }
            });
        } else {
            Toast.makeText(this, "请检查网络是否可用", Toast.LENGTH_SHORT).show();
        }

        stopSelf(startId);
        return START_STICKY;
    }

    private void sync(final ISyncBiz syncBiz) {
        syncBiz.syncRecord();
        syncBiz.setOnSyncRecordDoneListener(new SyncBiz.OnSyncRecordDoneListener() {
            @Override
            public void done() {
                syncBiz.syncBudget();
                syncBiz.setOnSyncBudgetDoneListener(new SyncBiz.OnSyncBudgetDoneListener() {
                    @Override
                    public void done() {
                        Intent intent = new Intent(ConstantContainer.SYNC_URI);
                        LocalBroadcastManager.getInstance(SyncService.this).sendBroadcast(intent);
                    }
                });
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("info", "post all service 销毁");
    }
}
