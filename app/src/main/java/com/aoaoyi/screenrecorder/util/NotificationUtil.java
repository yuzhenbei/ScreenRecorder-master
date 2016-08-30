package com.aoaoyi.screenrecorder.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.aoaoyi.screenrecorder.ui.service.MyService;
import com.aoaoyi.screenrecorder.R;

/**
 * Created by yuzhenbei on 2016/5/24.
 */
public class NotificationUtil {


    public static void openNotification1(MyService mMyService){
        if (null != mMyService){
            mMyService.startForeground(101, NotificationUtil.getNotification(mMyService));
            Log.e("openNotification1", "ok");
            //mMyService.getNotificationManager().notify(101, NotificationUtil.getNotification(mMyService));
        }else {
            Log.e("openNotification1", "error");
        }
    }

    public static void stopNotification1(MyService mMyService){
        if (null != mMyService){
            mMyService.getNotificationManager().cancel(101);
            //mMyService.stopForeground(true);
        }
    }

    public static void openNotification2(MyService mMyService){
        if (null != mMyService){
            mMyService.startForeground(102, NotificationUtil.getNotification2(mMyService));
        }
    }

    public static void stopNotification2(MyService mMyService){
        if (null != mMyService){
            mMyService.getNotificationManager().cancel(102);
            //mMyService.stopForeground(true);
        }
    }

    public static Notification getNotification(Context pContext){
        return new NotificationCompat.Builder(pContext)
                .setContentTitle("这是通知标题1------")
                .setContentText("这是通知内容1------")
                .setWhen(System.currentTimeMillis())
                .setContentIntent(PendingIntent.getBroadcast(pContext, 0, new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS), PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.drawable.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(pContext.getResources(), R.drawable.ic_launcher))
                .setColor(Color.parseColor("#ce3f31"))
                .build();
    }

    public static Notification getNotification2(Context pContext){
        return new NotificationCompat.Builder(pContext)
                .setContentTitle("这是通知标题2******")
                .setContentText("这是通知内容2*******")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(pContext.getResources(), R.drawable.ic_launcher))
                .setColor(Color.parseColor("#ce3f31"))
                .build();
    }

    public static Notification getNotification3(Context pContext){
        return new NotificationCompat.Builder(pContext)
                .setContentTitle("这是通知标题3******")
                .setContentText("这是通知内容3*******")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_logo)
                .setLargeIcon(BitmapFactory.decodeResource(pContext.getResources(), R.drawable.ic_launcher))
                .setColor(Color.parseColor("#ce3f31"))
                .build();
    }
}
