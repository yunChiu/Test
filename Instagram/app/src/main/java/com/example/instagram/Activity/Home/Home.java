package com.example.instagram.Activity.Home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.instagram.Activity.Account.Account;
import com.example.instagram.Activity.Add.Add;
import com.example.instagram.Activity.MainActivity;
import com.example.instagram.Activity.Search.Search;
import com.example.instagram.Activity.Shop.Shop;
import com.example.instagram.DataCallback;
import com.example.instagram.DoubleClick;
import com.example.instagram.IG_JsonData;
import com.example.instagram.MyLinearLayoutManager;
import com.example.instagram.OkHttpManager;
import com.example.instagram.OnDoubleClickListener;
import com.example.instagram.R;
import com.example.instagram.SetAnimation;
import com.example.instagram.SetTime;
import com.example.instagram.SharedPreference;
import com.example.instagram.model.Edges;
import com.example.instagram.model.User;

import java.util.ArrayList;

import static com.example.instagram.Activity.Home.FollowingAccount.BUNDLE_KEY_PROFILE;

public class Home extends MainActivity {
    RecyclerView rv_story, rv_post;
    StoryAdapter storyAdapter;
    NodeAdapter nodeAdapter;
    View nav_search, nav_add, nav_shop, nav_user;

    ArrayList<User> userList = new ArrayList<>(); //所有追蹤中的帳號
    int tag = 0; //目前顯示的帳號
    Edges edges = new Edges(); //目前帳號的貼文

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayList<String> followedAccount = SharedPreference.getFollowing(this);
        for (int i=0; i<followedAccount.size(); i++) {
            getFollowedData(followedAccount.get(i));
        }

//        SharedPreference.clearLikePost(Home.this);
//        SharedPreference.clearKeepPost(Home.this);
        initView();
    }

    private void initView() {
        setContentView(R.layout.activity_home);
        //限時動態
        rv_story = findViewById(R.id.rv_story);
        storyAdapter = new StoryAdapter();
        rv_story.setAdapter(storyAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rv_story.setLayoutManager(linearLayoutManager);
        //貼文
        rv_post = findViewById(R.id.rv_post);
        nodeAdapter = new NodeAdapter();
        rv_post.setAdapter(nodeAdapter);
        rv_post.setLayoutManager(new MyLinearLayoutManager(this,false));
        //nav
        nav_search = findViewById(R.id.ll_search);
        nav_search.setOnClickListener(onClickListener_nav);
        nav_add = findViewById(R.id.ll_add);
        nav_add.setOnClickListener(onClickListener_nav);
        nav_shop = findViewById(R.id.ll_shop);
        nav_shop.setOnClickListener(onClickListener_nav);
        nav_user = findViewById(R.id.ll_user);
        nav_user.setOnClickListener(onClickListener_nav);
    }

    private View.OnClickListener onClickListener_nav = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ll_search:
                    startActivity(new Intent(Home.this, Search.class));
                    break;
                case R.id.ll_add:
                    startActivity(new Intent(Home.this, Add.class));
                    break;
                case R.id.ll_shop:
                    startActivity(new Intent(Home.this, Shop.class));
                    break;
                case R.id.ll_user:
                    startActivity(new Intent(Home.this, Account.class));
                    break;
                default:
                    break;
            }
        }
    };

    private void getFollowedData(String userName) {
        String url = "https://www.instagram.com/" + userName + "/?__a=1";
        OkHttpManager okHttpManager = new OkHttpManager(dataCallback);
        okHttpManager.setContext(Home.this); //設context給getUserAgent用
        okHttpManager.getStringData(url);
    }

    private DataCallback dataCallback = new DataCallback() {
        @Override
        public void onDataSuccess(String data) {
            userList.addAll(IG_JsonData.getUserJsonData(data));
            if (userList.size() > 0) {
                storyAdapter.notifyDataSetChanged();
                updatePost(tag);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
                builder.setMessage("API打太多次").setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

        @Override
        public void onDataFail(String msg) {
            Log.e("DataCallback","onDataFail" + msg);

            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setMessage("DataCallback: " + msg).setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

    public void updatePost(int tag){
        edges = userList.get(tag).getEdge_owner_to_timeline_media().edges;
        nodeAdapter.notifyDataSetChanged();
    }

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
            View view = LayoutInflater.from(Home.this).inflate(R.layout.adapter_story, parent, false);
            return new StoryAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StoryAdapter.ViewHolder holder, final int position) {
            Glide.with(Home.this).load(userList.get(position).getProfile_pic_url_hd()).circleCrop().into(holder.img_hd);
            holder.tv_name.setText(userList.get(position).getUsername());
            holder.ly_cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tag = position;
                    updatePost(tag);
                }
            });
        }

        @Override
        public int getItemCount() {
            return userList.size();
        }
    }

    //貼文adapter
    private class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.ViewHolder> {

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_user, img_photoSmall, img_liked;
            View ll_keep;
            ViewPager2 pager_child;
            TextView tv_user, tv_userMsg, tv_likedCount, tv_commentCount, tv_time;
            Button btn_like, btn_msg, btn_share, btn_keep, btn_more;

            ViewHolder(View item){
                super(item);

                img_user = item.findViewById(R.id.img_user);
                tv_user = item.findViewById(R.id.tv_user);

                pager_child = item.findViewById(R.id.pager_photo);
                ll_keep = item.findViewById(R.id.ll_keep);
                img_photoSmall = item.findViewById(R.id.img_small);
                img_liked = item.findViewById(R.id.img_liked);

                tv_likedCount = item.findViewById(R.id.tv_likedCount);
                tv_userMsg = item.findViewById(R.id.tv_userMsg);
                tv_commentCount = item.findViewById(R.id.tv_commentCount);
                tv_time = item.findViewById(R.id.tv_time);

                btn_more = item.findViewById(R.id.btn_more);
                btn_like = item.findViewById(R.id.btn_like);
                btn_msg = item.findViewById(R.id.btn_msg);
                btn_share = item.findViewById(R.id.btn_share);
                btn_keep = item.findViewById(R.id.btn_keep);
                item.setTag(item);
            }
        }

        @Override
        public NodeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(Home.this).inflate(R.layout.adapter_node, parent, false);
            return new NodeAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final NodeAdapter.ViewHolder holder, final int position) {
            //頭貼
            Glide.with(Home.this).load(userList.get(tag).getProfile_pic_url_hd()).circleCrop().into(holder.img_user);
            //帳號
            holder.tv_user.setText(userList.get(tag).getUsername());
            holder.tv_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Home.this,FollowingAccount.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(BUNDLE_KEY_PROFILE, userList.get(tag));
                    intent.putExtras(bundle);
                    intent.putExtra("hd",userList.get(tag).getProfile_pic_url_hd());
                    intent.putExtra("userName",userList.get(tag).getUsername());
                    startActivity(intent);
                }
            });
            //更多
            holder.btn_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String copyLink = "https://www.instagram.com/p/"+
                            edges.getNodeList().get(position).getShortcode()+
                            "/?utm_medium=copy_link";
                    String shareLink = "https://www.instagram.com/p/"+
                            edges.getNodeList().get(position).getShortcode()+
                            "/?utm_medium=share_sheet";
                    showDialogBtn_more(Home.this, copyLink, shareLink);
                }
            });
            //照片
            Edges edges_child = edges.getNodeList().get(position).getEdge_sidecar_to_children();
            holder.pager_child.setAdapter(new NodeChildAdapter(edges_child));
            //讚數
            setLike(edges.getNodeList().get(position).getEdge_liked_by().getCount(), holder.tv_likedCount);
            //內容
            setText(userList.get(tag).getUsername(),
                    edges.getNodeList().get(position).getEdge_media_to_caption().getEdges_inner().getNode_inner().getText(),
                    holder.tv_userMsg);
            //留言
            setComment(edges.getNodeList().get(position).getEdge_media_to_comment().getCount(), holder.tv_commentCount);
            //發文時間
            holder.tv_time.setText(String.format("%s • ", SetTime.setPostTime(edges.getNodeList().get(position).getTaken_at_timestamp())));
            //愛心
            if (SharedPreference.getLikedPost(Home.this).contains(edges.getNodeList().get(position).getShortcode())) {
                setLike(edges.getNodeList().get(position).getEdge_liked_by().getCount()+1, holder.tv_likedCount);
                holder.btn_like.setBackgroundResource(R.drawable.like_fill);
            }
            holder.btn_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.btn_like.startAnimation(new SetAnimation().icon());
                    if (SharedPreference.getLikedPost(Home.this).contains(edges.getNodeList().get(position).getShortcode())) {
                        holder.btn_like.setBackgroundResource(R.drawable.like_border);
                        setLike(edges.getNodeList().get(position).getEdge_liked_by().getCount(), holder.tv_likedCount);
                        SharedPreference.disLike(Home.this,edges.getNodeList().get(position).getShortcode());
                    }
                    else {
                        holder.btn_like.setBackgroundResource(R.drawable.like_fill);
                        setLike(edges.getNodeList().get(position).getEdge_liked_by().getCount()+1, holder.tv_likedCount);
                        SharedPreference.like(Home.this,edges.getNodeList().get(position).getShortcode());
                    }
                }
            });
            //收藏
            if (SharedPreference.getKeepPost(Home.this).contains(edges.getNodeList().get(position).getShortcode()))
                holder.btn_keep.setBackgroundResource(R.drawable.keep_fill);
            holder.btn_keep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.btn_keep.startAnimation(new SetAnimation().icon());
                    if (SharedPreference.getKeepPost(Home.this).contains(edges.getNodeList().get(position).getShortcode())) {
                        holder.btn_keep.setBackgroundResource(R.drawable.keep_border);
                        SharedPreference.cancelKeep(Home.this,edges.getNodeList().get(position).getShortcode());
                    }
                    else {
                        Glide.with(Home.this)
                                .load(edges.getNodeList().get(position).getDisplay_url())
                                .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(5)))
                                .placeholder(R.drawable.keep_fill)
                                .into(holder.img_photoSmall);
                        holder.ll_keep.setVisibility(View.VISIBLE);
                        holder.ll_keep.startAnimation(new SetAnimation().keepNode());
                        holder.btn_keep.setBackgroundResource(R.drawable.keep_fill);
                        SharedPreference.keep(Home.this,edges.getNodeList().get(position).getShortcode());
                    }
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return edges.getNodeList().size();
        }
    }

    //貼文中照片集的adapter
    private class NodeChildAdapter extends RecyclerView.Adapter<NodeChildAdapter.ViewHolder> {
        Edges edges_child;

        NodeChildAdapter(Edges edges_child) {
            this.edges_child = edges_child;
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            ImageView img_photo, img_liked;

            ViewHolder(View item){
                super(item);
                img_photo = item.findViewById(R.id.img_photo);
                img_liked = item.findViewById(R.id.img_liked);
            }
        }

        @Override
        public NodeChildAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(Home.this).inflate(R.layout.adapter_node_child, parent, false);
            return new NodeChildAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final NodeChildAdapter.ViewHolder holder, final int position) {
            Glide.with(Home.this).load(edges_child.getNodeList().get(position).getDisplay_url()).placeholder(R.drawable.logo).into(holder.img_photo);
            holder.itemView.setOnTouchListener(new DoubleClick(Home.this, new OnDoubleClickListener() {
                @Override
                public void onDoubleClick(int action) {
                    holder.img_liked.setVisibility(View.VISIBLE);
                }
            }));
        }

        @Override
        public int getItemCount() {
            return edges_child.getNodeList().size();
        }
    }
}
