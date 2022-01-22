package com.example.instagram;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.instagram.Activity.Home.FollowingAccount;
import com.example.instagram.Dialog.DialogBtn;
import com.example.instagram.Dialog.OnDialogClickListener;
import com.example.instagram.model.Edges;
import com.example.instagram.model.User;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import static android.content.Context.CLIPBOARD_SERVICE;
import static com.example.instagram.Activity.Home.FollowingAccount.BUNDLE_KEY_PROFILE;

public class NodeAdapter extends RecyclerView.Adapter<NodeAdapter.ViewHolder> {

    Context context;
    User user;
    Edges edges;

    float moveStart_x = 0;
    float moveStart_y = 0;

    public NodeAdapter(Context context, User user){
        this.context = context;
        this.user = user;
        edges = user.getEdge_owner_to_timeline_media().getEdges();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_user, img_photoSmall, img_liked;
        View ll_keep;
        TabLayout tabLayout;
        ViewPager2 pager_child;
        TextView tv_user, tv_child, tv_userMsg, tv_likedCount, tv_commentCount, tv_time;
        Button btn_like, btn_msg, btn_share, btn_keep, btn_more;

        ViewHolder(View item){
            super(item);

            img_user = item.findViewById(R.id.img_user);
            tv_user = item.findViewById(R.id.tv_user);

            tabLayout = item.findViewById(R.id.tabLayout);
            pager_child = item.findViewById(R.id.pager_photo);
            tv_child = item.findViewById(R.id.tv_nodechildcount);
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
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_node, parent, false);
        return new NodeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NodeAdapter.ViewHolder holder, final int position) {
        //頭貼
        Glide.with(context).load(user.getProfile_pic_url_hd()).circleCrop().into(holder.img_user);
        //帳號
        holder.tv_user.setText(user.getUsername());
        holder.tv_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FollowingAccount.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(BUNDLE_KEY_PROFILE, user);
                intent.putExtras(bundle);
                context.startActivity(intent);
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
                showDialogBtn_more(context, copyLink, shareLink);
            }
        });
        //照片
        final Edges edges_child = edges.getNodeList().get(position).getEdge_sidecar_to_children();
        if (edges_child.getNodeList().size() == 1) {
            holder.tv_child.setVisibility(View.GONE);
            holder.tabLayout.setVisibility(View.GONE);
        }
        holder.pager_child.setAdapter(new NodeChildAdapter(edges_child, holder.btn_like, holder.tv_likedCount, position));
        holder.pager_child.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                holder.tv_child.setText(String.format("%s / %s", position + 1, edges_child.getNodeList().size()));
            }
        });
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(holder.tabLayout, holder.pager_child, true, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {

            }
        });
        tabLayoutMediator.attach();
        //讚數
        SetElements.setLikeCount(edges.getNodeList().get(position).getEdge_liked_by().getCount(), holder.tv_likedCount);
        //內容
        SetElements.setText(user.getUsername(),
                edges.getNodeList().get(position).getEdge_media_to_caption().getEdges_inner().getNode_inner().getText(),
                holder.tv_userMsg);
        //留言
        SetElements.setCommentCount(edges.getNodeList().get(position).getEdge_media_to_comment().getCount(), holder.tv_commentCount);
        //發文時間
        holder.tv_time.setText(String.format("%s • ", SetTime.setPostTime(edges.getNodeList().get(position).getTaken_at_timestamp())));
        //愛心
        if (SharedPreference.getLikedPost(context).contains(edges.getNodeList().get(position).getShortcode())) {
            SetElements.setLikeCount(edges.getNodeList().get(position).getEdge_liked_by().getCount()+1, holder.tv_likedCount);
            holder.btn_like.setBackgroundResource(R.drawable.like_fill);
        }
        holder.btn_like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btn_like.startAnimation(new SetAnimation().icon());
                if (SharedPreference.getLikedPost(context).contains(edges.getNodeList().get(position).getShortcode())) {
                    holder.btn_like.setBackgroundResource(R.drawable.like_border);
                    SetElements.setLikeCount(edges.getNodeList().get(position).getEdge_liked_by().getCount(), holder.tv_likedCount);
                    SharedPreference.disLike(context,edges.getNodeList().get(position).getShortcode());
                } else {
                    holder.btn_like.setBackgroundResource(R.drawable.like_fill);
                    SetElements.setLikeCount(edges.getNodeList().get(position).getEdge_liked_by().getCount()+1, holder.tv_likedCount);
                    SharedPreference.like(context,edges.getNodeList().get(position).getShortcode());
                }
            }
        });
        //收藏
        if (SharedPreference.getKeepPost(context).contains(edges.getNodeList().get(position).getShortcode()))
            holder.btn_keep.setBackgroundResource(R.drawable.keep_fill);
        holder.btn_keep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.btn_keep.startAnimation(new SetAnimation().icon());
                if (SharedPreference.getKeepPost(context).contains(edges.getNodeList().get(position).getShortcode())) {
                    holder.btn_keep.setBackgroundResource(R.drawable.keep_border);
                    SharedPreference.cancelKeep(context,edges.getNodeList().get(position).getShortcode());
                }
                else {
                    Glide.with(context)
                            .load(edges.getNodeList().get(position).getDisplay_url())
                            .apply(new RequestOptions().transform(new CenterCrop(), new RoundedCorners(5)))
                            .placeholder(R.drawable.keep_fill)
                            .into(holder.img_photoSmall);
                    holder.ll_keep.setVisibility(View.VISIBLE);
                    holder.ll_keep.startAnimation(new SetAnimation().keepNode());
                    holder.btn_keep.setBackgroundResource(R.drawable.keep_fill);
                    SharedPreference.keep(context,edges.getNodeList().get(position).getShortcode());
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

    public void update(User user){
        this.user = user;
        edges = user.getEdge_owner_to_timeline_media().getEdges();
        this.notifyDataSetChanged();
    }

    //貼文中照片集的adapter
    private class NodeChildAdapter extends RecyclerView.Adapter<NodeChildAdapter.ViewHolder> {
        Edges edges_child;
        Button btn_like;
        TextView tv_likeCount;
        int parentPosition;

        NodeChildAdapter(Edges edges_child, Button btn_like, TextView tv_likeCount, int parentPosition) {
            this.edges_child = edges_child;
            this.btn_like = btn_like;
            this.tv_likeCount = tv_likeCount;
            this.parentPosition = parentPosition;
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
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_node_child, parent, false);
            return new NodeChildAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final NodeChildAdapter.ViewHolder holder, final int position) {
            Glide.with(context).load(edges_child.getNodeList().get(position).getDisplay_url())
                    .override(SetElements.getDeviceWidth(context),SetElements.getPicHeight(context,
                            edges_child.getNodeList().get(position).getDimensions().getWidth(),
                            edges_child.getNodeList().get(position).getDimensions().getHeight()))
                    .placeholder(R.drawable.logo)
                    .into(holder.img_photo);
            holder.itemView.setOnTouchListener(new DoubleClick(context, new OnDoubleClickListener() {
                @Override
                public void onDoubleClick() {
                    //雙擊愛心
                    holder.img_liked.startAnimation(new SetAnimation().doubleClickLike());
                    btn_like.startAnimation(new SetAnimation().icon());
                    btn_like.setBackgroundResource(R.drawable.like_fill);
                    SetElements.setLikeCount(edges.getNodeList().get(parentPosition).getEdge_liked_by().getCount() + 1, tv_likeCount);
                    SharedPreference.like(context, edges.getNodeList().get(parentPosition).getShortcode());
                }
            }, new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    //控制滑動
                    switch (motionEvent.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            moveStart_x = motionEvent.getX();
                            moveStart_y = motionEvent.getY();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            float moveEnd_x = motionEvent.getX();
                            float moveEnd_y = motionEvent.getY();
                            if (Math.abs(moveStart_x - moveEnd_x) > Math.abs(moveStart_y - moveEnd_y)) {
                                //水平滑動
                                if ((moveEnd_x >= moveStart_x && position != 0) ||
                                        (moveStart_x >= moveEnd_x && position != edges_child.getNodeList().size()-1)) {
                                    //往右滑且當前照片不是第一張 或 往左滑且當前照片不是最後一張
                                    view.getParent().getParent().requestDisallowInterceptTouchEvent(true);
                                }else {
                                    view.getParent().getParent().requestDisallowInterceptTouchEvent(false);
                                }
                            } else {
                                //垂直滑動
                                view.getParent().getParent().requestDisallowInterceptTouchEvent(false);
                            }
                            break;
                        case MotionEvent.ACTION_UP:
                            view.getParent().getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                        default:
                            break;
                    }
                    return true;
                }
            }));
        }

        @Override
        public int getItemCount() {
            return edges_child.getNodeList().size();
        }
    }

    public void showDialogBtn_more(final Context context, final String copyLink, final String shareLink) {
        final DialogBtn dialogBtn = new DialogBtn(context, new OnDialogClickListener() {
            @Override
            public void onDialogClick(int action) {
                switch (action){
                    case 0:
                        //複製連結
                        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("text", copyLink);
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(context,"已複製連結",Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        //分享
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.putExtra(Intent.EXTRA_TEXT, shareLink);
                        intent.setType("text/plain");
                        context.startActivity(Intent.createChooser(intent,"IG Sharing"));
                        break;
                    case 2:
                        //檢舉
                        break;
                    case 3:
                        //為什麼
                        break;
                    case 4:
                        //隱藏
                        break;
                    case 5:
                        //退追
                        break;
                    default:
                        break;
                }
            }
        });
        dialogBtn.show();
    }
}
