package com.aoaoyi.screenrecorder.ui;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.MediaRecorder;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.provider.DocumentFile;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.aoaoyi.screenrecorder.ui.binder.MyBinder;
import com.aoaoyi.screenrecorder.ui.service.MyService;
import com.aoaoyi.screenrecorder.R;
import com.aoaoyi.screenrecorder.widget.VideoRangeSeekBar;
import com.aoaoyi.screenrecorder.widget.VideoSeekBarView;
import com.aoaoyi.screenrecorder.widget.VideoTimelineView;

import java.io.IOException;
import java.lang.reflect.Method;

/**
 * Created by yuzhenbei on 2016/3/1.
 */
public class MainActivity1 extends Activity implements ServiceConnection {

    private static final String TAG = "MainActivity1";
    private static final int REQUEST_CODE = 1000;
    private int mScreenDensity;
    private MediaProjectionManager mProjectionManager;
    private static final int DISPLAY_WIDTH = 720;
    private static final int DISPLAY_HEIGHT = 1280;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private MediaProjectionCallback mMediaProjectionCallback;
    private ToggleButton mToggleButton;
    private MediaRecorder mMediaRecorder;
    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    private MyBinder mMyBinder;
    private Button mStart1;
    private Button mStop1;
    private Button mStart2;
    private Button mStop2;

    private VideoSeekBarView mVideoSeekBarView;
    private VideoTimelineView mVideoTimelineView;
    private VideoRangeSeekBar mVideoRangeSeekBar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;

        mMediaRecorder = new MediaRecorder();

        mProjectionManager = (MediaProjectionManager) getSystemService
                (Context.MEDIA_PROJECTION_SERVICE);

        mToggleButton = (ToggleButton) findViewById(R.id.toggle);
        mToggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onToggleScreenShare(v);
                /*Intent _intent = new Intent(Intent.ACTION_MAIN);
                _intent.setClassName("android", "com.android.internal.app.PlatLogoActivity");
                startActivity(_intent);*/

