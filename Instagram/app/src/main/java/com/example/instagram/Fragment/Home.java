package com.example.instagram.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.instagram.DataCallback;
import com.example.instagram.FollowingActivity;
import com.example.instagram.MyLinearLayoutManager;
import com.example.instagram.OkHttpManager;
import com.example.instagram.R;
import com.example.instagram.model.MyNode;
import com.example.instagram.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Home extends Fragment {
    private RecyclerView rv_video, rv_post = null;
    private VideoAdapter videoAdapter = null;
    private PostAdapter postAdapter = null;

    ArrayList<String> followedAccount = new ArrayList<>(); //取代限時動態的帳號列 3
    ArrayList<User> users = new ArrayList<>(); //所有追蹤中的帳號

    int followedTag = 0; //目前顯示的帳號
    ArrayList<MyNode> nodes = new ArrayList<>(); //目前帳號的貼文

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //新增追蹤帳號
        followedAccount.add("mei.mei888");
        followedAccount.add("eatlife_c");
        followedAccount.add("rimazeidan51");
        for (int i=0; i<followedAccount.size(); i++) {
            getFollowedData(followedAccount.get(i));
        }

        //限時動態
        rv_video = view.findViewById(R.id.rv_video);
        videoAdapter = new VideoAdapter(users);
        rv_video.setAdapter(videoAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_video.setLayoutManager(linearLayoutManager);
        //貼文
        rv_post = view.findViewById(R.id.rv_post);
        postAdapter = new PostAdapter(nodes);
        rv_post.setAdapter(postAdapter);
        rv_post.setLayoutManager(new MyLinearLayoutManager(getActivity(),false));

        return view;
    }

    private void getFollowedData(String userName) {
        String url = "https://www.instagram.com/" + userName + "/?__a=1";
        OkHttpManager okHttpManager = new OkHttpManager(dataCallback);
        okHttpManager.setContext(getActivity()); //設context給getUserAgent用
        //okHttpManager.getStringData(url);
    }

    private DataCallback dataCallback = new DataCallback() {
        @Override
        public void onDataSuccess(String data) {
            getUserData(data);
            videoAdapter.notifyDataSetChanged();
            updatePost(followedTag);
        }

        @Override
        public void onDataFail(String msg) {
            Log.e("DataCallback","onDataFail" + msg);
        }
    };

    public void updatePost(int followedTag){
        nodes.clear();
        nodes.addAll(users.get(followedTag).getMyNode());
        postAdapter.notifyDataSetChanged();
    }

    public void getUserData(String data){
        try {
            User user = new User();
            JSONObject jsonObject = new JSONObject(data);
            JSONObject jsonObjectTemp1 = jsonObject.getJSONObject("graphql").getJSONObject("user");
            user.setBiography(jsonObjectTemp1.getString("biography"));//自我介紹
            user.setExternal_url(jsonObjectTemp1.getString("external_url"));//個人連結(沒用到)
            user.setFollowed_by(jsonObjectTemp1.getJSONObject("edge_followed_by").getInt("count"));//追蹤數
            user.setFull_name(jsonObjectTemp1.getString("full_name"));//名字
            user.setProfile_pic_url_hd(jsonObjectTemp1.getString("profile_pic_url_hd"));//頭像*
            user.setUsername(jsonObjectTemp1.getString("username"));//帳號名稱*
            user.setTotal_post(jsonObjectTemp1.getJSONObject("edge_owner_to_timeline_media").getInt("count"));//貼文數量
            //貼文
            ArrayList<MyNode> nodeList = new ArrayList<>();
            for (int i=0; i<jsonObjectTemp1.getJSONObject("edge_owner_to_timeline_media").getJSONArray("edges").length(); i++){
                JSONObject jsonObjectTemp2 = jsonObjectTemp1.getJSONObject("edge_owner_to_timeline_media").getJSONArray("edges").getJSONObject(i).getJSONObject("node");
                String display_url = jsonObjectTemp2.getString("display_url");
                String text = jsonObjectTemp2.getJSONObject("edge_media_to_caption").getJSONArray("edges").getJSONObject(0).getJSONObject("node").getString("text");
                int taken_at_timestamp = jsonObjectTemp2.getInt("taken_at_timestamp");
                int comment_count = jsonObjectTemp2.getJSONObject("edge_media_to_comment").getInt("count");
                int liked_by = jsonObjectTemp2.getJSONObject("edge_liked_by").getInt("count");
                //照片集
                ArrayList<String> photoUrlList = new ArrayList<>();
                if(jsonObjectTemp2.has("edge_sidecar_to_children")){
                    for (int j=0; j<jsonObjectTemp2.getJSONObject("edge_sidecar_to_children").getJSONArray("edges").length(); j++){
                        String photo = jsonObjectTemp2.getJSONObject("edge_sidecar_to_children").getJSONArray("edges").getJSONObject(j).getJSONObject("node").getString("display_url");
                        photoUrlList.add(photo);
                    }
                }else {
                    photoUrlList.add(display_url);
                }
                //nodeList.add(new MyNode(display_url, text, taken_at_timestamp, comment_count, liked_by, photoUrlList));
            }
            user.setMyNode(nodeList);
            users.add(user);
        } catch (JSONException e) {
            Log.e("MainActivity", "getUserData-" + e.getMessage());
        }
    }

    //限時動態adapter
    private class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
        ArrayList<User> usersList = new ArrayList<>();

        public VideoAdapter(ArrayList<User> usersList){
            this.usersList = usersList;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            View ly_cell;
            ImageView img_hd;
            TextView tv_followedName;
            ViewHolder(View item) {
                super(item);
                ly_cell = item.findViewById(R.id.ll_cell);
                img_hd = item.findViewById(R.id.img_hd);
                tv_followedName = item.findViewById(R.id.tv_followedName);
            }
        }

        @Override
        public VideoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_video, parent, false);
            return new VideoAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull VideoAdapter.ViewHolder holder, final int position) {
            Glide.with(getContext()).load(usersList.get(position).getProfile_pic_url_hd()).circleCrop().into(holder.img_hd);
            holder.tv_followedName.setText(usersList.get(position).getUsername());
            holder.ly_cell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    followedTag = position;
                    updatePost(followedTag);
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return usersList.size();
        }
    }
    //貼文adapter
    private class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
        ArrayList<MyNode> postList = new ArrayList<>();

        public PostAdapter(ArrayList<MyNode> postList){
            this.postList = postList;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_user, img_liked;
            View rl_cell;
            ViewPager2 pager_photo;
            TextView tv_user, tv_userMsg, tv_likedCount, tv_commentCount;
            Button btn_like, btn_msg, btn_share, btn_keep, btn_more;

            ViewHolder(View item){
                super(item);
                img_user = item.findViewById(R.id.img_user);
                tv_user = item.findViewById(R.id.tv_user);

                rl_cell = item.findViewById(R.id.rl_cell);
                pager_photo = item.findViewById(R.id.pager_photo);
                img_liked = item.findViewById(R.id.img_liked);

                tv_likedCount = item.findViewById(R.id.tv_likedCount);
                tv_userMsg = item.findViewById(R.id.tv_userMsg);
                tv_commentCount = item.findViewById(R.id.tv_commentCount);

                btn_more = item.findViewById(R.id.btn_more);
                btn_like = item.findViewById(R.id.btn_like);
                btn_msg = item.findViewById(R.id.btn_msg);
                btn_share = item.findViewById(R.id.btn_share);
                btn_keep = item.findViewById(R.id.btn_keep);
            }
        }

        @Override
        public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_post, parent, false);
            return new PostAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final PostAdapter.ViewHolder holder, final int position) {
            //頭貼
            Glide.with(getContext()).load(users.get(followedTag).getProfile_pic_url_hd()).circleCrop().into(holder.img_user);
            //帳號
            holder.tv_user.setText(users.get(followedTag).getUsername());
            holder.tv_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeFollowing homeFollowing = new HomeFollowing();
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.frame, homeFollowing).addToBackStack(null).commit();

                    /*
                    User following = users.get(followedTag);
                    Intent intent = new Intent(getContext(), FollowingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(FollowingActivity.BundleKey_Following, following);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    */
                }
            });
            //更多
            holder.btn_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    String[] moreList = new String[]{"檢舉......","隱藏","開啟貼文通知","複製連結","分享到......","取消追蹤"};
                    builder.setItems(moreList, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            //照片
            PhotoAdapter photoAdapter = new PhotoAdapter(postList.get(position).getPhotoUrlList());
            holder.pager_photo.setAdapter(photoAdapter);
            //照片 雙擊愛心 失敗
            final GestureDetector gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public void onLongPress(MotionEvent e) {
                    Log.e("DoubleClick","onLongPress");
                }

                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    icon_anim();
                    holder.img_liked.setVisibility(View.VISIBLE);
                    Log.e("DoubleClick","onDoubleTap");
                    return false;
                }

                @Override
                public boolean onDown(MotionEvent e) {
                    //每次點擊都會觸發
                    return true;
                }
            });
            holder.rl_cell.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gestureDetector.onTouchEvent(event);
                }
            });
            //讚數 如果沒有讚整行不顯示
            if (postList.get(position).getEdge_liked_by_count() == 0) {
                holder.tv_likedCount.setVisibility(View.GONE);
            }else {
                holder.tv_likedCount.setText(String.format("%s人說讚", String.valueOf(postList.get(position).getEdge_liked_by_count())));
            }
            //設定貼文字體及顏色
            StyleSpan bold = new StyleSpan(android.graphics.Typeface.BOLD);
            String userMsg = String.format("%s %s", users.get(followedTag).getUsername(), postList.get(position).getText());
            SpannableString userMsgStyle = new SpannableString(userMsg);
            userMsgStyle.setSpan(bold, 0, users.get(followedTag).getUsername().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            holder.tv_userMsg.setText(userMsgStyle);
            //留言 如果沒有留言整行不顯示
            if (postList.get(position).getEdge_media_to_comment_count() == 0) {
                holder.tv_commentCount.setVisibility(View.GONE);
            }else {
                holder.tv_commentCount.setText(String.format("查看全部%s則留言", String.valueOf(postList.get(position).getEdge_media_to_comment_count())));
            }



            //點擊愛心
            final boolean[] isLiked = {false};
            holder.btn_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.btn_like.startAnimation(icon_anim());
                    isLiked[0] = !isLiked[0];
                    if (isLiked[0]) {
                        holder.btn_like.setBackgroundResource(R.drawable.like_fill);
                    }
                    else {
                        holder.btn_like.setBackgroundResource(R.drawable.like_border);
                    }
                }
            });
            //收藏
            final boolean[] isKeep = {false};
            holder.btn_keep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.btn_keep.startAnimation(icon_anim());
                    isKeep[0] = !isKeep[0];
                    if (isKeep[0]) {
                        holder.btn_keep.setBackgroundResource(R.drawable.keep_fill);
                    }
                    else {
                        holder.btn_keep.setBackgroundResource(R.drawable.keep_border);
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
            return postList.size();
        }

    }

    public AnimationSet icon_anim(){
        final AnimationSet animationSet = new AnimationSet(true);
        animationSet.addAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_icon1));
        animationSet.addAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.anim_icon2));
        return animationSet;
    }

    //貼文中照片集的adapter
    private class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
        ArrayList<String> photoList = new ArrayList<>();

        PhotoAdapter(ArrayList<String> photoList) {
            this.photoList = photoList;
        }

        class ViewHolder extends RecyclerView.ViewHolder{

            ImageView img_photo;

            ViewHolder(View item){
                super(item);
                img_photo = item.findViewById(R.id.img_photo);
            }
        }

        @Override
        public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.adapter_photo, parent, false);
            return new PhotoAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull PhotoAdapter.ViewHolder holder, int position) {
            Glide.with(getContext()).load(photoList.get(position)).into(holder.img_photo);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getItemCount() {
            return photoList.size();
        }
    }
}
