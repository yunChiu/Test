package com.example.instagram;

import android.graphics.Bitmap;

public interface DataCallback {
    void onDataSuccess(String data);
    void onDataFail(String msg);
}
