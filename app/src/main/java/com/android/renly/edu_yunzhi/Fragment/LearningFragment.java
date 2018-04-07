package com.android.renly.edu_yunzhi.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.android.renly.edu_yunzhi.Common.BaseFragment;
import com.android.renly.edu_yunzhi.R;
import com.android.renly.edu_yunzhi.UI.BatchRadioButton;
import com.loopj.android.http.RequestParams;

import butterknife.Bind;
import butterknife.ButterKnife;

public class LearningFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.btn_1)
    BatchRadioButton btn1;
    @Bind(R.id.btn_2)
    BatchRadioButton btn2;
    @Bind(R.id.btn_3)
    BatchRadioButton btn3;
    @Bind(R.id.btn_change)
    RadioGroup btnChange;

    @Override
    protected String getUrl() {
        return null;
    }

    @Override
    protected RequestParams getParams() {
        return null;
    }

    @Override
    protected void initData(String content) {

    }

    @Override
    public int getLayoutid() {
        return R.layout.fragment_learning;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        btn1.setText("课程");
        btn2.setText("作业");
        btn3.setText("笔记");
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
