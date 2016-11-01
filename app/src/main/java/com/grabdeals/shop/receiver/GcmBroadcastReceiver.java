/**
 * 
 */
package com.grabdeals.shop.receiver;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.grabdeals.shop.service.GcmMessageHandler;


/**
 * @author Kondalarao
 *
 */
public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {

    private static final String TAG = "GcmBroadcastReceiver";

	@Override
    public void onReceive(Context context, Intent intent) {
    	
    	Log.d(TAG, "onReceive ...");

        // Explicitly specify that GcmMessageHandler will handle the intent.
        ComponentName comp = new ComponentName(context.getPackageName(),
                GcmMessageHandler.class.getName());

        // Start the service, keeping the device awake while it is launching.
        startWakefulService(context, (intent.setComponent(comp)));
        //setResultCode(Activity.RESULT_OK);
    }
	
	private static void generateNotification(Context context, String message) {/*

	    int icon = R.drawable.ic_launcher;
	    long when = System.currentTimeMillis();

	    NotificationManager notificationManager = (NotificationManager) 
	            context.getSystemService(Context.NOTIFICATION_SERVICE);

	    Notification notification = new Notification(icon, message, when);      

	    String title = context.getString(R.string.app_name);
	    Intent notificationIntent = new Intent(context, SplashScreen.class);

	      PendingIntent pintent = PendingIntent.getActivity(context, 0, intent,
	 Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

	    notification.setLatestEventInfo(context, title, message, intent);
	    notification.flags |= Notification.FLAG_AUTO_CANCEL;
	    notification.defaults |= Notification.DEFAULT_SOUND;
	    notification.defaults |= Notification.DEFAULT_VIBRATE; 
	    notificationManager.notify(1, notification);
	*/}
	
}