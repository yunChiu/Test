package com.example.instagram;

import android.util.Log;

import com.example.instagram.model.RandomImg;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RandomImg_JsonData {

    public static ArrayList<RandomImg> getImgList(String data){
        ArrayList<RandomImg> imgList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(data);
            for (int i=0; i<jsonArray.length(); i++){
                RandomImg randomImg = new RandomImg();
                randomImg.setId(jsonArray.getJSONObject(i).getString("id"));
                randomImg.setAuthor(jsonArray.getJSONObject(i).getString("author"));
                randomImg.setWidth(jsonArray.getJSONObject(i).getInt("width"));
                randomImg.setHeight(jsonArray.getJSONObject(i).getInt("height"));
                randomImg.setUrl(jsonArray.getJSONObject(i).getString("url"));
                randomImg.setDownload_url(jsonArray.getJSONObject(i).getString("download_url"));
                imgList.add(randomImg);
            }
        } catch (JSONException e) {
            Log.e("RandomImg_JsonData",e.getMessage());
        }
        return imgList;
    }
}
