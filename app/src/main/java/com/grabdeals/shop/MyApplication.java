package com.grabdeals.shop;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;

import com.grabdeals.shop.util.NetworkManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by KTirumalsetty on 10/31/2016.
 */

public class MyApplication extends Application {

    private static final String GCM_PROJECT_NUMBER = "";
    private static Context context;
    public static String gcmRegID;

//    public static Account sAccount;

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
        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this).build();

        ImageLoader.getInstance().init(config);
//        getRegId();
    }




}
