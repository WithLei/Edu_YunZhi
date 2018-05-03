package com.android.renly.edu_yunzhi_teacher.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.renly.edu_yunzhi_teacher.Bean.Class_forCorrect;
import com.android.renly.edu_yunzhi_teacher.R;
import com.android.renly.edu_yunzhi_teacher.R2;
import com.android.renly.edu_yunzhi_teacher.Utils.UIUtils;
import com.squareup.picasso.Picasso;

import butterknife.BindView;

/**
 * Created by Administrator on 2018/4/15.
 */

public class MyClassHolder extends BaseHolder<Class_forCorrect> {

    @BindView(R2.id.iv_correct_item_classlogo)
    ImageView ivCorrectItemClasslogo;
    @BindView(R2.id.tv_correct_item_classid)
    TextView tvCorrectItemClassid;
    @BindView(R2.id.tv_correct_item_teachername)
    TextView tvCorrectItemTeachername;
    @BindView(R2.id.tv_correct_item_latestwork)
    TextView tvCorrectItemLatestwork;
    @BindView(R2.id.tv_correct_item_latestworktime)
    TextView tvCorrectItemLatestworktime;
    @BindView(R2.id.tv_correct_item_classname)
    TextView tvCorrectItemClassname;

    @Override
    protected void refreshData() {
        Class_forCorrect data = this.getData();
        //装配数据
        tvCorrectItemClassid.setText(data.classId);
        tvCorrectItemClassname.setText(data.className);
        tvCorrectItemTeachername.setText(data.teacherName);
        tvCorrectItemLatestwork.setText(data.latestWork);
        tvCorrectItemLatestworktime.setText(data.latestWorkTime);
        Picasso.with(UIUtils.getContext()).load((String) data.classLogo).into(ivCorrectItemClasslogo);
    }

    @Override
    protected View initView() {
        return View.inflate(UIUtils.getContext(), R.layout.item_correcthomework, null);
    }
}
