package com.android.renly.edu_yunzhi.Activity;

import android.annotation.SuppressLint;
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

import com.android.renly.edu_yunzhi.Bean.Notice;
import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.Common.MyApplication;
import com.android.renly.edu_yunzhi.R;
import com.android.renly.edu_yunzhi.UI.CircleImageView;
import com.android.renly.edu_yunzhi.UI.CustomLinearLayoutManager;
import com.android.renly.edu_yunzhi.Utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoticeActivity extends BaseActivity {
    @BindView(R.id.notice_img)
    ImageView noticeImg;
    @BindView(R.id.notice_title)
    TextView noticeTitle;
    @BindView(R.id.lv_notice_noticeList)
    RecyclerView lvNoticeNoticeList;

    @Override
    protected void initData() {
        initNoticeData();
        initList();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notice;
    }

    List<Notice>noticeList;//通知列表

    //初始化公告列表【暂时写死
    public void initNoticeData() {
        noticeList = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            //1.
            Notice firstNotice = new Notice();
            firstNotice.title = "【开课啦】本学期课程通知";
            firstNotice.time = "2018 - 03 - 01 09:59";
            firstNotice.teacherName = "陈老师";

            //2.
            Notice secondNotice = new Notice();
            secondNotice.title = "【停课】本周数据库课程停课通知";
            secondNotice.time = "2018 - 02 - 29 14:32";
            secondNotice.teacherName = "潘老师";

            //3.
            Notice thirdNotice = new Notice();
            thirdNotice.title = "【更换教室】本周实验课更换教室通知";
            thirdNotice.time = "2018 - 02 - 12 12:34";
            thirdNotice.teacherName = "周老师";

            noticeList.add(firstNotice);
            noticeList.add(secondNotice);
            noticeList.add(thirdNotice);
        }
    }

    protected static final int WHAT_REQUEST_SUCCESS = 1;
    protected static final int WHAT_REQUEST_ERROR = 2;

    public noticeInfoAdapter noticeAdapter;

    public class noticeInfoAdapter extends RecyclerView.Adapter<noticeInfoAdapter.ViewHolder> {
        private List<Notice> noticeList;//通知列表

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView title;
            TextView teacherName;
            TextView publishTime;

            public ViewHolder(View view) {
                super(view);
                title = view.findViewById(R.id.tv_item_notice_title);
                teacherName = view.findViewById(R.id.tv_item_notice_teachername);
                publishTime = view.findViewById(R.id.tv_item_notice_time);
            }
        }

        public noticeInfoAdapter(List<Notice> noticeList) {
            this.noticeList = noticeList;
        }

        //加载item 的布局  创建ViewHolder实例
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        //对RecyclerView子项数据进行赋值
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Notice notice = noticeList.get(position);
            //设置数据
            holder.title.setText(notice.title);
            holder.publishTime.setText("  " + notice.time);
            holder.teacherName.setText("  " + notice.teacherName);
        }

        //返回子项个数
        @Override
        public int getItemCount() {
            return noticeList.size();
        }
    }

    private void initList() {
        noticeAdapter = new noticeInfoAdapter(noticeList);
        CustomLinearLayoutManager layoutmanager = new CustomLinearLayoutManager(MyApplication.context);
        layoutmanager.setScrollEnabled(false);
        //设置RecyclerView 布局
        lvNoticeNoticeList.setLayoutManager(layoutmanager);
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
                    lvNoticeNoticeList.setAdapter(noticeAdapter);
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

    @OnClick(R.id.notice_img)
    public void onViewClicked() {
        finish();
    }
}
