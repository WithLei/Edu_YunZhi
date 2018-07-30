package com.android.renly.edu_yunzhi.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.renly.edu_yunzhi.Bean.Task;
import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.R;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TaskInfoActivity extends BaseActivity {
    @BindView(R.id.tv_taskinfo)
    TextView tvTaskinfo;

    private Unbinder unbinder;

    @Override
    protected void initData() {
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String s = bundle.getString("Task");
        Task task = JSON.parseObject(s, new TypeReference<Task>(){});
        tvTaskinfo.setText(s);
        Log.e("print",task.toString());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_taskinfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
