package com.grabdeals.shop;

import android.app.Application;

import com.grabdeals.shop.util.NetworkManager;

/**
 * Created by KTirumalsetty on 10/31/2016.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate()
    {
        super.onCreate();
        NetworkManager.getInstance(this);
    }


}
