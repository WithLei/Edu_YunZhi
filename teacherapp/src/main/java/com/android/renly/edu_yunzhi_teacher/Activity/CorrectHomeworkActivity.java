package com.android.renly.edu_yunzhi_teacher.Activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi_teacher.Adapter.MyClassAdapter;
import com.android.renly.edu_yunzhi_teacher.Bean.Class_forCorrect;
import com.android.renly.edu_yunzhi_teacher.Common.BaseActivity;
import com.android.renly.edu_yunzhi_teacher.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class CorrectHomeworkActivity extends BaseActivity {

    @Bind(R.id.iv_correct_back)
    ImageView ivCorrectBack;
    @Bind(R.id.iv_correct_search)
    ImageView ivCorrectSearch;
    @Bind(R.id.iv_correct_more)
    ImageView ivCorrectMore;
    @Bind(R.id.lv_correct_class)
    ListView lvCorrectClass;
    private List<Class_forCorrect> class_forCorrects = new ArrayList<>();

    @Override
    protected void initData() {
        //此数据写死
        String[] ids = new String[]{
                "00001", "00002", "00003", "00004", "00005", "00006"
        };
        String[] classNames = new String[]{
                "高等数学A", "线性代数", "C语言程序设计",
                "大学英语1", "高等数学B", "面向对象程序设计"
        };
        String[] teacherNames = new String[]{
                "贝老师", "陈老师", "王老师",
                "孙老师", "蒋老师", "郑老师"
        };
        String[] latestWorks = new String[]{
                "第三章课后作业", "第四章课后作业", "第五章课后作业",
                "第一章课后作业", "第六章课后作业", "第二章课后作业"
        };
        String[] latestWorkTimes = new String[]{
                "2018-3-29 18.45", "2018-3-30 18.45", "2018-4-02 18.45",
                "2018-3-28 18.45", "2018-4-05 18.45", "2018-4-14 18.45"
        };
        String[] logoUrls = new String[]{
                "http://edu-image.nosdn.127.net/73b5696e-4dfa-4eeb-8525-4a65f05c3b05.jpg?imageView&quality=100",
                "http://edu-image.nosdn.127.net/F230B3E8A64D765AC0F35C4173AECA03.jpg?imageView&thumbnail=223y124&quality=100",
                "http://edu-image.nosdn.127.net/0731fab7-4070-4c1d-ba6a-2291a42f32cd.jpg?imageView&quality=100",
                "http://edu-image.nosdn.127.net/73b5696e-4dfa-4eeb-8525-4a65f05c3b05.jpg?imageView&quality=100",
                "http://edu-image.nosdn.127.net/F230B3E8A64D765AC0F35C4173AECA03.jpg?imageView&thumbnail=223y124&quality=100",
                "http://edu-image.nosdn.127.net/0731fab7-4070-4c1d-ba6a-2291a42f32cd.jpg?imageView&quality=100",
        };
        for (int i = 0; i < ids.length; i++) {
            //显示数据
            Class_forCorrect class_forCorrect = new Class_forCorrect();
            class_forCorrect.setClassId(ids[i]);
            class_forCorrect.setClassName(classNames[i]);
            class_forCorrect.setTeacherName(teacherNames[i]);
            class_forCorrect.setLatestWork(latestWorks[i]);
            class_forCorrect.setLatestWorkTime(latestWorkTimes[i]);
            class_forCorrect.setClassLogo(logoUrls[i]);
            class_forCorrects.add(class_forCorrect);
        }
        MyClassAdapter myClassAdapter = new MyClassAdapter(class_forCorrects);
        lvCorrectClass.setAdapter(myClassAdapter);
        lvCorrectClass.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //将数据发送给HomeworkListActivity
                Intent intent = new Intent(CorrectHomeworkActivity.this, HomeworkListActivity.class);
                intent.putExtra("Class_correct",class_forCorrects.get(position));
                startActivity(intent);
            }
        });
    }

    @OnClick({R.id.iv_correct_back,R.id.iv_correct_search,R.id.iv_correct_more})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_correct_back:
                //实现回退操作
                finish();
                break;
            case R.id.iv_correct_search:
                //实现搜索操作
                Toast.makeText(CorrectHomeworkActivity.this,"搜索",Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_correct_more:
                //实现更多操作
                Toast.makeText(CorrectHomeworkActivity.this,"更多",Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_correct_homework;
    }

}
