package com.android.renly.edu_yunzhi.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.renly.edu_yunzhi.Common.AppNetConfig;
import com.android.renly.edu_yunzhi.R;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PusherActivity extends Activity {
    @BindView(R.id.video_view)
    TXCloudVideoView videoView;
    @BindView(R.id.back_tv)
    TextView backTv;
    @BindView(R.id.back_ll)
    LinearLayout backLl;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.btnNew)
    Button btnNew;
    @BindView(R.id.btnScan)
    Button btnScan;
    @BindView(R.id.roomid)
    EditText roomid;
    @BindView(R.id.video_frame)
    FrameLayout videoFrame;
    @BindView(R.id.titlebar)
    RelativeLayout titlebar;
    private TXLivePusher mLivePusher;
    private TXLivePushConfig mLivePushConfig;

    private String roomName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playroom);
        ButterKnife.bind(this);

        initView();
        testCode();
        initLivePusher();
        startPusher();
//        startAudioPusher();
        setVideoQuality();

    }

    private void initView() {
        backLl.setVisibility(View.VISIBLE);
        titlebar.setVisibility(View.GONE);
        Intent intent = getIntent();
        roomName = intent.getStringExtra("RoomName");
        titleTv.setText(roomName);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopRtmpPublish();
    }

    //结束推流，注意做好清理工作
    public void stopRtmpPublish() {
        mLivePusher.stopCameraPreview(true); //停止摄像头预览
        mLivePusher.stopPusher();            //停止推流
        mLivePusher.setPushListener(null);   //解绑 listener
    }

    private static final int STANDARD = 1;
    private static final int HIGH = 2;
    private static final int SUPER = 3;

    private void setVideoQuality() {
        /**
         * setVideoQuality(quality, adjustBitrate, adjustResolution)
         *
         * quality:
         *      STANDARD
         *      HIGH √
         *      SUPER √
         *      MAIN_PUBLISHER
         *      SUB_PUBLISHER
         *      VIDEO_CHAT
         *
         * adjustBitrate:
         *      YES:流畅优先（网络较差时 - 模糊或马赛克）
         *      NO:画质优先（网络较差时 - 卡顿或跳转） √
         *
         * adjustResolution
         *      YES:动态分辨率（适合视频通话场景）
         *      NO:固定分辨率 √
         */
        mLivePusher.setVideoQuality(HIGH, false, false);
    }

    private void startAudioPusher() {
        /**
         * 纯音频推流
         */
        // 只有在推流启动前设置启动纯音频推流才会生效，推流过程中设置不会生效。
        mLivePushConfig.enablePureAudioPush(true);   // true 为启动纯音频推流，而默认值是 false；
        mLivePusher.setConfig(mLivePushConfig);      // 重新设置 config

        String rtmpUrl = AppNetConfig.PosterUrl;
        mLivePusher.startPusher(rtmpUrl);
    }

    private void startPusher() {
        /**
         * 启动推流
         *
         * startPusher 的作用是告诉 SDK 音视频流要推到哪个推流URL上去。
         * startCameraPreview 则是将界面元素和 Pusher 对象关联起来，从而能够将手机摄像头采集到的画面渲染到屏幕上。
         */
        String rtmpUrl = AppNetConfig.PosterUrl;
        mLivePusher.startPusher(rtmpUrl);

        TXCloudVideoView mCaptureView = (TXCloudVideoView) findViewById(R.id.video_view);
        mLivePusher.startCameraPreview(mCaptureView);
    }

    private void initLivePusher() {
        /**
         * 创建 Pusher 对象
         *
         * LivePushConfig 对象，该对象的用途是决定 LivePush 推流时各个环节的配置参数，
         * 比如推流用多大的分辨率、每秒钟要多少帧画面（FPS）以及Gop（表示多少秒一个I帧）等等。
         */
        mLivePusher = new TXLivePusher(this);
        mLivePushConfig = new TXLivePushConfig();
        mLivePusher.setConfig(mLivePushConfig);
    }

    private void testCode() {
        /**
         * 测试sdk是否连接
         */
        String sdkver = TXLiveBase.getSDKVersionStr();
        Log.d("liteavsdk", "liteav sdk version is : " + sdkver);
    }

    @OnClick({R.id.back_ll, R.id.btnNew, R.id.btnScan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.back_ll:
                finish();
                break;
            case R.id.btnNew:
                break;
            case R.id.btnScan:
                break;
        }
    }
}
