package com.android.renly.edu_yunzhi_teacher.Activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi_teacher.Common.BaseActivity;
import com.android.renly.edu_yunzhi_teacher.R;
import com.android.renly.edu_yunzhi_teacher.R2;
import com.android.renly.edu_yunzhi_teacher.UI.CorrectView;

import butterknife.BindView;

public class StudentHomeworkCorrectActivity extends BaseActivity implements View.OnClickListener {

    @BindView(R2.id.tv_studentcorrect_content)
    TextView tvStudentcorrectContent;
    @BindView(R2.id.tv_studentcorrect_answer)
    TextView tvStudentcorrectAnswer;
    @BindView(R2.id.btn_last)
    Button btnLast;
    @BindView(R2.id.btn_redo)
    Button btnRedo;
    @BindView(R2.id.btn_recover)
    Button btnRecover;
    @BindView(R2.id.btn_savesd)
    Button btnSavesd;
    @BindView(R2.id.btn_paintcolor)
    Button btnPaintcolor;
    @BindView(R2.id.btn_paintsize)
    Button btnPaintsize;
    @BindView(R2.id.btn_paintstyle)
    Button btnPaintstyle;
    @BindView(R2.id.fl_boardcontainer)
    FrameLayout flBoardcontainer;

    private CorrectView tuyaView;

    @Override
    protected void initData() {
        //通过intent获取上个页面传来的数据并显示
        Intent intent = getIntent();
        String[] extra = intent.getStringArrayExtra("content&answer");
        String content = extra[0];
        String answer = extra[1];
        tvStudentcorrectContent.setText(content);
        tvStudentcorrectAnswer.setText(answer);
        //虽然此时获取的是屏幕宽高，但是我们可以通过控制framlayout来实现控制涂鸦板大小
        //初始化CorrectView的参数
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        int screenWidth = defaultDisplay.getWidth();
        int screenHeight = defaultDisplay.getHeight();
        tuyaView = new CorrectView(this, screenWidth, screenHeight);
        flBoardcontainer.addView(tuyaView);
        tuyaView.requestFocus();
        tuyaView.selectPaintSize(1);
        //设置监听
        btnLast.setOnClickListener(this);
        btnPaintcolor.setOnClickListener(this);
        btnPaintsize.setOnClickListener(this);
        btnPaintstyle.setOnClickListener(this);
        btnRecover.setOnClickListener(this);
        btnRedo.setOnClickListener(this);
        btnSavesd.setOnClickListener(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_student_homework_correct;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R2.id.btn_last://撤销
                tuyaView.undo();
                break;
            case R2.id.btn_redo://重做
                tuyaView.redo();
                break;
            case R2.id.btn_recover://恢复
                tuyaView.recover();
                break;
            case R2.id.btn_savesd://保存
                finish();
//                tuyaView.saveToSDCard();
                //以后可以通过saveToSDCard()返回一个bitmap或者Url对象实现对批改成果的存储以及与后台进行同步操作
                Toast.makeText(StudentHomeworkCorrectActivity.this,"图片保存成功",Toast.LENGTH_SHORT).show();
                break;
            case R2.id.btn_paintcolor:
                //                sb_size.setVisibility(View.GONE);
                //选择color
                showPaintColorDialog(v);
                break;
            case R2.id.btn_paintsize:
                //                sb_size.setVisibility(View.VISIBLE);
                //选择size
                showPaintSizeDialog(v);
                break;
            case R2.id.btn_paintstyle:
                //                sb_size.setVisibility(View.GONE);
                //选择style
                showMoreDialog(v);
                break;
        }
    }

    private int select_paint_color_index = 0;
    private int select_paint_style_index = 0;

    private int select_paint_size_index = 0;

    //弹出画笔颜色选择对话框
    public void showPaintColorDialog(View parent) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("选择画笔颜色：");
        alertDialogBuilder.setSingleChoiceItems(R.array.paintcolor, select_paint_color_index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                select_paint_color_index = which;
                tuyaView.selectPaintColor(which);
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }


    //弹出画笔大小选项对话框
    public void showPaintSizeDialog(View parent) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("选择画笔大小：");
        alertDialogBuilder.setSingleChoiceItems(R.array.paintsize, select_paint_size_index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                select_paint_size_index = which;
                tuyaView.selectPaintSize(which);
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }

    //弹出选择画笔或橡皮擦的对话框
    public void showMoreDialog(View parent) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("选择画笔或橡皮擦：");
        alertDialogBuilder.setSingleChoiceItems(R.array.paintstyle, select_paint_style_index, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                select_paint_style_index = which;
                tuyaView.selectPaintStyle(which);
                dialog.dismiss();
            }
        });
        alertDialogBuilder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialogBuilder.create().show();
    }
}
