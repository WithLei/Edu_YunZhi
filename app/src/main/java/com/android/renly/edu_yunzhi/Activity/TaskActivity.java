package com.android.renly.edu_yunzhi.Activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi.Adapter.ListDropDownAdapter;
import com.android.renly.edu_yunzhi.Bean.Task;
import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.Common.MyApplication;
import com.android.renly.edu_yunzhi.R;
import com.android.renly.edu_yunzhi.UI.CircleImageView;
import com.android.renly.edu_yunzhi.UI.CustomLinearLayoutManager;
import com.android.renly.edu_yunzhi.UI.DropDownMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TaskActivity extends BaseActivity {
    @Bind(R.id.task_img)
    ImageView taskImg;
    @Bind(R.id.dropDownMenu)
    DropDownMenu mDropDownMenu;
    @Bind(R.id.task_title)
    TextView taskTitle;
    RecyclerView recyclerTask;

    @Override
    protected void initData() {
        initTaskData(null,null);
        initView();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_task;
    }

    private String headers[] = {"进度", "类型"};
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
                mDropDownMenu.setTabText(position == 0 ? headers[0] : processs[position]);
                //init context view
                if(position==0)
                    initTaskData(null,null);
                else
                    initTaskData(position == 1 ? "进行中" : "已结束",null);
                initList();
//                mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, recyclerTask);
                mDropDownMenu.closeMenu();
            }
        });

        typeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[1] : type[position]);
//                init context view
               switch (position){
                   case 0:
                       initTaskData(null,null);
                       break;
                    case 1:
                        initTaskData(null,"答疑讨论");
                        break;
                    case 2:
                        initTaskData(null,"课外活动");
                        break;
                    case 3:
                        initTaskData(null,"实验设计");
                        break;
                }
                initList();
//                mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, recyclerTask);
                mDropDownMenu.closeMenu();
            }
        });

        //init context view
        recyclerTask = new RecyclerView(this);
        recyclerTask.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        initList();

        //init dropdownview
        mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, recyclerTask);
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

    List<Task>taskList;//通知列表

    //初始化实训列表【暂时写死
    public void initTaskData(String process,String type) {
        taskList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            //1.
            Task firstTask = new Task();
            firstTask.title = "测试 - 答疑讨论";
            firstTask.startTime = "2018 - 04 - 10";
            firstTask.teacherName = "翁凯";
            firstTask.joinNum = 33;
            firstTask.process = "进行中";
            firstTask.type = "答疑讨论";

            //2.
            Task secondTask = new Task();
            secondTask.title = "测试 - 课外活动";
            secondTask.startTime = "2018 - 04 - 07";
            secondTask.teacherName = "孙麒";
            secondTask.joinNum = 65;
            secondTask.process = "进行中";
            secondTask.type = "课外活动";

            //3.
            Task thirdTask = new Task();
            thirdTask.title = "测试 - 实验设计";
            thirdTask.startTime = "2018 - 04 - 05";
            thirdTask.teacherName = "云智教育";
            thirdTask.joinNum = 18;
            thirdTask.process = "已结束";
            thirdTask.type = "实验设计";

            if (firstTask.process.equals(process)||process == null)
                if(firstTask.type.equals(type)||type == null)
                    taskList.add(firstTask);

            if (secondTask.process.equals(process)||process == null)
                if(secondTask.type.equals(type)||type == null)
                    taskList.add(secondTask);

            if (thirdTask.process.equals(process)||process == null)
                if(thirdTask.type.equals(type)||type == null)
                    taskList.add(thirdTask);
        }
    }

    public taskInfoAdapter taskInfoAdapter;

    public class taskInfoAdapter extends RecyclerView.Adapter<taskInfoAdapter.ViewHolder> {
        private List<Task> taskList;//通知列表

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView teacherName;
            TextView startTime;
            TextView joinNum;
            TextView process;
            ImageView typeImg;


            public ViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.tv_item_task_title);
                teacherName = view.findViewById(R.id.tv_item_task_teachername);
                startTime = view.findViewById(R.id.tv_item_task_time);
                joinNum = view.findViewById(R.id.tv_item_task_join);
                process = view.findViewById(R.id.task_process);
                typeImg = view.findViewById(R.id.item_task_img);
            }
        }

        public taskInfoAdapter(List<Task> taskList) {
            this.taskList = taskList;
        }

        //加载item 的布局  创建ViewHolder实例
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        //对RecyclerView子项数据进行赋值
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Task task = taskList.get(position);
            //设置数据
            holder.title.setText(task.title);
            holder.startTime.setText(task.startTime);
            holder.teacherName.setText(task.teacherName);
            holder.process.setText(task.process);
            holder.joinNum.setText("共 "+task.joinNum+" 人参加");
            /**
             * 类型
             * 答疑讨论 - 课外活动 - 实验设计
             * 进度
             * 进行中 - 已结束
             */
            switch (task.type){
                case "答疑讨论":
                    holder.typeImg.setImageDrawable(getResources().getDrawable(R.drawable.activitieslisticondiscuss));
                    break;
                case "课外活动":
                    holder.typeImg.setImageDrawable(getResources().getDrawable(R.drawable.activitieslisticontesting));
                    break;
                case "实验设计":
                    holder.typeImg.setImageDrawable(getResources().getDrawable(R.drawable.activitieslisticonassignment));
                    break;
            }
        }

        //返回子项个数
        @Override
        public int getItemCount() {
            return taskList.size();
        }
    }

    protected static final int WHAT_REQUEST_SUCCESS = 1;
    protected static final int WHAT_REQUEST_ERROR = 2;

    private void initList() {

        taskInfoAdapter = new taskInfoAdapter(taskList);
//        learntAdapter = new LearningFragment.courselearntInfoAdapter(learntData);
        CustomLinearLayoutManager layoutmanager = new CustomLinearLayoutManager(MyApplication.context);
        layoutmanager.setScrollEnabled(true);
        //设置RecyclerView 布局
//        recyclerMyclassLearnt.setLayoutManager(layoutmanager);
        recyclerTask.setLayoutManager(layoutmanager);
        new Thread() {
            @Override
            public void run() {
                try {
                    //暂时模拟读取json数据
                    handler.sendEmptyMessage(WHAT_REQUEST_SUCCESS);
                } catch (Exception e) {
                    handler.sendEmptyMessage(WHAT_REQUEST_ERROR);
                    Log.e("TAG", "加载数据失败");
                }
            }
        }.start();
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_REQUEST_SUCCESS:
                    recyclerTask.setAdapter(taskInfoAdapter);
//                    recyclerMyclassLearnt.setAdapter(learningAdapter);
                    break;
                case WHAT_REQUEST_ERROR:
                    Toast.makeText(MyApplication.context, "加载数据失败", Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };

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
