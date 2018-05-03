package com.android.renly.edu_yunzhi_teacher.Adapter;

import android.view.View;
import android.widget.TextView;

import com.android.renly.edu_yunzhi_teacher.Bean.Homework_correct;
import com.android.renly.edu_yunzhi_teacher.R;
import com.android.renly.edu_yunzhi_teacher.R2;
import com.android.renly.edu_yunzhi_teacher.Utils.UIUtils;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/15.
 */

public class MyHomeworkHolder extends BaseHolder<Homework_correct> {

    @BindView(R2.id.tv_homework_item_id)
    TextView tvHomeworkItemId;
    @BindView(R2.id.tv_homework_item_type)
    TextView tvHomeworkItemType;
    @BindView(R2.id.tv_homework_item_teachername)
    TextView tvHomeworkItemTeachername;
    @BindView(R2.id.tv_homework_item_publishtime)
    TextView tvHomeworkItemPublishtime;
    @BindView(R2.id.tv_homework_item_worktime)
    TextView tvHomeworkItemWorktime;
    @BindView(R2.id.tv_homework_item_situation)
    TextView tvHomeworkItemSituation;

    @Override
    protected void refreshData() {
        Homework_correct data = this.getData();
        //装配数据
        tvHomeworkItemId.setText(data.getId());
        tvHomeworkItemType.setText(data.getType());
        tvHomeworkItemTeachername.setText(data.getTeacherName());
        tvHomeworkItemPublishtime.setText(data.getPublishTime());
        tvHomeworkItemWorktime.setText(data.getWorkTime());
        tvHomeworkItemSituation.setText(data.getSituation());
    }

    @Override
    protected View initView() {
        return View.inflate(UIUtils.getContext(), R.layout.item_homework_forcorrect, null);
    }
}
