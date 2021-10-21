package com.example.instagram;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.webkit.WebSettings;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class OkHttpManager {
    private DataCallback dataCallback;
    private Context context;

    public OkHttpManager(DataCallback dataCallback){
        this.dataCallback = dataCallback;
    }
    public void setContext(Context context){
        this.context = context;
    }
    public Context getContext(){
        return context;
    }

    private String getUserAgent(){
        String userAgent = "";
        try {
            userAgent = WebSettings.getDefaultUserAgent(getContext());
        } catch (Exception e) {
            userAgent = System.getProperty("http.agent");
        }
        return userAgent;
    }

    public void getStringData(final String url){
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url(url)
                        .removeHeader("User-Agent")
                        .addHeader("User-Agent",getUserAgent())
                        .build();
                Call call = client.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull IOException e) {
                        Message msg = new Message();
                        msg.what = 0;
                        msg.obj = e.getMessage();
                        handler.sendMessage(msg);
                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                        Message msg = new Message();
                        msg.what = 1;
                        msg.obj = response.body().string();
                        handler.sendMessage(msg);
                    }
                });
            }
        }).start();
    }

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0){
                dataCallback.onDataFail(msg.obj.toString());
            }else {
                dataCallback.onDataSuccess(msg.obj.toString());
            }
        }
    };
}
