package com.android.renly.edu_yunzhi.Fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.renly.edu_yunzhi.Adapter.CourseListAdapter;
import com.android.renly.edu_yunzhi.Bean.CourseTitle;
import com.android.renly.edu_yunzhi.Bean.TitleFirst;
import com.android.renly.edu_yunzhi.Bean.TitleSecond;
import com.android.renly.edu_yunzhi.Bean.TitleThird;
import com.android.renly.edu_yunzhi.Common.BaseFragment;
import com.android.renly.edu_yunzhi.Common.MyApplication;
import com.android.renly.edu_yunzhi.R;
import com.loopj.android.http.RequestParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CourseListFragment extends BaseFragment {

    private static final String ARG_TITLE = "title";
    @BindView(R.id.recyclerview_courselist)
    RecyclerView recyclerviewCourselist;

    public static CourseListFragment getInstance(String title) {
        CourseListFragment fra = new CourseListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TITLE, title);
        fra.setArguments(bundle);
        return fra;
    }

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
        recyclerviewCourselist.setLayoutManager(new LinearLayoutManager(MyApplication.context));
        CourseTitle courseTitle = initTitleData();

        CourseListAdapter courseListAdapter = new CourseListAdapter(MyApplication.context,courseTitle);
        recyclerviewCourselist.setAdapter(courseListAdapter);

    }

    public static CourseTitle initTitleData() {

        CourseTitle courseTitle = new CourseTitle("主要课程");

        TitleFirst first = new TitleFirst("第 1 章 初次接触Java");

        TitleSecond second = new TitleSecond("1.1 Java语言概述");
        TitleThird third = new TitleThird("1.1.1 Java语言的发展历史");
        TitleThird third1 = new TitleThird("1.1.2 Java应用平台");
        TitleThird third2 = new TitleThird("1.1.3 Java语言特点");
        TitleThird third3 = new TitleThird("1.1.4 Java开发工具");
        second.addChild(third);
        second.addChild(third1);
        second.addChild(third2);
        second.addChild(third3);
        second.open();
        TitleSecond second1 = new TitleSecond("1.2 Java和C/C++的比较");
        TitleSecond second2 = new TitleSecond("1.3 Java平台工作原理");
        TitleSecond second3 = new TitleSecond("1.4 第一个Java程序");
        TitleThird third4 = new TitleThird("1.4.1 Java程序开发步骤");
        TitleThird third5 = new TitleThird("1.4.2 第一个Java应用程序");
        TitleThird third6 = new TitleThird("1.4.3 第一个Java应用小程序");
        second3.addChild(third4);
        second3.addChild(third5);
        second3.addChild(third6);

        first.addChild(second);
        first.addChild(second1);
        first.addChild(second2);
        first.addChild(second3);
        first.open();

        TitleFirst first1 = new TitleFirst("第 2 章 Java语言基础");
        TitleFirst first2 = new TitleFirst("第 3 章 类与对象");
        TitleFirst first3 = new TitleFirst("第 4 章 继承与多态");
        TitleFirst first4 = new TitleFirst("第 5 章 常用数据结构");
        TitleFirst first5 = new TitleFirst("第 6 章 Java异常处理");
        TitleFirst first6 = new TitleFirst("第 7 章 Java IO流");
        TitleFirst first7 = new TitleFirst("第 8 章 图形用户界面编程");
        TitleFirst first8 = new TitleFirst("第 9 章 applet");
        TitleFirst first9 = new TitleFirst("第 10 章 多线程编程");
        TitleFirst first10 = new TitleFirst("第 11 章 Java网络编程");
        TitleFirst first11 = new TitleFirst("第 12 章 数据库编程");



        courseTitle.addChild(first);
        courseTitle.addChild(first1);
        courseTitle.addChild(first2);
        courseTitle.addChild(first3);
        courseTitle.addChild(first4);
        courseTitle.addChild(first5);
        courseTitle.addChild(first6);
        courseTitle.addChild(first7);
        courseTitle.addChild(first8);
        courseTitle.addChild(first9);
        courseTitle.addChild(first10);
        courseTitle.addChild(first11);
        courseTitle.open();

        return courseTitle;
    }

    @Override
    public int getLayoutid() {
        return R.layout.fragment_courselist;
    }

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
