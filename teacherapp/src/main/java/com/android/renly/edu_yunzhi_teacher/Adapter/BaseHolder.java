package com.android.renly.edu_yunzhi_teacher.Adapter;

import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/4/15.
 */

public abstract class BaseHolder<T> {
    public View getRootView() {
        return rootView;
    }

    private View rootView;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
        refreshData();
    }

    //装配过程
    protected abstract void refreshData();

    private T data;
    public BaseHolder(){
        rootView = initView();
        rootView.setTag(this);
        ButterKnife.bind(this,rootView);
    }

    //提供item的布局
    protected abstract View initView();
}
