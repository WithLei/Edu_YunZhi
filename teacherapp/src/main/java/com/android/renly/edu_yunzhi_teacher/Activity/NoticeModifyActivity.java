package com.android.renly.edu_yunzhi_teacher.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi_teacher.Common.BaseActivity;
import com.android.renly.edu_yunzhi_teacher.R;
import com.android.renly.edu_yunzhi_teacher.R2;
import com.android.renly.edu_yunzhi_teacher.UI.CircleImageView;

import butterknife.BindView;
import butterknife.OnClick;

public class NoticeModifyActivity extends BaseActivity {


    @BindView(R2.id.notice_modify_back)
    CircleImageView noticeModifyBack;
    @BindView(R2.id.notice_modify_title)
    TextView noticeModifyTitle;
    @BindView(R2.id.notice_modify_finish)
    TextView noticeModifyFinish;
    @BindView(R2.id.notice_modify_theme)
    EditText noticeModifyTheme;
    @BindView(R2.id.notice_modify_content)
    EditText noticeModifyContent;
    @BindView(R2.id.notice_modify_teachername)
    TextView noticeModifyTeachername;

    //通过intent获取从NoticeActivity中获取当前项的数据，并初始化给noticeModifyTheme和noticeModifyTeachername
    @Override
    protected void initData() {
        Intent intent = getIntent();
        String[] befores = intent.getStringArrayExtra("Before");
        noticeModifyTheme.setText(befores[0]);
        noticeModifyTeachername.setText(befores[1]);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_notice_modify;
    }

    //是否退出当前页面的标识
    private static boolean EXIT = true;
    //handler的what标识
    private static final int WHAT_RESET_BACK = 1;
    //用于2S后改变Tag EXIT的属性
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WHAT_RESET_BACK:
                    EXIT = true;//复原
                    //                    Toast.makeText(NoticeAddActivity.this,"EXIT :"+EXIT,Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    //设置notice_modify_back以及notice_modify_finish的点击事件监听
    //notice_modify_back：退出当前页面，如果有数据一般不给予退出操作，需要在2S内点击两次才响应退出操作
    //notice_modify_finish：结束addNotice操作，通过intent携带数据，在NoticeActivity中onActivityResult中取得
    @OnClick({R2.id.notice_modify_back, R2.id.notice_modify_finish})
    public void onClick(View v) {
        boolean themeEmptyTag = TextUtils.isEmpty(noticeModifyTheme.getText().toString());
        switch (v.getId()) {
            case R2.id.notice_modify_back:
                //                Toast.makeText(NoticeAddActivity.this,"EXIT :"+ EXIT,Toast.LENGTH_SHORT).show();
                if (!themeEmptyTag && EXIT) {
                    Toast.makeText(NoticeModifyActivity.this, "亲，您的标题还有内容，再按一次退出", Toast.LENGTH_SHORT).show();
                    EXIT = false;
                    handler.sendEmptyMessageDelayed(WHAT_RESET_BACK, 2000);
                    //                    Toast.makeText(NoticeAddActivity.this,"EXIT :"+EXIT,Toast.LENGTH_SHORT).show();
                } else if (!EXIT) {
                    EXIT = true;
                    finish();
                } else {
                    finish();
                }
                break;
            case R2.id.notice_modify_finish:
                if (themeEmptyTag) {
                    Toast.makeText(NoticeModifyActivity.this, "亲，标题不能为空！", Toast.LENGTH_SHORT).show();
                } else {
                    int resultCode = 2;
                    String[] context = new String[3];
                    context[0] = noticeModifyTeachername.getText().toString();
                    context[1] = noticeModifyTheme.getText().toString();
                    context[2] = noticeModifyContent.getText().toString();
                    Intent intent = new Intent(); //同调用者一样 需要一个意图 把数据封装起来
                    intent.putExtra("Context", context);
                    setResult(resultCode, intent);
                    finish();
                }
                break;
        }
    }
}
