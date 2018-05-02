package com.android.renly.edu_yunzhi_teacher.Adapter;
import com.android.renly.edu_yunzhi_teacher.Bean.Homework_correct;

import java.util.List;

/**
 * Created by Administrator on 2018/4/15.
 */

public class MyHomeworkAdapter extends MyBaseAdapter<Homework_correct> {
    public MyHomeworkAdapter(List<Homework_correct> list) {
        super(list);
    }

    @Override
    protected BaseHolder<Homework_correct> getHolder() {
        return new MyHomeworkHolder();
    }
}
