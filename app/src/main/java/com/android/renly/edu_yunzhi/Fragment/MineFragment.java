package com.android.renly.edu_yunzhi.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi.Activity.LoadFragmentActivity;
import com.android.renly.edu_yunzhi.Activity.LoginActivity;
import com.android.renly.edu_yunzhi.Activity.MyInfoActivity;
import com.android.renly.edu_yunzhi.Activity.TaskActivity;
import com.android.renly.edu_yunzhi.Activity.UserInfoActivity;
import com.android.renly.edu_yunzhi.Bean.MessageEvent;
import com.android.renly.edu_yunzhi.Bean.User;
import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.Common.BaseFragment;
import com.android.renly.edu_yunzhi.Common.MyApplication;
import com.android.renly.edu_yunzhi.MainActivity;
import com.android.renly.edu_yunzhi.R;
import com.android.renly.edu_yunzhi.UI.CircleImageView;
import com.android.renly.edu_yunzhi.Utils.BitmapUtils;
import com.android.renly.edu_yunzhi.Utils.UIUtils;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MineFragment extends BaseFragment {

    @BindView(R.id.iv_mine_icon)
    CircleImageView ivMineIcon;
    @BindView(R.id.rl_mine_icon)
    RelativeLayout rlMineIcon;
    @BindView(R.id.tv_mine_name)
    TextView tvMineName;
    @BindView(R.id.rl_mine)
    RelativeLayout rlMine;
    @BindView(R.id.tv_mine_class)
    TextView tvMineClass;
    @BindView(R.id.tv_mine_task)
    TextView tvMineTask;
    @BindView(R.id.tv_mine_grade)
    TextView tvMineGrade;
    @BindView(R.id.tv_mine_logout)
    TextView tvMineLogout;
    @BindView(R.id.tv_mine_school)
    TextView tvMineSchool;

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
        EventBus.getDefault().unregister(this);
        unbinder.unbind();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Event(MessageEvent messageEvent) {
        ivMineIcon.setImageDrawable(getResources().getDrawable(R.drawable.user1));
        SharedPreferences sp = getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String realName = sp.getString("realName", "");
        boolean isStudent = sp.getBoolean("isStudent",false);
        tvMineLogout.setVisibility(View.VISIBLE);
        switch (messageEvent.getMessage()){
            case "studentLogin":
                tvMineName.setText(realName + " 同学，你好");
                break;
            case "teacherLogin":
                tvMineName.setText(realName + " 老师，您好");
                break;
        }

    }

    private void isLogin() {
        //查看本地是否有用户的登录信息
        SharedPreferences sp = this.getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String name = sp.getString("username", "");
        if (TextUtils.isEmpty(name)) {
            //本地没有保存过用户信息，给出提示：登录
            doLogin();

        } else {
            //已经登录过，则直接加载用户的信息并显示
            boolean isStudent = sp.getBoolean("isStudent",false);
            tvMineLogout.setVisibility(View.VISIBLE);
            ivMineIcon.setImageDrawable(getResources().getDrawable(R.drawable.user1));
            String realName = sp.getString("realName","");
            String schoolName = sp.getString("schoolName","");
            Log.e("print","realName[" + realName + "]");
            Log.e("print","schoolName[" + schoolName + "]");
            if(isStudent){
                //学生登录
                tvMineName.setText(realName + " 同学，你好");
                tvMineSchool.setText(schoolName);
            }
            else{
                //老师登录
                tvMineName.setText(realName + " 老师，您好");
                tvMineSchool.setText(schoolName);
            }
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
                .setMessage("您还没有登录哦！亲(^_−)−☆")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                            UIUtils.toast("进入登录页面",false);
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }
                })
                .setCancelable(true)
                .show();
    }

    @OnClick(R.id.iv_mine_icon)
    public void setting(View view) {
        //启动用户信息界面的Activity
        SharedPreferences sp = this.getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String name = sp.getString("username", "");
        if (TextUtils.isEmpty(name)) {
            Toast.makeText(MyApplication.context, "打开登录页面", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            getActivity().finish();
        } else
            ((BaseActivity) this.getActivity()).goToActivity(MyInfoActivity.class, null);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("TAG", "mine.onResume()");

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

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        EventBus.getDefault().register(this);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.tv_mine_class, R.id.tv_mine_task, R.id.tv_mine_grade, R.id.tv_mine_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_mine_class://课程管理
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.gotoLearningFragment();
//                LearningFragment learningFragment = (LearningFragment) getFragmentManager().findFragmentById(R.layout.fragment_myclass);

                break;
            case R.id.tv_mine_task://实训任务
                ((BaseActivity) this.getActivity()).goToActivity(TaskActivity.class, null);
                break;
            case R.id.tv_mine_grade://成绩查询
                break;
            case R.id.tv_mine_logout://退出登录
                SharedPreferences sharedPre = getContext().getSharedPreferences("user_info", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPre.edit();
                editor.clear();
                editor.apply();

                startActivity(new Intent(getContext(), LoginActivity.class));
                getActivity().finish();
                Toast.makeText(MyApplication.context, "退出登录", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
