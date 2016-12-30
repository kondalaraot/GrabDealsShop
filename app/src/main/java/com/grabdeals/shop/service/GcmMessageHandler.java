/**
 * 
 */
package com.grabdeals.shop.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.grabdeals.shop.R;
import com.grabdeals.shop.receiver.GcmBroadcastReceiver;
import com.grabdeals.shop.ui.SplashScreenActivity;


/**
 * @author Kondalarao
 *
 */
public class GcmMessageHandler extends IntentService {

    
    public static final int NOTIFICATION_ID = 1;
    private static final String TAG = "GcmMessageHandler";
    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    
    String mes;
    private Handler handler;
    
   public GcmMessageHandler() {
       super("GcmMessageHandler");
   }

   @Override
   public void onCreate() {
       // TODO Auto-generated method stub
       super.onCreate();
       handler = new Handler();
   }
   @Override
	protected void onHandleIntent(Intent intent) {

		Bundle extras = intent.getExtras();
		GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

		String messageType = gcm.getMessageType(intent);

		if (!extras.isEmpty()) /*{

			if(BaseActivity.isAppVisible){
				Log.i(TAG, "Is APP Open state ---: " + BaseActivity.isAppVisible);
				Log.i(TAG, "Received: " + extras.toString());

				Intent pushIntent = new Intent(this,ActivityShowGCMPush.class);
				pushIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				pushIntent.putExtra("GCM_EXTRA", extras);
				this.startActivity(pushIntent);
			}else{
				if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR
						.equals(messageType)) {
					//sendNotification("Send error: " + extras.toString());
				} else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED
						.equals(messageType)) {
					//sendNotification("Deleted messages on server: "+ extras.toString());
				} else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE
						.equals(messageType)) {
					Log.i(TAG, "Completed work @ " + SystemClock.elapsedRealtime());

					sendNotification(extras);

					Log.i(TAG, "Received: " + extras.toString());
				}
			}
			
		}*/
		// Release the wake lock provided by the WakefulBroadcastReceiver.
		GcmBroadcastReceiver.completeWakefulIntent(intent);}

   public void showToast(){
       handler.post(new Runnable() {
           public void run() {
               Toast.makeText(getApplicationContext(),mes , Toast.LENGTH_LONG).show();
           }
        });

   }
   
   private void sendNotification(Bundle  extras) {
	   
	   String message = extras.getString("message");
	   String title = extras.getString("title");
       mNotificationManager = (NotificationManager)
               this.getSystemService(Context.NOTIFICATION_SERVICE);

       PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
               new Intent(this, SplashScreenActivity.class), 0);

       NotificationCompat.Builder mBuilder =
               new NotificationCompat.Builder(this)
                       .setSmallIcon(R.mipmap.ic_launcher)
                       .setContentTitle(title)
                       .setStyle(new NotificationCompat.BigTextStyle()
                               .bigText(message))
                       .setContentText(message);
       				   
       					
      // Uri uri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
      // builder.setSound(uri);
       mBuilder.setDefaults(Notification.DEFAULT_SOUND);
       mBuilder.setContentIntent(contentIntent);
       Notification notification = mBuilder.build();
    // hide the notification after its selected
       notification.flags = Notification.FLAG_AUTO_CANCEL;
       mNotificationManager.notify(NOTIFICATION_ID, notification);
   }
}
