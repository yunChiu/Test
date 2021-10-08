package com.example.instagram2.Fragments.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.example.instagram2.Fragments.GridFragment;
import com.example.instagram2.Fragments.TagFragment;
import com.example.instagram2.Model.User;
import com.example.instagram2.R;
import com.google.android.material.tabs.TabLayout;

public class UserFragment extends Fragment {

    User user = new User();
    ImageView img_hd;
    TextView tv_userId, tv_postCount, tv_followerCount, tv_followingCount, tv_fullName, tv_biography, btn_edit, tv_storyKeepIcon;
    Button btn_lock, btn_add, btn_menu;
    View ll_storyKeep, ll_storyKeepDetail;

    TabLayout tabLayout;
    ViewPager2 viewPager;
    PageAdapter pageAdapter;

    Fragment grid = new GridFragment();
    Fragment tag = new TagFragment();
    Fragment[] fragments = new Fragment[]{grid,tag};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        btn_lock = view.findViewById(R.id.btn_lock);
        tv_userId = view.findViewById(R.id.tv_userId);
        btn_add = view.findViewById(R.id.btn_add);
        btn_menu = view.findViewById(R.id.btn_menu);

        img_hd = view.findViewById(R.id.img_hd);
        //Glide.with(this).load(following.getProfile_pic_url_hd()).circleCrop().into(img_hd);
        tv_postCount = view.findViewById(R.id.tv_postCount);
        tv_postCount.setText("0");
        tv_followerCount = view.findViewById(R.id.tv_followerCount);
        tv_followerCount.setText("0");
        tv_followingCount = view.findViewById(R.id.tv_followingCount);
        tv_followingCount.setText("0");

        tv_fullName = view.findViewById(R.id.tv_fullName);
        tv_fullName.setText("FullName");
        /*
        if (user.getFull_name().isEmpty()) {
            tv_fullName.setVisibility(View.GONE);
        }else {
            tv_fullName.setText(user.getFull_name());
        }
        */
        tv_biography = view.findViewById(R.id.tv_biography);
        if (user.getBiography().isEmpty()) {
            tv_biography.setVisibility(View.GONE);
        }else {
            tv_biography.setText(user.getBiography());
        }

        btn_edit = view.findViewById(R.id.btn_edit);

        tv_storyKeepIcon = view.findViewById(R.id.tv_storyKeepIcon);
        final boolean[] isOpened = {false};
        ll_storyKeep = view.findViewById(R.id.ll_storyKeep);
        ll_storyKeepDetail = view.findViewById(R.id.ll_storyKeepDetail);
        ll_storyKeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOpened[0] = !isOpened[0];
                if (isOpened[0]) {
                    ll_storyKeepDetail.setVisibility(View.VISIBLE);
                    tv_storyKeepIcon.setBackgroundResource(R.drawable.up);
                }
                else {
                    ll_storyKeepDetail.setVisibility(View.GONE);
                    tv_storyKeepIcon.setBackgroundResource(R.drawable.down);
                }
            }
        });

        viewPager = view.findViewById(R.id.viewPager);
        pageAdapter = new PageAdapter(getActivity().getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pageAdapter);
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.grid_selected));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.tag_selected));
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
        /*
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        double perHeight = (displayMetrics.widthPixels-4*2) / 3.0 ;
        int count = (int) Math.ceil(user.getNode().size()/3.0);
        int margin = (count-1)*4;
        int height = (int) Math.round(perHeight * count + margin);
        */
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 500);
        viewPager.setLayoutParams(params);

        return view;
    }

    private String setCount(int count){
        if (count > 1000.0) {
            return Math.round(count/100.0)/10.0 + "K";
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
