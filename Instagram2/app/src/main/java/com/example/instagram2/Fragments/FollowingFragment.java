package com.example.instagram2.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.instagram2.Model.User;
import com.example.instagram2.R;
import com.example.instagram2.SetCount;
import com.google.android.material.tabs.TabLayout;

public class FollowingFragment extends Fragment {

    User following = new User();

    ImageView img_hd;
    TextView tv_followingId, tv_postCount, tv_followerCount, tv_followingCount, tv_fullName, tv_biography, btn_isFollowed, btn_moreFollow;
    Button btn_back, btn_notification, btn_more;

    TabLayout tabLayout;
    ViewPager2 viewPager;
    PageAdapter pageAdapter;

    Fragment grid = new GridFragment();
    Fragment tag = new TagFragment();
    Fragment[] fragments = new Fragment[]{grid,tag};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //接收HomeFragment資料
        if (getArguments() != null) {
            following = (User) getArguments().getSerializable("followingData");
            //傳資料給GridFragment
            Bundle bundle = new Bundle();
            bundle.putSerializable("followingData", following);
            grid.setArguments(bundle);
        }

        View view = inflater.inflate(R.layout.fragment_following, container, false);
        btn_back = view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        //追蹤帳號
        tv_followingId = view.findViewById(R.id.tv_followingId);
        tv_followingId.setText(following.getUsername());
        btn_notification = view.findViewById(R.id.btn_notification);
        btn_more = view.findViewById(R.id.btn_more);

        //頭像
        img_hd = view.findViewById(R.id.img_hd);
        Glide.with(this).load(following.getProfile_pic_url_hd()).circleCrop().into(img_hd);
        //貼文數
        tv_postCount = view.findViewById(R.id.tv_postCount);
        //tv_postCount.setText(String.valueOf(following.getTotal_post()));
        tv_postCount.setText(SetCount.count_profile(following.getTotal_post()));
        //粉絲
        tv_followerCount = view.findViewById(R.id.tv_followerCount);
        //tv_followerCount.setText(setCount(following.getFollowed_by()));
        tv_followerCount.setText(SetCount.count_profile(following.getFollowed_by()));
        //追蹤中 api沒提供資料，預設0
        tv_followingCount = view.findViewById(R.id.tv_followingCount);
        tv_followingCount.setText("0");

        tv_fullName = view.findViewById(R.id.tv_fullName);
        if (following.getFull_name().isEmpty()) {
            tv_fullName.setVisibility(View.GONE);
        } else {
            tv_fullName.setText(following.getFull_name());
        }
        tv_biography = view.findViewById(R.id.tv_biography);
        if (following.getBiography().isEmpty()) {
            tv_biography.setVisibility(View.GONE);
        }else {
            tv_biography.setText(following.getBiography());
        }

        btn_isFollowed = view.findViewById(R.id.btn_isFollowed);
        btn_moreFollow = view.findViewById(R.id.btn_moreFollow);

        viewPager = view.findViewById(R.id.viewPager);
        pageAdapter = new PageAdapter(getActivity().getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pageAdapter);
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.grid_selected));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tag_unselected));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0)
                    tab.setIcon(R.drawable.grid_selected);
                else
                    tab.setIcon(R.drawable.tag_selected);
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0)
                    tab.setIcon(R.drawable.grid_unselected);
                else
                    tab.setIcon(R.drawable.tag_unselected);
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
        //設定viewpager的高度
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        double perHeight = (displayMetrics.widthPixels-4*2) / 3.0 ;
        int count = (int) Math.ceil(following.getMyNode().size()/3.0);
        int totalMargin = (count-1)*4;
        int height = (int) Math.round(perHeight * count + totalMargin);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        viewPager.setLayoutParams(params);
        return view;
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
