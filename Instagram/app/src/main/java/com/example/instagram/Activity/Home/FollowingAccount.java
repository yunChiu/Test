package com.example.instagram.Activity.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.instagram.Activity.MainActivity;
import com.example.instagram.R;
import com.example.instagram.model.Edges;
import com.example.instagram.model.User;

public class FollowingAccount extends MainActivity {

    public static final String BUNDLE_KEY_PROFILE = "BUNDLE_KEY_PROFILE";
    User user;
    Button btn_back, btn_notification, btn_more, btn_moreFollow;
    TextView tv_userName, tv_postCount, tv_followerCount, tv_followingCount,
            tv_fullName, tv_biography;
    ImageView img_hd;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            user = (User) getIntent().getExtras().getSerializable(BUNDLE_KEY_PROFILE);
        } catch (Exception e) {
            Log.e("Intent", e.getMessage());
            onBackPressed();
        }
        initView();
    }

    private void initView(){
        setContentView(R.layout.activity_following_account);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(onClickListener);
        tv_userName = findViewById(R.id.tv_userName);
        tv_userName.setText(user.getUsername());
        btn_notification = findViewById(R.id.btn_notification);
        btn_more = findViewById(R.id.btn_more);
        btn_more.setOnClickListener(onClickListener);
        img_hd = findViewById(R.id.img_hd);
        Glide.with(FollowingAccount.this).load(user.getProfile_pic_url_hd()).circleCrop().into(img_hd);
        tv_postCount = findViewById(R.id.tv_postCount);
        tv_postCount.setText(String.valueOf(user.getEdge_owner_to_timeline_media().getCount()));
        tv_followerCount = findViewById(R.id.tv_followerCount);
        tv_followerCount.setText(String.valueOf(user.getEdge_followed_by().getCount()));
        tv_followingCount = findViewById(R.id.tv_followingCount);
        tv_followingCount.setText(String.valueOf(user.getEdge_follow().getCount()));
        tv_fullName = findViewById(R.id.tv_fullName);
        setProfile(user.getFull_name(), tv_fullName);
        tv_biography = findViewById(R.id.tv_biography);
        setProfile(user.getBiography(), tv_biography);
        btn_moreFollow = findViewById(R.id.btn_moreFollow);
        btn_moreFollow.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_back:
                    onBackPressed();
                    break;
                case R.id.btn_more:
                    showDialogList_more(FollowingAccount.this);
                    break;
                case R.id.btn_moreFollow:
                    break;
                default:
                    break;
            }
        }
    };
}
