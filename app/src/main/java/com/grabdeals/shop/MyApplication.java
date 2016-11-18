package com.grabdeals.shop;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;

import com.grabdeals.shop.util.NetworkManager;

/**
 * Created by KTirumalsetty on 10/31/2016.
 */

public class MyApplication extends Application {

    private static final String GCM_PROJECT_NUMBER = "";
    private static Context context;
    public static String gcmRegID;

    //Creating a broadcast receiver for gcm registration
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    public static Context getAppContext() {
        return MyApplication.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        MultiDex.install(this);
        MyApplication.context = getApplicationContext();
        NetworkManager.getInstance(this);
//        getRegId();
    }




}
