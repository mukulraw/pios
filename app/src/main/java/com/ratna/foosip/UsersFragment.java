package com.ratna.foosip;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ratna.foosip.allUsersPOJO.Datum;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersFragment extends Fragment {

    RecyclerView grid;
    ProgressBar progress;
    GridLayoutManager manager;
    List<Datum> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.all_users_fragment , container , false);

        grid = view.findViewById(R.id.grid);
        progress = view.findViewById(R.id.progress);
        manager = new GridLayoutManager(getContext() , 1);
        list = new ArrayList<>();





        return view;

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

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.all_users_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Datum item = list.get(position);



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
