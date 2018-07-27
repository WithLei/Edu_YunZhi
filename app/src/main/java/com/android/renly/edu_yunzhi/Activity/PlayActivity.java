package com.android.renly.edu_yunzhi.Activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.renly.edu_yunzhi.Common.AppNetConfig;
import com.android.renly.edu_yunzhi.R;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLiveBase;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class PlayActivity extends Activity {
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
    @BindView(R.id.loadingImageView)
    ImageView mLoadingView;
    private TXCloudVideoView mView;
    private TXLivePlayer mLivePlayer;


    private String roomName;
    private String playUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playroom);
        ButterKnife.bind(this);

        initView();
        testCode();
        createPlayer();
        startPlay();
        adjustView();
    }

    ITXLivePlayListener mListener = new ITXLivePlayListener() {
        @Override
        public void onPlayEvent(int event, Bundle bundle) {
            Log.e("bundle", "bundle == " + bundle.toString() + "\n" + "event == " + event);
            if (event == TXLiveConstants.PLAY_EVT_PLAY_BEGIN) {
                stopLoadingAnimation();
            } else if (event == TXLiveConstants.PLAY_ERR_NET_DISCONNECT || event == TXLiveConstants.PLAY_EVT_PLAY_END) {
                stopPlay();
            } else if (event == TXLiveConstants.PLAY_EVT_PLAY_LOADING){
                startLoadingAnimation();
            } else if (event == TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME) {
                stopLoadingAnimation();
            }
        }

        @Override
        public void onNetStatus(Bundle bundle) {
        }
    };

    private void startLoadingAnimation() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.VISIBLE);
            ((AnimationDrawable) mLoadingView.getDrawable()).start();
        }
    }

    private void stopLoadingAnimation() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
            ((AnimationDrawable) mLoadingView.getDrawable()).stop();
        }
    }

    private void stopPlay(){
        if (mLivePlayer != null) {
            mLivePlayer.stopRecord();
            mLivePlayer.setPlayListener(null);
            mLivePlayer.stopPlay(true);
        }
    }

    private void initView() {
        backLl.setVisibility(View.VISIBLE);
        titlebar.setVisibility(View.GONE);
        Intent intent = getIntent();
        roomName = intent.getStringExtra("RoomName");
        playUrl = intent.getStringExtra("PlayUrl");
        titleTv.setText(roomName);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLivePlayer.stopPlay(true); // true 代表清除最后一帧画面
        mView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        pausePlayer();
    }

    @Override
    protected void onResume() {
        super.onResume();
        resumePlayer();
    }

    private void resumePlayer() {
        // 继续
        mLivePlayer.resume();
    }

    private void pausePlayer() {
        // 暂停
        mLivePlayer.pause();
    }

    private void adjustView() {
        /**
         * 画面调整
         * view：大小和位置
         *      如需修改画面的大小及位置，直接调整 step1 中添加的 “video_view” 控件的大小和位置即可。
         *
         * setRenderMode：铺满or适应
         *      可选值	                        含义
         *      RENDER_MODE_FULL_FILL_SCREEN	将图像等比例铺满整个屏幕，多余部分裁剪掉，此模式下画面不会留黑边，但可能因为部分区域被裁剪而显示不全。
         *      RENDER_MODE_ADJUST_RESOLUTION	将图像等比例缩放，适配最长边，缩放后的宽和高都不会超过显示区域，居中显示，画面可能会留有黑边。
         *
         * setRenderRotation：              画面旋转
         *      可选值	                        含义
         *      RENDER_ROTATION_PORTRAIT	    正常播放（Home 键在画面正下方）
         *      RENDER_ROTATION_LANDSCAPE	    画面顺时针旋转 270 度（Home 键在画面正左方）
         */

        // 设置填充模式
        mLivePlayer.setRenderMode(TXLiveConstants.RENDER_MODE_FULL_FILL_SCREEN);
        // 设置画面渲染方向
        mLivePlayer.setRenderRotation(TXLiveConstants.RENDER_ROTATION_PORTRAIT);
    }

    private void startPlay() {
        /**
         * 启动播放
         *
         * 可选值	                枚举值	  含义
         * PLAY_TYPE_LIVE_RTMP	      0	      传入的 URL 为 RTMP 直播地址
         * PLAY_TYPE_LIVE_FLV	      1	      传入的 URL 为 FLV 直播地址
         * PLAY_TYPE_LIVE_RTMP_ACC	  5	      低延迟链路地址（仅适合于连麦场景）
         * PLAY_TYPE_VOD_HLS	      3	      传入的 URL 为 HLS（m3u8）播放地址
         */
        String flvUrl = playUrl;
        mLivePlayer.startPlay(flvUrl, TXLivePlayer.PLAY_TYPE_LIVE_FLV); //推荐 FLV
    }

    private void createPlayer() {
        /**
         * 创建 Player
         *
         * 视频云 SDK 中的 TXLivePlayer 模块负责实现直播播放功能，
         * 并使用 setPlayerView 接口将这它与我们刚刚添加到界面上的 video_view 控件进行关联。
         */
        //mPlayerView 即 step1 中添加的界面 view
        mView = (TXCloudVideoView) findViewById(R.id.video_view);

        //创建 player 对象
        mLivePlayer = new TXLivePlayer(this);

        //关键 player 对象与界面 view
        mLivePlayer.setPlayerView(mView);
        mLivePlayer.setPlayListener(mListener);
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
