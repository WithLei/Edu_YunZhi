package com.android.renly.edu_yunzhi_teacher.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;

import com.android.renly.edu_yunzhi_teacher.R;
import com.android.renly.edu_yunzhi_teacher.UI.CorrectView;

public class TestActivity extends Activity implements View.OnClickListener {

    private FrameLayout frameLayout;
    private Button btn_undo;
    private Button btn_redo;
    private Button btn_save;
    private Button btn_recover;
    private CorrectView tuyaView;//自定义涂鸦板
    private Button btn_paintcolor;
    private Button btn_paintsize;
    private Button btn_paintstyle;
//    private SeekBar sb_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_test);
        initView();
        initData();
        initListener();
    }

    private void initView() {
        frameLayout = (FrameLayout) findViewById(R.id.fl_boardcontainer);
        btn_undo = (Button) findViewById(R.id.btn_last);
        btn_redo = (Button) findViewById(R.id.btn_redo);
        btn_save = (Button) findViewById(R.id.btn_savesd);
        btn_recover = (Button) findViewById(R.id.btn_recover);
        btn_paintcolor = (Button) findViewById(R.id.btn_paintcolor);
        btn_paintsize = (Button) findViewById(R.id.btn_paintsize);
        btn_paintstyle = (Button) findViewById(R.id.btn_paintstyle);
//        sb_size = (SeekBar) findViewById(R.id.sb_size);
    }

    private void initData() {
        //虽然此时获取的是屏幕宽高，但是我们可以通过控制framlayout来实现控制涂鸦板大小
        Display defaultDisplay = getWindowManager().getDefaultDisplay();
        int screenWidth = defaultDisplay.getWidth();
        int screenHeight = defaultDisplay.getHeight();
        tuyaView = new CorrectView(this, screenWidth, screenHeight);
        frameLayout.addView(tuyaView);
        tuyaView.requestFocus();
        tuyaView.selectPaintSize(1);
//        tuyaView.selectPaintSize(sb_size.getProgress());
    }

    private void initListener() {
        btn_undo.setOnClickListener(this);
        btn_redo.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_recover.setOnClickListener(this);
        btn_paintcolor.setOnClickListener(this);
        btn_paintsize.setOnClickListener(this);
        btn_paintstyle.setOnClickListener(this);
//        sb_size.setOnSeekBarChangeListener(new MySeekChangeListener());
    }

//    class MySeekChangeListener implements SeekBar.OnSeekBarChangeListener {
//        @Override
//        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//            tuyaView.selectPaintSize(seekBar.getProgress());
//            Toast.makeText(TestActivity.this, "当前画笔尺寸为" + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onStartTrackingTouch(SeekBar seekBar) {
//            tuyaView.selectPaintSize(seekBar.getProgress());
//            Toast.makeText(TestActivity.this, "当前画笔尺寸为" + seekBar.getProgress(), Toast.LENGTH_SHORT).show();
//        }
//
//        @Override
//        public void onStopTrackingTouch(SeekBar seekBar) {}
//    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_last://撤销
                tuyaView.undo();
                break;
            case R.id.btn_redo://重做
                tuyaView.redo();
                break;
            case R.id.btn_recover://恢
                tuyaView.recover();
                break;
            case R.id.btn_savesd://保存
                tuyaView.saveToSDCard();
                break;
            case R.id.btn_paintcolor:
//                sb_size.setVisibility(View.GONE);
                showPaintColorDialog(v);
                break;
            case R.id.btn_paintsize:
//                sb_size.setVisibility(View.VISIBLE);
                showPaintSizeDialog(v);
                break;
            case R.id.btn_paintstyle:
//                sb_size.setVisibility(View.GONE);
                showMoreDialog(v);
                break;
        }
    }

    private int select_paint_color_index = 0;
    private int select_paint_style_index = 0;

    private int select_paint_size_index = 0;

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
