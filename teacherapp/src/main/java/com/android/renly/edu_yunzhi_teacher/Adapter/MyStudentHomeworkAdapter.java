package com.android.renly.edu_yunzhi_teacher.Adapter;
import com.android.renly.edu_yunzhi_teacher.Bean.Homework_correct;
import com.android.renly.edu_yunzhi_teacher.Bean.StudentHomework;

import java.util.List;

/**
 * Created by Administrator on 2018/4/15.
 */

public class MyStudentHomeworkAdapter extends MyBaseAdapter<StudentHomework> {
    public MyStudentHomeworkAdapter(List<StudentHomework> list) {
        super(list);
    }

    @Override
    protected BaseHolder<StudentHomework> getHolder() {
        return new MyStudentHomeworkHolder();
    }
}
