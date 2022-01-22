package com.example.instagram.Activity.Account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.instagram.Activity.Add.Add;
import com.example.instagram.Activity.Home.Home;
import com.example.instagram.Activity.MainActivity;
import com.example.instagram.Activity.Search.Search;
import com.example.instagram.Activity.Shop.Shop;
import com.example.instagram.Dialog.DialogList_icon;
import com.example.instagram.Dialog.OnDialogClickListener;
import com.example.instagram.R;
import com.example.instagram.model.DialogListItem;

import java.util.ArrayList;

public class Account extends MainActivity {
    Button btn_menu;
    TextView tv_storyKeepIcon;
    View nav_home, nav_search, nav_add, nav_shop, ll_storyKeep, ll_storyKeepDetail;

    //典藏動態是否顯示
    boolean isVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_account);
        btn_menu = findViewById(R.id.btn_menu);
        btn_menu.setOnClickListener(onClickListener);
        ll_storyKeep = findViewById(R.id.ll_storyKeep);
        ll_storyKeep.setOnClickListener(onClickListener);
        tv_storyKeepIcon = findViewById(R.id.tv_storyKeepIcon);
        ll_storyKeepDetail = findViewById(R.id.ll_storyKeepDetail);
        //nav
        nav_home = findViewById(R.id.ll_home);
        nav_home.setOnClickListener(onClickListener);
        nav_search = findViewById(R.id.ll_search);
        nav_search.setOnClickListener(onClickListener);
        nav_add = findViewById(R.id.ll_add);
        nav_add.setOnClickListener(onClickListener);
        nav_shop = findViewById(R.id.ll_shop);
        nav_shop.setOnClickListener(onClickListener);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ll_home:
                    startActivity(new Intent(Account.this, Home.class));
                    break;
                case R.id.ll_search:
                    startActivity(new Intent(Account.this, Search.class));
                    break;
                case R.id.ll_add:
                    startActivity(new Intent(Account.this, Add.class));
                    break;
                case R.id.ll_shop:
                    startActivity(new Intent(Account.this, Shop.class));
                    break;
                case R.id.btn_menu:
                    showDialogList_menu(Account.this);
                    break;
                case R.id.ll_storyKeep:
                    if (isVisible) {
                        ll_storyKeepDetail.setVisibility(View.GONE);
                        tv_storyKeepIcon.setBackgroundResource(R.drawable.down);
                    } else {
                        ll_storyKeepDetail.setVisibility(View.VISIBLE);
                        tv_storyKeepIcon.setBackgroundResource(R.drawable.up);
                    }
                    isVisible = !isVisible;
                    break;
                default:
                    break;
            }
        }
    };

    public void showDialogList_menu(Context context) {
        ArrayList<DialogListItem> menu = new ArrayList<>();
        menu.add(new DialogListItem(R.drawable.setting,"設定"));
        menu.add(new DialogListItem(R.drawable.collection,"典藏"));
        menu.add(new DialogListItem(R.drawable.mystory,"你的動態"));
        menu.add(new DialogListItem(R.drawable.qrcode,"QR碼"));
        menu.add(new DialogListItem(R.drawable.keep_border,"我的珍藏"));
        menu.add(new DialogListItem(R.drawable.friendlist,"摯友"));
        menu.add(new DialogListItem(R.drawable.find,"探索用戶"));
        menu.add(new DialogListItem(R.drawable.covid19,"新冠病毒資訊中心"));
        DialogList_icon dialogListIcon = new DialogList_icon(context, menu, new OnDialogClickListener() {
            @Override
            public void onDialogClick(int action) {
                switch (action){
                    case 0:
                        //設定
                        break;
                    case 1:
                        //典藏
                        break;
                    case 2:
                        //你的動態
                        break;
                    case 3:
                        //QR碼
                        break;
                    case 4:
                        //我的珍藏
                        break;
                    case 5:
                        //摯友
                        break;
                    case 6:
                        //探索用戶
                        break;
                    case 7:
                        //新冠病毒資訊中心
                        break;
                    default:
                        break;
                }
            }
        });
        dialogListIcon.show();
    }
}
