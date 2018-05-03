package com.android.renly.edu_yunzhi_teacher.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi_teacher.Bean.Notice;
import com.android.renly.edu_yunzhi_teacher.Common.BaseActivity;
import com.android.renly.edu_yunzhi_teacher.Common.MyApplication;
import com.android.renly.edu_yunzhi_teacher.R;
import com.android.renly.edu_yunzhi_teacher.R2;
import com.android.renly.edu_yunzhi_teacher.UI.CircleImageView;
import com.android.renly.edu_yunzhi_teacher.UI.CustomLinearLayoutManager;
import com.android.renly.edu_yunzhi_teacher.UI.SlideLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NoticeActivity extends BaseActivity {
    @BindView(R2.id.notice_img)
    CircleImageView noticeImg;
    @BindView(R2.id.notice_title)
    TextView noticeTitle;
    @BindView(R2.id.notice_add)
    CircleImageView noticeAdd;
    @BindView(R2.id.lv_notice_noticeList)
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

    List<Notice> noticeList;//通知列表

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
            LinearLayout content;
            LinearLayout menu;


            public ViewHolder(View view) {
                super(view);
                content = view.findViewById(R2.id.item_content);
                menu = view.findViewById(R2.id.item_menu);
                SlideLayout slideLayout = (SlideLayout) view;
                slideLayout.setOnStateChangeListenter(new MyOnStateChangeListenter());
            }
        }

        public noticeInfoAdapter(List<Notice> noticeList) {
            this.noticeList = noticeList;
        }

        //加载item 的布局  创建ViewHolder实例
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notice_main, parent, false);
            ViewHolder holder = new ViewHolder(view);
            return holder;
        }

        //对RecyclerView子项数据进行赋值
        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            final Notice notice = noticeList.get(position);
            //设置数据
            TextView title= (TextView)holder.content.findViewById(R2.id.tv_item_notice_title);
            TextView teachername= (TextView)holder.content.findViewById(R2.id.tv_item_notice_teachername);
            TextView time= (TextView)holder.content.findViewById(R2.id.tv_item_notice_time);
            title.setText(notice.title);
            teachername.setText(notice.teacherName);
            time.setText(notice.time);
            holder.content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(NoticeActivity.this,"具体内容 position =" +position,Toast.LENGTH_SHORT).show();
                }
            });
            TextView delete = (TextView)holder.menu.findViewById(R2.id.notice_menu_delete);
            TextView modify = (TextView)holder.menu.findViewById(R2.id.notice_menu_modify);
            //设置修改TextView的点击事件监听，通过带回调的intent启动NoticeModifyActivity进入修改界面
            //并把当前项删除，刷新界面
            modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int requestCode = 3;
                    Intent intent = new Intent(NoticeActivity.this, NoticeModifyActivity.class);
                    String[] before = new String[2];
                    before[0] = notice.title;
                    before[1] = notice.teacherName;
                    intent.putExtra("Before",before);
                    startActivityForResult(intent, requestCode);
                    noticeList.remove(position);
                    noticeAdapter.notifyDataSetChanged();
                }
            });
            //设置删除TextView的点击事件监听，删除当前项，并刷新界面
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    noticeList.remove(position);
                    noticeAdapter.notifyDataSetChanged();
                }
            });
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

    //设置返回按钮notice_img以及新建按钮notice_add的点击事件监听
    //返回按钮：finish当前NoticeActivity
    //新建按钮：带回调的Intent启动NoticeAddActivity进入新建页面
    @OnClick({R2.id.notice_img, R2.id.notice_add})
    public void onClick(View v) {
        switch (v.getId()) {
            case R2.id.notice_img:
                finish();
                break;
            case R2.id.notice_add:
                int requestCode = 1;
                Intent intent = new Intent(NoticeActivity.this, NoticeAddActivity.class);
                startActivityForResult(intent, requestCode);
                break;
        }
    }

    //从NoticeAddAcivity以及从NoticeModifyAcivity传回的intent对象，从中获取数据，并修改noticeList，刷新界面
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //同一时间只有一个intent传回，故同时间只要处理一次
        if (((requestCode == 1) || (requestCode == 3)) && resultCode == 2) {
            String[] context = data.getStringArrayExtra("Context");
            Notice addedNotice = new Notice();
            addedNotice.teacherName = context[0];
            addedNotice.title = context[1];
            //获取当前时间作为发布时间
            Calendar calendar = Calendar.getInstance();
            addedNotice.time = calendar.get(Calendar.YEAR) + " - " + (calendar.get(Calendar.MONTH) + 1) + " - " +
                    calendar.get(Calendar.DAY_OF_MONTH) + "  " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
            noticeList.add(addedNotice);
            noticeAdapter.notifyDataSetChanged();
        }
//        }else if (requestCode == 3 && resultCode == 2){
//            String[] context = data.getStringArrayExtra("Context");
//            Notice addedNotice = new Notice();
//            addedNotice.teacherName = context[0];
//            addedNotice.title = context[1];
//            Calendar calendar = Calendar.getInstance();
//            addedNotice.time = calendar.get(Calendar.YEAR) + " - " + (calendar.get(Calendar.MONTH) + 1) + " - " +
//                    calendar.get(Calendar.DAY_OF_MONTH) + "  " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE);
//            noticeList.add(addedNotice);
//            noticeAdapter.notifyDataSetChanged();
//        }
    }

    private SlideLayout slideLayout;
    class MyOnStateChangeListenter implements SlideLayout.OnStateChangeListenter {

        @Override
        public void onClose(SlideLayout layout) {
            if(slideLayout ==layout){
                slideLayout = null;
            }

        }

        @Override
        public void onDown(SlideLayout layout) {
            if(slideLayout != null && slideLayout!=layout){
                slideLayout.closeMenu();
            }

        }

        @Override
        public void onOpen(SlideLayout layout) {
            slideLayout = layout;
        }
    }
}
