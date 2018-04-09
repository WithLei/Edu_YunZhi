package com.android.renly.edu_yunzhi.Fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi.Activity.LoadFragmentActivity;
import com.android.renly.edu_yunzhi.Bean.Course;
import com.android.renly.edu_yunzhi.Bean.News;
import com.android.renly.edu_yunzhi.Common.BaseFragment;
import com.android.renly.edu_yunzhi.Common.MyApplication;
import com.android.renly.edu_yunzhi.R;
import com.android.renly.edu_yunzhi.UI.BatchRadioButton;
import com.android.renly.edu_yunzhi.UI.CustomLinearLayoutManager;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;
import com.youth.banner.loader.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LearningFragment extends BaseFragment implements View.OnClickListener {

    @Bind(R.id.btn_1)
    BatchRadioButton btn1;
    @Bind(R.id.btn_2)
    BatchRadioButton btn2;
    @Bind(R.id.btn_3)
    BatchRadioButton btn3;
    @Bind(R.id.btn_change)
    RadioGroup btnChange;
    @Bind(R.id.fl_learning)
    FrameLayout flLearning;

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
        //默认显示首页
        setSelect(0);
        //登录判断
//        isLogin();
    }

    private MyclassFragment myclassFragment;
    private MyworkFragment myworkFragment;
    private MynoteFragment mynoteFragment;
    private FragmentTransaction transaction;

    private void setSelect(int select) {
        FragmentManager fragmentManager = this.getFragmentManager();
        transaction = fragmentManager.beginTransaction();
        //隐藏所有fragment
        hideFragments();
        switch (select) {
            case 0:
                if (myclassFragment == null) {
                    myclassFragment = new MyclassFragment();//commit()后调用生命周期方法
                    transaction.add(R.id.fl_learning, myclassFragment);
                }
                transaction.show(myclassFragment);//显示当前的Fragment
                break;
            case 1:
                if (myworkFragment == null) {
                    myworkFragment = new MyworkFragment();//commit()后调用生命周期方法
                    transaction.add(R.id.fl_learning, myworkFragment);
                }
                transaction.show(myworkFragment);//显示当前的Fragment
                break;
            case 2:
                if (mynoteFragment == null) {
                    mynoteFragment = new MynoteFragment();//commit()后调用生命周期方法
                    transaction.add(R.id.fl_learning, mynoteFragment);
                }
                transaction.show(mynoteFragment);//显示当前的Fragment
                break;
        }
        //提交事务
        transaction.commit();
    }

    private void hideFragments() {
        if (myclassFragment != null) {
            transaction.hide(myclassFragment);
        }
        if (myworkFragment != null) {
            transaction.hide(myworkFragment);
        }
        if (mynoteFragment != null) {
            transaction.hide(mynoteFragment);
        }
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
        //移除所有的未被执行的消息
//        handler.removeCallbacksAndMessages(null);
    }

    @OnClick({R.id.btn_1, R.id.btn_2, R.id.btn_3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                setSelect(0);
                break;
            case R.id.btn_2:
                setSelect(1);
                break;
            case R.id.btn_3:
                setSelect(2);
                break;
        }
    }

    private void isLogin() {
        //查看本地是否有用户的登录信息
        SharedPreferences sp = this.getActivity().getSharedPreferences("user_info", Context.MODE_PRIVATE);
        String name = sp.getString("name", "");
        if (TextUtils.isEmpty(name)) {
            //本地没有保存过用户信息，给出提示：登录
            doLogin();
        }
    }

    //给出提示：登录
    private void doLogin() {
        Toast.makeText(MyApplication.context,"未登录",Toast.LENGTH_SHORT).show();
        new AlertDialog.Builder(this.getActivity())
                .setTitle("提示")
                .setMessage("您还没有登录哦！么么~")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                            UIUtils.toast("进入登录页面",false);
                        LoadFragmentActivity.lunchFragment(MyApplication.context, LoginFragment.class,null);
                    }
                })
                .setCancelable(false)
                .show();
    }

    public class GlideImageLoader extends ImageLoader {
        //picasso 加载图片简单用法
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Picasso.with(context).load((String) path).into(imageView);
        }
    }

    public List<Course>learningData;//正在学习的课程
    public List<Course>learntData;//已经学习的课程
    public LearningFragment.courseInfoAdapter learningAdapter;
    public LearningFragment.courseInfoAdapter learntAdapter;

    //初始化课程列表【暂时写死
    public void initCoursedata() {
        learningData = new ArrayList<>();
        learntData = new ArrayList<>();
        for(int i=0;i<10;i++){
            //1.
            Course firstCourse = new Course();
            firstCourse.name = "Python免费公开课：零基础入门课程";
            firstCourse.imgUrl = "http://edu-image.nosdn.127.net/73b5696e-4dfa-4eeb-8525-4a65f05c3b05.jpg?imageView&quality=100";
            firstCourse.teacherName = "翁凯";

            //2.
            Course secondCourse = new Course();
            secondCourse.name = "Android开发基础教程";
            secondCourse.imgUrl = "http://edu-image.nosdn.127.net/F230B3E8A64D765AC0F35C4173AECA03.jpg?imageView&thumbnail=223y124&quality=100";
            secondCourse.teacherName = "孙麒";

            //3.
            Course thirdCourse = new Course();
            thirdCourse.name = "Spark大数据技术";
            thirdCourse.imgUrl = "http://edu-image.nosdn.127.net/0731fab7-4070-4c1d-ba6a-2291a42f32cd.jpg?imageView&quality=100";
            thirdCourse.teacherName = "云智教育";

            learningData.add(firstCourse);
            learningData.add(secondCourse);
            learningData.add(thirdCourse);

            learntData.add(secondCourse);
            learntData.add(firstCourse);
            learntData.add(thirdCourse);
        }
    }

    public class courseInfoAdapter extends RecyclerView.Adapter<LearningFragment.courseInfoAdapter.ViewHolder> {
        private List<Course>learningData;//正在学习的课程

        class ViewHolder extends RecyclerView.ViewHolder{
            ImageView img;
            TextView title;
            TextView teacher;

            public ViewHolder(View view) {
                super(view);
                img = view.findViewById(R.id.iv_item_class_img);
                title = view.findViewById(R.id.tv_item_class_title);
                teacher = view.findViewById(R.id.tv_item_class_teacher);


            }
        }

        public courseInfoAdapter(List<Course> learningData) {
            this.learningData = learningData;
        }

        //加载item 的布局  创建ViewHolder实例
        @Override
        public LearningFragment.courseInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_class,parent,false);
            LearningFragment.courseInfoAdapter.ViewHolder holder = new LearningFragment.courseInfoAdapter.ViewHolder(view);
            return holder;
        }

        //对RecyclerView子项数据进行赋值
        @Override
        public void onBindViewHolder(LearningFragment.courseInfoAdapter.ViewHolder holder, int position) {
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

        learningAdapter = new LearningFragment.courseInfoAdapter(learningData);
//        learntAdapter = new LearningFragment.courselearntInfoAdapter(learntData);
        CustomLinearLayoutManager layoutmanager = new CustomLinearLayoutManager(MyApplication.context);
        layoutmanager.setScrollEnabled(false);
        //设置RecyclerView 布局
        View.findViewById(R.id.recycler_myclass_learning).setLayoutManager(layoutmanager);
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
                    lvHomeNewsList.setAdapter(adapter);
                    break;
                case WHAT_REQUEST_ERROR:
                    Toast.makeText(MyApplication.context, "加载数据失败", Toast.LENGTH_LONG).show();
                    break;

            }
        }
    };

}
