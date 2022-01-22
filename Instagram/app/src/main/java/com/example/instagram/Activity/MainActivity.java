package com.example.instagram.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.instagram.Activity.Account.Account;
import com.example.instagram.Activity.Add.Add;
import com.example.instagram.Activity.Home.Home;
import com.example.instagram.Activity.Search.Search;
import com.example.instagram.Activity.Shop.Shop;
import com.example.instagram.Dialog.DialogAlert;
import com.example.instagram.Dialog.DialogAlert_option;
import com.example.instagram.Dialog.DialogBtn;
import com.example.instagram.Dialog.DialogList;
import com.example.instagram.Dialog.DialogList_icon;
import com.example.instagram.Dialog.OnDialogClickListener;
import com.example.instagram.R;
import com.example.instagram.model.DialogListItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    View ll_home, ll_search, ll_add, ll_shop, ll_account, progressBar;
    ImageView img_home, img_search, img_add, img_shop, img_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    public void initView() {
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.ll_progressBar);

        ll_home = findViewById(R.id.ll_home);
        ll_home.setOnClickListener(onClickListener_nav);
        ll_search = findViewById(R.id.ll_search);
        ll_search.setOnClickListener(onClickListener_nav);
        ll_add = findViewById(R.id.ll_add);
        ll_add.setOnClickListener(onClickListener_nav);
        ll_shop = findViewById(R.id.ll_shop);
        ll_shop.setOnClickListener(onClickListener_nav);
        ll_account = findViewById(R.id.ll_account);
        ll_account.setOnClickListener(onClickListener_nav);

        img_home = findViewById(R.id.img_home);
        img_search = findViewById(R.id.img_search);
        img_add = findViewById(R.id.img_add);
        img_shop = findViewById(R.id.img_shop);
        img_account = findViewById(R.id.img_account);
    }

    public void initNav(int nowPage){
        switch (nowPage){
            case 0:
                //home
                img_home.setImageResource(R.drawable.home_fill);
                ll_home.setOnClickListener(null);
                break;
            case 1:
                //search
                ll_search.setOnClickListener(null);
                break;
            case 2:
                //add
                break;
            case 3:
                //shop
                ll_shop.setOnClickListener(null);
                break;
            case 4:
                //account
                ll_account.setOnClickListener(null);
                break;
            case 5:
                //home branch
                //img_home.setImageResource(R.drawable.home_fill);
                break;
            default:
                break;
        }
    }

    public void showLoading(){
        progressBar.setVisibility(View.VISIBLE);
    }

    public void closeLoading(){
        progressBar.setVisibility(View.GONE);
    }

    public final View.OnClickListener onClickListener_nav = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.ll_home:
                    startActivity(new Intent(MainActivity.this, Home.class));
                    break;
                case R.id.ll_search:
                    startActivity(new Intent(MainActivity.this, Search.class));
                    break;
                case R.id.ll_add:
                    startActivity(new Intent(MainActivity.this, Add.class));
                    break;
                case R.id.ll_shop:
                    startActivity(new Intent(MainActivity.this, Shop.class));
                    break;
                case R.id.ll_user:
                    startActivity(new Intent(MainActivity.this, Account.class));
                    break;
                default:
                    break;
            }
        }
    };
}