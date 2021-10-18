package com.example.instagram.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.instagram.R;

public class HomeFollowingPost extends Fragment {
    GridView gridView;
    String[] test = new String[]{"a","b","c","d","e","f","g"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homefollowingpost, container, false);
        gridView = view.findViewById(R.id.gridView);
        gridView.setAdapter(new TestAdapter());
        return view;
    }

    public class TestAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return test.length;
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
        public View getView(int position, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.adapter_grid, null);
            ImageView img_post = view.findViewById(R.id.img_post);
            //Glide.with(getActivity()).load().into(img_post);
            return view;
        }
    }
}
