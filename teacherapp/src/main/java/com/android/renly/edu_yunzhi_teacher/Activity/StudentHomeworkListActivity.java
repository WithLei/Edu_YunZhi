package com.android.renly.edu_yunzhi_teacher.Activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi_teacher.Adapter.MyStudentHomeworkAdapter;
import com.android.renly.edu_yunzhi_teacher.Bean.Homework_correct;
import com.android.renly.edu_yunzhi_teacher.Bean.StudentHomework;
import com.android.renly.edu_yunzhi_teacher.Common.BaseActivity;
import com.android.renly.edu_yunzhi_teacher.R;
import com.android.renly.edu_yunzhi_teacher.R2;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class StudentHomeworkListActivity extends BaseActivity {
    @BindView(R2.id.iv_StudentHomeworklist_back)
    ImageView ivStudentHomeworklistBack;
    @BindView(R2.id.tv_StudentHomeworklist_teachername)
    TextView tvStudentHomeworklistTeachername;
    @BindView(R2.id.iv_StudentHomeworklist_search)
    ImageView ivStudentHomeworklistSearch;
    @BindView(R2.id.iv_StudentHomeworklist_more)
    ImageView ivStudentHomeworklistMore;
    @BindView(R2.id.lv_StudentHomeworklist_homework)
    ListView lvStudentHomeworklistHomework;
    private List<StudentHomework> studentHomeworkList = new ArrayList<>();

    @Override
    protected void initData() {
        Intent intent = getIntent();
        Homework_correct homework = (Homework_correct) intent.getSerializableExtra("StudentHomework");
        String homeworkId = homework.getId();
        String teacherName = homework.getTeacherName();
        String homeworkType = homework.getType();
        final String content = homework.getContent();
        String[] studentNames = new String[]{
                "喜羊羊", "美羊羊", "懒羊羊",
                "沸羊羊", "暖羊羊", "慢羊羊"
        };
        String[] situations = new String[]{
                "完成", "完成", "未完成",
                "未完成", "逾期上交", "未完成"
        };
        final String[] answers = new String[]{
                "  A", "  B", "  C",
                "  A", "  C", "  D"
        };
        for (int i = 0; i < studentNames.length; i++) {
            StudentHomework studentHomework = new StudentHomework();
            studentHomework.setStudentName(studentNames[i]);
            studentHomework.setHomeworkId(homeworkId);
            studentHomework.setHomeworkType(homeworkType);
            studentHomework.setSituation(situations[i]);
            studentHomework.setContent(content);
            studentHomework.setAnswer(answers[i]);
            studentHomeworkList.add(studentHomework);
        }
        tvStudentHomeworklistTeachername.setText(teacherName);
        MyStudentHomeworkAdapter myStudentHomeworkAdapter = new MyStudentHomeworkAdapter(studentHomeworkList);
        lvStudentHomeworklistHomework.setAdapter(myStudentHomeworkAdapter);
        lvStudentHomeworklistHomework.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent1 = new Intent(StudentHomeworkListActivity.this,StudentHomeworkCorrectActivity.class);
                String[] msg = new String[]{
                        content,answers[position]
                };
                intent1.putExtra("content&answer",msg);
                startActivity(intent1);
            }
        });
    }

    @OnClick({R2.id.iv_StudentHomeworklist_back,R2.id.iv_StudentHomeworklist_search,R2.id.iv_StudentHomeworklist_more})
    public void onClick(View v){
        switch (v.getId()){
            case R2.id.iv_StudentHomeworklist_back:
                finish();
                break;
            case R2.id.iv_StudentHomeworklist_search:
                Toast.makeText(StudentHomeworkListActivity.this,"搜索",Toast.LENGTH_SHORT).show();
                break;
            case R2.id.iv_StudentHomeworklist_more:
                Toast.makeText(StudentHomeworkListActivity.this,"更多",Toast.LENGTH_SHORT).show();
                break;
        }
    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_student_homework_list;
    }
}
