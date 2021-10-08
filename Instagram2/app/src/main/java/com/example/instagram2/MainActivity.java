package com.example.instagram2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.instagram2.Fragments.Main.AddFragment;
import com.example.instagram2.Fragments.Main.HomeFragment;
import com.example.instagram2.Fragments.Main.SearchFragment;
import com.example.instagram2.Fragments.Main.ShopFragment;
import com.example.instagram2.Fragments.Main.UserFragment;
import com.example.instagram2.Model.MyNode;
import com.example.instagram2.Model.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;
    FragmentManager fragmentManager = getSupportFragmentManager();

    Fragment home = new HomeFragment();
    Fragment search = new SearchFragment();
    Fragment add = new AddFragment();
    Fragment shop = new ShopFragment();
    Fragment user = new UserFragment();

    ArrayList<User> users = new ArrayList<>(); //所有追蹤中的帳號

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initFragment();
    }

    private void initView(){
        setContentView(R.layout.activity_main);
        frameLayout = findViewById(R.id.container);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    private void initFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.container, home, "home");
        fragmentTransaction.add(R.id.container, search,"search");
        fragmentTransaction.add(R.id.container, add,"add");
        fragmentTransaction.add(R.id.container, shop,"shop");
        fragmentTransaction.add(R.id.container, user,"user");
        fragmentTransaction.hide(search);
        fragmentTransaction.hide(add);
        fragmentTransaction.hide(shop);
        fragmentTransaction.hide(user);
        fragmentTransaction.commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    setFragment("home");
                    break;
                case R.id.search:
                    setFragment("search");
                    break;
                case R.id.add:
                    setFragment("add");
                    break;
                case R.id.shop:
                     setFragment("shop");
                    break;
                case R.id.user:
                    setFragment("user");
                    break;
                default:
                    Log.e("MainActivity","onNavigationItemSelectedError");
                    break;
            }
            return false;
        }
    };

    private void setFragment(String tag) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (Fragment fragment : fragmentManager.getFragments()) {
            if (fragment.getTag().equals("home") || fragment.getTag().equals("search") || fragment.getTag().equals("add") ||
                    fragment.getTag().equals("shop") || fragment.getTag().equals("user") || fragment.getTag().equals("com.bumptech.glide.manager")) {
                fragmentTransaction.hide(fragment);
            }else {
                fragmentTransaction.remove(fragment);
            }
        }
        fragmentTransaction.show(fragmentManager.findFragmentByTag(tag));
        fragmentTransaction.commit();
    }

    public void getData(String data){
        try {
            User user = new User();
            JSONObject jsonObject = new JSONObject(data);
            JSONObject jsonObjectTemp1 = jsonObject.getJSONObject("graphql").getJSONObject("user");
            user.setBiography(jsonObjectTemp1.getString("biography"));//自我介紹
            user.setExternal_url(jsonObjectTemp1.getString("external_url"));//個人連結(沒用到)
            user.setFollowed_by(jsonObjectTemp1.getJSONObject("edge_followed_by").getInt("count"));//追蹤數
            user.setFull_name(jsonObjectTemp1.getString("full_name"));//名字
            user.setProfile_pic_url_hd(jsonObjectTemp1.getString("profile_pic_url_hd"));//頭像*
            user.setUsername(jsonObjectTemp1.getString("username"));//帳號名稱*
            user.setTotal_post(jsonObjectTemp1.getJSONObject("edge_owner_to_timeline_media").getInt("count"));//貼文數量
            //貼文
            ArrayList<MyNode> myNodeList = new ArrayList<>();
            for (int i=0; i<jsonObjectTemp1.getJSONObject("edge_owner_to_timeline_media").getJSONArray("edges").length(); i++){
                JSONObject jsonObjectTemp2 = jsonObjectTemp1.getJSONObject("edge_owner_to_timeline_media").getJSONArray("edges").getJSONObject(i).getJSONObject("node");
                String display_url = jsonObjectTemp2.getString("display_url");
                String text = jsonObjectTemp2.getJSONObject("edge_media_to_caption").getJSONArray("edges").getJSONObject(0).getJSONObject("node").getString("text");
                int taken_at_timestamp = jsonObjectTemp2.getInt("taken_at_timestamp");
                int comment_count = jsonObjectTemp2.getJSONObject("edge_media_to_comment").getInt("count");
                int liked_by = jsonObjectTemp2.getJSONObject("edge_liked_by").getInt("count");
                //照片集
                ArrayList<String> photoUrlList = new ArrayList<>();
                if(jsonObjectTemp2.has("edge_sidecar_to_children")){
                    for (int j=0; j<jsonObjectTemp2.getJSONObject("edge_sidecar_to_children").getJSONArray("edges").length(); j++){
                        String photo = jsonObjectTemp2.getJSONObject("edge_sidecar_to_children").getJSONArray("edges").getJSONObject(j).getJSONObject("node").getString("display_url");
                        photoUrlList.add(photo);
                    }
                }else {
                    photoUrlList.add(display_url);
                }
                myNodeList.add(new MyNode(display_url, text, taken_at_timestamp, comment_count, liked_by, photoUrlList));
            }
            user.setMyNode(myNodeList);
            users.add(user);
        } catch (JSONException e) {
            Log.e("MainActivity", "getUserData-" + e.getMessage());
        }
    }
}