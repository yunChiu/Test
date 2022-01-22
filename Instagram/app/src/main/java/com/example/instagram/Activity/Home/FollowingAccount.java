package com.example.instagram.Activity.Home;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.instagram.Activity.Home.Fragment.Grid;
import com.example.instagram.Activity.Home.Fragment.Message;
import com.example.instagram.Activity.Home.Fragment.Node;
import com.example.instagram.Activity.MainActivity;
import com.example.instagram.Dialog.DialogList;
import com.example.instagram.Dialog.OnDialogClickListener;
import com.example.instagram.R;
import com.example.instagram.SetElements;
import com.example.instagram.model.Edges;
import com.example.instagram.model.User;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class FollowingAccount extends MainActivity {

    public static final String BUNDLE_KEY_PROFILE = "BUNDLE_KEY_PROFILE";
    User user = new User();
    Button btn_back, btn_notification, btn_more, btn_moreFollow;
    TextView tv_userName, tv_postCount, tv_followerCount, tv_followingCount,
            tv_fullName, tv_biography;
    ImageView img_hd;

    TabLayout tabLayout;
    ViewPager2 viewPager;
    PagerAdapter pagerAdapter;

    View nav_home, nav_search, nav_add, nav_shop, nav_user;

    int pages = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            user = (User) getIntent().getExtras().getSerializable(BUNDLE_KEY_PROFILE);
        } catch (Exception e) {
            Log.e("Intent", e.getMessage());
        }
        if (user.getEdge_felix_video_timeline().getEdges().getNodeList().size() == 0)
            pages = 2;
        else
            pages = 3;
        initView();
    }

    public void initView(){
        setContentView(R.layout.activity_following_account);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(onClickListener);
        tv_userName = findViewById(R.id.tv_userName);
        tv_userName.setText(user.getUsername());
        btn_notification = findViewById(R.id.btn_notification);
        btn_more = findViewById(R.id.btn_more);
        btn_more.setOnClickListener(onClickListener);
        img_hd = findViewById(R.id.img_hd);
        Glide.with(FollowingAccount.this).load(user.getProfile_pic_url_hd()).circleCrop().into(img_hd);
        tv_postCount = findViewById(R.id.tv_postCount);
        tv_postCount.setText(String.valueOf(user.getEdge_owner_to_timeline_media().getCount()));
        tv_followerCount = findViewById(R.id.tv_followerCount);
        tv_followerCount.setText(String.valueOf(user.getEdge_followed_by().getCount()));
        tv_followingCount = findViewById(R.id.tv_followingCount);
        tv_followingCount.setText(String.valueOf(user.getEdge_follow().getCount()));
        tv_fullName = findViewById(R.id.tv_fullName);
        SetElements.setProfile(user.getFull_name(), tv_fullName);
        tv_biography = findViewById(R.id.tv_biography);
        SetElements.setProfile(user.getBiography(), tv_biography);
        btn_moreFollow = findViewById(R.id.btn_moreFollow);
        btn_moreFollow.setOnClickListener(onClickListener);

        viewPager = findViewById(R.id.viewPager);
        pagerAdapter = new PagerAdapter(FollowingAccount.this);
        viewPager.setAdapter(pagerAdapter);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.grid));
        if (pages == 3)
            tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.play_unselected));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tag_unselected));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    //post
                    tab.setIcon(R.drawable.grid);
                } else if (tab.getPosition() == (pages-1)){
                    //tag
                    tab.setIcon(R.drawable.tag);
                } else {
                    //video
                    tab.setIcon(R.drawable.play);
                }
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    //post
                    tab.setIcon(R.drawable.grid_unselected);
                } else if (tab.getPosition() == (pages-1)){
                    //tag
                    tab.setIcon(R.drawable.tag_unselected);
                } else {
                    //video
                    tab.setIcon(R.drawable.play_unselected);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        viewPager.setLayoutParams(SetElements.getGridHeight(FollowingAccount.this,
                Math.max(user.getEdge_felix_video_timeline().getEdges().getNodeList().size(),
                        user.getEdge_owner_to_timeline_media().getEdges().getNodeList().size())));
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

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_back:
                    onBackPressed();
                    break;
                case R.id.btn_more:
                    showDialogList_more(FollowingAccount.this);
                    break;
                case R.id.btn_moreFollow:
                    break;
                default:
                    break;
            }
        }
    };

    public class PagerAdapter extends FragmentStateAdapter {

        public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                //post
                return new Grid(user, "post");
            } else if (position == 1){
                //video
                return new Grid(user, "video");
            } else {
                //tag
                return new Grid(user, "tag");
            }
        }

        @Override
        public int getItemCount() {
            return pages;
        }
    }

    public void showDialogList_more(final Context context) {
        ArrayList<String> menu = new ArrayList<>();
        menu.add("檢舉");
        menu.add("封鎖");
        menu.add("關於這個帳號");
        menu.add("限制");
        menu.add("隱藏限時動態");
        menu.add("複製個人檔案網址");
        menu.add("分享此個人檔案");
        final DialogList dialogList = new DialogList(context, menu, new OnDialogClickListener() {
            @Override
            public void onDialogClick(int action) {
                switch (action){
                    case 0:
                        //檢舉
                        break;
                    case 1:
                        //封鎖
                        break;
                    case 2:
                        //關於這個帳號
                        break;
                    case 3:
                        //限制
                        break;
                    case 4:
                        //隱藏限時動態
                        break;
                    case 5:
                        //複製個人檔案網址
                        break;
                    case 6:
                        //分享此個人檔案
                        break;
                    default:
                        break;
                }
            }
        });
        dialogList.show();
    }
}
