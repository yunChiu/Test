package com.example.instagram.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.instagram.R;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_main);

    }
}