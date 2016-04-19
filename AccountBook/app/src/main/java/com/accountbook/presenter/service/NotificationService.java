package com.accountbook.presenter.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.accountbook.R;
import com.accountbook.view.activity.EditActivity;

public class NotificationService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreate() {
        Log.i("service启动", "启动");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_layout);
        Intent record = new Intent(this, EditActivity.class);
        //FLAG_UPDATE_CURRENT 用于区分通知，防止当requestCode相同时，通知被覆盖
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, record, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.recordBtn, pendingIntent);

        builder.setContent(remoteViews).setTicker("快速记录").setSmallIcon(R.mipmap.ic_create_white_24dp).setPriority(Notification.PRIORITY_DEFAULT);
        Notification notification = builder.build();
        notification.flags = Notification.FLAG_NO_CLEAR;

        NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
        return START_STICKY;
    }
}
