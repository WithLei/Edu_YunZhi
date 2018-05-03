package com.android.renly.edu_yunzhi_teacher.Fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi_teacher.Bean.Topics;
import com.android.renly.edu_yunzhi_teacher.Common.AppNetConfig;
import com.android.renly.edu_yunzhi_teacher.Common.BaseFragment;
import com.android.renly.edu_yunzhi_teacher.Common.MyApplication;
import com.android.renly.edu_yunzhi_teacher.R;
import com.android.renly.edu_yunzhi_teacher.R2;
import com.android.renly.edu_yunzhi_teacher.UI.CustomLinearLayoutManager;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.android.renly.edu_yunzhi_teacher.Fragment.HomeFragment.WHAT_REQUEST_ERROR;
import static com.android.renly.edu_yunzhi_teacher.Fragment.HomeFragment.WHAT_REQUEST_SUCCESS;

public class MsgFragment extends BaseFragment implements OnClickListener {

    private static final int TYPE_HOT = 0;
    private static final int TYPE_NEW = 1;

    private int currentType = 1;
    private int CurrentPage = 1;

    @BindView(R2.id.btn_1)
    RadioButton btn1;
    @BindView(R2.id.btn_2)
    RadioButton btn2;
    @BindView(R2.id.btn_3)
    RadioButton btn3;
    @BindView(R2.id.btn_change)
    RadioGroup btnChange;
    @BindView(R2.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R2.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

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
        initTopicsdata();
        initAdapter();
    }

    private void initAdapter() {
        adapter = new topicItemInfoAdapter(data);
        CustomLinearLayoutManager layoutmanager = new CustomLinearLayoutManager(MyApplication.context);
        layoutmanager.setScrollEnabled(true);
        //设置RecyclerView 布局
        recyclerView.setLayoutManager(layoutmanager);

        //解决swipelayout与Recyclerview的冲突
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                //计算Recyclerview第一个item的位置是否可见
                int topRowVerticalPosition
                        =(recyclerView == null ||recyclerView.getChildCount() == 0) ? -1 : 1;

                //当第一个item不可见时，设置SwipeRefreshLayout不可用
                refreshLayout.setEnabled(topRowVerticalPosition >= 0);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });

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

    @Override
    public int getLayoutid() {
        return R.layout.fragment_msg;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R2.id.btn_1:
                Toast.makeText(MyApplication.context, "热帖界面", Toast.LENGTH_SHORT).show();
                break;
            case R2.id.btn_3:
                Toast.makeText(MyApplication.context, "消息界面", Toast.LENGTH_SHORT).show();
                break;
        }
    }
    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        btn1.setText("热帖");
        btn2.setVisibility(View.GONE);
        btn3.setText("消息");

        refreshLayout.setOnRefreshListener(this::refresh);

        btnChange.setOnCheckedChangeListener((radioGroup, id) -> {
            int pos = -1;
            if (id == R2.id.btn_1) {
                pos = TYPE_NEW;
            } else {
                pos = TYPE_HOT;
            }

            if (pos != currentType) {
                currentType = pos;
                refreshLayout.setRefreshing(true);
                refresh();
            }
        });
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void refresh() {
        CurrentPage = 1;
        getData();
    }

    private void getData() {
        handler.sendEmptyMessageDelayed(DELAY, 2000);
    }

    private static final int DELAY = 5;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case DELAY:
                    refreshLayout.setRefreshing(false);
                    break;
                case WHAT_REQUEST_SUCCESS:
                    recyclerView.setAdapter(adapter);
            }
        }
    };

    public List<Topics> data;
    public MsgFragment.topicItemInfoAdapter adapter;

    public class topicItemInfoAdapter extends RecyclerView.Adapter<MsgFragment.topicItemInfoAdapter.ViewHolder> {
        private List<Topics> topicsList;

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView article_title;
            TextView author_name;
            TextView reply_count;
            TextView is_image;

            public ViewHolder(View view) {
                super(view);
                article_title = view.findViewById(R2.id.article_title);
                author_name = view.findViewById(R2.id.author_name);
                reply_count = view.findViewById(R2.id.reply_count);
                is_image = view.findViewById(R2.id.is_image);
            }
        }

        public topicItemInfoAdapter(List<Topics> topicsList) {
            this.topicsList = topicsList;
        }

        //加载item 的布局  创建ViewHolder实例
        @Override
        public MsgFragment.topicItemInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_me,parent,false);
            MsgFragment.topicItemInfoAdapter.ViewHolder holder = new MsgFragment.topicItemInfoAdapter.ViewHolder(view);
            return holder;
        }

        //对RecyclerView子项数据进行赋值
        @Override
        public void onBindViewHolder(MsgFragment.topicItemInfoAdapter.ViewHolder holder, int position) {
            Topics topics = topicsList.get(position);
            //设置数据
            holder.article_title.setText(topics.title);
            holder.author_name.setText("\uf2c0 "+topics.name);
            holder.reply_count.setText("\uf0e6 "+topics.comment);
            if(topics.hasImg == false){
                holder.is_image.setVisibility(View.GONE);
            }
        }

        //返回子项个数
        @Override
        public int getItemCount() {
            return topicsList.size();
        }
    }


    //论坛【暂时写死
    public void initTopicsdata() {
        data = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            //1.
            Topics firstTopic = new Topics();
            firstTopic.title = "图书馆4.23世界图书日系列活动第一弹";
            firstTopic.name = "西电图书馆";
            firstTopic.comment = 151;
            firstTopic.hasImg = false;

            //2.
            Topics secondTopic = new Topics();
            secondTopic.title = "马上生日了，还在图书馆自习";
            secondTopic.name = "doubihui";
            secondTopic.comment = 47;
            secondTopic.hasImg = false;

            //3.
            Topics thirdTopic = new Topics();
            thirdTopic.title = "大佬们，学校附近有没有专业的给眼睛验光的医院啊";
            thirdTopic.name = "合伙人";
            thirdTopic.comment = 100;
            thirdTopic.hasImg = false;

            //4.
            Topics forthTopic = new Topics();
            forthTopic.title = "求C++大佬";
            forthTopic.name = "vimm";
            forthTopic.comment = 71;
            forthTopic.hasImg = false;

            //5
            Topics fifthTopic = new Topics();
            fifthTopic.title = "Paperpass18% 笔杆8%，知网大概什么水平？";
            fifthTopic.name = "依旧彷徨";
            fifthTopic.comment = 54;
            fifthTopic.hasImg = true;

            //6
            Topics sixthTopic = new Topics();
            sixthTopic.title = "和省内流量说拜拜！移动新版流量包上线：5元30M成最大槽点";
            sixthTopic.name = "激萌路小叔";
            sixthTopic.comment = 25;
            sixthTopic.hasImg = true;

            //7
            Topics seventhTopic = new Topics();
            seventhTopic.title = "欲望总是不可控制";
            seventhTopic.name = "zhjcyh";
            seventhTopic.comment = 268;
            seventhTopic.hasImg = false;

            //8
            Topics eigthTopic = new Topics();
            eigthTopic.title = "和喜欢的人提分手是一种什么体验？";
            eigthTopic.name = "eigthTopic";
            eigthTopic.comment = 64;
            eigthTopic.hasImg = false;

            data.add(firstTopic);
            data.add(secondTopic);
            data.add(thirdTopic);
            data.add(forthTopic);
            data.add(fifthTopic);
            data.add(sixthTopic);
            data.add(seventhTopic);
            data.add(eigthTopic);
        }
    }
}
