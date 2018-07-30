package com.android.renly.edu_yunzhi.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.renly.edu_yunzhi.Adapter.ListDropDownAdapter;
import com.android.renly.edu_yunzhi.Bean.Task;
import com.android.renly.edu_yunzhi.Common.AppNetConfig;
import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.Common.MyApplication;
import com.android.renly.edu_yunzhi.R;
import com.android.renly.edu_yunzhi.UI.CustomLinearLayoutManager;
import com.android.renly.edu_yunzhi.UI.DropDownMenu;
import com.android.renly.edu_yunzhi.Utils.UIUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cz.msebera.android.httpclient.Header;

public class TaskActivity extends BaseActivity {
    @BindView(R.id.task_img)
    ImageView taskImg;
    @BindView(R.id.dropDownMenu)
    DropDownMenu mDropDownMenu;
    @BindView(R.id.task_title)
    TextView taskTitle;
    RecyclerView recyclerTask;
    @BindView(R.id.fl_task_title)
    FrameLayout flTaskTitle;

    @Override
    protected void initData() {
        initTaskData();
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
    private String types[] = {"全部类型", "答疑讨论", "课外活动", "实验设计"};

    private int constellationPosition = 0;

    private boolean isStudent;
    private long userID;

    private void initView() {
        if (isStudent){
            flTaskTitle.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        }else{
            flTaskTitle.setBackgroundColor(getResources().getColor(R.color.colorTeacherPrimary));
        }
        showTaskList = taskList;

        //init process menu
        final ListView processView = new ListView(this);
        processView.setDividerHeight(0);
        processAdapter = new ListDropDownAdapter(this, Arrays.asList(processs));
        processView.setAdapter(processAdapter);

        //init type menu
        final ListView typeView = new ListView(this);
        typeView.setDividerHeight(0);
        typeAdapter = new ListDropDownAdapter(this, Arrays.asList(types));
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
                switch (position) {
                    case 0:
                        Log.e("print", taskList.size() + "");
                        process = null;
                        filterList();
                        break;
                    case 1:
                        process = "进行中";
                        filterList();
                        break;
                    case 2:
                        process = "已结束";
                        filterList();
                        break;
                }
                initList();
//                mDropDownMenu.setDropDownMenu(Arrays.asList(headers), popupViews, recyclerTask);
                mDropDownMenu.closeMenu();
            }
        });

        typeView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                typeAdapter.setCheckItem(position);
                mDropDownMenu.setTabText(position == 0 ? headers[1] : types[position]);
//                init context view
                switch (position) {
                    case 0:
                        type = null;
                        filterList();
                        break;
                    case 1:
                        type = "答疑讨论";
                        filterList();
                        break;
                    case 2:
                        type = "课外活动";
                        filterList();
                        break;
                    case 3:
                        type = "实验设计";
                        filterList();
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


    private void initTaskData() {
        taskList = new ArrayList<>();

        SharedPreferences sp = getSharedPreferences("user_info", MODE_PRIVATE);
        isStudent = sp.getBoolean("isStudent", false);
        userID = sp.getLong("userID", 0);

        RequestParams params = new RequestParams();
        params.put("id", userID);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(AppNetConfig.GET_MYTRAIN, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String response = new String(responseBody);
                JSONArray array = JSON.parseArray(response);
                for (int i = 0; i < array.size(); i++) {
                    JSONObject object = array.getJSONObject(i);
                    Task task = new Task();
                    task.title = object.getString("title");
                    switch (object.getInteger("type")) {
                        case 0:
                            task.type = "答疑讨论";
                            break;
                        case 1:
                            task.type = "课外活动";
                            break;
                        case 2:
                            task.type = "实验设计";
                            break;
                        default:
                            task.type = "未分类";
                            break;
                    }
                    task.process = object.getInteger("process") == 1 ? "已结束" : "进行中";
                    task.joinNum = (int) (Math.random() * 100) + 1;
                    task.teacherName = object.getString("poster");
                    task.startTime = UIUtils.timeStampToShortTime(object.getLong("time"));
                    task.content = object.getString("content");


                    taskList.add(task);
                }
                initFailTaskData();
                initView();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(TaskActivity.this, "网络开小差咯", Toast.LENGTH_SHORT).show();
                initFailTaskData();
                initView();
            }
        });
    }

    List<Task> taskList;//通知列表
    List<Task> showTaskList;//用于展示的通知列表
    String process, type;

    //初始化实训列表
    public void initFailTaskData() {
        for (int i = 0; i < 2; i++) {
            //1.
            Task firstTask = new Task();
            firstTask.title = "测试 - 答疑讨论";
            firstTask.startTime = "2018 - 04 - 10";
            firstTask.teacherName = "陈老师";
            firstTask.joinNum = 33;
            firstTask.process = "进行中";
            firstTask.type = "答疑讨论";
            firstTask.content = "测试内容1";

            //2.
            Task secondTask = new Task();
            secondTask.title = "测试 - 课外活动";
            secondTask.startTime = "2018 - 04 - 07";
            secondTask.teacherName = "王老师";
            secondTask.joinNum = 65;
            secondTask.process = "进行中";
            secondTask.type = "课外活动";
            secondTask.content = "测试内容2";

            //3.
            Task thirdTask = new Task();
            thirdTask.title = "测试 - 实验设计";
            thirdTask.startTime = "2018 - 04 - 05";
            thirdTask.teacherName = "潘老师";
            thirdTask.joinNum = 18;
            thirdTask.process = "已结束";
            thirdTask.type = "实验设计";
            thirdTask.content = "测试内容3";

            taskList.add(firstTask);
            taskList.add(secondTask);
            taskList.add(thirdTask);
        }
    }

    public void filterList() {
        showTaskList = new ArrayList<>();
        for (int i = 0; i < taskList.size(); i++) {
            Task task = taskList.get(i);
            if (task.process.equals(process) || process == null)
                if (task.type.equals(type) || type == null)
                    showTaskList.add(task);
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
            final Task task = taskList.get(position);
            //设置数据
            holder.title.setText(task.title);
            holder.startTime.setText(task.startTime);
            holder.teacherName.setText(task.teacherName);
            holder.process.setText(task.process);
            holder.joinNum.setText("共 " + task.joinNum + " 人参加");
            /**
             * 类型
             * 答疑讨论 - 课外活动 - 实验设计
             * 进度
             * 进行中 - 已结束
             */
            switch (task.type) {
                case "答疑讨论":
                    holder.typeImg.setImageDrawable(getResources().getDrawable(R.drawable.activitieslisticondiscuss));
                    break;
                case "课外活动":
                    holder.typeImg.setImageDrawable(getResources().getDrawable(R.drawable.activitieslisticontesting));
                    break;
                case "实验设计":
                    holder.typeImg.setImageDrawable(getResources().getDrawable(R.drawable.activitieslisticonassignment));
                    break;
                default:
                    holder.typeImg.setImageDrawable(getResources().getDrawable(R.drawable.activitieslisticondiscuss));
                    break;
            }
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(TaskActivity.this,TaskInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("Task",JSON.toJSONString(task));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {

                    return false;
                }
            });
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
        taskInfoAdapter = new taskInfoAdapter(showTaskList);
        CustomLinearLayoutManager layoutmanager = new CustomLinearLayoutManager(MyApplication.context);
        layoutmanager.setScrollEnabled(true);
        //设置RecyclerView 布局
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

    private Unbinder unbinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @OnClick(R.id.task_img)
    public void onViewClicked() {
        finish();
    }
}
