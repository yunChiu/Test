package com.example.instagram.Activity.Search;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram.BaseFragment;
import com.example.instagram.DataCallback;
import com.example.instagram.Dialog.OnDialogClickListener;
import com.example.instagram.Dialog.ShowDialog;
import com.example.instagram.OkHttpManager;
import com.example.instagram.R;
import com.example.instagram.Tag_JsonData;
import com.example.instagram.model.Tag;

public class SearchResult extends BaseFragment {

    RecyclerView recyclerView;
    ResultAdapter resultAdapter;
    View ll_searching;
    TextView tv_searching;

    int type = 0;
    String searchText = "";

    Tag tag = new Tag();

    public SearchResult(int type, String searchText){
        this.type = type;
        this.searchText = searchText;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);
        initView(view);
//        updateTab(type, searchText);
        return view;
    }

    public void initView(View view){
        ll_searching = view.findViewById(R.id.ll_searching);
        tv_searching = view.findViewById(R.id.tv_searching);

        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        resultAdapter = new ResultAdapter(tag);
        recyclerView.setAdapter(resultAdapter);
    }

    public void updateTab(int type, String searchText){
        if (!searchText.isEmpty()){
            Log.e("nowPosition","where"+type);
            switch (type){
                case 0:
                    //熱門
                    break;
                case 1:
                    //帳號
                    break;
                case 2:
                    //標籤
                    getTagData(searchText);
                    break;
                case 3:
                    //地標
                    break;
                default:
                    break;
            }
        }
    }

    private void getTagData(String tag) {
        String url = "https://www.instagram.com/explore/tags/" + tag + "/?__a=1";
        OkHttpManager okHttpManager = new OkHttpManager(dataCallback);
        okHttpManager.setContext(getContext()); //設context給getUserAgent用
        okHttpManager.getStringData(url);
        ll_searching.setVisibility(View.VISIBLE);
    }

    private DataCallback dataCallback = new DataCallback() {
        @Override
        public void onDataSuccess(String data) {
            ll_searching.setVisibility(View.GONE);
            tag = Tag_JsonData.getTagData(data);
            resultAdapter.notifyDataSetChanged();
            Log.e("tagData", data);
        }

        @Override
        public void onDataFail(String msg) {
            ll_searching.setVisibility(View.GONE);
            ShowDialog.alert(getContext(), "錯誤", msg, "確定", new OnDialogClickListener() {
                @Override
                public void onDialogClick(int action) {

                }
            });
        }
    };

    public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

        Tag tag = new Tag();

        public ResultAdapter(Tag tag){
            this.tag = tag;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tv_tagName, tv_tagCount;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_tagName = itemView.findViewById(R.id.tv_tagName);
                tv_tagCount = itemView.findViewById(R.id.tv_tagCount);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_search_result, parent, false);
            return new ResultAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.tv_tagName.setText(searchText);
            holder.tv_tagCount.setText(tag.getCount() + "則貼文");
        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
