package com.ratna.foosip;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.ag.floatingactionmenu.OptionsFabLayout;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import SharedPreferences.SavedParameter;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class Wall extends Fragment {

    RecyclerView grid;
    WallAdapter adapter;
    OptionsFabLayout fab;
    List<postListbean> list;
    ProgressBar progress;
    SavedParameter savedParameter;
    BroadcastReceiver commentReceiver;
    TextView newPosts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wall_layout, container, false);


        savedParameter = new SavedParameter(getContext());
        list = new ArrayList<>();
        newPosts = view.findViewById(R.id.textView63);
        grid = view.findViewById(R.id.grid);
        adapter = new WallAdapter(getContext() , list);
        fab = view.findViewById(R.id.add);
        progress = view.findViewById(R.id.progressBar8);


        fab.setMiniFabsColors(
                R.color.colorPrimary,
                R.color.green_fab);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);

        grid.setAdapter(adapter);
        grid.setLayoutManager(staggeredGridLayoutManager);


        fab.setMainFabOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (fab.isOptionsMenuOpened()) {
                    fab.closeOptionsMenu();
                }

            }
        });


        fab.setMiniFabSelectedListener(new OptionsFabLayout.OnMiniFabSelectedListener() {
            @Override
            public void onMiniFabSelected(MenuItem fabItem) {


                switch (fabItem.getItemId()) {
                    case R.id.fab_add:


                        Intent intent = new Intent(getContext() , ShareMoment.class);
                        startActivity(intent);
                        fab.closeOptionsMenu();


                        break;
                    case R.id.fab_link:

                        Intent intent1 = new Intent(getContext() , SharePost.class);
                        startActivity(intent1);
                        fab.closeOptionsMenu();

                    default:
                        break;
                }


            }
        });


        commentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                Log.d("asdasasddsa" , "ASasasdad");

                // checking for type intent filter
                if (intent.getAction().equals("post")) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications


                    Log.d("data", intent.getStringExtra("data"));

                    String json = intent.getStringExtra("data");

                    Gson gson = new Gson();

                    postListbean item = gson.fromJson(json, postListbean.class);

                    try {
                        if (!item.getUser_id().equals(savedParameter.getUID()))
                        {
                            adapter.addData(item);

                            newPosts.setVisibility(View.VISIBLE);
                            // grid.scrollToPosition(0);
                        }
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }





                    //displayFirebaseRegId();

                }/* else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    txtMessage.setText(message);
                }*/
            }
        };

        newPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                grid.scrollToPosition(0);
                newPosts.setVisibility(View.GONE);

            }
        });

        LocalBroadcastManager.getInstance(getContext()).registerReceiver(commentReceiver,
                new IntentFilter("post"));

        return view;
    }


    @Override
    public void onStop() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(commentReceiver);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();

        progress.setVisibility(View.VISIBLE);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://foosip.com/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        final AllAPIs cr = retrofit.create(AllAPIs.class);

        Call<List<postListbean>> call = cr.getPosts(savedParameter.getQrCode());

        call.enqueue(new Callback<List<postListbean>>() {
            @Override
            public void onResponse(Call<List<postListbean>> call, Response<List<postListbean>> response) {

                try {

                    if (response.body().size() > 0)
                    {
                        adapter.setGridData(response.body());
                    }

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<List<postListbean>> call, Throwable t) {
                progress.setVisibility(View.GONE);
            }
        });

    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int mSpace;

        public SpacesItemDecoration(int space) {
            this.mSpace = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.bottom = mSpace;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildAdapterPosition(view) == 0)
                outRect.top = mSpace;
        }
    }

    class WallAdapter extends RecyclerView.Adapter<WallAdapter.ViewHolder> {

        Context context;
        List<postListbean> list = new ArrayList<>();


        public WallAdapter(Context context , List<postListbean> list) {
            this.context = context;
            this.list = list;
        }

        public void setGridData(List<postListbean> list)
        {
            this.list = list;
            notifyDataSetChanged();
        }

        public void addData(postListbean item)
        {
            list.add(0 , item);
            notifyItemInserted(0);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.wall_list_model, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.setIsRecyclable(false);

            postListbean item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();

            ImageLoader loader = ImageLoader.getInstance();

            loader.displayImage("http://foosip.com/" + item.getSenderImage() , holder.profile , options);

            holder.name.setText(item.getSenderName());

            holder.type.setText(item.getPostType());

            holder.likes.setText(item.getTotalLikes() + " likes");
            holder.comments.setText(item.getTotalComments() + " comments");

            if (item.getType().equals("image"))
            {
                loader.displayImage("http://foosip.com/uploads/posts/" + item.getPost() , holder.image , options);
                holder.image.setVisibility(View.VISIBLE);
                holder.post.setVisibility(View.GONE);
            }
            else
            {
                holder.post.setText(item.getPost());
                holder.image.setVisibility(View.GONE);
                holder.post.setVisibility(View.VISIBLE);
            }


        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            CircleImageView profile;
            TextView name , type , post;
            ImageView image;

            TextView likes , comments;

            public ViewHolder(View itemView) {
                super(itemView);


                profile = itemView.findViewById(R.id.view5);
                name = itemView.findViewById(R.id.textView31);
                type = itemView.findViewById(R.id.textView58);
                post = itemView.findViewById(R.id.textView59);
                image = itemView.findViewById(R.id.imageView7);

                likes = itemView.findViewById(R.id.textView60);
                comments = itemView.findViewById(R.id.textView61);


            }
        }
    }
}
