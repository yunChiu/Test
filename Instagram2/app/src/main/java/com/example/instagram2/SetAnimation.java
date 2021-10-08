package com.example.instagram2;

import android.content.Context;
import android.icu.number.Scale;
import android.util.Log;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;

public class SetAnimation {
    public AnimationSet icon() {
        ScaleAnimation zoom_in = new ScaleAnimation(0.2f,1.2f,0.2f,1.2f, ScaleAnimation.RELATIVE_TO_SELF,0.5f, ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        zoom_in.setDuration(200);
        ScaleAnimation zoom_out = new ScaleAnimation(1.2f,0.8f,1.2f,0.8f, ScaleAnimation.RELATIVE_TO_SELF,0.5f, ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        zoom_out.setDuration(350);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(zoom_in);
        animationSet.addAnimation(zoom_out);
        return animationSet;
    }

    public AnimationSet doubleClickLike(){
        ScaleAnimation zoom_in = new ScaleAnimation(0.2f,1.3f,0.2f,1.3f, ScaleAnimation.RELATIVE_TO_SELF,0.5f, ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        zoom_in.setDuration(300);
        ScaleAnimation zoom_out = new ScaleAnimation(1.3f,0.8f,1.3f,0.8f, ScaleAnimation.RELATIVE_TO_SELF,0.5f, ScaleAnimation.RELATIVE_TO_SELF,0.5f);
        zoom_out.setDuration(500);
        TranslateAnimation stop = new TranslateAnimation(0f,0f,0f,0f);
        stop.setDuration(1000);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(zoom_in);
        animationSet.addAnimation(zoom_out);
        animationSet.addAnimation(stop);
        animationSet.setFillBefore(true);
        return animationSet;
    }

    public AnimationSet keepNode() {
        TranslateAnimation slide_up = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF,0f,TranslateAnimation.RELATIVE_TO_SELF,0f,
                TranslateAnimation.RELATIVE_TO_SELF,1f,TranslateAnimation.RELATIVE_TO_SELF,0f);
        slide_up.setDuration(700);
        TranslateAnimation slide_down = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_SELF,0f,TranslateAnimation.RELATIVE_TO_SELF,0f,
                TranslateAnimation.RELATIVE_TO_SELF,0f,TranslateAnimation.RELATIVE_TO_SELF,1f);
        slide_down.setStartOffset(slide_up.getDuration() + 1200);
        slide_down.setDuration(700);

        AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(slide_up);
        animationSet.addAnimation(slide_down);
        animationSet.setFillAfter(true);
        return animationSet;
    }
}
