package com.android.renly.edu_yunzhi.Fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.renly.edu_yunzhi.Activity.LoadFragmentActivity;
import com.android.renly.edu_yunzhi.Activity.LoginActivity;
import com.android.renly.edu_yunzhi.Common.BaseFragment;
import com.android.renly.edu_yunzhi.Common.MyApplication;
import com.android.renly.edu_yunzhi.R;
import com.android.renly.edu_yunzhi.UI.BatchRadioButton;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class LearningFragment extends BaseFragment{

    @BindView(R.id.btn_1)
    BatchRadioButton btn1;
    @BindView(R.id.btn_2)
    BatchRadioButton btn2;
    @BindView(R.id.btn_3)
    BatchRadioButton btn3;
    @BindView(R.id.btn_change)
    RadioGroup btnChange;
    @BindView(R.id.fl_learning)
    FrameLayout flLearning;

    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    protected void initData(String content) {
        //默认显示首页
        setSelect(0);
        //登录判断
//        isLogin();
    }

    private static MyclassFragment myclassFragment;
    private static MyworkFragment myworkFragment;
    private static MynoteFragment mynoteFragment;
    private FragmentTransaction transaction;

    private void setSelect(int select) {
        FragmentManager fragmentManager = getChildFragmentManager();
        transaction = fragmentManager.beginTransaction();
        //隐藏所有fragment
        hideFragments();
        switch (select) {
            case 0:
                if (myclassFragment == null) {
                    myclassFragment = new MyclassFragment();//commit()后调用生命周期方法
                    transaction.add(R.id.fl_learning, myclassFragment);
                }
                transaction.show(myclassFragment);//显示当前的Fragment
                break;
            case 1:
                if (myworkFragment == null) {
                    myworkFragment = new MyworkFragment();//commit()后调用生命周期方法
                    transaction.add(R.id.fl_learning, myworkFragment);
                }
                transaction.show(myworkFragment);//显示当前的Fragment
                break;
            case 2:
                if (mynoteFragment == null) {
                    mynoteFragment = new MynoteFragment();//commit()后调用生命周期方法
                    transaction.add(R.id.fl_learning, mynoteFragment);
                }
                transaction.show(mynoteFragment);//显示当前的Fragment
                break;
        }
        //提交事务
        transaction.commit();
    }

    private void hideFragments() {
        if (myclassFragment != null) {
            transaction.hide(myclassFragment);
        }
        if (myworkFragment != null) {
            transaction.hide(myworkFragment);
        }
        if (mynoteFragment != null) {
            transaction.hide(mynoteFragment);
        }
    }

    @Override
    public int getLayoutid() {
        return R.layout.fragment_learning;
    }

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        btn1.setText("课程");
        btn2.setText("作业");
        btn3.setText("笔记");
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        //移除所有的未被执行的消息
//        handler.removeCallbacksAndMessages(null);
        try{
            FragmentManager frgManager = getChildFragmentManager();
            FragmentTransaction transaction = frgManager.beginTransaction();
            transaction.remove(myclassFragment);
            transaction.remove(myworkFragment);
            transaction.remove(mynoteFragment);
            transaction.commit();
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_1, R.id.btn_2, R.id.btn_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                setSelect(0);
                break;
            case R.id.btn_2:
                setSelect(1);
                break;
            case R.id.btn_3:
                setSelect(2);
                break;
        }
    }

    private void isLogin() {
        //查看本地是否有用户的登录信息
        SharedPreferences sp = this.getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String name = sp.getString("name", "");
        if (TextUtils.isEmpty(name)) {
            //本地没有保存过用户信息，给出提示：登录
            doLogin();
        }
    }

    //给出提示：登录
    private void doLogin() {
        Toast.makeText(MyApplication.context, "未登录", Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(this.getActivity())
                .setTitle("提示")
                .setMessage("您还没有登录哦！么么~")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                            UIUtils.toast("进入登录页面",false);
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                })
                .setCancelable(false)
                .show();
    }

    public void gotoClassFragment(){
        setSelect(0);
    }

}
