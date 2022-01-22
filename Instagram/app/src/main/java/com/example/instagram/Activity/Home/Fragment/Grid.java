package com.example.instagram.Activity.Home.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.instagram.Activity.Home.FollowingAccount;
import com.example.instagram.Activity.Home.FollowingNode;
import com.example.instagram.BaseFragment;
import com.example.instagram.R;
import com.example.instagram.SetElements;
import com.example.instagram.model.Edges;
import com.example.instagram.model.User;

import static com.example.instagram.Activity.Home.FollowingAccount.BUNDLE_KEY_PROFILE;
import static com.example.instagram.Activity.Home.FollowingNode.BUNDLE_KEY_NODE;

public class Grid extends BaseFragment {

    View ll_bg;
    RecyclerView recyclerView;
    NodeAdapter nodeAdapter;
    User user;
    Edges edges;

    public Grid(User user, String tab){
        this.user = user;
        if (tab.equals("post")){
            this.edges = user.getEdge_owner_to_timeline_media().getEdges();
        }else if (tab.equals("video")){
            this.edges = user.getEdge_felix_video_timeline().getEdges();
        }else {
            this.edges = new Edges();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_grid, container, false);
        initView(view);
        return view;
    }

    private void initView(View view){
        ll_bg = view.findViewById(R.id.ll_bg);
        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        nodeAdapter = new NodeAdapter();
        recyclerView.setAdapter(nodeAdapter);

        if (!edges.getNodeList().isEmpty()) {
            ll_bg.setVisibility(View.GONE);
        }
    }

    public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.ViewHolder> {

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.img);
            }
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_grid, parent, false);
            return new NodeAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.img.setLayoutParams(SetElements.setGridItemMargin(position, edges.getNodeList().size()));
            Glide.with(getActivity())
                    .load(edges.getNodeList().get(position).getDisplay_url())
                    .override((SetElements.getDeviceWidth(getActivity())-8) / 3)
                    .centerCrop().into(holder.img);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), FollowingNode.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BUNDLE_KEY_NODE, user);
                    intent.putExtras(bundle);
                    intent.putExtra("position",position);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return edges.getNodeList().size();
        }
    }
}
