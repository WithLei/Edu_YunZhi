package com.android.renly.edu_yunzhi.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class setInfoActivity extends BaseActivity {
    @BindView(R.id.tv_title_back)
    TextView tvTitleBack;
    @BindView(R.id.tv_title_titleName)
    TextView tvTitleTitleName;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.et_setinfo)
    EditText etSetinfo;

    private String titleName;

    @Override
    protected void initData() {
        Intent intent = getIntent();
        titleName = intent.getStringExtra("titleName");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_setinfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        tvTitleBack.setCompoundDrawables(null, null, null, null);
        tvTitleBack.setText("取消");
        tvTitleTitleName.setText(titleName);
        tvTitleRight.setClickable(false);
        SharedPreferences sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        switch (titleName){
            case "设置名字":
                String username = sp.getString("username", "");
                etSetinfo.setText(username);
                break;
            case "我的地址":
                etSetinfo.setText("");
                break;
        }

    }

    @OnClick({R.id.tv_title_back, R.id.tv_title_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_title_back:
                finish();
                break;
            case R.id.tv_title_right:
                commitData();
                finish();
                break;
        }
    }

    private void commitData() {
        //提交数据到后台
    }
}
