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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.edu_yunzhi.Common.AppNetConfig;
import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.R;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cz.msebera.android.httpclient.Header;

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
    @BindView(R.id.iv_query_back)
    ImageView ivQueryBack;
    @BindView(R.id.tv_grade_grade)
    TextView tvGradeGrade;
    @BindView(R.id.tv_grade_classname)
    TextView tvGradeClassname;
    @BindView(R.id.tv_querygrade_username)
    TextView tvQuerygradeUsername;
    @BindView(R.id.tv_grade_teachername)
    TextView tvGradeTeachername;
    @BindView(R.id.rl_grade)
    RelativeLayout rlGrade;
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
                String id = etQueryId.getText().toString();
                String pwd = etQueryPwd.getText().toString();
                if (!TextUtils.isEmpty(id) && !TextUtils.isEmpty(pwd)){
                    llQuery.setVisibility(View.GONE);
                    ivQueryLoading.setVisibility(View.VISIBLE);
                    isQueryVisible = false;
                    ((AnimationDrawable) ivQueryLoading.getDrawable()).start();
                    getMyGrade(id, pwd);
                }
                else
                    Toast.makeText(this, "学号密码输入有误", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void getMyGrade(String id, String pwd) {
        RequestParams params = new RequestParams();
        params.put("id",id);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(AppNetConfig.GET_MYGRADE, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                if (response.equals("[]")){
                    ((AnimationDrawable) ivQueryLoading.getDrawable()).stop();
                    ivQueryLoading.setVisibility(View.GONE);

                    llQuery.setVisibility(View.VISIBLE);
                    isQueryVisible = true;

                    Toast.makeText(QueryGradeActivity.this, "输入账号有误", Toast.LENGTH_SHORT).show();
                }
                JSONArray array = JSON.parseArray(response);
//                for (int i = 0;i < array.size(); i++){
//                    JSONObject object = array.getJSONObject(i);
//                    String courseName = object.getString("courseName");
//                    String teacherName = object.getString("teacherName");
//                    double grade = object.getDouble("grade");
//                }
                JSONObject object = array.getJSONObject(0);
                String courseName = object.getString("courseName");
                String teacherName = object.getString("teacherName");
                double grade = object.getDouble("grade");

                tvGradeClassname.setText(courseName);
                tvGradeTeachername.setText(teacherName);
                tvGradeGrade.setText((int)grade + "");
                tvQuerygradeUsername.setText("喜羊羊");

                ((AnimationDrawable) ivQueryLoading.getDrawable()).stop();
                ivQueryLoading.setVisibility(View.GONE);
                rlGrade.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                ((AnimationDrawable) ivQueryLoading.getDrawable()).stop();
                ivQueryLoading.setVisibility(View.GONE);

                llQuery.setVisibility(View.VISIBLE);
                isQueryVisible = true;

                Toast.makeText(QueryGradeActivity.this, "网络开小差咯", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean isQueryVisible = true;

    @Override
    public void onBackPressed() {
        if (!isQueryVisible){
            ivQueryLoading.setVisibility(View.GONE);
            rlGrade.setVisibility(View.GONE);
            llQuery.setVisibility(View.VISIBLE);
        }else
            super.onBackPressed();
    }
}
