package com.example.instagram.Activity.Home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.instagram.Activity.Home.Fragment.Node;
import com.example.instagram.Activity.MainActivity;
import com.example.instagram.DoubleClick;
import com.example.instagram.NodeAdapter;
import com.example.instagram.OnDoubleClickListener;
import com.example.instagram.R;
import com.example.instagram.SetAnimation;
import com.example.instagram.SetElements;
import com.example.instagram.SetTime;
import com.example.instagram.SharedPreference;
import com.example.instagram.model.Edges;
import com.example.instagram.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import static com.example.instagram.Activity.Home.FollowingAccount.BUNDLE_KEY_PROFILE;

public class FollowingNode extends MainActivity {

    public static final String BUNDLE_KEY_NODE = "BUNDLE_KEY_NODE";
    User user = new User();

    Button btn_back;
    RecyclerView recyclerView;
    NodeAdapter nodeAdapter;
    int position = 0;

    View nav_home, nav_search, nav_add, nav_shop, nav_user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            user = (User) getIntent().getExtras().getSerializable(BUNDLE_KEY_NODE);
            position = getIntent().getIntExtra("position",0);
        } catch (Exception e) {
            Log.e("FollowingNodeError", e.getMessage());
        }
        initView();
    }

    public void initView(){
        setContentView(R.layout.activity_following_node);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        nodeAdapter = new NodeAdapter(FollowingNode.this, user);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(nodeAdapter);
        recyclerView.scrollToPosition(position);
        //nav
        nav_home = findViewById(R.id.ll_home);
        nav_home.setOnClickListener(onClickListener_nav);
        nav_search = findViewById(R.id.ll_search);
        nav_search.setOnClickListener(onClickListener_nav);
        nav_add = findViewById(R.id.ll_add);
        nav_add.setOnClickListener(onClickListener_nav);
        nav_shop = findViewById(R.id.ll_shop);
        nav_shop.setOnClickListener(onClickListener_nav);
        nav_user = findViewById(R.id.ll_user);
        nav_user.setOnClickListener(onClickListener_nav);
    }
}
