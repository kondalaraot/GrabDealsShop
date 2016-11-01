package com.grabdeals.shop;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.grabdeals.shop.util.Constants;
import com.grabdeals.shop.util.NetworkManager;

import java.io.IOException;

/**
 * Created by KTirumalsetty on 10/31/2016.
 */

public class MyApplication extends Application {

    private static final String GCM_PROJECT_NUMBER = "";
    private static Context context;
    private GoogleCloudMessaging gcm;
    public static String gcmRegID;

    public static Context getAppContext() {
        return MyApplication.context;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        NetworkManager.getInstance(this);
        getRegId();
    }

    public void getRegId(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    gcmRegID = gcm.register(GCM_PROJECT_NUMBER);
                    msg = "Device registered, registration ID=" + gcmRegID;
                    if (Constants.DEBUG)Log.i("GCM",  msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                //etRegId.setText(msg + "\n");
            }
        }.execute(null, null, null);
    }



}
