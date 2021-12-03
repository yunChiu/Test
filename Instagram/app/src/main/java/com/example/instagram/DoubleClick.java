package com.example.instagram;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class DoubleClick implements View.OnTouchListener {
    private Context context;
    private final OnDoubleClickListener onDoubleClickListener;

    public DoubleClick(Context context, OnDoubleClickListener onDoubleClickListener){
        this.context = context;
        this.onDoubleClickListener = onDoubleClickListener;
    }

    private final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.e("DoubleClick","onDoubleTap");
            onDoubleClickListener.onDoubleClick(1);
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    });

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        gestureDetector.onTouchEvent(event);
        return true;
    }
}
