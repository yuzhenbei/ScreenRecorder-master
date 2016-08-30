package com.aoaoyi.screenrecorder.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;

/**
 * 设备屏幕相关的Width、Height
 *
 * Created by yuzhenbei
 */
public class DeviceWindowUtil {
    /**
     * 获取屏幕宽度
     *
     * @param pContext Context
     * @return  int(px)
     */
    public static int getWindowWidth(Context pContext){
        DisplayMetrics _DisplayMetrics = pContext.getResources().getDisplayMetrics();
        return _DisplayMetrics.widthPixels;
    }

    /**
     * 获取屏幕高度
     *
     * @param pContext Context
     * @return   int(px)
     */
    public static int getWindowHeight(Context pContext){
        DisplayMetrics _DisplayMetrics = pContext.getResources().getDisplayMetrics();
        return _DisplayMetrics.heightPixels;
    }

    /**
     * 获取状态栏的高度
     *
     * @param pContext Context
     * @return int(px)
     */
    public static int getStatusHeight(Context pContext) {
        int _Statusheight = 50;
        int _ResourceId = pContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (_ResourceId > 0) {
            _Statusheight = pContext.getResources().getDimensionPixelSize(_ResourceId);
        }
        return _Statusheight;
    }

    /**
     * 获取ActionBar的高度
     *
     * @param pContext Context
     * @return int(px)
     */
    public static int getActionBarHeight(Context pContext) {
        int _ActionBarHeight = 0;
        TypedValue _TypedValue = new TypedValue();
        if (pContext.getTheme().resolveAttribute(android.R.attr.actionBarSize, _TypedValue, true))
        {
            _ActionBarHeight = TypedValue.complexToDimensionPixelSize(_TypedValue.data, pContext.getResources().getDisplayMetrics());
        }
        return _ActionBarHeight;
    }

    /**
     * 显示区域的高(屏幕高度 - 状态栏的高度)
     *
     * @param pContext Context
     * @return int(px)
     */
    public static int getDisplayHeightMinusStatus(Context pContext){
        return getWindowHeight(pContext) - getStatusHeight(pContext);
    }

    /**
     * 显示区域的高(屏幕高度 - 状态栏的高度 - ActionBar的高度)
     *
     * @param pContext Context
     * @return int(px)
     */
    public static int getDisplayHeight(Context pContext){
        return getWindowHeight(pContext) - getStatusHeight(pContext) - getActionBarHeight(pContext);
    }

    /**
     * 获取NavigationBar的高度
     *
     * @param pContext Context
     * @return int(px)
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static int getNavigationBarHeight(Context pContext){
        try {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
                if (((Activity)pContext).getWindow().getDecorView().getSystemUiVisibility() == View.SYSTEM_UI_FLAG_VISIBLE) {
                    Resources resources = pContext.getResources();
                    int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
                    if (resourceId > 0) {
                        return resources.getDimensionPixelSize(resourceId);
                    }
                }
            }
            return 0;
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * Android中的Dip转换成像素(px)
     *
     * @param pContext Context
     * @param pFloat dp or dip
     * @return px
     */
    public static int dip2px(Context pContext, float pFloat) {
        int value = (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, pFloat, pContext.getResources().getDisplayMetrics()) + 0.5f);
        return value;
    }

    /**
     * 像素（px）转换成Android中的Dip
     *
     * @param pContext Context
     * @param pFloat px
     * @return dp or dip
     */
    public static int px2dip(Context pContext, float pFloat) {
        return (int) (0.5F + pFloat / pContext.getResources().getDisplayMetrics().density);
    }

    /**
     * Android中的sp转换成像素(px)
     *
     * @param pContext Context
     * @param sp sp
     * @return px
     */
    public static float sp2px(Context pContext, float sp){
        return sp * pContext.getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * get ScreenDensityDpi
     *
     * @param pContext Context
     * @return
     */
    public static int getScreenDensityDpi(Context pContext){
        return pContext.getResources().getDisplayMetrics().densityDpi;
    }

    /**
     * get Screen Orientation
     *
     * @param pContext
     * @return Configuration.ORIENTATION_PORTRAIT(竖) or Configuration.ORIENTATION_LANDSCAPE(横)
     */
    public static int getScreenOrientation(Context pContext){
       return pContext.getResources().getConfiguration().orientation;
    }

    /**
     * 判断PopupWindow向上显示还是向下显示
     *
     * @param pContext
     * @param pView
     * @return true:向上; false:向下;
     */
    public static boolean isOnHalfOfWindow(Context pContext, View pView){
        int _HalfOfWindow = DeviceWindowUtil.getWindowHeight(pContext) - pView.getBottom();
        return pView.getTop() > _HalfOfWindow ? true : false;
    }

    public static String getResolution(Context pContext) {
        String _Resolution;

        try {
            _Resolution = pContext.getResources().getDisplayMetrics().widthPixels + " x " + pContext.getResources().getDisplayMetrics().heightPixels;
        } catch (Exception e) {
            _Resolution = "";
        }

        return _Resolution;
    }

    public static float getDensity(Context pContext) {
        try {
            return pContext.getResources().getDisplayMetrics().density;
        } catch (Exception e) {
            return 1;
        }
    }

    public static int dp(Context pContext, float value) {
        if (value == 0) {
            return 0;
        }
        return (int) Math.ceil(getDensity(pContext) * value);
    }

    /**
     * 通过判断设备是否有返回键、菜单键
     *
     * @param pContext
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context pContext) {
        boolean hasMenuKey = ViewConfiguration.get(pContext).hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
        if (!hasMenuKey && !hasBackKey) {
            return true;
        }
        return false;
    }

}
