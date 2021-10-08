package com.example.instagram2.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.instagram2.Model.Node;
import com.example.instagram2.Model.Tag;
import com.example.instagram2.R;
import com.example.instagram2.SetCount;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class SearchTagFragment extends Fragment {
    Tag tag = new Tag();

    TextView tv_tagId, tv_postCount;
    Button btn_back, btn_more;

    TabLayout tabLayout;
    ViewPager2 viewPager;
    PagerAdapter pagerAdapter;
    Fragment[] fragments = new Fragment[2];

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //接收SearchFragment資料
        if (getArguments() != null) {
            tag = (Tag) getArguments().getSerializable("searchTagData");
        }

        View view = inflater.inflate(R.layout.fragment_searchtag, container, false);
        btn_back = view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        tv_tagId = view.findViewById(R.id.tv_tagId);
        tv_tagId.setText(String.format("#%s", tag.getGraphql().getHashtag().getName()));

        btn_more = view.findViewById(R.id.btn_more);

        tv_postCount = view.findViewById(R.id.tv_postCount);
        tv_postCount.setText(SetCount.count_post(tag.getGraphql().getHashtag().getEdge_hashtag_to_media().getCount()));

        viewPager = view.findViewById(R.id.viewPager);
        pagerAdapter = new PagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
        viewPager.setLayoutParams(setViewpagerSize(tag.getGraphql().getHashtag().getEdge_hashtag_to_top_posts().getEdges().getNodeList().size()));
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("人氣"));
        tabLayout.addTab(tabLayout.newTab().setText("最近"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if (tab.getPosition() == 0) {
                    viewPager.setLayoutParams(setViewpagerSize(tag.getGraphql().getHashtag().getEdge_hashtag_to_top_posts().getEdges().getNodeList().size()));
                }else {
                    viewPager.setLayoutParams(setViewpagerSize(tag.getGraphql().getHashtag().getEdge_hashtag_to_media().getEdges().getNodeList().size()));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        return view;
    }

    private class PagerAdapter extends RecyclerView.Adapter<PagerAdapter.ViewHolder> {
        class ViewHolder extends RecyclerView.ViewHolder{
            GridView gridView;

            ViewHolder(View item){
                super(item);
                gridView = item.findViewById(R.id.gridView);
            }
        }

        @Override
        public PagerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_posttab, parent, false);
            return new PagerAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final PagerAdapter.ViewHolder holder, int position) {
            if (position == 0)
                holder.gridView.setAdapter(new PostAdapter(tag.getGraphql().getHashtag().getEdge_hashtag_to_top_posts().getEdges().getNodeList()));
            else
                holder.gridView.setAdapter(new PostAdapter(tag.getGraphql().getHashtag().getEdge_hashtag_to_media().getEdges().getNodeList()));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return fragments.length;
        }
    }

    public class PostAdapter extends BaseAdapter {
        ArrayList<Node> nodeList = new ArrayList<>();
        public PostAdapter(ArrayList<Node> nodeList) {
            this.nodeList = nodeList;
        }

        @Override
        public int getCount() {
            return nodeList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            view = getLayoutInflater().inflate(R.layout.adapter_grid, null);
            ImageView img_post = view.findViewById(R.id.img_post);
            DisplayMetrics displayMetrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if(position+1 == nodeList.size()) {
                //最後一個
                params.setMargins(0,0,0,0);
            }else if ((position+1)%3 == 0) {
                //右邊直排
                params.setMargins(0,0,0,4);
            }else if ((position+1) > nodeList.size()-3) {
                //下一橫排沒東西
                params.setMargins(0,0,4,0);
            }else {
                params.setMargins(0,0,4,4);
            }
            img_post.setLayoutParams(params);
            Glide.with(getActivity()).load(nodeList.get(position).getDisplay_url()).override((displayMetrics.widthPixels-8)/3).centerCrop().into(img_post);
            return view;
        }
    }

    public LinearLayout.LayoutParams setViewpagerSize(int postCount) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        double perHeight = (displayMetrics.widthPixels-4*2) / 3.0;
        int column = (int) Math.ceil(postCount/3.0);
        int totalMargin = (column-1)*4;
        int height = (int) Math.round(perHeight * column + totalMargin);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width,height);
        return params;
    }
}
