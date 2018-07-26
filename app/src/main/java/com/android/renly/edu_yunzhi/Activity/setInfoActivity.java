package com.android.renly.edu_yunzhi.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class setInfoActivity extends BaseActivity {
    @BindView(R.id.tv_title_titleName)
    TextView tvTitleTitleName;
    @BindView(R.id.tv_title_right)
    TextView tvTitleRight;
    @BindView(R.id.et_setinfo)
    EditText etSetinfo;
    @BindView(R.id.ll_title_back)
    LinearLayout llTitleBack;
    @BindView(R.id.tv_title_cancle)
    TextView tvTitleCancle;

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
        llTitleBack.setVisibility(View.GONE);
        tvTitleCancle.setVisibility(View.VISIBLE);
        tvTitleTitleName.setText(titleName);
        tvTitleRight.setClickable(false);
        SharedPreferences sp = this.getSharedPreferences("user_info", Context.MODE_PRIVATE);
        switch (titleName) {
            case "设置名字":
                String username = sp.getString("username", "");
                etSetinfo.setText(username);
                break;
            case "我的地址":
                etSetinfo.setText("");
                break;
        }
        etSetinfo.setSelection(etSetinfo.getText().length());
    }

    @OnTextChanged(value = R.id.et_setinfo, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterTextChanged(Editable text) {
        tvTitleRight.setClickable(true);
    }

    private void showInputKeyboard() {
        //打开软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (!imm.isActive(etSetinfo)){

        }
    }

    private void hideInputKeyboard() {
        //收起软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive(etSetinfo)) {//如果开启`
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @OnClick({R.id.tv_title_right, R.id.tv_title_cancle})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_title_cancle:
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
        String text = etSetinfo.getText().toString();
        SharedPreferences sp = getSharedPreferences("user_info", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        switch (titleName) {
            case "设置名字":
                editor.putString("username", text);
                editor.apply();
                break;
            case "我的地址":
//                editor.putString("address",text);
//                editor.apply();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
