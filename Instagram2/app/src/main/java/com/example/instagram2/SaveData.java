package com.example.instagram2;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SaveData {
    //新增追蹤帳號
    public static void setFollowing(Context context, String id){
        
    }
    //取得追蹤帳號
    public static ArrayList<String> getFollowing(Context context){
        String following = context.getSharedPreferences("saveData",MODE_PRIVATE).getString("following","");
        ArrayList<String> returnList = new ArrayList<>();
        if (following.isEmpty()) {
            //預設三個追蹤者
            returnList.add("mei.mei888");
            returnList.add("eatlife_c");
            returnList.add("rimazeidan51");
            context.getSharedPreferences("saveData",MODE_PRIVATE).edit().putString("following", returnList.toString()).apply();
        }else {
            try {
                JSONArray jsonArray = new JSONArray(following);
                for (int i=0; i<jsonArray.length(); i++){
                    returnList.add((String) jsonArray.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return returnList;
    }
    //刪除已追蹤帳號
    public static void delFollowing(Context context, String id){


    }

    //新增搜尋紀錄
    public static void setHistory(Context context, String newRecord) {
        ArrayList<String> oldHistory = getAllHistory(context);
        ArrayList<String> newHistory = new ArrayList<>();
        //先把最新的搜尋紀錄加入
        newHistory.add(newRecord);
        if (oldHistory.size() > 0) {
            for (String s : oldHistory) {
                //如果新紀錄與舊紀錄相同,不將舊紀錄加到newHistory
                if (!newRecord.equals(s)) {
                    newHistory.add(s);
                }
            }
        }
        Log.e("搜尋紀錄：",newHistory.toString());
        context.getSharedPreferences("saveData",MODE_PRIVATE).edit().putString("history", newHistory.toString()).apply();
    }
    //查看前五筆搜尋紀錄
    public static ArrayList<String> getTop5History(Context context) {
        ArrayList<String> history = getAllHistory(context);
        if (history.size() > 5)
            history.subList(0, 5);
        return history;
    }
    //查看所有搜尋紀錄
    public static ArrayList<String> getAllHistory(Context context) {
        String history = context.getSharedPreferences("saveData",MODE_PRIVATE).getString("history","");
        ArrayList<String> returnList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(history);
            for (int i=0; i<jsonArray.length(); i++){
                returnList.add((String) jsonArray.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnList;
    }
    //刪除特定搜尋紀錄
    public static void delSearchHistory(Context context, int position) {
        ArrayList<String> history = getAllHistory(context);
        history.remove(position);
        context.getSharedPreferences("saveData",MODE_PRIVATE).edit().putString("history", history.toString()).apply();
    }
    //刪除所有搜尋紀錄
    public static void delAllSearchHistory(Context context) {
        context.getSharedPreferences("saveData",MODE_PRIVATE).edit().putString("history", "").apply();
    }

    //按愛心
    public static void like(Context context, String shortcode){
        ArrayList<String> likedPostList = getLikedPost(context);
        if (likedPostList.size() > 0) {
            for (String s : likedPostList) {
                if (!shortcode.equals(s)) {
                    likedPostList.add(shortcode);
                }
            }
        }else {
            likedPostList.add(shortcode);
        }
        context.getSharedPreferences("saveData",MODE_PRIVATE).edit().putString("likedPost", likedPostList.toString()).apply();
    }
    //取消愛心
    public static void disLike(Context context, String shortcode){
        ArrayList<String> likedPostList = getLikedPost(context);
        for (String s : likedPostList) {
            if (!shortcode.equals(s)) {
                likedPostList.remove(shortcode);
            }
        }
        context.getSharedPreferences("saveData",MODE_PRIVATE).edit().putString("likedPost", likedPostList.toString()).apply();
    }
    //已按愛心的列表
    public static ArrayList<String> getLikedPost(Context context){
        String likedPost = context.getSharedPreferences("saveData",MODE_PRIVATE).getString("likedPost","");
        ArrayList<String> returnList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(likedPost);
            for (int i=0; i<jsonArray.length(); i++){
                returnList.add((String) jsonArray.get(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return returnList;
    }
}
