package com.android.renly.edu_yunzhi.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.renly.edu_yunzhi.Bean.Topics;
import com.android.renly.edu_yunzhi.Common.BaseFragment;
import com.android.renly.edu_yunzhi.Common.MyApplication;
import com.android.renly.edu_yunzhi.R;
import com.android.renly.edu_yunzhi.UI.CustomLinearLayoutManager;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.android.renly.edu_yunzhi.Fragment.HomeFragment.WHAT_REQUEST_ERROR;
import static com.android.renly.edu_yunzhi.Fragment.HomeFragment.WHAT_REQUEST_SUCCESS;

public class HotNewsFragment extends BaseFragment {
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void initData(Context content) {
        initTopicsdata();
        initAdapter();
    }

    @Override
    public int getLayoutid() {
        return R.layout.fragment_hotnews;
    }

    private void initAdapter() {
        adapter = new HotNewsFragment.topicItemInfoAdapter(data);
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


    private Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
//        refreshLayout.setOnRefreshListener(this::refresh);
        return rootView;
    }

    private void refresh() {
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
    public HotNewsFragment.topicItemInfoAdapter adapter;

    public class topicItemInfoAdapter extends RecyclerView.Adapter<topicItemInfoAdapter.ViewHolder> {
        private List<Topics> topicsList;

        class ViewHolder extends RecyclerView.ViewHolder{
            TextView article_title;
            TextView author_name;
            TextView reply_count;
            TextView is_image;

            public ViewHolder(View view) {
                super(view);
                article_title = view.findViewById(R.id.article_title);
                author_name = view.findViewById(R.id.author_name);
                reply_count = view.findViewById(R.id.reply_count);
                is_image = view.findViewById(R.id.is_image);
            }
        }

        public topicItemInfoAdapter(List<Topics> topicsList) {
            this.topicsList = topicsList;
        }

        //加载item 的布局  创建ViewHolder实例
        @Override
        public HotNewsFragment.topicItemInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post_me,parent,false);
            HotNewsFragment.topicItemInfoAdapter.ViewHolder holder = new HotNewsFragment.topicItemInfoAdapter.ViewHolder(view);
            return holder;
        }

        //对RecyclerView子项数据进行赋值
        @Override
        public void onBindViewHolder(HotNewsFragment.topicItemInfoAdapter.ViewHolder holder, int position) {
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
            firstTopic.name = "云智图书馆";
            firstTopic.comment = 151;
            firstTopic.hasImg = false;

            //2.
            Topics secondTopic = new Topics();
            secondTopic.title = "马上生日了，还在图书馆自习";
            secondTopic.name = "喜羊羊";
            secondTopic.comment = 47;
            secondTopic.hasImg = false;

            //3.
            Topics thirdTopic = new Topics();
            thirdTopic.title = "大佬们，学校附近有没有专业的给眼睛验光的医院啊";
            thirdTopic.name = "美羊羊";
            thirdTopic.comment = 100;
            thirdTopic.hasImg = false;

            //4.
            Topics forthTopic = new Topics();
            forthTopic.title = "求C++大佬";
            forthTopic.name = "懒羊羊";
            forthTopic.comment = 71;
            forthTopic.hasImg = false;

            //5
            Topics fifthTopic = new Topics();
            fifthTopic.title = "Paperpass18% 笔杆8%，大概什么水平？";
            fifthTopic.name = "沸羊羊";
            fifthTopic.comment = 54;
            fifthTopic.hasImg = true;

            //6
            Topics sixthTopic = new Topics();
            sixthTopic.title = "和省内流量说拜拜！移动新版流量包上线：5元30M成最大槽点";
            sixthTopic.name = "暖羊羊";
            sixthTopic.comment = 25;
            sixthTopic.hasImg = true;

            //7
            Topics seventhTopic = new Topics();
            seventhTopic.title = "欲望总是不可控制";
            seventhTopic.name = "慢羊羊";
            seventhTopic.comment = 268;
            seventhTopic.hasImg = false;

            //8
            Topics eigthTopic = new Topics();
            eigthTopic.title = "和喜欢的人提分手是一种什么体验？";
            eigthTopic.name = "灰太狼";
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
