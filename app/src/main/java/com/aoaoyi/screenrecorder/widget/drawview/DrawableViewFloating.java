package com.aoaoyi.screenrecorder.widget.drawview;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;

import com.aoaoyi.screenrecorder.R;

/**
 * Created by yuzhenbei on 2016/8/8.
 */
public class DrawableViewFloating extends FrameLayout implements View.OnClickListener{

    private FrameLayout mFrameLayout;
    private LayoutInflater mLayoutInflater;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWMParams;
    private DrawableViewConfig mDrawableViewConfig;
    private DrawableView mDrawableView;
    private View mToolsView;
    private Button mUndo;
    private Button mClose;

    public DrawableViewFloating(Context context) {
        this(context, null);
    }

    public DrawableViewFloating(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableViewFloating(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DrawableViewFloating(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.floating_undo:
                mDrawableView.undo();
                break;
            case R.id.floating_close:
                if (null != mFrameLayout){
                    mWindowManager.removeView(mFrameLayout);
                }
                break;
        }

    }

    private void init(){
        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mWMParams = getParams(new WindowManager.LayoutParams());
        mWindowManager.addView(initView(), mWMParams);
    }

    private WindowManager.LayoutParams getParams(WindowManager.LayoutParams pWMParams){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            pWMParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else {
            pWMParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        pWMParams.format = PixelFormat.RGBA_8888;
        pWMParams.flags =  WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE| WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN| WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR|
                WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
        pWMParams.gravity = Gravity.LEFT| Gravity.TOP;
        pWMParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        pWMParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        return pWMParams;
    }

    private View initView(){
        mLayoutInflater = LayoutInflater.from(getContext());
        mFrameLayout = (FrameLayout)mLayoutInflater.inflate(R.layout.floating_drawable_view, null);
        mDrawableViewConfig = new DrawableViewConfig();
        mDrawableViewConfig.setStrokeColor(getResources().getColor(android.R.color.black));
        mDrawableViewConfig.setShowCanvasBounds(true);
        mDrawableViewConfig.setStrokeWidth(20.0f);
        mDrawableViewConfig.setMinZoom(1.0f);
        mDrawableViewConfig.setMaxZoom(1.0f);
        mDrawableViewConfig.setCanvasHeight(1280);
        mDrawableViewConfig.setCanvasWidth(720);
        mDrawableView = (DrawableView)mFrameLayout.findViewById(R.id.floating_drawable_view);
        mDrawableView.setConfig(mDrawableViewConfig);
        mToolsView = mFrameLayout.findViewById(R.id.floating_tools);
        mUndo = (Button)mFrameLayout.findViewById(R.id.floating_undo);
        mUndo.setOnClickListener(this);
        mClose = (Button)mFrameLayout.findViewById(R.id.floating_close);
        mClose.setOnClickListener(this);
        return mFrameLayout;
    }

    public void setViewStatue(){

    }

}