package com.example.instagram2.Fragments.Main;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.instagram2.DataCallback;
import com.example.instagram2.Fragments.SearchTagFragment;
import com.example.instagram2.Model.Node;
import com.example.instagram2.Model.Tag;
import com.example.instagram2.OkHttpManager;
import com.example.instagram2.R;
import com.example.instagram2.SaveData;
import com.example.instagram2.SetCount;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragment extends Fragment {

    ImageView btn_back, btn_map;
    TextView searchBarFake, tv_loading, tv_noResult;
    EditText searchBarReal;
    GridView gridView;
    View ll_tab, ll_loading, ll_locationDefault;

    TabLayout tabLayout;
    ViewPager2 viewPager;
    PagerAdapter pagerAdapter;

    Fragment[] fragments = new Fragment[4];

    String imgApi = "https://source.unsplash.com/900x1600/?travel"; //隨機圖片api
    int imgCount = 10; //照片牆圖片數量
    Tag result = new Tag();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        btn_back = view.findViewById(R.id.btn_back);
        searchBarFake = view.findViewById(R.id.search_bar_fake);
        searchBarReal = view.findViewById(R.id.search_bar_real);
        btn_map = view.findViewById(R.id.btn_map);

        gridView = view.findViewById(R.id.gridView);
        ll_tab = view.findViewById(R.id.ll_tab);

        searchBarFake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_back.setVisibility(View.VISIBLE);
                searchBarReal.setVisibility(View.VISIBLE);
                searchBarFake.setVisibility(View.GONE);
                btn_map.setVisibility(View.GONE);
                gridView.setVisibility(View.GONE);
                ll_tab.setVisibility(View.VISIBLE);
            }
        });
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_back.setVisibility(View.GONE);
                searchBarReal.setVisibility(View.GONE);
                searchBarFake.setVisibility(View.VISIBLE);
                btn_map.setVisibility(View.VISIBLE);
                gridView.setVisibility(View.VISIBLE);
                ll_tab.setVisibility(View.GONE);
            }
        });
        searchBarReal.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == EditorInfo.IME_ACTION_DONE){
                    if (!searchBarReal.getText().toString().isEmpty()) {
                        startSearch(searchBarReal.getText().toString());
                    }else {
                        pagerAdapter.notifyDataSetChanged();
                    }
                }
                return false;
            }
        });

        gridView.setAdapter(new GridAdapter());

        viewPager = view.findViewById(R.id.viewPager);
        pagerAdapter = new PagerAdapter();
        viewPager.setAdapter(pagerAdapter);
        tabLayout = view.findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("熱門"));
        tabLayout.addTab(tabLayout.newTab().setText("帳號"));
        tabLayout.addTab(tabLayout.newTab().setText("標籤"));
        tabLayout.addTab(tabLayout.newTab().setText("地標"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    case 0:
                        searchBarReal.setHint("搜尋");
                        break;
                    case 1:
                        searchBarReal.setHint("搜尋帳號");
                        break;
                    case 2:
                        searchBarReal.setHint("搜尋主題標籤（#）");
                        break;
                    case 3:
                        searchBarReal.setHint("搜尋地標");
                        break;
                    default:
                        Log.e("SearchFragment","onTabSelectedError");
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

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
        ll_loading = view.findViewById(R.id.ll_loading);
        tv_loading = view.findViewById(R.id.tv_loading);
        tv_noResult = view.findViewById(R.id.tv_noResult);
        ll_locationDefault = view.findViewById(R.id.ll_locationDefault);
        return view;
    }

    public void getData(String tag) {
        String url = "https://www.instagram.com/explore/tags/" + tag + "/?__a=1";
        OkHttpManager okHttpManager = new OkHttpManager(dataCallback);
        okHttpManager.setContext(getActivity()); //設context給getUserAgent用
        okHttpManager.getStringData(url);
    }

    private DataCallback dataCallback = new DataCallback() {
        @Override
        public void onDataSuccess(String data) {
            ll_loading.setVisibility(View.GONE);
            if (data.equals("{}")) {
                tv_noResult.setVisibility(View.VISIBLE);
                tv_noResult.setText("查無「" + searchBarReal.getText().toString() + "」的結果");
                result = new Tag();
            }else {
                getJsonData(data);
                SaveData.setHistory(getActivity(),searchBarReal.getText().toString());
            }
            pagerAdapter.notifyDataSetChanged();
        }

        @Override
        public void onDataFail(String msg) {
            Log.e("SearchFragment","onDataFail" + msg);
        }
    };

    public void getJsonData(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONObject jsonObject_hashTag = jsonObject.getJSONObject("graphql").getJSONObject("hashtag");
            result.getGraphql().getHashtag().setName(jsonObject_hashTag.getString("name"));
            result.getGraphql().getHashtag().getEdge_hashtag_to_media().setCount(jsonObject.getJSONObject("graphql").getJSONObject("hashtag").getJSONObject("edge_hashtag_to_media").getInt("count"));
            ArrayList<Node> nodeList_recent = new ArrayList<>(); //最新貼文
            JSONArray jsonArray_edges_recent = jsonObject_hashTag.getJSONObject("edge_hashtag_to_media").getJSONArray("edges");
            for (int i=0; i<jsonArray_edges_recent.length(); i++) {
                String display_url = jsonArray_edges_recent.getJSONObject(i).getJSONObject("node").getString("display_url");
                nodeList_recent.add(new Node(display_url));
            }
            ArrayList<Node> nodeList_top = new ArrayList<>(); //人氣貼文
            JSONArray jsonArray_edges_top = jsonObject_hashTag.getJSONObject("edge_hashtag_to_top_posts").getJSONArray("edges");
            for (int i=0; i<jsonArray_edges_top.length(); i++) {
                String display_url = jsonArray_edges_top.getJSONObject(i).getJSONObject("node").getString("display_url");
                nodeList_top.add(new Node(display_url));
            }
            result.getGraphql().getHashtag().getEdge_hashtag_to_media().getEdges().setNodeList(nodeList_recent);
            result.getGraphql().getHashtag().getEdge_hashtag_to_top_posts().getEdges().setNodeList(nodeList_top);
        } catch (JSONException e) {
            Log.e("SearchFragment", "getJsonData-" + e.getMessage());
        }
    }

    //照片牆
    public class GridAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imgCount;
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
            if(position+1 == imgCount) {
                //最後一個
                params.setMargins(0,0,0,0);
            }else if ((position+1)%3 == 0) {
                //右邊直排
                params.setMargins(0,0,0,4);
            }else if ((position+1) > imgCount-3) {
                //下一橫排沒東西
                params.setMargins(0,0,4,0);
            }else {
                params.setMargins(0,0,4,4);
            }
            img_post.setLayoutParams(params);
            Glide.with(getActivity()).load(imgApi).override((displayMetrics.widthPixels-4)/3).centerCrop().into(img_post);
            return view;
        }
    }

    public void startSearch(String text){
        ll_loading.setVisibility(View.VISIBLE);
        tv_noResult.setVisibility(View.GONE);
        tv_loading.setText(String.format("正在搜尋「%s」......", searchBarReal.getText().toString()));
        getData(text);
    }

    //搜尋結果
    public class PagerAdapter extends RecyclerView.Adapter<PagerAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder{

            View ll_loading, ll_locationDefault, ll_history;
            TextView tv_noResult, tv_loading;
            RecyclerView rv_result, rv_history;

            ViewHolder(View item){
                super(item);
                //ll_loading = item.findViewById(R.id.ll_loading);
                ll_locationDefault = item.findViewById(R.id.ll_locationDefault);
                ll_history = item.findViewById(R.id.ll_history);
                //tv_loading = item.findViewById(R.id.tv_loading);
                //tv_noResult = item.findViewById(R.id.tv_noResult);
                rv_result = item.findViewById(R.id.rv_result);
                rv_result.setLayoutManager(new LinearLayoutManager(getActivity()));
                rv_history = item.findViewById(R.id.rv_history);
                rv_history.setLayoutManager(new LinearLayoutManager(getActivity()));
            }
        }

        @NonNull
        @Override
        public PagerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_searchtab, parent, false);
            return new PagerAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PagerAdapter.ViewHolder holder, int position) {
            ResultAdapter resultAdapter = new ResultAdapter();
            if (SaveData.getTop5History(getActivity()).size() > 0 && searchBarReal.getText().toString().isEmpty()) {
                holder.ll_history.setVisibility(View.VISIBLE);
                holder.rv_history.setAdapter(new HistoryAdapter(SaveData.getTop5History(getActivity())));
            }
            else
                holder.ll_history.setVisibility(View.GONE);
            holder.rv_result.setAdapter(resultAdapter);
            if (searchBarReal.getText().toString().isEmpty() || result.getGraphql().getHashtag().getName().isEmpty()) {
                if (position == 3)
                    holder.ll_locationDefault.setVisibility(View.VISIBLE);
                holder.rv_result.setVisibility(View.GONE);
            }else {
                holder.ll_locationDefault.setVisibility(View.GONE);
                holder.rv_result.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return fragments.length;
        }
    }

    private class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {
        class ViewHolder extends RecyclerView.ViewHolder{
            TextView tv_tagName, tv_tagCount;

            ViewHolder(View item){
                super(item);
                tv_tagName = item.findViewById(R.id.tv_tagName);
                tv_tagCount = item.findViewById(R.id.tv_tagCount);
            }
        }

        @Override
        public ResultAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_searchresult, parent, false);
            return new ResultAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ResultAdapter.ViewHolder holder, int position) {
            holder.tv_tagName.setText("#" + result.getGraphql().getHashtag().getName());
            holder.tv_tagCount.setText(SetCount.count_post(result.getGraphql().getHashtag().getEdge_hashtag_to_media().getCount()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment searchTag = new SearchTagFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("searchTagData", result);
                    searchTag.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container,searchTag,"searchTag").addToBackStack("back").commit();
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }

    private class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
        ArrayList<String> history = null;

        public HistoryAdapter(ArrayList<String>  history) {
            this.history = history;
        }

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView tv_tagName, tv_tagCount;
            ImageView img_delete;

            ViewHolder(View item){
                super(item);
                tv_tagName = item.findViewById(R.id.tv_tagName);
                //tv_tagCount = item.findViewById(R.id.tv_tagCount);
                img_delete = item.findViewById(R.id.img_delete);
            }
        }

        @Override
        public HistoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_searchhistory, parent, false);
            return new HistoryAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final HistoryAdapter.ViewHolder holder, final int position) {
            holder.tv_tagName.setText(String.format("#%s", history.get(position)));
            //holder.tv_tagCount.setText(SetCount.count_post(result.getGraphql().getHashtag().getEdge_hashtag_to_media().getCount()));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment searchTag = new SearchTagFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("searchTagData", result);
                    searchTag.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container,searchTag,"searchTag").addToBackStack("back").commit();
                }
            });
            holder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SaveData.delSearchHistory(getActivity(),position);
                    pagerAdapter.notifyDataSetChanged();
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return history.size();
        }
    }
}

