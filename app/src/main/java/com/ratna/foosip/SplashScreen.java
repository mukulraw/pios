package com.ratna.foosip;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import Utils.NotificationUtils;
import app.Config;

/**
 * Created by ratna on 2/7/2017.
 */
public class SplashScreen extends AppCompatActivity {

    public static final int MY_PERMISSIONS_REQUEST_WRITE_CALENDAR = 123;
    private static final String TAG = Home.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;

    SharedPreferences fcmPref;
    SharedPreferences.Editor fcmEdit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

       //checkPermission();

        fcmPref = getSharedPreferences(Config.SHARED_PREF , Context.MODE_PRIVATE);
        fcmEdit = fcmPref.edit();

       if (checkPermission())
       {




           try {

               String tok = FirebaseInstanceId.getInstance().getToken();

               Log.d("token", tok);

               fcmEdit.putString("token" , tok);

               fcmEdit.apply();

               displayFirebaseRegId();

           } catch (Exception e) {

               new Thread() {
                   @Override
                   public void run() {
                       //If there are stories, add them to the table
                       //try {
                       // code runs in a thread
                       //runOnUiThread(new Runnable() {
                       //  @Override
                       //public void run() {
                       new MyFirebaseInstanceIDService().onTokenRefresh();
                       //}
                       //});
                       //} catch (final Exception ignored) {
                       //}
                   }
               }.start();

               e.printStackTrace();
           }

       }



        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {


                Log.d("intent" , "1");

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    //FirebaseMessaging.getInstance().subscribeToTopic(app.Config.TOPIC_GLOBAL);

                    Log.d("intent" , "2");

                    displayFirebaseRegId();

                }


            }
        };

        //displayFirebaseRegId();

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));


    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        //SharedPreferences pref = getApplicationContext().getSharedPreferences(app.Config.SHARED_PREF, 0);
        String regId = fcmPref.getString("token", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)){

                Intent intent = new Intent(SplashScreen.this,SignIn.class);
                startActivity(intent);
                finish();
//            Toast.makeText(this,regId.toString(),Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"reg id not recieved yet!!",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver


        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission()
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) + ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) + ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) + ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)  != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.READ_PHONE_STATE) ||
                        ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.RECEIVE_SMS) || ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Write permission is necessary to write event!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(SplashScreen.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS , Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_PHONE_STATE, Manifest.permission.RECEIVE_SMS , Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_CALENDAR);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_CALENDAR:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mRegistrationBroadcastReceiver = new BroadcastReceiver() {
                        @Override
                        public void onReceive(Context context, Intent intent) {

                            // checking for type intent filter
                            if (intent.getAction().equals(app.Config.REGISTRATION_COMPLETE)) {
                                // gcm successfully registered
                                // now subscribe to `global` topic to receive app wide notifications
                                //FirebaseMessaging.getInstance().subscribeToTopic(app.Config.TOPIC_GLOBAL);

                                try {

                                    String tok = FirebaseInstanceId.getInstance().getToken();

                                    Log.d("token", tok);

                                    fcmEdit.putString("token" , tok);

                                    fcmEdit.apply();

                                    displayFirebaseRegId();

                                } catch (Exception e) {

                                    new Thread() {
                                        @Override
                                        public void run() {
                                            //If there are stories, add them to the table
                                            //try {
                                            // code runs in a thread
                                            //runOnUiThread(new Runnable() {
                                            //  @Override
                                            //public void run() {
                                            new MyFirebaseInstanceIDService().onTokenRefresh();
                                            //}
                                            //});
                                            //} catch (final Exception ignored) {
                                            //}
                                        }
                                    }.start();

                                    e.printStackTrace();
                                }

                            }


                        }
                    };
                } else {
//code for deny
                }
                break;
        }
    }
}

