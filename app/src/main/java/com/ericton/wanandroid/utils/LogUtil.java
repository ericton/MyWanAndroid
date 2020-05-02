package com.ericton.wanandroid.utils;

import android.util.Log;

import com.ericton.wanandroid.BuildConfig;

/**
 * Created by Tang.
 * Date: 2020-04-23
 */
public class LogUtil {

    public static void i(String tag,String message) {
        if (BuildConfig.DEBUG) {
            Log.i(tag,message);
        }
    }
    public static void i(String message) {
        if (BuildConfig.DEBUG) {
            Log.i("MyWanAndroidLog",message);
        }
    }
}
