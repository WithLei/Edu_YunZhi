package com.android.renly.edu_yunzhi_teacher.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by Administrator on 2018/4/15.
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {

    public List<T> list;

    //通过构造器初始化集合数据
    public MyBaseAdapter(List<T> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //将具体的集合数据装配到具体的一个item_layout中。
    //问题一:item_layout的布局是不确定的。
    //问题二:将集合中指定位置的数据装配到item，是不确定的。
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BaseHolder<T> holder;
        if (convertView == null){
            holder = getHolder();
        }else {
            holder = (BaseHolder<T>) convertView.getTag();
        }
        //装配数据
        T t = list.get(position);
        holder.setData(t);

        return holder.getRootView();
    }

    protected abstract BaseHolder<T> getHolder();
}
