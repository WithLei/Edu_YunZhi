package com.android.renly.edu_yunzhi_teacher.Adapter;

import com.android.renly.edu_yunzhi_teacher.Bean.Class_forCorrect;

import java.util.List;

/**
 * Created by Administrator on 2018/4/15.
 */

public class MyClassAdapter extends MyBaseAdapter<Class_forCorrect> {
    public MyClassAdapter(List<Class_forCorrect> list) {
        super(list);
    }

    @Override
    protected BaseHolder<Class_forCorrect> getHolder() {
        return new MyClassHolder();
    }
}
