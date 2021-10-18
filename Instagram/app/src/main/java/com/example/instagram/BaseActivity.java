package com.example.instagram;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.instagram.Fragment.Add;
import com.example.instagram.Fragment.Home;
import com.example.instagram.Fragment.HomeFollowing;
import com.example.instagram.Fragment.Search;
import com.example.instagram.Fragment.Shop;
import com.example.instagram.Fragment.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BaseActivity extends AppCompatActivity {
    FrameLayout frameLayout;
    BottomNavigationView bottomNavigationView;

    Home home = new Home();
    Search search = new Search();
    Add add = new Add();
    Shop shop = new Shop();
    User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initFragment();
    }

    private void initView(){
        setContentView(R.layout.activity_base);
        frameLayout = findViewById(R.id.frame);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        //bottomNavigationView.setItemHorizontalTranslationEnabled(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            switch (item.getItemId()) {
                case R.id.home:
                    fragmentTransaction.show(home);
                    fragmentTransaction.hide(search);
                    fragmentTransaction.hide(add);
                    fragmentTransaction.hide(shop);
                    fragmentTransaction.hide(user);
                    fragmentTransaction.commit();
                    break;
                case R.id.search:
                    fragmentTransaction.show(search);
                    fragmentTransaction.hide(home);
                    fragmentTransaction.hide(add);
                    fragmentTransaction.hide(shop);
                    fragmentTransaction.hide(user);
                    fragmentTransaction.commit();
                    break;
                case R.id.add:
                    fragmentTransaction.show(add);
                    fragmentTransaction.hide(home);
                    fragmentTransaction.hide(search);
                    fragmentTransaction.hide(shop);
                    fragmentTransaction.hide(user);
                    fragmentTransaction.commit();
                    break;
                case R.id.shop:
                    fragmentTransaction.show(shop);
                    fragmentTransaction.hide(home);
                    fragmentTransaction.hide(search);
                    fragmentTransaction.hide(add);
                    fragmentTransaction.hide(user);
                    fragmentTransaction.commit();
                    break;
                case R.id.user:
                    fragmentTransaction.show(user);
                    fragmentTransaction.hide(home);
                    fragmentTransaction.hide(search);
                    fragmentTransaction.hide(add);
                    fragmentTransaction.hide(shop);
                    fragmentTransaction.commit();
                    break;
                default:
                    Log.e("BaseActivity","onNavigationItemSelectedError");
                    break;
            }
            return false;
        }
    };

    private void initFragment(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.frame, home);
        fragmentTransaction.add(R.id.frame, search);
        fragmentTransaction.add(R.id.frame, add);
        fragmentTransaction.add(R.id.frame, shop);
        fragmentTransaction.add(R.id.frame, user);
        fragmentTransaction.hide(search);
        fragmentTransaction.hide(add);
        fragmentTransaction.hide(shop);
        fragmentTransaction.hide(user);
        fragmentTransaction.commit();
    }
}
