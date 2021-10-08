package com.example.instagram2.Fragments.Main;

import android.content.DialogInterface;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.instagram2.Fragments.FollowingFragment;
import com.example.instagram2.SetAnimation;
import com.example.instagram2.DataCallback;
import com.example.instagram2.Model.MyNode;
import com.example.instagram2.Model.User;
import com.example.instagram2.MyLinearLayoutManager;
import com.example.instagram2.OkHttpManager;
import com.example.instagram2.R;
import com.example.instagram2.SetCount;
import com.example.instagram2.SetTime;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class HomeFragment extends Fragment {
    private RecyclerView rv_story, rv_post = null;
    private StoryAdapter storyAdapter = null;
    private PostAdapter postAdapter = null;

    ArrayList<String> followedAccount = new ArrayList<>(); //取代限時動態的帳號列 3
    ArrayList<User> users = new ArrayList<>(); //所有追蹤中的帳號

    int followedTag = 0; //目前顯示的帳號
    ArrayList<MyNode> myNodes = new ArrayList<>(); //目前帳號的貼文

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
        rv_story = view.findViewById(R.id.rv_story);
        storyAdapter = new StoryAdapter(users);
        rv_story.setAdapter(storyAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        rv_story.setLayoutManager(linearLayoutManager);
        //貼文
        rv_post = view.findViewById(R.id.rv_post);
        postAdapter = new PostAdapter(myNodes);
        rv_post.setAdapter(postAdapter);
        rv_post.setLayoutManager(new MyLinearLayoutManager(getActivity(),false));
        return view;
    }

    private void getFollowedData(String userName) {
        String url = "https://www.instagram.com/" + userName + "/?__a=1";
        OkHttpManager okHttpManager = new OkHttpManager(dataCallback);
        okHttpManager.setContext(getActivity()); //設context給getUserAgent用
        okHttpManager.getStringData(url);
    }

    private DataCallback dataCallback = new DataCallback() {
        @Override
        public void onDataSuccess(String data) {
            getUserData(data);
            if (users.size() != 0) {
                storyAdapter.notifyDataSetChanged();
                updatePost(followedTag);
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("API打太多次").setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }

        @Override
        public void onDataFail(String msg) {
            Log.e("DataCallback","onDataFail" + msg);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("網路連線中斷").setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getActivity().finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
    };

    public void updatePost(int followedTag){
        myNodes.clear();
        myNodes.addAll(users.get(followedTag).getMyNode());
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
            ArrayList<MyNode> myNodeList = new ArrayList<>();
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
                myNodeList.add(new MyNode(display_url, text, taken_at_timestamp, comment_count, liked_by, photoUrlList));
            }
            user.setMyNode(myNodeList);
            users.add(user);
        } catch (JSONException e) {
            Log.e("MainActivity", "getUserData-" + e.getMessage());
        }
    }

    //限時動態adapter
    private class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder> {
        ArrayList<User> usersList = new ArrayList<>();

        public StoryAdapter(ArrayList<User> usersList){
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
        public StoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_story, parent, false);
            return new StoryAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StoryAdapter.ViewHolder holder, final int position) {
            Glide.with(getActivity()).load(usersList.get(position).getProfile_pic_url_hd()).circleCrop().into(holder.img_hd);
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
            ImageView img_user, img_photoSmall, img_liked;
            View ll_keep, rl_doubleClick;
            ViewPager2 pager_photo;
            TextView tv_user, tv_userMsg, tv_likedCount, tv_commentCount, tv_time;
            Button btn_like, btn_msg, btn_share, btn_keep, btn_more;

            ViewHolder(View item){
                super(item);
                img_user = item.findViewById(R.id.img_user);
                tv_user = item.findViewById(R.id.tv_user);

                pager_photo = item.findViewById(R.id.pager_photo);
                ll_keep = item.findViewById(R.id.ll_keep);
                img_photoSmall = item.findViewById(R.id.img_small);
                rl_doubleClick = item.findViewById(R.id.rl_doubleClick);
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
        public PostAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_post, parent, false);
            return new PostAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final PostAdapter.ViewHolder holder, final int position) {
            //頭貼
            Glide.with(getActivity()).load(users.get(followedTag).getProfile_pic_url_hd()).circleCrop().into(holder.img_user);
            //帳號
            holder.tv_user.setText(users.get(followedTag).getUsername());
            holder.tv_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment following = new FollowingFragment();
                    User followingData = users.get(followedTag);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("followingData", followingData);
                    following.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container,following,"following").addToBackStack("back").commit();
                }
            });
            //更多
            holder.btn_more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
            final boolean[] isLiked = {false};
            final GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    holder.img_liked.startAnimation(new SetAnimation().doubleClickLike());
                    holder.btn_like.startAnimation(new SetAnimation().icon());
                    isLiked[0] = true;
                    holder.btn_like.setBackgroundResource(R.drawable.like_fill);
                    return true;
                }
                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }
            });

            holder.rl_doubleClick.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
            });
            //讚數 如果沒有讚整行不顯示
            if (postList.get(position).getEdge_liked_by_count() == 0)
                holder.tv_likedCount.setVisibility(View.GONE);
            else
                holder.tv_likedCount.setText(String.format("%,d個讚", postList.get(position).getEdge_liked_by_count()));
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
                holder.tv_commentCount.setText(String.format("查看全部%s則留言", postList.get(position).getEdge_media_to_comment_count()));
            }
            //發文時間
            holder.tv_time.setText(String.format("%s • ", SetTime.setPostTime(postList.get(position).getTaken_at_timestamp())));
            //點擊愛心
            holder.btn_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.btn_like.startAnimation(new SetAnimation().icon());
                    isLiked[0] = !isLiked[0];
                    if (isLiked[0])
                        holder.btn_like.setBackgroundResource(R.drawable.like_fill);
                    else
                        holder.btn_like.setBackgroundResource(R.drawable.like_border);
                }
            });
            //收藏
            final boolean[] isKeep = {false};
            holder.btn_keep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.btn_keep.startAnimation(new SetAnimation().icon());
                    isKeep[0] = !isKeep[0];
                    if (isKeep[0]) {
                        Glide.with(getActivity())
                                .load(postList.get(position).getDisplay_url())
                                .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(5)))
                                .placeholder(R.drawable.keep_fill)
                                .into(holder.img_photoSmall);
                        holder.ll_keep.setVisibility(View.VISIBLE);
                        holder.ll_keep.startAnimation(new SetAnimation().keepNode());
                        holder.btn_keep.setBackgroundResource(R.drawable.keep_fill);
                    }
                    else
                        holder.btn_keep.setBackgroundResource(R.drawable.keep_border);
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

    //貼文中照片集的adapter
    private class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {
        ArrayList<String> photoList = new ArrayList<>();

        PhotoAdapter(ArrayList<String> photoList) {
            this.photoList = photoList;
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
        public PhotoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.adapter_photo, parent, false);
            return new PhotoAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final PhotoAdapter.ViewHolder holder, final int position) {
            Glide.with(getActivity()).load(photoList.get(position)).placeholder(R.drawable.logo).into(holder.img_photo);
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
