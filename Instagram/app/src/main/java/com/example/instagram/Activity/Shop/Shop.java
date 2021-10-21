package com.example.instagram.Activity.Shop;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.instagram.R;

public class Shop extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_shop);


    }
}
