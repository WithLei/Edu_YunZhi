package com.android.renly.edu_yunzhi.Activity;

import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.R;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QueryGradeActivity extends BaseActivity {
    @BindView(R.id.tv_querygrade_title)
    TextView tvQuerygradeTitle;
    @BindView(R.id.fl_querygrade_title)
    FrameLayout flQuerygradeTitle;
    @BindView(R.id.et_query_id)
    EditText etQueryId;
    @BindView(R.id.et_query_pwd)
    EditText etQueryPwd;
    @BindView(R.id.btn_query)
    Button btnQuery;
    @BindView(R.id.ll_query)
    LinearLayout llQuery;
    @BindView(R.id.iv_query_loading)
    ImageView ivQueryLoading;
    @BindView(R.id.ll_grade)
    LinearLayout llGrade;
    @BindView(R.id.iv_query_back)
    ImageView ivQueryBack;
    private long userID;
    private boolean isStudent;

    @Override
    protected void initData() {
        initView();
    }

    private void initView() {
        SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        userID = sp.getLong("id", 0);
        isStudent = sp.getBoolean("isStudent", false);

        if (isStudent) {
            //学生角色登录
            flQuerygradeTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        } else {
            //教师角色登录
            flQuerygradeTitle.setBackgroundColor(getResources().getColor(R.color.colorTeacherPrimary));
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_querygrade;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_query_back, R.id.btn_query})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_query_back:
                finish();
                break;
            case R.id.btn_query:
                llQuery.setVisibility(View.GONE);
                ivQueryLoading.setVisibility(View.VISIBLE);
                ((AnimationDrawable) ivQueryLoading.getDrawable()).start();

                String id = etQueryId.getText().toString();
                String pwd = etQueryPwd.getText().toString();
                if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(pwd))
                    getMyGrade(id,pwd);
                else
                    Toast.makeText(this, "学号密码输入有误", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void getMyGrade(String id, String pwd) {
        RequestParams params = new RequestParams();
    }
}
