package com.android.renly.edu_yunzhi.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.R;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyInfoActivity extends BaseActivity {
    @BindView(R.id.tv_myinfo_headphoto)
    TextView tvMyinfoHeadphoto;
    @BindView(R.id.iv_myinfo_headphoto)
    ImageView ivMyinfoHeadphoto;
    @BindView(R.id.fl_myinfo_headphoto)
    FrameLayout flMyinfoHeadphoto;
    @BindView(R.id.tv_myinfo_name)
    TextView tvMyinfoName;
    @BindView(R.id.tv_myinfo_setName)
    TextView tvMyinfoSetName;
    @BindView(R.id.fl_myinfo_name)
    FrameLayout flMyinfoName;
    @BindView(R.id.tv_myinfo_realName)
    TextView tvMyinfoRealName;
    @BindView(R.id.tv_myinfo_setRealName)
    TextView tvMyinfoSetRealName;
    @BindView(R.id.fl_myinfo_realName)
    FrameLayout flMyinfoRealName;
    @BindView(R.id.tv_myinfo_school)
    TextView tvMyinfoSchool;
    @BindView(R.id.tv_myinfo_setSchool)
    TextView tvMyinfoSetSchool;
    @BindView(R.id.fl_myinfo_school)
    FrameLayout flMyinfoSchool;
    @BindView(R.id.tv_myinfo_qr)
    TextView tvMyinfoQr;
    @BindView(R.id.tv_myinfo_more)
    TextView tvMyinfoMore;
    @BindView(R.id.tv_myinfo_addr)
    TextView tvMyinfoAddr;
    @BindView(R.id.tv_title_titleName)
    TextView tvTitleTitleName;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.ll_title_back)
    LinearLayout llTitleBack;
    @BindView(R.id.tv_title_back)
    TextView tvTitleBack;
    @BindView(R.id.fl_myinfo_title)
    FrameLayout flMyinfoTitle;

    @Override
    protected void initData() {
        Intent intent = getIntent();
        String backInfo = intent.getStringExtra("backInfo");
        tvTitleBack.setText(backInfo);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_myinfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvTitleRight.setVisibility(View.GONE);
        SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        String username = sp.getString("username", "");
        String realName = sp.getString("realName", "");
        String schoolName = sp.getString("schoolName", "");
        String avatarSrc = sp.getString("avatarSrc", "");
        boolean isStudent = sp.getBoolean("isStudent", false);

        if (isStudent) {
            //学生角色
            flMyinfoTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            //教师角色
            flMyinfoTitle.setBackgroundColor(getResources().getColor(R.color.colorTeacherPrimary));
        }
        tvMyinfoSetName.setText(username);
        tvMyinfoSetRealName.setText(realName);
        tvMyinfoSetSchool.setText(schoolName);
        Picasso.with(this).load(avatarSrc).into(ivMyinfoHeadphoto);
    }

    @OnClick({R.id.ll_title_back, R.id.fl_myinfo_headphoto, R.id.fl_myinfo_name, R.id.tv_myinfo_qr, R.id.tv_myinfo_more, R.id.tv_myinfo_addr})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_title_back:
                finish();
                break;
            case R.id.fl_myinfo_headphoto:
                intent = new Intent(MyInfoActivity.this, setInfoActivity.class);
                intent.putExtra("titleName", "个人头像");
                startActivity(intent);
                break;
            case R.id.fl_myinfo_name:
                intent = new Intent(MyInfoActivity.this, setInfoActivity.class);
                intent.putExtra("titleName", "设置名字");
                startActivity(intent);
                break;
            case R.id.tv_myinfo_qr:
                intent = new Intent(MyInfoActivity.this, setInfoActivity.class);
                intent.putExtra("titleName", "我的二维码");
                startActivity(intent);
                break;
            case R.id.tv_myinfo_more:
                Toast.makeText(this, "施工中", Toast.LENGTH_SHORT).show();
                break;
            case R.id.tv_myinfo_addr:
                intent = new Intent(this, setInfoActivity.class);
                intent.putExtra("titleName", "我的地址");
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initView();
    }
}
