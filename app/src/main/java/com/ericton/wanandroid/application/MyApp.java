package com.ericton.wanandroid.application;

import android.app.Application;

import com.firefly1126.permissionaspect.PermissionCheckSDK;

/**
 * Created by Tang.
 * Date: 2020-04-26
 */
public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PermissionCheckSDK.init(this);
    }
}
