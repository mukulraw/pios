package com.ratna.foosip;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ratna on 4/6/2018.
 */

public class  HomeChat extends AppCompatActivity  {

    private FirebaseAuth mAuth;
    private Toolbar mToolbar;

    private ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    private DatabaseReference mUserRef;

    private TabLayout mTabLayout;
    private static final int GALLERY_PICK = 1;

    // Storage Firebase
    private StorageReference mImageStorage;

    private String mCurrentUserId;
    private DatabaseReference mRootRef;

    public static int CAMERA_PREVIEW_RESULT = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        mAuth = FirebaseAuth.getInstance();

        mToolbar = (Toolbar) findViewById(R.id.main_page_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Foosip Chat");



        if (mAuth.getCurrentUser() != null) {


            mUserRef = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        }


        mRootRef = FirebaseDatabase.getInstance().getReference();
        mCurrentUserId = mAuth.getCurrentUser().getUid();


        //------- IMAGE STORAGE ---------
        mImageStorage = FirebaseStorage.getInstance().getReference();

        //Tabs
        mViewPager = (ViewPager) findViewById(R.id.main_tabPager);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(4);

        mTabLayout = (TabLayout) findViewById(R.id.main_tabs);

        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.user_select));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.tounge_select));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.send_message_select));
        mTabLayout.addTab(mTabLayout.newTab().setIcon(R.drawable.post_it_select));

        mTabLayout.setupWithViewPager(mViewPager);

        mTabLayout.getTabAt(0).setIcon(R.drawable.user_select);
        mTabLayout.getTabAt(1).setIcon(R.drawable.tounge_select);
        mTabLayout.getTabAt(2).setIcon(R.drawable.send_message_select);
        mTabLayout.getTabAt(3).setIcon(R.drawable.post_it_select);





    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser == null){

            sendToStart();

        } else {

            mUserRef.child("online").setValue("true");

        }

    }


    @Override
    protected void onStop() {
        super.onStop();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if(currentUser != null) {

            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);

        }

    }

    private void sendToStart() {

        Intent startIntent = new Intent(HomeChat.this, SignIn.class);
        startActivity(startIntent);
        finish();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.main_menu, menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

//        if(item.getItemId() == R.id.main_logout_btn){
//
//            mUserRef.child("online").setValue(ServerValue.TIMESTAMP);
//
//            FirebaseAuth.getInstance().signOut();
//            sendToStart();
//
//        }
//
//        if(item.getItemId() == R.id.main_settings_btn){
//
//            Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
//            startActivity(settingsIntent);
//
//        }

        if(item.getItemId() == R.id.main_all_btn){

            Intent settingsIntent = new Intent(HomeChat.this, AllUsersActivity.class);
            startActivity(settingsIntent);

        }

        return true;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){
//
//            Uri imageUri = data.getData();
//
//            final String current_user_ref = "restaurant/" + "qr_03";
//
//            DatabaseReference user_message_push = mRootRef.child("restaurant")
//                    .push();
//
//            final String push_id = user_message_push.getKey();
//
//
//            StorageReference filepath = mImageStorage.child("group_message_images").child( push_id + ".jpg");
//
//            filepath.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
//
//                    if(task.isSuccessful()){
//
//                        String download_url = task.getResult().getDownloadUrl().toString();
//
//
//                        Map messageMap = new HashMap();
//                        messageMap.put("message", download_url);
//                        messageMap.put("seen", false);
//                        messageMap.put("type", "image");
//                        messageMap.put("time", ServerValue.TIMESTAMP);
//                        messageMap.put("from", mCurrentUserId);
//
//                        Map messageUserMap = new HashMap();
//                        messageUserMap.put(current_user_ref + "/" + push_id, messageMap);
//
//
//
//                        mRootRef.updateChildren(messageUserMap, new DatabaseReference.CompletionListener() {
//                            @Override
//                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
//
//                                if(databaseError != null){
//
//                                    Log.d("CHAT_LOG", databaseError.getMessage().toString());
//
//                                }
//
//                            }
//                        });
//
//
//                    }
//
//                }
//            });
//
//        }
//
//    }


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && requestCode == CAMERA_PREVIEW_RESULT) {
//            String path = data.getStringExtra(CameraPreviewActivity.RESULT_IMAGE_PATH);
//
//            Toast.makeText(this, "Image Save on: " + path, Toast.LENGTH_LONG).show();
//
//        }
//    }


    //Important for Android 6.0 permisstion request, don't forget this!

}

