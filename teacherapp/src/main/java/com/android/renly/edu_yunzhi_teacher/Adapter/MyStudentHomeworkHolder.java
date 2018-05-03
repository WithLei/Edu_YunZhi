package com.android.renly.edu_yunzhi_teacher.Adapter;

import android.view.View;
import android.widget.TextView;

import com.android.renly.edu_yunzhi_teacher.Bean.StudentHomework;
import com.android.renly.edu_yunzhi_teacher.R;
import com.android.renly.edu_yunzhi_teacher.R2;
import com.android.renly.edu_yunzhi_teacher.Utils.UIUtils;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/15.
 */

public class MyStudentHomeworkHolder extends BaseHolder<StudentHomework> {


    @BindView(R2.id.tv_studenthomework_item_id)
    TextView tvStudenthomeworkItemId;
    @BindView(R2.id.tv_studenthomework_item_type)
    TextView tvStudenthomeworkItemType;
    @BindView(R2.id.tv_studenthomework_item_studentname)
    TextView tvStudenthomeworkItemStudentname;
    @BindView(R2.id.tv_studenthomework_item_situation)
    TextView tvStudenthomeworkItemSituation;
//    @Bind(R2.id.tv_studenthomework_item_content)
//    TextView tvStudenthomeworkItemContent;
//    @Bind(R2.id.tv_studenthomework_item_answer)
//    TextView tvStudenthomeworkItemAnswer;

    @Override
    protected void refreshData() {
        StudentHomework data = this.getData();
        //装配数据
        tvStudenthomeworkItemId.setText(data.getHomeworkId());
        tvStudenthomeworkItemType.setText(data.getHomeworkType());
        tvStudenthomeworkItemStudentname.setText(data.getStudentName());
        tvStudenthomeworkItemSituation.setText(data.getSituation());
//        tvStudenthomeworkItemContent.setText(data.getContent());
//        tvStudenthomeworkItemAnswer.setText(data.getAnswer());
    }

    @Override
    protected View initView() {
        return View.inflate(UIUtils.getContext(), R.layout.item_studenthomework_forcorrect, null);
    }
}
