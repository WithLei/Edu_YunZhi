package com.android.renly.edu_yunzhi;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi.Activity.LoginActivity;
import com.android.renly.edu_yunzhi.Activity.PusherActivity;
import com.android.renly.edu_yunzhi.Common.AppManager;
import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.Fragment.HomeFragment;
import com.android.renly.edu_yunzhi.Fragment.LearningFragment;
import com.android.renly.edu_yunzhi.Fragment.MineFragment;
import com.android.renly.edu_yunzhi.Fragment.MsgFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MainActivity extends BaseActivity {

    @BindView(R.id.iv_main_bottom_mainpage)
    ImageView ivMainBottomMainpage;
    @BindView(R.id.tv_main_bottom_mainpage)
    TextView tvMainBottomMainpage;
    @BindView(R.id.fl_main)
    FrameLayout flMain;
    @BindView(R.id.ll_main_bottom_mainpage)
    LinearLayout llMainBottomMainpage;
    @BindView(R.id.iv_main_bottom_learning)
    ImageView ivMainBottomLearning;
    @BindView(R.id.tv_main_bottom_learning)
    TextView tvMainBottomLearning;
    @BindView(R.id.ll_main_bottom_learning)
    LinearLayout llMainBottomLearning;
    @BindView(R.id.iv_main_bottom_msg)
    ImageView ivMainBottomMsg;
    @BindView(R.id.tv_main_bottom_msg)
    TextView tvMainBottomMsg;
    @BindView(R.id.ll_main_bottom_msg)
    LinearLayout llMainBottomMsg;
    @BindView(R.id.iv_main_bottom_mine)
    ImageView ivMainBottomMine;
    @BindView(R.id.tv_main_bottom_mine)
    TextView tvMainBottomMine;
    @BindView(R.id.ll_main_bottom_mine)
    LinearLayout llMainBottomMine;
    @BindView(R.id.iv_main_bottom_live)
    ImageView ivMainBottomLive;
    private View LiveDialogView;
    private EditText et_roomName;
    private FragmentTransaction transaction;

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
    }


    @Override
    protected void initData() {
        initView();
        //将当前的Activity添加到栈管理中
        AppManager.getInstance().addActivity(this);

        //默认显示首页
        setSelect(0);
    }

    private void initView() {
        if (isStudent()){
            ivMainBottomLive.setVisibility(View.GONE);
        }else{
            ivMainBottomLive.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.ll_main_bottom_mainpage, R.id.ll_main_bottom_learning, R.id.ll_main_bottom_msg, R.id.ll_main_bottom_mine, R.id.iv_main_bottom_live})
    public void showTab(View view) {
//        Toast.makeText(MainActivity.this,"响应",Toast.LENGTH_SHORT).show();
        SharedPreferences sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String name = sp.getString("username", "");
        if (!TextUtils.isEmpty(name)) {
            switch (view.getId()) {
                case R.id.ll_main_bottom_mainpage:
                    setSelect(0);
                    break;
                case R.id.ll_main_bottom_learning:
                    setSelect(1);
                    break;
                case R.id.ll_main_bottom_msg:
                    setSelect(2);
                    break;
                case R.id.ll_main_bottom_mine:
                    setSelect(3);
                    break;
                case R.id.iv_main_bottom_live:
                    startLive();
                    break;
            }
        } else
            doLogin();
    }

    private void startLive() {
        initDialogView();
        new AlertDialog.Builder(this)
                .setView(LiveDialogView)
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        gotoPusherActivity();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setTitle("新建直播间")
                .create()
                .show();
    }

    private void initDialogView(){
        //初始化Dialog的视图
        LiveDialogView = View.inflate(MainActivity.this,R.layout.dialog_live,null);
        et_roomName = (EditText) LiveDialogView.findViewById(R.id.et_roomName);
    }

    private void gotoPusherActivity(){
        String roomName = et_roomName.getText().toString();
        if (!roomName.equals("")){
            Bundle bundle = new Bundle();
            bundle.putString("RoomName", roomName);

            Message msg = new Message();
            msg.setData(bundle);
            msg.what = GOTO_PUSHERACTIVITY;

            Log.e("log","test1");
            handler.sendMessage(msg);
            Log.e("log","test2");
        }else{
            Toast.makeText(this, "直播名称不能为空", Toast.LENGTH_SHORT).show();
        }

    }

    private HomeFragment homeFragment;
    private LearningFragment learningFragment;
    private MsgFragment msgFragment;
    private MineFragment mineFragment;

    private void setSelect(int select) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        //隐藏所有fragment的显示
        hideFragments();
        switch (select) {
            case 0:
                Log.e("print",homeFragment == null ? "true" : "false");
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();//commit()后调用生命周期方法
                    transaction.add(R.id.fl_main, homeFragment);
                }
                transaction.show(homeFragment);//显示当前的Fragment

                //改变图片和文字颜色
                ivMainBottomMainpage.setImageResource(R.drawable.homepageblue);
                tvMainBottomMainpage.setTextColor(getResources().getColor(R.color.home_back_selected));
                break;
            case 1:
                Log.e("print",learningFragment == null ? "true" : "false");
                if (learningFragment == null) {
                    learningFragment = new LearningFragment();//commit()后调用生命周期方法
                    transaction.add(R.id.fl_main, learningFragment);
                }
                transaction.show(learningFragment);//显示当前的Fragment


                //改变图片和文字颜色
                ivMainBottomLearning.setImageResource(R.drawable.questionsblue);
                tvMainBottomLearning.setTextColor(getResources().getColor(R.color.home_back_selected));
                break;
            case 2:
                Log.e("print",msgFragment == null ? "true" : "false");
                if (msgFragment == null) {
                    msgFragment = new MsgFragment();//commit()后调用生命周期方法
                    transaction.add(R.id.fl_main, msgFragment);
                }
                transaction.show(msgFragment);//显示当前的Fragment

                //改变图片和文字颜色
                ivMainBottomMsg.setImageResource(R.drawable.interactiveblue);
                tvMainBottomMsg.setTextColor(getResources().getColor(R.color.home_back_selected));
                break;
            case 3:
                Log.e("print",mineFragment == null ? "true" : "false");
                if (mineFragment == null) {
                    mineFragment = new MineFragment();//commit()后调用生命周期方法
                    transaction.add(R.id.fl_main, mineFragment);
                }
                transaction.show(mineFragment);//显示当前的Fragment

                //改变图片和文字颜色
                ivMainBottomMine.setImageResource(R.drawable.mine_fill);
                tvMainBottomMine.setTextColor(getResources().getColor(R.color.home_back_selected));
                break;
        }
        transaction.commit();//提交事务
    }

    private void hideFragments() {
        ivMainBottomMainpage.setImageResource(R.drawable.homepage);
        ivMainBottomLearning.setImageResource(R.drawable.questions);
        ivMainBottomMsg.setImageResource(R.drawable.interactive);
        ivMainBottomMine.setImageResource(R.drawable.mine);
        tvMainBottomMainpage.setTextColor(getResources().getColor(R.color.bottom_unselect));
        tvMainBottomLearning.setTextColor(getResources().getColor(R.color.bottom_unselect));
        tvMainBottomMsg.setTextColor(getResources().getColor(R.color.bottom_unselect));
        tvMainBottomMine.setTextColor(getResources().getColor(R.color.bottom_unselect));
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (learningFragment != null) {
            transaction.hide(learningFragment);
        }
        if (msgFragment != null) {
            transaction.hide(msgFragment);
        }
        if (mineFragment != null) {
            transaction.hide(mineFragment);
        }

    }

    //重写onkeyup()

    private static final int WHAT_RESET_BACK = 1;
    private static final int GOTO_PUSHERACTIVITY = 2;

    private boolean flag = true;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_RESET_BACK:
                    flag = true;//复原
                    break;
                case GOTO_PUSHERACTIVITY:
                    Log.e("log","test3");
                    String PusherRoomName = msg.getData().getString("RoomName");
                    Bundle pusherBundle = new Bundle();
                    pusherBundle.putString("RoomName",PusherRoomName);
                    Log.e("log","test4");

                    Intent pusherIntent = new Intent(MainActivity.this,PusherActivity.class);
                    pusherIntent.putExtras(pusherBundle);
                    startActivity(pusherIntent);
                    break;
            }
        }
    };

    //为了避免出现内存的泄露，需要在onDestroy()中，移除所有未被执行的消息
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        //移除所有的未被执行的消息
        handler.removeCallbacksAndMessages(null);
        try{
            FragmentManager frgManager = this.getSupportFragmentManager();
            FragmentTransaction transaction = frgManager.beginTransaction();
            transaction.remove(homeFragment);
            transaction.remove(learningFragment);
            transaction.remove(msgFragment);
            transaction.remove(mineFragment);
            transaction.commit();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && flag) {
            Toast.makeText(this, "再点击一次退出当前应用", Toast.LENGTH_SHORT).show();
            flag = false;
            //发送延迟消息
            handler.sendEmptyMessageDelayed(WHAT_RESET_BACK, 2000);
            return true;
        }

        return super.onKeyUp(keyCode, event);
    }

    public void refresh(){
        onCreate(null);
    }

    public void gotoHomeFragment(){
        initView();
        setSelect(0);
    }
    public void gotoLearningFragment() {
        setSelect(1);
    }
    public void gotoMsgFragment(){setSelect(2);}
    public void gotoMineFragment(){setSelect(3);}

    private boolean isStudent() {
        //查看本地是否有用户的登录信息
        SharedPreferences sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        return sp.getBoolean("isStudent", false);
    }

    //给出提示：登录
    private void doLogin() {
        new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("您还没有登录哦！亲(^_−)−☆")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                            UIUtils.toast("进入登录页面",false);
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                })
                .setCancelable(true)
                .show();
    }

    //当出现未捕获的异常时，能够给用户一个相对友好的提示
    //在出现异常时，能够将异常信息发送给后台，便于在后续的版本中解决Bug
//    @Override
//    public void UncaughtException(Thread thread,Throwable ex){
//        new Thread(){
//            public void run(){
//                Looper.prepare();
//                Toast.makeText(MainActivity.this, "出现了未捕获的异常", Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//        }.start();
//
//        //收集异常信息
//        collectionException(ex);
//
//        try{
//            Thread.sleep(2000);
//
//            //移除当前Activity
//            ActivityManager.getInstance().removeCurrent();
//            //结束当前的进程
//            android.os.Process.killProcess(android.os.Process.myPid());
//            //结束虚拟机
//            System.exit(0);
//        }
//        catch(InterruptedException e){
//            e.printStackTrace();
//        }
//
//    }
//
//    private void collectionException(final Throwable ex) {
//        final String exMessage = ex.getMessage();
//        //收集具体的用户的手机的信息
//        String message = Build.DEVICE + ":" + Build.MODEL + ":" + Build.PRODUCT;
//        //发送给后台此异常信息
//        new Thread(){
//            @Override
//            public void run() {
//                Log.e("TAG","test",ex);
//            }
//        };
//    }

}

