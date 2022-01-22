package com.example.instagram;

import android.app.Application;

public class MyApplication extends Application {
    private static MyApplication instance;

    public static MyApplication getInstance(){
        if (instance == null)
            instance = new MyApplication();
        return instance;
    }
}
