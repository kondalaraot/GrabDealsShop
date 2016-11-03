package com.grabdeals.shop.util;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

import com.grabdeals.shop.BuildConfig;
import com.grabdeals.shop.MyApplication;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class UserAgent {

    private static final String OS_NAME = "Android";
    private static final String CLIENT_TYPE = "shop_keeper";
    //App Code (APP001  - User app,  APP002 - Shop) 
    private static final String APP_CODE = "APP002";
    private static final String APP_VERSION = "APP002";
    private static final String TAG = "UserInfo";

    public static String getAppCode() {
        return APP_CODE;
    }

    public static String getAppVersion() {

/*version code*/
//		BuildConfig.VERSION_CODE
        return BuildConfig.VERSION_NAME;
    }

    public static String getOsName() {

        return OS_NAME;
    }
    public static String getOsVersion() {

        return Build.VERSION.RELEASE;
    }
    public static String getDeviceMake() {
        String manufacturer = Build.MANUFACTURER;
        return manufacturer;
    }
    public static String getDeviceModel() {
        String model = Build.MODEL;
        return model;
    }
    public static String getNotificationID() {

        return "testid";
    }
    public static String getDeviceMacAddress() {

        return "";
    }
    public static String getMacAddr() {
        try {
            List<NetworkInterface> all = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface nif : all) {
                if (!nif.getName().equalsIgnoreCase("wlan0")) continue;

                byte[] macBytes = nif.getHardwareAddress();
                if (macBytes == null) {
                    return "";
                }

                StringBuilder res1 = new StringBuilder();
                for (byte b : macBytes) {
                    res1.append(String.format("%02X:",b));
                }

                if (res1.length() > 0) {
                    res1.deleteCharAt(res1.length() - 1);
                }
                return res1.toString();
            }
        } catch (Exception ex) {
        }
        return "02:00:00:00:00:00";
    }

    public static String getLocale() {
        return Locale.getDefault().getLanguage();
    }

    public static String getCountry() {
        return Locale.getDefault().getCountry();
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer) || manufacturer.equals(Build.UNKNOWN)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public static String getOperator() {
        TelephonyManager telephonyManager = ((TelephonyManager) MyApplication.getAppContext().
                getSystemService(Context.TELEPHONY_SERVICE));
        String operatorName = telephonyManager.getNetworkOperatorName();
        return operatorName;
    }


	/*public static String getUAChannelID() {
		AppPreferences appPreferences = AppPreferences.getInstance(ApplicationData.getAppContext());
		String installationId = appPreferences.getString(AppPreferences.CHANNEL_ID, null);
		if (installationId == null || installationId.equals("")) {
			String channelID = UAirship.shared().getPushManager().getChannelId();
			Log.i(TAG,"channelID"+channelID);
			appPreferences.putString(AppPreferences.CHANNEL_ID, channelID);
		}
		return installationId;
	}*/



    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

}
