package com.android.renly.edu_yunzhi.Activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
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

    private static final int NONE = 0;
    private static final int PHOTO_GRAPH = 1;// 拍照
    private static final int PHOTO_ZOOM = 2; // 缩放
    private static final int PHOTO_RESOULT = 3;// 结果
    private static final String IMAGE_UNSPECIFIED = "image/*";
    private String change_path = "/edu_YunZhi";

    ButtonDialogView dialogView;


    @Override
    protected void initData() {
        initDialog();
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

        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
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
                dialogView.show();
                break;
        }
    }

    private void initDialog() {
        View v = View.inflate(this,R.layout.view_dialog,null);
        Button btn1 = v.findViewById(R.id.dialog_btn1);
        Button btn2 = v.findViewById(R.id.dialog_btn2);
        Button btn3 = v.findViewById(R.id.dialog_btn3);
        btn1.setText("拍摄照片");
        btn2.setText("从手机相册选择");
        btn3.setText("取消");
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //从拍照获取图片
                String filePath = Environment.getExternalStorageDirectory() + change_path;
                File localFile = new File(filePath);
                if (!localFile.exists()) {
                    localFile.mkdir();
                }
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory() + change_path
                        , "temp.jpg")));
                startActivityForResult(intent, PHOTO_GRAPH);
                dialogView.hide();
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //从相册获取图片
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_UNSPECIFIED);
                startActivityForResult(intent, PHOTO_ZOOM);
                dialogView.hide();
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogView.hide();
            }
        });
        dialogView = new ButtonDialogView(EditNoteActivity.this,v,true,true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case NONE:
                return;
            case PHOTO_GRAPH:
                // 设置文件保存路径
                String filePath = Environment.getExternalStorageDirectory() + change_path;
                File localFile = new File(filePath);
                if (!localFile.exists()) {
                    localFile.mkdir();
                }
                File picture = new File(Environment.getExternalStorageDirectory() + change_path
                        + "/temp.jpg");
                startPhotoZoom(Uri.fromFile(picture));
                break;
            case PHOTO_ZOOM:
                if (data == null)
                    return;
                startPhotoZoom(data.getData());
                break;
            case PHOTO_RESOULT:
                if (data == null)
                    return;
                Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap photo = extras.getParcelable("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);// (0-100)压缩文件
                    byte[] bts = stream.toByteArray();

                    //此处可以把Bitmap保存到sd卡中
                    ivNotepic.setImageBitmap(photo); //把图片显示在ImageView控件上
                    ivNotepic.setVisibility(View.VISIBLE);
                    handler.sendEmptyMessageDelayed(HIDE_PIC,2000);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 收缩图片
     *
     * @param uri
     */
    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_RESOULT);
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

    private static final int HIDE_PIC = 1;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case HIDE_PIC:
                    ivNotepic.setVisibility(View.GONE);
                    break;
            }
        }
    };
}
