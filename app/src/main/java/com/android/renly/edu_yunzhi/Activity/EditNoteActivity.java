package com.android.renly.edu_yunzhi.Activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class EditNoteActivity extends BaseActivity {
    @BindView(R.id.ll_editnote_back)
    LinearLayout llEditnoteBack;
    @BindView(R.id.iv_editnote_share)
    ImageView ivEditnoteShare;
    @BindView(R.id.rl_editnote_title)
    RelativeLayout rlEditnoteTitle;
    @BindView(R.id.iv_editnote_delete)
    ImageView ivEditnoteDelete;
    @BindView(R.id.iv_editnote_record)
    ImageView ivEditnoteRecord;
    @BindView(R.id.iv_editnote_pic)
    ImageView ivEditnotePic;

    private Unbinder unbinder;

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_editnote;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
    }

    @OnClick({R.id.ll_editnote_back, R.id.iv_editnote_share, R.id.iv_editnote_delete, R.id.iv_editnote_record, R.id.iv_editnote_pic})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_editnote_back:
                finish();
                break;
            case R.id.iv_editnote_share:
                new AlertDialog.Builder(this)
                        .setTitle("分享")
                        .setCancelable(true)
                        .show();
                break;
            case R.id.iv_editnote_delete:
                new AlertDialog.Builder(this)
                        .setMessage("确定删除吗")
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Toast.makeText(EditNoteActivity.this, "已删除", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();
                break;
            case R.id.iv_editnote_record:
                Toast.makeText(this, "语音输入", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_editnote_pic:
                Toast.makeText(this, "拍照输入", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
