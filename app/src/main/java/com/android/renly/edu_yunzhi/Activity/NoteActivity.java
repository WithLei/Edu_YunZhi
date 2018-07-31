package com.android.renly.edu_yunzhi.Activity;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.android.renly.edu_yunzhi.Adapter.NoteAdapter;
import com.android.renly.edu_yunzhi.Bean.Note;
import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class NoteActivity extends BaseActivity {
    @BindView(R.id.note_img)
    ImageView noteImg;
    @BindView(R.id.rl_note_title)
    RelativeLayout rlNoteTitle;
    @BindView(R.id.spinner_note)
    Spinner spinnerNote;
    @BindView(R.id.iv_note_addnote)
    ImageView ivNoteAddnote;
    @BindView(R.id.iv_note_refresh)
    ImageView ivNoteRefresh;
    @BindView(R.id.gv_note)
    GridView gvNote;

    private Unbinder unbinder;

    private List<Note> noteList;
    private NoteAdapter adapter;

    @Override
    protected void initData() {
        initList();
    }

    private void initList() {
        noteList = new ArrayList<>();
        Note note = new Note("今天天气真好", "2018/12/31 11:48", false);
        Note note1 = new Note("lalalala", "2017/1/11 16:48", false);
        Note note2 = new Note("test", "2018/7/31 16:29", false);

        noteList.add(note);
        noteList.add(note1);
        noteList.add(note2);

        if (noteList.size() > 0) {
            handler.sendEmptyMessage(INIT_LIST);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_note;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        handler.removeCallbacksAndMessages(null);
    }

    @OnClick({R.id.note_img, R.id.iv_note_addnote, R.id.iv_note_refresh})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.note_img:
                finish();
                break;
            case R.id.iv_note_addnote:
                startActivity(new Intent(NoteActivity.this, EditNoteActivity.class));
                break;
            case R.id.iv_note_refresh:
                ((AnimationDrawable) ivNoteRefresh.getDrawable()).start();
                handler.sendEmptyMessageDelayed(STOP_REFRESH, 4000);
                break;
        }
    }

    private static final int STOP_REFRESH = 1;
    private static final int INIT_LIST = 2;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case STOP_REFRESH:
                    ((AnimationDrawable) ivNoteRefresh.getDrawable()).stop();
                    break;
                case INIT_LIST:
                    adapter = new NoteAdapter(NoteActivity.this, noteList);
                    gvNote.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    break;
            }
        }
    };
}
