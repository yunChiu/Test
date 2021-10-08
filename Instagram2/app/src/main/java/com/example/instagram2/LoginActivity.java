package com.example.instagram2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.instagram2.Model.DataStatus;
import com.example.instagram2.Model.Login;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    EditText et_account, et_password;
    TextView tv_login, tv_signup;

    DataStatus dataStatus = new DataStatus();
    Login login = new Login();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView(){
        setContentView(R.layout.activity_login);
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        tv_login = findViewById(R.id.tv_login);
        tv_login.setOnClickListener(onClickListener);
        tv_signup = findViewById(R.id.tv_signup);
        tv_signup.setOnClickListener(onClickListener);
    }

    public View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_login:
                    if (et_account.getText().toString().isEmpty())
                        Toast.makeText(LoginActivity.this,"請輸入帳號",Toast.LENGTH_SHORT).show();
                    else if (et_password.getText().toString().isEmpty())
                        Toast.makeText(LoginActivity.this,"請輸入密碼",Toast.LENGTH_SHORT).show();
                    else
                        DataManager.login(dataCallback, et_account.getText().toString(), et_password.getText().toString());
                    break;
                case R.id.tv_signup:
                    DataManager.logout(dataCallback_logout, login.getAccessToken());
                    break;
                default:
                    Log.e("LoginActivity","onClickListenerError");
                    break;
            }
        }
    };

    private DataCallback dataCallback = new DataCallback() {
        @Override
        public void onDataSuccess(String data) {
            getLoginData(data);
            if (!dataStatus.getSuccess()) {
                Toast.makeText(LoginActivity.this,"登入失敗",Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity_f",dataStatus.getMessage());
            }else {
                Toast.makeText(LoginActivity.this,"登入成功",Toast.LENGTH_SHORT).show();
                Log.e("LoginActivity_s",data);
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
            }
        }

        @Override
        public void onDataFail(String msg) {
            Log.e("DataCallback","onDataFail" + msg);
            Toast.makeText(LoginActivity.this,"登入失敗：" + msg,Toast.LENGTH_SHORT).show();
        }
    };

    private DataCallback dataCallback_logout = new DataCallback() {
        @Override
        public void onDataSuccess(String data) {
            try {
                if (new JSONObject(data).getBoolean("Success"))
                    Toast.makeText(LoginActivity.this,"已登出",Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(LoginActivity.this,"登出失敗：" + new JSONObject(data).getString("Message"),Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                Log.e("DataCallback","onDataSuccess" + e.getMessage());
            }
        }

        @Override
        public void onDataFail(String msg) {
            Log.e("DataCallback","onDataFail" + msg);
            Toast.makeText(LoginActivity.this,"登出失敗：" + msg,Toast.LENGTH_SHORT).show();
        }
    };

    public void getLoginData(String data) {
        try {
            /*
            if (!new JSONObject(data).getBoolean("Success"))
                return;
             */
            dataStatus.setMessage(new JSONObject(data).getString("Message"));
            dataStatus.setReturnCode(new JSONObject(data).getString("ReturnCode"));
            dataStatus.setSuccess(new JSONObject(data).getBoolean("Success"));
            JSONObject jsonObject = new JSONObject(data).getJSONObject("Result");
            login.setId(jsonObject.getInt("id"));
            login.setUserName(jsonObject.getString("userName"));
            login.setPassword(jsonObject.getString("password"));
            login.setEmail(jsonObject.getString("email"));
            login.setAccessToken(jsonObject.getString("accessToken"));
            login.setRefreshToken(jsonObject.getString("refreshToken"));
        } catch (JSONException e) {
            Log.e("LoginActivity","getUserData-"+e.getMessage());
        }
    }
}
