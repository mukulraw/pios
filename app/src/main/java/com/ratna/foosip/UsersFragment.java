package com.ratna.foosip;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ratna.foosip.allUsersPOJO.Datum;
import com.ratna.foosip.allUsersPOJO.allUsersBean;
import com.ratna.foosip.allUsersPOJO.allUsersRequestBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import SharedPreferences.SavedParameter;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class UsersFragment extends Fragment {

    RecyclerView grid;
    ProgressBar progress;
    GridLayoutManager manager;
    List<Datum> list;
    AllUsersAdapter adapter;
    SavedParameter savedParameter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.all_users_fragment , container , false);

        savedParameter = new SavedParameter(getContext());

        grid = view.findViewById(R.id.grid);
        progress = view.findViewById(R.id.progress);
        manager = new GridLayoutManager(getContext() , 1);
        list = new ArrayList<>();

        adapter = new AllUsersAdapter(getContext() , list);

        grid.setAdapter(adapter);
        grid.setLayoutManager(manager);






        return view;

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

        allUsersRequestBean body = new allUsersRequestBean();

        body.setRid(savedParameter.getQrCode());

        Map<String, String> map = new HashMap<>();

        map.put("Content-Type" , "application/json");
        map.put("Authorization" , savedParameter.getTOKEN());

        Call<allUsersBean> call = cr.getAllUsers(body , map);

        call.enqueue(new Callback<allUsersBean>() {
            @Override
            public void onResponse(Call<allUsersBean> call, Response<allUsersBean> response) {


                adapter.setGridData(response.body().getData());


                progress.setVisibility(View.GONE);

            }

            @Override
            public void onFailure(Call<allUsersBean> call, Throwable t) {

                progress.setVisibility(View.GONE);

            }
        });


    }

    class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.ViewHolder>
    {

        List<Datum> list = new ArrayList<>();
        Context context;

        public AllUsersAdapter(Context context , List<Datum> list)
        {
            this.context = context;
            this.list = list;
        }


        public void setGridData(List<Datum> list)
        {
            this.list = list;
            notifyDataSetChanged();
        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.all_users_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            final Datum item = list.get(position);

            DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true).cacheOnDisk(true).resetViewBeforeLoading(false).build();

            ImageLoader loader = ImageLoader.getInstance();
            loader.displayImage("http://foosip.com/" + item.getProfilePic() , holder.profile , options);

            Log.d("image" , "http://foosip.com/" + item.getProfilePic());

            holder.name.setText(item.getFirstName());


            if (position == list.size() - 1)
            {

                holder.line.setVisibility(View.GONE);

            }else
            {

                holder.line.setVisibility(View.VISIBLE);

            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent profileIntent = new Intent(context, ProfileActivity.class);
                    profileIntent.putExtra("user_id", item.getUid());
                    context.startActivity(profileIntent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {

            CircleImageView profile;
            TextView name , status , age , line;

            public ViewHolder(View itemView) {
                super(itemView);

                profile = itemView.findViewById(R.id.view);
                name = itemView.findViewById(R.id.textView18);
                status = itemView.findViewById(R.id.textView28);
                age = itemView.findViewById(R.id.textView29);
                line = itemView.findViewById(R.id.textView30);

            }
        }

    }

}
