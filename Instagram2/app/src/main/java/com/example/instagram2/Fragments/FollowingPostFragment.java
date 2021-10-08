package com.example.instagram2.Fragments;

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
import com.example.instagram2.Model.MyNode;
import com.example.instagram2.Model.User;
import com.example.instagram2.R;
import com.example.instagram2.SetAnimation;
import com.example.instagram2.SetTime;

import java.util.ArrayList;
import java.util.Calendar;

public class FollowingPostFragment extends Fragment {
    private RecyclerView rv_post = null;
    private PostAdapter postAdapter = null;

    User followingData = new User();
    int position = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            followingData = (User) getArguments().getSerializable("followingData");
            position = getArguments().getInt("position");
        }


        View view = inflater.inflate(R.layout.fragment_followingpost, container, false);
        Button btn_back = view.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        //貼文
        rv_post = view.findViewById(R.id.rv_post);
        postAdapter = new PostAdapter(followingData.getMyNode());
        rv_post.setAdapter(postAdapter);
        rv_post.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_post.scrollToPosition(position);
        return view;
    }

    private class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
        ArrayList<MyNode> postList = new ArrayList<>();

        public PostAdapter(ArrayList<MyNode> postList){
            this.postList = postList;
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_user, img_photoSmall;
            View ll_keep;
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

                tv_likedCount = item.findViewById(R.id.tv_likedCount);
                tv_userMsg = item.findViewById(R.id.tv_userMsg);
                tv_commentCount = item.findViewById(R.id.tv_commentCount);
                tv_time = item.findViewById(R.id.tv_time);

                btn_more = item.findViewById(R.id.btn_more);
                btn_like = item.findViewById(R.id.btn_like);
                btn_msg = item.findViewById(R.id.btn_msg);
                btn_share = item.findViewById(R.id.btn_share);
                btn_keep = item.findViewById(R.id.btn_keep);
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
            Glide.with(getActivity()).load(followingData.getProfile_pic_url_hd()).circleCrop().into(holder.img_user);
            //帳號
            holder.tv_user.setText(followingData.getUsername());
            holder.tv_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment following = new FollowingFragment();
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
            //讚數 如果沒有讚整行不顯示
            if (postList.get(position).getEdge_liked_by_count() == 0) {
                holder.tv_likedCount.setVisibility(View.GONE);
            }else {
                holder.tv_likedCount.setText(String.format("%,d個讚", postList.get(position).getEdge_liked_by_count()));
            }
            //設定貼文字體及顏色
            StyleSpan bold = new StyleSpan(android.graphics.Typeface.BOLD);
            String userMsg = String.format("%s %s", followingData.getUsername(), postList.get(position).getText());
            SpannableString userMsgStyle = new SpannableString(userMsg);
            userMsgStyle.setSpan(bold, 0, followingData.getUsername().length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            holder.tv_userMsg.setText(userMsgStyle);
            //發文時間
            holder.tv_time.setText(String.format("%s • ", SetTime.setPostTime(postList.get(position).getTaken_at_timestamp())));
            //留言 如果沒有留言整行不顯示
            if (postList.get(position).getEdge_media_to_comment_count() == 0) {
                holder.tv_commentCount.setVisibility(View.GONE);
            }else {
                holder.tv_commentCount.setText(String.format("查看全部%s則留言", postList.get(position).getEdge_media_to_comment_count()));
            }
            //點擊愛心
            final boolean[] isLiked = {false};
            holder.btn_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.btn_like.startAnimation(new SetAnimation().icon());
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
        public void onBindViewHolder(@NonNull final PhotoAdapter.ViewHolder holder, int position) {
            Glide.with(getActivity()).load(photoList.get(position)).placeholder(R.drawable.logo).into(holder.img_photo);
            final GestureDetector gestureDetector = new GestureDetector(getActivity(), new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onDoubleTap(MotionEvent e) {
                    holder.img_liked.startAnimation(new SetAnimation().doubleClickLike());
                    Log.e("DoubleClick","onDoubleTap");
                    return true;
                }
                @Override
                public boolean onDown(MotionEvent e) {
                    return true;
                }
            });
            holder.itemView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    gestureDetector.onTouchEvent(event);
                    return true;
                }
            });
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
