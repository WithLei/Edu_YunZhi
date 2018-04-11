package com.android.renly.edu_yunzhi.Activity;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.renly.edu_yunzhi.Adapter.ListDropDownAdapter;
import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.R;
import com.android.renly.edu_yunzhi.UI.CircleImageView;
import com.android.renly.edu_yunzhi.UI.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskActivity extends BaseActivity {
    @Bind(R.id.task_img)
    CircleImageView taskImg;
    @Bind(R.id.dropDownMenu)
    DropDownMenu mDropDownMenu;

    @Override
    protected void initData() {
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_task;
    }

    private String headers[] = {"实训进度", "类型"};
    private List<View> popupViews = new ArrayList<>();

    private ListDropDownAdapter processAdapter;
    private ListDropDownAdapter typeAdapter;

    private String processs[] = {"全部进度", "进行中", "已结束"};
    private String type[] = {"全部类型", "答疑讨论", "课外活动", "实验设计"};

    private int constellationPosition = 0;

    private void initView() {

        //init process menu
        final ListView processView = new ListView(this);
        processView.setDividerHeight(0);
        processAdapter = new ListDropDownAdapter(this, Arrays.asList(processs));
        processView.setAdapter(processAdapter);

        //init type menu
        final ListView typeView = new ListView(this);
        typeView.setDividerHeight(0);
        typeAdapter = new ListDropDownAdapter(this, Arrays.asList(type));
        typeView.setAdapter(typeAdapter);

        //init popupViews
        popupViews.add(processView);
        popupViews.add(typeView);

        //add item click event
        processView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                processAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[1] : processs[position]);
                mDropDownMenu.closeMenu();
            }
        });

        typeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[2] : type[position]);
                mDropDownMenu.closeMenu();
            }
        });

        //init context view
        TextView contentView = new TextView(this);
        contentView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        contentView.setText("内容显示区域");
        contentView.setGravity(Gravity.CENTER);
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);

        //init dropdownview
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, contentView);
    }

    @Override
    public void onBackPressed() {
        //退出activity前关闭菜单
        if (mDropDownMenu.isShowing()) {
            mDropDownMenu.closeMenu();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.task_img)
    public void onViewClicked() {
                finish();
    }
}
