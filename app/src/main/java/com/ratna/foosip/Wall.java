package com.ratna.foosip;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class Wall extends Fragment {

    RecyclerView grid;
    WallAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wall_layout , container , false);


        grid = view.findViewById(R.id.grid);
        adapter = new WallAdapter(getContext());

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

        grid.setAdapter(adapter);
        grid.setLayoutManager(staggeredGridLayoutManager);


        return view;
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

    class WallAdapter extends RecyclerView.Adapter<WallAdapter.ViewHolder>
    {

        Context context;
        int images[] = new int[]
                {
                        R.drawable.dp1,
                        R.drawable.dp2,
                        R.drawable.dp3,
                        R.drawable.dp4,
                        R.drawable.dp5,
                        R.drawable.dp1,
                        R.drawable.dp2,
                        R.drawable.dp3,
                        R.drawable.dp4,
                        R.drawable.dp5
                };


        public WallAdapter(Context context)
        {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.wall_list_model , parent , false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            holder.image.setImageResource(images[position]);

        }

        @Override
        public int getItemCount() {
            return images.length;
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            RoundedImageView image;

            public ViewHolder(View itemView) {
                super(itemView);

                image = itemView.findViewById(R.id.imageView7);

            }
        }
    }
}
