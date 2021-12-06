package com.example.instagram.Activity.Home;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.instagram.R;
import com.example.instagram.SharedPreference;

import java.util.ArrayList;

public class FollowingAccount extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        initView();
    }

    private void initView(){
        setContentView(R.layout.activity_following_account);
    }
}
