package com.example.instagram;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowMetrics;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SetElements {
    public static int getDeviceWidth(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowMetrics windowMetrics = windowManager.getCurrentWindowMetrics();
        return windowMetrics.getBounds().width();
    }

    /** Node **/
    public static int getPicHeight(Context context, int width, int height) {
        int deviceWidth = getDeviceWidth(context);
        int h = (deviceWidth * height) / width;
        return h;
    }

    public static void setLikeCount(int count, TextView textView) {
        if (count == 0)
            textView.setVisibility(View.GONE);
        else
            textView.setText(String.format("%s個讚", count));
    }

    public static void setText(String name, String text, TextView textView) {
        StyleSpan bold = new StyleSpan(android.graphics.Typeface.BOLD);
        String userMsg = String.format("%s %s", name, text);
        SpannableString userMsgStyle = new SpannableString(userMsg);
        userMsgStyle.setSpan(bold, 0, name.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(userMsgStyle);
    }

    public static void setCommentCount(int count, TextView textView) {
        if (count == 0)
            textView.setVisibility(View.GONE);
        else
            textView.setText(String.format("查看全部%s則留言", count));
    }


    /** Profile **/
    public static void setProfile(String data, TextView textView) {
        if (data.isEmpty())
            textView.setVisibility(View.GONE);
        else
            textView.setText(data);
    }


    /** Grid **/
    public static LinearLayout.LayoutParams getGridHeight(Context context, int nodeCount){
        int width = getDeviceWidth(context);
        double perHeight = (width - 4*2) / 3.0 ;
        int count = (int) Math.ceil(nodeCount / 3.0);
        int totalMargin = (count - 1) * 4;
        int height = (int) Math.round(perHeight * count + totalMargin);
        return new LinearLayout.LayoutParams(width, height);
    }

    public static ViewGroup.LayoutParams setGridItemMargin(int position, int nodeCount){
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if(position+1 == nodeCount) {
            //最後一個
            params.setMargins(0,0,0,0);
        }else if ((position+1)%3 == 0) {
            //右邊直排
            params.setMargins(0,0,0,4);
        }else if ((position+1) > nodeCount-3) {
            //下一橫排沒東西
            params.setMargins(0,0,4,0);
        }else {
            params.setMargins(0,0,4,4);
        }
        return params;
    }
}
