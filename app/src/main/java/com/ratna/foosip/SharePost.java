package com.ratna.foosip;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import SharedPreferences.SavedParameter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SharePost extends AppCompatActivity {

    Toolbar toolbar;
    Button post;
    Spinner spinner;
    EditText comment;
    ProgressBar progress;
    List<String> list;
    String type;
    SavedParameter savedParameter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_post);

        savedParameter = new SavedParameter(this);

        list = new ArrayList<>();

        list.add("Recommendation");
        list.add("Feeling");
        list.add("Thought");
        list.add("Type1");


        toolbar = findViewById(R.id.toolbar4);
        post = findViewById(R.id.button3);
        spinner = findViewById(R.id.spinner);
        comment = findViewById(R.id.editText7);
        progress = findViewById(R.id.progressBar7);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle("Share a post");
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this ,
                android.R.layout.simple_list_item_1 , list);

        spinner.setAdapter(adapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                type = list.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String c = comment.getText().toString();

                if (c.length() > 0)
                {

                    progress.setVisibility(View.VISIBLE);

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://foosip.com/")
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);

                    Call<String> call = cr.postText(savedParameter.getUID() , savedParameter.getQrCode() , type , c);

                    call.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {

                            if (response.body().equals("success"))
                            {
                                finish();
                            }
                            else
                            {
                                Toast.makeText(SharePost.this , "Error uploading filr" , Toast.LENGTH_SHORT).show();
                            }


                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });



                }


            }
        });



    }
}
