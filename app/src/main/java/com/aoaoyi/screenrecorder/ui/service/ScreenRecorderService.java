package com.aoaoyi.screenrecorder.ui.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ScreenRecorderService extends Service {
    public ScreenRecorderService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
