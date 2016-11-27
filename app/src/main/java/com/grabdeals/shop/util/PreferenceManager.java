package com.grabdeals.shop.util;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.grabdeals.shop.ui.LoginActivity;

/**
 * Created by KTirumalsetty on 11/2/2016.
 */

public class PreferenceManager {
    private static final String KEY_ACC_ID = "acc_id" ;
    private static final String KEY_SHOP_ID = "shop_id";
    // Shared Preferences
    SharedPreferences mPreferences;

    // Editor for Shared preferences
    SharedPreferences.Editor mEditor;

    // Context
    Context mContext;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "ShopKeeper";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_AUTH_TOKEN = "auth_token";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Constructor
    public PreferenceManager(Context context){
        this.mContext = context;
        mPreferences = mContext.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        mEditor = mPreferences.edit();
    }

    public void setAuthToken(String authToken) {
        mEditor.putString(KEY_AUTH_TOKEN, authToken).commit();

    }
    public void setAccountID(String accId) {
        mEditor.putString(KEY_ACC_ID, accId).commit();

    }public void setShopID(String shopID) {
        mEditor.putString(KEY_SHOP_ID, shopID).commit();

    }
    public String getAuthToken() {
        String bearerToken = mPreferences.getString(KEY_AUTH_TOKEN,"");
        return bearerToken;
    }
    public String getShopID() {
        return mPreferences.getString(KEY_SHOP_ID,"");
    }
    public String getAccID() {
        return mPreferences.getString(KEY_ACC_ID,"");
    }

    /**
     * Clear session details
     * */
    public void logoutUser(){
        // Clearing all data from Shared Preferences
        mEditor.clear();
        mEditor.commit();

        // After logout redirect user to Loing Activity
        Intent i = new Intent(mContext, LoginActivity.class);
        // Closing all the Activities
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        mContext.startActivity(i);
    }
}
