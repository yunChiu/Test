package com.example.instagram;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.instagram.model.User;
import com.google.android.material.tabs.TabLayout;

public class FollowingActivity extends AppCompatActivity {
    public static final String BundleKey_Following = "following";
    User following = new User();
    ImageView img_hd;
    TextView tv_followingId, tv_postCount, tv_followingCount, tv_followerCount, tv_fullName, tv_biography, btn_isFollowed, btn_moreFollow;
    Button btn_back, btn_notification, btn_more;
    TabLayout tabLayout;
    ViewPager2 viewPager;
    PageAdapter pageAdapter;
    Fragment[] fragments = new Fragment[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        following = (User) getIntent().getExtras().getSerializable(BundleKey_Following);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_following);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        tv_followingId = findViewById(R.id.tv_followingId);
        tv_followingId.setText(following.getUsername());
        btn_notification = findViewById(R.id.btn_notification);
        btn_more = findViewById(R.id.btn_more);

        img_hd = findViewById(R.id.img_hd);
        Glide.with(this).load(following.getProfile_pic_url_hd()).circleCrop().into(img_hd);
        tv_postCount = findViewById(R.id.tv_postCount);
        tv_postCount.setText(String.valueOf(following.getTotal_post()));
        tv_followingCount = findViewById(R.id.tv_followingCount);
        tv_followingCount.setText(setCount(following.getFollowed_by()));
        tv_followerCount = findViewById(R.id.tv_followerCount);
        tv_followerCount.setText("0");

        tv_fullName = findViewById(R.id.tv_fullName);
        if (following.getFull_name().equals("")) {
            tv_fullName.setVisibility(View.GONE);
        }else {
            tv_fullName.setText(following.getFull_name());
        }
        tv_biography = findViewById(R.id.tv_biography);
        if (following.getBiography().equals("")) {
            tv_biography.setVisibility(View.GONE);
        }else {
            tv_biography.setText(following.getBiography());
        }

        btn_isFollowed = findViewById(R.id.btn_isFollowed);
        btn_moreFollow = findViewById(R.id.btn_moreFollow);

        viewPager = findViewById(R.id.viewPager);
        pageAdapter = new PageAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pageAdapter);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.grid));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tag));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private String setCount(int count){
        if (count > 1000.0) {
            return Math.round(count/1000.0)/10.0 + "K";
        }
        else {
            return String.valueOf(count);
        }
    }

    public class PageAdapter extends FragmentStateAdapter {

        public PageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments[position];
        }

        @Override
        public int getItemCount() {
            return fragments.length;
        }
    }
}
