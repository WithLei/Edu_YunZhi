package com.android.renly.edu_yunzhi.Activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;

import com.android.renly.edu_yunzhi.Common.BaseActivity;
import com.android.renly.edu_yunzhi.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PKActivity extends BaseActivity {
    @BindView(R.id.iv_pk_a)
    ImageView ivPkA;
    @BindView(R.id.iv_pk_b)
    ImageView ivPkB;
    @BindView(R.id.iv_pk_c)
    ImageView ivPkC;
    @BindView(R.id.iv_pk_d)
    ImageView ivPkD;
    @BindView(R.id.iv_pk_e)
    ImageView ivPkE;
    @BindView(R.id.iv_pk_f)
    ImageView ivPkF;
    @BindView(R.id.iv_pk_g)
    ImageView ivPkG;

    @Override
    protected void initData() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_pk;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_pk_a, R.id.iv_pk_b, R.id.iv_pk_c, R.id.iv_pk_d, R.id.iv_pk_e, R.id.iv_pk_f, R.id.iv_pk_g})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_pk_a:
                ivPkA.setVisibility(View.GONE);
                ivPkB.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_pk_b:
                ivPkB.setVisibility(View.GONE);
                ivPkC.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_pk_c:
                ivPkC.setVisibility(View.GONE);
                ivPkD.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_pk_d:
                ivPkD.setVisibility(View.GONE);
                ivPkF.setVisibility(View.VISIBLE);
                handler.sendEmptyMessageDelayed(ANIMATIONDFE,2000);
                break;
            case R.id.iv_pk_e:
                ivPkE.setVisibility(View.GONE);
                ivPkF.setVisibility(View.VISIBLE);
                handler.sendEmptyMessageDelayed(ANIMATIONEFG,2000);
                break;
            case R.id.iv_pk_g:
                finish();
                break;
        }
    }
    private static final int ANIMATIONDFE = 1;
    private static final int ANIMATIONEFG = 2;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case ANIMATIONDFE:
                    ivPkF.setVisibility(View.GONE);
                    ivPkE.setVisibility(View.VISIBLE);
                    break;
                case ANIMATIONEFG:
                    ivPkF.setVisibility(View.GONE);
                    ivPkG.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
}
