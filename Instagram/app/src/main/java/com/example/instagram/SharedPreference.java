package com.example.instagram;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.instagram.model.RandomImg;
import com.example.instagram.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SharedPreference {

    private static SharedPreferences sharedPreferences = null;

    public static SharedPreferences getSharedPreferences(Context context) {
        sharedPreferences = context.getSharedPreferences("saveData", MODE_PRIVATE);
        return sharedPreferences;
    }

    //新增追蹤帳號
    public static void setFollowing(Context context, String id){

    }

    //取得追蹤帳號
    public static ArrayList<String> getFollowing(Context context){
        String following = getSharedPreferences(context).getString("following","");
        ArrayList<String> returnList = new ArrayList<>();
        if (following.isEmpty()) {
            //預設三個追蹤者
            returnList.add("mei.mei888");
            returnList.add("eatlife_c");
            returnList.add("rimazeidan51");
            getSharedPreferences(context).edit().putString("following", returnList.toString()).apply();
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

    //新增追蹤帳號資料
    public static void setUserJsonData(Context context, String userListStr){
        getSharedPreferences(context).edit().putString("userListJsonData", userListStr).apply();
    }
    //取得追蹤帳號資料
    public static ArrayList<User> getUserJsonData(Context context){
        String userListStr = getSharedPreferences(context).getString("userListJsonData","");
        if (!userListStr.isEmpty()) {
            try {
                ArrayList<User> returnList = new ArrayList<>();
                JSONArray jsonArray = new JSONArray(userListStr);
                for (int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                    User user = IG_JsonData.getUserData(jsonObject.toString());
                    returnList.add(user);
                }
                return returnList;
            } catch (JSONException e) {
                Log.e("SharedPreference","getUserJsonData" + e.getMessage());
                return null;
            }
        } else
            return null;
    }

    public static void delUserJsonData(Context context){
        getSharedPreferences(context).edit().putString("userListJsonData", "").apply();
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
        getSharedPreferences(context).edit().putString("history", newHistory.toString()).apply();
    }
    //查看前五筆搜尋紀錄
    public static ArrayList<String> getTop5History(Context context) {
        ArrayList<String> history = getAllHistory(context);
        if (history.size() > 5) {
            ArrayList<String> returnList = new ArrayList<>(history.subList(0, 5));
            Log.e("Top5",returnList.toString());
            return returnList;
        }else {
            return history;
        }
    }
    //查看所有搜尋紀錄
    public static ArrayList<String> getAllHistory(Context context) {
        String history = getSharedPreferences(context).getString("history","");
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
        Log.e("delHistory", history.get(position));
        history.remove(position);
        getSharedPreferences(context).edit().putString("history", history.toString()).apply();
    }
    //刪除所有搜尋紀錄
    public static void delAllSearchHistory(Context context) {
        getSharedPreferences(context).edit().putString("history", "").apply();
    }

    //按愛心
    public static void like(Context context, String shortcode){
        ArrayList<String> likedPostList = getLikedPost(context);
        if (!likedPostList.contains(shortcode))
            likedPostList.add(shortcode);
        Log.e("Liked", likedPostList.toString());
        getSharedPreferences(context).edit().putString("likedPost", likedPostList.toString()).apply();
    }
    //取消愛心
    public static void disLike(Context context, String shortcode){
        ArrayList<String> likedPostList = getLikedPost(context);
        for (String s : likedPostList) {
            if (shortcode.equals(s)) {
                likedPostList.remove(shortcode);
            }
        }
        Log.e("Dislike", likedPostList.toString());
        getSharedPreferences(context).edit().putString("likedPost", likedPostList.toString()).apply();
    }
    //已按愛心的列表
    public static ArrayList<String> getLikedPost(Context context){
        String likedPost = getSharedPreferences(context).getString("likedPost","");
        ArrayList<String> returnList = new ArrayList<>();
        if (!likedPost.isEmpty()){
            try {
                JSONArray jsonArray = new JSONArray(likedPost);
                for (int i=0; i<jsonArray.length(); i++){
                    returnList.add((String) jsonArray.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return returnList;
    }
    //清除所有愛心
    public static void clearLikePost(Context context){
        getSharedPreferences(context).edit().putString("likedPost", "").apply();
    }

    //新增收藏
    public static void keep(Context context, String shortcode){
        ArrayList<String> keepPostList = getKeepPost(context);
        keepPostList.add(shortcode);
        Log.e("keep", keepPostList.toString());
        getSharedPreferences(context).edit().putString("keepPost", keepPostList.toString()).apply();
    }
    //取消收藏
    public static void cancelKeep(Context context, String shortcode){
        ArrayList<String> keepPostList = getKeepPost(context);
        for (String s : keepPostList) {
            if (shortcode.equals(s)) {
                keepPostList.remove(shortcode);
            }
        }
        Log.e("cancelKeep", keepPostList.toString());
        getSharedPreferences(context).edit().putString("keepPost", keepPostList.toString()).apply();
    }
    //收藏列表
    public static ArrayList<String> getKeepPost(Context context){
        String keepPost = getSharedPreferences(context).getString("keepPost","");
        ArrayList<String> returnList = new ArrayList<>();
        if (!keepPost.isEmpty()) {
            try {
                JSONArray jsonArray = new JSONArray(keepPost);
                for (int i=0; i<jsonArray.length(); i++){
                    returnList.add((String) jsonArray.get(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return returnList;
    }
    //清除所有收藏
    public static void clearKeepPost(Context context){
        getSharedPreferences(context).edit().putString("keepPost", "").apply();
    }

    //存搜尋假照片牆用的隨機照片
    public static void setRandomImgList(Context context, String randomImgListStr){
        getSharedPreferences(context).edit().putString("randomImgListStr", randomImgListStr).apply();
    }
    public static ArrayList<RandomImg> getRandomImgList(Context context){
        String imgListStr = getSharedPreferences(context).getString("randomImgListStr","");
        if (!imgListStr.isEmpty())
            return RandomImg_JsonData.getImgList(imgListStr);
        else
            return null;
    }
}
