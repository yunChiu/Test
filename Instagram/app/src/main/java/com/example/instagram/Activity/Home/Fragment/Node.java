package com.example.instagram.Activity.Home.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.instagram.Activity.Account.Account;
import com.example.instagram.Activity.Add.Add;
import com.example.instagram.Activity.Search.Search;
import com.example.instagram.Activity.Shop.Shop;
import com.example.instagram.BaseFragment;
import com.example.instagram.DataCallback;
import com.example.instagram.Dialog.OnDialogClickListener;
import com.example.instagram.Dialog.ShowDialog;
import com.example.instagram.IG_JsonData;
import com.example.instagram.MyLinearLayoutManager;
import com.example.instagram.NodeAdapter;
import com.example.instagram.OkHttpManager;
import com.example.instagram.R;
import com.example.instagram.SharedPreference;
import com.example.instagram.model.User;
import java.util.ArrayList;

public class Node extends BaseFragment {

    RecyclerView rv_story, rv_post;
    StoryAdapter storyAdapter;
    NodeAdapter nodeAdapter;
    View nav_search, nav_add, nav_shop, nav_user, progressBar;

    ArrayList<String> followedAccount = new ArrayList<>();
    ArrayList<User> userList = new ArrayList<>(); //所有追蹤中的帳號資料
    User user_show = new User(); //目前顯示的帳號

    ArrayList<String> dataList_toSP = new ArrayList<>(); //要存到SharedPreference的帳號資料表
    ArrayList<User> userListFromSP = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_node, container, false);
        initView(view);
        //從SharedPreference取出所有追蹤中的帳號及資料
        followedAccount = SharedPreference.getFollowing(getContext());
        userListFromSP = SharedPreference.getUserJsonData(getContext());
        //取第一個追蹤中的帳號
        getFollowedData(followedAccount.get(userList.size()));
        showLoading();

//        SharedPreference.clearLikePost(Home.this);
//        SharedPreference.clearKeepPost(Home.this);
        return view;
    }

    private void initView(View view) {
        progressBar = view.findViewById(R.id.ll_progressBar);
        //限時動態
        rv_story = view.findViewById(R.id.rv_story);
        storyAdapter = new StoryAdapter();
        rv_story.setAdapter(storyAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_story.setLayoutManager(linearLayoutManager);
        //貼文
        rv_post = view.findViewById(R.id.rv_post);
        nodeAdapter = new NodeAdapter(getContext(), user_show);
        rv_post.setAdapter(nodeAdapter);
        rv_post.setLayoutManager(new MyLinearLayoutManager(getActivity(),false));
        //nav
        nav_search = view.findViewById(R.id.ll_search);
        nav_search.setOnClickListener(onClickListener_nav);
        nav_add = view.findViewById(R.id.ll_add);
        nav_add.setOnClickListener(onClickListener_nav);
        nav_shop = view.findViewById(R.id.ll_shop);
        nav_shop.setOnClickListener(onClickListener_nav);
        nav_user = view.findViewById(R.id.ll_user);
        nav_user.setOnClickListener(onClickListener_nav);
    }

    public void showLoading(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public void closeLoading(){
        progressBar.setVisibility(View.GONE);
    }

    private final View.OnClickListener onClickListener_nav = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ll_search:
                    startActivity(new Intent(getActivity(), Search.class));
                    break;
                case R.id.ll_add:
                    startActivity(new Intent(getActivity(), Add.class));
                    break;
                case R.id.ll_shop:
                    startActivity(new Intent(getActivity(), Shop.class));
                    break;
                case R.id.ll_user:
                    startActivity(new Intent(getActivity(), Account.class));
                    break;
                default:
                    break;
            }
        }
    };

    private void getFollowedData(String userName) {
        String url = "https://www.instagram.com/" + userName + "/?__a=1";
        OkHttpManager okHttpManager = new OkHttpManager(dataCallback);
        okHttpManager.setContext(getActivity()); //設context給getUserAgent用
        okHttpManager.getStringData(url);
    }

    private final DataCallback dataCallback = new DataCallback() {
        @Override
        public void onDataSuccess(String data) {
            User user = IG_JsonData.getUserData(data);
            if (user == null) {
                if (userListFromSP == null || userListFromSP.isEmpty()) {
                    //API打太多次且SharedPreference無資料
                    ShowDialog.alert(getContext(), "錯誤", "API載入次數過多且無歷史資料！", "退出", new OnDialogClickListener() {
                        @Override
                        public void onDialogClick(int action) {
                            getActivity().finish();
                        }
                    });
                } else {
                    //API打太多次
                    ShowDialog.alert(getContext(), "注意", "API載入次數過多，無法更新資料，\n即將顯示歷史資料！", "確定", null);
                    closeLoading();
                    userList = userListFromSP;
                    storyAdapter.notifyDataSetChanged();
                    user_show = userList.get(0);
                    nodeAdapter.update(user_show);
                }
            }else {
                dataList_toSP.add(data);
                userList.add(user);
                if (userList.size() == followedAccount.size()) {
                    //全部載完
                    closeLoading();
                    SharedPreference.setUserJsonData(getContext(), dataList_toSP.toString());
                    storyAdapter.notifyDataSetChanged();
                    user_show = userList.get(0);
                    nodeAdapter.update(user_show);
                } else {
                    //繼續載下一筆資料
                    getFollowedData(followedAccount.get(userList.size()));
                }
            }
        }

        @Override
        public void onDataFail(String msg) {
            ShowDialog.alert(getActivity(), "錯誤", msg, "退出", new OnDialogClickListener() {
                @Override
                public void onDialogClick(int action) {
                    getActivity().finish();
                }
            });
        }
    };

    //限時動態adapter
    private class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder {
            View ly_cell;
            ImageView img_hd;
            TextView tv_name;
            ViewHolder(View item) {
                super(item);
                ly_cell = item.findViewById(R.id.ll_cell);
                img_hd = item.findViewById(R.id.img_hd);
                tv_name = item.findViewById(R.id.tv_name);
            }
        }

        @Override
        public StoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_story, parent, false);
            return new StoryAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StoryAdapter.ViewHolder holder, final int position) {
            Glide.with(getActivity()).load(userList.get(position).getProfile_pic_url_hd()).circleCrop().into(holder.img_hd);
            holder.tv_name.setText(userList.get(position).getUsername());
            holder.ly_cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    user_show = userList.get(position);
                    nodeAdapter.update(user_show);
                }
            });
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }
    }
}
