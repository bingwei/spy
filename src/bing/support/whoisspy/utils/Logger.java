package bing.support.whoisspy.utils;

import android.util.Log;

public class Logger {
    private static final String TAG = "WhoIsSpy";

    public static void v(Object message) {
        Log.v(TAG, String.valueOf(message));
    }

    public static void d(Object message) {
        Log.d(TAG, String.valueOf(message));
    }

    public static void i(Object message) {
        Log.i(TAG, String.valueOf(message));
    }

    public static void w(Object message) {
        Log.w(TAG, String.valueOf(message));
    }

    public static void e(Object message) {
        Log.e(TAG, String.valueOf(message));
    }
    public static void wtf(Object message) {
    	Log.wtf(TAG, String.valueOf(message));
    }
}
