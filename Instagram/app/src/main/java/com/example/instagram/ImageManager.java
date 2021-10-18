package com.example.instagram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageManager {
    private ImageCallback imageCallback;
    private Context context;

    public void setContext(Context context){
        this.context = context;
    }
    public Context getContext(){
        return context;
    }

    public ImageManager(ImageCallback imageCallback) {
        this.imageCallback = imageCallback;
    }

    public void getImage(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    DisplayMetrics displayMetrics = new DisplayMetrics();
                    WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
                    windowManager.getDefaultDisplay().getMetrics(displayMetrics);
                    Bitmap bitmap = Glide.with(context).asBitmap().load(url).submit(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT).get();
                    Message msg = new Message();
                    msg.obj = bitmap;
                    handler.sendMessage(msg);
                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            imageCallback.onImageFinished(msg.obj);
        }
    };
}
