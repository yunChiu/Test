package com.example.instagram.Activity.Search;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.bumptech.glide.Glide;
import com.example.instagram.Activity.Home.FollowingNode;
import com.example.instagram.Activity.Home.Fragment.Grid;
import com.example.instagram.Activity.MainActivity;
import com.example.instagram.DataCallback;
import com.example.instagram.Dialog.OnDialogClickListener;
import com.example.instagram.Dialog.ShowDialog;
import com.example.instagram.OkHttpManager;
import com.example.instagram.R;
import com.example.instagram.RandomImg_JsonData;
import com.example.instagram.SetElements;
import com.example.instagram.SharedPreference;
import com.example.instagram.model.RandomImg;

import java.util.ArrayList;

import static com.example.instagram.Activity.Home.FollowingNode.BUNDLE_KEY_NODE;

public class Search extends MainActivity {

    ArrayList<RandomImg> randomImgList = new ArrayList<>();
    RecyclerView recyclerView;
    GridAdapter gridAdapter;
    TextView tv_searchBar;
    View nav_home, nav_search, nav_add, nav_shop, nav_user;
    int[] size = new int[]{8,10,15,15};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharedPreference.getRandomImgList(Search.this) == null)
            getRandomImg();
        else
            randomImgList = SharedPreference.getRandomImgList(Search.this);
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_search);
        recyclerView = findViewById(R.id.recyclerview);
        GridLayoutManager manager = new GridLayoutManager(Search.this,3);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position%4 == 1)
                    return 2;
                else
                    return 1;
            }
        });
        recyclerView.setLayoutManager(manager);
        gridAdapter = new GridAdapter();
        recyclerView.setAdapter(gridAdapter);
        tv_searchBar = findViewById(R.id.searchBar);
        tv_searchBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Search.this, SearchType.class));
            }
        });
        //nav
        nav_home = findViewById(R.id.ll_home);
        nav_home.setOnClickListener(onClickListener_nav);
        nav_add = findViewById(R.id.ll_add);
        nav_add.setOnClickListener(onClickListener_nav);
        nav_shop = findViewById(R.id.ll_shop);
        nav_shop.setOnClickListener(onClickListener_nav);
        nav_user = findViewById(R.id.ll_user);
        nav_user.setOnClickListener(onClickListener_nav);
    }

    private void getRandomImg() {
        String url = "https://picsum.photos/v2/list";
        OkHttpManager okHttpManager = new OkHttpManager(dataCallback);
        okHttpManager.setContext(Search.this); //設context給getUserAgent用
        okHttpManager.getStringData(url);
    }

    private DataCallback dataCallback = new DataCallback() {
        @Override
        public void onDataSuccess(String data) {
            SharedPreference.setRandomImgList(Search.this, data);
            randomImgList = RandomImg_JsonData.getImgList(data);
            gridAdapter.notifyDataSetChanged();
        }

        @Override
        public void onDataFail(String msg) {
            ShowDialog.alert(Search.this, "錯誤", msg, "返回", new OnDialogClickListener() {
                @Override
                public void onDialogClick(int action) {
                    onBackPressed();
                }
            });
        }
    };

    public class GridAdapter extends RecyclerView.Adapter<GridAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img, img_tag;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);
                img_tag = itemView.findViewById(R.id.img_tag);
                img_tag.setVisibility(View.GONE);
            }
        }

        @NonNull
        @Override
        public GridAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(Search.this).inflate(R.layout.adapter_grid, parent, false);
            return new GridAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull GridAdapter.ViewHolder holder, final int position) {
            holder.img.setLayoutParams(SetElements.setGridItemMargin(position, randomImgList.size()));
            Glide.with(Search.this)
                    .load(randomImgList.get(position).getDownload_url())
                    .override((SetElements.getDeviceWidth(Search.this)-8) / 3)
                    .centerCrop().into(holder.img);
        }

        @Override
        public int getItemCount() {
            return randomImgList.size();
        }
    }
}