                /*Intent intent = new Intent("com.android.internal.app.PLATLOGOACTIVITY");
                startActivity(intent);*/

            }
        });
        mStart1 = (Button)findViewById(R.id.button1);
        mStart1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBinder.openNotification1();
            }
        });
        mStop1 = (Button)findViewById(R.id.button2);
        mStop1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBinder.stopNotification1();
            }
        });
        mStart2 = (Button)findViewById(R.id.button3);
        mStart2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBinder.openNotification2();
            }
        });
        mStop2 = (Button)findViewById(R.id.button4);
        mStop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMyBinder.stopNotification2();
            }
        });

        //

        mVideoSeekBarView = (VideoSeekBarView)findViewById(R.id.video_seekbar);
        mVideoSeekBarView.delegate = new VideoSeekBarView.SeekBarDelegate(){
            @Override
            public void onSeekBarDrag(float progress) {
                if (progress < mVideoTimelineView.getLeftProgress()) {
                    progress = mVideoTimelineView.getLeftProgress();
                    mVideoSeekBarView.setProgress(progress);
                } else if (progress > mVideoTimelineView.getRightProgress()) {
                    progress = mVideoTimelineView.getRightProgress();
                    mVideoSeekBarView.setProgress(progress);
                }
            }
        };
        mVideoTimelineView = (VideoTimelineView)findViewById(R.id.video_timeline_view);
        mVideoTimelineView.setVideoPath(Environment
                .getExternalStoragePublicDirectory(Environment
                        .DIRECTORY_DOWNLOADS) + "/video_11.mp4");
        mVideoTimelineView.setDelegate(new VideoTimelineView.VideoTimelineViewDelegate(){
            @Override
            public void onLeftProgressChanged(float progress) {

                mVideoSeekBarView.setProgress(mVideoTimelineView.getLeftProgress());
            }

            @Override
            public void onRifhtProgressChanged(float progress) {

                mVideoSeekBarView.setProgress(mVideoTimelineView.getLeftProgress());
            }
        });

        mVideoRangeSeekBar = (VideoRangeSeekBar)findViewById(R.id.video_range_seek_bar);
        mVideoRangeSeekBar.setVideoPath(Environment
                .getExternalStoragePublicDirectory(Environment
                        .DIRECTORY_DOWNLOADS) + "/video_11.mp4");

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode != REQUEST_CODE) {
            Log.e(TAG, "Unknown request code: " + requestCode);
            return;
        }
        if (resultCode != RESULT_OK) {
            Toast.makeText(this,
                    "Screen Cast Permission Denied", Toast.LENGTH_SHORT).show();
            mToggleButton.setChecked(false);
            return;
        }
        mMediaProjectionCallback = new MediaProjectionCallback();
        mMediaProjection = mProjectionManager.getMediaProjection(resultCode, data);
        mMediaProjection.registerCallback(mMediaProjectionCallback, null);
        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();
        mMediaRecorder.getMaxAmplitude();
        int _MaxAmplitude = mMediaRecorder.getMaxAmplitude();
        Log.v(TAG, "MaxAmplitude::" + _MaxAmplitude);
        Log.v(TAG, "Permission:1:" + ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO));
        Log.v(TAG, "Permission:2:" + PermissionChecker.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO));
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED){
            Log.v(TAG, "Permission::" + false);
        }else {
            Log.v(TAG, "Permission::" + true);

        }

        Log.v(TAG, "CheckOp::" + checkOp(this, 27));

        DocumentFile _DocumentFile = DocumentFile.fromFile(null);


    }


    public void onToggleScreenShare(View view) {
        if (((ToggleButton) view).isChecked()) {
            initRecorder();
            shareScreen();
            startService();
        } else {
            mMediaRecorder.stop();
            mMediaRecorder.reset();
            Log.v(TAG, "Stopping Recording");
            stopScreenSharing();
            stopService();
        }
    }

    private void shareScreen() {
        if (mMediaProjection == null) {
            startActivityForResult(mProjectionManager.createScreenCaptureIntent(), REQUEST_CODE);
            return;
        }
        mVirtualDisplay = createVirtualDisplay();
        mMediaRecorder.start();
    }

    private VirtualDisplay createVirtualDisplay() {
        return mMediaProjection.createVirtualDisplay("MainActivity",
                DISPLAY_WIDTH, DISPLAY_HEIGHT, mScreenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_PUBLIC,
                mMediaRecorder.getSurface(), null /*Callbacks*/, null
                /*Handler*/);
    }

    private void initRecorder() {
        try {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setOutputFile(Environment
                    .getExternalStoragePublicDirectory(Environment
                            .DIRECTORY_DOWNLOADS) + "/video_11.mp4");
            mMediaRecorder.setVideoSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setVideoEncodingBitRate(5 * 1024 * 1000);
            mMediaRecorder.setVideoFrameRate(30);
            int rotation = getWindowManager().getDefaultDisplay().getRotation();
            int orientation = ORIENTATIONS.get(rotation + 90);
            mMediaRecorder.setOrientationHint(orientation);
            mMediaRecorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mMyBinder = (MyBinder) service;
        if (null != mMyBinder){
            Log.e("ServiceConnected::", "OK");
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    private class MediaProjectionCallback extends MediaProjection.Callback {
        @Override
        public void onStop() {
            if (mToggleButton.isChecked()) {
                mToggleButton.setChecked(false);
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                Log.v(TAG, "Recording Stopped");
            }
            mMediaProjection = null;
            stopScreenSharing();
        }
    }

    private void stopScreenSharing() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        //mMediaRecorder.release(); //If used: mMediaRecorder object cannot
        // be reused again
        destroyMediaProjection();
    }

    @Override
    protected void onPause() {
        stopService();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        destroyMediaProjection();
        if (mVideoTimelineView != null) {
            mVideoTimelineView.destroy();
        }
        super.onDestroy();
    }

    private void destroyMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.unregisterCallback(mMediaProjectionCallback);
            mMediaProjection.stop();
            mMediaProjection = null;
        }
        Log.i(TAG, "MediaProjection Stopped");
    }


    private static int checkOp(Context context, int op){
        final int version = Build.VERSION.SDK_INT;
        if (version >= 19){
            //AppOpsManager
            Object object = context.getSystemService(Context.APP_OPS_SERVICE);
            Class c = object.getClass();
            try {
                Class[] cArg = new Class[3];
                cArg[0] = int.class;
                cArg[1] = int.class;
                cArg[2] = String.class;
                Method lMethod = c.getDeclaredMethod("checkOp", cArg);
                return (Integer) lMethod.invoke(object, op, Binder.getCallingUid(), context.getPackageName());
            } catch(NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    private void startService(){
        Intent _Intent = new Intent(this, MyService.class);
        startService(_Intent);
        bindService(_Intent, this, BIND_AUTO_CREATE);
    }

    private void stopService(){
        if (null != mMyBinder){
            mMyBinder.stopService();
        }
    }

}
