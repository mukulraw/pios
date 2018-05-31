package com.ratna.foosip;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.ratna.foosip.HomeChat;
import com.ratna.foosip.R;

import org.json.JSONObject;

/**
 * Created by ratna on 10/18/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "FCM Service";
    private Bitmap bitmap;
    private Context context = this;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            sendUserNotification(remoteMessage.getNotification().getTitle() , remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }



        /*if (remoteMessage.getData().size() > 0) {
            Toast.makeText(context,"get mEssage",Toast.LENGTH_LONG).show();
            sendUserNotification(remoteMessage.getData().get("title"), remoteMessage.getData().get("text"));


        }*/

    }


    private void handleDataMessage(JSONObject data2) {
        Log.e(TAG, "push json: " + data2.toString());

        try {
            //JSONObject data = data2.getJSONObject("data");


            String type = data2.getString("type");






            if (type.equals("comment"))
            {
                JSONObject dat = data2.getJSONObject("data");

                Log.d("ddata" , dat.toString());

                Intent registrationComplete = new Intent("commentData");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);

            }
            else if (type.equals("view"))
            {

                JSONObject dat = data2.getJSONObject("data");
                Log.d("view" , "called");

                Intent registrationComplete = new Intent("view");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            }
            else if (type.equals("gift"))
            {

                JSONObject dat = data2.getJSONObject("data");
                Log.d("view" , "called");

                Intent registrationComplete = new Intent("gift");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            }
            else if (type.equals("like"))
            {
                String dat = data2.getString("data");

                Log.d("view" , "called");

                Intent registrationComplete = new Intent("like");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            }
            else if (type.equals("request"))
            {
                String dat = data2.getString("data");

                Log.d("request" , "called");

                Intent registrationComplete = new Intent("request");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            }
            else if (type.equals("status"))
            {
                String dat = data2.getString("data");

                Log.d("status" , "called");

                Intent registrationComplete = new Intent("status");
                registrationComplete.putExtra("data", dat.toString());

                LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
            }






        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }


    private void sendUserNotification(String title, String mess) {
        int notifyID = 1;
        Intent intent;
        NotificationChannel mChannel;
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        intent = new Intent(context, HomeChat.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        String CHANNEL_ID = context.getPackageName();// The id of the channel.
        CharSequence name = "Sample one";// The user-visible name of the channel.
        int importance = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH;
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context);
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
        notificationBuilder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(mess));
        notificationBuilder.setContentText(mess);
        notificationBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
        notificationBuilder.setSmallIcon(getNotificationIcon(notificationBuilder));

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            notificationManager.createNotificationChannel(mChannel);
        }
        if (notificationManager != null) {
            notificationManager.notify(notifyID /* ID of notification */, notificationBuilder.build());
        }


    }

    private int getNotificationIcon(NotificationCompat.Builder notificationBuilder) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int color = 0x036085;
            notificationBuilder.setColor(color);
            return R.mipmap.ic_launcher;

        } else {
            return R.mipmap.ic_launcher;
        }
    }

}