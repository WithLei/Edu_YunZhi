package com.android.renly.edu_yunzhi.Adapter;


import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi.Bean.TitleFirst;
import com.android.renly.edu_yunzhi.Bean.TitleSecond;
import com.android.renly.edu_yunzhi.Bean.TitleThird;
import com.android.renly.edu_yunzhi.Common.BaseNode;
import com.android.renly.edu_yunzhi.R;
import com.simon.multilevellist.MultiAdapter;
import com.simon.multilevellist.MultiLevelAdapter;
import com.simon.multilevellist.OnMultiLevelItemClickListener;
import com.simon.multilevellist.tree.INode;
import com.simon.multilevellist.tree.IParent;

public class CourseListAdapter extends MultiLevelAdapter {

    private Context context;

    public CourseListAdapter(Context context, IParent courseTitle) {
        super(courseTitle);
        this.context = context;
    }

    @Override
    protected void registerViewHolderCreators() {
        registerViewHolderCreator(TYPE_FIRST, new CourseListAdapter.CityViewHolderCreator());
        registerViewHolderCreator(TYPE_SECOND, new CourseListAdapter.AreaViewHolderCreator());
        registerViewHolderCreator(TYPE_THIRD, new CourseListAdapter.StreetViewHolderCreator());
    }

    @Override
    public void onClickChild(INode child) {
        Toast.makeText(context, ((BaseNode) child).getName() + " is on click !", Toast.LENGTH_SHORT).show();
    }


    @Override
    public int getItemViewType(INode data) {

        if(data instanceof TitleFirst) return TYPE_FIRST;

        if(data instanceof TitleSecond) return TYPE_SECOND;

        if(data instanceof TitleThird) return TYPE_THIRD;

        return 0;
    }

    private static final int TYPE_FIRST = 1;
    private static final int TYPE_SECOND = 2;
    private static final int TYPE_THIRD = 3;

    private class CityViewHolderCreator implements MultiAdapter.ViewHolderCreator {

        @NonNull
        @Override
        public BaseHolder create(Context context, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_first, parent, false);
            return new CourseListAdapter.CityViewHolder(view, CourseListAdapter.this);
        }
    }

    private static class CityViewHolder extends MultiLevelViewHolder<TitleFirst> {

        private TextView tvCityName;

        CityViewHolder(View itemView, OnMultiLevelItemClickListener listener) {
            super(itemView, TYPE_FIRST, listener);
            tvCityName = (TextView) itemView.findViewById(R.id.tv_item_first);
        }

        @Override
        public void bindViewHolder(TitleFirst data) {
            tvCityName.setText(data.getName());
        }
    }

    private  class AreaViewHolderCreator implements MultiAdapter.ViewHolderCreator {

        @NonNull
        @Override
        public BaseHolder create(Context context, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_second, parent, false);
            return new CourseListAdapter.AreaViewHolder(view, CourseListAdapter.this);
        }
    }

    private static class AreaViewHolder extends MultiLevelViewHolder<TitleSecond> {

        private TextView tvAreaName;

        AreaViewHolder(View itemView, OnMultiLevelItemClickListener listener) {
            super(itemView, TYPE_SECOND, listener);
            tvAreaName = (TextView) itemView.findViewById(R.id.tv_item_second);
        }

        @Override
        public void bindViewHolder(TitleSecond data) {
            tvAreaName.setText(data.getName());
        }
    }


    private class StreetViewHolderCreator implements MultiAdapter.ViewHolderCreator {

        @NonNull
        @Override
        public BaseHolder create(Context context, ViewGroup parent) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_third, parent, false);
            return new CourseListAdapter.StreetViewHolder(view, CourseListAdapter.this);
        }
    }

    private static class StreetViewHolder extends MultiLevelViewHolder<TitleThird> {

        private TextView tvStreetName;

        StreetViewHolder(View itemView, OnMultiLevelItemClickListener listener) {
            super(itemView, TYPE_THIRD, listener);
            tvStreetName = (TextView) itemView.findViewById(R.id.tv_item_third);
        }

        @Override
        public void bindViewHolder(TitleThird data) {
            tvStreetName.setText(data.getName());
        }
    }


}

