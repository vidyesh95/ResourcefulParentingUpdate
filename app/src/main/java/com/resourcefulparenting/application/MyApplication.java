package com.resourcefulparenting.application;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.resourcefulparenting.BuildConfig;


import okhttp3.OkHttpClient;

public class MyApplication extends Application {
    public void onCreate() {
        super.onCreate();
    //      Smartlook.setupAndStartRecording("3eccb81c01c93d27f79f463a448cdc0764d4f96a");
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this);
        }
    }
}
