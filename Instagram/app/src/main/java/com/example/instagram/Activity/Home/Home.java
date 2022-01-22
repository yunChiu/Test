package com.example.instagram.Activity.Home;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.instagram.Activity.Home.Fragment.Message;
import com.example.instagram.Activity.Home.Fragment.Node;
import com.example.instagram.Activity.Home.Fragment.TakePic;
import com.example.instagram.R;

public class Home extends AppCompatActivity {

    private ViewPager2 pager;
    private PagerAdapter pagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_home);
        pager = findViewById(R.id.pager);
        pagerAdapter = new PagerAdapter(Home.this);
        pager.setAdapter(pagerAdapter);
        pager.setCurrentItem(1);
    }

    private class PagerAdapter extends FragmentStateAdapter {

        public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            if (position == 0) {
                return new TakePic();
            } else if (position == 1) {
                return new Node();
            } else {
                return new Message();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}
