package com.aoaoyi.screenrecorder.ui.binder;

import android.os.Binder;

import com.aoaoyi.screenrecorder.ui.service.MyService;
import com.aoaoyi.screenrecorder.util.NotificationUtil;

/**
 * Created by yuzhenbei on 2016/5/24.
 */
public class MyBinder extends Binder {

    private MyService mMyService;

    public MyBinder(MyService pMyService){
        super();
        mMyService = pMyService;
    }

    public void openNotification1(){
        NotificationUtil.openNotification1(mMyService);
    }

    public void stopNotification1(){
        NotificationUtil.stopNotification1(mMyService);
    }

    public void openNotification2(){
        NotificationUtil.openNotification2(mMyService);
    }

    public void stopNotification2(){
        NotificationUtil.stopNotification2(mMyService);
    }


    public void stopService(){
        if (null != mMyService){
            mMyService.getNotificationManager().cancel(101);
            mMyService.getNotificationManager().cancel(102);
            mMyService.stopForeground(true);
            mMyService.stopSelf();
        }
    }

}
