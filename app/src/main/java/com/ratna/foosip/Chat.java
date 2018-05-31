package com.ratna.foosip;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import SharedPreferences.SavedParameter;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Chat extends AppCompatActivity {

    Toolbar toolbar;
    ProgressBar progress;
    EmojiconEditText message;
    FloatingActionButton send;
    ImageButton emoji;
    RecyclerView grid;
    private EmojIconActions emojIcon;

    View root;

    String groupId;
    String groupName;

    SavedParameter savedParameter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat2);

        savedParameter = new SavedParameter(this);

        groupId = getIntent().getStringExtra("groupId");
        groupName = getIntent().getStringExtra("name");

        toolbar = findViewById(R.id.toolbar2);
        progress = findViewById(R.id.progressBar5);
        message = findViewById(R.id.editText6);
        send = findViewById(R.id.floatingActionButton3);
        emoji = findViewById(R.id.imageButton);
        grid = findViewById(R.id.grid);
        root = findViewById(R.id.contentRoot);


        emojIcon = new EmojIconActions(this, root, message, emoji);
        emojIcon.ShowEmojIcon();


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitleTextColor(Color.BLACK);
        toolbar.setTitle(groupName);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String m = message.getText().toString();

                if (m.length() > 0)
                {



                    progress.setVisibility(View.VISIBLE);

                    final Retrofit retrofit = new Retrofit.Builder()
                            .baseUrl("http://foosip.com/")
                            .addConverterFactory(ScalarsConverterFactory.create())
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();

                    final AllAPIs cr = retrofit.create(AllAPIs.class);

                    Call<createGroupBean> call = cr.sendMessage(groupId , savedParameter.getUID() , m);
                    call.enqueue(new Callback<createGroupBean>() {
                        @Override
                        public void onResponse(Call<createGroupBean> call, Response<createGroupBean> response) {


                            if (response.body().getStatus().equals("1"))
                            {
                                message.setText("");
                            }


                            progress.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<createGroupBean> call, Throwable t) {
                            progress.setVisibility(View.GONE);
                        }
                    });




                }

            }
        });


    }
}
