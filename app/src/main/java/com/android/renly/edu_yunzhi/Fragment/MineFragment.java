package com.android.renly.edu_yunzhi.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.renly.edu_yunzhi.Activity.LoadFragmentActivity;
import com.android.renly.edu_yunzhi.Activity.UserInfoActivity;
import com.android.renly.edu_yunzhi.Bean.User;
import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.Common.BaseFragment;
import com.android.renly.edu_yunzhi.Common.MyApplication;
import com.android.renly.edu_yunzhi.MainActivity;
import com.android.renly.edu_yunzhi.R;
import com.android.renly.edu_yunzhi.Utils.BitmapUtils;
import com.android.renly.edu_yunzhi.Utils.UIUtils;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MineFragment extends BaseFragment {

    @Bind(R.id.iv_mine_icon)
    ImageView ivMineIcon;
    @Bind(R.id.rl_mine_icon)
    RelativeLayout rlMineIcon;
    @Bind(R.id.tv_mine_name)
    TextView tvMineName;
    @Bind(R.id.rl_mine)
    RelativeLayout rlMine;
    @Bind(R.id.tv_mine_class)
    TextView tvMineClass;
    @Bind(R.id.tv_mine_task)
    TextView tvMineTask;
    @Bind(R.id.tv_mine_grade)
    TextView tvMineGrade;
    @Bind(R.id.tv_mine_logout)
    TextView tvMineLogout;

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
        //判断用户是否已经登录
        isLogin();
    }

    @Override
    public int getLayoutid() {
        return R.layout.fragment_mine;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void isLogin() {
        //查看本地是否有用户的登录信息
        SharedPreferences sp = this.getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String name = sp.getString("name", "");
        if (TextUtils.isEmpty(name)) {
            //本地没有保存过用户信息，给出提示：登录
            doLogin();

        } else {
            //已经登录过，则直接加载用户的信息并显示
            tvMineLogout.setVisibility(View.VISIBLE);
            doUser();
        }

    }

    private boolean userisLogin() {
        //查看本地是否有用户的登录信息
        SharedPreferences sp = this.getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String name = sp.getString("name", "");
        if (TextUtils.isEmpty(name)) {
            //本地没有保存过用户信息，给出提示：登录
            return false;

        } else {
            //已经登录过，则直接加载用户的信息并显示
            return true;
        }

    }

    //加载用户信息并显示
    private void doUser() {

        //1.读取本地保存的用户信息
        User user = ((BaseActivity) this.getActivity()).readUser();
        //2.获取对象信息，并设置给相应的视图显示。
        tvMineName.setText(user.getUsername());

        //判断本地是否已经保存头像的图片，如果有，则不再执行联网操作
        boolean isExist = readImage();
        if (isExist) {
            return;
        }

        //使用Picasso联网获取图片
        Picasso.with(this.getActivity()).load(user.getImgUrl()).transform(new Transformation() {
            @Override
            public Bitmap transform(Bitmap source) {//下载以后的内存中的bitmap对象
                //压缩处理
                Bitmap bitmap = BitmapUtils.zoom(source, UIUtils.dp2px(62), UIUtils.dp2px(62));
                //圆形处理
                bitmap = BitmapUtils.circleBitmap(bitmap);
                //回收bitmap资源
                source.recycle();
                return bitmap;
            }

            @Override
            public String key() {
                return "";//需要保证返回值不能为null。否则报错
            }
        }).into(ivMineIcon);

    }

    //给出提示：登录
    private void doLogin() {
        new AlertDialog.Builder(this.getActivity())
                .setTitle("提示")
                .setMessage("您还没有登录哦！么么~")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                            UIUtils.toast("进入登录页面",false);
                        LoadFragmentActivity.lunchFragment(MyApplication.context, LoginFragment.class, null);
                    }
                })
                .setCancelable(false)
                .show();
    }

    @OnClick(R.id.iv_mine_icon)
    public void setting(View view) {
        //启动用户信息界面的Activity
        if (userisLogin())
            ((BaseActivity) this.getActivity()).goToActivity(UserInfoActivity.class, null);
        else
            LoadFragmentActivity.lunchFragment(MyApplication.context, LoginFragment.class, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG", "onResume()");

        //读取本地保存的图片
        readImage();
    }

    private boolean readImage() {
        File filesDir;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {//判断sd卡是否挂载
            //路径1：storage/sdcard/Android/data/包名/files
            filesDir = this.getActivity().getExternalFilesDir("");

        } else {//手机内部存储
            //路径：data/data/包名/files
            filesDir = this.getActivity().getFilesDir();

        }
        File file = new File(filesDir, "icon.png");
        if (file.exists()) {
            //存储--->内存
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            ivMineIcon.setImageBitmap(bitmap);
            return true;
        }
        return false;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.tv_mine_class, R.id.tv_mine_task, R.id.tv_mine_grade, R.id.tv_mine_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_mine_class://课程管理
                MainActivity mainActivity = (MainActivity) getActivity();
//                LearningFragment learningFragment = getFragmentManager().findFragmentById(R.layout.fragment_myclass);
                mainActivity.gotoLearningFragment();

                break;
            case R.id.tv_mine_task://实训任务
                break;
            case R.id.tv_mine_grade://成绩查询
                break;
            case R.id.tv_mine_logout://退出登录
                break;
        }
    }
}
