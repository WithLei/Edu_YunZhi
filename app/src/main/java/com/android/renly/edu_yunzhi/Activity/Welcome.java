package com.android.renly.edu_yunzhi.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.renly.edu_yunzhi.Common.AppManager;
import com.android.renly.edu_yunzhi.MainActivity;
import com.android.renly.edu_yunzhi.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Welcome extends Activity {

    @BindView(R.id.iv_welcome_icon)
    ImageView ivWelcomeIcon;
    @BindView(R.id.rl_welcome)
    RelativeLayout rlWelcome;

    public static final int LEAVE_WELCOMEPAGE = 3;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LEAVE_WELCOMEPAGE:
//                    finish();
                    Intent intent = new Intent(Welcome.this, MainActivity.class);
                    startActivity(intent);
                    AppManager.getInstance().remove(Welcome.this);
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 去掉窗口标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        if (getSupportActionBar() != null){
//            getSupportActionBar().hide();
//        }
        // 隐藏顶部的状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        //将当前的activity添加到ActivityManager中
        AppManager.getInstance().addActivity(this);

        ButterKnife.bind(this);
        handler.sendEmptyMessageDelayed(LEAVE_WELCOMEPAGE,5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}

