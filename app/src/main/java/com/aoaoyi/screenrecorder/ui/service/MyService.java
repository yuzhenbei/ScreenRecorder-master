package com.aoaoyi.screenrecorder.ui.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.aoaoyi.screenrecorder.ui.binder.MyBinder;
import com.aoaoyi.screenrecorder.widget.drawview.DrawableViewFloating;

public class MyService extends Service {

    private MyBinder mMyBinder;
    private NotificationManager mNotificationManager;

    private DrawableViewFloating mDrawableViewFloating;

    public MyService() {
        mMyBinder = new MyBinder(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mMyBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        mDrawableViewFloating = new DrawableViewFloating(this);
        //startForeground(103, getNotification3());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    public NotificationManager getNotificationManager() {
        return mNotificationManager;
    }
}
