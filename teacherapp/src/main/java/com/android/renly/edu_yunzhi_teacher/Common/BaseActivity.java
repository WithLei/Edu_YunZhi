package com.android.renly.edu_yunzhi_teacher.Common;


import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.android.renly.edu_yunzhi_teacher.Bean.User;
import com.loopj.android.http.AsyncHttpClient;

import butterknife.ButterKnife;

/**
 * Created by renly
 */
public abstract class BaseActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(getLayoutId());
        ButterKnife.bind(this);

        //将当前的activity添加到ActivityManager中
        AppManager.getInstance().addActivity(this);

        initData();

    }

    protected abstract void initData();

    protected abstract int getLayoutId();

    public AsyncHttpClient client = new AsyncHttpClient();

    //启动新的activity
    public void goToActivity(Class Activity,Bundle bundle){
        Intent intent = new Intent(this,Activity);
        //携带数据
        if(bundle != null && bundle.size() != 0){
            intent.putExtra("data",bundle);
        }

        startActivity(intent);
    }

    //销毁当前的Activity
    public void removeCurrentActivity(){
        AppManager.getInstance().removeCurrent();
    }

    //销毁所有的activity
    public void removeAll(){
        AppManager.getInstance().removeAll();
    }

    //保存用户信息
    public void saveUser(User user){
        SharedPreferences sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("name",user.getUsername());
        editor.putString("imageurl",user.getImgUrl());
        editor.putString("phone",user.getPhone());
        editor.commit();//必须提交，否则保存不成功
    }

    //读取用户信息
    public User readUser(){
        SharedPreferences sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        User user = new User();
        user.setUsername(sp.getString("name",""));
        user.setImgUrl(sp.getString("imageurl", ""));
        user.setPhone(sp.getString("phone", ""));

        return user;
    }
}

