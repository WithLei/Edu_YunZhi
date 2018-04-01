package com.android.renly.edu_yunzhi.UI;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.renly.edu_yunzhi.R;
import com.android.renly.edu_yunzhi.Utils.UIUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public abstract class LoadingPage extends FrameLayout {
    //记录4种不同的显示状态
    private static final int STATE_LADING = 1;
    private static final int STATE_ERROR = 2;
    private static final int STATE_EMPTY = 3;
    private static final int STATE_SUCCESS = 4;


    //当前状态
    private int state_current = STATE_LADING;

    //加载4种不同的界面
    private View loadingView;
    private View errorView;
    private View emptyView;
    private View successView;
    private LayoutParams params;

    private ResultState resultState = null;

    public LoadingPage(Context context){
        this(context,null);
    }

    public LoadingPage(Context context, AttributeSet attrs){
        this(context,attrs,0);
    }

    public LoadingPage(Context context, AttributeSet attrs,int defStyleAttr){
        super(context,attrs,defStyleAttr);

        init();
    }

    private void init(){
        //视图与布局的关联\

        params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if(loadingView == null){
            loadingView = UIUtils.getView(R.layout.page_loading);
            addView(loadingView,params);
        }
        if(errorView == null){
            errorView = UIUtils.getView(R.layout.page_error);
            addView(errorView,params);
        }
        if(emptyView == null){
            emptyView = UIUtils.getView(R.layout.page_empty);
            addView(emptyView,params);
        }

        //加载布局的显示

        showSafePage();

    }

    private void showSafePage(){
        //需要主线程执行
        UIUtils.runOnUiThread(new Runnable(){
            @Override
            public void run() {
                showPage();
            }
        });
    }

    private void showPage() {
        //需要主线程执行
        loadingView.setVisibility(state_current == STATE_LADING ? View.VISIBLE : View.GONE);
        errorView.setVisibility(state_current == STATE_ERROR ? View.VISIBLE : View.GONE);
        emptyView.setVisibility(state_current == STATE_EMPTY ? View.VISIBLE : View.GONE);

        if(successView == null){
            successView = UIUtils.getView(layoutID());
            addView(successView,params);
        }
        successView.setVisibility(state_current == STATE_SUCCESS ? View.VISIBLE : View.GONE);
    }

    public void show(){
        String url = url();
        if (TextUtils.isEmpty(url)) {
            resultState = ResultState.SUCCESS;
            resultState.setContent("");
            return;
        }

        UIUtils.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AsyncHttpClient client = new AsyncHttpClient();
                client.get(url(), params(), new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String content) {
                        if (TextUtils.isEmpty(content)) {// "" or null
//                    state_current = STATE_EMPTY;
                            resultState = ResultState.EMPTY;
                            resultState.setContent("");
                        } else {
//                    state_current = STATE_SUCCESS;
                            resultState = ResultState.SUCCESS;
                            resultState.setContent(content);
                        }

//                showSafePage();
                        loadPage();
                    }

                    @Override
                    public void onFailure(Throwable error, String content) {
//                state_current = STATE_ERROR;
                        resultState = ResultState.ERROR;
                        resultState.setContent("");

//                showSafePage();
                        loadPage();

                    }
                });
            }
        }, 2000);
    }

    private void loadPage() {
        switch (resultState){
            case ERROR:
                state_current = STATE_ERROR;
                break;
            case EMPTY:
                state_current = STATE_EMPTY;
                break;
            case SUCCESS:
                state_current = STATE_SUCCESS;
                break;
        }
        showSafePage();

        if(state_current == STATE_SUCCESS){
            onSuccess(resultState,successView);
        }
    }
    //内部类：保存联网状态以及返回的数据
    public enum ResultState{
        ERROR(2),EMPTY(3),SUCCESS(4);

        private int state;
        private String content;

        public String getContent(){
            return content;
        }

        public void setContent(String content){
            this.content = content;
        }

        ResultState(int state){
            this.state = state;
        }
    }

    protected abstract RequestParams params();

    protected abstract String url();

    protected abstract void onSuccess(ResultState resultState,View successView);
    public abstract int layoutID();


}
