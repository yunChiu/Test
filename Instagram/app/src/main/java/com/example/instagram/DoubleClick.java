package com.example.instagram;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

public class DoubleClick implements View.OnTouchListener {
    private Context context;
    private final OnDoubleClickListener onDoubleClickListener;
    private final View.OnTouchListener onTouchListener;

    public DoubleClick(Context context, OnDoubleClickListener onDoubleClickListener, View.OnTouchListener onTouchListener){
        this.context = context;
        this.onDoubleClickListener = onDoubleClickListener;
        this.onTouchListener = onTouchListener;
    }

    private final GestureDetector gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDoubleTap(MotionEvent motionEvent) {
            onDoubleClickListener.onDoubleClick();
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }
    });

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        gestureDetector.onTouchEvent(motionEvent);
        onTouchListener.onTouch(view, motionEvent);
        return true;
    }
}
