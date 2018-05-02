package com.android.renly.edu_yunzhi_teacher;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi_teacher.Common.AppManager;
import com.android.renly.edu_yunzhi_teacher.Common.BaseActivity;
import com.android.renly.edu_yunzhi_teacher.Fragment.HomeFragment;
import com.android.renly.edu_yunzhi_teacher.Fragment.LearningFragment;
import com.android.renly.edu_yunzhi_teacher.Fragment.MineFragment;
import com.android.renly.edu_yunzhi_teacher.Fragment.MsgFragment;

import butterknife.Bind;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Bind(R.id.iv_main_bottom_mainpage)
    ImageView ivMainBottomMainpage;
    @Bind(R.id.tv_main_bottom_mainpage)
    TextView tvMainBottomMainpage;
    @Bind(R.id.fl_main)
    FrameLayout flMain;
    @Bind(R.id.ll_main_bottom_mainpage)
    LinearLayout llMainBottomMainpage;
    @Bind(R.id.iv_main_bottom_learning)
    ImageView ivMainBottomLearning;
    @Bind(R.id.tv_main_bottom_learning)
    TextView tvMainBottomLearning;
    @Bind(R.id.ll_main_bottom_learning)
    LinearLayout llMainBottomLearning;
    @Bind(R.id.iv_main_bottom_msg)
    ImageView ivMainBottomMsg;
    @Bind(R.id.tv_main_bottom_msg)
    TextView tvMainBottomMsg;
    @Bind(R.id.ll_main_bottom_msg)
    LinearLayout llMainBottomMsg;
    @Bind(R.id.iv_main_bottom_mine)
    ImageView ivMainBottomMine;
    @Bind(R.id.tv_main_bottom_mine)
    TextView tvMainBottomMine;
    @Bind(R.id.ll_main_bottom_mine)
    LinearLayout llMainBottomMine;
    private FragmentTransaction transaction;

    @Override
    protected void initData() {
        //将当前的Activity添加到栈管理中
        AppManager.getInstance().addActivity(this);

        //默认显示首页
        setSelect(0);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @OnClick({R.id.ll_main_bottom_mainpage, R.id.ll_main_bottom_learning, R.id.ll_main_bottom_msg, R.id.ll_main_bottom_mine})
    public void showTab(View view) {
//        Toast.makeText(MainActivity.this,"响应",Toast.LENGTH_SHORT).show();
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
        }
    }

    private HomeFragment homeFragment;
    private LearningFragment learningFragment;
    private MsgFragment msgFragment;
    private MineFragment mineFragment;

    private void setSelect(int select) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();

        //隐藏所有fragment的显示
        hideFragments();
        switch (select) {
            case 0:
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
    private boolean flag = true;
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_RESET_BACK:
                    flag = true;//复原
                    break;
            }
        }
    };

    //为了避免出现内存的泄露，需要在onDestroy()中，移除所有未被执行的消息
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //移除所有的未被执行的消息
        handler.removeCallbacksAndMessages(null);
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

    public void gotoLearningFragment(){
        setSelect(1);
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

