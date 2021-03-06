package com.android.renly.edu_yunzhi.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi.Bean.Homework;
import com.android.renly.edu_yunzhi.Common.BaseFragment;
import com.android.renly.edu_yunzhi.Common.MyApplication;
import com.android.renly.edu_yunzhi.R;
import com.android.renly.edu_yunzhi.UI.CustomLinearLayoutManager;
import com.android.renly.edu_yunzhi.Utils.UIUtils;
import com.example.answer.AnalogyExaminationActivity;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class MyworkFragment extends BaseFragment {
    @BindView(R.id.tv_mywork_seeall)
    TextView tvMyworkSeeall;
    @BindView(R.id.recycler_mywork_homework)
    RecyclerView recyclerMyworkHomework;

    @Override
    protected void initData(Context content) {
        initHomeworkdata();
        initList();
        initItemClickListener();
    }

    private void initItemClickListener() {
        homeworkAdapter.setOnItemClickListener(new homeworkInfoAdapter.OnItemClickListener(){

            @Override
            public void onClick(int position) {
                Intent intent=new Intent(getActivity(),AnalogyExaminationActivity.class);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {

            }
        });
    }

    @Override
    public int getLayoutid() {
        return R.layout.fragment_mywork;
    }

    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    List<Homework>homeworkList;//作业列表

    //初始化课程列表【暂时写死
    public void initHomeworkdata() {
        homeworkList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            //1.
            Homework firstHomework = new Homework();
            firstHomework.name = "命题的等值演算、范式与应用";
            firstHomework.workTime = "5分钟";
            firstHomework.publishTime = "2018.04.11 20:10";
            firstHomework.teacherName = "潘老师";
            firstHomework.situation = "待完成";

            //2.
            Homework secondHomework = new Homework();
            secondHomework.name = "推理理论、一阶逻辑的基本概念";
            secondHomework.workTime = "1小时40分钟";
            secondHomework.publishTime = "2018.03.28 12:25";
            secondHomework.teacherName = "陈老师";
            secondHomework.situation = "待批";

            //3.
            Homework thirdHomework = new Homework();
            thirdHomework.name = "课堂测试——等值验算与范式";
            thirdHomework.workTime = "30分钟";
            thirdHomework.publishTime = "2018.03.18 8:10";
            thirdHomework.situation = "90分";
            thirdHomework.teacherName = "周老师";

            homeworkList.add(firstHomework);
            homeworkList.add(secondHomework);
            homeworkList.add(thirdHomework);
        }
    }

    public static class homeworkInfoAdapter extends RecyclerView.Adapter<homeworkInfoAdapter.ViewHolder> {
        private List<Homework> homeworkList;//作业列表
        private OnItemClickListener mOnItemClickListener;

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView workTime;
            TextView publishTime;
            TextView situantion;
            TextView teacher;

            public ViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.tv_item_homework_title);
                workTime = view.findViewById(R.id.tv_item_homework_worktime);
                publishTime = view.findViewById(R.id.tv_item_homework_publishtime);
                situantion = view.findViewById(R.id.tv_item_homework_situation);
                teacher = view.findViewById(R.id.tv_item_homework_teachername);
            }
        }

        public homeworkInfoAdapter(List<Homework> homeworkList) {
            this.homeworkList = homeworkList;
        }

        //加载item 的布局  创建ViewHolder实例
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_homework, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        //对RecyclerView子项数据进行赋值
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Homework homework = homeworkList.get(position);
            //设置数据
            holder.title.setText(homework.name);
            holder.teacher.setText(homework.teacherName);
            holder.publishTime.setText("  " + homework.publishTime);
            holder.workTime.setText("  " + homework.workTime);
            holder.situantion.setText(homework.situation);
            /***
             * 待完成 -> 待批 -> 分数
             *   蓝   ->  绿  ->  红
             * 作业完成进度
             */
            switch (homework.situation){
                case "待完成":
                    holder.situantion.setTextColor(UIUtils.getColor(R.color.home_back_selected));
                    break;
                case "待批":
                    holder.situantion.setTextColor(UIUtils.getColor(R.color.summerGreen));
                    break;
                default:
                    holder.situantion.setTextColor(UIUtils.getColor(R.color.color_c61a04));
            }

            if( mOnItemClickListener!= null){
                holder.itemView.setOnClickListener( new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnItemClickListener.onClick(position);
                    }
                });
                holder.itemView.setOnLongClickListener( new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        mOnItemClickListener.onLongClick(position);
                        return false;
                    }
                });
            }
        }

        //返回子项个数
        @Override
        public int getItemCount() {
            return homeworkList.size();
        }

        public interface OnItemClickListener{
            void onClick( int position);
            void onLongClick( int position);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
            this. mOnItemClickListener=onItemClickListener;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
    protected static final int WHAT_REQUEST_SUCCESS = 1;
    protected static final int WHAT_REQUEST_ERROR = 2;

    public homeworkInfoAdapter homeworkAdapter;

    private void initList() {
        homeworkAdapter = new homeworkInfoAdapter(homeworkList);
        CustomLinearLayoutManager layoutmanager = new CustomLinearLayoutManager(MyApplication.context);
        layoutmanager.setScrollEnabled(true);
        //设置RecyclerView 布局
        recyclerMyworkHomework.setLayoutManager(layoutmanager);
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
                    recyclerMyworkHomework.setAdapter(homeworkAdapter);
//                    recyclerMyclassLearnt.setAdapter(learningAdapter);
                    break;
                case WHAT_REQUEST_ERROR:
                    Toast.makeText(MyApplication.context, "加载数据失败", Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };

}
