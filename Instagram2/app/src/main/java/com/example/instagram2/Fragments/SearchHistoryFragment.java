package com.example.instagram2.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.instagram2.R;
import com.example.instagram2.SaveData;

import java.util.ArrayList;

public class SearchHistoryFragment extends Fragment {
    TextView tv_delAll;
    RecyclerView rv_history;
    HistoryAdapter historyAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_searchhistory, container, false);
        Button btn_back = view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        tv_delAll = view.findViewById(R.id.tv_delAll);
        tv_delAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SaveData.delAllSearchHistory(getActivity());
            }
        });
        rv_history = view.findViewById(R.id.rv_history);
        historyAdapter = new HistoryAdapter(SaveData.getAllHistory(getActivity()));
        rv_history.setAdapter(historyAdapter);
        rv_history.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
        ArrayList<String> history = null;

        public HistoryAdapter(ArrayList<String> history) {
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
            /*
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
            */
            holder.img_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SaveData.delSearchHistory(getActivity(),position);
                    historyAdapter.notifyDataSetChanged();
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
