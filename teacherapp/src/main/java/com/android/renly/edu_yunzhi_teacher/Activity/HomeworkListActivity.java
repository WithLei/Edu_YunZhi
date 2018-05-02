package com.android.renly.edu_yunzhi_teacher.Activity;


import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi_teacher.Adapter.MyHomeworkAdapter;
import com.android.renly.edu_yunzhi_teacher.Bean.Class_forCorrect;
import com.android.renly.edu_yunzhi_teacher.Bean.Homework_correct;
import com.android.renly.edu_yunzhi_teacher.Common.BaseActivity;
import com.android.renly.edu_yunzhi_teacher.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;

public class HomeworkListActivity extends BaseActivity {

    @Bind(R.id.iv_homeworklist_back)
    ImageView ivHomeworklistBack;
    @Bind(R.id.iv_homeworklist_search)
    ImageView ivHomeworklistSearch;
    @Bind(R.id.iv_homeworklist_more)
    ImageView ivHomeworklistMore;
    @Bind(R.id.lv_homeworklist_homework)
    ListView lvHomeworklistHomework;
    private List<Homework_correct> homeworkList = new ArrayList<>();

    @Override
    protected void initData() {
        //此数据写死
        String[] ids = new String[]{
                "3-1", "3-2", "3-5", "3-6", "3-12", "3-15"
        };
        String[] types = new String[]{
                "选择题", "选择题", "选择题",
                "简答题", "简答题", "简答题"
        };
        String[] workTimes = new String[]{
                "一天", "一天", "一天",
                "一天", "一周", "一周"
        };
        String[] publicTimes = new String[]{
                "2018-3-29 18.45", "2018-3-30 18.45", "2018-4-02 18.45",
                "2018-3-28 18.45", "2018-4-05 18.45", "2018-4-14 18.45"
        };
        String[] contents = new String[]{
                "大煮干丝”是哪个菜系的代表菜之一( )。\n A.四川菜系 \n B.山东菜系 \n C.广东菜系 \n D.淮扬菜系 \n",
                "红茶属于( )茶。\n A.半发酵 \n B.发酵 \n C.不发酵 \n D.微发酵",
                "满汉全席起兴于（ ）。\n A.清代 \n B.唐代 \n C.宋代 \n D.两汉",
                "简述政策制定系统的基本功能。",
                "简述公共政策问题的基本内涵。",
                "简述公共政策学的研究对象。"
        };
        Intent intent = getIntent();
//        String logoUrl;
        Class_forCorrect class_correct = (Class_forCorrect) intent.getSerializableExtra("Class_correct");
//        logoUrl = class_correct.getClassLogo();
        String teacherName = class_correct.getTeacherName();
        for (int i = 0; i < ids.length; i++) {
            //显示数据
            Homework_correct homework = new Homework_correct();
            homework.setId(ids[i]);
            homework.setType(types[i]);
            homework.setSituation("未批改");
            homework.setPublishTime(publicTimes[i]);
            homework.setTeacherName(teacherName);
            homework.setWorkTime(workTimes[i]);
            homework.setContent(contents[i]);
            homeworkList.add(homework);
        }
        MyHomeworkAdapter myHomeworkAdapter = new MyHomeworkAdapter(homeworkList);
        lvHomeworklistHomework.setAdapter(myHomeworkAdapter);
        lvHomeworklistHomework.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            //通过intent传输信息，在StudentHomeworkListActivity显示
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(HomeworkListActivity.this,StudentHomeworkListActivity.class);
                int requestCode = 1;
                intent1.putExtra("StudentHomework",homeworkList.get(position));
                startActivityForResult(intent1,requestCode);
            }
        });
    }
    @OnClick({R.id.iv_homeworklist_back,R.id.iv_homeworklist_search,R.id.iv_homeworklist_more})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.iv_homeworklist_back:
                //实现回退操作
                finish();
                break;
            case R.id.iv_homeworklist_search:
                //实现搜索操作
                Toast.makeText(HomeworkListActivity.this,"搜索",Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_homeworklist_more:
                //实现更多操作
                Toast.makeText(HomeworkListActivity.this,"更多",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_homework_list;
    }
}
