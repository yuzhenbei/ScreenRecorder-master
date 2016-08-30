package com.aoaoyi.screenrecorder.util;

import android.util.Log;


/**
 * APP为Release包时Log将不被执行,为Debug包时Log将被执行
 * <br />
 *
 * @author Yuzhenbei
 *
 */
public class MLog {
	
	public static final String TAG = MLog.class.getSimpleName();
	public static boolean IS_LOG = true;

	public static void i(String pTag, String pMsg) {
		if (IS_LOG)
			Log.i(pTag, pMsg);
	}
	
	public static void i(String pTag, String pMsg, Throwable pThrowable) {
		if (IS_LOG)
			Log.i(pTag, pMsg, pThrowable);
	}

	public static void e(String pTag, String pMsg) {
		if (IS_LOG)
			Log.e(pTag, pMsg);
	}

	public static void e(String pTag, String pMsg, Throwable pThrowable) {
		if (IS_LOG)
			Log.e(pTag, pMsg, pThrowable);
	}
	
	public static void d(String pTag, String pMsg) {
		if (IS_LOG)
			Log.d(pTag, pMsg);
	}
	
	public static void d(String pTag, String pMsg, Throwable pThrowable) {
		if (IS_LOG)
			Log.d(pTag, pMsg, pThrowable);
	}

	public static void v(String pTag, String pMsg) {
		if (IS_LOG)
			Log.v(pTag, pMsg);
	}
	
	public static void v(String pTag, String pMsg, Throwable pThrowable) {
		if (IS_LOG)
			Log.v(pTag, pMsg, pThrowable);
	}
	
	public static void w(String pTag, String pString) {
		if (IS_LOG)
			Log.w(pTag, pString);
	}
	
	public static void w(String pTag, String pMsg, Throwable pThrowable) {
		if (IS_LOG)
			Log.w(pTag, pMsg, pThrowable);
	}
	
	public static void w(String pTag, Throwable pThrowable) {
		if (IS_LOG)
			Log.w(pTag, pThrowable);
	}
	
	public static void wtf(String pTag, String pString) {
		if (IS_LOG)
			Log.wtf(pTag, pString);
	}
	
	public static void wtf(String pTag, String pMsg, Throwable pThrowable) {
		if (IS_LOG)
			Log.wtf(pTag, pMsg, pThrowable);
	}
	
	public static void wtf(String pTag, Throwable pThrowable) {
		if (IS_LOG)
			Log.wtf(pTag, pThrowable);
	}
	
	public static void isLoggable(String pTag, int plevel) {
		if (IS_LOG)
			Log.isLoggable(pTag, plevel);
	}
	
	public static void sysout(String pTag, String pString) {
		if (IS_LOG)
			System.out.println(pTag + "  " + pString);
	}
}
