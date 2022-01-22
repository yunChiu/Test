package com.example.instagram.Activity.Search;

import android.content.Context;
import android.icu.text.Edits;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.adapter.FragmentViewHolder;
import androidx.viewpager2.widget.ViewPager2;

import com.example.instagram.Activity.MainActivity;
import com.example.instagram.DataCallback;
import com.example.instagram.Dialog.OnDialogClickListener;
import com.example.instagram.Dialog.ShowDialog;
import com.example.instagram.OkHttpManager;
import com.example.instagram.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

import static com.example.instagram.Tag_JsonData.getTagData;

public class SearchType extends MainActivity {

    ImageView btn_back;
    TextView tv_loading, tv_noResult;
    EditText et_searchText;

    TabLayout tabLayout;
    ViewPager2 viewPager;
    PagerAdapter pagerAdapter;

    String searchText = "";

    SearchResult searchResult = new SearchResult();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    public void initView(){
        setContentView(R.layout.activity_search_type);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        et_searchText = findViewById(R.id.searchText);
        et_searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_SEARCH){
                    searchText = et_searchText.getText().toString();
                    //關鍵盤
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(et_searchText.getWindowToken(),0);
                    //更新當前Tab資料
                    searchResult.updateTab(tabLayout.getSelectedTabPosition(), searchText);
                }
                return false;
            }
        });
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("熱門"));
        tabLayout.addTab(tabLayout.newTab().setText("帳號"));
        tabLayout.addTab(tabLayout.newTab().setText("標籤"));
        tabLayout.addTab(tabLayout.newTab().setText("地標"));
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
        pagerAdapter = new PagerAdapter(SearchType.this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    public class PagerAdapter extends FragmentStateAdapter {

        public PagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            searchResult = new SearchResult();
            return searchResult;
        }

        @Override
        public int getItemCount() {
            return 4;
        }
    }
}
