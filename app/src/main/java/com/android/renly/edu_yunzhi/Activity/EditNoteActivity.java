package com.android.renly.edu_yunzhi.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.R;
import com.android.renly.edu_yunzhi.Utils.ButtonDialogView;
import com.android.renly.edu_yunzhi.Utils.JsonParser;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

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
    @BindView(R.id.et_note)
    EditText etNote;
    @BindView(R.id.tv_editnote_time)
    TextView tvEditnoteTime;
    @BindView(R.id.iv_notepic)
    ImageView ivNotepic;

    private Unbinder unbinder;

    // 语音听写UI
    private RecognizerDialog mIatDialog;
    // 用HashMap存储听写结果
    private HashMap<String, String> mIatResults = new LinkedHashMap<String, String>();

    // 引擎类型
    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private SharedPreferences mSharedPreferences;

    private boolean mTranslateEnable = false;
    int ret = 0; // 函数调用返回值

    @Override
    protected void initData() {
        etNote.setSelection(etNote.length());
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        mIatDialog = new RecognizerDialog(EditNoteActivity.this, mInitListener);
        if (mIatDialog == null)
            Log.e("print", "mIatDialog == null");

        mSharedPreferences = getSharedPreferences("com.iflytek.setting",
                Activity.MODE_PRIVATE);
        if (mSharedPreferences == null)
            Log.e("print", "mSharedPreferences == null");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_editnote;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SpeechUtility.createUtility(EditNoteActivity.this, SpeechConstant.APPID + "=5b5ed6bd");
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
                // 移动数据分析，收集开始听写事件
                FlowerCollector.onEvent(EditNoteActivity.this, "iat_recognize");

                mIatResults.clear();
                boolean isShowDialog = mSharedPreferences.getBoolean(
                        "iat_show", true);
                if (isShowDialog) {
                    // 显示听写对话框
                    mIatDialog.setListener(mRecognizerDialogListener);
                    mIatDialog.show();
                    Toast.makeText(this, "请开始说话", Toast.LENGTH_SHORT).show();
                } else {
                    if (ret != ErrorCode.SUCCESS) {
                        Toast.makeText(this, "听写失败,错误码：" + ret, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "请开始说话", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.iv_editnote_pic:
                Toast.makeText(this, "拍照输入", Toast.LENGTH_SHORT).show();
                View v = View.inflate(this,R.layout.item_note,null);
                ButtonDialogView dialogView = new ButtonDialogView(EditNoteActivity.this,v,true,true);
                dialogView.show();
                break;
        }
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d("tag", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(EditNoteActivity.this, "初始化失败，错误码：" + code, Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            if (mTranslateEnable) {
                printTransResult(results);
            } else {
                if (!isLast)
                    printResult(results);
            }

        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            if (mTranslateEnable && error.getErrorCode() == 14002) {
                Toast.makeText(EditNoteActivity.this, error.getPlainDescription(true) + "\n请确认是否已开通翻译功能", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(EditNoteActivity.this, error.getPlainDescription(true), Toast.LENGTH_SHORT).show();
            }
        }

    };

    private void printTransResult(RecognizerResult results) {
        String trans = JsonParser.parseTransResult(results.getResultString(), "dst");
        String oris = JsonParser.parseTransResult(results.getResultString(), "src");

        if (TextUtils.isEmpty(trans) || TextUtils.isEmpty(oris)) {
            Toast.makeText(this, "解析结果失败，请确认是否已开通翻译功能。", Toast.LENGTH_SHORT).show();
        } else {
            String str = etNote.getText().toString();
            etNote.setText(str + "原始语言:\n" + oris + "\n目标语言:\n" + trans);
        }

    }

    private void printResult(RecognizerResult results) {
        String text = JsonParser.parseIatResult(results.getResultString());

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mIatResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : mIatResults.keySet()) {
            resultBuffer.append(mIatResults.get(key));
        }

        etNote.setText(etNote.getText() + resultBuffer.toString());
        etNote.setSelection(etNote.length());
    }

    @Override
    protected void onResume() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onResume(EditNoteActivity.this);
        FlowerCollector.onPageStart("tag");
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        tvEditnoteTime.setText(dateFormat.format(now));
        super.onResume();
    }

    @Override
    protected void onPause() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onPageEnd("tag");
        FlowerCollector.onPause(EditNoteActivity.this);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
