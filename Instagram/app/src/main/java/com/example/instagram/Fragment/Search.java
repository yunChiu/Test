package com.example.instagram.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.instagram.BaseActivity;
import com.example.instagram.R;

public class Search extends Fragment {

    TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        textView = view.findViewById(R.id.test);
        return view;
    }
}
