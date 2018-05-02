package com.android.renly.edu_yunzhi_teacher.Activity;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.renly.edu_yunzhi_teacher.Bean.Task;
import com.android.renly.edu_yunzhi_teacher.Common.BaseActivity;
import com.android.renly.edu_yunzhi_teacher.R;
import com.android.renly.edu_yunzhi_teacher.UI.CircleImageView;

import java.util.Calendar;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskAddActivity extends BaseActivity {

    @Bind(R.id.cv_addtask_back)
    CircleImageView cvAddtaskBack;
    @Bind(R.id.tv_addtask_title)
    TextView tvAddtaskTitle;
    @Bind(R.id.tv_addtask_finish)
    TextView tvAddtaskFinish;
    @Bind(R.id.et_addtask_type)
    EditText etAddtaskType;
    @Bind(R.id.et_addtask_name)
    EditText etAddtaskName;
    private String[] datas = {"答疑讨论", "课外活动", "实验设计"};

    @Override
    protected void initData() {

    }

    @OnClick({R.id.cv_addtask_back, R.id.et_addtask_type, R.id.tv_addtask_finish})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cv_addtask_back:
                finish();
                break;
            case R.id.et_addtask_type:
                //构建一个popupwindow的布局
                View popupView = TaskAddActivity.this.getLayoutInflater().inflate(R.layout.popupwindow, null);

                //为了演示效果，简单的设置了一些数据，实际中大家自己设置数据即可，相信大家都会。
                ListView lsvMore = (ListView) popupView.findViewById(R.id.lsvMore);
                lsvMore.setAdapter(new ArrayAdapter<String>(TaskAddActivity.this, android.R.layout.simple_list_item_1, datas));

                //创建PopupWindow对象，指定宽度和高度
                PopupWindow window = new PopupWindow(popupView, 400, 400);
                //设置动画
                window.setAnimationStyle(R.style.popup_window_anim);
                //  设置背景颜色
                window.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#F8F8F8")));
                // 设置可以获取焦点
                window.setFocusable(true);
                //  设置可以触摸弹出框以外的区域
                window.setOutsideTouchable(true);
                //更新popupwindow的状态
                window.update();
                // 以下拉的方式显示，并且可以设置显示的位置
                window.showAsDropDown(etAddtaskType, 0, 20);
                lsvMore.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        etAddtaskType.setText(datas[position]);
                        window.dismiss();
                    }
                });
                break;
            case R.id.tv_addtask_finish:
                //通过intent传回数据，刷新adapter
                String type = etAddtaskType.getText().toString();
                String name = etAddtaskName.getText().toString();
                if (!name.isEmpty() && !type.isEmpty()) {
                    Task task = new Task();
                    task.joinNum = 0;
                    task.type = type;
                    Calendar calendar = Calendar.getInstance();
                    task.startTime = calendar.get(Calendar.YEAR) + " - " + (calendar.get(Calendar.MONTH) + 1) + " - " +
                            calendar.get(Calendar.DAY_OF_MONTH);
                    task.teacherName = name;
                    task.title = "测试 - " + type;
                    task.process = "进行中";
                    Intent intent = new Intent();
                    intent.putExtra("Task",task);
                    int resultCode = 2;
                    setResult(2,intent);
                    finish();
                }
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_task_add;
    }
}
