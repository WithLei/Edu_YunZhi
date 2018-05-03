package com.android.renly.edu_yunzhi_teacher.Fragment;

import android.annotation.SuppressLint;
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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi_teacher.Activity.TeacherAddClassActivity;
import com.android.renly.edu_yunzhi_teacher.Bean.Course;
import com.android.renly.edu_yunzhi_teacher.Bean.Course_forAdd;
import com.android.renly.edu_yunzhi_teacher.Common.BaseFragment;
import com.android.renly.edu_yunzhi_teacher.Common.MyApplication;
import com.android.renly.edu_yunzhi_teacher.R;
import com.android.renly.edu_yunzhi_teacher.R2;
import com.android.renly.edu_yunzhi_teacher.UI.CustomLinearLayoutManager;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyclassFragment extends BaseFragment {

    @BindView(R2.id.tv_class_classinfo)
    TextView tvClassClassinfo;
    @BindView(R2.id.tv_class_addclass)
    TextView tvClassAddclass;
    @BindView(R2.id.recycler_myclass_learning)
    RecyclerView recyclerMyclassLearning;
    @BindView(R2.id.tv_myclass_seeall)
    TextView tvMyclassSeeall;
    @BindView(R2.id.ll_class_learnt)
    LinearLayout llClassLearnt;
    @BindView(R2.id.recycler_myclass_learnt)
    RecyclerView recyclerMyclassLearnt;
    @BindView(R2.id.rl_class_learnt)
    RelativeLayout rlClassLearnt;
    @BindView(R2.id.test)
    ImageView test;

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
        //初始化数据
        initCoursedata();
        //初始化列表
        initList();
        //如果为教师用户，将进行以下操作
        llClassLearnt.setVisibility(View.INVISIBLE);
        rlClassLearnt.setVisibility(View.INVISIBLE);
        tvClassClassinfo.setText("任教课程");
    }

    @Override
    public int getLayoutid() {
        return R.layout.fragment_myclass;
    }

    public List<Course> learningData;//正在学习的课程
    public List<Course> learntData;//已经学习的课程
    public courseInfoAdapter learningAdapter;
    public courseInfoAdapter learntAdapter;

    //初始化课程列表【暂时写死
    public void initCoursedata() {
        learningData = new ArrayList<>();
        learntData = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            //1.
            Course firstCourse = new Course();
            firstCourse.name = "Python免费公开课：零基础入门课程";
            firstCourse.imgUrl = "http://edu-image.nosdn.127.net/73b5696e-4dfa-4eeb-8525-4a65f05c3b05.jpg?imageView&quality=100";
            firstCourse.teacherName = "2017.09.01 - 2018.02.16";

            //2.
            Course secondCourse = new Course();
            secondCourse.name = "Android开发基础教程";
            secondCourse.imgUrl = "http://edu-image.nosdn.127.net/F230B3E8A64D765AC0F35C4173AECA03.jpg?imageView&thumbnail=223y124&quality=100";
            secondCourse.teacherName = "2018.01.03 - 2018.01.16";

            //3.
            Course thirdCourse = new Course();
            thirdCourse.name = "Spark大数据技术";
            thirdCourse.imgUrl = "http://edu-image.nosdn.127.net/0731fab7-4070-4c1d-ba6a-2291a42f32cd.jpg?imageView&quality=100";
            thirdCourse.teacherName = "2017.04.01 - 2018.02.05";

            learningData.add(firstCourse);
            learningData.add(secondCourse);
            learningData.add(thirdCourse);

            learntData.add(secondCourse);
            learntData.add(firstCourse);
            learntData.add(thirdCourse);
        }
    }

    @OnClick(R2.id.tv_class_addclass)
    public void onViewClicked() {
        //如果为教师用户，将进行以下操作
        Intent intent = new Intent(getActivity(), TeacherAddClassActivity.class);
        //携带教师用户的用户名操作，先指定为空
        String teecherName = "";
        intent.putExtra("teacherName", teecherName);
        int requestCode = 1;
        startActivityForResult(intent, requestCode);
    }

    public class courseInfoAdapter extends RecyclerView.Adapter<courseInfoAdapter.ViewHolder> {
        private List<Course> learningData;//正在学习的课程

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img;
            TextView title;
            TextView teacher;

            public ViewHolder(View view) {
                super(view);
                img = view.findViewById(R2.id.iv_item_class_img);
                title = view.findViewById(R2.id.tv_item_class_title);
                teacher = view.findViewById(R2.id.tv_item_class_teacher);
            }
        }

        public courseInfoAdapter(List<Course> learningData) {
            this.learningData = learningData;
        }

        //加载item 的布局  创建ViewHolder实例
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        //对RecyclerView子项数据进行赋值
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Course course = learningData.get(position);
            //设置数据
            holder.title.setText(course.name);
            holder.teacher.setText(course.teacherName);
            String imagePath = course.imgUrl;
            Picasso.with(MyApplication.context).load(imagePath).into(holder.img);
        }

        //返回子项个数
        @Override
        public int getItemCount() {
            return learningData.size();
        }
    }

    protected static final int WHAT_REQUEST_SUCCESS = 1;
    protected static final int WHAT_REQUEST_ERROR = 2;

    private void initList() {

        learningAdapter = new courseInfoAdapter(learningData);
        //        learntAdapter = new LearningFragment.courselearntInfoAdapter(learntData);
        CustomLinearLayoutManager layoutmanager = new CustomLinearLayoutManager(MyApplication.context);
        layoutmanager.setScrollEnabled(false);
        //设置RecyclerView 布局
        //        recyclerMyclassLearnt.setLayoutManager(layoutmanager);
        recyclerMyclassLearning.setLayoutManager(layoutmanager);
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
                    recyclerMyclassLearning.setAdapter(learningAdapter);
                    //                    recyclerMyclassLearnt.setAdapter(learningAdapter);
                    break;
                case WHAT_REQUEST_ERROR:
                    Toast.makeText(MyApplication.context, "加载数据失败", Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //接收从TeacherAddActivity传回的Intent，并从中抽取出所需的数据刷新页面
        if (requestCode == 1 && resultCode == 2) {
            //简单起见，先用Course_forAdd代替Course
            Course_forAdd courseMsg = (Course_forAdd) data.getParcelableExtra("CourseMsg");
            Course addedCourse = new Course();
            addedCourse.name = courseMsg.getName();
            addedCourse.imgUrl = "https://user.qzone.qq.com/1147158321/infocenter";
            addedCourse.teacherName = courseMsg.getTeacherName();
            learningData.add(0,addedCourse);
            learningAdapter.notifyDataSetChanged();
            test.setImageBitmap(courseMsg.getClassLogo());
        }
    }
}
