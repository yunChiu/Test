package com.example.instagram2.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.instagram2.Model.User;
import com.example.instagram2.R;

public class GridFragment extends Fragment {
    GridView gridView;
    User followingData = new User();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null)
            followingData = (User) getArguments().getSerializable("followingData");

        View view = inflater.inflate(R.layout.fragment_post, container, false);
        gridView = view.findViewById(R.id.gridView);
        if (followingData != null)
            gridView.setAdapter(new PostAdapter());
        return view;
    }

    public class PostAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return followingData.getMyNode().size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.adapter_grid, null);
            ImageView img_post = view.findViewById(R.id.img_post);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if(position+1 == followingData.getMyNode().size()) {
                //最後一個
                params.setMargins(0,0,0,0);
            }else if ((position+1)%3 == 0) {
                //右邊直排
                params.setMargins(0,0,0,4);
            }else if ((position+1) > followingData.getMyNode().size()-3) {
                //下一橫排沒東西
                params.setMargins(0,0,4,0);
            }else {
                params.setMargins(0,0,4,4);
            }
            img_post.setLayoutParams(params);
            Glide.with(getActivity()).load(followingData.getMyNode().get(position).getDisplay_url()).override((displayMetrics.widthPixels-8)/3).centerCrop().into(img_post);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment followingPost = new FollowingPostFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("followingData", followingData);
                    bundle.putInt("position", position);
                    followingPost.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container,followingPost,"followingPost").addToBackStack("back").commit();
                }
            });
            return view;
        }
    }
}
