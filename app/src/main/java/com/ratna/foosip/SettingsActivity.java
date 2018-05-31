package com.ratna.foosip;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ratna.foosip.profilePOJO.picRequestBean;
import com.ratna.foosip.profilePOJO.profileBean;
import com.ratna.foosip.profilePOJO.profileRequestBean;
import com.ratna.foosip.profilePOJO.profileUpdateBean;
import com.ratna.foosip.profilePOJO.statusRequestBean;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import SharedPreferences.SavedParameter;
import SharedPreferences.UserSession;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SettingsActivity extends AppCompatActivity {

    //Android Layout

    private CircleImageView mDisplayImage;
    private TextView mName;
    private TextView mStatus;

    private Button mStatusBtn;
    private Button mImageBtn;

    private EditText ed_interest,ed_first_name,ed_last_name;

    private TextView txt_email,txt_user_name;


    private static final int GALLERY_PICK = 1;

    // Storage Firebase
    private StorageReference mImageStorage;

    //private ProgressDialog mProgressDialog;

    private SavedParameter savedParameter;

    String base_64;

    UserSession userSession;

    ProgressBar progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        savedParameter = new SavedParameter(SettingsActivity.this);
        userSession = new UserSession(this);

        boolean flag = UserSession.getProfile(this);
        if (flag) {
            Intent intent = new Intent(SettingsActivity.this, ScanQR.class);
            startActivity(intent);
            finish();
        }

        mDisplayImage = findViewById(R.id.settings_image);
        mName = findViewById(R.id.settings_name);
        mStatus = findViewById(R.id.settings_status);

        progress = findViewById(R.id.progress);

        mStatusBtn = findViewById(R.id.settings_status_btn);
        mImageBtn = findViewById(R.id.settings_image_btn);

        ed_interest = findViewById(R.id.ed_interest);
        ed_first_name = findViewById(R.id.ed_first_name);
        ed_last_name = findViewById(R.id.ed_last_name);

        txt_email = findViewById(R.id.txt_email);
        txt_user_name = findViewById(R.id.txt_user_name);

        mImageStorage = FirebaseStorage.getInstance().getReference();



        setView();






        Log.d("Bearer" , savedParameter.getTOKEN());






        mStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String status_value = mStatus.getText().toString();

                Intent status_intent = new Intent(SettingsActivity.this, StatusActivity.class);
                status_intent.putExtra("status_value", status_value);
                startActivity(status_intent);

            }
        });


        mImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);


                /*
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(SettingsActivity.this);
                        */

            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

        loadData();

    }

    public void loadData()
    {

        progress.setVisibility(View.VISIBLE);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://foosip.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);

        profileRequestBean body = new profileRequestBean();

        body.setId(savedParameter.getUID());

        Map<String, String> map = new HashMap<>();

        map.put("Content-Type" , "application/json");
        map.put("Authorization" , savedParameter.getTOKEN());

        Call<profileBean> call = cr.getProfile(body , map);


        call.enqueue(new retrofit2.Callback<profileBean>() {
            @Override
            public void onResponse(Call<profileBean> call, Response<profileBean> response) {


                mName.setText(response.body().getData().getFirstName() + " " + response.body().getData().getLastName());
                mStatus.setText(response.body().getData().getStatus());

                txt_email.setText(response.body().getData().getEmail());
                txt_user_name.setText(response.body().getData().getName());

                ed_first_name.setText(response.body().getData().getFirstName());
                ed_last_name.setText(response.body().getData().getLastName());

                ed_interest.setText(response.body().getData().getInterest());

                if (response.body().getData().getProfilePic().length() > 0)
                {

                    DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();
                    ImageLoader loader = ImageLoader.getInstance();
                    loader.displayImage("http://foosip.com/" + response.body().getData().getProfilePic() , mDisplayImage , options);

                    Log.d("iimmaaggee" , response.body().getData().getProfilePic());

                }


                progress.setVisibility(View.GONE);


            }

            @Override
            public void onFailure(Call<profileBean> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });


    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_PICK && resultCode == RESULT_OK){

            Uri imageUri = data.getData();

            CropImage.activity(imageUri)
                    .setAspectRatio(1, 1)
                    .setMinCropWindowSize(500, 500)
                    .start(this);

            //Toast.makeText(SettingsActivity.this, imageUri, Toast.LENGTH_LONG).show();

        }


        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {





                Uri resultUri = result.getUri();

                File thumb_filePath = new File(resultUri.getPath());

                //String current_user_id = mCurrentUser.getUid();


                Bitmap thumb_bitmap = new Compressor(this)
                        .setMaxWidth(200)
                        .setMaxHeight(200)
                        .setQuality(75)
                        .compressToBitmap(thumb_filePath);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                base_64 = Base64.encodeToString(thumb_byte,Base64.DEFAULT);


                progress.setVisibility(View.VISIBLE);

                final Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://foosip.com/")
                        .addConverterFactory(ScalarsConverterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                final AllAPIs cr = retrofit.create(AllAPIs.class);

                picRequestBean body = new picRequestBean();

                body.setId(savedParameter.getUID());
                body.setProfilePic(base_64);

                Map<String, String> map = new HashMap<>();

                map.put("Content-Type" , "application/json");
                map.put("Authorization" , savedParameter.getTOKEN());

                Call<profileBean> call = cr.updatePic(body , map);

                call.enqueue(new retrofit2.Callback<profileBean>() {
                    @Override
                    public void onResponse(Call<profileBean> call, Response<profileBean> response) {

                        progress.setVisibility(View.GONE);
                        loadData();

                    }

                    @Override
                    public void onFailure(Call<profileBean> call, Throwable t) {

                        progress.setVisibility(View.GONE);
                    }
                });





            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

                Exception error = result.getError();

            }
        }


    }

    private void setView() {

        LinearLayout ll_submit = findViewById(R.id.ll_submit);
        ll_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!ed_first_name.getText().toString().equals(""))
                {
                    if(!ed_last_name.getText().toString().equals(""))
                    {



                        progress.setVisibility(View.VISIBLE);

                        final Retrofit retrofit = new Retrofit.Builder()
                                .baseUrl("http://foosip.com/")
                                .addConverterFactory(ScalarsConverterFactory.create())
                                .addConverterFactory(GsonConverterFactory.create())
                                .build();

                        final AllAPIs cr = retrofit.create(AllAPIs.class);

                        profileUpdateBean body = new profileUpdateBean();

                        body.setId(savedParameter.getUID());
                        body.setFirstName(ed_first_name.getText().toString());
                        body.setInterest(ed_interest.getText().toString());
                        body.setLastName(ed_last_name.getText().toString());

                        Map<String, String> map = new HashMap<>();

                        map.put("Content-Type" , "application/json");
                        map.put("Authorization" , savedParameter.getTOKEN());

                        Call<profileBean> call = cr.updateProfile(body , map);


                        call.enqueue(new retrofit2.Callback<profileBean>() {
                            @Override
                            public void onResponse(Call<profileBean> call, Response<profileBean> response) {

                                userSession.setPROFILE(true);
                                Toast.makeText(SettingsActivity.this, "Updated", Toast.LENGTH_SHORT).show();
                                Intent mainIntent = new Intent(SettingsActivity.this, ScanQR.class);
                                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(mainIntent);
                                finish();

                                progress.setVisibility(View.GONE);
                            }

                            @Override
                            public void onFailure(Call<profileBean> call, Throwable t) {
                                progress.setVisibility(View.GONE);
                            }
                        });



                        /*new ProfileEditCall(SettingsActivity.this, SavedParameter.getEMAIL(SettingsActivity.this),
                                SavedParameter.getUSERNAME(SettingsActivity.this),ed_interest.getText().toString(),
                                ed_first_name.getText().toString(),ed_last_name.getText().toString(),base_64 );
*/
                    }else{

                        Toast.makeText(SettingsActivity.this,"Please enter Last Name",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(SettingsActivity.this,"Please enter First Name",Toast.LENGTH_SHORT).show();
                }



            }
        });

    }

    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(20);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }


}
